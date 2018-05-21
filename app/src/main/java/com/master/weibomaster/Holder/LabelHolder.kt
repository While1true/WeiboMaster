package com.master.weibomaster.Holder

import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.widget.TextView
import com.master.weibomaster.Activity.SearchActivity
import com.master.weibomaster.Model.Category_Second
import com.master.weibomaster.R
import com.master.weibomaster.Util.ActivityUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import java.util.Random

/**
 * Created by 不听话的好孩子 on 2018/5/18.
 */
class LabelHolder :BaseHolder<Category_Second>(R.layout.textview2) {
    val color =  arrayOf(-765666,-765666, -14776091, -765666,-7461718, -11243910,-12345273, -14776091, -7461718, -11243910)
    override fun onViewBind(p0: Holder, p1: Category_Second, p2: Int) {
        val textView = p0.getView<TextView>(R.id.textview)
//        val nextInt = Random().nextInt(color.size)
//        textView.setTextColor(color[nextInt])
        textView.setTextColor(-11243910)
        textView.text = p1.category+"(${p1.count})"

        p0.itemView.setOnClickListener {
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityUtils.getTopActivity())
            val intent = Intent(p0.itemView.context, SearchActivity::class.java)

            if(p1.parent=="热门分类")
            intent.putExtra("category", p1.category)
            else{
                intent.putExtra("come", p1.parent)
                intent.putExtra("words", p1.category)
            }
            ActivityCompat.startActivity(p0.itemView.context, intent, option.toBundle())

        }
    }
}