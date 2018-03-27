package com.master.weibomaster.Util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import com.master.weibomaster.App
import com.master.weibomaster.Model.DownStatu
import com.master.weibomaster.Rx.MyObserver
import com.master.weibomaster.Rx.RxSchedulers
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import java.io.File


/**
 * Created by 不听话的好孩子 on 2018/1/23.
 */
class DownLoadUtils {
    class DownObserver : ObservableOnSubscribe<DownStatu> {
        val id: Long
        val wait: Long
        var downStatu: DownStatu?=null

        constructor(id: Long, wait: Long) {
            this.id = id
            this.wait = wait
            downStatu= DownStatu(id=id)
        }

        constructor(id: Long) : this(id, 1000)

        override fun subscribe(e: ObservableEmitter<DownStatu>) {
            val query = DownloadManager.Query()
            query.setFilterById(id)
            var cursor = downloadManager.query(query)

            if (cursor == null && !e.isDisposed) {
                e.onError(Throwable("获取不到记录"))
                return
            }
            if (!cursor!!.moveToFirst() && !e.isDisposed) {
                e.onError(Throwable("获取不到记录"))
                return
            }
            var state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            while (state != DownloadManager.STATUS_FAILED && !e.isDisposed) {
                val current = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                downStatu!!.total = total
                downStatu!!.current = current
                val statetemp=if (state == DownloadManager.STATUS_SUCCESSFUL) 1 else 0
                downStatu!!.state = statetemp
                if (total != -1L)
                    e.onNext(downStatu!!)
                if (state == DownloadManager.STATUS_SUCCESSFUL) {
                    break
                }
                cursor.close()
                Thread.sleep(wait)
                cursor = downloadManager.query(query)
                cursor.moveToFirst()
                state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }
            if (state == DownloadManager.STATUS_SUCCESSFUL) {
                downStatu!!.state = 1
                e.onComplete()
            } else {
                e.onError(Throwable("下载失败"))
            }
            cursor.close()
        }
    }

    companion object {
        private val downloadManager: DownloadManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { App.app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

        fun download(url: String, downloadFile: File, showNotify: Boolean, notifyName: String, notifyDescription: String): Long {
            val uriStr = Uri.parse(url)
            val request = DownloadManager.Request(uriStr)
            request.setAllowedOverRoaming(false)

            val isshow = if (showNotify) DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED else DownloadManager.Request.VISIBILITY_HIDDEN
            request.setNotificationVisibility(isshow)
            request.setTitle(notifyName)
            request.setDescription(notifyDescription)
            request.setVisibleInDownloadsUi(true)

            PrefUtil.put(url,Environment.getExternalStoragePublicDirectory(downloadFile.absolutePath).absolutePath)
            request.setDestinationInExternalPublicDir(downloadFile.parent, downloadFile.name)
            val enqueue = downloadManager.enqueue(request)


            val intentFilter = IntentFilter()
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            App.app.registerReceiver(receiver, intentFilter)
            return enqueue
        }

        fun download(url: String, downloadFile: File, shownotify: Boolean): Long {
            return download(url, downloadFile, shownotify, downloadFile.name, "正在下载中...")
        }

        fun download(url: String, shownotify: Boolean): Long {
            var name = ""
            val lastIndexOf = url.lastIndexOf("/")
            if (lastIndexOf == -1)
                name += System.currentTimeMillis()
            else
                name += url.substring(lastIndexOf + 1)
            return download(url, File(Environment.DIRECTORY_DOWNLOADS, name), shownotify)
        }

        fun download(url: String): Long {
            return download(url, true)
        }

        fun remove(vararg ids: Long) {
            downloadManager.remove(*ids)
        }

        fun downloadWithProgress(path: String, observer: MyObserver<DownStatu>) {
            val download = DownLoadUtils.download(path)
            val file=PrefUtil.get(path,"")
            Observable.create(DownLoadUtils.DownObserver(download))
                    .observeOn(Schedulers.io())
                    .compose(RxSchedulers.compose())
                    .doOnNext { observer.onNext(it) }
                    .filter {
                        it.path= file as String
                        it.state == 1 }
                    .subscribe(observer)
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            }

        }

    }

}