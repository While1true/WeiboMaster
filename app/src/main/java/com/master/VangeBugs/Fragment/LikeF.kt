package com.master.VangeBugs.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.*
import android.view.View
import android.widget.LinearLayout
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Holder.BugListHolder
import com.master.VangeBugs.Model.*
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.DataObserver
import com.master.VangeBugs.Rx.Utils.RxBus
import com.master.VangeBugs.Util.RefreshDifferUtils
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler
import com.nestrefreshlib.State.DefaultStateListener
import com.nestrefreshlib.State.StateLayout
import coms.pacs.pacs.BaseComponent.BaseFragment
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class LikeF : BaseFragment() {
    override fun getLayoutId() = R.layout.refreshlayout

    private var stateLayout: StateLayout? = null
    private var refreshAdapterHandler: RefreshAdapterHandler? = null

    override fun contentView(): View? {
        stateLayout = StateLayout(context)
        stateLayout?.setStateListener(object : DefaultStateListener() {
            override fun netError(p0: Context?) {
                getdata()
            }
        })
        return stateLayout?.setContent(getLayoutId())
    }

    override fun init(savedInstanceState: Bundle?) {
        refreshlayout.attrsUtils.overscrolL_ELASTIC = true
    }

    override fun loadLazy() {
        super.loadLazy()
        stateLayout?.showLoading()
        getdata()
        RxBus.getDefault().toObservable(UPDATE_INDICATE, Base::class.java)
                .subscribe({
                    getdata()
                })
    }

    private fun getdata() {
        ApiImpl.apiImpl.getUnsolvedLike()
                .subscribe(object : DataObserver<List<xx>>(this) {
                    override fun OnNEXT(bean: List<xx>) {
                        if (adapter == null)
                            stateLayout?.showItem()
                        showData(bean)
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        stateLayout?.ShowError()
                    }
                })
    }

    var adapter: SAdapter? = null
    private fun showData(bean: List<xx>) {
        if (adapter == null) {
            adapter = SAdapter(bean)
                    .apply {
                        addType(BugListHolder())
                        addLifeOwener { lifecycle }
                    }

            val getmScroll = refreshlayout.getmScroll<RecyclerView>()
            getmScroll.itemAnimator = DefaultItemAnimator()
            getmScroll.addItemDecoration(InnerDecorate(context,LinearLayout.VERTICAL))
            getmScroll.adapter=adapter
            getmScroll.itemAnimator=DefaultItemAnimator()
            getmScroll.layoutManager=LinearLayoutManager(context)
//            RefreshAdapterHandler().attachRefreshLayout(refreshlayout,adapter,LinearLayoutManager(context))
        } else {
            adapter?.setList(bean)
            adapter?.notifyDataSetChanged()
//            adapter?.differUpdate(bean)
//            RefreshDifferUtils.differUpdate(bean,refreshlayout)
        }
    }
}