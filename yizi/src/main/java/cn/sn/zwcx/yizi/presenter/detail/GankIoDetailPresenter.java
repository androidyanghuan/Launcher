package cn.sn.zwcx.yizi.presenter.detail;

import cn.sn.zwcx.yizi.contract.detail.GankIoDetailContract;
import cn.sn.zwcx.yizi.model.detail.GankIoDetailModel;

/**
 * Created by on 2018/1/23 14:56.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDetailPresenter extends GankIoDetailContract.GankIoDetailPresenter {

    public static GankIoDetailPresenter newInstance(){
        return new GankIoDetailPresenter();
    }

    @Override
    public GankIoDetailContract.IGankIoDetailModel getModel() {
        return GankIoDetailModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadGankIoDetail(String url) {
        if (mIView == null)
            return;
        try {
            mIView.showGankIoDetail(url);
        } catch (Exception e) {
            e.printStackTrace();
            mIView.showNetworkError();
        }
    }
}
