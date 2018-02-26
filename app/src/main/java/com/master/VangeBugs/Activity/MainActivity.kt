package com.master.VangeBugs.Activity

import android.content.Intent
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import com.master.VangeBugs.Base.BaseActivity
import com.master.VangeBugs.R
import com.master.VangeBugs.Rx.MyObserver
import com.master.VangeBugs.Rx.RxSchedulers
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.exitTransition=android.transition.Explode()
        }

        var text="""
            I leave uncultivated today,
            was precisely yesterday perishes tomorrow which person of the body implored

             """.trimIndent()
        tv.text = text
        val split = text.split(" ")
        val size = split.size
        Observable.interval(1000,800,TimeUnit.MILLISECONDS)
                .take(size.toLong())
                .compose(RxSchedulers.compose())
                .subscribe(object : MyObserver<Long>(this){
                    override fun onNext(t: Long) {
                        super.onNext(t)

                        val indexOf = text.indexOf(split[t.toInt()%size])
                        val textAppearanceSpan = TextAppearanceSpan(this@MainActivity, R.style.textappearance)
                        var spann=SpannableString(text)
                        spann.setSpan(textAppearanceSpan,indexOf,indexOf+split[t.toInt()%size].length,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        tv.text=spann
                    }
                })
        next.setOnClickListener { startActivity(Intent(this,CategoryActivity::class.java)) }
    }

    override fun loadData() {
    }

    override fun getLayoutId()= R.layout.activity_main

    override fun needTitle(): Boolean {
        return false
    }

}
