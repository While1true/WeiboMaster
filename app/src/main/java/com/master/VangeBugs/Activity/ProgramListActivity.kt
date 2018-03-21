package com.master.VangeBugs.Activity

import android.content.Context
import android.support.v7.widget.InnerDecorate
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Base.BaseActivity
import com.master.VangeBugs.Holder.BugListHolder
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.DataObserver
import com.master.VangeBugs.Util.StateBarUtils
import com.nestrefreshlib.RefreshViews.AdapterHelper.AdapterScrollListener
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter
import com.nestrefreshlib.RefreshViews.RefreshListener
import com.nestrefreshlib.State.DefaultStateListener
import com.nestrefreshlib.State.Interface.StateEnum
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ProgramListActivity : BaseActivity() {
    var pagenum = 1
    var pagesize = 8
    var titlex = ""
    var id = 0
    var nomore = false
    var list = mutableListOf<Any>()
    var mAdapter: StateAdapter? = null
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        titlex = intent.getStringExtra("title")
        setTitle(titlex)

        id = intent.getIntExtra("id",0)
        initAdapter()
    }

    override fun loadData() {
        refreshlayout.attrsUtils.overscroll=true
        ApiImpl.apiImpl.getBugList(id.toString(), pagenum, pagesize)
                .subscribe(object : DataObserver<List<Bug>>(this) {
                    override fun OnNEXT(bean: List<Bug>) {
                        if (pagenum == 1) {
                            list.clear()
                        }
                        list.addAll(bean)
                        if (pagenum == 1&&bean.isEmpty()) {
                            mAdapter?.showEmpty()
                        } else {
                            if (bean.size < pagesize) {
                                nomore = true
                                mAdapter?.showState(StateEnum.SHOW_NOMORE,if(pagenum==1) "" else "这是底线了")
                            }else {
                                mAdapter?.showState(StateEnum.SHOW_NOMORE,"正在加载中...")
                            }
                            refreshlayout.NotifyCompleteRefresh0()
                        }
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        if (pagenum == 1)
                            mAdapter?.ShowError()
                        if (pagenum > 1)
                            pagenum--
                        refreshlayout.NotifyCompleteRefresh0()
                    }
                })
    }


    private fun initAdapter() {
        mAdapter = StateAdapter(list)
                .setStateListener(object : DefaultStateListener(){
                    override fun netError(p0: Context?) {
                        mAdapter?.showLoading()
                        loadData()
                    }

                })
                .addLifeOwener(this)
                .addType(BugListHolder()) as StateAdapter?
        mAdapter?.showLoading()
       refreshlayout.getmScroll<RecyclerView>().apply {
           addItemDecoration(InnerDecorate(this@ProgramListActivity, LinearLayout.VERTICAL))
           layoutManager=LinearLayoutManager(this@ProgramListActivity)
           adapter=mAdapter
           addOnScrollListener(AdapterScrollListener(refreshlayout))
       }

        refreshlayout.setListener(object : RefreshListener() {
            override fun Loading() {
                if (!nomore) {
                    pagenum++
                    loadData()
                }
            }

            override fun Refreshing() {
                refreshlayout.attrsUtils.canfootr=true
                nomore = false
                pagenum = 1
                loadData()
            }

        })
    }

    override fun getLayoutId() = R.layout.refreshlayout
}