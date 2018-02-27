package cn.sn.zwcx.yizi.activitys.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.utils.DisplayUtil;
import cn.sn.zwcx.sdk.utils.StatusBarUtil;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.detail.GankIoDetailContract;
import cn.sn.zwcx.yizi.presenter.detail.GankIoDetailPresenter;

/**
 * Created by on 2018/1/23 14:35.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDetailActivity extends BaseWebViewLoadActivity<GankIoDetailContract.GankIoDetailPresenter,GankIoDetailContract.IGankIoDetailModel>
        implements GankIoDetailContract.IGankIoDetailView{

    private String url,title;

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString(BundleKeyConstant.ARG_KEY_GANKIO_DETAIL_TITLE);
            url = bundle.getString(BundleKeyConstant.ARG_KEY_GANKIO_DETAIL_URL);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) appBar.getChildAt(0)
                .getLayoutParams();
        // 控件的高强制设成56dp+状态栏高度
        params.height = DisplayUtil.dp2px(56) + StatusBarUtil.getStatusBarHeight (context);
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return GankIoDetailPresenter.newInstance();
    }

    @Override
    public void showGankIoDetail(String url) {
        flNetView.setVisibility(View.GONE);
        nswvDetailContent.loadUrl(url);
    }

    @Override
    protected void loadDetail() {
        mPresenter.loadGankIoDetail(url);
    }

    @Override
    protected String getToolbarTitle() {
        return title;
    }
}
