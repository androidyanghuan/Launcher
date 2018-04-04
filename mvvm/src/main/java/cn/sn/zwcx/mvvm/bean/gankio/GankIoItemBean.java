package cn.sn.zwcx.mvvm.bean.gankio;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.activitys.WebViewActivity;
import cn.sn.zwcx.mvvm.constants.Constant;

/**
 * Created by on 2018/3/27 17:02.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoItemBean extends BaseObservable implements Serializable {
    @SerializedName("_id")
    private String id;
    private String createdAt;
    private String desc;
    private List<String> images;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    public GankIoItemBean() {

    }

    public GankIoItemBean(String id, String createdAt, String desc, List<String> images, String publishedAt, String source, String type, String url, boolean used, String who) {
        this.id = id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.images = images;
        this.publishedAt = publishedAt;
        this.source = source;
        this.type = type;
        this.url = url;
        this.used = used;
        this.who = who;
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
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }

    @Bindable
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        notifyPropertyChanged(BR.desc);
    }

    @Bindable
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    @Bindable
    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        notifyPropertyChanged(BR.publishedAt);
    }

    @Bindable
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        notifyPropertyChanged(BR.source);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
        notifyPropertyChanged(BR.used);
    }

    @Bindable
    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
        notifyPropertyChanged(BR.who);
    }

    @Override
    public String toString() {
        return "GankIoItemBean{" +
                "id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", desc='" + desc + '\'' +
                ", images=" + images +
                ", publishedAt='" + publishedAt + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", used=" + used +
                ", who='" + who + '\'' +
                '}';
    }

    public void switchClick(View view){
        Context context = view.getContext();
        Bundle args = new Bundle();
        args.putString("url",url);
        args.putString("source",source);
        if (images != null && images.size() > 0)
            args.putString("imgsrc", images.get(0));
        args.putString("imgsrc", Constant.MAIN_NV_USER_ICON);
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtras(args);
        context.startActivity(intent);
    }

}
