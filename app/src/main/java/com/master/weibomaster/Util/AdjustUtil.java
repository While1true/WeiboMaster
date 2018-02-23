package com.master.weibomaster.Util;

/**
 * Created by vange on 2018/1/16.
 */


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by 不听话的好孩子 on 2017/9/27.
 */

/**
 * -------------------------------------------------------------------------------------------------------
 * 按屏幕宽度等比例适配】
 * dp模式下按正常情况使用
 * dp模式下系统空间某些drawable会受影响，显示偏差，如RatingBar 解决办法是设置maxheight=minheight=某个值
 * <p>
 * ---------------------------------------------------------------------------------------------------------
 * pt模式
 * 先生成pt xml
 * 使用dp的地方用生成的dimens
 * 使用麻烦，但不会影响系统控件，
 * 适配：控件尽量使用match_parent wrap_content margin和padding 用pt格式
 * --------------------------------------------------------------------------------------------------------
 * <p>
 * 把设计稿的尺寸方向和density配置进来
 * 在application中调用Adjustutil.adjust
 */
public class AdjustUtil {
    enum Orention {
        LAND, PORT, BOTH
    }

    enum Unit {
        DP, PT
    }

    private static Orention type = Orention.PORT;
    private static Unit unit = Unit.PT;
    private static int DESIGN_WIDTHs = 1080;
    private static int DESIGN_HEIGHTs = 1920;
    private static float DESIGN_SCALEs = 3.0f;

    private static Point point;
    public static float screenScale = 0;
    public static float originalScreenScale = 0;

    /**
     * 平板配置
     *
     * @param app
     */
    public static void adjust(Application app) {
        adjust(app, unit);
    }

    public static void adjust(Application app, Unit unit) {
        adjust(app, type, unit, DESIGN_WIDTHs, DESIGN_HEIGHTs, DESIGN_SCALEs);
    }

    /**
     * @param app
     * @param orention      方向
     * @param units         单位
     * @param DESIGN_WIDTH  设计稿宽
     * @param DESIGN_HEIGHT 设计稿高
     * @param DESIGN_SCALE  设计设备密度
     */
    public static void adjust(Application app, Orention orention, Unit units, int DESIGN_WIDTH, int DESIGN_HEIGHT, float DESIGN_SCALE) {
        type = orention;
        unit = units;
        DESIGN_WIDTHs = DESIGN_WIDTH;
        DESIGN_HEIGHTs = DESIGN_HEIGHT;
        DESIGN_SCALEs = DESIGN_SCALE;
        originalScreenScale = app.getResources().getDisplayMetrics().density;
        doAdjust(app);
    }

    private static void doAdjust(Application context) {
        /**
         * 和设计尺寸一样不调整
         */
//        if (checkIfNotNeedAdjust(context))
//            return;

        /**
         * 计算缩放
         */
        calculateScale(context);

        /**
         * 设置缩放
         */
        applyScale(context);

    }

    /**
     * 改变density
     * 屏幕选注不重启activity时要在onConfigChanged中调用
     *
     * @param context
     */
    public static void changeTypeValue(Context context) {
        if (type == Orention.BOTH) {
            /**
             * 和设计尺寸一样不调整
             */
//            if (checkIfNotNeedAdjust(context))
//                return;
            calculateScale(context);
        }
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (unit == Unit.DP) {
            displayMetrics.density = screenScale;
            displayMetrics.scaledDensity = screenScale;

            DisplayMetrics metrics = getMetricsOnMiui(resources);
            if (metrics != null) {
                metrics.density = screenScale;
                metrics.scaledDensity = screenScale;
            }
        } else if (unit == Unit.PT) {
            displayMetrics.xdpi = 72f * screenScale;
            displayMetrics.scaledDensity = screenScale;
            DisplayMetrics metrics = getMetricsOnMiui(resources);
            if (metrics != null) {
                metrics.xdpi = 72f * screenScale;
                metrics.scaledDensity = screenScale;
            }
        }
    }

    /**
     * 注册监听调整
     *
     * @param context
     */
    private static void applyScale(final Application context) {
        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                if (!(activity instanceof AdjustNormal)) {
                    changeTypeValue(context);
                    changeTypeValue(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 计算scale
     */
    private static void calculateScale(Context context) {
        if (point == null)
            point = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        if (point.x > point.y) {
            screenScale = DESIGN_SCALEs * point.x / Math.max(DESIGN_WIDTHs, DESIGN_HEIGHTs);
        } else {
            screenScale = DESIGN_SCALEs * point.x / Math.min(DESIGN_WIDTHs, DESIGN_HEIGHTs);
        }

    }


    //解决MIUI更改框架导致的MIUI7+Android5.1.1上出现的失效问题
    // (以及极少数基于这部分miui去掉art然后置入xposed的手机)
    private static DisplayMetrics getMetricsOnMiui(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 不需要调整的activity 实现该接口
     */
    public interface AdjustNormal {

    }
}

class MyClass {
    public static final String sample2 = " <dimen name=\"size{0}\">{1}dp</dimen>";

    public static void main(String[] args) {
        generateXml();
    }

    /**
     * 生成xml文件
     */
    private static void generateXml() {
        File file = new File("123.txt");
        StringBuilder builder = new StringBuilder();
        BufferedWriter writer = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "utf-8"));
            for (int i = 1; i < 800; i++) {
                builder.append(sample2.replace("{0}", i + "").replace("{1}", i + "") + "\n");
            }
            writer.write(new String(builder.toString().getBytes("utf-8"), "utf-8"));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
