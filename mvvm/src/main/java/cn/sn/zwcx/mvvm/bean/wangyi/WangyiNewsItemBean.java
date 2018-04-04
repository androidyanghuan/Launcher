package cn.sn.zwcx.mvvm.bean.wangyi;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.activitys.WebViewActivity;

/**
 * Created by on 2018/3/21 16:39.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiNewsItemBean extends BaseObservable {
    @SerializedName("lmodify")
    private String lmodify;
    @SerializedName("source")
    private String source;
    @SerializedName("title")
    private  String title;
    @SerializedName("url")
    private String url;
    @SerializedName("imgsrc")
    private String imgsrc;

    public WangyiNewsItemBean() {

    }

    public WangyiNewsItemBean(String lmodify, String source, String title, String url, String imgsrc) {
        this.lmodify = lmodify;
        this.source = source;
        this.title = title;
        this.url = url;
        this.imgsrc = imgsrc;
    }

    @Bindable
    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
        notifyPropertyChanged(BR.lmodify);
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
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
    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
        notifyPropertyChanged(BR.imgsrc);
    }

    @Override
    public String toString() {
        return "WangyiNewsItemBean{" +
                "lmodify='" + lmodify + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                '}';
    }

    public void clickItem(View view){
        Intent intent = new Intent(view.getContext(), WebViewActivity.class);
        Bundle args = new Bundle();
        args.putString("imgsrc",imgsrc);
        args.putString("url",url);
        args.putString("source",source);
        intent.putExtras(args);
        view.getContext().startActivity(intent);
    }

}
