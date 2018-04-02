package com.master.weibomaster.Util.download;

import android.app.IntentService;
import android.content.Intent;


import com.master.rxlib.Rx.MyObserver;
import com.master.rxlib.Rx.Net.RetrofitHttpManger;
import com.master.rxlib.Rx.Utils.RxBus;
import com.master.rxlib.Rx.Utils.RxLifeUtils;
import com.master.weibomaster.App;
import com.master.weibomaster.Util.FileUtils;
import com.master.weibomaster.Util.RxManager;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.ResponseBody;

import static com.master.weibomaster.Util.download.ProgressDLUtils.addListenerUrl;


/**
 * Created by 不听话的好孩子 on 2018/4/2.
 */

public class DownLoadService extends IntentService {
    private static final Map<String,Disposable> runningMap = new LinkedHashMap<>();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownLoadService(String name) {
        super(name);
    }
    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        File file = (File) intent.getSerializableExtra("file");

        down(url, file);
    }

    private void down(String url, final File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        addListenerUrl(url);

        Disposable subscribe = RxManager.create(ProgressDLUtils.DownloadInterface.class)
                .download(url)
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        FileUtils.writeFile(responseBody.byteStream(), file);
                        return file;
                    }
                })
                .subscribe();
        RxLifeUtils.getInstance().add(this, subscribe);
    }


    public static Observable download(String url) {
        return download(url, getFileByUrl(url));
    }

    public static File getFileByUrl(String url) {
        return ProgressDLUtils.getFileByUrl(url);
    }

    public static Observable download(String url,String fileName) {
        File file = new File(ProgressDLUtils.FILE + fileName);
        return download(url, file);
    }

    public static Observable download(final String url,File file) {
        Disposable disposable = runningMap.get(url);
        if(disposable == null || disposable.isDisposed()) {
            Intent intent = new Intent( App.app,DownLoadService.class);
            intent.putExtra("url", url);
            intent.putExtra("file",file);
            App.app.startService(intent);
        }

        Observable var10000 = RxBus.getDefault().toObservable(MyObserver.Progress.class).filter(new Predicate<MyObserver.Progress>() {
            @Override
            public boolean test(MyObserver.Progress progress) throws Exception {
                return progress.getUrl().equals(url);
            }
        });
        return var10000;
    }

    public static void stop(String url) {
        Disposable disposable = runningMap.get(url);
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
           runningMap.remove(url);
        }

    }
}
