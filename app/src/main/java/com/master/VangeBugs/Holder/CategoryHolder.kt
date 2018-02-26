package com.master.VangeBugs.Holder

import com.master.VangeBugs.Model.BugCategory
import com.master.VangeBugs.R
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.toast

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryHolder : BaseHolder<BugCategory>(R.layout.category_item) {
    override fun onViewBind(p0: Holder?, p1: BugCategory?, p2: Int) {
        p0?.setText(R.id.title,p1?.category)
        p0?.setText(R.id.subtitle,"当前总Bug数："+p1?.dcount)
        p0?.itemView?.setOnClickListener {
            "点击了".toast()
        }
    }
}