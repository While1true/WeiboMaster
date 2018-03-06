package com.master.weibomaster.Activity

import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Base.BaseActivity
import com.master.weibomaster.Fragment.CategoryF
import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.ToDo
import com.master.weibomaster.Model.UPDATE_INDICATE
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Rx.Utils.RxBus
import com.master.weibomaster.Util.StateBarUtils
import kotlinx.android.synthetic.main.category_layout.*

/**
 * Created by 不听话的好孩子 on 2018/2/24.
 */
class CategoryActivity : BaseActivity() {
    val fragments = arrayOf(CategoryF::class.java, CategoryF::class.java)
    override fun initView() {
        setTitle("分类")
        StateBarUtils.performTransStateBar(window)
        var adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = fragments[position].newInstance()

            override fun getCount() = 2
        }
        vp.adapter = adapter


        vp.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    rg_menu.check(R.id.category)
                } else {
                    rg_menu.check(R.id.collect)
                }
            }
        })
        rg_menu.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.category) {
                vp.currentItem = 0
            } else {
                vp.currentItem = 1
            }
        }
        RxBus.getDefault().toObservable(UPDATE_INDICATE, Base::class.java)
                .subscribe({ loadData()})

    }

    override fun loadData() {
        ApiImpl.apiImpl.getToDo()
                .subscribe(object : DataObserver<ToDo>(this){
                    override fun OnNEXT(bean: ToDo?) {
                        collect.indicate=bean?.like__count?:0
                        category.indicate=bean?.count?:0
                    }
                })
    }

    override fun getLayoutId() = R.layout.category_layout
}