package com.master.weibomaster.Holder

import android.app.AlertDialog
import android.content.Intent
import com.master.weibomaster.Activity.CategoryListActivity
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.R
import com.master.weibomaster.Util.ActivityUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.mtoString

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ArticalListHolder : BaseHolder<Artical>(R.layout.artical_list_layout) {
    override fun onViewBind(p0: Holder?, p1: Artical?, p2: Int) {
        p0?.setText(R.id.content,p1?.content)

        p0?.itemView?.setOnClickListener {

        }
        p0?.itemView?.setOnLongClickListener {
            var islike = (p1!!.is_like==0)

            AlertDialog.Builder(p0!!.itemView.context)
                    .setTitle(if (islike) "取消关注该Bug？" else "重点关注该Bug？")
                    .setMessage(if (islike) "将从关注页移除" else "将直接添加在关注页方便查看")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", { dialog, which ->
//                        ApiImpl.apiImpl.like(p3!!.pk.toLong(), (p1!!.islike + 1) % 2)
//                                .subscribe(object : DataObserver<Any>(p0?.itemView) {
//                                    override fun onComplete() {
//                                        super.onComplete()
//                                        RxLifeUtils.getInstance().remove(p0?.itemView)
//                                    }
//
//                                    override fun OnNEXT(bean: Any?) {
//                                        p1?.islike = (p1?.islike + 1) % 2
//                                        p0?.setText(R.id.issue, "发布者：" + p1?.publisher + (if (p1?.like == 1) "/(已重点关注)" else ""))
//                                        RxBus.getDefault().post(Base(code = UPDATE_INDICATE, data = p2))
//                                        "成功".toast()
//                                    }
//
//                                    override fun OnERROR(error: String?) {
//                                        super.OnERROR(error)
//                                        "失败".toast()
//                                    }
//
//                                })
                    }).create().show()
            true
        }
    }

    private fun getissue(issue: String?): CharSequence? {
        if (issue.mtoString().length > 10) {
            return issue?.substring(0, 10)
        } else {
            return issue
        }
    }
}