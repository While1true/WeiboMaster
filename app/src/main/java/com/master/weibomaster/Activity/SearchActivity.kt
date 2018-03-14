package com.master.weibomaster.Activity

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.CardView
import com.master.VangeBugs.Base.BaseActivity
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Util.InputUtils
import com.master.weibomaster.Util.SizeUtils
import com.master.weibomaster.Util.StateBarUtils
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter
import com.nestrefreshlib.State.Interface.StateEnum
import kotlinx.android.synthetic.main.search_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/16.
 */
class SearchActivity : BaseActivity() {
    var currentPage = 1
    var listArticals = ArrayList<Artical>()
    var content: String = ""
    lateinit var observer : DataObserver<List<Artical>>
    lateinit var sAdapter: StateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        StateBarUtils.performTransStateBar(window)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition=android.transition.Explode()
        }
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val cardview = findViewById<CardView>(R.id.cardview)
            cardview.maxCardElevation = 0f
            cardview.setContentPadding(0, 0, 0, SizeUtils.dp2px(6f))
        }
    }
    override fun initView() {
        iv_back.setOnClickListener {
            InputUtils.hideKeyboard(et_input)
            onBackPressed()
        }
    }



    override fun loadData() {

    }


    override fun getLayoutId(): Int {
        return R.layout.search_activity
    }

    override fun needTitle(): Boolean {
        return false
    }

}