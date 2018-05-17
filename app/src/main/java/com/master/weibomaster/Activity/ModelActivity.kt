package com.master.weibomaster.Activity

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.Holder.PatternHolder
import com.master.weibomaster.Model.ChoiceAdapter
import com.master.weibomaster.Model.Pattern
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Util.DeviceUtils
import com.master.weibomaster.Util.PrefUtil
import com.nestrefreshlib.State.DefaultStateListener
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/5/17.
 */
class ModelActivity : BaseActivity() {
    var adapter: ChoiceAdapter? = null
    var pattern: String = ""
    override fun initView() {
        setTitle("设置云图模板")
        pattern= PrefUtil.get("pattern","") as String
        refreshlayout.attrsUtils.overscrolL_ELASTIC = true
        val recyclerView = refreshlayout.getmScroll<RecyclerView>()
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ChoiceAdapter()
                .setChoiceType(1, R.id.mark)
                .setStateListener(object : DefaultStateListener(){
                    override fun netError(p0: Context?) {
                        loadData()
                    }

                })
                .addType(PatternHolder()) as ChoiceAdapter
        recyclerView.adapter = adapter
        adapter?.showLoading()
    }
    fun choseModel(v: View) {
        if(adapter!!.choices.size==0){
            PrefUtil.put("pattern","")
        }else {
            val i = adapter!!.choices[0]
            var list:List<Pattern>?=adapter?.getBeanlist()
            PrefUtil.put("pattern", list!![i].img)
        }
        finish()
    }
    override fun loadData() {
        ApiImpl.apiImpl.getPattern(DeviceUtils.deviceID).subscribe(object : DataObserver<List<Pattern>>(this) {
            override fun OnNEXT(bean: List<Pattern>?) {
                if (bean!!.isEmpty()) {
                    adapter?.showEmpty()
                } else {
                    adapter?.setList(bean)

                    for(i in 0 until bean.size){
                        if(bean[i].img==pattern){
                            adapter?.choices?.add(i)
                            break
                        }
                    }
                    adapter?.showItem()
                }
            }

            override fun OnERROR(error: String?) {
                super.OnERROR(error)
                adapter?.ShowError()
            }

        })
    }

    override fun getLayoutId() = R.layout.model_layout
}