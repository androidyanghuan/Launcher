package cn.sn.zwcx.yizi.api;


import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceListBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by on 2018/1/15 11:59.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface WeixinApi {
    public static final String HOST = "http://v.juhe.cn";

    @GET("/weixin/query")
    Observable<WeixinChoiceListBean> getWeixinChoiceList(@Query("pno") int page, @Query("ps") int
            ps, @Query("dtype") String dttype, @Query("key") String key);
}
