package com.master.weibomaster.Activity

import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import com.master.VangeBugs.Base.BaseActivity
import com.master.weibomaster.R
import com.master.weibomaster.Util.StateBarUtils
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.web_activity.*

/**
 * Created by 不听话的好孩子 on 2018/3/7.
 */
class WebViewActivity : BaseActivity() {
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        val url = intent.getStringExtra("url")

        var test = "https://weibo.com"
        webview.loadUrl(test + url)
        webview.settings.javaScriptEnabled = true
        stateLayout?.showLoading()
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String): Boolean {
                if (p1.startsWith("weixin://") //微信
                        || p1.startsWith("alipays://") //支付宝
                        || p1.startsWith("mailto://") //邮件
                        || p1.startsWith("tel://")//电话
                        || p1.startsWith("sinaweibo://")//微博
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(p1))
                    startActivity(intent)
                    return true
                }
                p0?.loadUrl(p1)
                return true
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                stateLayout?.showItem()
            }
        }
        webview.webChromeClient = object : WebChromeClient() {
        }
    }

    override fun loadData() {
    }

    override fun needTitle(): Boolean {
        return false
    }

    override fun getLayoutId() = R.layout.web_activity

    override fun onBackPressed() {
        if (!webview.canGoBack()) {
            super.onBackPressed()
        } else {
            webview.goBack()
        }
    }
}