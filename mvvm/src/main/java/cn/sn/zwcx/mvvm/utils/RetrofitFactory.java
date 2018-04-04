package cn.sn.zwcx.mvvm.utils;


import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.sn.zwcx.mvvm.global.App;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by on 2018/3/10 14:01.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RetrofitFactory {
    private final String TAG = RetrofitFactory.class.getSimpleName();
    private volatile static RetrofitFactory mRetrofitFactory;

    private static OkHttpClient mClient;

    public static final int TIME_OUT = 30000;

    private RetrofitFactory() {
        File cacheFile = new File(App.me.getContext().getCacheDir(),"cacheData");
        Cache cache = new Cache(cacheFile,1024 * 1024);
        mClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT,TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT,TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.tokenInterceptor())     // 添加token拦截
                .addInterceptor(InterceptorUtil.loggingInterceptor())   // 添加拦截日志
                .retryOnConnectionFailure(true) // 失败重连
                .addInterceptor(InterceptorUtil.cacheInterceptor())
                .addNetworkInterceptor(InterceptorUtil.cacheInterceptor())
                .cache(cache)
                .build();

    }

    public static RetrofitFactory getInstance() {
        if (mRetrofitFactory == null) {
            synchronized (RetrofitFactory.class) {
                if (mRetrofitFactory == null)
                    mRetrofitFactory = new RetrofitFactory();
            }
        }
        return mRetrofitFactory;
    }

    public <T> T createApi(Class<T> clz,String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // 添加rxjava转换器
                .client(mClient)
                .build();
        return retrofit.create(clz);
    }

}
