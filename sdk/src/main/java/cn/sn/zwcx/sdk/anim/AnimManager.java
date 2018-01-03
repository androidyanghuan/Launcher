package cn.sn.zwcx.sdk.anim;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by on 2017/12/27 20:03.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class AnimManager {
    /**
     * Alpha & ScaleX 动画
     * Alpha 0 -> 1
     * ScaleX 0.8 -> 1
     * @param context
     * @param view
     * @param startDelay
     * @param duration
     */
    public static void animAlphaAndScaleX(Context context, @NonNull View view, int startDelay,int duration){
        view.setAlpha(0f);
        view.setScaleX(0.8f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            view.animate().alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(startDelay)
                    .setDuration(duration)
                    .setInterpolator(AnimUtil.getFastOutSlowInInterpolator(context))
               //     .setInterpolator(AnimationUtils.loadInterpolator(context,android.R.interpolator.fast_out_slow_in))
                    .start();
        } else {
            view.animate().alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(startDelay)
                    .setDuration(duration)
                    .setInterpolator(AnimationUtils.loadInterpolator(context,android.R.interpolator.linear))
                    .start();
        }
    }

    /**
     * Alpha & Scale 动画
     * alpha 0 -> 1
     * scaleX 0 -> 1
     * scaleY 0 -> 1
     * @param context
     * @param view
     * @param startDelay
     * @param duration
     */
    public static void animAlphaAndScale(Context context,@NonNull View view,int startDelay, int duration){
        view.setAlpha(0f);
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(startDelay)
                .setDuration(duration)
                .setInterpolator(AnimationUtils.loadInterpolator(context,android.R.interpolator.overshoot))
                .start();
    }

}
