package cn.sn.zwcx.yizi.activitys.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.View;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.utils.DisplayUtil;
import cn.sn.zwcx.sdk.utils.StatusBarUtil;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.detail.WebViewLoadConaract;
import cn.sn.zwcx.yizi.presenter.detail.WebViewLoadPresenter;

/**
 * Created by on 2018/1/9 9:48.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WebViewLoadActivity extends BaseWebViewLoadActivity<WebViewLoadConaract.WebViewLoadPresenter,WebViewLoadConaract.IWebViewLoadModel>
    implements WebViewLoadConaract.IWebViewLoadView{

    private String mTitle,mUrl;

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mTitle = bundle.getString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_TITLE);
            mUrl = bundle.getString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_URL);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) appBar.getChildAt(0).getLayoutParams();
        // 控件的高强制设成56dp+状态栏高度
        params.height = DisplayUtil.dp2px(56) + StatusBarUtil.getStatusBarHeight(context);
    }

    @Override
    public void showUrlDetail(String url) {
        flNetView.setVisibility(View.GONE);
        nswvDetailContent.loadUrl(url);
    }

    @Override
    protected void loadDetail() {
        mPresenter.loadUrl(mUrl);
    }

    @Override
    protected String getToolbarTitle() {
        return mTitle;
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return WebViewLoadPresenter.newInstance();
    }
}
