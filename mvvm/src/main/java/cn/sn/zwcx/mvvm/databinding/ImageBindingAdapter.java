package cn.sn.zwcx.mvvm.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;

import java.io.File;
import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.bean.douban.child.Casts;
import cn.sn.zwcx.mvvm.constants.Constant;
import cn.sn.zwcx.mvvm.utils.DisplayUtils;

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
        else
            ImageLoader.loadImage(simpleDraweeView,url);
    }

    @BindingAdapter({"android:src"})
    public static void loadSrc(PhotoView photoView,String url){
        if (TextUtils.isEmpty(url))
            url = Constant.MAIN_NV_USER_ICON;
        Glide.with(photoView.getContext())
                .load(url)
                .into(photoView);
    }

    @BindingAdapter({"setMovieIcon"})
    public static void setMovieIcon(ImageView imageView, String url){
        if (TextUtils.isEmpty(url))
            url = Constant.MAIN_NV_USER_ICON;
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .into(imageView);
    }

    @BindingAdapter({"setGankIoItemIcon"})
    public static void setGankIoItemIcon(ImageView imageView, String url){
        if (TextUtils.isEmpty(url))
            url = Constant.MAIN_NV_USER_ICON;
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @BindingAdapter({"headBlurBackground"})
    public static void headBlurBackground(ImageView imageView, String url){
        if (TextUtils.isEmpty(url))
            url = Constant.MAIN_NV_USER_ICON;
        DisplayUtils.displayBlurImg(imageView.getContext(),url,imageView);
    }

    @BindingAdapter({"setRating"})
    public static void setRating(TextView textView, double ratingNum){
        String rating = textView.getContext().getString(R.string.rating_str);
        textView.setText(rating + ratingNum);
    }

    @BindingAdapter({"setCollectCount"})
    public static void setCollectCount(TextView textView,int collectCount){
        String personGrade = textView.getContext().getString(R.string.person_grade);
        textView.setText(" " + collectCount + personGrade);
    }


    @BindingAdapter({"setProtagonist"})
    public static void setProtagonist(TextView textView, List<Casts> casts){
        if (casts == null )
            return;
        StringBuilder sb = new StringBuilder();
        int size = casts.size();
        for (int i = 0; i < size; i++) {
            sb.append(casts.get(i).getName());
            if (i != size - 1)
                sb.append(File.separator);
        }
        textView.setText(sb.toString());
    }

    @BindingAdapter({"setGenre"})
    public static void setGenre(TextView textView,List<String> genres){
        if (genres == null )
            return;
        String type = textView.getContext().getString(R.string.type_str);
        StringBuilder sb = new StringBuilder(type);
        int size = genres.size();
        for (int i = 0; i < size; i++) {
            sb.append(genres.get(i));
            if (i != size - 1)
                sb.append(File.separator);
        }
        textView.setText(sb.toString());
    }

    @BindingAdapter({"setShowTime"})
    public static void setShowTime(TextView textView,String year){
        String date = textView.getContext().getString(R.string.show_data);
        textView.setText(date + year);
    }

    @BindingAdapter({"setCountrie"})
    public static void setCountrie(TextView textView, List<String> countries){
        if (countries == null)
            return;
        String country = textView.getContext().getString(R.string.production_country);
        StringBuilder sb = new StringBuilder(country);
        int size = countries.size();
        for (int i = 0; i < size; i++) {
            if (i != size - 1)
                sb.append(countries.get(i) + File.separator);
            else
                sb.append(countries.get(i));
        }
        textView.setText(sb.toString());
    }

    @BindingAdapter({"setAlias"})
    public static void setAlias(TextView textView,List<String> akas){
        if (akas == null)
            return;
        StringBuilder sb = new StringBuilder();
        int size = akas.size();
        for (int i = 0; i < size; i++) {
            if (i != size - 1)
                sb.append(akas.get(i) + File.separator);
            else
                sb.append(akas.get(i));
        }
        textView.setText(sb.toString());
    }

    @BindingAdapter({"android:srcCompat"})
    public static void showTitleIcon(AppCompatImageView imageView, String type){
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

    @BindingAdapter({"setPublishedTime"})
    public static void showPublishedTime(TextView textView, String publishedAt){
        if (!TextUtils.isEmpty(publishedAt) && publishedAt.contains("T")) {
            String[] split = publishedAt.split("T");
            textView.setText(split[0]);
        } else
            textView.setText(publishedAt);
    }

    @BindingAdapter({"setAuthorName"})
    public static void showAuthor(TextView textView, String author){
        if (!TextUtils.isEmpty(author))
            textView.setText(author);
        else
            textView.setText("佚名");
    }

}
