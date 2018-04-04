package cn.sn.zwcx.mvvm.bean.zhihu;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.sn.zwcx.mvvm.BR;

/**
 * Created by on 2018/1/10 15:31.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */
public class ZhihuDailyDetailBean extends BaseObservable {
    @SerializedName("body")
    private String body;
    @SerializedName("image_source")
    private String image_source;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("share_url")
    private String mShareUrl;
    @SerializedName("ga_prefix")
    private String ga_prefix;
    @SerializedName("type")
    private int type;
    @SerializedName("id")
    private int id;
    @SerializedName("js")
    private List<String> js;
    @SerializedName("css")
    private List<String> css;

    @Bindable
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        notifyPropertyChanged(BR.body);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    @Bindable
    public String getShareUrl() {
        return mShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.mShareUrl = shareUrl;
        notifyPropertyChanged(BR.shareUrl);
    }

    @Bindable
    public List<String> getCss() {
        return css;
    }

    @Bindable
    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
        notifyPropertyChanged(BR.image_source);
    }

    @Bindable
    public String getmShareUrl() {
        return mShareUrl;
    }

    public void setmShareUrl(String mShareUrl) {
        this.mShareUrl = mShareUrl;
        notifyPropertyChanged(BR.mShareUrl);
    }

    @Bindable
    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
        notifyPropertyChanged(BR.ga_prefix);
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public List<String> getJs() {
        return js;
    }

    public void setJs(List<String> js) {
        this.js = js;
        notifyPropertyChanged(BR.js);
    }

    public void setCss(List<String> css) {
        this.css = css;
        notifyPropertyChanged(BR.css);
    }
}
