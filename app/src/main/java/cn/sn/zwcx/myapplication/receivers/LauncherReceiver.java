package cn.sn.zwcx.myapplication.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/12/11.
 */

public class LauncherReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Yang Huan","-------------onReceive----------------");
        if (intent == null) return;
        String action = intent.getAction();
        Log.e("Yang Huan","===============action============" + action);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.e("Yang Huan","*******************开机完成了************");
        } else if ("123456".equals(action)) {
            Log.e("Yang Huan","======================自定义广播==================");
            try {
                ClassLoader cl = context.getClassLoader();
                Class<?> c = cl.loadClass("android.os.SystemProperties");
                Method method = c.getMethod("get", String.class);
                String homepage = (String) method.invoke(c, "ro.rk.homepage_base");
                Log.e("Yang Huan","homepage:" + homepage);
                Method set = c.getMethod("set", String.class);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
