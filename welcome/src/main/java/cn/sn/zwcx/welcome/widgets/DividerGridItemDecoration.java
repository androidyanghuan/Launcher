package cn.sn.zwcx.welcome.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by on 2018/1/4 14:20.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = DividerGridItemDecoration.class.getSimpleName();

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDrawable;

    public DividerGridItemDecoration(Context context) {
        TypedArray ta = context.obtainStyledAttributes(ATTRS);
        mDrawable = ta.getDrawable(0);
        ta.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizantal(c,parent);
        drawVertical(c,parent);
    }

    /** 绘制竖直方向的分割线 */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            View childAt = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            int top = childAt.getTop() - layoutParams.topMargin;
            int bottom = childAt.getBottom() + layoutParams.bottomMargin;
            int left = childAt.getRight() + layoutParams.rightMargin;
            int right = left + mDrawable.getIntrinsicWidth();
        //    Log.e(TAG,"top:" + top + " bottom:" + bottom + " left:" + left + " right:" + right);
            mDrawable.setBounds(left,top,right,bottom);
            mDrawable.draw(c);
        }
    }

    /** 绘制水平方向的分割线 */
    private void drawHorizantal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            View childAt = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            int left = childAt.getLeft() - layoutParams.leftMargin;
            int right = childAt.getRight() + layoutParams.rightMargin + mDrawable.getIntrinsicWidth();
            int top = childAt.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left,top,right,bottom);
            mDrawable.draw(c);
        }
    }

    /** 是否最后一行 */
    private boolean isLastRow(RecyclerView parent, int pos, int childCount, int spanCount) {
    //    Log.e(TAG,"pos:" + pos + " child count:" + childCount + " span count:" + spanCount);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)
                // 如果是最后一行则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // 当方向为纵向滚动时
            if (orientation == StaggeredGridLayoutManager.VERTICAL){
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else {
                // 当方向为横向滚动时
                if ((pos + 1) % spanCount == 0)
                    return true;
            }
        }
        return false;
    }

    /** 是否是最后一列 */
    private boolean isLastColum(RecyclerView parent,int pos,int spanCount,int childCount){
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            // 如果是最后一列则不需要绘制右边
            if ((pos + 1) % spanCount == 0)
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.HORIZONTAL){
                childCount = childCount - childCount % spanCount;
                // 如果是最后一列则不需要绘制右边
                if (pos >= childCount)
                    return true;
            } else {
                // 如是最后一列则不需要绘制右边
                if ((pos + 1) % spanCount == 0)
                    return true;
            }
        }
        return false;
    }

    /** 获取列数 */
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        else if (layoutManager instanceof StaggeredGridLayoutManager)
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        return spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int spanCount = getSpanCount(parent);
        int itemCount = parent.getAdapter().getItemCount();
        for (int i = 0; i < itemCount; i++){
            if (isLastRow(parent,i,itemCount,spanCount))
                // 如果是最后一行则不需要绘制底部
                outRect.set(0,0,mDrawable.getIntrinsicWidth(),0);
            else if (isLastColum(parent,i,spanCount,itemCount))
                // 如果是最后一列则不需要右边
                outRect.set(0,0,0,mDrawable.getIntrinsicHeight());
            else
                // 绘制底部和右边
            outRect.set(0,0,mDrawable.getIntrinsicWidth(),mDrawable.getIntrinsicHeight());
        }
    }

}
