package com.master.VangeBugs.Rx.Utils;



import com.master.VangeBugs.Model.Base;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by vange on 2017/8/31.
 */

public class RxBus {

    private final Subject<Object> bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus= PublishSubject.create().toSerialized();
    }
    // 单例RxBus
    public static RxBus getDefault() {

        return InstanceHolder.defaultInstance ;
    }
    // 发送一个新的事件
    public void post (Object o) {
        bus.onNext(o);
    }
    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObservable (Class<T> eventType) {
        return bus.ofType(eventType);

    }
    public <T extends Base> Observable<T> toObservable (final int Tag, Class<T> eventType) {
        return bus.ofType(eventType)
                .filter(new Predicate<T>() {
                    @Override
                    public boolean test(T t) throws Exception {
                        return t.getError_code()==Tag;
                    }
                });

    }
    private static class InstanceHolder{
        private static  RxBus defaultInstance=new RxBus();
    }
}
