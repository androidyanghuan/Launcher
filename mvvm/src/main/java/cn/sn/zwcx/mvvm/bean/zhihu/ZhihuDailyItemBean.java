package cn.sn.zwcx.mvvm.bean.zhihu;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.activitys.WebViewActivity;

/**
 * Created by on 2018/1/10 9:05.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuDailyItemBean extends BaseObservable {
    @SerializedName("images")
    private String[] images;
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    private String date;
    public boolean hasFadeIn = false;

    @Bindable
    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Override
    public String toString() {
        return "ZhihuDailyItemBean{" +
                "images=" + Arrays.toString(images) +
                ", type=" + type +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", hasFadeIn=" + hasFadeIn +
                '}';
    }

    public void onClickItem(View view){
        Bundle args = new Bundle();
        args.putString("id",id);
        Intent intent = new Intent(view.getContext(), WebViewActivity.class);
        intent.putExtras(args);
        view.getContext().startActivity(intent);
    }

}
