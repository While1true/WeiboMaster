package com.master.VangeBugs.Model

import com.nestrefreshlib.Adpater.Impliment.SAdapter
import java.io.Serializable


/**
 * Created by vange on 2018/1/16.
 */

data class Base<T> constructor(var message: String="", var data: T, var code: Int) : Serializable

data class DownStatu constructor(
        var id: Long = 0,
        var current: Long = 0,
        var total: Long = -1,
        var name: String = "",
        var path: String = "",
        var url: String = "",
        var state: Int = 0
)


data class BugCategory(
        val category: String, //基本
        val dcount: Int //13399
)


data class Bug(
        val id: Long,
		val category: String, //PACS
		val state: Int, //0
		val publisher: String, //小毛
		val title: String, //未知原因
		val issue: String, //未知原因
		val time: String, //2018-02-26
		var like: Int //2018-02-26
):SAdapter.DifferCallback.differ {
	override fun firstCondition()=id.toString()

	override fun secondCondition()=publisher
}


data class ToDo(
		val like__count: Int, //2000
		val count: Int //2000
)

