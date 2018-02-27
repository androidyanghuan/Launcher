package cn.sn.zwcx.sdk.utils;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import cn.sn.zwcx.sdk.global.GlobalApplication;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by on 2018/1/8 8:49.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class AppUtil {

    /**
     * 获取上下文对象
     *
     * @return 上下文对象
     */
    public static Context getContext() {
        return GlobalApplication.getContext();
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return GlobalApplication.getHandler();
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return GlobalApplication.getMainThreadId();
    }

    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备IME
     * @param context
     * @return
     */
    public static String getIME(Context context) {
        String ime = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            ime = tm.getDeviceId();
            if (TextUtils.isEmpty(ime))
                ime = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        }
        return ime;
    }

    /**
     * 获取SDCard的绝对路径
     * @return
     */
    public static String getSDPath(){
        String sdPath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return sdPath;
    }

    /**
     * 安装apk
     * @param context
     * @param data
     */
    public static void promptInstall(Context context, Uri data){
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setDataAndType(data,"application/vnd.android.package-archive");
        // FLAG_ACTIVITY_NEW_TASK 可以保证安装成功后可以正常打开app
        promptInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }

    /**
     * 复制文本到剪贴板
     * @param context
     * @param text
     */
    public static void copy2clipboard(Context context,String text){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("clip",text);
        cm.setPrimaryClip(cd);
    }

    /**
     * 运行在主线程
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable){
        if (isRunOnUIThread()){
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }

    /** 是否在主线程运行 */
    private static boolean isRunOnUIThread() {
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId())
            return true;
        return false;
    }

}
