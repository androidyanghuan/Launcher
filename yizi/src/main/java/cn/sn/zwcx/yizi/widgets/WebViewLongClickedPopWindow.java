package cn.sn.zwcx.yizi.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Trace;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.sn.zwcx.sdk.utils.DisplayUtil;
import cn.sn.zwcx.yizi.R;

/**
 * Created by on 2018/1/8 10:13.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WebViewLongClickedPopWindow extends PopupWindow {
    /**
     * 书签条目弹出菜单 * @value * {@value} *
     */
    public static final int FAVORITES_ITEM_POPUPWINDOW = 0;
    /**
     * 书签页面弹出菜单 * @value * {@value} *
     */
    public static final int FAVORITES_VIEW_POPUPWINDOW = 1;
    /**
     * 历史条目弹出菜单 * @value * {@value} *
     */
    public static final int HISTORY_ITEM_POPUPWINDOW = 3;
    /**
     * 历史页面弹出菜单 * @value * {@value} *
     */
    public static final int HISTORY_VIEW_POPUPWINDOW = 4;
    /**
     * 图片项目弹出菜单 * @value * {@value} *
     */
    public static final int IMAGE_VIEW_POPUPWINDOW = 5;
    /**
     * 超链接项目弹出菜单 * @value * {@value} *
     */
    public static final int ACHOR_VIEW_POPUPWINDOW = 6;
    private LayoutInflater itemLongClickedPopWindowInflater;
    private View longClickedPopWindowView;
    private Context mContext;
    private int mType;
    private LinearLayout llPopupRoot;

    public interface OnItemClickListener{
        void onShowPicClicked();
        void onSavePicClicked();
    }

    private OnItemClickListener mOnItemClickListener;

    private boolean isShowAniming;//show动画是否在执行中
    private boolean isHideAniming;//hide动画是否在执行中

    public WebViewLongClickedPopWindow(Context context, int type, int width, int height) {
        super(context);
        this.mContext = context;
        this.mType = type;
        // 创建
        initTab();
        // 设置宽和高
        setWidth(width);
        setHeight(height);
        setContentView(longClickedPopWindowView);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.shape_corner_bg));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setElevation(DisplayUtil.dp2px(10));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /** 初始化 */
    private void initTab() {
        itemLongClickedPopWindowInflater = LayoutInflater.from(mContext);
        switch (mType){
            case FAVORITES_ITEM_POPUPWINDOW:

                break;
            case FAVORITES_VIEW_POPUPWINDOW:

                break;
            case HISTORY_ITEM_POPUPWINDOW:

                break;
            case HISTORY_VIEW_POPUPWINDOW:

                break;
            case ACHOR_VIEW_POPUPWINDOW:

                break;
            case IMAGE_VIEW_POPUPWINDOW:
                longClickedPopWindowView = itemLongClickedPopWindowInflater.inflate(R.layout.popup_pic_longclick,null);
                llPopupRoot = longClickedPopWindowView.findViewById(R.id.ll_popup_root);
                TextView goToBrowse = longClickedPopWindowView.findViewById(R.id.item_go_image_browse);
                TextView savePicture = longClickedPopWindowView.findViewById(R.id.item_save_image);
                goToBrowse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onShowPicClicked();
                    }
                });
                savePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onSavePicClicked();
                    }
                });
                break;
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (!isShowAniming){
            isShowAniming = true;
            popupAnim(llPopupRoot.getRootView(),0.0f,1.0f,150, true);
        }
    }

    /**
     * PopupWindow属性动画
     * @param rootView 执行属性动画的view
     * @param start start值
     * @param end end值
     * @param duration 动画执行时间
     * @param flag true代表show,false代表hide
     */
    private void popupAnim(final View rootView, float start, float end, int duration, final boolean flag) {
        ValueAnimator va = ValueAnimator.ofFloat(start,end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                rootView.setPivotX(0);
                rootView.setPivotY(rootView.getMeasuredHeight());
                rootView.setScaleX(value);
                rootView.setScaleY(value);
                rootView.setAlpha(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!flag){
                    isHideAniming = false;
                    WebViewLongClickedPopWindow.super.dismiss();
                } else
                    isShowAniming = false;
            }
        });
        va.start();
    }
}
