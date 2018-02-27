package cn.sn.zwcx.yizi.presenter.gankio.tabs;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.sdk.rx.RxBus;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.activitys.detail.GankIoDetailActivity;
import cn.sn.zwcx.yizi.activitys.pic.ImageBrowseActivity;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.constants.RxBusCode;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoDayContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoDayItemBean;
import cn.sn.zwcx.yizi.model.gankio.tabs.GankIoDayModel;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/1/23 11:02.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDayPresenter extends GankIoDayContract.GankIoDayPresenter {
    private String mYear = "2018";
    private String mMonth = "1";
    private String mDay = "23";

    private int mAndroidPages = 0;
    private int mIOSPages = 0;
    private List<GankIoDayItemBean> mList = new ArrayList<>();

    public static GankIoDayPresenter newInstance(){
        return new GankIoDayPresenter();
    }
    @Override
    public GankIoDayContract.IGankIoDayModel getModel() {
        return GankIoDayModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadLatestList() {
        if (mIModel == null || mIView == null)
            return;
        //GankIo每日数据大部分时间返回空值，这里直接写死一个日期数据
        mRxManager.register(mIModel.getGankIoDayList(mYear,mMonth,mDay)
        .subscribe(new Consumer<List<GankIoDayItemBean>>() {
            @Override
            public void accept(List<GankIoDayItemBean> gankIoDayItemBeans) throws Exception {
                if (mIView == null)
                    return;
                mList = gankIoDayItemBeans;
                mIView.updateContentList(mList);
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

    }

    @Override
    public void onItemClick(int position, GankIoDayItemBean item) {
        Bundle bundle = new Bundle();
        if (item.getType().equals(AppUtil.getContext().getString(R.string.welfare_str))) {
            bundle.putString(BundleKeyConstant.ARG_KEY_IMAGE_BROWSE_URL, item.getUrl());
            mIView.startNewActivity(ImageBrowseActivity.class, bundle);
        } else {
            bundle.putString(BundleKeyConstant.ARG_KEY_GANKIO_DETAIL_URL,item.getUrl());
            bundle.putString(BundleKeyConstant.ARG_KEY_GANKIO_DETAIL_TITLE,item.getDesc());
            mIView.startNewActivity(GankIoDetailActivity.class,bundle);
        }
    }

    @Override
    public void onMoreClick(int position, GankIoDayItemBean item) {
        //更多福利直接跳到福利界面
        if (item.getType().equals(AppUtil.getContext().getString(R.string.welfare_str))) {
            RxBus.get().send(RxBusCode.RX_BUS_CODE_GANKIO_WELFARE_TYPE);
            RxBus.get().send(RxBusCode.RX_BUS_CODE_GANKIO_SELECT_TO_CHILD, 2);
        } else {
            //跳到custom界面
            RxBus.get().send(RxBusCode.RX_BUS_CODE_GANKIO_CUSTOM_TYPE,item.getType());
            RxBus.get().send(RxBusCode.RX_BUS_CODE_GANKIO_SELECT_TO_CHILD,1);
        }
    }

    @Override
    public void onRefeshClick(int position, GankIoDayItemBean item) {
        if (mIModel == null || mIView == null)
            return;
        if (item.getType().equals(AppUtil.getContext().getString(R.string.android_str))) {
            mAndroidPages++;
            mIView.itemNotifyChanged(mIModel.getGankIoDayAndroid(mAndroidPages % 6), position);
        } else {
            mIOSPages++;
            mIView.itemNotifyChanged(mIModel.getGankIoDayIOS(mIOSPages % 3),position);
        }

    }
}
