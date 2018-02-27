package cn.sn.zwcx.yizi.model.gankio.tabs;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.sdk.helper.RetrofitCreateHelper;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.yizi.api.GankioApi;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoWelfareContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoWelfareListBean;
import io.reactivex.Observable;

/**
 * Created by on 2018/2/2 9:06.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoWelfareModel extends BaseModel implements GankIoWelfareContract.IGankIoWelfareModel {

    @NonNull
    public static GankIoWelfareModel newInstance(){
        return new GankIoWelfareModel();
    }

    @Override
    public Observable<GankIoWelfareListBean> getWelfareList(int pre_page, int page) {
        return RetrofitCreateHelper.createApi(GankioApi.class,GankioApi.HOST)
                .getGankIoWelfareList(pre_page,page)
                .compose(RxHelper.<GankIoWelfareListBean>rxSchedulerHelper());
    }

    @Override
    public Observable<Boolean> recordItemIsRead(String key) {
        // 不记录
        return null;
    }
}
