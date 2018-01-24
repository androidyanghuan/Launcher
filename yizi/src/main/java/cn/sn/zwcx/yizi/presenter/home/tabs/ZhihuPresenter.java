package cn.sn.zwcx.yizi.presenter.home.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.activitys.detail.ZhihuDailyDetailActivity;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.home.tabs.ZhihuContract;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyItemBean;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyListBean;
import cn.sn.zwcx.yizi.model.home.tabs.ZhihuModel;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/1/10 15:02.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuPresenter extends ZhihuContract.ZhihuPresenter {

    /**
     * 日报日期
     */
    private String mDate;

    @NonNull
    public static ZhihuPresenter newInstance() {
        return new ZhihuPresenter();
    }

    @Override
    public ZhihuContract.IZhihuModel getModel() {
        return ZhihuModel.newInstance() ;
    }

    @Override
    public void onStart() {

    }


    @Override
    public void loadLatestList() {
        if (mIModel == null)
            return;
        mRxManager.register(mIModel.getDailyList().subscribe(new Consumer<ZhihuDailyListBean>() {
            @Override
            public void accept(ZhihuDailyListBean zhihuDailyListBean) throws Exception {
                mDate = zhihuDailyListBean.getDate();
                if (mIView != null)
                    mIView.updateContentList(zhihuDailyListBean.getStories());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mIView != null){
                    if (mIView.isVisiable())
                        mIView.showToast(AppUtil.getContext().getResources().getString(R.string.network_error));
                    mIView.showNetworkError();
                }
            }
        }));
    }

    @Override
    public void loadMoreList() {
        if (mIView == null)
            return;
        mRxManager.register(mIModel.getDailyList(mDate).subscribe(new Consumer<ZhihuDailyListBean>() {
            @Override
            public void accept(ZhihuDailyListBean zhihuDailyListBean) throws Exception {
                if (mDate.equals(zhihuDailyListBean.getDate()))
                    return;
                mDate = zhihuDailyListBean.getDate();
                if (mIView != null)
                    mIView.updateContentList(zhihuDailyListBean.getStories());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mIView != null)
                    mIView.showLoadMoreError();
            }
        }));
    }

    @Override
    public void onItemClick(final int position, ZhihuDailyItemBean item) {
        mRxManager.register(mIModel.recordItemIsRead(item.getId()).subscribe(new Consumer<Boolean>() {
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
        bundle.putString(BundleKeyConstant.ARG_KEY_ZHIHU_DETAIL_ID,item.getId());
        bundle.putString(BundleKeyConstant.ARG_KEY_ZHIHU_DETAIL_TITLE,item.getTitle());
        mIView.startNewActivity(ZhihuDailyDetailActivity.class,bundle);
    }
}
