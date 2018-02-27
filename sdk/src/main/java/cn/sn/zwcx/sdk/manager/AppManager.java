package cn.sn.zwcx.sdk.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by on 2017/12/15 10:09.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class AppManager {
    /** 本类对象 */
    private static AppManager instance;

    /** Activity 栈 */
    private static Stack<Activity> activityStack;

    // 构造函数私有
    private AppManager(){
        if (activityStack == null)
        activityStack = new Stack<>();
    }

    /** 单一实例 */
    public static AppManager getInstance(){
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null)
                    instance = new AppManager();
            }
        }
        return instance;
    }

    /** 添加Activity到栈中 */
    public void addActivity(Activity activity){
        activityStack.add(activity);
    }

    /** 获取当前Activity(堆栈中最后一个压入的) */
    public Activity getCurrentActivity(){
        return activityStack.lastElement();
    }

    /** 结束当前Activity(堆栈中最后一个压入的) */
    public void finishActivity(){
        finishActivity(activityStack.lastElement());
    }

    /** 结束指定的Activity */
    public void finishActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /** 结束指定类名的Activity */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack){
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /** 结束所有的Activity */
    public void finishAllActivity(){
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /** 退出本应用 */
    public void exitApp(Context context){
        try {
            finishAllActivity();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            am.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** app是否退出了 */
    public boolean isAppExit(){
        return activityStack == null || activityStack.isEmpty();
    }

}
