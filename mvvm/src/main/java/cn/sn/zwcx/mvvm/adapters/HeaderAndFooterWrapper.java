package cn.sn.zwcx.mvvm.adapters;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.sn.zwcx.mvvm.utils.WrapperUtil;
import cn.sn.zwcx.mvvm.views.ViewHolder;

/**
 * Created by on 2018/4/4 15:23.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * https://github.com/hongyangAndroid/baseAdapter
 */

public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null)
            return ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
        else if (mFooterViews.get(viewType) != null)
            return ViewHolder.createViewHolder(parent.getContext(), mFooterViews.get(viewType));
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHanderViewPosition(position) || isFootViewPosition(position))
            return;
        mInnerAdapter.onBindViewHolder(holder, position - getHeandersCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHanderViewPosition(position))
            return mHeaderViews.keyAt(position);
        else if (isFootViewPosition(position))
            return mFooterViews.keyAt(position - getHeandersCount() - getRealItemCount());
        return mInnerAdapter.getItemViewType(position - getHeandersCount());
    }

    @Override
    public int getItemCount() {
        return getHeandersCount() + getRealItemCount() + getFooterCount();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHanderViewPosition(position) || isFootViewPosition(position))
            WrapperUtil.setFullSpan(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtil.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtil.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null)
                    return layoutManager.getSpanCount();
                else if (mFooterViews.get(viewType) != null)
                    return layoutManager.getSpanCount();
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    private boolean isHanderViewPosition(int position) {
        return position < getHeandersCount();
    }

    private boolean isFootViewPosition(int position) {
        return position >= getHeandersCount() + getRealItemCount();
    }

    public int getHeandersCount() {
        return mHeaderViews.size();
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        if (getRealItemCount() == 0)
            return;
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

}
