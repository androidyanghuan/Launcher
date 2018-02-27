package cn.sn.zwcx.yizi.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.sdk.utils.SpUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.contract.home.tabs.WangyiContract;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsItemBean;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsListBean;

/**
 * Created by on 2018/1/16 8:24.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiAdapter extends BaseCompatAdapter<WangyiNewsItemBean,BaseViewHolder> {

    public WangyiAdapter(int layoutResId) {
        super(layoutResId);
    }

    public WangyiAdapter(@Nullable List<WangyiNewsItemBean> data) {
        super(data);
    }

    public WangyiAdapter(int layoutResId, @Nullable List<WangyiNewsItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WangyiNewsItemBean item) {
        if (DBUtil.newInstance(mContext).isRead(DBConfig.TABLE_WANGYI,item.getDocid(), ItemState.STATE_IS_READ))
            helper.setTextColor(R.id.tv_item_title, Color.GRAY);
        else {
            if (SpUtil.getNightModel(mContext))
                helper.setTextColor(R.id.tv_item_title,Color.WHITE);
            else
                helper.setTextColor(R.id.tv_item_title,Color.BLACK);
        }
        helper.setText(R.id.tv_item_title,item.getTitle());
        helper.setText(R.id.tv_item_who,item.getSource());
        helper.setText(R.id.tv_item_time,item.getPtime());
        Glide.with(mContext)
                .load(item.getImgsrc())
                .crossFade()
                .into((ImageView) helper.getView(R.id.iv_item_image));
    }
}
