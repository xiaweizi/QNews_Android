package com.xiaweizi.qnews.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaweizi.qnews.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.net
 * 类名：    QClitent
 * 创建者：  夏韦子
 * 创建日期： 2017/2/23
 * 创建时间： 19:38
 */

public class QClitent {

    private static QClitent mQClient;

    private OkHttpClient.Builder mBuilder;

    private QClitent() {
        initSetting();
    }

    public static QClitent getInstance() {
        if (mQClient == null) {
            synchronized (QClitent.class) {
                if (mQClient == null) {
                    mQClient = new QClitent();
                }
            }
        }
        return mQClient;
    }

    /**
     * 创建相应的服务接口
     */
    public <T> T create(Class<T> service, String baseUrl) {
        checkNotNull(service, "service is null");
        checkNotNull(baseUrl, "baseUrl is null");

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(service);
    }

    private <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    private void initSetting() {

        //初始化OkHttp
        mBuilder = new OkHttpClient.Builder()
                .connectTimeout(9, TimeUnit.SECONDS)    //设置连接超时 9s
                .readTimeout(10, TimeUnit.SECONDS);      //设置读取超时 10s

        if (BuildConfig.DEBUG) { // 判断是否为debug
            // 如果为 debug 模式，则添加日志拦截器
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mBuilder.addInterceptor(interceptor);
        }

    }
}
