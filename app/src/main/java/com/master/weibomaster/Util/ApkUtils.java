package com.master.weibomaster.Util;

import android.content.Intent;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;
import com.master.weibomaster.App;
import com.master.weibomaster.Model.DownStatu;
import com.master.weibomaster.Model.Science;
import com.master.weibomaster.Rx.MyObserver;

import java.io.File;

/**
 * Created by 不听话的好孩子 on 2018/3/27.
 */

public class ApkUtils {
    public static void startApk(Science science) {
        String url = science.getUrl();
        String path = PrefUtil.INSTANCE.get(url, "").toString();
        File file=new File(path);
        if (file.exists()) {
            try {
                if(PluginManager.getInstance(ActivityUtils.getTopActivity()).getLoadedPlugin(science.getPackageName())==null)
                PluginManager.getInstance(ActivityUtils.getTopActivity()).loadPlugin(file);
                Intent intent = new Intent();
                intent.setClassName(ActivityUtils.getTopActivity(), science.getMainActivity());
                ActivityUtils.getTopActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DownLoadUtils.Companion.download(url);
        }
    }

    public static void Download(String url, MyObserver<DownStatu> listener) {
        DownLoadUtils.Companion.downloadWithProgress(url, listener);
    }
}
