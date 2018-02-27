package cn.sn.zwcx.yizi.presenter.home.tabs;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.activitys.detail.WeixinChoiceDetailActivity;
import cn.sn.zwcx.yizi.global.MyApplication;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.home.tabs.WeixinContract;
import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceItemBean;
import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceListBean;
import cn.sn.zwcx.yizi.model.home.tabs.WeixinModel;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/1/15 14:10.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinPresenter extends WeixinContract.WeixinPresenter {
    private int mCurrentPager;
    private final int mPagerSize = 20;
    private String mDttype;
    private boolean isLoading;

    @NonNull
    public static WeixinPresenter newInstance(){
        return new WeixinPresenter();
    }


    @Override
    public WeixinContract.IWeixinModel getModel() {
        return WeixinModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadLatestList() {
        mCurrentPager = 1;
        mRxManager.register(mIModel.getWeixinChioceList(mCurrentPager,mPagerSize,mDttype, MyApplication.JU_HE_APP_KEY).subscribe(new Consumer<WeixinChoiceListBean>() {
            @Override
            public void accept(WeixinChoiceListBean weixinChoiceListBean) throws Exception {
                if (mIView == null) return;
                Log.e("********************","error code:" + weixinChoiceListBean.getError_code());
                if (weixinChoiceListBean.getError_code().equals("0")){
                    mCurrentPager ++;
                    mIView.updateContentList(weixinChoiceListBean.getResult().getList());
                } else
                    mIView.showNetworkError();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mIView != null)
                    if (mIView.isVisiable()){
                        mIView.showToast(AppUtil.getContext().getResources().getString(R.string.network_error));
                        mIView.showNetworkError();
                    }
            }
        }));
    }

    @Override
    public void loadMoreList() {
        if (!isLoading){
            isLoading = true;
            mRxManager.register(mIModel.getWeixinChioceList(mCurrentPager,mPagerSize,mDttype, MyApplication.JU_HE_APP_KEY).subscribe(new Consumer<WeixinChoiceListBean>() {
                @Override
                public void accept(WeixinChoiceListBean weixinChoiceListBean) throws Exception {
                    isLoading = false;
                    if (mIView == null) return;
                    if (weixinChoiceListBean.getError_code().equals("0")){
                        mCurrentPager ++;
                        mIView.updateContentList(weixinChoiceListBean.getResult().getList());
                    } else
                        mIView.showLoadMoreError();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    isLoading = false;
                    if (mIView == null)
                        return;
                    mIView.showLoadMoreError();
                }
            }));
        }
    }

    @Override
    public void onItemClick(final int position, WeixinChoiceItemBean item) {
        mRxManager.register(mIModel.recordItemIsRead(item.getId()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (mIView == null) return;
                if (aBoolean)
                    mIView.itemNotifyChanged(position);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }));
        if (mIView == null) return;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_URL,item.getUrl());
        bundle.putString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_TITLE,item.getTitle());
        bundle.putString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_IMAGE_URL,item.getFirstImg());
        bundle.putString(BundleKeyConstant.ARG_KEY_WEIXIN_DETAIL_COPYRIGHT,item.getSource());
        mIView.startNewActivity(WeixinChoiceDetailActivity.class,bundle);
    }
}
