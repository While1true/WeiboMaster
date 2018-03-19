package com.master.VangeBugs.Fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.R
import com.nestrefreshlib.Adpater.Base.MyCallBack
import coms.pacs.pacs.BaseComponent.BaseFragment
import kotlinx.android.synthetic.main.textview.*

/**
 * Created by 不听话的好孩子 on 2018/2/28.
 */
class DetailF : BaseFragment() {

    private var bug: Bug? = null
    var callback: MyCallBack<Boolean>? = null


    override fun getLayoutId() = R.layout.textview

    override fun init(savedInstanceState: Bundle?) {
        setTitle("Bug详情")
        textview.movementMethod = ScrollingMovementMethod.getInstance()
        show(bug!!)
    }

    fun display(bug: Bug?):DetailF {
        this.bug = bug
        return this
    }

    override fun needTitle(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        callback=null
    }
    private fun show(bug: Bug) {
    }
}