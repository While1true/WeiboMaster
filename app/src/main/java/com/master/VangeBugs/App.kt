package com.master.VangeBugs

import android.app.Application
import android.content.Context
import com.master.VangeBugs.Util.AdjustUtil
import com.nestrefreshlib.RefreshViews.RefreshLayout

/**
 * Created by vange on 2018/1/16.
 */
class App : Application() {
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
    }


    init {
        app = this
    }

    companion object {
        lateinit var app: App
    }

}