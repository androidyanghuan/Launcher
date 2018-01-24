package cn.sn.zwcx.yizi.model.bean.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by on 2018/1/10 9:05.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuDailyItemBean {
    @SerializedName("images")
    private String[] images;
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    private String date;
    public boolean hasFadeIn = false;

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ZhihuDailyItemBean{" +
                "images=" + Arrays.toString(images) +
                ", type=" + type +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", hasFadeIn=" + hasFadeIn +
                '}';
    }
}
