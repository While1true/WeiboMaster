package com.master.VangeBugs.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import com.master.VangeBugs.Rx.RxSchedulers
import com.master.VangeBugs.Util.StateBarUtils
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by 不听话的好孩子 on 2018/2/23.
 */
class BlankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        StateBarUtils.performTransStateBar(window)
        super.onCreate(savedInstanceState)
        Observable.timer(2, TimeUnit.SECONDS)
                .compose(RxSchedulers.compose())
                .subscribe {
                    startActivity(Intent(this,ProgramActivity::class.java))
                    finish()
                }

    }
}