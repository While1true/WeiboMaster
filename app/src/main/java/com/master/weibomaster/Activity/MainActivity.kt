package com.master.weibomaster.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Base.BaseActivity
import com.master.weibomaster.R
import com.master.weibomaster.Rx.MyObserver
import com.master.weibomaster.Rx.RxSchedulers
import com.master.weibomaster.Util.DeviceUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import android.content.pm.ActivityInfo
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.Menu
import android.view.MenuItem
import com.master.weibomaster.Model.REQUEST_CODE_CHOOSE
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Services.DownLoadService
import com.master.weibomaster.Util.FileUtils
import com.master.weibomaster.Util.GlideV4ImageEngine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import coms.pacs.pacs.Utils.log
import coms.pacs.pacs.Utils.pop
import coms.pacs.pacs.Utils.toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class MainActivity : BaseActivity() {

    override fun initView() {
        RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET).subscribe { }
        var text = """
            I leave uncultivated today,
            was precisely yesterday perishes tomorrow which person of the body implored

             """.trimIndent()
        tv.text = text
        val split = text.split(" ")
        val size = split.size
        Observable.interval(1000, 800, TimeUnit.MILLISECONDS)
                .take(size.toLong())
                .compose(RxSchedulers.compose())
                .subscribe(object : MyObserver<Long>(this) {
                    override fun onNext(t: Long) {
                        super.onNext(t)

                        val indexOf = text.indexOf(split[t.toInt() % size])
                        val textAppearanceSpan = TextAppearanceSpan(this@MainActivity, R.style.textappearance)
                        var spann = SpannableString(text)
                        spann.setSpan(textAppearanceSpan, indexOf, indexOf + split[t.toInt() % size].length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        tv.text = spann
                    }
                })
        next.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
            finish()
        }
        share.setOnClickListener {
            Matisse.from(this@MainActivity)
                    .choose(MimeType.allOf())
                    .countable(true)
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(GlideV4ImageEngine())
                    .forResult(REQUEST_CODE_CHOOSE)
        }
    }

    override fun loadData() {
        val url = "http://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h164_1.33.1.23_fe680b.apk"
//        DownLoadService.download(url)
//                .compose(RxSchedulers.compose())
//                .subscribe(object : MyObserver<MyObserver.Progress>(this) {
//                    override fun onNext(progress: Progress) {
//                        super.onNext(progress)
//                        val text = "已下载：${progress.current / 1024 / 1024} M----速度${String.format("%.2f", progress.speed / 1024 / 1024)} M/s"
//                        log(text)
//                    }
//                })
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun needTitle(): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val mSelected = Matisse.obtainResult(data)
            val uri = mSelected[0]
            val uri2File = FileUtils.Uri2File(this, uri)

            val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), uri2File)
            val body = MultipartBody.Part.createFormData("pattern", uri2File.name, requestBody)
            ApiImpl.apiImpl.uploadPattern(DeviceUtils.deviceID, uri2File.name, body)
                    .subscribe(object : DataObserver<String>(this) {
                        override fun OnNEXT(bean: String?) {
                            bean.toast()
                        }

                    })
        }
    }

}
