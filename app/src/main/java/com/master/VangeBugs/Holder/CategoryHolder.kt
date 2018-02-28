package com.master.VangeBugs.Holder

import android.content.Intent
import com.master.VangeBugs.Activity.ProgramListActivity
import com.master.VangeBugs.Model.BugCategory
import com.master.VangeBugs.R
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.startActivity

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryHolder : BaseHolder<BugCategory>(R.layout.category_item) {
    override fun onViewBind(p0: Holder?, p1: BugCategory?, p2: Int) {
        p0?.setText(R.id.title, p1?.category)
        p0?.setText(R.id.subtitle, "当前待解决Bug数：" + p1?.dcount)
        p0?.itemView?.setOnClickListener {
            val intent = Intent(p0?.itemView.context, ProgramListActivity::class.java)
            intent.putExtra("category", p1?.category)
            startActivity(p0?.itemView.context, intent)
        }
    }
}