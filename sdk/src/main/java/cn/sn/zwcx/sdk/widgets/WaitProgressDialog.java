package cn.sn.zwcx.sdk.widgets;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by on 2017/12/12 16:17.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WaitProgressDialog extends ProgressDialog {
    public WaitProgressDialog(Context context) {
        this(context,0);
    }

    public WaitProgressDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(false);
    }
}
