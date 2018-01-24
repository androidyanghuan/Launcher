package cn.sn.zwcx.yizi.model.gankio;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.contract.gankio.GankIoMainContract;

/**
 * Created by on 2018/1/22 21:42.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoMainModel implements GankIoMainContract.IGankIoMainModel {

    public static GankIoMainModel newInstance(){
        return new GankIoMainModel();
    }

    @Override
    public String[] getTabs() {
        return new String[]{AppUtil.getContext().getResources().getString(R.string.day_recommend),
                AppUtil.getContext().getResources().getString(R.string.cargo_custom),
                AppUtil.getContext().getResources().getString(R.string.welfare_str)};
    }
}
