package cn.sn.zwcx.yizi.activitys.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.utils.HtmlUtils;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.detail.ZhihuDetailContract;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyDetailBean;
import cn.sn.zwcx.yizi.presenter.detail.ZhihuDetailPresenter;

/**
 * Created by on 2018/1/10 17:34.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuDailyDetailActivity extends BaseWebViewLoadActivity<ZhihuDetailContract.ZhihuDetailPresenter,ZhihuDetailContract.IZhihuDetailModel>
        implements ZhihuDetailContract.IZhihuDetailView {

    private String mId,mTitle;

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mId = bundle.getString(BundleKeyConstant.ARG_KEY_ZHIHU_DETAIL_ID);
            mTitle = bundle.getString(BundleKeyConstant.ARG_KEY_ZHIHU_DETAIL_TITLE);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvDetailTitle.setText(mTitle);
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return ZhihuDetailPresenter.newInstance();
    }

    @Override
    public void showDailyDetail(ZhihuDailyDetailBean bean) {
        flNetView.setVisibility(View.GONE);
        Glide.with(context)
                .load(bean.getImage())
                .crossFade()
                .into(ivDetail);
        tvDetailTitle.setText(bean.getTitle());
        tvDetailCopyright.setText(bean.getImage_source());
        String htmlData = HtmlUtils.createHtmlData(bean.getBody(), bean.getCss(), bean.getJs());
        nswvDetailContent.loadData(htmlData,HtmlUtils.MIME_TYPE,HtmlUtils.ENCODING);
    }

    @Override
    protected void loadDetail() {
        mPresenter.loadDailyDetail(mId);
    }

    @Override
    protected String getToolbarTitle() {
        return context.getResources().getString(R.string.zhihu_detail_title);
    }
}
