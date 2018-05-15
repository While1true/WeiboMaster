package com.master.weibomaster.Holder

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.master.picwatchlib.PicFragment
import com.master.weibomaster.R
import com.master.weibomaster.Util.ActivityUtils
import com.master.weibomaster.Util.SizeUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.PositionHolder
import java.util.regex.Pattern

/**
 * Created by 不听话的好孩子 on 2018/5/15.
 */
abstract class PicHolder(spancountx: Int, spiltx: List<String>) :PositionHolder() {
    val spancount:Int
    val spilt:List<String>

    init {
        spancount=spancountx
        spilt=spiltx
    }
    override fun onBind(p0: Holder?, p1: Int) {
        val marginLayoutParams = p0?.itemView?.layoutParams as ViewGroup.MarginLayoutParams
        val i = spilt.size / spancount
        if(i==0){
            marginLayoutParams.height= ArticalListHolder.eachwidth
        }else{
            marginLayoutParams.width= ArticalListHolder.eachwidth /spancount
            marginLayoutParams.height= ArticalListHolder.eachwidth /spancount

        }
        val margin = SizeUtils.dp2px(3f)
        marginLayoutParams.setMargins(margin,margin,margin,margin)
        Glide.with(ActivityUtils.getTopActivity())
                .load(if (spilt[p1].startsWith("http")) spilt[p1] else ("https:" + spilt[p1]))
                .transition(DrawableTransitionOptions().crossFade(800))
                .into(p0!!.getView<ImageView>(R.id.image))

        p0?.itemView?.setOnClickListener {
            val pattern= Pattern.compile("(/.*?/)")
            val arrayListOf = arrayListOf<String>()
            for (s in spilt) {
                handlerImageUrl(s, pattern, arrayListOf)
            }
            PicFragment.Go(ActivityUtils.getTopActivity(), arrayListOf,p1,p0?.itemView, 0xDD999999.toInt())
        }

    }

    private fun handlerImageUrl(s: String, pattern: Pattern, arrayListOf: ArrayList<String>) {
        val stringBuffer = StringBuffer()
        var url = ""
        url = if (!s.startsWith("http")) {
            "https:" + s
        } else {
            s
        }
        val matcher = pattern.matcher(url)
        try {
            matcher.find()
            matcher.find()
            matcher.appendReplacement(stringBuffer, "/large/")
            matcher.appendTail(stringBuffer)
            url = stringBuffer.toString()
        } catch (e: Exception) {
        }
        arrayListOf.add(url)
    }

}