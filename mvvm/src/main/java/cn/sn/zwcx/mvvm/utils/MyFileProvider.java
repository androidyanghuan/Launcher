package cn.sn.zwcx.mvvm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by on 2018/4/21 16:32.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class MyFileProvider {
    public static Uri getUriForFile(Context context, File file){
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            fileUri = getUriForFile24(context,file);
        else
            fileUri = Uri.fromFile(file);
        return fileUri;
    }

    public static Uri getUriForFile24(Context context, File file) {
        return FileProvider.getUriForFile(context,context.getPackageName() + ".fileprovider",file);
    }

    public static void setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writeAble){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getUriForFile(context,file),type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else
            intent.setDataAndType(Uri.fromFile(file),type);

    }
}
