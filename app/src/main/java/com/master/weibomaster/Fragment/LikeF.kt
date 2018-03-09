package com.master.weibomaster.Fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.master.VangeBugs.Api.ApiImpl
import com.master.weibomaster.Holder.ArticalListHolder
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Util.DeviceUtils
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.RefreshViews.RefreshListener
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler
import coms.pacs.pacs.BaseComponent.BaseFragment
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class LikeF : BaseFragment() {
    override fun getLayoutId() = R.layout.refreshlayout

    var pagenum = 0
    var pagesize = 18
    var nomore = false
    var list = mutableListOf<Any>()
    var adapter: SAdapter? = null
    var refreshAdapterHandler: RefreshAdapterHandler? = null


    override fun init(savedInstanceState: Bundle?) {
        stateLayout?.showLoading()
    }

    override fun loadLazy() {
        super.loadLazy()
        ApiImpl.apiImpl.getLikeList(DeviceUtils.deviceID, pagenum, pagesize)
                .subscribe(object : DataObserver<List<Artical>>(this) {
                    override fun OnNEXT(bean: List<Artical>) {
                        showData(bean)
                        if (pagenum == 0) {
                            if(bean.isEmpty()){
                                nomore=true
                                stateLayout?.showEmpty()
                            }else if(bean.size<pagesize){
                                nomore=true
                                refreshAdapterHandler?.stopLoading("这是底线了")
                                stateLayout?.showItem()
                            }else{
                                stateLayout?.showItem()
                            }

                        } else {
                            if(bean.size<pagesize){
                                nomore=true
                                refreshAdapterHandler?.stopLoading("这是底线了")
                            }
                        }
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        if (pagenum == 0)
                            stateLayout?.ShowError()
                        else
                            refreshAdapterHandler?.stopLoading("出错了，上拉重试")

                    }
                })
    }
    override fun loadData() {

    }

    private fun showData(bean: List<Artical>) {
        list.addAll(bean)
        if(!list.isEmpty()){
            if(adapter==null) {
                adapter = SAdapter(list)
                        .addLifeOwener(this)
                        .addType(ArticalListHolder())

                val recyclerView = refreshlayout.getmScroll<RecyclerView>()
//                recyclerView.addItemDecoration(InnerDecorate(context, LinearLayout.VERTICAL))
                refreshAdapterHandler = RefreshAdapterHandler()
                refreshAdapterHandler?.attachRefreshLayout(refreshlayout, adapter, LinearLayoutManager(context))

                refreshlayout.setListener(object : RefreshListener() {
                    override fun Loading() {
                        if (!nomore) {
                            refreshAdapterHandler?.startLoading("正在加载...")
                            pagenum++
                            loadData()
                        }
                    }

                    override fun Refreshing() {
                        list.clear()
                        nomore = false
                        pagenum = 0
                        loadData()
                    }

                })
            }else{
                adapter?.notifyDataSetChanged()
            }

        }
        refreshlayout.NotifyCompleteRefresh0()
    }
}