package com.master.VangeBugs.Base

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.Utils.RxLifeUtils
import com.master.VangeBugs.Util.ActivityUtils
import com.master.VangeBugs.Util.SizeUtils
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by vange on 2018/1/16.
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.addActivity(this)
        var view = contentView()
        if (needTitle()) {
            setContentView(R.layout.titlebar_activity)
            setSupportActionBar(toolbar)
            if (view == null)
                layoutInflater.inflate(getLayoutId(), fl_content, true)
            else
                fl_content.addView(view)
            handleTitlebar()
            iv_back.setOnClickListener { onBack() }
        } else {
            if (view == null)
                setContentView(getLayoutId())
            else
                setContentView(view)
        }

        initView()
        loadData()
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

    open fun contentView(): View? = null

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