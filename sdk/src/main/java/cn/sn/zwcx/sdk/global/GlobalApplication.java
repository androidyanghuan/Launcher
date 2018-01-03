package cn.sn.zwcx.sdk.global;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

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
    protected Context mContext;

    /** 主线程id */
    protected int mainThreadId;

    /** Handler处理类 */
    protected Handler mHandler;

    /** 是否开启调试模式 */
    private boolean mDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
        mContext = getApplicationContext();
        mainThreadId = Process.myTid();
        mHandler = new Handler();
    }

    /** 获取本类对象 */
    public static GlobalApplication getInstance() {
        if (me == null) {
            synchronized (GlobalApplication.class) {
                if (me == null) {
                    me = new GlobalApplication();
                }
            }
        }
        return me;
    }

    /** 获取上下文 */
    public Context getContext() {
        return mContext;
    }

    /** 获取全局Handler */
    public Handler getHandler() {
        return mHandler;
    }

    /** 获取主线程Id */
    public int getMainThreadId() {
        return mainThreadId;
    }

    /** 设置debug模式 */
    public boolean isDebug() {
        return mDebug;
    }

    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public String getVersionName(Context context) {
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
    public int getVersionCode(Context context) {
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
    public String getIME(Context context) {
        String ime = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
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
    public String getSDPath(){
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
    public void promptInstall(Context context, Uri data){
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
    public void copy2clipboard(Context context,String text){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("clip",text);
        cm.setPrimaryClip(cd);
    }

    public void runOnUIThread(Runnable runnable){
        if (isRunOnUIThread()){
            runnable.run();
        } else {
            getHandler().post(runnable);
        }
    }

    /** 是否在主线程运行 */
    private boolean isRunOnUIThread() {
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId())
            return true;
        return false;
    }
}
