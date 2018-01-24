package cn.sn.zwcx.yizi.model.detail;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.base.BaseModel;
import cn.sn.zwcx.yizi.contract.detail.WebViewLoadConaract;

/**
 * Created by on 2018/1/9 17:43.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WebViewLoadModel extends BaseModel implements WebViewLoadConaract.IWebViewLoadModel {
    @NonNull
    public static WebViewLoadModel newInstance(){
        return new WebViewLoadModel();
    }
}
