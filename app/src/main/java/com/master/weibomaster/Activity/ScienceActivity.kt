package com.master.weibomaster.Activity

import android.support.v7.widget.GridLayoutManager
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.Holder.RefreshLayoutPageLoading
import com.master.weibomaster.Holder.ScienceHolder
import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.Science
import com.master.weibomaster.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/3/27.
 */
class ScienceActivity : BaseActivity() {
    override fun initView() {
        setTitle("探索实验室")
    }

    override fun loadData() {
        refreshlayout.attrsUtils.overscrolL_ELASTIC=true
        object :RefreshLayoutPageLoading<Science>(refreshlayout,GridLayoutManager(this,2),false){
            override fun getObservable()=ApiImpl.apiImpl.science()
        }
                .addType(ScienceHolder())
                .Go()

    }

    override fun getLayoutId()= R.layout.refreshlayout
}