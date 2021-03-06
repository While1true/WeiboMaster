package com.master.weibomaster.Model

import com.nestrefreshlib.Adpater.Impliment.SAdapter
import java.io.Serializable


/**
 * Created by vange on 2018/1/16.
 */

data class Base<T> constructor(var message: String = "", var data: T, var code: Int) : Serializable

data class DownStatu constructor(
        var id: Long = 0,
        var current: Long = 0,
        var total: Long = -1,
        var name: String = "",
        var path: String = "",
        var url: String = "",
        var state: Int = 0
)

data class Category(var category: String = "", var count: Int,var wordsTop10:String)

data class Category_Second(var category: String = "", var count: Int,var parent:String="")


data class Statistic(var lasttime: String = "", var count: Int, var ip: String = "")


data class Artical(
        val id: Int, //186
        val category: String, //派饭
        val content: String, //《大日经》是密教最重要的奠基经典。我们现在放生派饭，就是在做《大日经》中所教的成佛方法。有人说：《大日经》没有说过放生派饭哦！只说金胎两界及四部曼荼罗，诸尊真言及手印。你未消化！街上，即是诸尊会集的大曼荼罗；放生派饭，即是诸尊的事业—羯磨曼荼罗，但愿你能领会悟入。
        val come: String, //纻麻兰若-常观世音法语集
        val mid: String, //4187660929263940
        val hrefStr: String, //常观世音微语录
        val href: String, //常观世音微语录
        val timestr: String, //常观世音微语录
        val datelong: String, //1513899631000
        val imgs: String, ////ww1.sinaimg.cn/thumbnail/608185adjw1ery25r6ugmj20ku0ku0x1.jpg
        var like_count: Int, //0
        var is_like: Int //0
) : Serializable, SAdapter.DifferCallback.differ {
    override fun firstCondition() = id.toString()

    override fun secondCondition() = datelong.toString()
}

data class Science(var name: String = "未知",
                   var description: String = "未知",
                   var packageName: String = "",
                   var mainActivity: String = "",
                   var url: String = "",
                   var size: Long = 0,
                   var date: String = "",
                   var icon: String = "")

data class ToDo(
        val like__count: Int, //2000
        val count: Int //2000
)

data class Pattern(var name: String = "",
                   var img: String = "",
                   var user: String = "")