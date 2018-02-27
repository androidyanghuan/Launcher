package cn.sn.zwcx.sdk.rx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by on 2018/1/16 15:02.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RxBus {
    private final String TAG = RxBus.class.getSimpleName();

    private static volatile RxBus defaultInstance;

    private Map<Class,List<Disposable>> subscriptionsByEventType = new HashMap<>();
    private Map<Object,List<Class>> eventTypeBySubscriber = new HashMap<>();
    private Map<Class,List<SubscriberMethod>> subscriberMethodByEventType = new HashMap<>();
    private Subject<Object> bus;

    private RxBus(){
        bus = PublishSubject.create().toSerialized();
    }

    public static RxBus get(){
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null){
            synchronized (RxBus.class){
                if (defaultInstance == null){
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 注册
     * @param subscriber 订阅者
     */
    public void register(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        Method[] declaredMethods = subscriberClass.getDeclaredMethods();
        for (Method method : declaredMethods){
            if (method.isAnnotationPresent(Subscribe.class)){
                //获得参数类型
                Class[] parameterTypes = method.getParameterTypes();
                //参数不为空 且参数个数为1
                if (parameterTypes != null && parameterTypes.length == 1){
                    Class eventType = parameterTypes[0];
                    addEventTypeToMap(subscriber,eventType);
                    Subscribe sub = method.getAnnotation(Subscribe.class);
                    int code = sub.code();
                    ThreadMode threadMode = sub.threadMethod();
                    SubscriberMethod subscriberMethod = new SubscriberMethod(method, threadMode,eventType,subscriber,code);
                    addSubscriberToMap(eventType,subscriberMethod);
                    addSubscriber(subscriberMethod);
                } else if (parameterTypes == null || parameterTypes.length == 0){
                    Class eventType = BusData.class;
                    addEventTypeToMap(subscriber,eventType);
                    Subscribe sub = method.getAnnotation(Subscribe.class);
                    int code = sub.code();
                    ThreadMode threadMode = sub.threadMethod();
                    SubscriberMethod subscriberMethod = new SubscriberMethod(method,threadMode,eventType,subscriber,code);
                    addSubscriberToMap(eventType,subscriberMethod);
                    addSubscriber(subscriberMethod);
                }
            }
        }
    }

    /**
     * 用RxJava添加订阅
     * @param subscriberMethod
     */
    private void addSubscriber(final SubscriberMethod subscriberMethod) {
        Flowable flowable;
        if (subscriberMethod.code == -1)
            flowable = toObservable(subscriberMethod.eventType);
        else
            flowable = toObservable(subscriberMethod.code,subscriberMethod.eventType);
        Disposable subscription = postToObservable(flowable,subscriberMethod)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        callEvent(subscriberMethod,o);
                    }
                });
        addSubscriptionToMap(subscriberMethod.subscriber.getClass(),subscription);
    }

    /**
     * 将订阅事件以event类型为key保存到map,用于取消订阅时用
     * @param eventType     event类型
     * @param subscription  订阅事件
     */
    private void addSubscriptionToMap(Class eventType, Disposable subscription) {
        List<Disposable> disposables = subscriptionsByEventType.get(eventType);
        if (disposables == null){
            disposables = new ArrayList<>();
            subscriptionsByEventType.put(eventType,disposables);
        }
        if (!disposables.contains(subscription))
            disposables.add(subscription);
    }

    /**
     * 回调到订阅者的方法中
     * @param method
     * @param object
     */
    private void callEvent(SubscriberMethod method, Object object) {
        Class eventClass = object.getClass();
        List<SubscriberMethod> methods = subscriberMethodByEventType.get(eventClass);
        if (methods != null && methods.size() > 0) {
            for (SubscriberMethod subscriberMethod : methods) {
                Subscribe sub = subscriberMethod.method.getAnnotation(Subscribe.class);
                int c = sub.code();
                if (c == method.code && method.subscriber.equals(subscriberMethod.subscriber) && method.method.equals(subscriberMethod.method)) {
                    subscriberMethod.invoke(object);
                }

            }
        }
    }

    /**
     * 用于处理订阅事件在那个线程中执行
     * @param flowable
     * @param subscriberMethod
     * @return
     */
    private Flowable postToObservable(Flowable flowable, SubscriberMethod subscriberMethod) {
        Scheduler scheduler = null;
        switch (subscriberMethod.threadMode){
            case MAIN:
                scheduler = AndroidSchedulers.mainThread();
                break;
            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;
            case CURRENT_THREAD:
                scheduler = Schedulers.trampoline();
                break;
                default:
                    new IllegalStateException("Unknown thread mode: " + subscriberMethod.threadMode);
        }
        return flowable.observeOn(scheduler);
    }

    /**
     * 根据传递的code和 eventType 类型返回特定类型(eventType)的 被观察者
     * @param code      事件code
     * @param eventType 事件类型
     * @return
     */
    private <T> Flowable<T> toObservable(final int code, final Class<T> eventType) {
        return bus.toFlowable(BackpressureStrategy.BUFFER).ofType(Message.class)
                .filter(new Predicate<Message>() {
                    @Override
                    public boolean test(Message message) throws Exception {
                        return message.getCode() == code && eventType.isInstance(message.getObject());
                    }
                }).map(new Function<Message, Object>() {
                    @Override
                    public Object apply(Message message) throws Exception {
                        return message.getObject();
                    }
                }).cast(eventType);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * @param eventType 事件类型
     * @return
     */
    private <T> Flowable<T> toObservable(Class<T> eventType) {
        return bus.toFlowable(BackpressureStrategy.BUFFER).ofType(eventType);
    }

    /**
     * 将注解方法信息以event类型为key保存到map中
     * @param eventType         event类型
     * @param subscriberMethod  注解方法信息
     */
    private void addSubscriberToMap(Class eventType, SubscriberMethod subscriberMethod) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods == null){
            subscriberMethods = new ArrayList<>();
            subscriberMethodByEventType.put(eventType,subscriberMethods);
        }
        if (!subscriberMethods.contains(subscriberMethod))
            subscriberMethods.add(subscriberMethod);
    }

    /**
     *  将event的类型以订阅中的subscribe为key,保存到map里面
     * @param subscriber    订阅者
     * @param eventType     event类型
     */
    private void addEventTypeToMap(Object subscriber, Class eventType) {
        List<Class> eventTypes = eventTypeBySubscriber.get(subscriber);
        if (eventTypes == null){
            eventTypes = new ArrayList<>();
            eventTypeBySubscriber.put(subscriber,eventTypes);
        }
        if (!eventTypes.contains(eventType))
            eventTypes.add(eventType);
    }

    /**
     * 取消注册
     * @param subscriber object
     */
    public void unRegister(Object subscriber) {
        List<Class> subscriberTypes = eventTypeBySubscriber.get(subscriber);
        if (subscriberTypes != null){
            for (Class<?> eventType : subscriberTypes){
                unsubscribeByEventType(subscriber.getClass());
                unsubscribeMethodByEventType(subscriber,eventType);
            }
        }
    }

    public void send(int code, Object o) {
        bus.onNext(new Message(code, o));
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    public void send(int code) {
        bus.onNext(new Message(code, new BusData()));
    }

    /**
     * 移除subscriber对应的subscriberMethods
     * @param subscriber
     * @param eventType
     */
    private void unsubscribeMethodByEventType(Object subscriber, Class eventType) {
        List<SubscriberMethod> subscriberMethods = subscriberMethodByEventType.get(eventType);
        if (subscriberMethods != null){
            Iterator<SubscriberMethod> iterator = subscriberMethods.iterator();
            while (iterator.hasNext()){
                SubscriberMethod subscriberMethod = iterator.next();
                if (subscriberMethod.subscriber.equals(subscriber))
                    iterator.remove();
            }
        }
    }

    /**
     * @param eventType
     */
    private void unsubscribeByEventType(Class eventType) {
        List<Disposable> disposables = subscriptionsByEventType.get(eventType);
        if (disposables != null) {
            Iterator<Disposable> iterator = disposables.iterator();
            while (iterator.hasNext()){
                Disposable disposable = iterator.next();
                if (disposable != null && !disposable.isDisposed()){
                    disposable.dispose();
                    iterator.remove();
                }
            }
        }
    }

    private class Message{
        private int code;
        private Object object;

        public Message() {
        }

        public Message(int code, Object object) {
            this.code = code;
            this.object = object;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

}
