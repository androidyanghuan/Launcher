package cn.sn.zwcx.sdk.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by on 2018/1/9 8:34.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class SnackbarUtil {

    /** 定义Snackbar的几种类型 */
    enum SnackbarType{
        SNACKBAR_INFO,
        SNACKBAR_CONFIRM,
        SNACKBAR_WARNING,
        SNACKBAR_ALERT
    }

    /**
     * 短显示Snackbar，自定义颜色
     * @param view
     * @param message
     * @param messageColor
     * @param backgroundColor
     * @return
     */
    public static Snackbar shortSnackbar(View view, String message, int messageColor, int backgroundColor){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar,messageColor, backgroundColor);
        return snackbar;
    }

    /**
     * 长显示Snackbar，自定义颜色
     * @param view
     * @param message
     * @param messageColor
     * @param backgroundColor
     * @return
     */
    public static Snackbar longSnackbar(View view, String message, int messageColor, int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        setSnackbarColor(snackbar,messageColor,backgroundColor);
        return snackbar;
    }

    /**
     * 自定义时长显示Snackbar，自定义颜色
     * @param view
     * @param message
     * @param messageColor
     * @param backgroundColor
     * @return
     */
    public static Snackbar indefiniteSnackbar(View view, String message,int duration,int messageColor, int backgroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setDuration(duration);
        setSnackbarColor(snackbar,messageColor,backgroundColor);
        return snackbar;
    }

    /**
     * 设置Snackbar文字和背景颜色
     * @param snackbar
     * @param messageColor
     * @param backgroundColor
     */
    public static void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
        View view = snackbar.getView();
        if (view != null){
            view.setBackgroundColor(backgroundColor);
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(messageColor);
        }
    }

    /**
     * 短显示Snackbar，可选预设类型
     * @param view
     * @param message
     * @param type
     * @return
     */
    public static Snackbar shortSnackbar(View view, String message, SnackbarType type) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        switchType(snackbar,type);
        return snackbar;
    }

    /**
     * 预设类型
     * @param snackbar
     * @param type
     */
    private static void switchType(Snackbar snackbar, SnackbarType type) {
        switch (type){
            case SNACKBAR_INFO:
                setSnackbarColor(snackbar, Color.WHITE);
                break;
            case SNACKBAR_CONFIRM:
                setSnackbarColor(snackbar,Color.GREEN);
                break;
            case SNACKBAR_WARNING:
                setSnackbarColor(snackbar,Color.YELLOW);
                break;
            case SNACKBAR_ALERT:
                setSnackbarColor(snackbar,Color.BLACK);
                break;
        }
    }

    /**
     * 设置Snackbar背景颜色
     * @param snackbar
     * @param backgroundColor
     */
    public static void setSnackbarColor(Snackbar snackbar, int backgroundColor) {
        View view = snackbar.getView();
        if (view != null)
            view.setBackgroundColor(backgroundColor);
    }

    /**
     * 向Snackbar中添加view
     * @param snackbar
     * @param layoutId
     * @param index 新加布局在Snackbar中的位置
     */
    public static void SnackbarAddView( Snackbar snackbar,int layoutId,int index) {
        View snackbarView = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
        View inflateView = LayoutInflater.from(snackbar.getContext()).inflate(layoutId, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        snackbarLayout.addView(inflateView,index,layoutParams);
    }
}
