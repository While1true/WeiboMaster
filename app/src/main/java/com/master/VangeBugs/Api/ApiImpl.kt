package com.master.VangeBugs.Api

import com.master.VangeBugs.Rx.Net.RetrofitHttpManger
import com.master.VangeBugs.Rx.RxSchedulers

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ApiImpl : Api {
    override fun getProgress()= api.getProgress().compose(RxSchedulers.compose())

    companion object {
        private val api by lazy { RetrofitHttpManger.create(Api::class.java) }
        val apiImpl by lazy { ApiImpl() }
    }
}