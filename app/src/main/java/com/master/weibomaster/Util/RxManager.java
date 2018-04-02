package com.master.weibomaster.Util;

import com.master.rxlib.Rx.Net.RetrofitHttpManger;

/**
 * Created by 不听话的好孩子 on 2018/4/2.
 */

public class RxManager {

    public static <T> T create(Class<T> service) {
        return SingleHolder.manger.get().create(service);
    }

    public static RetrofitHttpManger get() {
        return SingleHolder.manger;
    }

    private static class SingleHolder {
        private static RetrofitHttpManger manger = new RetrofitHttpManger.Builder().Builder();
    }
}
