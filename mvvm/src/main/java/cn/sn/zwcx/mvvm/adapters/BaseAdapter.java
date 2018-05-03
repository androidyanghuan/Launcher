package cn.sn.zwcx.mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by on 2018/3/24 10:36.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

    protected int layoutId;

    protected int variableId;

    protected List<?> datas;

    public BaseAdapter(int layoutId, int variableId, List<?> datas) {
        this.layoutId = layoutId;
        this.variableId = variableId;
        this.datas = datas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        return new BaseViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setVariable(variableId,datas.get(position));
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
