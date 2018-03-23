package com.master.VangeBugs.Fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.master.VangeBugs.App
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.Model.DownStatu
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.MyObserver
import com.master.VangeBugs.Util.ActivityUtils
import com.master.VangeBugs.Util.DownLoadUtils
import com.master.VangeBugs.Util.FileUtils
import com.master.VangeBugs.Util.FragmentUtils
import com.master.VangeBugs.Widgets.MyPhotoView
import com.nestrefreshlib.Adpater.Base.MyCallBack
import com.tencent.smtt.sdk.WebView
import coms.pacs.pacs.BaseComponent.BaseFragment
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.photoview.*
import kotlinx.android.synthetic.main.webview.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 不听话的好孩子 on 2018/2/28.
 */
class WebF : BaseFragment() {

    private var bug: Bug? = null
    var callback: MyCallBack<Boolean>? = null
    val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")


    override fun getLayoutId() = R.layout.webview

    override fun init(savedInstanceState: Bundle?) {
        setTitle(bug!!.title)
        show(bug!!)
    }

    fun display(bug: Bug?): WebF {
        this.bug = bug
        return this
    }

    override fun needTitle(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        callback = null
    }

    private fun show(bug: Bug) {
        val css = """<style type="text/css">
                    img{
                        width : 100%;
                        height : auto;
                        border : 1px solid #999999;
                    }
                    p{
                        color:#3F51B5;
                    }
                    .boder{
                        margin : 5px;
                        padding : 6px;
                        border : 1px dashed #303F9F;
                    }
                    </style>"""
        val title = "<div class=\"boder\"><p><b>标题：</b>${bug.title}</p><p><b>测试人：</b>${bug.tester}</p><p><b>提交时间：</b> ${format.format(Date(bug.create_time))}</p></div>"
        var img = ""
        for (i in 0..300) {
            img += "<img src=\"https://avatars0.githubusercontent.com/u/20881342?s=400&u=29df528fe3acbeefd40021127e0f481b050d352a&v=4\" alt=${i}.jpg></img>"
        }

        webview.loadData(title + "\n" + css + "\n" + bug.content + img, "text/html; charset=UTF-8", null)

        val piclistenerJS = """javascript:(function(){
                                 var objs = document.getElementsByTagName("img");
                                 for(var i=0;i<objs.length;i++){
                                     objs[i].onclick=function(){
                                        window.imgCallback.call(this.src);
                                     };
                                 }
                                }
                             )()"""
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(ImgCallback(context), "imgCallback")
        webview.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                p0?.loadUrl(piclistenerJS)
            }
        }
        webview.setOnLongClickListener {
            val hitTestResult = webview.hitTestResult
            var type = hitTestResult.type
            var boleanx = false
            if (type == WebView.HitTestResult.IMAGE_TYPE) {
//                var imagurl = hitTestResult.extra
//                DownLoadUtils.downloadWithProgress(imagurl,object : MyObserver<DownStatu>(context){
//                    override fun onProgress(downStatu: DownStatu?) {
//                        super.onProgress(downStatu)
//                        if(downStatu?.total==downStatu?.current){
//                            FileUtils.send(context, File(downStatu?.path), "image/jpeg")
//                        }
//                    }
//                })
                var createBitmap: Bitmap? = null
                var canvas: Canvas?
                try {
                    var rect = Rect()
                    webview.getHitRect(rect)
                    createBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888)
                    canvas = Canvas(createBitmap)
                    canvas.translate(rect.left.toFloat(), rect.top.toFloat())
                    webview.draw(canvas)

                    val saveImageToGallery = FileUtils.saveImageToGallery(context, createBitmap, bug?.title + ".jpg")
                    FileUtils.send(context, saveImageToGallery, "image/jpeg")
                    boleanx = true
                } catch (e: OutOfMemoryError) {
                    "内存溢出".toast()
                    createBitmap?.recycle()
                }

            }
            boleanx
        }

    }

    class ImgCallback constructor(val context: Context) {
        @JavascriptInterface
        open fun call(url: String) {
            FragmentUtils.showAddFragment(ActivityUtils.getTopActivity(), ImageF().setUrl(url))

        }

        @JavascriptInterface
        open fun longCall(url: String) {
            url.toast()
        }
    }

    class ImageF : BaseFragment() {
        override fun getLayoutId() = R.layout.photoview
        var imgurl: String? = null

        fun setUrl(imgurl: String): ImageF {
            this.imgurl = imgurl
            return this
        }

        override fun init(savedInstanceState: Bundle?) {
            Glide.with(context).load(imgurl).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    (model.mtoString() + "加载图片失败").toast()
                    stop()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    return false
                }

            }).into(photoview)
            photoview.setOnPhotoTapListener { _, _, _ ->
                photoview.setScale(0.1f, true)
            }
            photoview.setCloseListener(object : MyPhotoView.CloseListener {
                override fun close() {
                    stop()
                }

            })
        }

    }
}