package com.master.weibomaster.Fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Holder.CategoryHolder
import com.master.weibomaster.Model.Category
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.master.weibomaster.Base.BaseFragment
import com.master.weibomaster.Holder.LabelHolder
import com.master.weibomaster.Model.Category_Second
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryF : BaseFragment() {
    override fun loadData() {
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

    override fun getLayoutId() = R.layout.refreshlayout


    override fun init(savedInstanceState: Bundle?) {
        refreshlayout.attrsUtils.overscrolL_ELASTIC=true
        refreshlayout.setBackgroundColor(0xfffCfCfC.toInt())
        stateLayout?.showLoading()
    }

    private fun showData(bean: List<Category>) {
        var cats=arrayListOf<Any>()
        for ( item in bean){
            cats.add(item)
            val words = item.wordsTop10.split(";")
            for (word in words){
                val wods = word.split(",")
                val cat=Category_Second(wods[0],wods[1].toInt(),item.category)
                cats.add(cat)
            }
        }
        val recyclerview = refreshlayout.getmScroll<RecyclerView>()
        val adapterx = SAdapter(cats)
                .apply {
                    addType(CategoryHolder())
                    addType(LabelHolder())
                    addLifeOwener { lifecycle }
                }
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        recyclerview?.apply {
            layoutManager =flexboxLayoutManager
            adapter = adapterx
        }

    }
}