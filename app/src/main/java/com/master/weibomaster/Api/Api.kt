package com.master.weibomaster.Api

import com.master.weibomaster.Model.*
import com.update.UpdateBean
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
interface Api {
    @GET("latestSplash")
    fun latestSplash(): Observable<Base<Artical>>

    @GET("category")
    fun getCategory(): Observable<Base<List<Category>>>

    @GET("statistics")
    fun statistics(@Query("user") user: String): Observable<Base<Statistic>>
    @GET("downloadstatistic")
    fun downloadstatistic(@Query("user") user: String): Observable<Base<Statistic>>

    @GET("science")
    fun science(): Observable<Base<List<Science>>>

    @GET("todo")
    fun getToDo(@Query("like_user") like_user: String): Observable<Base<ToDo>>

    @GET("articallist")
    fun getArticalList(@Query("come") come: String,@Query("category") category: String, @Query("like_user") like_user: String, @Query("pagenum") pagenum: Int, @Query("pagesize") pagesize: Int): Observable<Base<List<Artical>>>

    @GET("search")
    fun getSearchList(@Query("come") come: String,@Query("category") category: String = "", @Query("words") words: String, @Query("like_user") like_user: String, @Query("pagenum") pagenum: Int, @Query("pagesize") pagesize: Int): Observable<Base<List<Artical>>>

    @GET("getlikelist")
    fun getLikeList(@Query("like_user") like_user: String, @Query("pagenum") pagenum: Int, @Query("pagesize") pagesize: Int): Observable<Base<List<Artical>>>

    @GET("like")
    fun like(@Query("like_id") like_id: String, @Query("flag") flag: Int, @Query("like_user") like_user: String): Observable<Base<Any>>

    @GET("getWordCloud")
    fun generatePic(@Query("id") id: Int, @Query("context") context: String, @Query("user") user: String, @Query("pattern") pattern: String? = null): Observable<Base<String>>

    @Multipart
    @POST("uploadPattern")
    fun uploadPattern(@Query("user") user: String, @Query("name") name: String, @Part pattern: MultipartBody.Part): Observable<Base<String>>

    @GET("update")
    fun update(): Observable<Base<UpdateBean>>

    @GET("getPattern")
    fun getPattern(@Query("user")user:String?): Observable<Base<List<Pattern>>>

}