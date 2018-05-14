package com.master.weibomaster.Activity

import android.content.Intent
import android.support.test.internal.util.LogUtil
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.Fragment.CategoryF
import com.master.weibomaster.Fragment.LikeF
import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.Statistic
import com.master.weibomaster.Model.ToDo
import com.master.weibomaster.Model.UPDATE_INDICATE
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Rx.Utils.RxBus
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Util.StateBarUtils
import com.update.UpdateBean
import com.update.UpdateUtils
import coms.pacs.pacs.Utils.pop
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.category_layout.*

/**
 * Created by 不听话的好孩子 on 2018/2/24.
 */
class CategoryActivity : BaseActivity() {
    val fragments = arrayOf(CategoryF::class.java, LikeF::class.java)
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
                .subscribe({ loadData() })

        ApiImpl.apiImpl.update().filter {
            Integer.parseInt(it?.data.updateNumber) > packageManager.getPackageInfo(packageName, 0).versionCode
        }.subscribe(object : DataObserver<UpdateBean>(this@CategoryActivity) {
                    override fun OnNEXT(bean: UpdateBean?) {
                        UpdateUtils.checkUpdate(this@CategoryActivity, bean, { _, _: Int ->
                            ApiImpl.apiImpl.downloadstatistic(DeviceUtils.deviceID)
                                    .subscribe(object : DataObserver<Statistic>(this@CategoryActivity) {
                                        override fun OnNEXT(bean: Statistic) {
                                            LogUtil.logDebug("下载统计", "下载统计成功")
                                        }

                                    })
                        })
                    }
                })

    }

    override fun loadData() {
        ApiImpl.apiImpl.getToDo(DeviceUtils.deviceID)
                .subscribe(object : DataObserver<ToDo>(this) {
                    override fun OnNEXT(bean: ToDo?) {
                        collect.indicate = bean?.like__count ?: 0
                        category.indicate = bean?.count ?: 0
                    }
                })
    }

    override fun getLayoutId() = R.layout.category_layout


    var last = 0L
    override fun onBackPressed() {
        if (!pop()) {
            val currentTimeMillis = System.currentTimeMillis()

            if (currentTimeMillis - last > 2000) {
                last = currentTimeMillis
                toast("双击退出", 1)
            } else {
                finish()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_search -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this@CategoryActivity)
                ActivityCompat.startActivity(this@CategoryActivity, Intent(this@CategoryActivity, SearchActivity::class.java), option.toBundle())
            }
            R.id.science -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this@CategoryActivity)
                ActivityCompat.startActivity(this@CategoryActivity, Intent(this@CategoryActivity, ScienceActivity::class.java), option.toBundle())
            }
        }
        return true
    }
}