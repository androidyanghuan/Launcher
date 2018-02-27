package cn.sn.zwcx.yizi.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.sdk.utils.SpUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoCustomItemBean;
import cn.sn.zwcx.yizi.widgets.RvLoadMoreView;

/**
 * Created by on 2018/1/24 9:01.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoCustomAdapter extends BaseMultiItemQuickAdapter<GankIoCustomItemBean,BaseViewHolder> {

    private String mImageSize = "?imageView2/0/w/200";

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GankIoCustomAdapter(@NonNull List<GankIoCustomItemBean> data) {
        super(data);
        setLoadMoreView(new RvLoadMoreView());
        setEnableLoadMore(true);
        openLoadAnimation();

        addItemType(GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_NORMAL,R.layout.item_gank_io_custom_normal);
        addItemType(GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_IMAGE,R.layout.item_gank_io_custom_image);
        addItemType(GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_NO_IMAGE,R.layout.item_gank_io_custom_no_image);

    }

    @Override
    protected void convert(BaseViewHolder helper, GankIoCustomItemBean item) {
        initTypeImage(helper,item);
        helper.setText(R.id.tv_item_who,
                TextUtils.isEmpty(item.getWho()) ? AppUtil.getContext().getResources().getString(R.string.anonymity_str) : item.getWho());
        helper.setText(R.id.tv_item_type,item.getType());
        helper.setText(R.id.tv_item_time,item.getCreatedAt().substring(0,10));
        switch (helper.getItemViewType()) {
            case GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_NORMAL:
                helper.setText(R.id.tv_item_title,item.getDesc());
                initTextColor(helper,item);
                if (item.getImages() != null)
                    if (item.getImages().size() > 0 && !TextUtils.isEmpty(item.getImages().get(0)))
                        Glide.with(mContext).load(item.getImages().get(0) + mImageSize)
                        .asBitmap()
                        .into((ImageView) helper.getView(R.id.iv_item_image));
                break;
            case GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_IMAGE:
                Glide.with(mContext)
                        .load(item.getUrl())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.mipmap.img_default_meizi)
                        .into((ImageView) helper.getView(R.id.iv_item_image));
                break;
            case GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_NO_IMAGE:
                helper.setText(R.id.tv_item_title,item.getDesc());
                initTextColor(helper,item);
                break;
        }

    }

    /** 初始化文本颜色 */
    private void initTextColor(BaseViewHolder helper, GankIoCustomItemBean item) {
        if (DBUtil.newInstance(mContext).isRead(DBConfig.TABLE_GANKIO_CUSTOM, item.getType() + item.get_id(), ItemState.STATE_IS_READ)) {
            helper.setTextColor(R.id.tv_item_title, Color.GRAY);
        } else {
            if (SpUtil.getNightModel(mContext))
                helper.setTextColor(R.id.tv_item_title,Color.WHITE);
            else
                helper.setTextColor(R.id.tv_item_title,Color.BLACK);
        }
    }

    /** 初始化图片类型 */
    private void initTypeImage(BaseViewHolder helper, GankIoCustomItemBean item) {
        switch (item.getType()) {
            case "福利":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_title_welfare);
                break;
            case "Android":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_title_android);
                break;
            case "ios":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_title_ios);
                break;
            case "前端":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_title_front);
                break;
            case "休息视频":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_title_video);
                break;
            case "瞎推荐":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_item_tuijian);
                break;
            case "拓展资源":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_item_tuozhan);
                break;
            case "App":
                helper.setImageResource(R.id.iv_type_item_title,R.drawable.ic_vector_item_app);
                break;
        }
    }
}
