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
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import kotlinx.android.synthetic.main.search_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/16.
 */
class SearchActivity : BaseActivity() {
    var currentPage = 1
    var listArticals = ArrayList<Artical>()
    var content: String = ""
    lateinit var observer : DataObserver<List<Artical>>
    lateinit var sAdapter: SAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_search,menu!!)
//        val item = menu.findItem(R.id.toolbar_search)
//        item.expandActionView()
//        searchView = item.actionView as SearchView
//        searchView?.queryHint="姓名/医保卡号/就诊号"
//        val searchtext = searchView?.findViewById<SearchView.SearchAutoComplete>(android.support.v7.appcompat.R.id.search_src_text)
//        searchtext?.setHintTextColor(resources.getColor(R.color.hintcolor))
//        searchtext?.setTextColor(0xffffffff.toInt())
//        val mCloseButton = findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
//        mCloseButton.setOnClickListener {  }
//
//        Observable.create(SearchWatcher(searchView!!))
//                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .filter({
//                    content = it
//                    if(TextUtils.isEmpty(content)) {
//                        sAdapter.setBeanList(null)
//                        listArticals.clear()
//                        sAdapter.showItem()
//                    }
//                    return@filter !TextUtils.isEmpty(content)
//                })
//                .flatMap(Function<String, Observable<Base<List<Artical>>>> {
//                    currentPage = 1
//                    return@Function ApiImpl.apiImpl.getArticalList(it, 20, currentPage)
//                })
//                .subscribe(observer)
//
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        val searchView = item.actionView as SearchView
//
//        return true
//    }
}