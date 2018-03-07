package com.master.weibomaster.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RadioButton;
import android.widget.TextView;

import com.master.weibomaster.R;


/**
 * Created by vange on 2017/9/21.
 */

public class IndicateTextView extends android.support.v7.widget.AppCompatTextView {
    int indicateColor, indicateTextColor, indicate, max = 99999;
    float indicateRadius, indicatesize;
    Paint paint;

    public IndicateTextView(Context context) {
        this(context, null);
    }

    public IndicateTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicateTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttrs(attrs);
    }

    private void obtainAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IndicateView);
        indicateColor = typedArray.getColor(R.styleable.IndicateView_indicateColor, 0xffff4070);
        indicateTextColor = typedArray.getColor(R.styleable.IndicateView_indicateTextColor, 0xffffffff);
        indicate = typedArray.getInt(R.styleable.IndicateView_indicate, 0);
        indicateRadius = typedArray.getFloat(R.styleable.IndicateView_indicateRadius, dp2px(8));
        indicatesize = typedArray.getFloat(R.styleable.IndicateView_indicatesize, dp2px(9));
        max = typedArray.getInt(R.styleable.IndicateView_maxnum, 99999);
        typedArray.recycle();
    }

    public float dp2px(float v) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, getResources().getDisplayMetrics());
    }

    RectF rect = new RectF();
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (indicate == 0) {
            return;
        }
        if (paint == null) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        paint.setColor(indicateColor);
        int width = getWidth();


        /**
         * 大于最大值画一个圆
         */
        if (indicate > max) {
            canvas.drawCircle(Math.min(width - indicateRadius/2, width / 2 + getCompoundDrawables()[1].getIntrinsicWidth()/2 - indicateRadius), indicateRadius/2, indicateRadius / 2, paint);
            return;
        }
        /**
         * 右上角
         * draw背景
         */
        paint.setTextSize(indicatesize);
        float v = paint.measureText(indicate + "");
        float min = Math.min(width - v - indicateRadius * 2, width / 2 + getCompoundDrawables()[1].getIntrinsicWidth()/2 - indicateRadius);
        rect.set(min, 0, min+v+2*indicateRadius, indicateRadius * 2);
        canvas.drawRoundRect(rect, indicateRadius, indicateRadius, paint);
        /**
         * 画数字
         */
        paint.setColor(indicateTextColor);

        Paint.FontMetrics fm = paint.getFontMetrics();
        float baseLineY = indicateRadius -(fm.ascent - (fm.ascent - fm.descent) / 2);
        canvas.drawText(indicate + "", Math.min(width - v - indicateRadius,width/2+getCompoundDrawables()[1].getIntrinsicWidth()/2), baseLineY, paint);

    }


    public int getIndicateColor() {
        return indicateColor;
    }

    public void setIndicateColor(int indicateColor) {
        this.indicateColor = indicateColor;
        invalidate();
    }

    public int getIndicateTextColor() {
        return indicateTextColor;
    }

    public void setIndicateTextColor(int indicateTextColor) {
        this.indicateTextColor = indicateTextColor;
        invalidate();
    }

    public int getIndicate() {
        return indicate;
    }

    public void setIndicate(int indicate) {
        this.indicate = indicate;
        invalidate();
    }

    public float getIndicateRadius() {
        return indicateRadius;
    }

    public void setIndicateRadius(float indicateRadius) {
        this.indicateRadius = indicateRadius;
        invalidate();
    }

    public float getIndicatesize() {
        return indicatesize;
    }

    public void setIndicatesize(float indicatesize) {
        this.indicatesize = indicatesize;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
