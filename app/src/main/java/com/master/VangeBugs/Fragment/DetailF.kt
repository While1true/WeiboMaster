package com.master.VangeBugs.Fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import com.master.VangeBugs.Api.ApiImpl
import com.master.VangeBugs.Model.Base
import com.master.VangeBugs.Model.Bug
import com.master.VangeBugs.Model.UPDATE_INDICATE
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.DataObserver
import com.master.VangeBugs.Rx.Utils.RxBus
import com.nestrefreshlib.Adpater.Base.MyCallBack
import coms.pacs.pacs.BaseComponent.BaseFragment
import coms.pacs.pacs.Utils.toast
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
        like.setOnClickListener {
            like.isEnabled=false
            ApiImpl.apiImpl.like(bug!!.id,(bug!!.like+1)%2)
                    .subscribe(object : DataObserver<Any>(this){
                        override fun OnNEXT(bean: Any?) {
                            RxBus.getDefault().post(Base(code = UPDATE_INDICATE, data = ""))
                            bug!!.like=(bug!!.like+1)%2
                            callback?.call(bug!!.like==1)
                            like.isSelected = bug!!.like==1
                            if( bug!!.like==1){
                                "已设为重点关注".toast()
                            }else{
                                "取消关注成功".toast()
                            }
                            like.text = if(bug!!.like==1)"已关注" else "关注"
                        }

                        override fun onComplete() {
                            super.onComplete()
                            like.isEnabled=true
                        }

                    })
        }
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
        val text = """

                标题：${bug.title}
                项目：${bug.category}
                发布者：${bug.publisher}
                发布时间:${bug.time}
                状态：${if (bug.state == 1) "已解决" else "待解决"}

                问题：${bug.issue}


            """.trimIndent()
        textview.setTextSize(TypedValue.COMPLEX_UNIT_SP,22f)
        like.isSelected = bug.like==1
        like.text = if(bug.like==1)"已关注" else "关注"
        textview.text = text
    }
}