package com.master.weibomaster.Rx.Net;

import java.util.LinkedHashMap;

/**
 * Created by vange on 2017/9/4.
 */

public class RequestParams extends LinkedHashMap<String, String> {


    public RequestParams() {
        super(3);
    }

    public RequestParams add(String key, String value) {
        value=value==null?"":value;
        put(key, value);
        return this;
    }
    public RequestParams add(String key, int value) {
        put(key, value+"");
        return this;
    }
    public RequestParams add(String key) {
        put(key, "");
        return this;
    }

    public RequestParams remove(String key) {
        remove(key);
        return this;
    }
}
