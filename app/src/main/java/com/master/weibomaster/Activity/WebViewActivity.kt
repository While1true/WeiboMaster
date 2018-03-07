package com.master.weibomaster.Activity

import com.master.VangeBugs.Base.BaseActivity
import com.master.weibomaster.R
import com.master.weibomaster.Util.StateBarUtils
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.web_activity.*

/**
 * Created by 不听话的好孩子 on 2018/3/7.
 */
class WebViewActivity :BaseActivity() {
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        val url=intent.getStringExtra("url")

        var test="https://weibo.com/1619101101/%s?from=page_1005051619101101_profile&wvr=6&mod=weibotime&type=comment"
        val s = url.split("/")[2].split("?")[0]
        webview.loadUrl(String.format(test,s))
        webview.settings.javaScriptEnabled=true
        webview.webViewClient=object :WebViewClient(){
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String): Boolean {
                if(!p1.startsWith("http")&&!p1.startsWith("https")&&!p1.startsWith("www.")){
                    return false
                }
                p0?.loadUrl(p1)
                return true
            }
        }
    }

    override fun loadData() {
    }

    override fun needTitle(): Boolean {
        return false
    }
    override fun getLayoutId()= R.layout.web_activity

    override fun onBackPressed() {
        if(!webview.canGoBack()) {
            super.onBackPressed()
        }else{
            webview.goBack()
        }
    }
}