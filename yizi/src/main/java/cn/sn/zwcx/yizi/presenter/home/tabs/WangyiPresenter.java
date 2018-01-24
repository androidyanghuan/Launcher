package cn.sn.zwcx.yizi.presenter.home.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.home.tabs.WangyiContract;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsItemBean;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsListBean;
import cn.sn.zwcx.yizi.model.home.tabs.WangyiModel;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/1/10 20:36.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiPresenter extends WangyiContract.WangyiPresenter {

    private int mCurrentIndex;
    private boolean isLoading;

    @NonNull
    public static WangyiPresenter newInstance(){
        return new WangyiPresenter();
    }

    @Override
    public WangyiContract.IWangyiModel getModel() {
        return WangyiModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadLatestList() {
        mCurrentIndex = 0;
        mRxManager.register(mIModel.getNewsList(mCurrentIndex).subscribe(new Consumer<WangyiNewsListBean>() {
            @Override
            public void accept(WangyiNewsListBean wangyiNewsListBean) throws Exception {
                if (mIView != null) {
                    ArrayList<WangyiNewsItemBean> newsList = wangyiNewsListBean.getNewsList();
                    int size = newsList.size();
                    for (int i = 0; i < size; i++) {
                        // 过滤掉无效的新闻
                        if (TextUtils.isEmpty(newsList.get(i).getUrl()))
                            newsList.remove(i);
                    }
                    mCurrentIndex += 20;
                    mIView.updateContentList(newsList);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mIView != null){
                    if (mIView.isVisiable()){
                        mIView.showToast(AppUtil.getContext().getResources().getString(R.string.network_error));
                        mIView.showNetworkError();
                    }
                }
            }
        }));
    }

    @Override
    public void loadMoreList() {
        if (!isLoading){
            isLoading = true;
            mRxManager.register(mIModel.getNewsList(mCurrentIndex).subscribe(new Consumer<WangyiNewsListBean>() {
                @Override
                public void accept(WangyiNewsListBean wangyiNewsListBean) throws Exception {
                    isLoading = false;
                    if (mIView == null)
                        return;
                    if (wangyiNewsListBean.getNewsList().size() > 0) {
                        mCurrentIndex += 20;
                        mIView.updateContentList(wangyiNewsListBean.getNewsList());
                    } else
                        mIView.showNoMoreDate();
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
    public void onItemClick(final int position, WangyiNewsItemBean item) {
        mRxManager.register(mIModel.recordItemIsRead(item.getDocid()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (mIView == null)
                    return;
                if (aBoolean)
                    mIView.itemNotifyChanged(position);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }));
        if (mIView == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_ID,item.getDocid());
        bundle.putString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_URL,item.getUrl());
        bundle.putString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_TITLE,item.getTitle());
        bundle.putString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_IMAGE_URL,item.getImgsrc());
        bundle.putString(BundleKeyConstant.ARG_KEY_WANGYI_DETAIL_COPYRIGHT,item.getSource());
        mIView.startNewActivity(null,bundle);
    }
}
