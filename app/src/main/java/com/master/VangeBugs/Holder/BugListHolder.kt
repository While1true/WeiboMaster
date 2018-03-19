package com.master.VangeBugs.Holder

import android.text.TextUtils
import com.master.VangeBugs.Fragment.DetailF
import com.master.VangeBugs.Fragment.WebF
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.R
import com.master.VangeBugs.Util.ActivityUtils
import com.master.VangeBugs.Util.FragmentUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Base.MyCallBack
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.mtoString
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class BugListHolder : BaseHolder<Bug>(R.layout.bug_list_layout) {
    val dateformat=SimpleDateFormat("yyyy-MM-dd hh:mm")
    override fun onViewBind(p0: Holder?, p3: Bug, p2: Int) {
        p0?.setText(R.id.issue, "发布者：" + p3.tester)
        p0?.setText(R.id.publisher, if (TextUtils.isEmpty(p3.title
                        )) getissue(p3.content) else p3.title)
        p0?.setText(R.id.time, dateformat.format(Date(p3.create_time)))
        p0?.setText(R.id.state, if (p3.status == "N") "待解决" else "解决")

        p0?.itemView?.setOnClickListener {
            val display = WebF().display(p3)
            display.callback= MyCallBack<Boolean> {
                p0.setText(R.id.issue, "发布者：" + p3.tester)
            }
            FragmentUtils.showAddFragment(ActivityUtils.getTopActivity(), display)
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