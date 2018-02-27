package cn.sn.zwcx.yizi.activitys.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.View;

import com.bumptech.glide.Glide;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.utils.DisplayUtil;
import cn.sn.zwcx.sdk.utils.HtmlUtils;
import cn.sn.zwcx.sdk.utils.StatusBarUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.detail.WangyiDetailConstant;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsDetailBean;
import cn.sn.zwcx.yizi.presenter.detail.WangyiDetailPresenter;

/**
 * Created by on 2018/1/11 17:34.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiDailyDetailActivity extends BaseWebViewLoadActivity<WangyiDetailConstant.WangyiDetailPresenter,WangyiDetailConstant.IWangyiDetialModel>
        implements WangyiDetailConstant.IWangyiDetailView{

    private String mTitle, mUrl, mId, mImageUrl, mCopyright;

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mTitle = bundle.getString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_TITLE);
            mUrl = bundle.getString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_URL);
            mId = bundle.getString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_ID);
            mImageUrl = bundle.getString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_IMAGE_URL);
            mCopyright = bundle.getString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_COPYRIGHT);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    //    tvDetailTitle.setText(mTitle);
    //    tvDetailCopyright.setText(mCopyright);
    //    Glide.with(context).load(mImageUrl).crossFade().into(ivDetail);
        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) appBar.getChildAt(0).getLayoutParams();
        // 控件的高强制设成56dp+状态栏高度
        lp.height = DisplayUtil.dp2px(56) + StatusBarUtil.getStatusBarHeight(context);
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return WangyiDetailPresenter.newInstance();
    }

    @Override
    public void showNewsDetail(WangyiNewsDetailBean bean) {
        flNetView.setVisibility(View.GONE);
        nswvDetailContent.loadData(bean.getBody(), HtmlUtils.MIME_TYPE,HtmlUtils.ENCODING);
    }

    @Override
    public void showNewsDetail(String url) {
        flNetView.setVisibility(View.GONE);
        nswvDetailContent.loadUrl(url);
    }

    @Override
    protected void loadDetail() {
        mPresenter.loadNewsDetailWithUrl(mUrl);
    }

    @Override
    protected String getToolbarTitle() {
        return context.getResources().getString(R.string.wangyi_detail_title);
    }
}
