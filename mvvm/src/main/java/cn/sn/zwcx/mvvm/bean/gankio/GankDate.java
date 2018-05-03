package cn.sn.zwcx.mvvm.bean.gankio;

/**
 * Created by on 2018/4/21 17:22.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankDate {
    private String year,month,day;

    public GankDate(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
