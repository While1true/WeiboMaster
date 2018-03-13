package com.master.weibomaster.Rx;


import com.master.weibomaster.Rx.Utils.RxLifeUtils;
import com.master.weibomaster.Util.K2JUtils;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/9/13.
 */

public abstract class MyObserver<T> implements Observer<T> {
    private static final String TAG = "MyObserver";
    private Object tag = null;
    Disposable d;

    protected MyObserver(Object tag) {
        this.tag = tag;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        RxLifeUtils.getInstance().add(tag, d);
    }


    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        K2JUtils.log("web",e.getMessage());
    }

    @Override
    public void onComplete() {

    }
    public void dispose(){
        if (d != null && !d.isDisposed())
            d.isDisposed();
    }

    public void onProgress(Progress progress) {
    }
    public static class Progress{
        Long total=0l;
        Long current=0l;
        String file;
        float speed;

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getCurrent() {
            return current;
        }

        public void setCurrent(Long current) {
            this.current = current;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        @Override
        public String toString() {
            return "Progress{" +
                    "total=" + total +
                    ", current=" + current +
                    ", file=" + file +
                    ", speed=" + speed +
                    '}';
        }
    }

}

