package cn.sn.zwcx.sdk.anim;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by on 2017/12/27 19:48.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ToolbarAnimManager {
    /**
     * Toolbar 进场动画
     * <p>
     *  TextViw & ActionMenuView渐变动画
     * @param context
     * @param toolbar
     */
    public static void animIn(Context context, Toolbar toolbar){
        ImageButton ib = null;
        TextView tv = null;
        ActionMenuView amv = null;
        int childCount = toolbar.getChildCount();
        for (int i = 0; i < childCount; i++){
            View view = toolbar.getChildAt(i);
            if (view instanceof ImageButton){
                ib = (ImageButton) view;
                continue;
            }
            if (view instanceof ActionMenuView){
                amv = (ActionMenuView) view;
                continue;
            }
            if (view instanceof TextView)
                tv = (TextView) view;
        }
        if (ib != null)
            animNavigationIcon(context,ib);
    }

    /**
     * Toolbar Title 动画
     * <p>
     *  NavigationIcon渐变动画
     * @param context
     * @param imageButton
     */
    public static void animNavigationIcon(Context context, @NonNull ImageButton imageButton) {

    }
}
