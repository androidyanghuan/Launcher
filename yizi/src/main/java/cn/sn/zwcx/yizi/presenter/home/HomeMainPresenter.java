package cn.sn.zwcx.yizi.presenter.home;

import android.support.annotation.NonNull;

import cn.sn.zwcx.yizi.contract.home.HomeMainContract;
import cn.sn.zwcx.yizi.model.home.HomeMainModel;

/**
 * Created by on 2017/12/27 11:46.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HomeMainPresenter extends HomeMainContract.HomeMainPresenter {

    private static HomeMainPresenter instance;

    private HomeMainPresenter(){}

    @NonNull
    public static HomeMainPresenter newInstance(){
        if (instance == null) {
            synchronized (HomeMainPresenter.class) {
                if (instance == null)
                    instance = new HomeMainPresenter();
            }
        }
        return instance;
    }

    @Override
    public void getTabList() {
        if (mIView == null || mIModel == null) return;
        mIView.showTabList(mIModel.getTabs());
    }

    @Override
    public HomeMainContract.IHomeMainModel getModel() {
        return HomeMainModel.newInstance();
    }

    @Override
    public void onStart() {

    }
}
