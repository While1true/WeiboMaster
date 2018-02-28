package com.master.VangeBugs

import android.app.Application
import android.content.Context
import com.master.VangeBugs.Util.AdjustUtil
import com.nestrefreshlib.RefreshViews.RefreshLayout
import com.nestrefreshlib.State.Interface.Recorder
import com.nestrefreshlib.State.StateLayout

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
        StateLayout.init(Recorder.Builder().setNomoreRes(R.layout.info).build())
    }


    init {
        app = this
    }

    companion object {
        lateinit var app: App
    }

}