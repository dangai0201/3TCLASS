package com.tttlive.education.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tttlive.education.ui.Interface.RoomInterface;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Administrator on 2018/7/5/0005.
 *
 * 检测丢包率
 */
public class PingUtil {
    private static final String TAG_CLASS = PingUtil.class.getSimpleName();
    private Activity mContext;
    private RoomInterface mRoomInterface;

    //丢包率
    public static final int PING_HTTP_LINE_LOST = 0x01;
    //网络延时
    private static final int PING_HTTP_LINE_DELAY = 0x02;
    //网络断开
    private static final int PING_HTTP_CONNECTION_DIS = 0x03;



    Handler handler1 = null;
    Thread mThread = null;
//    private String count;
//    private String size;
//    private String time;
//    private String ip;
    private String countCmd;
    private String sizeCmd;
    private String timeCmd;
    private String ip_adress;
    private String result = "";
    private long delaytime;


    private String lost = "";// 丢包
    private String delay = "";// 延迟
    private String ping;

    public PingUtil(RoomInterface roomInterface, Activity activity) {
        this.mContext = activity;
        this.mRoomInterface = roomInterface;

    }


    /**
     * 测试ping 服务器
     */
    @SuppressLint("HandlerLeak")
    public void PingHttp(String count  ,String size , String time , String ip) {
        countCmd = " -c " + count + " ";
        sizeCmd = " -s " + size + " ";
        timeCmd = " -i " + time + " ";
        ip_adress = ip;

        delaytime = (long) Double.parseDouble("1");

        ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;

        Log.i(TAG_CLASS, "====MainThread====:" + Thread.currentThread().getId() + "ping " + ping);


        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10:
                        String resultmsg = (String) msg.obj;
                        Log.i(TAG_CLASS + "aa  ", "====handlerThread====:"
                                + Thread.currentThread().getId());
                        Log.i(TAG_CLASS + "aa  ", "====resultmsg====:" + msg.what);
                        Log.i(TAG_CLASS + "aa  ", "====resultmsg====:" + resultmsg);
                        break;
                    case PING_HTTP_LINE_LOST:
                        String resultLost = (String) msg.obj;
                        Log.i(TAG_CLASS + "11  ", "====resultmsg====:" + msg.what);
                        Log.i(TAG_CLASS + "11  ", "====resultmsg====:" + resultLost);
                        mRoomInterface.pingLineLost(resultLost);
                        break;
                    case PING_HTTP_LINE_DELAY:
                        String resultDelay = (String) msg.obj;
                        Log.i(TAG_CLASS + "12  ", "====resultmsg====:" + msg.what);
                        Log.i(TAG_CLASS + "12  ", "====resultmsg====:" + resultDelay);
                        mRoomInterface.pingLineDelay(resultDelay);
                        break;
                    case PING_HTTP_CONNECTION_DIS:
                        mRoomInterface.pingHttpDis();
                        break;
                }
            }
        };


        mThread = new Thread() {

            @Override
            public void run() {
                delay = "";
                lost = "";
                Process process = null;
                BufferedReader successReader = null;
                BufferedReader errorReader = null;

                DataOutputStream dos = null;


                try {
                    process = Runtime.getRuntime().exec(ping);
                    Log.i(TAG_CLASS, "====receive====:");

                    String command = "ping" + countCmd + timeCmd + sizeCmd
                            + ip_adress;
                    InputStream in = process.getInputStream();

                    OutputStream out = process.getOutputStream();
                    // success
                    successReader = new BufferedReader(
                            new InputStreamReader(in));
                    // error
                    errorReader = new BufferedReader(new InputStreamReader(
                            process.getErrorStream()));

                    String lineStr;

                    while ((lineStr = successReader.readLine()) != null) {
                        Log.i(TAG_CLASS, "====receive====:" + lineStr);
                        Message msg = handler1.obtainMessage();
                        msg.obj = lineStr + "\r\n";
                        msg.what = 10;
                        msg.sendToTarget();
                        result = result + lineStr + "\n";
                        if (lineStr.contains("packet loss")) {
                            Log.i(TAG_CLASS, "=====Message=====" + lineStr.toString());
                            int i = lineStr.indexOf("received");
                            int j = lineStr.indexOf("%");
                            Log.i(TAG_CLASS,
                                    "====丢包率====:"
                                            + lineStr.substring(i + 10, j + 1));//
                            lost = lineStr.substring(i + 10, j + 1);
                            Message msgLost = handler1.obtainMessage();
                            msgLost.obj = lost + "\r\n";
                            msgLost.what = PING_HTTP_LINE_LOST;
                            msgLost.sendToTarget();
                        }

                        if (lineStr.contains("avg")) {
                            int i = lineStr.indexOf("/", 20);
                            int j = lineStr.indexOf(".", i);
                            Log.i(TAG_CLASS,
                                    "====平均时延:===="
                                            + lineStr.substring(i + 1, j));
                            delay = lineStr.substring(i + 1, j);

                            Message msgDelay = handler1.obtainMessage();
                            msgDelay.obj = delay + "\r\n";
                            msgDelay.what = PING_HTTP_LINE_DELAY;
                            msgDelay.sendToTarget();

                        }
                        sleep(delaytime * 1000);
                    }

                    while ((lineStr = errorReader.readLine()) != null) {
                        Log.i(TAG_CLASS, "==error======" + lineStr);
                        Message msgDelay = handler1.obtainMessage();
                        msgDelay.obj = delay + "\r\n";
                        msgDelay.what = PING_HTTP_CONNECTION_DIS;
                        msgDelay.sendToTarget();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (dos != null) {
                            dos.close();
                        }
                        if (successReader != null) {
                            successReader.close();
                        }
                        if (errorReader != null) {
                            errorReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (process != null) {
                        process.destroy();


                    }


                }
            }

        };
        mThread.start();
    }

}
