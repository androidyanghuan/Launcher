package cn.sn.zwcx.yizi.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoWelfareItemBean;

/**
 * Created by on 2018/1/27 17:29.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoWelfareAdapter extends BaseCompatAdapter<GankIoWelfareItemBean,BaseViewHolder> {

    public GankIoWelfareAdapter(int layoutResId) {
        super(layoutResId);
    }

    public GankIoWelfareAdapter(@Nullable List<GankIoWelfareItemBean> data) {
        super(data);
    }

    public GankIoWelfareAdapter(int layoutResId, @Nullable List<GankIoWelfareItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GankIoWelfareItemBean item) {
        Glide.with(mContext)
                .load(item.getUrl())
                .crossFade(500)
                .placeholder(R.mipmap.img_default_meizi)
                .into((ImageView) helper.getView(R.id.iv_item_image));
    }
}
