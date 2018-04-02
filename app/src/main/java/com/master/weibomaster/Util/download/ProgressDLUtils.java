package com.master.weibomaster.Util.download;



import com.master.rxlib.Rx.MyObserver;
import com.master.rxlib.Rx.Net.RetrofitHttpManger;
import com.master.weibomaster.App;
import com.master.weibomaster.Util.RxManager;

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
    public static final File FILE=new File(App.app.getFilesDir(), "file/");
    public static interface DownloadInterface {
        @GET
        @Streaming
        Observable<ResponseBody> download(@Url String url);
    }

    public static void addListenerUrl(String url) {
            RxManager.get().addDownloadUrlListener(url,FILE);
    }

    public static void removeListenerUrl(String url) {
        RxManager.get().removeDownloadListener(url);
    }

    public Observable<MyObserver.Progress> download(String url, String fileName) {
        return DownLoadService.download(url, fileName);

    }

    public static Observable<MyObserver.Progress> download(final String url, final File file) {
        return DownLoadService.download(url, file);
    }

    public static Observable<MyObserver.Progress> download(String url) {
        return DownLoadService.download(url);
    }

    public static File getFileByUrl(String url){
        int index = url.lastIndexOf("/");
        String name = url.substring(index);
        return new File(FILE+name);
    }
}

