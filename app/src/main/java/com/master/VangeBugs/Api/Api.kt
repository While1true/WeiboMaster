package com.master.VangeBugs.Api

import com.master.VangeBugs.Model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
interface Api {
    @GET("category")
    fun getPrograms():Observable<Base<List<BugCategory>>>

    @GET("todo")
    fun getToDo():Observable<Base<ToDo>>

    @GET("buglist")
    fun getBugList(@Query("category")category:String,@Query("pagenum")pagenum: Int,@Query("pagesize")pagesize: Int):Observable<Base<List<xx>>>

    @GET("unsolvelike")
    fun getUnsolvedLike():Observable<Base<List<xx>>>

    @GET("like")
    fun like(@Query("id")id:Long,@Query("like")like:Int):Observable<Base<Any>>

}