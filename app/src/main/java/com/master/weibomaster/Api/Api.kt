package com.master.VangeBugs.Api

import com.master.weibomaster.Model.Artical
import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.Category
import com.master.weibomaster.Model.ToDo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
interface Api {
    @GET("category")
    fun getCategory():Observable<Base<List<Category>>>

    @GET("todo")
    fun getToDo():Observable<Base<ToDo>>
    @GET("articallist")
    fun getArticalList(@Query("category")category:String, @Query("pagenum")pagenum: Int, @Query("pagesize")pagesize: Int):Observable<Base<List<Artical>>>


}