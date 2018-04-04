package cn.sn.zwcx.mvvm.apis;



import cn.sn.zwcx.mvvm.bean.zhihu.ZhihuDailyDetailBean;
import cn.sn.zwcx.mvvm.bean.zhihu.ZhihuDailyListBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by on 2018/3/20 16:31.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */
public interface ZhihuApi {
    public final String HOST = "http://news-at.zhihu.com";

    @GET("/api/4/news/latest")
    Observable<ZhihuDailyListBean> getLastDailyList();

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDailyListBean> getDailyListWithDate(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<ZhihuDailyDetailBean> getZhihuDailyDetail(@Path("id") String id);
}
