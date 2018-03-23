package com.master.VangeBugs.Activity

import android.Manifest
import android.view.View
import com.master.VangeBugs.Base.BaseActivity
import com.master.VangeBugs.Fragment.CategoryF
import com.master.VangeBugs.R
import com.master.VangeBugs.Util.FragmentUtils
import com.master.VangeBugs.Util.StateBarUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by 不听话的好孩子 on 2018/3/19.
 */
class ProgramActivity :BaseActivity() {
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        setTitle("项目列表")
        RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe {  }
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