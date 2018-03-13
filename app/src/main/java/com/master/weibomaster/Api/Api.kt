package com.master.VangeBugs.Api

import com.master.weibomaster.Model.Artical
import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.Category
import com.master.weibomaster.Model.ToDo
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
interface Api {
    @GET("category")
    fun getCategory(): Observable<Base<List<Category>>>

    @GET("todo")
    fun getToDo(): Observable<Base<ToDo>>

    @GET("articallist")
    fun getArticalList(@Query("category") category: String, @Query("like_user") like_user: String, @Query("pagenum") pagenum: Int, @Query("pagesize") pagesize: Int): Observable<Base<List<Artical>>>

    @GET("getlikelist")
    fun getLikeList(@Query("like_user") like_user: String, @Query("pagenum") pagenum: Int, @Query("pagesize") pagesize: Int): Observable<Base<List<Artical>>>

    @GET("like")
    fun like(@Query("like_id") like_id: String, @Query("flag") flag: Int, @Query("like_user") like_user: String): Observable<Base<Any>>

    @GET("getWordCloud")
    fun generatePic(@Query("id") id: Int, @Query("context") context: String, @Query("user") user: String, @Query("pattern") pattern: String? = null): Observable<Base<String>>

    @Multipart
    @POST("uploadPattern")
    fun uploadPattern(@Query("user") user: String, @Query("name") name: String,  @Part pattern:MultipartBody.Part): Observable<Base<String>>

}