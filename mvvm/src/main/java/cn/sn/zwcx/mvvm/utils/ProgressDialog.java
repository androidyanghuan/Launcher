package cn.sn.zwcx.mvvm.utils;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by on 2018/3/10 15:41.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ProgressDialog {
    private volatile static android.app.ProgressDialog pd;

    public static void show(Context context, boolean cancelable, String message) {
        try {
            if (pd == null) {
                pd = new android.app.ProgressDialog(context, android.app.ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(cancelable);
                pd.setMessage(message);
                pd.show();
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //  dialog.cancel();
                    }
                });
            }else{
                pd.setCancelable(cancelable);
                pd.setMessage(message);
                pd.show();
                pd.setOnCancelListener(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cancel(){
        if (pd != null && pd.isShowing())
            pd.cancel();
    }

}
