package com.master.weibomaster.Services

import android.content.Intent
import android.app.IntentService
import android.support.annotation.Nullable
import com.master.weibomaster.App
import com.master.weibomaster.Rx.MyObserver
import com.master.weibomaster.Rx.Net.ProgressDLUtils
import com.master.weibomaster.Rx.Net.ProgressDLUtils.addListenerUrl
import com.master.weibomaster.Rx.Net.RetrofitHttpManger
import com.master.weibomaster.Rx.Utils.RxBus
import com.master.weibomaster.Rx.Utils.RxLifeUtils
import com.master.weibomaster.Util.FileUtils
import com.master.weibomaster.Util.MemoryUtils
import io.reactivex.Observable
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.io.File


/**
 * Created by 不听话的好孩子 on 2018/3/8.
 */


class DownLoadService : IntentService("") {

    override fun onHandleIntent(@Nullable intent: Intent) {
        val url = intent.getStringExtra("url")
        val file = intent.getSerializableExtra("file") as File

        down(url,file)
    }

    companion object {
        fun download(url: String):Observable<MyObserver.Progress> {
            val i = url.lastIndexOf(".")
            val fileName = url.substring(i)
            val file = File(MemoryUtils.FILE.toString() + fileName)
            return download(url, file)
        }

        fun download(url: String, fileName: String):Observable<MyObserver.Progress> {
            val file = File(MemoryUtils.FILE.toString() + fileName)
            return download(url, file)
        }

        fun download(url: String, file: File):Observable<MyObserver.Progress> {
            val intent = Intent(App.app, DownLoadService::class.java)
            intent.putExtra("url", url)
            intent.putExtra("file", file)
            App.app.startService(intent)

            return RxBus.getDefault().toObservable(MyObserver.Progress::class.java)
                    .filter { progress -> progress.file == url }
        }

        fun stop(url:String){
            RxLifeUtils.getInstance().remove(url)
        }

    }

    private fun down(url: String, file: File) {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        addListenerUrl(url)

        val subscribe = RetrofitHttpManger.create<ProgressDLUtils.DownloadInterface>(ProgressDLUtils.DownloadInterface::class.java)
                .download(url)
                .map(Function<ResponseBody, File> { responseBody ->
                    FileUtils.writeFile(responseBody, file)
                    file
                })
                .subscribe()
        RxLifeUtils.getInstance().add(this,subscribe)
        RxLifeUtils.getInstance().add(url,subscribe)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxLifeUtils.getInstance().remove(this)
    }

}
