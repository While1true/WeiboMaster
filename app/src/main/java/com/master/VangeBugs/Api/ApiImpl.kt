package com.master.VangeBugs.Api

import com.master.VangeBugs.Rx.Net.RetrofitHttpManger
import com.master.VangeBugs.Rx.RxSchedulers

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ApiImpl : Api {
    override fun like(id: Long, like: Int)=api.like(id,like).compose(RxSchedulers.compose())

    override fun getUnsolvedLike()=api.getUnsolvedLike().compose(RxSchedulers.compose())

    override fun getToDo() = api.getToDo().compose(RxSchedulers.compose())

    override fun getBugList(category: String, pagenum: Int, pagesize: Int) = api.getBugList(category, pagenum, pagesize).compose(RxSchedulers.compose())

    override fun getPrograms() = api.getPrograms().compose(RxSchedulers.compose())

    companion object {
        private val api by lazy { RetrofitHttpManger.create(Api::class.java) }
        val apiImpl by lazy { ApiImpl() }
    }
}