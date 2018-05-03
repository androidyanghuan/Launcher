package cn.sn.zwcx.mvvm.bean.douban;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.activitys.WebViewActivity;
import cn.sn.zwcx.mvvm.bean.douban.child.Avatars;

/**
 * Created by on 2018/4/18 11:13.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class DirectorBean extends BaseObservable implements Serializable {
    private String alt;
    private String type;
    private Avatars avatars;
    private String name;
    private String id;

    public DirectorBean() {

    }

    public DirectorBean(String alt, String type, Avatars avatars, String name, String id) {
        this.alt = alt;
        this.type = type;
        this.avatars = avatars;
        this.name = name;
        this.id = id;
    }

    @Bindable
    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
        notifyPropertyChanged(BR.alt);
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
    public Avatars getAvatars() {
        return avatars;
    }

    public void setAvatars(Avatars avatars) {
        this.avatars = avatars;
        notifyPropertyChanged(BR.avatars);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Override
    public String toString() {
        return "DirectorBean{" +
                "alt='" + alt + '\'' +
                ", type='" + type + '\'' +
                ", avatars=" + avatars +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public void onItemClick(View view){
        Context context = view.getContext();
        Bundle args = new Bundle();
        args.putString("imgsrc",avatars.getLarge());
        args.putString("url",alt);
        args.putString("source",name);
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtras(args);
        context.startActivity(intent);
    }

}
