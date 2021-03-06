package cn.sn.zwcx.yizi.model.bean.wangyi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by on 2018/1/10 20:10.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 新闻实体类
 */
public class WangyiNewsItemBean implements Serializable {

    /**
     * docid
     */
    @SerializedName("docid")
    private String docid;
    /**
     * 标题
     */
    @SerializedName("title")
    private String title;
    /**
     * 小内容
     */
    @SerializedName("digest")
    private String digest;
    /**
     * 图片地址
     */
    @SerializedName("imgsrc")
    private String imgsrc;
    /**
     * 来源
     */
    @SerializedName("source")
    private String source;
    /**
     * 时间
     */
    @SerializedName("ptime")
    private String ptime;
    /**
     * TAG
     */
    @SerializedName("tag")
    private String tag;

    /**
     * url
     */
    @SerializedName("url")
    private String url;

    public boolean hasFadedIn = false;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WangyiNewsItemBean{" +
                "docid='" + docid + '\'' +
                ", title='" + title + '\'' +
                ", digest='" + digest + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                ", source='" + source + '\'' +
                ", ptime='" + ptime + '\'' +
                ", tag='" + tag + '\'' +
                ", url='" + url + '\'' +
                ", hasFadedIn=" + hasFadedIn +
                '}';
    }
}
