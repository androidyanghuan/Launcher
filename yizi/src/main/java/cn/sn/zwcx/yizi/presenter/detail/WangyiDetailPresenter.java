package cn.sn.zwcx.yizi.presenter.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.yizi.contract.detail.WangyiDetailConstant;
import cn.sn.zwcx.yizi.helper.JsonHelper;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsDetailBean;
import cn.sn.zwcx.yizi.model.detail.WangyiDetailModel;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by on 2018/1/11 20:07.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiDetailPresenter extends WangyiDetailConstant.WangyiDetailPresenter {

    @NonNull
    public static WangyiDetailPresenter newInstance(){
        return new WangyiDetailPresenter();
    }

    @Override
    public WangyiDetailConstant.IWangyiDetialModel getModel() {
        return WangyiDetailModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadNewsDetailWithUrl(String url) {
        if (mIView == null)
            return;
        try {
            mIView.showNewsDetail(url);
        } catch (Exception e) {
            mIView.showNetworkError();
            e.printStackTrace();
        }
    }

    @Override
    public void loadNewsDetailWithId(final String id) {
        mRxManager.register(mIModel.getNewsDetail(id).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                if (mIView == null)
                    return;
                // 新闻的Json数据比较特殊，返回的json key不固定，需要手动的获取json数据，然后再用gson解析
                WangyiNewsDetailBean newsDetailBeans = JsonHelper.getNewsDetailBeans(responseBody.string(), id);
                mIView.showNewsDetail(newsDetailBeans);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mIView == null)
                    return;
                mIView.showNetworkError();
            }
        }));
    }
}
