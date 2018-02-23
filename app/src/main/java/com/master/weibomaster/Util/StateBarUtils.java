package com.master.weibomaster.Util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by vange on 2018/1/16.
 */

public class StateBarUtils {
    public static void performTransStateBar(Window window){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            View decorView = window.getDecorView();
            int flags=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(flags);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setFitsSystemWindows(true);
        }else if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            WindowManager.LayoutParams layoutParams=window.getAttributes();
            layoutParams.flags|=WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
