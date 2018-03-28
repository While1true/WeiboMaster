package com.master.weibomaster.Util;

import android.content.Intent;

import com.didi.virtualapk.PluginManager;
import com.master.weibomaster.Model.Science;
import com.master.weibomaster.Rx.MyObserver;
import com.master.weibomaster.Rx.RxSchedulers;
import com.master.weibomaster.Services.DownLoadService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 不听话的好孩子 on 2018/3/27.
 */

public class ApkUtils {
    public static void startApk(Science science, MyObserver<MyObserver.Progress> listener) {
        if (!startApk(science))
            DownLoadService.Companion.download(science.getUrl()).compose(RxSchedulers.<MyObserver.Progress>compose()).subscribe(listener);
    }

    public static boolean startApk(Science science) {
        String url = science.getUrl();
        File file = DownLoadService.Companion.getFileByUrl(url);
        long available = 0;
        try {
            InputStream is = new FileInputStream(file);
            available = is.available();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (available == science.getSize()) {
            try {
                if (PluginManager.getInstance(ActivityUtils.getTopActivity()).getLoadedPlugin(science.getPackageName()) == null)
                    PluginManager.getInstance(ActivityUtils.getTopActivity()).loadPlugin(file);
                Intent intent = new Intent();
                intent.setClassName(ActivityUtils.getTopActivity(), science.getMainActivity());
                ActivityUtils.getTopActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
