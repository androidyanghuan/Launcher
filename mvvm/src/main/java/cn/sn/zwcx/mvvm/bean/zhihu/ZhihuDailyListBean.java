package cn.sn.zwcx.mvvm.bean.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by on 2018/1/10 9:28.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuDailyListBean {
    @SerializedName("date")
    private String date;
    @SerializedName("top_stories")
    private ArrayList<ZhihuDailyItemBean> mZhihuDailyItems;
    @SerializedName("stories")
    private ArrayList<ZhihuDailyItemBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ZhihuDailyItemBean> getmZhihuDailyItems() {
        return mZhihuDailyItems;
    }

    public void setmZhihuDailyItems(ArrayList<ZhihuDailyItemBean> mZhihuDailyItems) {
        this.mZhihuDailyItems = mZhihuDailyItems;
    }

    public ArrayList<ZhihuDailyItemBean> getStories() {
        return stories;
    }

    public void setStories(ArrayList<ZhihuDailyItemBean> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "ZhihuDailyListBean{" +
                "date='" + date + '\'' +
                ", mZhihuDailyItems=" + mZhihuDailyItems +
                ", stories=" + stories +
                '}';
    }
}
