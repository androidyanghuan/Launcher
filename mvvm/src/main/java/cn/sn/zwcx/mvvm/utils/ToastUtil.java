package cn.sn.zwcx.mvvm.utils;

import android.widget.Toast;

import cn.sn.zwcx.mvvm.global.App;

/**
 * Created by on 2018/2/27 17:17.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ToastUtil {
    /** Toast对象 */
    private static Toast mToast = null;

    /**
     * 根据资源id显示Toast
     * @param resource
     */
    public static void showToast(int resource){
        showToast(App.me.getContext().getResources().getString(resource));
    }

    /**
     * 根据字符串显示Toast
     * @param str
     */
    public static void showToast(String str) {
        showToast(str,Toast.LENGTH_LONG);
    }

    /**
     * 显示指定时长的Toast
     * @param str
     * @param duration
     */
    public static void showToast(final String str, final int duration) {
        App.me.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(App.me.getContext(), str, duration);
                } else {
                    mToast.setText(str);
                    mToast.setDuration(duration);
                }
                mToast.show();
            }
        });
    }
}
