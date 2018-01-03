package cn.sn.zwcx.welcome.adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.sn.zwcx.welcome.R;
import cn.sn.zwcx.welcome.bean.Application;

/**
 * Created by on 2018/1/2 16:44.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HomeAppAdapter extends RecyclerView.Adapter<HomeAppAdapter.HomeViewHolder> {

    private List<Application> datas;

    public interface OnItemClickListener{
        void onItemClickListener(View view,int position);
    }

    private OnItemClickListener mListener;

    public HomeAppAdapter(List<Application> datas) {
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeViewHolder(View.inflate(parent.getContext(), R.layout.home_tvrv_item_layout,null));
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final int position) {
        holder.icon.setBackground(datas.get(position).icon);
        holder.name.setText(datas.get(position).name);
        holder.name.setVisibility(View.INVISIBLE);
        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClickListener(holder.itemView,position);
                }
            });
        }
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    ViewGroup vg = (ViewGroup) v;
                    View childAt = vg.getChildAt(0);
                    String resourceName = childAt.getResources().getResourceName(childAt.getId());
                //    String resourceName = v.getResources().getResourceName(v.getId());
                    Log.e("yang huang: ","resource name:" + resourceName);
                    if (hasFocus){
                        v.setBackgroundResource(R.drawable.default_focus);
                        holder.name.setVisibility(View.VISIBLE);
                    } else {
                        v.setBackgroundResource(R.drawable.default_focus);
                        holder.name.setVisibility(View.INVISIBLE);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon;
        private TextView name;

        public HomeViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.main_tvrv_item_icon);
            name = itemView.findViewById(R.id.main_tvrv_item_name);
        }
    }

}
