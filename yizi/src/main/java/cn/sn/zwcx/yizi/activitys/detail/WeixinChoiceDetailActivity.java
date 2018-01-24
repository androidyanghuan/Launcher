package cn.sn.zwcx.yizi.activitys.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.View;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.utils.DisplayUtil;
import cn.sn.zwcx.sdk.utils.StatusBarUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.detail.WeixinDetailConstant;
import cn.sn.zwcx.yizi.presenter.home.tabs.WeixinPresenter;

/**
 * Created by on 2018/1/15 15:19.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinChoiceDetailActivity extends BaseWebViewLoadActivity<WeixinDetailConstant.WeixinDetailPresenter,WeixinDetailConstant.IWeixinDetialModel>
        implements WeixinDetailConstant.IWeixinDetailView {

    private String mTitle, mUrl, mImageUrl, mCopyright;

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mTitle = bundle.getString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_TITLE);
            mUrl = bundle.getString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_URL);
            mImageUrl = bundle.getString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_IMAGE_URL);
            mCopyright = bundle.getString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_COPYRIGHT);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //微信精选内部已经有Title等信息，直接显示webview内容
        //tvDetailTitle.setText(mTitle);
        //tvDetailcopyright.setText(mCopyright);
        //Glide.with(mContext).load(mImageUrl).crossFade().into(ivDetail);
        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) appBar.getChildAt(0).getLayoutParams();
        // 控件的高强制设成56dp+状态栏高度
        lp.height = DisplayUtil.dp2px(56) + StatusBarUtil.getStatusBarHeight(context);
    }

    @Override
    public void showWeixinChoiceDetail(String url) {
        flNetView.setVisibility(View.GONE);
        nswvDetailContent.loadUrl(url);
    }

    @Override
    protected void loadDetail() {
        mPresenter.loadWeixinChoiceDetail(mUrl);
    }

    @Override
    protected String getToolbarTitle() {
        return context.getResources().getString(R.string.weixin_detail_title);
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return WeixinPresenter.newInstance();
    }
}
