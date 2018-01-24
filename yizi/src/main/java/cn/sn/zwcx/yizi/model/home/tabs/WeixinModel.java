package cn.sn.zwcx.yizi.model.home.tabs;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.helper.RetrofitCreateHelper;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.yizi.api.WeixinApi;
import cn.sn.zwcx.yizi.contract.home.tabs.WeixinContract;
import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceListBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by on 2018/1/15 14:46.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinModel extends BaseModel implements WeixinContract.IWeixinModel {

    @NonNull
    public static WeixinModel newInstance(){
        return new WeixinModel();
    }

    @Override
    public Observable<WeixinChoiceListBean> getWeixinChioceList(int page, int pageSize, String dttype, String key) {
        return RetrofitCreateHelper.createApi(WeixinApi.class,WeixinApi.HOST).getWeixinChoiceList(page,pageSize,dttype,key)
                .compose(RxHelper.<WeixinChoiceListBean>rxSchedulerHelper());
    }

    @Override
    public Observable<Boolean> recordItemIsRead(final String key) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DBUtil.newInstance(AppUtil.getContext()).insertRead(DBConfig.TABLE_WEIXIN,key, ItemState.STATE_IS_READ));
                e.onComplete();
            }
        }).compose(RxHelper.<Boolean>rxSchedulerHelper());
    }
}
