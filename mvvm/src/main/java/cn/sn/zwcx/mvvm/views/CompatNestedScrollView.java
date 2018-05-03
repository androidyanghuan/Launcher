package cn.sn.zwcx.mvvm.views;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import cn.sn.zwcx.mvvm.global.App;
import cn.sn.zwcx.mvvm.utils.DisplayUtils;
import cn.sn.zwcx.mvvm.utils.StatusBarUtil;

/**
 * Created by on 2018/4/14 9:25.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 自定义CompatNestedScrollView,主要实现根据滑动距离控制绑定View的alpha值
 */

public class CompatNestedScrollView extends NestedScrollView {

    private ViewGroup childViewGroup;

    private View headView,bindView;

    public CompatNestedScrollView(Context context) {
        super(context);
    }

    public CompatNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("only can one child in this view");
        } else {
            if (getChildAt(0) instanceof ViewGroup) {
                childViewGroup = (ViewGroup) getChildAt(0);
                if (childViewGroup != null)
                    headView = childViewGroup.getChildAt(0);
            } else
                throw new IllegalArgumentException("child view must be instanceof ViewGroup");
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float alpha = 1.f;
        if (headView != null && bindView != null) {
            // 如果上滑超过Toolbar的高度，开启伴随动画
            float slideValue = t - (DisplayUtils.dp2px(56)) + StatusBarUtil.getStatusBarHeight(App.me.getContext());
            if (slideValue < 0)
                slideValue = 0;
            float fraction = slideValue / (headView.getHeight() / 1.5f);
            if (fraction > 1)
                fraction = 1;
            alpha *= fraction;
            bindView.setAlpha(alpha);
        }
    }

    /**
     * 绑定要变化Alpha的View
     * @param view 需要变化Alpha的View
     */
    public void bindAlphaView(View view) {
        bindView = view;
    }
}
