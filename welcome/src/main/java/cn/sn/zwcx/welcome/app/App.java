package cn.sn.zwcx.welcome.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by on 2017/12/29 20:25.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class App extends Application {
    /** Application类对象 */
    public static App me;

    /** 全局上下文 */
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
        context = getApplicationContext();
    }

    public Context getContext(){
        return context;
    }
}
