package cn.sn.zwcx.mvvm.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cn.sn.zwcx.mvvm.global.App;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by on 2018/3/2 9:35.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * http://blog.csdn.net/android_ls/article/details/53137867
 */

public class ImageLoaderConfig {
    private static final String TAG = ImageLoaderConfig.class.getSimpleName();

    private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";

    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small_cache";

    private static final int MAX_DISK_SMALL_CACHE_SIZE = 10 * ByteConstants.MB;

    private static final int MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE = 5 * ByteConstants.MB;

    private static ImagePipelineConfig sImagePipelineConfig;

    /**
     * Creates config using android http stack as network backend.
     */
    public static ImagePipelineConfig getImagePipelineConfig(final Context context) {
        if (sImagePipelineConfig == null) {
            /**
             * 推荐缓存到应用本身的缓存文件夹，这么做的好处是:
             * 1、当应用被用户卸载后能自动清除缓存，增加用户好感（可能以后用得着时，还会想起我）
             * 2、一些内存清理软件可以扫描出来，进行内存的清理
             */
            File fileCacheDir = context.getApplicationContext().getCacheDir();
            DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setBaseDirectoryPath(fileCacheDir)
                    .build();
            DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                    .setBaseDirectoryPath(fileCacheDir)
                    .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE)
                    .build();
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
            Set<RequestListener> requestListeners = new HashSet<>();
            requestListeners.add(new RequestLoggingListener());
            // 当内存紧张时采取的措施
            MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
            memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
                @Override
                public void trim(MemoryTrimType trimType) {
                    double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                    Log.e(TAG,String.format("Fresco create suggeste trim ratio : %d",suggestedTrimRatio));
                    if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio)
                    // 清除内存缓存
                        Fresco.getImagePipeline().clearMemoryCaches();
                }
            });
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            sImagePipelineConfig = OkHttpImagePipelineConfigFactory.newBuilder(context,okHttpClient)
                    // 若不是要求忒高清显示应用，就用使用RGB_565吧（默认是ARGB_8888)
                    .setBitmapsConfig(Bitmap.Config.RGB_565)
                    // 在解码时改变图片的大小，支持PNG、JPG以及WEBP格式的图片，与ResizeOptions配合使用
                    .setDownsampleEnabled(true)
                    // 设置Jpeg格式的图片支持渐进式显示
                    .setProgressiveJpegConfig(new ProgressiveJpegConfig() {
                        @Override
                        public int getNextScanNumberToDecode(int scanNumber) {
                            return scanNumber + 2;
                        }

                        @Override
                        public QualityInfo getQualityInfo(int scanNumber) {
                            boolean isGoodEnough = scanNumber >= 5;
                            return ImmutableQualityInfo.of(scanNumber,isGoodEnough,false);
                        }
                    })
                    .setRequestListeners(requestListeners)
                    // 报内存警告时的监听
                    .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                    // 设置内存配置
                    .setBitmapMemoryCacheParamsSupplier(new BitmapMemoryCacheParamsSupplier((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
                    // 设置主磁盘配置
                    .setMainDiskCacheConfig(mainDiskCacheConfig)
                    // 设置小图的磁盘配置
                    .setSmallImageDiskCacheConfig(smallDiskCacheConfig)
                    .build();

        }
        return sImagePipelineConfig;
    }
}
