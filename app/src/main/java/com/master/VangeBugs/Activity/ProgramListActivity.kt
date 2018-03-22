package com.master.VangeBugs.Activity

import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Base.BaseActivity
import com.master.VangeBugs.Holder.BugListHolder
import com.master.VangeBugs.Holder.RefreshLayoutPageLoading
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.R
import com.master.VangeBugs.Util.StateBarUtils
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class ProgramListActivity : BaseActivity() {
    var titlex = ""
    var id = 0
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        titlex = intent.getStringExtra("title")
        setTitle(titlex)
        id = intent.getIntExtra("id", 0)
    }

    override fun loadData() {
//        refreshlayout.attrsUtils.overscroll = true
        object : RefreshLayoutPageLoading<Bug>(refreshlayout,true) {
            override fun getObservable() =
                    ApiImpl.apiImpl.getBugList(id.toString(), pagenum, pagesize)
        }
                .addType(BugListHolder())
                .Go()
    }

    override fun getLayoutId() = R.layout.refreshlayout
}