package cn.sn.zwcx.mvvm.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by on 2017/12/15 8:35.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class StatusBarUtil {
    /** 默认状态栏的Alpha */
    private static final int DEFAULT_STATUS_BAR_ALPHA = 0;

    /**
     * 设置状态栏的颜色
     * @param activity 需要设置的Activity
     * @param color 颜色值
     */
    public static void setColor(Activity activity, @ColorInt int color){
        setBarColor(activity,color);
    }

    /**
     * 设置状态栏背景色
     * 4.4以下不处理
     * 4.4使用默认沉浸试状态栏
     * @param activity
     * @param color
     */
    public static void setBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            // 沉浸式状态栏(4.4-5.0透明，5.0以上半透明)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // android5.0以上设置透明效果,清除flags,为5.0以上也全透明
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 让应用的主题内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                // 设置状态栏颜色
                window.setStatusBarColor(color);
            }
        }
    }

    /**
     * 设置全透明
     * @param activity
     */
    public static void setTransparent(Activity activity){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        setColor(activity, Color.TRANSPARENT);
    }

    /**
     * 修正Toolbar的位置
     * android4.4以下无法显示内容在Toolbar下所以不需要修正
     * @param toolbar
     * @param activity
     */
    public static void fixToolbar(Toolbar toolbar, Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight(activity);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            layoutParams.setMargins(0,statusBarHeight,0,0);
        }
    }

    /**
     * 获取状态栏的高度
     * @param context
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0,statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 计算状态栏的颜色
     * @param color
     * @param alpha
     */
    public static int calculateStatusBarColor(@ColorInt int color,int alpha){
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}
