package com.master.VangeBugs.Activity

import android.view.View
import com.master.VangeBugs.Base.BaseActivity
import com.master.VangeBugs.Fragment.CategoryF
import com.master.VangeBugs.R
import com.master.VangeBugs.Util.FragmentUtils
import com.master.VangeBugs.Util.StateBarUtils
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by 不听话的好孩子 on 2018/3/19.
 */
class ProgramActivity :BaseActivity() {
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        setTitle("项目列表")
        iv_back.visibility= View.INVISIBLE
        FragmentUtils.showAddFragment(this, R.id.fl_content,CategoryF())
    }

    override fun loadData() {
    }

    override fun onBackPressed() {
       finish()
    }

    override fun getLayoutId()=0

}