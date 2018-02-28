package com.master.VangeBugs.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Holder.BugListHolder
import com.master.VangeBugs.Holder.CategoryHolder
import com.master.VangeBugs.Model.Base
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.Model.BugCategory
import com.master.VangeBugs.Model.UPDATE_INDICATE
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.DataObserver
import com.master.VangeBugs.Rx.MyObserver
import com.master.VangeBugs.Rx.Utils.RxBus
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import com.nestrefreshlib.Adpater.Impliment.SAdapter
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

    override fun contentView(): View? {
        stateLayout = StateLayout(context)
        stateLayout?.setStateListener(object : DefaultStateListener() {
            override fun netError(p0: Context?) {
                init(null)
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
                .subscribe(object : DataObserver<List<Bug>>(this) {
                    override fun OnNEXT(bean: List<Bug>) {
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
    private fun showData(bean: List<Bug>) {
        if (adapter == null) {
            adapter = SAdapter(bean)
                    .apply {
                        addType(BugListHolder())
                        addLifeOwener { lifecycle }
                    }
            refreshlayout.setAdapter(adapter)
            refreshlayout.getmScroll<RecyclerView>().itemAnimator = DefaultItemAnimator()
        } else {
            adapter!!.differUpdate(bean)
        }
    }
}