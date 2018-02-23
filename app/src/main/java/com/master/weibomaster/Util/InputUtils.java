package com.master.weibomaster.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.master.weibomaster.App;


/**
 * Created by vange on 2017/10/9.
 * 输入法的隐藏
 * TextInputLayout相关操作
 */

public class InputUtils {
    /**
     * 隐藏输入法
     * @param editText
     */
    public static void hideKeyboard(EditText editText) {
        InputMethodManager service = (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        service.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    /**
     * 隐藏输入法
     * @param context
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 隐藏输入法
     * @param dialog
     */
    public static void hideKeyboard(Dialog dialog) {
        InputMethodManager manager= (InputMethodManager) App.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static class AndroidBug5497Workaround {
        private final ViewTreeObserver.OnGlobalLayoutListener listener;

        // For more information, see https://code.google.com/p/android/issues/detail?id=5497
        // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

        public static void assistActivity (Activity activity) {
            new AndroidBug5497Workaround(activity);
        }

        private View mChildOfContent;
        private int usableHeightPrevious;
        private FrameLayout.LayoutParams frameLayoutParams;

        private AndroidBug5497Workaround(Activity activity) {
            FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
            mChildOfContent = content.getChildAt(0);
            listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            };
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(listener);
            frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
        }

        private void possiblyResizeChildOfContent() {
            int usableHeightNow = computeUsableHeight();
            if (usableHeightNow != usableHeightPrevious) {
                int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
                int heightDifference = usableHeightSansKeyboard - usableHeightNow;
                if (heightDifference > (usableHeightSansKeyboard/4)) {
                    // keyboard probably just became visible
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                } else {
                    // keyboard probably just became hidden
                    frameLayoutParams.height = usableHeightSansKeyboard;
                }
                mChildOfContent.requestLayout();
                usableHeightPrevious = usableHeightNow;
            }
        }

        private int computeUsableHeight() {
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);
            return (r.bottom - r.top);
        }

        public void unresgisterListener(){
            mChildOfContent.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }

    }


}
