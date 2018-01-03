package cn.sn.zwcx.sdk.utils;

import android.content.Context;
import android.widget.Toast;

import cn.sn.zwcx.sdk.global.GlobalApplication;

/**
 * Created by on 2017/12/20 9:14.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ToastUtil {
    /** Toast对象 */
    private static Toast mToast = null;

    /**
     * 根据资源Id显示一个Toast
     * @param resourceId
     */
    public static void showToast(int resourceId){
        showToast(GlobalApplication.getInstance().getContext().getResources().getString(resourceId));
    }

    /**
     * 根据字符串显示一个Toast
     * @param str
     */
    public static void showToast(String str) {
        showToast(str,Toast.LENGTH_LONG);
    }

    /**
     * 指定显示Toast的时长
     * @param str
     * @param duration
     */
    public static void showToast(final String str, final int duration) {
        GlobalApplication ga = GlobalApplication.getInstance();
        final Context context = ga.getContext();
        ga.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null){
                    mToast = Toast.makeText(context, str,duration);
                } else {
                    mToast.setText(str);
                    mToast.setDuration(duration);
                }
                mToast.show();
            }
        });

    }

}
