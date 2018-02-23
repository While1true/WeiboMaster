package com.master.weibomaster.Util;

import android.util.TypedValue;

import com.master.weibomaster.App;


/**
 * Created by vange on 2017/10/11.
 */

public class SizeUtils {
    public static int dp2px(float value){
      return (int) (0.5f+ TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,
                    App.app.getResources().getDisplayMetrics()));
    }
}
