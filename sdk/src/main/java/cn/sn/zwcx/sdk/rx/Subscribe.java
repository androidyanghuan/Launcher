package cn.sn.zwcx.sdk.rx;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by on 2018/1/16 15:52.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    int code() default -1;
    ThreadMode threadMethod() default ThreadMode.CURRENT_THREAD;
}
