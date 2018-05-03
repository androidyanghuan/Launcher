package cn.sn.zwcx.mvvm.utils;

import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by on 2018/4/23 9:44.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class MLog {

    private static boolean isLog = true;

    public static void LogE(String tag, String message){
        if (isLog)
            Log.e(tag,message);
    }

}
