package cn.sn.zwcx.mvvm.bean.douban.child;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

import cn.sn.zwcx.mvvm.BR;

/**
 * Created by on 2018/4/12 16:53.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class Avatars extends BaseObservable implements Serializable{
    private String small;
    private String large;
    private String medium;

    @Bindable
    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
        notifyPropertyChanged(BR.small);
    }

    @Bindable
    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
        notifyPropertyChanged(BR.large);
    }

    @Bindable
    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
        notifyPropertyChanged(BR.medium);
    }

    @Override
    public String toString() {
        return "Avatars{" +
                "small='" + small + '\'' +
                ", large='" + large + '\'' +
                ", medium='" + medium + '\'' +
                '}';
    }
}
