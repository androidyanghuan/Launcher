package cn.sn.zwcx.mvvm.bean.gankio;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.sn.zwcx.mvvm.BR;

/**
 * Created by on 2018/3/27 16:47.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDayBean extends BaseObservable {
    private List<String> category;
    private boolean error;
    private Results results;


    public void setCategory(List<String> category) {
        this.category = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public List<String> getCategory() {
        return category;
    }


    public void setError(boolean error) {
        this.error = error;
        notifyPropertyChanged(BR.error);
    }

    @Bindable
    public boolean getError() {
        return error;
    }

    public void setResults(Results results) {
        this.results = results;
        notifyPropertyChanged(BR.results);
    }

    @Bindable
    public Results getResults() {
        return results;
    }

    public void setTitle(){
        int size = category.size();
        for (int i = 0; i < size; i++)
            category.get(i);
    }

    public class Results extends BaseObservable implements Serializable {
    @SerializedName("Android")
    private List<GankIoItemBean> android;

    @SerializedName("iOS")
    private List<GankIoItemBean> ios;

    @SerializedName("休息视频")
    private List<GankIoItemBean> restMove;

    @SerializedName("拓展资源")
    private List<GankIoItemBean> resource;

    @SerializedName("福利")
    private List<GankIoItemBean> welfare;

    @SerializedName("前端")
    private List<GankIoItemBean> web;

    @SerializedName("App")
    private List<GankIoItemBean> app;

    @SerializedName("瞎推荐")
    private List<GankIoItemBean> recommend;

        @Bindable
        public List<GankIoItemBean> getWeb() {
            return web;
        }

        public void setWeb(List<GankIoItemBean> web) {
            this.web = web;
            notifyPropertyChanged(BR.web);
        }

        @Bindable
        public List<GankIoItemBean> getApp() {
            return app;
        }

        public void setApp(List<GankIoItemBean> app) {
            this.app = app;
            notifyPropertyChanged(BR.app);
        }

        @Bindable
        public List<GankIoItemBean> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<GankIoItemBean> recommend) {
            this.recommend = recommend;
            notifyPropertyChanged(BR.recommend);
        }

        @Bindable
        public List<GankIoItemBean> getAndroid() {
            return android;
        }

        public void setAndroid(List<GankIoItemBean> android) {
            this.android = android;
            notifyPropertyChanged(BR.android);
        }

        @Bindable
        public List<GankIoItemBean> getIos() {
            return ios;
        }

        public void setIos(List<GankIoItemBean> ios) {
            this.ios = ios;
            notifyPropertyChanged(BR.ios);
        }

        @Bindable
        public List<GankIoItemBean> getRestMove() {
            return restMove;
        }

        public void setRestMove(List<GankIoItemBean> restMove) {
            this.restMove = restMove;
            notifyPropertyChanged(BR.restMove);
        }

        @Bindable
        public List<GankIoItemBean> getResource() {
            return resource;
        }

        public void setResource(List<GankIoItemBean> resource) {
            this.resource = resource;
            notifyPropertyChanged(BR.resource);
        }

        @Bindable
        public List<GankIoItemBean> getWelfare() {
            return welfare;
        }

        public void setWelfare(List<GankIoItemBean> welfare) {
            this.welfare = welfare;
            notifyPropertyChanged(BR.welfare);
        }

        @Override
        public String toString() {
            return "Results{" +
                    "android=" + android +
                    ", ios=" + ios +
                    ", restMove=" + restMove +
                    ", resource=" + resource +
                    ", welfare=" + welfare +
                    ", web=" + web +
                    ", app=" + app +
                    ", recommend=" + recommend +
                    '}';
        }

        public void setTitle(){
            if (android != null && android.size() > 0) {

            }
        }

        public String setImage(){
            int size = android.size();
            for (int i = 0; i < size; i++) {
                List<String> images = android.get(i).getImages();
                if (images != null && images.size() > 0)
                    return images.get(0);
            }
            return null;
        }

    }
}
