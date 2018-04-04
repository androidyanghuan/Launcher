package cn.sn.zwcx.mvvm.bean.weixin;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.activitys.WebViewActivity;

/**
 * Created by on 2018/3/23 16:19.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinChoiceItemBean extends BaseObservable {

    private String id;
    private String title;
    private String source;
    private String firstImg;
    private String mark;
    private String url;

    public WeixinChoiceItemBean() {

    }

    public WeixinChoiceItemBean(String id, String title, String source, String firstImg, String mark, String url) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.firstImg = firstImg;
        this.mark = mark;
        this.url = url;
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
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        notifyPropertyChanged(BR.source);
    }

    @Bindable
    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
        notifyPropertyChanged(BR.firstImg);
    }

    @Bindable
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
        notifyPropertyChanged(BR.mark);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Override
    public String toString() {
        return "WeixinChoiceItemBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", firstImg='" + firstImg + '\'' +
                ", mark='" + mark + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public void onItemClick(View view){
        Context context = view.getContext();
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle args = new Bundle();
        args.putString("imgsrc",firstImg);
        args.putString("source",source);
        args.putString("url",url);
        intent.putExtras(args);
        context.startActivity(intent);
    }

}
