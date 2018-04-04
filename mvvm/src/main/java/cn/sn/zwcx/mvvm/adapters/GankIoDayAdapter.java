package cn.sn.zwcx.mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoItemBean;

/**
 * Created by on 2018/3/29 9:25.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDayAdapter extends RecyclerView.Adapter<GankIoDayAdapter.GankIoDayViewHolder> {
    private final String TAG = GankIoDayAdapter.class.getSimpleName();
    private List<GankIoItemBean> items;
    private List<GankIoItemBean> android;
    private List<GankIoItemBean> app;
    private List<GankIoItemBean> ios;
    private List<GankIoItemBean> restMove;
    private List<GankIoItemBean> web;
    private List<GankIoItemBean> resource;
    private List<GankIoItemBean> recommend;
    private List<GankIoItemBean> welfare;
    private int androidIndex,appIndex,iosIndex,restMoveIndex,webIndex,resourceIndex,recommendIndex,welfareIndex;

    public interface OnMoreClickListener{
        void setOnMoreClick(View view, GankIoItemBean itemBean);
    }

    private OnMoreClickListener mOnMoreClickListener;

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener){
        this.mOnMoreClickListener = onMoreClickListener;
    }

    public GankIoDayAdapter(List<GankIoItemBean> items, List<GankIoItemBean> android, List<GankIoItemBean> app, List<GankIoItemBean> ios, List<GankIoItemBean> restMove, List<GankIoItemBean> web, List<GankIoItemBean> resource, List<GankIoItemBean> recommend, List<GankIoItemBean> welfare) {
        this.items = items;
        this.android = android;
        this.app = app;
        this.ios = ios;
        this.restMove = restMove;
        this.web = web;
        this.resource = resource;
        this.recommend = recommend;
        this.welfare = welfare;
    }

    @Override
    public GankIoDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.gank_io_fragment_day_item, parent, false);
        return new GankIoDayViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final GankIoDayViewHolder holder, final int position) {
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        binding.setVariable(BR.gankIoDayItem,items.get(position));
        binding.executePendingBindings();
        holder.switchStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GankIoItemBean gankIoItemBean = items.get(position);
                String type = gankIoItemBean.getType();
                switchType(type,position);
                notifyItemChanged(position);
            }
        });
        holder.moreStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMoreClickListener != null)
                    mOnMoreClickListener.setOnMoreClick(v,items.get(position));
            }
        });
    }

    private void switchType(String type, int position) {
        switch (type) {
            case "Android":
                androidIndex++;
                if (androidIndex >= android.size())
                    androidIndex = 0;
                GankIoItemBean androidItemBean = android.get(androidIndex);
                items.remove(position);
                items.add(position,androidItemBean);
                break;
            case "App":
                appIndex++;
                if (appIndex >= app.size())
                    appIndex = 0;
                GankIoItemBean appItemBean = app.get(appIndex);
                items.remove(position);
                items.add(position,appItemBean);
                break;
            case "iOS":
                iosIndex++;
                if (iosIndex >= ios.size())
                    iosIndex = 0;
                GankIoItemBean iosItemBean = ios.get(iosIndex);
                items.remove(position);
                items.add(position,iosItemBean);
                break;
            case "休息视频":
                restMoveIndex++;
                if (restMoveIndex >= restMove.size())
                    restMoveIndex = 0;
                GankIoItemBean restMoveItemBean = restMove.get(restMoveIndex);
                items.remove(position);
                items.add(position,restMoveItemBean);
                break;
            case "前端":
                webIndex++;
                if (webIndex >= web.size())
                    webIndex = 0;
                GankIoItemBean webItemBean = web.get(webIndex);
                items.remove(position);
                items.add(position,webItemBean);
                break;
            case "拓展资源":
                resourceIndex++;
                if (resourceIndex >= resource.size())
                    resourceIndex = 0;
                GankIoItemBean resourceItemBean = resource.get(resourceIndex);
                items.remove(position);
                items.add(position,resourceItemBean);
                break;
            case "瞎推荐":
                recommendIndex++;
                if (recommendIndex >= recommend.size())
                    recommendIndex = 0;
                GankIoItemBean recommendItemBean = recommend.get(recommendIndex);
                items.remove(position);
                items.add(position,recommendItemBean);
                break;
            case "福利":
                welfareIndex++;
                if (welfareIndex >= welfare.size())
                    welfareIndex = 0;
                GankIoItemBean welfareItemBean = welfare.get(welfareIndex);
                items.remove(position);
                items.add(position,welfareItemBean);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class GankIoDayViewHolder extends RecyclerView.ViewHolder {

        private TextView switchStr,moreStr;

        public GankIoDayViewHolder(View itemView) {
            super(itemView);
            switchStr = itemView.findViewById(R.id.gank_io_item_switch_tv);
            moreStr = itemView.findViewById(R.id.gank_io_item_more_tv);
        }
    }
}
