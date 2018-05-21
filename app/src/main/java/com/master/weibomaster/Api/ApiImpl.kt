package com.master.weibomaster.Api

import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.Science
import com.master.weibomaster.Model.Statistic
import com.master.weibomaster.Rx.Net.RetrofitHttpManger
import com.master.weibomaster.Rx.RxSchedulers
import com.master.weibomaster.Util.DeviceUtils
import com.update.UpdateBean
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ApiImpl : Api {
    override fun getPattern(user: String?)=api.getPattern(user).compose(RxSchedulers.compose())
    override fun update() = api.update().compose(RxSchedulers.compose())

    override fun science() = api.science().compose(RxSchedulers.compose())

    override fun statistics(user: String) = api.statistics(user).compose(RxSchedulers.compose())

    override fun downloadstatistic(user: String) = api.downloadstatistic(user).compose(RxSchedulers.compose())

    override fun latestSplash() = api.latestSplash().compose(RxSchedulers.compose())

    override fun uploadPattern(user: String, name: String, pattern: MultipartBody.Part) = api.uploadPattern(user, name, pattern).compose(RxSchedulers.compose())

    override fun generatePic(id: Int, context: String, user: String, pattern: String?) = api.generatePic(id, context, user, pattern)

    override fun getLikeList(like_user: String, pagenum: Int, pagesize: Int) = api.getLikeList(like_user, pagenum, pagesize).compose(RxSchedulers.compose())

    override fun like(like_id: String, flag: Int, like_user: String) = api.like(like_id, flag, like_user).compose(RxSchedulers.compose())

    override fun getCategory() = api.getCategory().compose(RxSchedulers.compose())

    override fun getToDo(like_user: String) = api.getToDo(like_user).compose(RxSchedulers.compose())

    override fun getArticalList(come:String,category: String, like_user: String, pagenum: Int, pagesize: Int) = api.getArticalList(come,category, like_user, pagenum, pagesize).compose(RxSchedulers.compose())

    override fun getSearchList(come:String,category: String, words: String, like_user: String, pagenum: Int, pagesize: Int) = api.getSearchList(come,category, words, like_user, pagenum, pagesize).compose(RxSchedulers.compose())

    companion object {
        private val api by lazy { RetrofitHttpManger.create(Api::class.java) }
        val apiImpl by lazy { ApiImpl() }
    }
}