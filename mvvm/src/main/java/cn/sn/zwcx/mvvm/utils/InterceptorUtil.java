package cn.sn.zwcx.mvvm.utils;

import android.util.Log;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by on 2018/3/10 11:33.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class InterceptorUtil {
    private static final String TAG = InterceptorUtil.class.getSimpleName();

    private static String token = "";

    public static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * 日志拦截器
     * @return
     */
    public static HttpLoggingInterceptor loggingInterceptor(){
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(TAG,"message:" + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);// 设置打印数据的级别
    }

    /**
     * token验证的拦截器1
     */
    public static void tokenInterceptor1() {
        new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                // 拿到请求体，并添加header携带上token
                Request request = chain.request().newBuilder()
                  //      .header("token",token)
                        .build();
                // 拿到响应体
                Response response = chain.proceed(request);
            /*    if (response.code() == 201) {
                    // 重新获取新的Token,这里用了一个特殊的接口来获取新的Token
                    Call<String> call = RetrofitFactory.getInstance().api().loginByToken("123456",token);
                    // 拿到请求体
                    Request requestToken = call.request();
                    // 获取响应体
                    Response responseToken = chain.proceed(requestToken);
                    //我这假设新的token是在header里返回的
                    //我们拿到新的token头
                    List<String> tokens = responseToken.headers().values("token");
                    if (tokens != null)
                        // 给Token赋新值
                        token = tokens.get(0);
                    //这是只需要替换掉之前的token重新请求接口就行了
                    Request newRequest = request.newBuilder()
                            .header("token",token)
                            .build();
                    return chain.proceed(newRequest);
                }*/
                return response;
            }
        };
    }

    /**
     * Token验证的拦截器
     */
    public static Interceptor tokenInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                /**
                 * 1.拦截返回的数据
                 * 2.判断token是否有效
                 * 3.失效获取新的token
                 * 4.重新请求接口
                 */
                // 拿到请求体并添加header携带token
                Request request = chain.request().newBuilder()
                  //      .header("token",token)
                        .build();
                // 拿到响应体
             /*    Response response = chain.proceed(request);
               ResponseBody responseBody = response.body();
                // 得到缓冲源
                BufferedSource source = responseBody.source();
                // 请求全部
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();
                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null)
                    charset = contentType.charset(UTF8);
                // 读取返回数据
                String bodyString = buffer.clone().readString(charset);
                if (TextUtils.isEmpty(bodyString)) {
                    // 处理返回的数据我这创建了一个BaseEntity来将数据转化为对象
                    BaseEntity bean = JSON.parseObject(bodyString,BaseEntity.class);
                    //假设当返回的code为42444时token失效
                    if (bean.getCode() == 4244) {
                        //重新获取新token,这用了一个特殊接口来获取新的Token
                        Call<String> call = RetrofitFactory.getInstance().api().loginByToken("123456",token);
                        // 拿到请求体
                        Request requestToken = call.request();
                        // 获取响应体
                        Response responseToken = chain.proceed(requestToken);
                        //我这假设新的token是在header里返回的
                        //我们拿到新的token头
                        List<String> tokens = responseToken.headers().values("token");
                        if (tokens != null)
                            // 给token赋新值
                            token = tokens.get(0);

                        //这是只需要替换掉之前的token重新请求接口就行了
                        Request newRequest = request.newBuilder()
                                .header("token",token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                }*/
                return chain.proceed(request);
            }
        };
    }

    public static Interceptor cacheInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // 获取请求
                Request request = chain.request();
                // 判断当前是否有网络连接，如果有就从网络上获取数据，如果没有就从缓存读取数据
                if (!NetworkUtil.isAvailableByPing()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                int maxAge = 60;
                if (NetworkUtil.isAvailableByPing()) {
                    String cache = request.cacheControl().toString();
                    return response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    int maxTime = 3 * 24 * 60 * 60;
                    Log.e(TAG,"current network no response,set cache indate 3 day...");
                    return response.newBuilder()
                            .header("Cache-Control","public, only-if-cached, max-stale=" + maxTime)
                            .removeHeader("Pragma")
                            .build();
                }
            }
        };
    }

}
