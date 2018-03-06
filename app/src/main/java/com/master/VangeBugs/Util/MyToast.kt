package com.kxjsj.doctorassistant.Utils

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.master.VangeBugs.App
import com.master.VangeBugs.Util.SizeUtils


/**
 * Created by vange on 2017/9/27.
 */

/**
 * toast控件
 */
class MyToast private constructor(context: Context) {

    /**
     * toaste文字控件
     */
    private val textView: TextView

    /**
     * 懒加载toast
     */
    private val toast by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Toast(context) }


    init {
        /**
         * 设置顶部且宽度全屏
         */
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)

        textView = TextView(context)

        /**
         * textview属性
         */
        textView.setBackgroundColor(0xFF79C4A0.toInt())
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        textView.setTextColor((0xffffffff).toInt())
        textView.gravity = Gravity.CENTER
        val padding = SizeUtils.dp2px(15f)
        textView.setPadding(padding,padding,padding,padding)


        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParams

        toast.view = textView

    }

    /**
     * 同伴对象
     */
    companion object {

        /**
         * 懒加载单例
         */
        private val myToast by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MyToast(App.app) }


        /**
         * 显示toast
         * @param text
         * @param during
         */
        fun showToaste(text: CharSequence?, during: Int) {
                myToast.textView.text = text
                myToast.toast.duration = during
                myToast.toast.show()
        }

        /**
         * 在主线程初始化
         * 不然在子线程调用崩溃
         */
        fun init(){myToast.textView.text=""}

        /**
         * 显示toast
         * @param text
         * @param during
         */
        fun showToasteLong(text: CharSequence, during: Int) {
                showToaste(text, during)
            myToast.textView.postDelayed({ myToast.toast.show() }, 3000L)

        }

        /**
         * 取消toast
         */
        fun cancel() {
            myToast.toast.cancel()
        }
    }
}
