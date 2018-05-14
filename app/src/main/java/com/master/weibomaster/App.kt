package com.master.weibomaster

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.didi.virtualapk.PluginManager
import com.master.weibomaster.Util.AdjustUtil
import com.nestrefreshlib.RefreshViews.RefreshLayout
import com.tencent.smtt.sdk.QbSdk
import coms.pacs.pacs.Utils.log
import coms.pacs.pacs.Utils.mtoString
import io.reactivex.plugins.RxJavaPlugins
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException


/**
 * Created by vange on 2018/1/16.
 */
class App : Application(){

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        PluginManager.getInstance(base).init()
        MultiDex.install(this)
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
                log(exception?.message.mtoString())
            }

            override fun fatalError(exception: SAXParseException?) {
            }

        }}
        preInitX5Core()

    }

    private fun preInitX5Core() {
        try {
            QbSdk.initX5Environment(applicationContext, null)
        } catch (e: Exception) {
        }
    }

    init {
        app = this
    }

    companion object {
        lateinit var app: App
    }
}