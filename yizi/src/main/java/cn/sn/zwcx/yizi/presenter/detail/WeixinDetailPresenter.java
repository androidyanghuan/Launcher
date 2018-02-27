package cn.sn.zwcx.yizi.presenter.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.yizi.contract.detail.WeixinDetailConstant;
import cn.sn.zwcx.yizi.model.detail.WeixinDetailModel;

/**
 * Created by on 2018/2/6 15:05.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinDetailPresenter extends WeixinDetailConstant.WeixinDetailPresenter {

    @NonNull
    public static WeixinDetailPresenter newInstance(){
        return new WeixinDetailPresenter();
    }

    @Override
    public void loadWeixinChoiceDetail(String url) {
        if (mIView == null)
            return;
        try {
            mIView.showWeixinChoiceDetail(url);
        } catch (Exception e) {
            e.printStackTrace();
            mIView.showNetworkError();
        }
    }

    @Override
    public WeixinDetailConstant.IWeixinDetialModel getModel() {
        return WeixinDetailModel.newInstance();
    }

    @Override
    public void onStart() {

    }
}
