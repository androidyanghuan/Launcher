package cn.sn.zwcx.mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by on 2018/5/3 15:13.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleItemViewHolder>
        implements RecycleItemTouchHelper.ItemTouchHelperCallback {

    private List<?> datas;

    private int layoutId,variable;

    @Override
    public void onItemDelete(int positon) {
        datas.remove(positon);
        notifyItemRemoved(positon);
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        Collections.swap(datas,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public List<?> getData() {
        return datas;
    }

    public interface OnItemLongClickListener{
        void onItemLongClickListener(RecycleItemViewHolder holder,int positon);
    }

    private OnItemLongClickListener mListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mListener = onItemLongClickListener;
    }

    public RecycleAdapter(List<?> datas, int layoutId, int variable) {
        this.datas = datas;
        this.layoutId = layoutId;
        this.variable = variable;
    }

    @Override
    public RecycleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        return new RecycleItemViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final RecycleItemViewHolder holder, final int position) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setVariable(variable,datas.get(position));
        binding.executePendingBindings();
        if (holder.itemView != null)
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mListener != null)
                        mListener.onItemLongClickListener(holder, position);
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class RecycleItemViewHolder extends RecyclerView.ViewHolder {
        public RecycleItemViewHolder(View itemView) {
            super(itemView);
        }

    }

}
