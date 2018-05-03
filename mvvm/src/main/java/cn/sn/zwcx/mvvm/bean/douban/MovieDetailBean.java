package cn.sn.zwcx.mvvm.bean.douban;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.bean.douban.child.Casts;
import cn.sn.zwcx.mvvm.bean.douban.child.Directors;
import cn.sn.zwcx.mvvm.bean.douban.child.Images;
import cn.sn.zwcx.mvvm.bean.douban.child.Rating;

/**
 * Created by on 2018/4/17 19:32.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class MovieDetailBean extends BaseObservable implements Serializable {
    private Rating rating;
    @SerializedName("reviews_count")
    private int reviewsCount;
    @SerializedName("wish_count")
    private int wishCount;
    @SerializedName("douban_site")
    private String doubanSite;
    private String year;
    private Images images;
    private String alt;
    private String id;
    @SerializedName("mobile_url")
    private String mobileUrl;
    private String title;
    @SerializedName("do_count")
    private String doCount;
    @SerializedName("share_url")
    private String shareUrl;
    @SerializedName("seasons_count")
    private String seasonsCount;
    @SerializedName("schedule_url")
    private String scheduleUrl;
    @SerializedName("episodes_count")
    private String episodesCount;
    private List<String> countries;
    private List<String> genres;
    @SerializedName("collect_count")
    private int collectCount;
    private List<Casts> casts;
    @SerializedName("current_season")
    private String currentSeason;
    @SerializedName("original_title")
    private String originalTitle;
    private String summary;
    private String subtype;
    private List<Directors> directors;
    @SerializedName("comments_count")
    private int commentsCount;
    @SerializedName("ratings_count")
    private int ratingsCount;
    private List<String> aka;

    @Bindable
    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
        notifyPropertyChanged(BR.rating);
    }

    @Bindable
    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
        notifyPropertyChanged(BR.reviewsCount);
    }

    @Bindable
    public int getWishCount() {
        return wishCount;
    }

    public void setWishCount(int wishCount) {
        this.wishCount = wishCount;
        notifyPropertyChanged(BR.wishCount);
    }

    @Bindable
    public String getDoubanSite() {
        return doubanSite;
    }

    public void setDoubanSite(String doubanSite) {
        this.doubanSite = doubanSite;
        notifyPropertyChanged(BR.doubanSite);
    }

    @Bindable
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        notifyPropertyChanged(BR.year);
    }

    @Bindable
    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    @Bindable
    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
        notifyPropertyChanged(BR.alt);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
        notifyPropertyChanged(BR.mobileUrl);
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
    public String getDoCount() {
        return doCount;
    }

    public void setDoCount(String doCount) {
        this.doCount = doCount;
        notifyPropertyChanged(BR.doCount);
    }

    @Bindable
    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
        notifyPropertyChanged(BR.shareUrl);
    }

    @Bindable
    public String getSeasonsCount() {
        return seasonsCount;
    }

    public void setSeasonsCount(String seasonsCount) {
        this.seasonsCount = seasonsCount;
        notifyPropertyChanged(BR.seasonsCount);
    }

    @Bindable
    public String getScheduleUrl() {
        return scheduleUrl;
    }

    public void setScheduleUrl(String scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
        notifyPropertyChanged(BR.scheduleUrl);
    }

    @Bindable
    public String getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(String episodesCount) {
        this.episodesCount = episodesCount;
        notifyPropertyChanged(BR.episodesCount);
    }

    @Bindable
    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
        notifyPropertyChanged(BR.countries);
    }

    @Bindable
    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
        notifyPropertyChanged(BR.genres);
    }

    @Bindable
    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
        notifyPropertyChanged(BR.collectCount);
    }

    @Bindable
    public List<Casts> getCasts() {
        return casts;
    }

    public void setCasts(List<Casts> casts) {
        this.casts = casts;
        notifyPropertyChanged(BR.casts);
    }

    @Bindable
    public String getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(String currentSeason) {
        this.currentSeason = currentSeason;
        notifyPropertyChanged(BR.currentSeason);
    }

    @Bindable
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        notifyPropertyChanged(BR.originalTitle);
    }

    @Bindable
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
        notifyPropertyChanged(BR.summary);
    }

    @Bindable
    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
        notifyPropertyChanged(BR.subtype);
    }

    @Bindable
    public List<Directors> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Directors> directors) {
        this.directors = directors;
        notifyPropertyChanged(BR.directors);
    }

    @Bindable
    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
        notifyPropertyChanged(BR.commentsCount);
    }

    @Bindable
    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
        notifyPropertyChanged(BR.ratingsCount);
    }

    @Bindable
    public List<String> getAka() {
        return aka;
    }

    public void setAka(List<String> aka) {
        this.aka = aka;
        notifyPropertyChanged(BR.aka);
    }

    @Override
    public String toString() {
        return "MovieDetailBean{" +
                "rating=" + rating +
                ", reviewsCount=" + reviewsCount +
                ", wishCount=" + wishCount +
                ", doubanSite='" + doubanSite + '\'' +
                ", year='" + year + '\'' +
                ", images=" + images +
                ", alt='" + alt + '\'' +
                ", id='" + id + '\'' +
                ", mobileUrl='" + mobileUrl + '\'' +
                ", title='" + title + '\'' +
                ", doCount='" + doCount + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", seasonsCount='" + seasonsCount + '\'' +
                ", scheduleUrl='" + scheduleUrl + '\'' +
                ", episodesCount='" + episodesCount + '\'' +
                ", countries=" + countries +
                ", genres=" + genres +
                ", collectCount=" + collectCount +
                ", casts=" + casts +
                ", currentSeason='" + currentSeason + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", summary='" + summary + '\'' +
                ", subtype='" + subtype + '\'' +
                ", directors=" + directors +
                ", commentsCount=" + commentsCount +
                ", ratingsCount=" + ratingsCount +
                ", aka=" + aka +
                '}';
    }
}
