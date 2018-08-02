package com.tttlive.education.net;

import android.support.multidex.BuildConfig;


import com.tttlive.education.constant.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Iverson on 2016/12/23 下午11:10
 * 此类用于：
 */

public class NetManager {

    private static final int CONNECT_TIME_OUT = 60;
    private static final int WRITE_TIME_OUT = 60;
    private static final int READ_TIME_OUT = 60;
    private Retrofit mRetrofit;

    public static NetManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final NetManager INSTANCE = new NetManager();
    }

    private NetManager() {
    }

    public <S> S create(Class<S> service) {
//        if("LoginApi".equals(service.getSimpleName())){
//            return create(service, Constant.LoginBaseUrl);
//        }else {
        return create(service, Constant.BaseUrl);
//        }
    }

    /**
     * 生成网络请求链接
     *
     * @param service
     * @param url
     * @param <S>
     * @return
     */
    public <S> S create(Class<S> service, String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder clientBuilder = okHttpClient.newBuilder()
                //添加网络通用请求信息, see http://stackoverflow.com/a/33667739
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
//                            LoginInfo loginInfo = DataManager.getInstance().getLoginInfo();
                        Request request = chain.request().newBuilder()
//                                    .addHeader("token", loginInfo != null ? loginInfo.getToken() : "")
//                                    .addHeader("device", Builder.MODEL)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addNetworkInterceptor(logging);
        }

        okHttpClient = clientBuilder.build();
//
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return mRetrofit.create(service);
    }
}
