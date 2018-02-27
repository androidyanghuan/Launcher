package cn.sn.zwcx.sdk.helper;


import java.util.concurrent.TimeUnit;


import cn.sn.zwcx.sdk.helper.okhttp.CacheInterceptor;
import cn.sn.zwcx.sdk.helper.okhttp.HttpCache;
import cn.sn.zwcx.sdk.helper.okhttp.TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by on 2018/1/10 15:34.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RetrofitCreateHelper {

    private static final int TIMEOUT_READ = 20;
    private static final int TIMEOUT_CONNECTION = 10;
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static CacheInterceptor cacheInterceptor = new CacheInterceptor();

    private static OkHttpClient client = new OkHttpClient.Builder()
            //SSL证书
            .sslSocketFactory(TrustManager.getUnsafeOkHttpClient())
            .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            //打印日志
            .addInterceptor(interceptor)
            //设置Cache拦截器
            .addNetworkInterceptor(cacheInterceptor)
            .addInterceptor(cacheInterceptor)
            .cache(HttpCache.getCache())
            //time out
            .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
            //失败重连
            .retryOnConnectionFailure(true)
            .build();

    public static <T> T createApi(Class<T> clazz, String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
}
