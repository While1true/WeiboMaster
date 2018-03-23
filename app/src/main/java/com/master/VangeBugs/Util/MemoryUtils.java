package com.master.VangeBugs.Util;


import com.master.VangeBugs.App;

import java.io.File;

/**
 * Created by 不听话的好孩子 on 2018/3/9.
 */

public class MemoryUtils {
    public static final File FILE=new File(App.app.getFilesDir(), "pic/");
    public static Float caculateFolderSize(File file) {
        Float length = 0f;
        listfile(file, length);
        return length / 1024 / 1024;
    }

    public static void clearFile(File file){
        if(file.isDirectory()){
            for (File file1 : file.listFiles()) {
                    clearFile(file1);
            }
        }else{
            file.delete();
        }
    }

    private static void listfile(File file, Float longx) {
        if (file.isDirectory()) {
            for (File x : file.listFiles()) {
                if (x.isDirectory()) {
                    listfile(x, longx);
                } else {
                    longx += x.getTotalSpace();
                }
            }
        }

    }

}
