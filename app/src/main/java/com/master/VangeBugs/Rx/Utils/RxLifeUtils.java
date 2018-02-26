package com.master.VangeBugs.Rx.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/12.
 */

public class RxLifeUtils {

    private HashMap<Object, ArrayList<Disposable>> Disposables;

    private RxLifeUtils() {
        Disposables = new LinkedHashMap<>(5);
    }

    private HashMap<Object, ArrayList<Disposable>> getMap() {
        return Disposables;
    }

    public void add(Object tag, Disposable disposable) {
        ArrayList<Disposable> list = null;
        if (Disposables.containsKey(tag)) {
            list = Disposables.get(tag);
            list.add(disposable);
        } else {
            list = new ArrayList<>(1);
            list.add(disposable);
            Disposables.put(tag, list);
        }
    }

    public void remove(Object tag) {
        if (Disposables.containsKey(tag)) {
            for (Disposable disposable : Disposables.get(tag)) {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        }
       Disposables.remove(tag);
    }

    public static RxLifeUtils getInstance() {
        return InstanceHolder.utils;
    }

    private static class InstanceHolder {
        private static RxLifeUtils utils = new RxLifeUtils();
    }

}
