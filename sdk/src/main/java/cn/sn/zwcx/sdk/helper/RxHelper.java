package cn.sn.zwcx.sdk.helper;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by on 2018/1/8 16:01.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RxHelper {
    /**
     * 统一线程处理
     * <p>
     * 发布事件io线程，接收事件主线程
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        //compose处理线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 生成Flowable
     * @param t
     * @return Flowable
     */
    public static <T> Flowable<T> createFlowable(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 生成Observable
     * @param t
     * @return Flowable
     */
    public static <T> Observable<T> createObservable(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        });
    }
}
