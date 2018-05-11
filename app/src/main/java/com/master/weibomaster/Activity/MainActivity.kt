package com.master.weibomaster.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Environment
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.R
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.master.weibomaster.Fragment.WordCloudF
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.Model.Statistic
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Rx.Net.RetrofitHttpManger
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Util.FileUtils
import com.master.weibomaster.Util.FragmentUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.PositionHolder
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Utils.toast
import java.io.File


class MainActivity : BaseActivity() {

    var bean: Artical? = null
    override fun initView() {
        RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET).subscribe {
            if (it) {
                dowhatyouwant()
            } else {
                "缺少权限".toast()
                finish()
            }
        }

        next.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
            finish()
        }
        share.setOnClickListener {
            FileUtils.sendView(refreshlayout, bean?.content + ".jpg", "image/jpeg")
        }
        stateLayout?.showLoading()
        ApiImpl.apiImpl.statistics(DeviceUtils.deviceID)
                .subscribe(object :DataObserver<Statistic>(this){
                    override fun OnNEXT(bean: Statistic) {

                       AlertDialog.Builder(this@MainActivity)
                               .setTitle("登录提醒")
                               .setMessage("历史登录次数：${bean.count} \n上次ip:${bean?.ip?:""}\n上次登录时间：${bean.lasttime}")
                               .setPositiveButton("知道了",{_,_->})
                               .create()
                               .show()


                    }

                })
    }

    private fun dowhatyouwant() {
//        try {
//            val file = Environment.getExternalStorageDirectory()
//            val IpConfig = File(file, "config.txt")
//            val readLine = IpConfig.readLines()
//
//            if (readLine.isNotEmpty() && !TextUtils.isEmpty(readLine[0].trim())) {
//                RetrofitHttpManger.setBASEURL("http://${readLine[0]}/masterWeiBo/")
//            }
//        } catch (e: Exception) {
//        }

        ApiImpl.apiImpl.latestSplash().subscribe(object : DataObserver<Artical>(this) {
            override fun OnNEXT(bean: Artical) {
                this@MainActivity.bean = bean
                stateLayout?.showItem()
                val split = bean.imgs?.split(";")
                if (split != null && split.isNotEmpty()) {
                    showImgs(split, bean)
                } else {
                    showcloud(bean)
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                stateLayout?.showItem()
                startActivity(Intent(this@MainActivity, CategoryActivity::class.java))
                finish()
            }

        })
    }

    override fun loadData() {

    }

    private fun showcloud(bean: Artical) {
        val wordCloudF = WordCloudF()
        wordCloudF.artical = bean
        FragmentUtils.showAddFragment(this, R.id.content, wordCloudF)
    }

    private fun showImgs(asList: List<String>, bean: Artical) {
        val recyclerview = refreshlayout.getmScroll<RecyclerView>()
        if (asList.size == 1) {
            recyclerview.layoutManager = LinearLayoutManager(this)
        } else {
            recyclerview.layoutManager = GridLayoutManager(this, 2)
        }
        recyclerview.adapter = SAdapter(asList.size + 1)
                .addLifeOwener(this)
                .addType(R.layout.imageview, object : PositionHolder() {
                    override fun onBind(p0: Holder?, p1: Int) {
                        Glide.with(this@MainActivity)
                                .load(if (asList[p1].startsWith("http")) asList[p1] else ("https:" + asList[p1]))
                                .into(p0!!.getView<ImageView>(R.id.image))
                    }

                    override fun istype(p0: Int) = p0 < asList.size
                }
                ).addType(R.layout.textview, object : PositionHolder() {
            override fun istype(p0: Int) = true

            override fun gridSpanSize(item: Any?, position: Int): Int {
                return 2
            }


            override fun onBind(p0: Holder?, p1: Int) {
                p0?.setText(R.id.textview, bean.content + "\n" + bean.timestr + " [常观世音] ")
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun getLayoutId() = R.layout.activity_main

    override fun needTitle(): Boolean {
        return false
    }


}
