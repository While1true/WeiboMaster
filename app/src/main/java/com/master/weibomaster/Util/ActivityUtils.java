package com.master.weibomaster.Util;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/2/28.
 */

public class ActivityUtils {
    private static List<FragmentActivity> activities = new ArrayList<>(20);

    public static void addActivity(FragmentActivity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public static void remove(FragmentActivity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public static FragmentActivity getTopActivity() {
        if (activities.isEmpty())
            return null;
        FragmentActivity fragmentActivity = activities.get(activities.size() - 1);
        if (fragmentActivity.isDestroyed()) {
            activities.remove(fragmentActivity);
            return getTopActivity();
        }
        return fragmentActivity;
    }

    public static FragmentActivity get(int index) {
        return activities.get(index);
    }
}
