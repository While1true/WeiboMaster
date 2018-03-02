package com.master.VangeBugs.Activity

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.InnerDecorate
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Base.BaseActivity
import com.master.VangeBugs.Holder.BugListHolder
import com.master.VangeBugs.Holder.NomoreHolder
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.Model.xx
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.DataObserver
import com.master.VangeBugs.Util.StateBarUtils
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.RefreshViews.RefreshListener
import com.nestrefreshlib.RefreshViews.RefreshWrap.MyRefreshInnerHandler
import com.nestrefreshlib.State.DefaultStateListener
import com.nestrefreshlib.State.StateLayout
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ProgramListActivity : BaseActivity() {
    var pagenum = 0
    var pagesize = 18
    var category = ""
    var category_id = ""
    var stateLayout: StateLayout? = null
    var list = mutableListOf<Any>()
    var adapter: SAdapter? = null
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        category = intent.getStringExtra("category")
        category_id = intent.getStringExtra("category_id")
        setTitle(category)
        stateLayout?.showLoading()

    }

    override fun loadData() {
        ApiImpl.apiImpl.getBugList(category_id, pagenum, pagesize)
                .subscribe(object : DataObserver<List<xx>>(this) {
                    override fun OnNEXT(bean: List<xx>) {
                        if (pagenum == 0) {
                            list.clear()
                        }
                        list.addAll(bean)
                        if (adapter == null) {
                            initAdapter()
                        }
                        if (pagenum == 0) {
                            if (bean.isEmpty())
                                stateLayout?.showEmpty()
                            else if (bean.size < pagesize) {
//                                stateLayout?.showState(StateEnum.SHOW_INFO, "没有更多了")
                                refreshlayout.attrsUtils.canfootr = false
                                stateLayout?.showItem()
                            }
                        } else if (bean.size < pagesize) {
                            list.add(Any())
                            adapter?.addType(NomoreHolder())
                            refreshlayout.attrsUtils.canfootr = false
                            stateLayout?.showItem()
                        }
                        adapter?.notifyDataSetChanged()
                        refreshlayout.NotifyCompleteRefresh0()
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        if (pagenum == 0)
                            stateLayout?.ShowError()
                        if (pagenum > 0)
                            pagenum--
                        refreshlayout.NotifyCompleteRefresh0()
                    }
                })
    }


    private fun initAdapter() {
        adapter = SAdapter(list)
                .addLifeOwener(this)
                .addType(BugListHolder())

        val recyclerView = refreshlayout.getmScroll<RecyclerView>()
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.addItemDecoration(InnerDecorate(this,LinearLayout.VERTICAL))
        MyRefreshInnerHandler.setInnerRecyclerviewAdapter(
                refreshlayout,MyRefreshInnerHandler(),
                adapter)
//        refreshlayout.setInnerAdapter()

        refreshlayout.setListener(object : RefreshListener() {
            override fun Loading() {
                pagenum++
                loadData()
            }

            override fun Refreshing() {
                refreshlayout.attrsUtils.canfootr = true
                pagenum = 0
                loadData()
            }

        })
    }

    override fun getLayoutId() = R.layout.refreshlayout
    override fun contentView(): View? {
        stateLayout = StateLayout(this).setContent(getLayoutId())
        stateLayout?.setStateListener(object : DefaultStateListener(){
            override fun netError(p0: Context?) {
                loadData()
            }

        })
        return stateLayout
    }
}