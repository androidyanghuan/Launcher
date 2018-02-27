package cn.sn.zwcx.yizi.model.home;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.global.MyApplication;
import cn.sn.zwcx.yizi.contract.home.HomeMainContract;

/**
 * Created by on 2017/12/27 14:14.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 主页 model
 */

public class HomeMainModel extends BaseModel implements HomeMainContract.IHomeMainModel {
    private static HomeMainModel instance;

    private HomeMainModel(){}

    @NonNull
    public static HomeMainModel newInstance(){
        if (instance == null){
            synchronized (HomeMainModel.class){
                if (instance == null)
                    instance = new HomeMainModel();
            }
        }
        return instance;
    }


    @Override
    public String[] getTabs() {
        Context context = MyApplication.mi.getContext();
        Resources resources = context.getResources();
        return resources.getStringArray(R.array.home_tabs);
    }
}
