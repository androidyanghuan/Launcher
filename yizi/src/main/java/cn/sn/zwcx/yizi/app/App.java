package cn.sn.zwcx.yizi.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by on 2017/12/27 14:44.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class App extends Application {

    public static App mi;

    private Context context;

    public static boolean ISLOG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mi = this;
        context = getApplicationContext();
    }

    /** 获取Application的context */
    public Context getContext(){
        return context;
    }
}
