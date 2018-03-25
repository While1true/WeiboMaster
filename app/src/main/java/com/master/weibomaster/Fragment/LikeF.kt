package com.master.weibomaster.Fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Holder.ArticalListHolder
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Util.DeviceUtils
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.RefreshViews.RefreshListener
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler
import com.master.weibomaster.Base.BaseFragment
import com.master.weibomaster.Holder.RefreshLayoutPageLoading
import com.master.weibomaster.Model.Base
import io.reactivex.Observable
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class LikeF : BaseFragment() {
    override fun getLayoutId() = R.layout.refreshlayout


    override fun init(savedInstanceState: Bundle?) {
    }

    override fun loadLazy() {
        super.loadLazy()
        object :RefreshLayoutPageLoading<Artical>(refreshlayout,LinearLayoutManager(context),true){
            override fun getObservable()=ApiImpl.apiImpl.getLikeList(DeviceUtils.deviceID,pagenum,18)

        }.AddLifeOwner(this)
                .addType(ArticalListHolder())
                .Go()

    }
    override fun loadData() {
    }

}