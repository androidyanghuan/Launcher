package cn.sn.zwcx.yizi.model.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.yizi.contract.detail.WeixinDetailConstant;
import cn.sn.zwcx.yizi.contract.home.tabs.WeixinContract;

/**
 * Created by on 2018/1/15 15:07.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinDetailModel extends BaseModel implements WeixinDetailConstant.IWeixinDetialModel {
    @NonNull
    public static WeixinDetailModel newInstance(){
        return new WeixinDetailModel();
    }
}
