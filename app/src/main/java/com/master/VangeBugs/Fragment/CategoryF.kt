package com.master.VangeBugs.Fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Holder.CategoryHolder
import com.master.VangeBugs.Model.BugCategory
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.DataObserver
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.State.StateLayout
import coms.pacs.pacs.BaseComponent.BaseFragment

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryF : BaseFragment() {
    override fun getLayoutId() = R.layout.recyclerview

    private var stateLayout: StateLayout? = null

    override fun contentView(): View? {
        stateLayout = StateLayout(context)
        return stateLayout?.setContent(getLayoutId())
    }

    override fun init(savedInstanceState: Bundle?) {
        stateLayout?.showLoading()
        ApiImpl.apiImpl.getProgress()
                .subscribe(object : DataObserver<List<BugCategory>>(this) {
                    override fun OnNEXT(bean: List<BugCategory>) {
                        stateLayout?.showItem()
                        showData(bean)
                    }
                })
    }

    private fun showData(bean: List<BugCategory>) {
        val recyclerview = contentView?.findViewById<RecyclerView>(R.id.recyclerview)
        val adapterx = SAdapter(bean)
                .apply {
                    addType(CategoryHolder())
                    addLifeOwener { lifecycle }
                }
        recyclerview?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterx
        }

    }
}