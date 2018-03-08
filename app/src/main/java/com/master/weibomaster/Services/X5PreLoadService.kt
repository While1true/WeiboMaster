package com.master.weibomaster.Services

import com.tencent.smtt.sdk.QbSdk
import android.content.Intent
import android.app.IntentService
import android.support.annotation.Nullable


/**
 * Created by 不听话的好孩子 on 2018/3/8.
 */


class X5PreLoadService : IntentService(TAG) {

    internal var cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {

        override fun onViewInitFinished(arg0: Boolean) {
            // TODO Auto-generated method stub
            //初始化完成回调
        }

        override fun onCoreInitFinished() {
            // TODO Auto-generated method stub
        }
    }

    override fun onHandleIntent(@Nullable intent: Intent?) {
        //在这里添加我们要执行的代码，Intent中可以保存我们所需的数据，
        //每一次通过Intent发送的命令将被顺序执行
        initX5()
    }

    /**
     * 初始化X5内核
     */
    private fun initX5() {
        try {
            if (!QbSdk.isTbsCoreInited()) {
                QbSdk.preInit(applicationContext, null)// 设置X5初始化完成的回调接口
            }
            QbSdk.initX5Environment(applicationContext, cb)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = X5PreLoadService::class.java.simpleName
    }
}
