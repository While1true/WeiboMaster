package com.master.weibomaster.Holder

import android.app.AlertDialog
import android.content.Intent
import android.widget.TextView
import com.master.VangeBugs.Api.ApiImpl
import com.master.weibomaster.Activity.CategoryListActivity
import com.master.weibomaster.Activity.WebViewActivity
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.UPDATE_INDICATE
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Rx.Utils.RxBus
import com.master.weibomaster.Rx.Utils.RxLifeUtils
import com.master.weibomaster.Util.ActivityUtils
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Widgets.IndicateTextView
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.toast

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ArticalListHolder : BaseHolder<Artical>(R.layout.artical_list_layout) {
    override fun onViewBind(p0: Holder?, p1: Artical?, p2: Int) {
        p0?.setText(R.id.content, p1?.content)
        val collect = p0?.getView<IndicateTextView>(R.id.collect)
        var islike = (p1!!.is_like == 1)
        collect?.isSelected = islike
        collect?.text = if (islike) "已收藏" else "收藏"
        collect?.indicate = p1?.like_count
        p0?.setText(R.id.time, p1?.timestr)
        p0?.setText(R.id.come, p1?.come)

        p0?.setOnClickListener(R.id.net,{
            val intent=Intent(ActivityUtils.getTopActivity(),WebViewActivity::class.java)
            intent.putExtra("url",p1?.href)
            ActivityUtils.getTopActivity().startActivity(intent)
        })
        collect?.setOnClickListener {
            doCollect(p0, p1, collect, p2)
        }
        p0?.itemView?.setOnLongClickListener {

            true
        }
    }

    private fun doCollect(p0: Holder?, p1: Artical, collect: IndicateTextView?, p2: Int) {

        if (p1!!.is_like == 0) {
            collectNet(p1, p0, collect, p2)
        } else {
            AlertDialog.Builder(p0!!.itemView.context)
                    .setTitle(if (p1!!.is_like == 1) "取消收藏该博文？" else "收藏该博文？")
                    .setMessage(if (p1!!.is_like == 1) "将从收藏页移除" else "将添加在收藏页方便查看")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", { dialog, which ->
                        collectNet(p1, p0, collect, p2)
                    }).create().show()
        }

    }

    private fun collectNet(p1: Artical, p0: Holder?, collect: IndicateTextView?, p2: Int) {
        ApiImpl.apiImpl.like(p1?.id.mtoString(), (p1?.is_like + 1) % 2, DeviceUtils.getUniqueId(ActivityUtils.getTopActivity()))
                .subscribe(object : DataObserver<Any>(p0?.itemView) {
                    override fun onComplete() {
                        super.onComplete()
                        RxLifeUtils.getInstance().remove(p0?.itemView)
                    }

                    override fun OnNEXT(bean: Any?) {
                        p1.is_like = (p1.is_like + 1) % 2
                        collect?.text = if (p1.is_like == 1) "已收藏" else "收藏"
                        collect?.isSelected = p1.is_like == 1
                        collect!!.indicate = collect!!.indicate + (if (p1.is_like == 1) 1 else -1)
                        RxBus.getDefault().post(Base(code = UPDATE_INDICATE, data = p2))
                        val toaste = if (p1.is_like == 1) "收藏成功" else "取消收藏成功"
                        toaste.toast()
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        error.mtoString().toast()
                    }

                })
    }

    private fun getissue(issue: String?): CharSequence? {
        if (issue.mtoString().length > 10) {
            return issue?.substring(0, 10)
        } else {
            return issue
        }
    }
}