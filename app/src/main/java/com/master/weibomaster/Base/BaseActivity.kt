package com.master.VangeBugs.Base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import com.master.weibomaster.R
import com.master.weibomaster.Rx.Utils.RxLifeUtils
import com.master.weibomaster.Util.ActivityUtils
import com.master.weibomaster.Util.SizeUtils
import com.nestrefreshlib.State.DefaultStateListener
import com.nestrefreshlib.State.StateLayout
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by vange on 2018/1/16.
 */
abstract class BaseActivity : AppCompatActivity(){
    var stateLayout: StateLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.addActivity(this)
        initStateLayout()
        if (needTitle()) {
            setContentView(R.layout.titlebar_activity)
            setSupportActionBar(toolbar)
            fl_content.addView(stateLayout)
            handleTitlebar()
            iv_back.setOnClickListener { onBack() }
        } else {
            setContentView(stateLayout)
        }

        initView()
        loadData()
    }

    private fun initStateLayout() {
        stateLayout = StateLayout(this).setContent(getLayoutId())
        stateLayout?.setStateListener(object : DefaultStateListener() {
            override fun netError(p0: Context?) {
                stateLayout?.showLoading()
                loadData()
            }

        })
    }

    private fun handleTitlebar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val cardview = findViewById<CardView>(R.id.cardview)
            cardview.maxCardElevation = 0f
            cardview.setContentPadding(0, 0, 0, SizeUtils.dp2px(6f))
        }
    }

    abstract fun initView()

    abstract fun loadData()

    abstract fun getLayoutId(): Int

    fun setTitle(title: String) {
        tv_title.text = title
    }

    open fun needTitle(): Boolean {
        return true
    }

    open fun onBack() {
        onBackPressed()
    }

    open fun setMenuClickListener(res: Int, listener: View.OnClickListener) {
        iv_menu.setOnClickListener(listener)
        if (res != 0) {
            iv_menu.setImageResource(res)
        }
        iv_menu.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        ActivityUtils.remove(this)
        RxLifeUtils.getInstance().remove(this)
    }


}