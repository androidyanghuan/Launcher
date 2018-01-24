package cn.sn.zwcx.yizi.model.bean.rxbus;

import android.net.Uri;

/**
 * Created by on 2017/1/22 17:26.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * RxBus传递头像uri bean
 */

public class RxEventHeadBean {
    private Uri uri;

    public RxEventHeadBean(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "RxEventHeadBean{" +
                "uri=" + uri +
                '}';
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
