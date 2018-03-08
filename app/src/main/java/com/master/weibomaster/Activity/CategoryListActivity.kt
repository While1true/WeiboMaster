package com.master.weibomaster.Activity

import android.content.Context
import android.support.v7.widget.InnerDecorate
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Base.BaseActivity
import com.master.weibomaster.Holder.ArticalListHolder
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Util.StateBarUtils
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.RefreshViews.RefreshListener
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler
import com.nestrefreshlib.State.DefaultStateListener
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryListActivity : BaseActivity() {
    var pagenum = 0
    var pagesize = 18
    var category = ""
    var nomore = false
    var list = mutableListOf<Any>()
    var adapter: SAdapter? = null
    var refreshAdapterHandler: RefreshAdapterHandler? = null
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        category = intent.getStringExtra("category")
        setTitle(category)
        stateLayout?.showLoading()
    }

    override fun loadData() {
        ApiImpl.apiImpl.getArticalList(category, DeviceUtils.deviceID, pagenum, pagesize)
                .subscribe(object : DataObserver<List<Artical>>(this) {
                    override fun OnNEXT(bean: List<Artical>) {
                        if (pagenum == 0) {
                            list.clear()
                        }
                        list.addAll(bean)
                        if (adapter == null) {
                            initAdapter()
                        }
                        if (pagenum == 0) {
                            if (bean.isEmpty()) {
                                stateLayout?.showEmpty()
                            } else if (bean.size < pagesize) {
                                nomore = true
                                refreshAdapterHandler?.stopLoading("")
//                                stateLayout?.showState(StateEnum.SHOW_INFO, "没有更多了")
                                stateLayout?.showItem()
                            } else {
                                stateLayout?.showItem()
                            }
                        } else if (bean.size < pagesize) {
                            nomore = true
                            refreshAdapterHandler?.stopLoading("这是底线了")
                        }
                        refreshlayout.NotifyCompleteRefresh0()
                        adapter!!.notifyDataSetChanged()
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        if (pagenum == 0)
                            stateLayout?.ShowError()
                        else
                            refreshAdapterHandler?.stopLoading("出错了，上拉重试")
                        if (pagenum > 0)
                            pagenum--
                        refreshlayout.NotifyCompleteRefresh0()
                    }
                })
    }


    private fun initAdapter() {
        adapter = SAdapter(list)
                .addLifeOwener(this)
                .addType(ArticalListHolder())

        val recyclerView = refreshlayout.getmScroll<RecyclerView>()
//        recyclerView.addItemDecoration(InnerDecorate(this, LinearLayout.VERTICAL))
        refreshAdapterHandler = RefreshAdapterHandler()
        refreshAdapterHandler?.attachRefreshLayout(refreshlayout, adapter, LinearLayoutManager(this))

        refreshlayout.setListener(object : RefreshListener() {
            override fun Loading() {
                if (!nomore) {
                    refreshAdapterHandler?.startLoading("正在加载...")
                    pagenum++
                    loadData()
                }
            }

            override fun Refreshing() {
                nomore = false
                list.clear()
                pagenum = 0
                loadData()
            }

        })
    }

    override fun getLayoutId() = R.layout.refreshlayout
}