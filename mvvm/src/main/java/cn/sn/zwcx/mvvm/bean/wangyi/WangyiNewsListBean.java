package cn.sn.zwcx.mvvm.bean.wangyi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by on 2018/3/21 16:51.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiNewsListBean {

    @SerializedName("T1348647909107")
    private ArrayList<WangyiNewsItemBean> newsList;

    public ArrayList<WangyiNewsItemBean> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<WangyiNewsItemBean> newsList) {
        this.newsList = newsList;
    }
}
