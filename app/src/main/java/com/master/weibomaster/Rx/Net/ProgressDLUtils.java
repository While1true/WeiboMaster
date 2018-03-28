package com.master.weibomaster.Rx.Net;

import com.master.weibomaster.Rx.MyObserver;
import com.master.weibomaster.Rx.RxSchedulers;
import com.master.weibomaster.Rx.Utils.RxBus;
import com.master.weibomaster.Rx.Utils.RxLifeUtils;
import com.master.weibomaster.Util.ActivityUtils;
import com.master.weibomaster.Util.FileUtils;
import com.master.weibomaster.Util.MemoryUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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
    public static void addListenerUrl(String url){
        if(!RetrofitHttpManger.progrssUrls.contains(url)){
            RetrofitHttpManger.progrssUrls.add(url) ;
        }
    }
    public static void removeListenerUrl(String url){
        if(RetrofitHttpManger.progrssUrls.contains(url)){
            RetrofitHttpManger.progrssUrls.remove(url);
        }
    }

    public static void download(String url, String fileName, final MyObserver<File> callback) {
        File file = new File(MemoryUtils.FILE + fileName);
        download(url, file, callback);
    }

    public static void download(final String url, final File file, final MyObserver<File> callback) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        addListenerUrl(url);
        Disposable progressDisposable = RxBus.getDefault().toObservable(MyObserver.Progress.class)
                .filter(new Predicate<MyObserver.Progress>() {
                    @Override
                    public boolean test(MyObserver.Progress progress) throws Exception {
                        return progress.getUrl().equals(url);
                    }
                })
                .compose(RxSchedulers.<MyObserver.Progress>compose())
                .subscribe(new Consumer<MyObserver.Progress>() {
                    @Override
                    public void accept(MyObserver.Progress progress) throws Exception {
                        callback.onProgress(progress);
                    }
                });
        RxLifeUtils.getInstance().add(ActivityUtils.getTopActivity(), progressDisposable);
        RetrofitHttpManger.create(DownloadInterface.class)
                .download(url)
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        FileUtils.writeFile(responseBody.byteStream(), file);
                        return file;
                    }
                })
                .compose(RxSchedulers.<File>compose())
                .subscribe(callback);

    }

    public static void download(String url, final MyObserver<File> callback) {
        int i = url.lastIndexOf("/");
        String fileName = url.substring(i);
        File file = new File(MemoryUtils.FILE + fileName);
        download(url, file, callback);
    }
}

