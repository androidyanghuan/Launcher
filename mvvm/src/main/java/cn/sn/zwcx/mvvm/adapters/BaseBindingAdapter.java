package cn.sn.zwcx.mvvm.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2018/3/15 17:40.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BaseBindingAdapter<M,B extends ViewDataBinding> extends RecyclerView.Adapter {
    protected Context context;

    protected List<M> items;

    protected abstract int getLayoutId(int viewType);

    public BaseBindingAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(viewType), parent, false);
        return null;
    }


}
