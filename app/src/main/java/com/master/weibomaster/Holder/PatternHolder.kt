package com.master.weibomaster.Holder

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.master.weibomaster.Model.Pattern
import com.master.weibomaster.R
import com.master.weibomaster.Rx.Net.RetrofitHttpManger
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder

/**
 * Created by 不听话的好孩子 on 2018/5/17.
 */
class PatternHolder : BaseHolder<Pattern>(R.layout.pattertlayout) {
    override fun onViewBind(p0: Holder, p1: Pattern, p2: Int) {
        Glide.with(p0.itemView)
                .load(RetrofitHttpManger.PIC_HOST+p1.img)
                .transition(DrawableTransitionOptions().crossFade(800))
                .into(p0.getView<ImageView>(R.id.image))
        p0.setText(R.id.name,p1.name)
    }
}