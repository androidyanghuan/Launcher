package cn.sn.zwcx.mvvm.bean.douban;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.bean.douban.child.Subjects;

/**
 * Created by on 2018/4/12 16:41.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HotMovieBean extends BaseObservable {
    private int count;
    private int start;
    private int total;
    private List<Subjects> subjects;
    private String title;

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    @Bindable
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
        notifyPropertyChanged(BR.start);
    }

    @Bindable
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public List<Subjects> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subjects> subjects) {
        this.subjects = subjects;
        notifyPropertyChanged(BR.subjects);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Override
    public String toString() {
        return "HotMovieBean{" +
                "count=" + count +
                ", start=" + start +
                ", total=" + total +
                ", subjects=" + subjects +
                ", title='" + title + '\'' +
                '}';
    }

}
