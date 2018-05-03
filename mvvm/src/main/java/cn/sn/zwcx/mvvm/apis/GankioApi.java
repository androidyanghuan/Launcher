package cn.sn.zwcx.mvvm.apis;

import cn.sn.zwcx.mvvm.bean.gankio.GankIoCustomListBean;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoDayBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by on 2018/3/27 16:39.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface GankioApi {
    public final String HOST = "http://gank.io/";

    /**
     * 每日数据： http://gank.io/api/day/年/月/日
     * @param year
     * @param month
     * @param day
     * eg:http://gank.io/api/day/2017/04/25
     * @return
     */
    @GET("api/day/{year}/{month}/{day}")
    Observable<GankIoDayBean> getGankIoDay(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    /**
     * 定制数据：http://gank.io/api/data/all/10/1
     * @param type 数据类型
     * @param pre_page 请求个数
     * @param page 第几页
     * @return
     */
    @GET("api/data/{type}/{pre_page}/{page}")
    Observable<GankIoCustomListBean> getGankIoCustomList(@Path("type") String type,@Path("pre_page") int pre_page,@Path("page") int page);

}
