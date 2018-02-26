package com.master.weibomaster.Activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.Fragment.CategoryF
import com.master.weibomaster.R
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

    }

    override fun loadData() {
    }

    override fun getLayoutId() = R.layout.category_layout
}