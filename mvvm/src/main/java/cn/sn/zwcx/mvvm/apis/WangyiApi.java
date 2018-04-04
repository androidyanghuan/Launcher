package cn.sn.zwcx.mvvm.apis;

import cn.sn.zwcx.mvvm.bean.wangyi.WangyiNewsListBean;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by on 2018/3/21 16:21.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface WangyiApi {
    public final String HOST = "http://c.m.163.com/";

    @GET("nc/article/headline/T1348647909107/{id}-20.html")
    Observable<WangyiNewsListBean> getNewsList(@Path("id") int id);

    @GET("nc/article/{id}/full.html")
    Observable<ResponseBody> getNewsDetail(@Path("id") String id);

}
