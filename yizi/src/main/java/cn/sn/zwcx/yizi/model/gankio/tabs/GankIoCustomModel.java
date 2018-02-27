package cn.sn.zwcx.yizi.model.gankio.tabs;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.sdk.config.DBConfig;
import cn.sn.zwcx.sdk.config.ItemState;
import cn.sn.zwcx.sdk.helper.RetrofitCreateHelper;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.DBUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.api.GankioApi;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoCustomContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoCustomItemBean;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoCustomListBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

/**
 * Created by on 2018/1/25 17:25.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoCustomModel extends BaseModel implements GankIoCustomContract.IGankIoCustomModel {

    @NonNull
    public static GankIoCustomModel newInstance(){
        return new GankIoCustomModel();
    }

    @Override
    public Observable<Boolean> recordItemIsRead(final String key) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DBUtil.newInstance(AppUtil.getContext()).insertRead(DBConfig.TABLE_GANKIO_CUSTOM,key, ItemState.STATE_IS_READ));
                e.onComplete();
            }
        }).compose(RxHelper.<Boolean>rxSchedulerHelper());
    }

    @Override
    public Observable<GankIoCustomListBean> getCustomGankIoList(String type, int prePage, int page) {
        return RetrofitCreateHelper.createApi(GankioApi.class,GankioApi.HOST)
                .getGankIoCustomList(type,prePage,page)
                .map(new Function<GankIoCustomListBean, GankIoCustomListBean>() {
                    @Override
                    public GankIoCustomListBean apply(GankIoCustomListBean gankIoCustomListBean) throws Exception {
                        for (GankIoCustomItemBean itemBean : gankIoCustomListBean.getResults()) {
                            if (itemBean.getType().equals(AppUtil.getContext().getResources().getString(R.string.welfare_str)))
                                itemBean.itemType = GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_IMAGE;
                            else if (itemBean.getImages() != null) {
                                if (itemBean.getImages().size() > 0 && !TextUtils.isEmpty(itemBean.getImages().get(0)))
                                    itemBean.itemType = GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_NORMAL;
                            } else
                                    itemBean.itemType = GankIoCustomItemBean.GANK_IO_DAY_ITEM_CUSTOM_NO_IMAGE;
                        }
                        return gankIoCustomListBean;
                    }
                })
                .compose(RxHelper.<GankIoCustomListBean>rxSchedulerHelper());
    }
}
