package cn.sn.zwcx.mvvm.global;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.header.FunGameBattleCityHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.utils.ImageLoaderConfig;

/**
 * Created by on 2018/2/27 17:30.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class App extends Application {
    /** 本类对象 */
    public static App me = null;

    /** 本应用的上下文 */
    private Context context = null;

    /** Handler对象 */
    private Handler handler = null;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorAccent,android.R.color.white);// 全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(DynamicTimeFormat("更新于 "));
             //   return new FunGameBattleCityHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static DateFormat DynamicTimeFormat(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(s + "MM月dd日 HH时mm分");
        return sdf;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
        context = getApplicationContext();
        handler = new Handler();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(false).build();
        Fresco.initialize(context, ImageLoaderConfig.getImagePipelineConfig(context));

    }

    /** 获取本应用的上下文 */
    public Context getContext() {
        return context;
    }

    /**
     * 在主线程运行
     * @param runnable
     */
    public void runOnUIThread(Runnable runnable){
        if (isRunOnUIThread())
            runnable.run();
        else
            getHandler().post(runnable);
    }

    /**
     * 获取Handler对象实例
     * @return
     */
    private Handler getHandler() {
        return handler;
    }

    /**
     * 当前线程是不是主线程
     * @return
     */
    private boolean isRunOnUIThread() {
        return Process.myTid() == getMainThreadId();
    }

    /**
     * 获取主线程id
     * @return
     */
    private int getMainThreadId() {
        return Process.myTid();
    }

}
