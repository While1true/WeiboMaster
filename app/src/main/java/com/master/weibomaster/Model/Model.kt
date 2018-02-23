package com.master.weibomaster.Model

import java.io.Serializable


/**
 * Created by vange on 2018/1/16.
 */

data class Base<T> constructor(var message: String, var data: T, var code: Int) : Serializable

data class DownStatu constructor(
		var id: Long=0,
		var current:Long=0,
		var total:Long=-1,
		var name: String="",
		var path: String="",
		var url: String="",
		var state:Int=0
)