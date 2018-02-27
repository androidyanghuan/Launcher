package cn.sn.zwcx.sdk.helper.okhttp;

import java.io.IOException;

import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.HttpUtils;
import cn.sn.zwcx.sdk.utils.NetworkConnectUtil;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by on 2018/1/10 15:52.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkConnectUtil.isNetworkConnected(AppUtil.getContext())){
            // 有网络时缓存1小时
            int maxAge = 60 * 60;
            request = request.newBuilder()
                    .removeHeader("User_Agent")
                    .header("User_Agent", HttpUtils.getUserAgent())
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control","public, max-age=" + maxAge)
                    .build();
        } else {
            // 无网络时缓存调为四周
            int maxStale = 60 * 60 * 24 * 28;
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .removeHeader("User_Agent")
                    .header("User_Agent",HttpUtils.getUserAgent())
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control","public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
