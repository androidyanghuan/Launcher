package cn.sn.zwcx.sdk.rx;

import java.lang.reflect.Method;

/**
 * Created by on 2018/1/16 15:25.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class SubscriberMethod {
    public Method method;
    public ThreadMode threadMode;
    public Class<?> eventType;
    public Object subscriber;
    public int code;

    public SubscriberMethod(Method method, ThreadMode threadMode, Class<?> eventType, Object subscriber, int code) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
        this.subscriber = subscriber;
        this.code = code;
    }

    /**
     * 调用函数
     * @param o
     */
    public void invoke(Object o){
        try {
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes != null && parameterTypes.length == 1)
                method.invoke(subscriber,o);
            else if (parameterTypes == null || parameterTypes.length == 0)
                method.invoke(subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
