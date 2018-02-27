package cn.sn.zwcx.yizi.presenter.gankio.tabs;

import android.os.Bundle;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.activitys.detail.GankIoDetailActivity;
import cn.sn.zwcx.yizi.activitys.pic.ImageBrowseActivity;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoCustomContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoCustomItemBean;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoCustomListBean;
import cn.sn.zwcx.yizi.model.gankio.tabs.GankIoCustomModel;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/1/25 14:47.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoCustomPresenter extends GankIoCustomContract.GankIoCustomPresenter{

    private int mCurrentPage;
    private boolean isLoading;

    public static GankIoCustomPresenter newInstance(){
        return new GankIoCustomPresenter();
    }

    @Override
    public GankIoCustomContract.IGankIoCustomModel getModel() {
        return GankIoCustomModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadLatestList() {
        if (mIModel == null || mIView == null)
            return;
        //一次加载20条数据
        mCurrentPage = 1;
        mRxManager.register(mIModel.getCustomGankIoList(mIView.getCustomType(),20,mCurrentPage)
                .subscribe(new Consumer<GankIoCustomListBean>() {
                    @Override
                    public void accept(GankIoCustomListBean gankIoCustomListBean) throws Exception {
                        if (mIView == null)
                            return;
                        if (gankIoCustomListBean.isError()) {
                            mIView.showNetworkError();
                        } else {
                            mCurrentPage++;
                            mIView.updateContentList(gankIoCustomListBean.getResults());
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
        if (!isLoading)
            isLoading = true;
        //一次加载20条数据
        mRxManager.register(mIModel.getCustomGankIoList(mIView.getCustomType(),20,mCurrentPage)
                .subscribe(new Consumer<GankIoCustomListBean>() {
                    @Override
                    public void accept(GankIoCustomListBean gankIoCustomListBean) throws Exception {
                        isLoading = false;
                        if (mIView == null)
                            return;
                        if (gankIoCustomListBean.isError()) {
                            mIView.showNetworkError();
                        } else {
                            if (gankIoCustomListBean.getResults().size() > 0) {
                                mCurrentPage++;
                                mIView.updateContentList(gankIoCustomListBean.getResults());
                            } else
                                mIView.showNoMoreDate();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        isLoading = false;
                        if (mIView != null) {
                            mIView.showLoadMoreError();
                        }
                    }
                }));
    }

    @Override
    public void onItemClick(final int position, GankIoCustomItemBean item) {
        mRxManager.register(mIModel.recordItemIsRead(item.getType() + item.get_id())
        .subscribe(new Consumer<Boolean>() {
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
        Bundle bundle = new Bundle();
        if (item.getType().equals(AppUtil.getContext().getResources().getString(R.string.welfare_str))) {
            bundle.putString(BundleKeyConstant.ARG_KEY_IMAGE_BROWSE_URL, item.getUrl());
            mIView.startNewActivity(ImageBrowseActivity.class, bundle);
        } else {
            bundle.putString(BundleKeyConstant.ARG_KEY_GANKIO_DETAIL_TITLE,item.getDesc());
            bundle.putString(BundleKeyConstant.ARG_KEY_GANKIO_DETAIL_URL,item.getUrl());
            mIView.startNewActivity(GankIoDetailActivity.class,bundle);
        }
    }

    @Override
    public void customTypeChange(String customType) {
        if (mIModel == null || mIView == null)
            return;
        mCurrentPage = 1;
        //一次加载20条数据
        mRxManager.register(mIModel.getCustomGankIoList(customType,20,mCurrentPage)
                .subscribe(new Consumer<GankIoCustomListBean>() {
                    @Override
                    public void accept(GankIoCustomListBean gankIoCustomListBean) throws Exception {
                        if (mIView == null)
                            return;
                        if (gankIoCustomListBean.isError()) {
                            mIView.showNetworkError();
                        } else {
                            mCurrentPage++;
                            mIView.refreshCustomList(gankIoCustomListBean.getResults());
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
}
