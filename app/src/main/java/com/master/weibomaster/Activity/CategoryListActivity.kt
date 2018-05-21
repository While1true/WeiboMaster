package com.master.weibomaster.Activity

import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.Holder.*
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.Model.Base
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Util.StateBarUtils
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import com.nestrefreshlib.RefreshViews.RefreshListener
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler
import io.reactivex.Observable
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryListActivity : BaseActivity() {
    var category=""
    var come=""
    override fun initView() {
        StateBarUtils.performTransStateBar(window)
        val category = intent.getStringExtra("category")
        if(category!=null){
            this.category=category
        }
        val come = intent.getStringExtra("come")
        if(come!=null){
            this.come=come
        }
        setTitle(category?:come?:"热门分类")

        setMenuClickListener(R.drawable.ic_search, View.OnClickListener {
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this@CategoryListActivity)
            val intent = Intent(this@CategoryListActivity, SearchActivity::class.java)
            intent.putExtra("category", category)
            intent.putExtra("come", come)
            ActivityCompat.startActivity(this@CategoryListActivity, intent, option.toBundle())

        })
    }

    override fun loadData() {
        object : RefreshLayoutPageLoading<Artical>(refreshlayout, LinearLayoutManager(this@CategoryListActivity), true) {
            override fun getObservable() = ApiImpl.apiImpl.getArticalList(come,category, DeviceUtils.deviceID, pagenum, 18)

        }
                .addType(ArticalListHolder())
                .addType(ArticalListHolder_1())
                .addType(ArticalListHolder2_4())
                .addType(ArticalListHolder_4())
                .addType(ArticalListHolder_6())
                .AddLifeOwner(this).Go()

    }


    override fun getLayoutId() = R.layout.refreshlayout
}