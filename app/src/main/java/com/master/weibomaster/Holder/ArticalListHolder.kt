package com.master.weibomaster.Holder

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.master.weibomaster.Api.ApiImpl
import com.master.weibomaster.Activity.WebViewActivity
import com.master.weibomaster.Fragment.WordCloudF
import com.master.weibomaster.Model.Artical
import com.master.weibomaster.Model.Base
import com.master.weibomaster.Model.UPDATE_INDICATE
import com.master.weibomaster.R
import com.master.weibomaster.Rx.DataObserver
import com.master.weibomaster.Rx.Utils.RxBus
import com.master.weibomaster.Rx.Utils.RxLifeUtils
import com.master.weibomaster.Util.*
import com.master.weibomaster.Widgets.IndicateTextView
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import com.nestrefreshlib.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Utils.log
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.toast

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
open class ArticalListHolder : BaseHolder<Artical>(R.layout.artical_list_layout) {
    companion object Poll {
        val onePoll: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
        val eachwidth = ActivityUtils.getTopActivity().resources.displayMetrics.widthPixels - SizeUtils.dp2px(32f + 16f)
    }

    override fun onViewBind(p0: Holder?, p1: Artical?, p2: Int) {
        p0?.setText(R.id.content, p1?.content)
        val collect = p0?.getView<IndicateTextView>(R.id.collect)
        var islike = (p1!!.is_like == 1)
        collect?.isSelected = islike
        collect?.text = if (islike) "已收藏" else "收藏"
        collect?.indicate = p1?.like_count
        p0?.setText(R.id.time, p1?.timestr)
        p0?.setText(R.id.come, p1?.come)

        p0?.setOnClickListener(R.id.share, {
            val text = p1?.content + "\n" + p1?.come + "\n" + p1?.timestr
            FileUtils.sendText(ActivityUtils.getTopActivity(), text)
        })

        p0?.setOnClickListener(R.id.cloud, {
            val fragment = WordCloudF()
            fragment.artical = p1
            FragmentUtils.showAddFragment(ActivityUtils.getTopActivity(), fragment)
        })

        p0?.setOnClickListener(R.id.content, {
            goOriginalWeb(p1)
        })

        p0?.setOnClickListener(R.id.net, {
            goOriginalWeb(p1)
        })
        collect?.setOnClickListener {
            doCollect(p0, p1, collect, p2)
        }

        showImgs(p1, p0)
    }

    private fun goOriginalWeb(p1: Artical?){
        val intent = Intent(ActivityUtils.getTopActivity(), WebViewActivity::class.java)
        intent.putExtra("url", p1?.href)
        intent.putExtra("fid", p1?.href)
        ActivityUtils.getTopActivity().startActivity(intent)
    }

    private fun showImgs(p1: Artical, p0: Holder?) {
        val split = p1.imgs?.split(";")
        val recyclerview = p0?.getView<RecyclerView>(R.id.refreshlayout)

        if (split != null && split.isNotEmpty()) {
            recyclerview?.visibility = View.VISIBLE
            var spancount = 2
            if (split.size == 1) {
                recyclerview?.layoutManager = LinearLayoutManager(ActivityUtils.getTopActivity())
            } else if (split.size <= 4) {
                recyclerview?.layoutManager = GridLayoutManager(ActivityUtils.getTopActivity(), 2)
            } else {
                spancount = 3
                recyclerview?.layoutManager = GridLayoutManager(ActivityUtils.getTopActivity(), 3)
            }
            recyclerview?.recycledViewPool = onePoll
            log(onePoll.hashCode().toString())
            recyclerview?.adapter = SAdapter(split.size)
                    .addLifeOwener(ActivityUtils.getTopActivity())
                    .addType(R.layout.imageview_, object : PicHolder(spancount, split) {
                        override fun istype(p0: Int) = split.size == 1
                    })
                    .addType(R.layout.imageview_, object : PicHolder(spancount, split) {
                        override fun istype(p0: Int) = split.size <= 4
                    })
                    .addType(R.layout.imageview_, object : PicHolder(spancount, split) {
                        override fun istype(p0: Int) = true
                    })
        } else {
            recyclerview?.visibility = View.GONE
        }
    }


    private fun doCollect(p0: Holder?, p1: Artical, collect: IndicateTextView?, p2: Int) {

        if (p1!!.is_like == 0) {
            collectNet(p1, p0, collect, p2)
        } else {
            AlertDialog.Builder(p0!!.itemView.context)
                    .setTitle(if (p1!!.is_like == 1) "取消收藏该博文？" else "收藏该博文？")
                    .setMessage(if (p1!!.is_like == 1) "将从收藏页移除" else "将添加在收藏页方便查看")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", { dialog, which ->
                        collectNet(p1, p0, collect, p2)
                    }).create().show()
        }

    }

    private fun collectNet(p1: Artical, p0: Holder?, collect: IndicateTextView?, p2: Int) {
        ApiImpl.apiImpl.like(p1?.id.mtoString(), (p1?.is_like + 1) % 2, DeviceUtils.getUniqueId(ActivityUtils.getTopActivity()))
                .subscribe(object : DataObserver<Any>(p0?.itemView) {
                    override fun onComplete() {
                        super.onComplete()
                        RxLifeUtils.getInstance().remove(p0?.itemView)
                    }

                    override fun OnNEXT(bean: Any?) {
                        p1.is_like = (p1.is_like + 1) % 2
                        collect?.text = if (p1.is_like == 1) "已收藏" else "收藏"
                        collect?.isSelected = p1.is_like == 1
                        collect!!.indicate = collect!!.indicate + (if (p1.is_like == 1) 1 else -1)
                        RxBus.getDefault().post(Base(code = UPDATE_INDICATE, data = p2))
                        val toaste = if (p1.is_like == 1) "收藏成功" else "取消收藏成功"
                        toaste.toast()
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        error.mtoString().toast()
                    }

                })
    }

    override fun istype(item: Any?, position: Int): Boolean {
        val split = (item as Artical).imgs?.split(";")
        return split == null || split.isEmpty()
    }

    private fun getissue(issue: String?): CharSequence? {
        if (issue.mtoString().length > 10) {
            return issue?.substring(0, 10)
        } else {
            return issue
        }
    }
}