package cn.sn.zwcx.yizi.model.home.tabs;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.helper.RetrofitCreateHelper;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.yizi.api.WangyiApi;
import cn.sn.zwcx.yizi.contract.home.tabs.WangyiContract;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsListBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by on 2018/1/10 20:54.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiModel implements WangyiContract.IWangyiModel {

    @NonNull
    public static WangyiModel newInstance(){
        return new WangyiModel();
    }

    @Override
    public Observable<WangyiNewsListBean> getNewsList(int id) {
        return RetrofitCreateHelper.createApi(WangyiApi.class,WangyiApi.HOST)
                .getNewsList(id)
                .compose(RxHelper.<WangyiNewsListBean>rxSchedulerHelper());
    }

    @Override
    public Observable<Boolean> recordItemIsRead(final String key) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DBUtil.newInstance(AppUtil.getContext()).insertRead(DBConfig.TABLE_WANGYI,key, ItemState.STATE_IS_READ));
                e.onComplete();
            }
        }).compose(RxHelper.<Boolean>rxSchedulerHelper());
    }
}
