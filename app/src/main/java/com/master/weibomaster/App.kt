package com.master.weibomaster

import android.app.Application
import android.content.Context
import com.master.weibomaster.Util.AdjustUtil
import com.nestrefreshlib.RefreshViews.RefreshLayout
import com.tencent.smtt.sdk.QbSdk
import coms.pacs.pacs.Utils.log
import coms.pacs.pacs.Utils.mtoString
import io.reactivex.plugins.RxJavaPlugins

/**
 * Created by vange on 2018/1/16.
 */
class App : Application(), QbSdk.PreInitCallback {

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
        RxJavaPlugins.setErrorHandler { log(it.message.mtoString()) }
        if(!QbSdk.isTbsCoreInited()){
            QbSdk.preInit(this,this)
        }


    }


    init {
        app = this
    }

    companion object {
        lateinit var app: App
    }


    override fun onCoreInitFinished() {
        QbSdk.initX5Environment(this,null)
    }

    override fun onViewInitFinished(p0: Boolean) {
    }
}