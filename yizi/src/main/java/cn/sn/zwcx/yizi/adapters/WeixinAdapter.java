package cn.sn.zwcx.yizi.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.sdk.utils.SpUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceItemBean;

/**
 * Created by on 2018/1/15 17:41.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinAdapter extends BaseCompatAdapter<WeixinChoiceItemBean,BaseViewHolder> {
    public WeixinAdapter(int layoutResId) {
        super(layoutResId);
    }

    public WeixinAdapter(@Nullable List<WeixinChoiceItemBean> data) {
        super(data);
    }

    public WeixinAdapter(int layoutResId, @Nullable List<WeixinChoiceItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeixinChoiceItemBean item) {
        if (DBUtil.newInstance(mContext).isRead(DBConfig.TABLE_WEIXIN, item.getId(), ItemState.STATE_IS_READ))
            helper.setTextColor(R.id.tv_item_title,Color.GRAY);
        else {
            if (SpUtil.getNightModel(mContext))
                helper.setTextColor(R.id.tv_item_title,Color.WHITE);
            else
                helper.setTextColor(R.id.tv_item_title,Color.BLACK);
        }
        helper.setText(R.id.tv_item_title,item.getTitle());
        helper.setText(R.id.tv_item_who,item.getSource());
        Glide.with(mContext).load(item.getFirstImg()).crossFade().into((ImageView) helper.getView(R.id.iv_item_image));
    }
}
