package com.master.weibomaster.Rx.Net;

import com.master.weibomaster.Rx.MyObserver;
import com.master.weibomaster.Services.DownLoadService;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 不听话的好孩子 on 2018/3/13.
 */

public class ProgressDLUtils {
    public static interface DownloadInterface {
        @GET
        @Streaming
        Observable<ResponseBody> download(@Url String url);
    }

    public static void addListenerUrl(String url) {
        if (!RetrofitHttpManger.progrssUrls.contains(url)) {
            RetrofitHttpManger.progrssUrls.add(url);
        }
    }

    public static void removeListenerUrl(String url) {
        if (RetrofitHttpManger.progrssUrls.contains(url)) {
            RetrofitHttpManger.progrssUrls.remove(url);
        }
    }

    public Observable<MyObserver.Progress> download(String url, String fileName) {
        return DownLoadService.Companion.download(url, fileName);

    }

    public static Observable<MyObserver.Progress> download(final String url, final File file) {
        return DownLoadService.Companion.download(url, file);
    }

    public static Observable<MyObserver.Progress> download(String url) {
        return DownLoadService.Companion.download(url);
    }
}

