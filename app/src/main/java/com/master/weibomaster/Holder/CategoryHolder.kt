package com.master.weibomaster.Holder

import android.content.Intent
import com.master.weibomaster.Activity.CategoryListActivity
import com.master.weibomaster.Model.Category
import com.master.weibomaster.R
import com.master.weibomaster.Util.ActivityUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryHolder : BaseHolder<Category>(R.layout.category_item) {
    override fun onViewBind(p0: Holder?, p1: Category?, p2: Int) {
        p0?.setText(R.id.title, p1?.category)
        p0?.setText(R.id.subtitle, "相关博文条数：" + p1?.count)
        p0?.itemView?.setOnClickListener {
            var intent = Intent(ActivityUtils.getTopActivity(), CategoryListActivity::class.java)
            if (p1?.category != "热门分类")
                intent.putExtra("come", p1?.category)
//            val intent = Intent(p0?.itemView.context, ProgramListActivity::class.java)
//            intent.putExtra("category", p1?.category)
//            intent.putExtra("category_id", p1?.category_id)
            ActivityUtils.getTopActivity().startActivity(intent!!)
        }
    }
}