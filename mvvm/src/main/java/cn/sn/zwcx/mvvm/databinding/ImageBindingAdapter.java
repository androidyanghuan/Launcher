package cn.sn.zwcx.mvvm.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.constants.Constant;

/**
 * Created by on 2018/3/5 9:33.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ImageBindingAdapter {
    @BindingAdapter({"icon"})
    public static void loadImage(SimpleDraweeView simpleDraweeView, String url) {
        ImageLoader.loadImage(simpleDraweeView,url);
    }

    @BindingAdapter({"android:src"})
    public static void loadSrc(SimpleDraweeView simpleDraweeView,String url){
        if (TextUtils.isEmpty(url))
            ImageLoader.loadImage(simpleDraweeView, Constant.MAIN_NV_USER_ICON);
        ImageLoader.loadImage(simpleDraweeView,url);
    }

    @BindingAdapter({"srcCompat"})
    public static void loadTitleIcon(AppCompatImageView imageView, String type){
        switch (type) {
            case "Android":
                imageView.setImageResource(R.drawable.ic_vector_title_android);
                break;
            case "App":
                imageView.setImageResource(R.drawable.ic_vector_item_app);
                break;
            case "iOS":
                imageView.setImageResource(R.drawable.ic_vector_title_ios);
                break;
            case "休息视频":
                imageView.setImageResource(R.drawable.ic_vector_title_video);
                break;
            case "前端":
                imageView.setImageResource(R.drawable.ic_vector_title_front);
                break;
            case "拓展资源":
                imageView.setImageResource(R.drawable.ic_vector_item_tuozhan);
                break;
            case "瞎推荐":
                imageView.setImageResource(R.drawable.ic_vector_item_tuijian);
                break;
            case "福利":
                imageView.setImageResource(R.drawable.ic_vector_title_welfare);
                break;

        }
    }

}
