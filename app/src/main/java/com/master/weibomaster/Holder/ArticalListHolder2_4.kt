package com.master.weibomaster.Holder

import com.master.weibomaster.Model.Artical

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ArticalListHolder2_4 :ArticalListHolder(){
    override fun istype(item: Any?, position: Int): Boolean {
        val split = (item as Artical).imgs?.split(";")
        return split.size<=4
    }
}