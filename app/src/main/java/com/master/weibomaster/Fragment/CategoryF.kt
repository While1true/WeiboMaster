package com.master.weibomaster.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.master.VangeBugs.Api.ApiImpl
import com.master.weibomaster.Holder.CategoryHolder
import com.master.weibomaster.Model.Category
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import coms.pacs.pacs.BaseComponent.BaseFragment
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryF : BaseFragment() {
    override fun getLayoutId() = R.layout.refreshlayout


    override fun init(savedInstanceState: Bundle?) {
        refreshlayout.attrsUtils.overscrolL_ELASTIC=true
        stateLayout?.showLoading()
        ApiImpl.apiImpl.getCategory()
                .subscribe(object : DataObserver<List<Category>>(this) {
                    override fun OnNEXT(bean: List<Category>) {
                        stateLayout?.showItem()
                        showData(bean)
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        stateLayout?.ShowError()
                    }
                })
    }

    private fun showData(bean: List<Category>) {
        val recyclerview = refreshlayout.getmScroll<RecyclerView>()
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