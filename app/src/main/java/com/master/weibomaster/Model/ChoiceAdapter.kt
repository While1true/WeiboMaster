package com.master.weibomaster.Model

import android.view.View
import com.nestrefreshlib.Adpater.Base.Holder
import com.nestrefreshlib.RefreshViews.AdapterHelper.StateAdapter

/**
 * Created by 不听话的好孩子 on 2018/5/17.
 */
class ChoiceAdapter : StateAdapter {
    var choiceSize = 1
    var selectionId = -1
    var choices = arrayListOf<Int>()
    var choiceItemClickListener:OnChoiceItemClick?=null

    constructor(list: MutableList<*>?) : super(list)
    constructor(count: Int) : super(count)
    constructor() : super()

    fun setChoiceType(choiseSize: Int, selectionId: Int): ChoiceAdapter {
        this.choiceSize = choiceSize
        this.selectionId = selectionId
        return this
    }

    override fun onBindView(holder: Holder, t: Any?, positon: Int) {
        val view = holder.getView<View>(selectionId)
        if(view!=null) {
            view.isSelected = choices.contains(positon) && selectionId != -1
            holder.itemView.setOnClickListener {
                choiceItemClickListener?.onClick(holder,t,positon)
                if (choices.contains(positon)) {
                    choices.remove(positon)
                } else {
                    if (choices.size == choiceSize) {
                        choices.removeAt(0)
                        choices.add(positon)
                        notifyDataSetChanged()
                        return@setOnClickListener
                    }
                    choices.add(positon)
                }
                view.isSelected = choices.contains(positon) && selectionId != -1
            }
        }
        super.onBindView(holder, t, positon)
    }

    interface OnChoiceItemClick{
       fun onClick(holder: Holder, t: Any?, positon: Int)
    }


}
