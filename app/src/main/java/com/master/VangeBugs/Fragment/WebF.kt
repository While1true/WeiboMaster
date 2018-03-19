package com.master.VangeBugs.Fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.R
import com.master.VangeBugs.Util.ActivityUtils
import com.master.VangeBugs.Util.FragmentUtils
import com.master.VangeBugs.Widgets.MyPhotoView
import com.nestrefreshlib.Adpater.Base.MyCallBack
import com.tencent.smtt.sdk.WebView
import coms.pacs.pacs.BaseComponent.BaseFragment
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.pop
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.photoview.*
import kotlinx.android.synthetic.main.webview.*

/**
 * Created by 不听话的好孩子 on 2018/2/28.
 */
class WebF : BaseFragment() {

    private var bug: Bug? = null
    var callback: MyCallBack<Boolean>? = null


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
                        width:100%;
                        height:auto;
                    }
                    </style>"""
        webview.loadData(css + "\n" + bug.content, "text/html; charset=UTF-8", null)
        val piclistenerJS = """javascript:(function(){
                                var objs = document.getElementsByTagName("img");
                                for(var i=0;i<objs.length;i++){
                                    objs[i].onclick=function(){
                                         window.imgCallback.call(this.src);
                                    }
                                }
                                })()"""
        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(ImgCallback(context), "imgCallback")
        webview.webViewClient = object : com.tencent.smtt.sdk.WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                p0?.loadUrl(piclistenerJS)
            }
        }


    }

    class ImgCallback constructor(val context: Context) {
        @JavascriptInterface
        open fun call(url: String) {
            FragmentUtils.showAddFragment(ActivityUtils.getTopActivity(), ImageF().setUrl(url))

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
            Glide.with(context).load(imgurl).listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    (model.mtoString()+"加载图片失败").toast()
                    stop()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    return false
                }

            }).into(photoview)
            photoview.setOnPhotoTapListener { _, _, _ ->
                    stop()
            }
            photoview.setCloseListener(object : MyPhotoView.CloseListener{
                override fun close() {
                        stop()
                }

            })
        }

    }
}