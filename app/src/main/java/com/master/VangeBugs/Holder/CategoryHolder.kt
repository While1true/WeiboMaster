package com.master.VangeBugs.Holder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.example.zhouwei.library.CustomPopWindow
import com.master.VangeBugs.Activity.ProgramListActivity
import com.master.VangeBugs.Model.BugCategory
import com.master.VangeBugs.R
import com.master.VangeBugs.Util.ActivityUtils
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.Adpater.Impliment.BaseHolder
import coms.pacs.pacs.Utils.startActivity
import coms.pacs.pacs.Utils.toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 不听话的好孩子 on 2018/2/26.
 */
class CategoryHolder : BaseHolder<BugCategory>(R.layout.category_item) {
    val format=SimpleDateFormat("yyy-MM-dd")
    override fun onViewBind(p0: Holder?, p1: BugCategory?, p2: Int) {
        p0?.setText(R.id.title, p1?.project_name)
//        p0?.setText(R.id.subtitle, "当前待解决Bug数：" + p1?.dcount)
        p0?.setText(R.id.subtitle, p1?.name+"\n"+p1?.url+"\n"+format.format(Date(p1!!.online_date)))
        p0?.itemView?.setOnClickListener {
            val intent = Intent(p0?.itemView.context, ProgramListActivity::class.java)
            intent.putExtra("title", p1?.project_name)
            intent.putExtra("id", p1?.id)
            startActivity(p0?.itemView.context, intent)
        }



        p0?.itemView?.setOnLongClickListener {
            val measuredHeight = p0?.itemView.measuredHeight
            val menu = LayoutInflater.from(ActivityUtils.getTopActivity()).inflate(R.layout.popmenu,null)
            val popmenu=CustomPopWindow.PopupWindowBuilder(ActivityUtils.getTopActivity())
                    .setView(menu)
                    .create()
                    .showAsDropDown(p0?.itemView,0,-measuredHeight/2)

            menu.findViewById<View>(R.id.copy).setOnClickListener {
                copy2Clipboard(p1, p0)
                popmenu.dissmiss()
            }




            true
        }
    }

    private fun copy2Clipboard(p1: BugCategory?, p0: Holder?) {
        val text = p1?.project_name + "\n" + p1?.name + "\n" + p1?.url + "\n" + format.format(Date(p1!!.online_date))
        val clip: ClipboardManager = p0?.itemView?.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clip.primaryClip = ClipData.newPlainText("VangeText", text)
        "已复制到剪切板".toast()
    }
}