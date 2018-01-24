package cn.sn.zwcx.yizi.presenter.gankio;

import cn.sn.zwcx.yizi.contract.gankio.GankIoMainContract;
import cn.sn.zwcx.yizi.model.gankio.GankIoMainModel;

/**
 * Created by on 2018/1/22 21:34.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoMainPresenter extends GankIoMainContract.GankIoMainPresenter {

    public static GankIoMainPresenter newInstance(){
        return new GankIoMainPresenter();
    }

    @Override
    public GankIoMainContract.IGankIoMainModel getModel() {
        return GankIoMainModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void getTabList() {
        if (mIModel == null || mIView == null)
            return;
        mIView.showTabList(mIModel.getTabs());
    }
}
