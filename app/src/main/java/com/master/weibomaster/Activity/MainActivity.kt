package com.master.weibomaster.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SynthesizerListener
import com.master.weibomaster.Base.BaseActivity
import com.master.weibomaster.R
import com.master.weibomaster.Rx.MyObserver
import com.master.weibomaster.Rx.RxSchedulers
import com.master.weibomaster.Rx.Utils.RxLifeUtils
import com.master.weibomaster.Util.K2JUtils
import com.master.weibomaster.Voice.XFVoice
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), SynthesizerListener {
    override fun onBufferProgress(p0: Int, p1: Int, p2: Int, p3: String?) {
    }

    override fun onSpeakBegin() {
    }

    override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
    }

    override fun onSpeakPaused() {
    }

    override fun onSpeakResumed() {
    }

    override fun onCompleted(p0: SpeechError?) {
        K2JUtils.toast("播报进度："+100.toString())
    }

    override fun onSpeakProgress(p0: Int, p1: Int, p2: Int) {
        K2JUtils.toast("播报进度："+p0.toString())
    }

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
        XFVoice.readerString(text,this)
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

    override fun onStop() {
        super.onStop()
        XFVoice.mTts.stopSpeaking()
    }
    override fun onDestroy() {
        super.onDestroy()
        XFVoice.mTts.destroy()
    }

}
