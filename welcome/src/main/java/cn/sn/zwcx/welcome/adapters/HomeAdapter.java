package cn.sn.zwcx.welcome.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import cn.sn.zwcx.welcome.R;
import cn.sn.zwcx.welcome.bean.Application;
import cn.sn.zwcx.welcome.widgets.TVRecyclerView;

/**
 * Created by on 2018/1/2 14:42.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HomeAdapter extends TVRecyclerView.CustomAdapter<Application>{

    private List<Application> datas;

    public HomeAdapter(Context context, List<Application> data) {
        super(context, data);
        datas = data;
    }

    @Override
    protected RecyclerView.ViewHolder onSetViewHolder(View view) {
        return new GalleryViewHolder(view);
    }

    @NonNull
    @Override
    protected int onSetItemLayout() {
        return R.layout.item;
    }

    @Override
    protected void onSetItemData(RecyclerView.ViewHolder viewHolder, int position) {
        GalleryViewHolder holder = (GalleryViewHolder) viewHolder;
        holder.itemView.setFocusable(true);
        holder.name.setText(datas.get(position).name);
        holder.icon.setBackground(datas.get(position).icon);
     //   holder.name.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onItemFocus(View itemView, int position) {
        TextView tvFocus = itemView.findViewById(R.id.tv_focus);
        ImageView focusBg = itemView.findViewById(R.id.focus_bg);
        tvFocus.setVisibility(View.VISIBLE);
        focusBg.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // 抬高Z轴
            ViewCompat.animate(itemView).scaleX(1.1f).scaleY(1.1f).translationZ(1).start();
        }else {
            ViewCompat.animate(itemView).scaleX(1.1f).scaleY(1.1f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
    }

    @Override
    protected void onItemGetNormal(View itemView, int position) {
        TextView tvFocus = itemView.findViewById(R.id.tv_focus);
        ImageView focusBg = itemView.findViewById(R.id.focus_bg);
     //   tvFocus.setVisibility(View.INVISIBLE);
        focusBg.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ViewCompat.animate(itemView).scaleX(1f).scaleY(1f).translationZ(0).start();
        }else {
            ViewCompat.animate(itemView).scaleX(1f).scaleY(1f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
    }

    @Override
    protected int getCount() {
        return mData.size();
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView name;
        public GalleryViewHolder(View itemView) {
            super(itemView);
         //   icon = itemView.findViewById(R.id.main_tvrv_item_icon);
         //   name = itemView.findViewById(R.id.main_tvrv_item_name);
            name = itemView.findViewById(R.id.tv_focus);
            icon = itemView.findViewById(R.id.im);
        }
    }

}
