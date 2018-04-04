package cn.sn.zwcx.mvvm.apis;


import cn.sn.zwcx.mvvm.bean.weixin.WeixinChoiceListBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by on 2018/3/23 16:10.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface WeixinApi {
    public static final String HOST = "http://v.juhe.cn/";
    // http://v.juhe.cn/weixin/query?pno=1&ps=20&dtype=abc&key=799b785ba7b97223be80534651dd0d63
    @GET("weixin/query")
    Observable<WeixinChoiceListBean> getWeixinChoiceList(@Query("pno") int page, @Query("ps") int ps, @Query("dtype") String dtype, @Query("key") String key);
}
