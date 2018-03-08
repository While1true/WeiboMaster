package com.master.weibomaster.Widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by vange on 2017/12/22.
 */

public class SmartImageView extends android.support.v7.widget.AppCompatImageView {
    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable drawable = getDrawable();
        if(drawable !=null){
            int w=drawable.getIntrinsicWidth();
            int h=drawable.getIntrinsicHeight();
            int measuredWidth = getMeasuredWidth();
            int heightSmart=h*measuredWidth/w;
            setMeasuredDimension(measuredWidth,heightSmart);
        }

    }
}
