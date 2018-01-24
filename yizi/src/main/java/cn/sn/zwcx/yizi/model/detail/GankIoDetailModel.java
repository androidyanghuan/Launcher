package cn.sn.zwcx.yizi.model.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.yizi.contract.detail.GankIoDetailContract;

/**
 * Created by on 2018/1/23 15:02.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDetailModel extends BaseModel implements GankIoDetailContract.IGankIoDetailModel {

    @NonNull
    public static GankIoDetailModel newInstance(){
        return new GankIoDetailModel();
    }
}
