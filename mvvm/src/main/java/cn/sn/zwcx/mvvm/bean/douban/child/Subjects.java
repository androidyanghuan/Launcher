package cn.sn.zwcx.mvvm.bean.douban.child;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.activitys.WebViewActivity;

/**
 * Created by on 2018/4/12 16:50.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class Subjects extends BaseObservable implements Serializable{
    private Rating rating;
    private List<String> genres;
    private String title;
    private List<Casts> casts;
    @SerializedName("collect_count")
    private int collectCount;
    private String originalTitle;
    private String subtype;
    private List<Directors> directors;
    private String year;
    private Images images;
    private String alt;
    private String id;

    @Bindable
    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
        notifyPropertyChanged(BR.rating);
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
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
    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
        notifyPropertyChanged(BR.collectCount);
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

    public String getProtagonist(){
        if (casts == null && casts.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        int size = casts.size();
        for (int i = 0; i < size; i++) {
            sb.append(casts.get(i).getName());
            if (i != size - 1)
                sb.append(File.separator);
        }
        return sb.toString();
    }

    public String getGenre(){
        if (genres == null && genres.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        int size = genres.size();
        for (int i = 0; i < size; i++) {
            sb.append(genres.get(i));
            if (i != size - 1)
                sb.append(File.separator);
        }
        return sb.toString();
    }

    public String getRatingStr(){
        return String.valueOf(rating.getAverage());
    }

    public void onItemClick(View view){
        Context context = view.getContext();
        Bundle args = new Bundle();
        args.putString("url",alt);
        args.putString("source",title);
        args.putString("imgsrc",directors.get(0).getAvatars().getLarge());
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtras(args);
        context.startActivity(intent);
    }

}
