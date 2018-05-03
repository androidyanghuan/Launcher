package cn.sn.zwcx.mvvm.bean.douban.child;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.sn.zwcx.mvvm.BR;

/**
 * Created by on 2018/4/12 16:53.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class Casts extends BaseObservable implements Serializable {
    private String alt;
    private Avatars avatars;
    private String name;
    private String id;

    @Bindable
    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
        notifyPropertyChanged(BR.alt);
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
        return "Casts{" +
                "alt='" + alt + '\'' +
                ", avatars=" + avatars +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
