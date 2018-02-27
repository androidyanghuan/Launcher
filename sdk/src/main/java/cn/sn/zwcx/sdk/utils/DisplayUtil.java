package cn.sn.zwcx.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsic;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.sn.zwcx.sdk.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by on 2018/1/8 11:14.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class DisplayUtil {
    /**
     * 将px值转换为dp值
     */
    public static int px2dp(float pxValue) {
        float density = AppUtil.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * 将dp值转换为px值
     */
    public static int dp2px(float dpValue) {
        float density = AppUtil.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 将px值转换为sp值
     */
    public static int px2sp(float pxValue) {
        float scaledDensity = AppUtil.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scaledDensity + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(float dpValue) {
        float scaledDensity = AppUtil.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scaledDensity + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidthPixels(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeightPixels(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 将一个view转换成bitmap位图
     * @param view 要转换的View
     * @return view转换的bitmap
     */
    public static Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    /**
     * 获取模糊虚化的bitmap
     * @param context
     * @param bitmap  要模糊的图片
     * @param radius  模糊等级 >=0 && <=25
     * @return
     */
    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap, int radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            return blurBitmap(context,bitmap,radius);
        }
        return bitmap;
    }

    /**
     * android系统的模糊方法
     *  api必须大于16才有效
     * @param bitmap 要模糊的图片
     * @param radius 模糊等级 >=0 && <=25
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, int radius) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur sib = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation allIn = Allocation.createFromBitmap(rs,bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs,bmp);
            sib.setRadius(radius);
            sib.setInput(allIn);
            sib.forEach(allOut);
            allOut.copyTo(bmp);
            bitmap.recycle();
            rs.destroy();
            return bmp;
        }
        return bitmap;
    }

    /**
     * 显示网络虚化图片
     * @param context   context
     * @param imgUrl    图片url
     * @param imageView 要显示的imageview
     */
    public static void displayBlurImg(Context context, final String imgUrl, ImageView imageView) {
        // "23":模糊度；"4":图片缩放4倍后再进行模糊
        Glide.with(context)
                .load(imgUrl)
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(300)
                .bitmapTransform(new BlurTransformation(context,23,4))
                .into(imageView);
    }

    /**
     * 显示本地虚化图片
     * @param context   context
     * @param file      本地图片file
     * @param imageView 要显示的imageview
     */
    public static void displayBlurImg(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(300)
                .bitmapTransform(new BlurTransformation(context,23,4))
                .into(imageView);
    }

    /**
     * 显示资源虚化图片
     * @param context    context
     * @param resourceId 图片资源id
     * @param imageView  要显示的imageview
     */
    public static void displayBlurImg(Context context, Integer resourceId, ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(300)
                .bitmapTransform(new BlurTransformation(context,23,4))
                .into(imageView);
    }

}
