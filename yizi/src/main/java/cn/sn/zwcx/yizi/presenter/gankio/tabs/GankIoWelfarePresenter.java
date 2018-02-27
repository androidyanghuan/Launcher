package cn.sn.zwcx.yizi.presenter.gankio.tabs;

import android.os.Bundle;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.activitys.pic.ImageBrowseActivity;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoWelfareContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoWelfareItemBean;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoWelfareListBean;
import cn.sn.zwcx.yizi.model.gankio.tabs.GankIoWelfareModel;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/1/27 17:43.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoWelfarePresenter extends GankIoWelfareContract.GankIoWelfarePresenter {

    private int mCurrentPage;
    private boolean isLoading;

    public static GankIoWelfarePresenter newInstance(){
        return new GankIoWelfarePresenter();
    }

    @Override
    public GankIoWelfareContract.IGankIoWelfareModel getModel() {
        return GankIoWelfareModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadLatestList() {
        if (mIModel == null || mIView == null)
            return;
        mCurrentPage = 1;
        mRxManager.register(mIModel.getWelfareList(20,mCurrentPage).subscribe(new Consumer<GankIoWelfareListBean>() {
            @Override
            public void accept(GankIoWelfareListBean gankIoWelfareListBean) throws Exception {
                if (mIView == null)
                    return;
                if (gankIoWelfareListBean.isError()) {
                    mIView.showNetworkError();
                } else {
                    mCurrentPage++;
                    mIView.updateContentList(gankIoWelfareListBean.getResults());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mIView != null) {
                    if (mIView.isVisiable())
                        mIView.showToast(AppUtil.getContext().getResources().getString(R.string.network_error));
                    mIView.showNetworkError();
                }
            }
        }));
    }

    @Override
    public void loadMoreList() {
        if (!isLoading) {
            isLoading = true;
            // 一次性加载20条数据
            mRxManager.register(mIModel.getWelfareList(20,mCurrentPage).subscribe(new Consumer<GankIoWelfareListBean>() {
                @Override
                public void accept(GankIoWelfareListBean gankIoWelfareListBean) throws Exception {
                    isLoading = false;
                    if (mIView == null)
                        return;
                    if (gankIoWelfareListBean.isError()) {
                        mIView.showNetworkError();
                    } else {
                        if (gankIoWelfareListBean.getResults().size() > 0) {
                            mCurrentPage++;
                            mIView.updateContentList(gankIoWelfareListBean.getResults());
                        }
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    isLoading = false;
                    if (mIView != null)
                        mIView.showLoadMoreError();
                }
            }));
        }
    }

    @Override
    public void onItemClick(int position, GankIoWelfareItemBean item) {
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeyConstant.ARG_KEY_IMAGE_BROWSE_URL,item.getUrl());
        mIView.startNewActivity(ImageBrowseActivity.class,bundle);
    }
}
