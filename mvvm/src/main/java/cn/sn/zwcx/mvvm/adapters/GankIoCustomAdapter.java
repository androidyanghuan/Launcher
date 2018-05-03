package cn.sn.zwcx.mvvm.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoItemBean;

/**
 * Created by on 2018/4/8 15:54.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoCustomAdapter extends RecyclerView.Adapter<GankIoCustomAdapter.GankIoCostomViewHolder> {

    private List<GankIoItemBean> items;

    public GankIoCustomAdapter(List<GankIoItemBean> items) {
        this.items = items;
    }

    @Override
    public GankIoCostomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        ViewDataBinding dataBinding = null;
        switch (viewType) {
            case 0:
                dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.gank_io_fragment_custom_item_image, parent, false);
                break;
            case 1:
                dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.gank_io_fragment_custom_item_normal,parent,false);
                break;
            case 2:
                dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.gank_io_fragment_custom_item_no_image,parent,false);
        }
        return new GankIoCostomViewHolder(dataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(GankIoCostomViewHolder holder, int position) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setVariable(BR.gankIoItem,items.get(position));
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = items.get(position).getType();
        List<String> images = items.get(position).getImages();
        switch (type) {
            case "福利":
                return 0;
        }
        if (images != null && !TextUtils.isEmpty(images.get(0)))
            return 1;
        else
            return 2;
    }

    public class GankIoCostomViewHolder extends RecyclerView.ViewHolder {

        public GankIoCostomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
