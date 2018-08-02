package com.tttlive.education.ui.room.socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tttlive.education.base.MyApplication;
import com.tttlive.education.constant.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;


public class NetworkDiagnosisService extends Service {
    private static final String LOG_TAG = "NetworkDiagnosisService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startDiagnosis(this);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 执行以下操作并写到日志：
     * 1. 检查是否有网络连接；
     * 2. 检查能否ping通WS和API的服务器；
     * 3. 检查能否ping通QQ或百度的服务器。
     */
    public void startDiagnosis(Context context){
        Log.i(LOG_TAG, "------------- Start Diagnosis ---------------");
        Log.i(LOG_TAG, "isNetworkConnected? %s"+ Networks.isNetworkConnected(context));
        Log.i(LOG_TAG, "Network type = %s"+ Networks.getNetworkType(context));

        pingWithLog(Constant.MAIN_HOST_FOR_PING, 6);
        pingWithLog(Constant.MAIN_HOST_FOR_PING, 6);
        pingWithLog("baidu.com", 3);
        pingWithLog("qq.com", 3);

        Log.i(LOG_TAG, "REPEAT : isNetworkConnected? %s"+ Networks.isNetworkConnected(context));
        Log.i(LOG_TAG, "REPEAT : Network type = %s"+Networks.getNetworkType(context));
        Log.i(LOG_TAG, "------------- Finish Diagnosis ---------------");

        stopSelf();
    }

    private void pingWithLog(String host, int count){
        Log.i(LOG_TAG, "Ping host %s"+ host);
        if (Networks.isNetworkConnected(MyApplication.getContext())){
            ping(host, count);
        }
        else{
            Log.e(LOG_TAG, "Network disconnected, cancel ping!");
        }
    }

    private void ping(String host, int count) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(String.format(Locale.US,
                    "/system/bin/ping -c %d %s", count, host));

//            process = new ProcessBuilder()
//                    .command("/system/bin/ping -c 10 " + host)
//                    .redirectErrorStream(true)
//                    .start();
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String string;
            while ((string = reader.readLine()) != null) {
                Log.i(LOG_TAG, string);
            }
            in.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to ping host " + host, e);
        } finally {
            if (process != null)
                process.destroy();
        }
    }

    public class ServiceBinder extends Binder {
        public NetworkDiagnosisService getService() {
            return NetworkDiagnosisService.this;
        }
    }

}
