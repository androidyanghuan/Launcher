package cn.sn.zwcx.mvvm.bean.douban.child;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

import cn.sn.zwcx.mvvm.BR;

/**
 * Created by on 2018/4/12 17:03.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class Rating extends BaseObservable implements Serializable {
    private int max;
    private double average;
    private String stars;
    private int min;

    @Bindable
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }

    @Bindable
    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
        notifyPropertyChanged(BR.average);
    }

    @Bindable
    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
        notifyPropertyChanged(BR.stars);
    }

    @Bindable
    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        notifyPropertyChanged(BR.min);
    }

    @Override
    public String toString() {
        return "Rating{" +
                "max=" + max +
                ", average=" + average +
                ", stars='" + stars + '\'' +
                ", min=" + min +
                '}';
    }
}
