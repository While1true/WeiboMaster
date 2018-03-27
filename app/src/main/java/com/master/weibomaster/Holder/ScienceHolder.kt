package com.master.weibomaster.Holder

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.master.weibomaster.Model.Science
import com.master.weibomaster.R
import com.master.weibomaster.Util.ApkUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Base.ItemHolder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder

/**
 * Created by 不听话的好孩子 on 2018/3/27.
 */
class ScienceHolder : BaseHolder<Science>(R.layout.science_layout) {
    override fun onViewBind(p0: Holder, p1: Science, p2: Int) {
        val view = p0.getView<ImageView>(R.id.icon)
        Glide.with(view).load(p1.icon).into(view)
        p0.setText(R.id.title,p1.name)
        p0.setText(R.id.description,p1.description)
        p0.itemView.setOnClickListener {
            ApkUtils.startApk(p1)
        }
    }
}