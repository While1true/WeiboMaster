package com.master.weibomaster.Util;

/**
 * Created by vange on 2017/9/28.
 */

import android.util.Log;

import com.kxjsj.doctorassistant.Utils.MyToast;


/**
 * kotlin项目使用，小范围使用学习
 */
public class K2JUtils {

    /**
     * 关联kotlin的sharepref
     * @param key
     * @param value
     */
    public static void put(String key,Object value){
        PrefUtil.INSTANCE.put(key,value);
    }
    /**
     * 关联kotlin的sharepref
     * @param key
     * @param defaultvalue
     */
    public static <T>T get(String key,Object defaultvalue){
       return (T) PrefUtil.INSTANCE.get(key,defaultvalue);
    }

    /**
     * 关联kotlin吐司
     * @param charSequence
     * @param during
     */
    public static void toast(CharSequence charSequence,int during){
        MyToast.Companion.showToaste(charSequence,during);
    }
    /**
     * 关联kotlin吐司
     * @param charSequence
     * @param
     */
    public static void toast(CharSequence charSequence){
        toast(charSequence,1);
    }
    /**
     * 给kotlin调用log
     * @param descripte
     * @param message
     */
    public static void log(String descripte,CharSequence message){
        if (true)
            Log.i("xxxxxxxxxx"+ "--" + descripte + "--", "log: "+message);
    }
}
