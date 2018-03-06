package com.master.VangeBugs.Api

import com.master.VangeBugs.Model.Base
import com.master.VangeBugs.Model.BugCategory
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
interface Api {
    @GET("category")
    fun getProgress():Observable<Base<List<BugCategory>>>
}