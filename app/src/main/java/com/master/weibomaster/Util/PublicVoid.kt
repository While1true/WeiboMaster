package coms.pacs.pacs.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.TypedValue
import com.kxjsj.doctorassistant.Utils.MyToast
import com.master.weibomaster.App
import com.master.weibomaster.Util.FragmentUtils
import com.master.weibomaster.Util.K2JUtils
import com.master.weibomaster.Util.PrefUtil

/**
 * Created by vange on 2018/1/16.
 */

fun startActivity(context: Context, clazz: Class<out Activity>) {
    context.startActivity(Intent(context, clazz))
}


/**
 * 空也可toString
 */
fun Any?.mtoString(): String {
    if (this == null)
        return "null"
    else
        return toString()
}

/**
 * toast
 * 到处吐司？
 */
fun Any?.toast(charSequence: CharSequence, int: Int) {
    MyToast.showToaste(charSequence, int)
}

/**
 * toast
 * 重载
 * 到处吐司？
 */
fun Any?.toast() {
    MyToast.showToaste(mtoString(), 1)
}


/**
 * dp2px
 */
fun Any?.dp2px(dp: Float): Int {
    return (0.5f + TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            App.app.resources.displayMetrics)).toInt()
}


fun Any?.save(key: String) {
    PrefUtil.put(key, mtoString())
}

/**
 * 打印log
 */
fun Any.log(charSequence: CharSequence) {
    K2JUtils.log(javaClass.simpleName, charSequence)
}

fun FragmentActivity.showAddFragment(fragment: Fragment) {
    FragmentUtils.showAddFragment(this, fragment)
}

fun FragmentActivity.showReplaceFragment(fragment: Fragment) {
    FragmentUtils.showReplaceFragment(this, fragment)
}

fun Fragment.showReplaceFragment(fragment: Fragment) {
    FragmentUtils.showFragmentReplaceFragment(this, fragment)
}

fun Fragment.showAddFragment(fragment: Fragment) {
    FragmentUtils.showFragmentAddFragment(this, fragment)
}

fun Fragment.pop(): Boolean {
    return fragmentManager.popBackStackImmediate()
}

fun FragmentActivity.pop(): Boolean {
    return supportFragmentManager.popBackStackImmediate()
}