package com.master.VangeBugs.Api

import com.master.weibomaster.Rx.Net.RetrofitHttpManger
import com.master.weibomaster.Rx.RxSchedulers

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ApiImpl : Api {

    override fun getCategory() = api.getCategory().compose(RxSchedulers.compose())
    override fun getToDo() = api.getToDo().compose(RxSchedulers.compose())
    override fun getArticalList(category: String, pagenum: Int, pagesize: Int) = api.getArticalList(category, pagenum, pagesize).compose(RxSchedulers.compose())
    companion object {
        private val api by lazy { RetrofitHttpManger.create(Api::class.java) }
        val apiImpl by lazy { ApiImpl() }
    }
}