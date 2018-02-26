package com.master.weibomaster.Activity

import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.R
import com.master.weibomaster.Util.StateBarUtils

/**
 * Created by 不听话的好孩子 on 2018/2/24.
 */
class CategoryActivity :BaseActivity() {
    override fun initView() {
        setTitle("分类")
        StateBarUtils.performTransStateBar(window)
    }

    override fun loadData() {
    }

    override fun getLayoutId()= R.layout.category_layout
}