package cn.sn.zwcx.sdk.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.support.v4.view.NestedScrollingChild;

/**
 * Created by on 2018/1/5 16:19.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 继承原生WebView是为了和AppBarLayout、CollapsingToolbarLayout等Android Design Support Library控件配合使用
 * 避免AppBarLayout + NestedScrollView + WebView嵌套导致WebView高度判断异常以及跳转后高度异常等问题
 */

public class NestedScrollWebView extends WebView implements NestedScrollingChild {

    /** 嵌套滚动子视图的辅助类对象 */
    private NestedScrollingChildHelper mChildHelper;

    /** 嵌套竖直偏移量 */
    private int mNestedYOffset;

    /** Y轴最后的移动距离 */
    private int mLastMotionY;

    /** 是否改变 */
    private boolean mChange;

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];

    public NestedScrollWebView(Context context) {
        this(context,null);
    }

    public NestedScrollWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestedScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /** 初始化 */
    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        // 事件跟踪
        MotionEvent trackedEvent = MotionEvent.obtain(event);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN)
            mNestedYOffset = 0;
        int y = (int) event.getY();
        event.offsetLocation(0,mNestedYOffset);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                result = super.onTouchEvent(event);
                mChange = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = mLastMotionY - y;
                if (dispatchNestedPreScroll(0,deltaY,mScrollConsumed,mScrollOffset)){
                    deltaY -= mScrollConsumed[1];
                    trackedEvent.offsetLocation(0,mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
                int oldY = getScrollY();
                mLastMotionY = y - mScrollOffset[1];
                int newScrollY = Math.max(0,oldY + deltaY);
                deltaY -= newScrollY - oldY;
                if (dispatchNestedScroll(0,newScrollY - deltaY,0,deltaY,mScrollOffset)){
                    mLastMotionY -= mScrollOffset[1];
                    trackedEvent.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
                if (mScrollConsumed[1] == 0 && mScrollOffset[1] == 0) {
                    if (mChange) {
                        mChange = false;
                        trackedEvent.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(trackedEvent);
                    } else {
                        result = super.onTouchEvent(trackedEvent);
                    }
                    trackedEvent.recycle();
                } else {
                    if (!mChange) {
                        mChange = true;
                        super.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0,
                                0, 0));
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                result = super.onTouchEvent(event);
                break;
        }
        return result;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
     //   super.setNestedScrollingEnabled(enabled);
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {

        return mChildHelper.dispatchNestedScroll(dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed,offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx,dy,consumed,offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX,velocityY,consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX,velocityY);
    }
}
