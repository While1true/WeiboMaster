package com.master.weibomaster.Rx.Net;


import com.master.weibomaster.Util.K2JUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ck on 2017/7/31.
 */

public class RetrofitHttpManger {
    private static final int DEFAULT_CONNECT_TIMEOUT = 12;
    private static final int DEFAULT_READ_TIMEOUT = 12;
    private static final String BASEURL = "http://192.168.0.110:8090/masterWeiBo/";
    private Retrofit mRetrofit;

    static List<String> progrssUrls = new ArrayList<>(16);


    private RetrofitHttpManger() {
        OkHttpClient httpclient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        K2JUtils.log("HttpLoggingInterceptor", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))

                // 添加公共参数拦截器
                // 添加通用的Header
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder();
                        builder.addHeader("zh", "grid");
                        builder.addHeader("mm", "grid");
                        return chain.proceed(builder.build());
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        String url = originalResponse.request().url().url().toString();
                        if (progrssUrls.contains(url)) {
                            return originalResponse.newBuilder()
                                    .body(new ProgressDownloadBody(originalResponse.body(), url))
                                    .build();
                        } else {
                            return originalResponse;
                        }
                    }
                })
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(httpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASEURL)
                .build();

    }


    public static <T> T create(Class<T> service) {
        return SingleHolder.manger.mRetrofit.create(service);
    }

    private static class SingleHolder {
        private static RetrofitHttpManger manger = new RetrofitHttpManger();
    }
}

