package cn.sn.zwcx.mvvm.apis;

import cn.sn.zwcx.mvvm.bean.douban.HotMovieBean;
import cn.sn.zwcx.mvvm.bean.douban.MovieDetailBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by on 2018/4/12 16:27.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface DoubanApi {
    public final String HOST = "https://api.douban.com/";

    /**
     * 获取豆瓣电影top250
     *
     * @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目，如"10"条，最多100
     */
    @GET("v2/movie/top250")
    Observable<HotMovieBean> getMovieTop250(@Query("start") int start, @Query("count") int count);

    /**
     * 获取豆瓣电影热映榜
     * @param start
     * @param count
     * @return
     */
    @GET("/v2/movie/in_theaters")
    Observable<HotMovieBean> getHotMovieList(@Query("start") int start, @Query("count") int count);

    /**
     * 获取电影详情
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    Observable<MovieDetailBean> getMovieDetail(@Path("id") String id);
}
