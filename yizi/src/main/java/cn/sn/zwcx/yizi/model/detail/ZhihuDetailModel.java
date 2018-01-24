package cn.sn.zwcx.yizi.model.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.sdk.helper.RetrofitCreateHelper;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.yizi.api.ZhihuApi;
import cn.sn.zwcx.yizi.contract.detail.ZhihuDetailContract;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyDetailBean;
import io.reactivex.Observable;

/**
 * Created by on 2018/1/10 17:22.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuDetailModel extends BaseModel implements ZhihuDetailContract.IZhihuDetailModel {

    @NonNull
    public static ZhihuDetailModel newInstance(){
        return new ZhihuDetailModel();
    }

    @Override
    public Observable<ZhihuDailyDetailBean> getDailyDetail(String id) {
        return RetrofitCreateHelper.createApi(ZhihuApi.class,ZhihuApi.HOST).getZhihuDailyDetail(id)
                .compose(RxHelper.<ZhihuDailyDetailBean>rxSchedulerHelper());
    }
}
