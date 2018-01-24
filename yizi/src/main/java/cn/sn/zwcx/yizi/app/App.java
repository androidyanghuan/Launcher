package cn.sn.zwcx.yizi.app;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import cn.sn.zwcx.sdk.global.GlobalApplication;
import cn.sn.zwcx.sdk.utils.AppUtil;

/**
 * Created by on 2017/12/27 14:44.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class App extends GlobalApplication{
    //这个key是自己在聚合数据申请的key，需要自己去聚合数据申请
    public static final String JU_HE_APP_KEY = "799b785ba7b97223be80534651dd0d63";
    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;
    public static App mi;

    public static boolean ISLOG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mi = this;
        getScreenSize();
    }

    /**
     * 获取设备屏幕宽和高
     */
    public void getScreenSize() {
        WindowManager wm = (WindowManager) AppUtil.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0f;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            SCREEN_WIDTH = SCREEN_WIDTH ^ SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH ^ SCREEN_HEIGHT;
            SCREEN_WIDTH = SCREEN_WIDTH ^ SCREEN_HEIGHT;
        }

    }

}
