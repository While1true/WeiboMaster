package com.master.VangeBugs.Widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import com.github.chrisbanes.photoview.OnScaleChangedListener
import com.github.chrisbanes.photoview.PhotoView
import com.master.VangeBugs.Util.SizeUtils
import coms.pacs.pacs.Utils.log
import coms.pacs.pacs.Utils.mtoString

/**
 * Created by 不听话的好孩子 on 2018/3/19.
 */
class MyPhotoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PhotoView(context, attrs, defStyleAttr), OnScaleChangedListener {

    var paint: Paint? = null
    var finish = false
    var measureText: Float? = null
    var dy = 0

    init {
        setOnScaleChangeListener(this)
        minimumScale = 0.1f
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.color = 0xffffffff.toInt()
        paint?.textSize = SizeUtils.dp2px(16f).toFloat()
        measureText = paint?.measureText("您可以单击/缩小 返回")
        val fontMetrics = paint?.fontMetrics
        dy = ((fontMetrics!!.ascent - fontMetrics!!.descent!!) / 2 - fontMetrics!!.ascent).toInt()
    }

    override fun onScaleChange(scaleFactor: Float, focusX: Float, focusY: Float) {
        log(scale.mtoString())
        if (scale < 0.15f) {
            if (listener != null) {
                if (!finish) {
                    listener!!.close()
                    finish = true
                }
            }
        }
    }

    var listener: CloseListener? = null
    fun setCloseListener(listener: CloseListener) {
        this.listener = listener
    }

    interface CloseListener {
        fun close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint?.color = 0x60666666
        canvas.drawRect(width / 2 - measureText!! / 2, height - measureText!! / 10, width / 2 + measureText!! / 2, height.toFloat(), paint)
        paint?.color = 0xffffffff.toInt()
        canvas.drawText("您可以单击/缩小返回", width / 2 - measureText!! / 2, (height - measureText!! / 20 + dy).toFloat(), paint)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_POINTER_UP || event?.action == MotionEvent.ACTION_CANCEL) {
            if (scale <= 0.6f) {
                setScale(0.1f, true)
                return true
            } else if (scale < 1f) {
                setScale(1f, true)
                return true
            }

        }
        return super.dispatchTouchEvent(event)
    }
}