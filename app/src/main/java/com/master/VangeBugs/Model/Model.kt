package com.master.VangeBugs.Model

import com.nestrefreshlib.Adpater.Impliment.SAdapter
import java.io.Serializable


/**
 * Created by vange on 2018/1/16.
 */

data class Base<T> constructor(var message: String="", var data: T, var error_code: Int) : Serializable

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
		val id:Int,
        val name: String, //基本
        val online_date: Long, //基本
        val project_name: String, //基本
        val url: String, //13399
        val dcount: Int //13399
)


data class Bug(
        val id: Long,
        val project_id: Long,
		val content: String, //PACS
		val status: String, //0
		val tester: String, //小毛
		val title: String, //未知原因
		val create_time: Long, //2018-02-26
		val handle_time: Long, //2018-02-26
		val handler: String //2018-02-26
)


data class ToDo(
		val like__count: Int, //2000
		val count: Int //2000
)

