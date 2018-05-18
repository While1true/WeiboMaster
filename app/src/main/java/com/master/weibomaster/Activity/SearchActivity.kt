package com.master.weibomaster.Activity

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.transition.Fade
import android.view.Window
import android.view.WindowManager
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.Holder.*
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.R
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Util.InputUtils
import com.master.weibomaster.Util.StateBarUtils
import com.nestrefreshlib.State.Interface.StateEnum
import coms.pacs.pacs.Rx.Utils.TextWatcher
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.search_activity.*
import java.util.concurrent.TimeUnit

/**
 * Created by 不听话的好孩子 on 2018/1/16.
 */
class SearchActivity : BaseActivity() {
    /**
     * 输入的搜索词
     */
    var content: String = ""
    /**
     * 子分词
     */
    var words: String = ""
    /**
     * 分类
     */
    var category: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        StateBarUtils.performTransStateBar(window)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = android.transition.Explode()
            window.exitTransition=Fade()
        }
        super.onCreate(savedInstanceState)
        handleTitlebar()
    }

    override fun initView() {
        val category_temp = intent.getStringExtra("category")
        val words = intent.getStringExtra("words")
        if (!TextUtils.isEmpty(category_temp)) {
            category = category_temp.trim()
        }
        if (!TextUtils.isEmpty(words)) {
            this.words = words.trim()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
        iv_back.setOnClickListener {
            InputUtils.hideKeyboard(et_input)
            onBackPressed()
        }
    }


    override fun loadData() {
        val pageLoading = object : RefreshLayoutPageLoading<Artical>(refreshlayout, LinearLayoutManager(this@SearchActivity), true) {
            override fun getObservable() = ApiImpl.apiImpl.getSearchList(category, words + content, DeviceUtils.deviceID, pagenum, pagesize)
        }.AddLifeOwner(this)
                .addType(ArticalListHolder())
                .addType(ArticalListHolder_1())
                .addType(ArticalListHolder2_4())
                .addType(ArticalListHolder_4())
                .addType(ArticalListHolder_6())
        if (!TextUtils.isEmpty(words)) {
            pageLoading.Go()
            et_input.hint = words
        }
        pageLoading.stateAdapter.setLayoutId(StateEnum.SHOW_EMPTY, R.layout.search_empty)
        pageLoading.stateAdapter.showEmpty()
        Observable.create(TextWatcher(et_input))
                .debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    val empty = TextUtils.isEmpty(it)
                    if (empty) {
                        pageLoading.stateAdapter.showEmpty()
                    }
                    !empty
                }.subscribe {
                    content = it
                    pageLoading.RestAndGo()
                }
    }
    override fun getLayoutId(): Int {
        return R.layout.search_activity
    }

    override fun needTitle(): Boolean {
        return false
    }

}