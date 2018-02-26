package com.master.weibomaster

import android.app.Application
import android.content.Context
import com.iflytek.cloud.SpeechUtility
import com.master.weibomaster.Util.AdjustUtil
import com.master.weibomaster.Voice.XFVoice
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

        SpeechUtility.createUtility(this, "appid=" + "5a90dd1f")
        XFVoice.setDefaultParam()

    }


    init {
        app = this
    }

    companion object {
        lateinit var app: App
    }

}