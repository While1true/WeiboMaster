package coms.pacs.pacs.BaseComponent

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.master.weibomaster.R
import com.master.weibomaster.Rx.Utils.RxLifeUtils
import com.master.weibomaster.Util.SizeUtils
import com.master.weibomaster.Util.StateBarUtils
import com.nestrefreshlib.State.StateLayout
import kotlinx.android.synthetic.main.titlebar_fragment.*

/**
 * Created by vange on 2018/1/16.
 */
abstract class BaseFragment : Fragment() {
    private var firestLoad = true
    private var viewCreated = false
    private var isvisable = false
    var stateLayout: StateLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var inflate: View? = null
        stateLayout = StateLayout(context).setContent(getLayoutId())
        if (needTitle()) {
             inflate = layoutInflater.inflate(R.layout.titlebar_fragment, container, false)
            val viewGroup = inflate!!.findViewById(R.id.fl_content) as ViewGroup
            viewGroup.addView(stateLayout)
            inflate!!.findViewById<View>(R.id.iv_back).setOnClickListener { onBack() }
            handleTitlebar(inflate)
        } else {
            inflate = stateLayout
        }
        inflate?.isClickable = true
        return inflate
    }

    private fun handleTitlebar(inflate: View) {
        inflate.findViewById<View>(R.id.extraspace).layoutParams.height = StateBarUtils.getStatusBarHeight(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val cardview = inflate.findViewById<CardView>(R.id.cardview)
            cardview.maxCardElevation = 0f
            cardview.setContentPadding(0, 0, 0, SizeUtils.dp2px(6f))
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        viewCreated = true
        if (firestLoad && isvisable) {
            firestLoad = false
            loadLazy()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isvisable = isVisibleToUser
        if (isvisable && viewCreated && firestLoad) {
            firestLoad = false
            loadLazy()
        }
    }

    abstract fun getLayoutId(): Int
    abstract fun init(savedInstanceState: Bundle?)

    open fun loadLazy() {}

    open fun contentView(): View? = null
    open fun needTitle(): Boolean {
        return false
    }

    open fun onBack() {
        fragmentManager.popBackStack()
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    open fun setMenuClickListener(res: Int, listener: View.OnClickListener) {
        iv_menu.setOnClickListener(listener)
        if (res != 0) {
            iv_menu.setImageResource(res)
        }
        iv_menu.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        RxLifeUtils.getInstance().remove(this)
    }

    fun stop() {
        fragmentManager.popBackStack()
    }

}