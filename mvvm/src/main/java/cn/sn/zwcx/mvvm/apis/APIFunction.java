package cn.sn.zwcx.mvvm.apis;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author yemao
 * @date 2017/4/9
 * @description API接口!
 */

public interface APIFunction {

//    @GET("https://www.baidu.com/")
 //   Observable<BaseEntity<ABean>> getBaidu(@Query("wd") String name);

    @POST("获取新token的地址")
    Call<String> loginByToken(@Query("mobile") String mobile, @Query("token") String cookie);

    //上传单张图片
    @POST("服务器地址")
    Observable<Object> imageUpload(@Part() MultipartBody.Part img);
    //上传多张图片
    @POST("服务器地址")
    Observable<Object> imagesUpload(@Part() List<MultipartBody.Part> imgs);
}
