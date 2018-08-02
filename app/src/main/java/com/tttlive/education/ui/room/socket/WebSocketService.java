package com.tttlive.education.ui.room.socket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tttlive.education.base.MyApplication;
import com.tttlive.education.constant.Constant;
import com.tttlive.education.ui.room.bean.BindWebsocket;
import com.tttlive.education.ui.room.bean.WebSocketResBean;
import com.tttlive.education.ui.room.bean.WebsocketImBean;
import com.tttlive.education.util.CustomToast;
import com.tttlive.education.util.SPTools;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class WebSocketService extends Service {


    private static final String LOG_TAG = WebSocketService.class.getSimpleName();

    private static final int SELF_CHECK_INTERVAL_SECONDS = 20;
    private URI uri;
    private Context mContext;

    /**
     * 连续尝试连接次数，超过限制时触发手动诊断和上报过程。
     */
    private int connectionAttemptCount = 0;

    /**
     * 最高允许的连续失败次数。
     * 达到这个次数将立即触发上报操作。
     */
    private static final int ATTEMPT_TOLERANCE = 10;


    public static Intent createIntent(Context context) {
        return new Intent(context, WebSocketService.class);
    }

    public WebSocketClient webSocket;
    private String pong;

    /**
     * 有时候会因为一些数据传输异常【如重复登录房间、或数据出现错误等】导致被服务器强行断开连接。
     * 为了避免这种情况下用户毫无察觉地不可用，在WebSocket初始化后创建一个定时自检的Service。
     *
     * 为什么不能单纯依靠OnClose方法来完成重连？
     * 因为OnClose方法里的重连可能连接失败！失败后就再也没有OnClose了！
     */
    private Subscription selfCheckSubscription;

    /**
     * 标记是否正在连接中，用于自检服务避免重复发起连接。
     */
    private boolean isAttemptConnecting;

    /**
     * 标记是否需要自动重连。
     */
    private boolean shouldAutoReconnect;


    /**
     * 由于断线后重连需要重新登录而不希望退出房间，所以这里缓存最新的登录请求。
     * 注意,即使在发起登录的时候没有登录成功，也要保存这个请求。
     * 下面两个时间点需要清除这个缓存的请求：1）发起登出请求的时候；2）准备关闭Service的时候。
     */
    private BindWebsocket cachedLoginRequest;
    private ArrayList<WsListener> activeListener = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "----- onCreate -----");
        initSocketWrapper("InitialConnect", true);
        startSelfCheckService();
    }

    public void startInitWebSocket() {
        initSocketWrapper("onError");
        startSelfCheckService();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Use this to force restart service
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 准备关闭Service。这个方法调用会关闭所有自检和Pong服务，调用者应当尽快解除对Service的连接并调用stopService。
     */
    public void prepareShutdown() {
        Log.i(LOG_TAG, "----- prepareShutdown -----");
        stopSelfCheckService();
        stopPongDaemonService();
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.close();
        }
        if (webSocket != null && !webSocket.isOpen()){
            webSocket = null;
        }
    }

    private boolean checkSocketAvailable() {
        if (webSocket == null || (!webSocket.isOpen())) {
            Log.e(LOG_TAG, "WebSocket not ready, ignore this operation!");
            return false;
        }
        return true;
    }


    /**
     * Register listener for specified type.
     *
     * @param listener see {@link WsListener}
     */
    public void registerListener(@NonNull WsListener listener) {
        if (activeListener == null){
            activeListener = new ArrayList<>();
        }
        activeListener.clear();
        activeListener.add(listener);
    }

    /**
     * Remove all listeners.
     */
    public void removeAllListeners() {
        Log.i(LOG_TAG, "Removing all listeners, count=%d. "+ activeListener.size());
        activeListener.clear();
    }

    /**
     * Send request to server.
     *
     * @param request see {@link }
     */
    public void sendRequest(String request) {
        if (!checkSocketAvailable()) {
            return;
        }
        Log.e(LOG_TAG ,"发送给服务器-："+request);
        webSocket.send(request);
    }

    private void initSocketWrapper(String forReason){
        initSocketWrapper(forReason, false);
    }

    private void initSocketWrapper(final String forReason, final boolean isFirstConnect){
        Observable.just(forReason)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        //如果正在连接则屏蔽该次消息
                        if (isAttemptConnecting){
                            return Boolean.FALSE;
                        }
                        return Boolean.TRUE;
                    }
                })
                //强制跳转主线程做通知操作
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if ( (webSocket == null) && (!isFirstConnect) && (!isAttemptConnecting)){
                            Log.e(LOG_TAG,"服务器连接中断，正在重连……    " + connectionAttemptCount);
                            if (uri == null){
                                prepareShutdown();
                            }
                        }
                    }
                })
                //跳转IO线程做操作
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(LOG_TAG, "Reconnect WebSocket from %s."+ forReason);
                        if (webSocket == null && isFirstConnect && !isAttemptConnecting){
                            initSocket();
                        }else {
                            Log.e(LOG_TAG,"正在重连……    " );
                            initSocket();
                        }
                    }
                });
    }

    //启动服务
    private void initSocket() {
        //0.12.0 开始在onClose里也会自动发起重连，因此将attemptConnecting状态的维护放在这个地方。  
        if (isAttemptConnecting) {
            return;
        }
        isAttemptConnecting = true;
        Observable.create(new Observable.OnSubscribe<WebSocket>() {
            @Override
            public void call(final Subscriber<? super WebSocket> subscriber) {
                connectionAttemptCount++;
                Log.d(LOG_TAG, "Connection attempt:%d"+ connectionAttemptCount);
                isAttemptConnecting =false;
                try {
                    if (Constant.exitRoom){
                        if (!TextUtils.isEmpty(Constant.SOCKET_URL_DEFAULT)) {
                            uri = new URI(Constant.SOCKET_URL_DEFAULT);
                        }
                    }else {
                        if (!TextUtils.isEmpty(Constant.SOCKET_URL)){
                            uri = new URI(Constant.SOCKET_URL);
                        }
                    }
                    Log.e(LOG_TAG , "SOCKET_URL  : " + uri);
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
                if (null == webSocket) {
                    webSocket = new WebSocketClient(uri) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.e(LOG_TAG, "onOpen: " + uri);
                            subscriber.onNext(webSocket);
                        }
                        @Override
                        public void onMessage(String s) {
                            Log.e(LOG_TAG, "onMessage: " + s);
                            dispatchMessage(s);
                        }
                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.e(LOG_TAG, "onClose: ");
                            initSocketWrapper("onClose");

                        }
                        @Override
                        public void onError(Exception e) {
                            Log.e(LOG_TAG, "onError: ");

                        }
                    };
                    webSocket.connect();
                }else {
                    Log.e(LOG_TAG, "initSocket webSocket  : " + webSocket);
                    subscriber.onError(new ConnectException
                            ("Cannot connect ws service!"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebSocket>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(LOG_TAG, "WebSocket init failed!");
                        e.printStackTrace();
                        //判断是否需要执行诊断服务
                        if (connectionAttemptCount <= ATTEMPT_TOLERANCE){
                            Log.e(LOG_TAG, "Continuous connection error occurred for %d times!"+
                                    connectionAttemptCount);
                            if (webSocket != null && webSocket.isOpen()) {
                                webSocket.close();
                            }
                            if (webSocket != null && !webSocket.isOpen()){
                                webSocket = null;
                            }
                            initSocketWrapper("onError");
                            startSelfCheckService();
                        }else {
                            if (uri == null){
                                Log.e(LOG_TAG , " uri == " + uri);
                                prepareShutdown();
                            }else {
                                Log.e(LOG_TAG , " uri == 111  " + webSocket);
                                if (webSocket != null && !webSocket.isOpen()){
                                    webSocket = null;
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(WebSocket webSocket) {
                        handler.sendEmptyMessageDelayed(0,0);

                        //在此处判断用户是否在房间内
                        if (Constant.USER_ISROOM){
                             //重连成功后再次发送加入房间
                            String reconnection = SPTools.getInstance(MyApplication.getContext()).getString(Constant.LIVE_ROOM_ID , "");
                             sendRequest(reconnection);
                        }
                        //重置标记
                        connectionAttemptCount = 0;
                        Log.e(LOG_TAG,"服务器连接成功");

                    }
                });
    }

    //提示信息
    private void notifyUiWsStatus(String msg) {
        Observable.just(msg)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        CustomToast.makeCustomText(MyApplication.getContext(), s, Toast.LENGTH_SHORT)
                                .show();
                        sendRequest(new Gson().toJson(s));
                    }
                });
    }

    /**
     * 启动自检服务。自检服务会立即执行，之后按周期执行。
     */
    private void startSelfCheckService() {
//        //为安全起见先解除之前的订阅
//        stopSelfCheckService();
        //订阅新的自检服务
        selfCheckSubscription = Observable.interval(SELF_CHECK_INTERVAL_SECONDS, SELF_CHECK_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        if (!shouldAutoReconnect){
                            Log.w(LOG_TAG, "Auto reconnect has been disabled, maybe kicked?");
                        }
                        return shouldAutoReconnect;
                    }
                })
                .map(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return (webSocket != null) && (webSocket.isOpen());
                    }
                })
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.i(LOG_TAG, "Self check task has been scheduled per %d seconds."+
                                SELF_CHECK_INTERVAL_SECONDS);
                        shouldAutoReconnect = true;
                        Log.i(LOG_TAG, "Auto reconnect feature has been enabled.");
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean websocketAlive) {//
                        if (websocketAlive) {
                            Log.v( LOG_TAG, "WebSocket self check: is alive.");
                            return;
                        }
                        initSocketWrapper("SelfCheckService");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e( LOG_TAG, "Error while executing self check!"+ throwable);
                    }
                });
    }

    private void stopSelfCheckService() {
        if (selfCheckSubscription != null && (!selfCheckSubscription.isUnsubscribed())) {
            selfCheckSubscription.unsubscribe();
            Log.i(LOG_TAG, "Self check service has been unSubscribed.");
        }
    }

    /**
     * 定时
     * 10秒心跳包
     * {"eType": 3}
     */

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handler.removeMessages(0);
                    // app的功能逻辑处理
                    startPongDaemonService();
                    // 再次发出msg，循环更新
                    handler.sendEmptyMessageDelayed(0, 10000);
                    break;

                case 1:
                    // 直接移除，定时器停止
                    handler.removeMessages(0);
                    break;

                default:
                    break;
            }
        }
    };


    //发送心跳包
    private void startPongDaemonService() {
        Gson gson = new Gson();
        final WsPongRequest wpr = new WsPongRequest();
        wpr.seteType(3);
        pong = gson.toJson(wpr);
        if (webSocket!=null && webSocket.isOpen()){
            webSocket.send(pong);
        }else{
            Log.e(LOG_TAG , "startPongDaemonService");
            if (webSocket !=null){
                Log.e(LOG_TAG, "WebSocket startPongDaemonService." +webSocket+"--webSocket.isOpen()---"+webSocket.isOpen());
                if (!webSocket.isOpen()){
                    webSocket.close();
                    webSocket = null;
                }
            }
            initSocketWrapper("startPongDaemonService");
        }
    }

    /**
     * 停止定时器
     */
    private void stopPongDaemonService() {
        handler.sendEmptyMessageDelayed(1,0);
    }

    /**
     * 接收到的信息
     * @param msg
     */
    private void dispatchMessage(String msg) {
        Gson gson=new Gson();
        String type = null;

        WebSocketResBean wskrb = gson.fromJson(msg , WebSocketResBean.class);
        if (wskrb.getEType() ==1 ){
            Log.e(LOG_TAG , "集群地址 : " + gson.toJson(wskrb));
            Constant.Websocket_url = wskrb.getMProtocolMsg().getMIpAddrMsg().getSDomain();
            Constant.SOCKET_URL_IP = "ws://" + Constant.Websocket_url + ":"  + Constant.Websocket_port;
            Constant.SOCKET_URL = Constant.SOCKET_URL_IP;
            Log.e(LOG_TAG , "暂停服务器 重连集群地址 " + Constant.SOCKET_URL);
            prepareShutdown();
        }

        WsPongRequest wpr = gson.fromJson(msg , WsPongRequest.class);
        if (wpr.geteType() == 3){
            return;
        }
        WebsocketImBean data = gson.fromJson(msg,WebsocketImBean.class);
        String sData = data.getMChatTransMsg().getSData();
        if(sData==null){
            Log.e(LOG_TAG ,"发送成功");
            return;
        }
        if (data.getEType() == 0) {
            Log.v(LOG_TAG , "" + data.getEType());
            return;
        }
        try {
            JSONObject json = new JSONObject(sData);
            Log.e(LOG_TAG ,"接收服务器返回json"+json);
            type = json.optString(Constant.FIELD_TYPE);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Message is not well-formed data!");
        }
        if (TextUtils.isEmpty(type)) {
            Log.e(LOG_TAG, "Cannot parse type from msg!");
            return;
        }
        Log.e( LOG_TAG, "Dispatching msg type : %s"+ type);
        notifyListener(sData);
    }

    /**
     * Notify active listener to handle data, if no listener matches, just discard.
     */
    @SuppressWarnings("unchecked")
    private <T> void notifyListener(String msg) {

        Observable.just(msg)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        WsListener listener =  activeListener.get(0);
                        if (listener == null) {
                            Log.e(LOG_TAG, "No listener handle type %s, discard this."+ s);
                            return;
                        }
                        Log.d(LOG_TAG, "Msg entity:%s."+ s);
                        listener.handleData(s);
                    }
                });
    }

    public class ServiceBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }


}
