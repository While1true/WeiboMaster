package com.master.VangeBugs.Rx;


import com.master.VangeBugs.Model.Base;
import com.master.VangeBugs.Rx.Utils.RxLifeUtils;
import com.master.VangeBugs.Util.K2JUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/13.
 */

public abstract class DataObserver<T> implements Observer<Base<T>> {
    private static final String TAG = "DataObserver";
    private Object tag = null;
    Disposable d;

    protected DataObserver(Object tag) {
        this.tag = tag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if(null!=tag)
        RxLifeUtils.getInstance().add(tag, d);
    }


    @Override
    public void onNext(Base<T> t) {
        if (t.getError_code()==200) {
            OnNEXT(t.getData());
        }else if(t.getError_code()==100){
            OnERROR(t.getMessage());
        }else{
            OnLOGIN();
        }
    }

    @Override
    public void onError(Throwable e) {
        OnERROR(e.getMessage());
    }

    @Override
    public void onComplete() {
        if (d != null && !d.isDisposed())
            d.isDisposed();
    }

    public abstract void OnNEXT(T bean);
    public void OnERROR(String error){
        K2JUtils.toast(error);
    }
    public void OnLOGIN(){
    }

}

