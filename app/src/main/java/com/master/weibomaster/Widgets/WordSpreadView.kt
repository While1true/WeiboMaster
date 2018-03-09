package com.master.weibomaster.Widgets

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.master.weibomaster.Rx.RxSchedulers
import com.master.weibomaster.Util.SizeUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by 不听话的好孩子 on 2018/3/9.
 */
class WordSpreadView : View, LifecycleObserver {
    var text = ""

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initx(context, attrs)
    }

    private fun initx(context: Context?, attrs: AttributeSet?) {
        text = text.replace("\\pP|\\pS|\\Z", "")

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode != MeasureSpec.EXACTLY) {
            width = Math.min(width, SizeUtils.dp2px(100f))
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = Math.min(height, SizeUtils.dp2px(100f))
        }
        setMeasuredDimension(width, height)
    }

    var subscribe: Disposable? = null
    var list = mutableListOf<Word>()
    var removelist = mutableListOf<Word>()
    var rect: Rect = Rect()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0, 0, w, h)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop() {
        try {
            visibility = INVISIBLE
            subscribe?.dispose()
        } catch (e: Exception) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onstart() {
        if (subscribe == null) {
            dostart()
        }
    }

    private fun dostart() {
        visibility = VISIBLE
        val toCharArray = text.toCharArray()
        subscribe = Observable.interval(30, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.compose())
                .subscribe {
                    for (word in list) {
                        word.increase()
                    }
                    if (it.toInt() % 8 == 0) {
                        var count = 3 + Random().nextInt(3)
                        for (i in 1..count) {
                            val nextInt = Random().nextInt(toCharArray.size)
                            list.add(Word(toCharArray[nextInt].toString(), rect.width() / 2, rect.height() / 2))
                        }
                    }
                    invalidate()
                }
    }

    fun start(): WordSpreadView {
        if (subscribe == null || subscribe!!.isDisposed) {
            dostart()
        }
        return this
    }

    fun addLifeOwner(lifeOwner: LifecycleOwner): WordSpreadView {
        lifeOwner.lifecycle.addObserver(this)
        return this
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (word in list) {
            if (!rect.contains(word.x, word.y)) {
                removelist.add(word)
            }
        }
        list.removeAll(removelist)
        removelist.clear()

        for (word in list) {
            word.draw(canvas)
        }
        if (!list.isEmpty()) {
            val paint = list[0].paint
            val textSize = paint.textSize
            paint.textSize = SizeUtils.dp2px(20f).toFloat()
            canvas.drawText("云端合成中，请稍后...", (width / 2 - paint.measureText("云端合成中，请稍后...") / 2), (height * 2 / 3).toFloat(), paint)
            paint.textSize = textSize
        }
    }


    class Word constructor(var word: String, var x: Int = 0, var y: Int = 0) {
        var colors = intArrayOf(-765666, -141259, -12345273, -14776091, -7461718, -11243910)
        val paint: Paint
        val xspeed: Int
        val yspeed: Int

        init {
            xspeed = RandSpeed()
            yspeed = RandSpeed()
            paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = colors[Random().nextInt(colors.size)]
            val sizeInt = 12 + Random().nextInt(8)
            paint.textSize = SizeUtils.dp2px(sizeInt.toFloat()).toFloat()
        }

        fun draw(canvas: Canvas) {
            canvas.drawText(word, x.toFloat(), y.toFloat(), paint)
        }

        fun increase() {
            x += xspeed
            y += yspeed
        }

        fun RandSpeed(): Int {
            val i = Random().nextInt(30) - 16
            if (i == 0) {
                return RandSpeed()
            }
            return i
        }
    }

}