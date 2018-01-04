package cn.sn.zwcx.welcome.adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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
    private final String TAG = HomeAppAdapter.class.getSimpleName();

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
        holder.itemView.setFocusable(true);
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
                    if (hasFocus){
                        holder.iconFocus.setVisibility(View.VISIBLE);
                        holder.name.setVisibility(View.VISIBLE);
                        ViewCompat.animate(holder.icon).scaleX(1.1f).scaleY(1.1f).translationZ(1).start();
                    } else {
                        holder.iconFocus.setVisibility(View.INVISIBLE);
                        holder.name.setVisibility(View.INVISIBLE);
                        ViewCompat.animate(holder.icon).scaleX(1f).scaleY(1f).translationZ(0).start();
                    }
                }
            });
            holder.itemView.setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View v, MotionEvent event) {
              //      Log.e(TAG,"action:" + event.getAction());
                    int action = event.getAction();
                    switch (action){
                        case MotionEvent.ACTION_HOVER_ENTER:
                            RecyclerView recyclerView = (RecyclerView) holder.itemView.getParent();
                            int[] location = new int[2];
                            recyclerView.getLocationOnScreen(location);
                            int x = location[0];
                            // 为了防止滚动冲突，在滚动的时候，获取焦点为了显示全，会回滚，这样会导致滚动停止
                            if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE){
                                // 当超出RecycleView的边缘时不去响应滚动
                                if (event.getRawX() > recyclerView.getWidth() + x || event.getRawX() < x)
                                    return true;
                                // 鼠标进入争取到焦点
                                v.requestFocusFromTouch();
                                v.requestFocus();
                                holder.iconFocus.setVisibility(View.VISIBLE);
                                holder.name.setVisibility(View.VISIBLE);
                                ViewCompat.animate(holder.icon).scaleX(1.1f).scaleY(1.1f).translationZ(1).start();
                            }
                            break;
                        case MotionEvent.ACTION_HOVER_MOVE:
                            break;
                        case MotionEvent.ACTION_HOVER_EXIT:
                            holder.iconFocus.setVisibility(View.INVISIBLE);
                            holder.name.setVisibility(View.INVISIBLE);
                            ViewCompat.animate(holder.icon).scaleX(1f).scaleY(1f).translationZ(0).start();
                            break;
                    }
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon,iconFocus;
        private TextView name;

        public HomeViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.main_tvrv_item_icon);
            name = itemView.findViewById(R.id.main_tvrv_item_name);
            iconFocus = itemView.findViewById(R.id.main_tvrv_item_icon_focus);
        }
    }

}
