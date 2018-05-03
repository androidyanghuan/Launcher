package cn.sn.zwcx.mvvm.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sn.zwcx.mvvm.R;

/**
 * Created by on 2018/4/26 17:30.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class CustomProgressDialog {
    protected volatile static Dialog mDialog;

    public static void createLoadingDialog(Context context,boolean cancelable,String message, int theme){
        View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog,null,false);
        ImageView loadingView = view.findViewById(R.id.progress_view);
        TextView loadingDescribe = view.findViewById(R.id.progress_message);
        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.loading_animation);
        loadingView.startAnimation(animation);
        loadingDescribe.setText(message);
        if (mDialog == null)
            mDialog = new Dialog(context,R.style.LoadingDialog);
        mDialog.setContentView(view);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public static void cancel(){
        if (mDialog != null && mDialog.isShowing())
            mDialog.cancel();
     //   mDialog = null;
    }
}
