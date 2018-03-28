package com.master.weibomaster.Holder

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.master.weibomaster.Model.Science
import com.master.weibomaster.R
import com.master.weibomaster.Rx.MyObserver
import com.master.weibomaster.Util.ActivityUtils
import com.master.weibomaster.Util.ApkUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Base.ItemHolder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.log
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.toast

/**
 * Created by 不听话的好孩子 on 2018/3/27.
 */
class ScienceHolder : BaseHolder<Science>(R.layout.science_layout) {
    override fun onViewBind(p0: Holder, p1: Science, p2: Int) {
        val view = p0.getView<ImageView>(R.id.icon)
        Glide.with(view).load(p1.icon).into(view)
        p0.setText(R.id.title, p1.name)
        p0.setText(R.id.description, p1.description)

        val progresslayout = p0?.getView<FrameLayout>(R.id.progresslayout)
        val progressbar = p0?.getView<ProgressBar>(R.id.progressBar)
        val progresstext = p0?.getView<TextView>(R.id.progresstext)
        p0.itemView.setOnClickListener {
            p0.itemView.isEnabled=false
            ApkUtils.startApk(p1, object : MyObserver<MyObserver.Progress>(ActivityUtils.getTopActivity()) {
                override fun onNext(t: Progress) {
                    super.onNext(t)
                    if (!t.isComplete) {
                        progresslayout.visibility = View.VISIBLE
                        progressbar.progress = (100 * t.current / t.total).toInt()
                        progresstext.text = "正在下载中...${progressbar.progress}%"
                    } else {
                        p0.itemView.isEnabled=true
                        progresslayout.visibility = View.INVISIBLE
                        ApkUtils.startApk(p1)
                    }

                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    p0.itemView.isEnabled=true
                    "下载失败".toast()
                    progresslayout.visibility=View.INVISIBLE
                }
            })
        }
    }
}