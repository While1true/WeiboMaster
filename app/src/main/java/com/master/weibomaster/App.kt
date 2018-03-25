package com.master.weibomaster

import android.app.Application
import android.content.Context
import com.master.weibomaster.Util.AdjustUtil
import com.nestrefreshlib.RefreshViews.RefreshLayout
import com.tencent.smtt.sdk.QbSdk
import io.reactivex.plugins.RxJavaPlugins
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import android.content.Intent
import com.master.weibomaster.Services.X5PreLoadService


/**
 * Created by vange on 2018/1/16.
 */
class App : Application(){

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        PluginManager.getInstance(base).init()
    }
    override fun onCreate() {
        super.onCreate()
        AdjustUtil.adjust(this)
        RefreshLayout.init(RefreshLayout.DefaultBuilder()
                .setCanheaderDefault(true)
                .setCanfootrDefault(true)
                .setScrollLayoutIdDefault(R.layout.recyclerview)
                .setHeaderLayoutidDefault(R.layout.header_layout)
                .setFooterLayoutidDefault(R.layout.footer_layout))
        RxJavaPlugins.setErrorHandler{object : ErrorHandler{
            override fun warning(exception: SAXParseException?) {
            }

            override fun error(exception: SAXParseException?) {
            }

            override fun fatalError(exception: SAXParseException?) {
            }

        }}
        preInitX5Core()

    }

    private fun preInitX5Core() {
        //预加载x5内核
        QbSdk.initX5Environment(applicationContext, null)
//        val intent = Intent(this, X5PreLoadService::class.java)
//        startService(intent)
    }

    init {
        app = this
    }

    companion object {
        lateinit var app: App
    }
}