package cn.sn.zwcx.yizi.model.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.sdk.helper.RetrofitCreateHelper;
import cn.sn.zwcx.sdk.helper.RxHelper;
import cn.sn.zwcx.yizi.api.WangyiApi;
import cn.sn.zwcx.yizi.contract.detail.WangyiDetailConstant;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by on 2018/1/11 21:06.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiDetailModel extends BaseModel implements WangyiDetailConstant.IWangyiDetialModel {

    @NonNull
    public static WangyiDetailModel newInstance(){
        return new WangyiDetailModel();
    }

    @Override
    public Observable<ResponseBody> getNewsDetail(String id) {
        return RetrofitCreateHelper.createApi(WangyiApi.class,WangyiApi.HOST)
                .getNewsDetail(id)
                .compose(RxHelper.<ResponseBody>rxSchedulerHelper());
    }
}
