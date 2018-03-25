package com.master.weibomaster.Api

import com.master.weibomaster.Rx.Net.RetrofitHttpManger
import com.master.weibomaster.Rx.RxSchedulers
import okhttp3.MultipartBody

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ApiImpl : Api {
    override fun latestSplash()= api.latestSplash().compose(RxSchedulers.compose())

    override fun uploadPattern(user: String, name: String, pattern: MultipartBody.Part)= api.uploadPattern(user,name,pattern).compose(RxSchedulers.compose())

    override fun generatePic(id: Int, context: String, user: String, pattern: String?)= api.generatePic(id,context, user,pattern)

    override fun getLikeList(like_user: String, pagenum: Int, pagesize: Int)= api.getLikeList(like_user,pagenum,pagesize).compose(RxSchedulers.compose())

    override fun like(like_id: String,flag:Int, like_user: String)= api.like(like_id,flag,like_user).compose(RxSchedulers.compose())

    override fun getCategory() = api.getCategory().compose(RxSchedulers.compose())

    override fun getToDo() = api.getToDo().compose(RxSchedulers.compose())

    override fun getArticalList(category: String,like_user: String, pagenum: Int, pagesize: Int) = api.getArticalList(category,like_user, pagenum, pagesize).compose(RxSchedulers.compose())

    override fun getSearchList(category: String,words: String,like_user: String, pagenum: Int, pagesize: Int) = api.getSearchList(category,words,like_user, pagenum, pagesize).compose(RxSchedulers.compose())

    companion object {
        private val api by lazy { RetrofitHttpManger.create(Api::class.java) }
        val apiImpl by lazy { ApiImpl() }
    }
}