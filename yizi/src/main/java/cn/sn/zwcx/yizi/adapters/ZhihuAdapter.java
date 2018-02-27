package cn.sn.zwcx.yizi.adapters;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.sdk.utils.SpUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyItemBean;

/**
 * Created by on 2018/1/10 10:30.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuAdapter extends BaseCompatAdapter<ZhihuDailyItemBean,BaseViewHolder> {

    public ZhihuAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public ZhihuAdapter(@Nullable List<ZhihuDailyItemBean> data) {
        super(data);
    }

    public ZhihuAdapter(@LayoutRes int layoutResId, @Nullable List<ZhihuDailyItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZhihuDailyItemBean item) {
        if (DBUtil.newInstance(mContext).isRead(DBConfig.TABLE_ZHIHU,item.getId(), ItemState.STATE_IS_READ))
            helper.setTextColor(R.id.tv_item_title,Color.GRAY);
        else {
            if (SpUtil.getNightModel(mContext))
                helper.setTextColor(R.id.tv_item_title,Color.WHITE);
            else
                helper.setTextColor(R.id.tv_item_title,Color.BLACK);
        }
        helper.setText(R.id.tv_item_title,item.getTitle());
        Log.e(TAG,"title:" + item.getTitle());
        Glide.with(mContext)
                .load(item.getImages()[0])
                .crossFade()
                .into((ImageView) helper.getView(R.id.iv_item_image));
    }
}
