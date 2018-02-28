package com.master.VangeBugs.Holder

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import com.master.VangeBugs.Activity.ProgramListActivity
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Fragment.DetailF
import com.master.VangeBugs.Model.Base
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.Model.UPDATE_INDICATE
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.DataObserver
import com.master.VangeBugs.Rx.Utils.RxBus
import com.master.VangeBugs.Rx.Utils.RxLifeUtils
import com.master.VangeBugs.Util.ActivityUtils
import com.master.VangeBugs.Util.FragmentUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Base.MyCallBack
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.startActivity
import coms.pacs.pacs.Utils.toast

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class BugListHolder : BaseHolder<Bug>(R.layout.bug_list_layout) {
    override fun onViewBind(p0: Holder?, p1: Bug?, p2: Int) {
        p0?.setText(R.id.issue, "发布者：" + p1?.publisher + (if (p1?.like == 1) "/(已重点关注)" else ""))
        p0?.setText(R.id.publisher, if (TextUtils.isEmpty(p1?.title
                        ?: "")) getissue(p1?.issue) else p1?.title)
        p0?.setText(R.id.time, getissue(p1?.time))
        p0?.setText(R.id.state, if (p1?.state == 0) "待解决" else "解决")

        p0?.itemView?.setOnClickListener {
            val display = DetailF().display(p1)
            display.callback= MyCallBack<Boolean> {
                p0?.setText(R.id.issue, "发布者：" + p1?.publisher + (if (it) "/(已重点关注)" else ""))
            }
            FragmentUtils.showAddFragment(ActivityUtils.getTopActivity(), display)
        }
        p0?.itemView?.setOnLongClickListener {
            var islike = (p1!!.like == 1)

            AlertDialog.Builder(p0!!.itemView.context)
                    .setTitle(if (islike) "取消关注该Bug？" else "重点关注该Bug？")
                    .setMessage(if (islike) "将从关注页移除" else "将直接添加在关注页方便查看")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", { dialog, which ->
                        ApiImpl.apiImpl.like(p1!!.id, (p1!!.like + 1) % 2)
                                .subscribe(object : DataObserver<Any>(p0?.itemView) {
                                    override fun onComplete() {
                                        super.onComplete()
                                        RxLifeUtils.getInstance().remove(p0?.itemView)
                                    }

                                    override fun OnNEXT(bean: Any?) {
                                        p1?.like = (p1?.like + 1) % 2
                                        p0?.setText(R.id.issue, "发布者：" + p1?.publisher + (if (p1?.like == 1) "/(已重点关注)" else ""))
                                        RxBus.getDefault().post(Base(code = UPDATE_INDICATE, data = p2))
                                        "成功".toast()
                                    }

                                    override fun OnERROR(error: String?) {
                                        super.OnERROR(error)
                                        "失败".toast()
                                    }

                                })
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