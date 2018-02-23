package com.master.weibomaster.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import com.master.weibomaster.Rx.RxSchedulers
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by 不听话的好孩子 on 2018/2/23.
 */
class BlankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Observable.timer(2, TimeUnit.SECONDS)
                .compose(RxSchedulers.compose())
                .subscribe {
                    val option=ActivityOptionsCompat.makeSceneTransitionAnimation(this@BlankActivity)
                    ActivityCompat.startActivity(this,Intent(this,MainActivity::class.java),option.toBundle())
                    finish()
                }

    }
}