package cn.sn.zwcx.yizi.presenter.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.contract.detail.ZhihuDetailContract;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyDetailBean;
import cn.sn.zwcx.yizi.model.detail.ZhihuDetailModel;
import io.reactivex.functions.Consumer;

/**
 * Created by on 2018/1/10 17:14.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuDetailPresenter extends ZhihuDetailContract.ZhihuDetailPresenter {

    @NonNull
    public static ZhihuDetailPresenter newInstance(){
        return new ZhihuDetailPresenter();
    }

    @Override
    public ZhihuDetailContract.IZhihuDetailModel getModel() {
        return ZhihuDetailModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadDailyDetail(String id) {
        if (mIModel == null)
            return;
        mRxManager.register(mIModel.getDailyDetail(id).subscribe(new Consumer<ZhihuDailyDetailBean>() {
            @Override
            public void accept(ZhihuDailyDetailBean bean) throws Exception {
                if (mIView != null)
                    mIView.showDailyDetail(bean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mIView != null){
                    mIView.showToast(AppUtil.getContext().getResources().getString(R.string.network_error));
                    mIView.showNetworkError();
                }
            }
        }));
    }
}
