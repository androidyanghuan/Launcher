package cn.sn.zwcx.sdk.global;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import com.mob.MobApplication;

/**
 * Created by on 2017/12/12 15:11.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GlobalApplication extends MobApplication {
    /** 本类对象 */
    private static GlobalApplication me;

    /** 上下文 */
    protected static Context mContext;

    /** 主线程id */
    protected static int mainThreadId;

    /** Handler处理类 */
    protected static Handler mHandler;

    /** 是否开启调试模式 */
    private static boolean mDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mainThreadId = Process.myTid();
        mHandler = new Handler();
    }

    /** 获取本类对象 */
    public static synchronized GlobalApplication getInstance() {
        return me;
    }

    /** 获取上下文 */
    public static Context getContext() {
        return mContext;
    }

    /** 获取全局Handler */
    public static Handler getHandler() {
        return mHandler;
    }

    /** 获取主线程Id */
    public static int getMainThreadId() {
        return mainThreadId;
    }

    /** 设置debug模式 */
    public static boolean isDebug() {
        return mDebug;
    }


}
