package cn.sn.zwcx.mvvm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.sn.zwcx.mvvm.bean.gankio.GankDate;

/**
 * Created by on 2018/4/21 17:10.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class SPUtils {
    public static SPUtils instance;
    public static final String SP_NAME = "cn.sn.zwcx.mvvm.information_preference";
    private Context mContext;
    private static SharedPreferences sp;
    private SPUtils(Context context){
        mContext = context;
        sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
    }

    public static SPUtils getInstance(Context context){
        if (instance == null) {
            synchronized (SPUtils.class) {
                if (instance == null) {
                    instance = new SPUtils(context);
                }
            }
        }
        return instance;
    }

    public void saveGankDate(String year, String month, String day){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("year",year);
        edit.putString("month",month);
        edit.putString("day",day);
        edit.apply();
    }

    public GankDate getGankData(){
        String year = sp.getString("year", null);
        String month = sp.getString("month", null);
        String day = sp.getString("day", null);
        return new GankDate(year,month,day);
    }

    public void saveUserPhoto(String photoPath){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("photo",photoPath);
        edit.apply();
    }

    public String getUserPhoto(){
        return sp.getString("photo","");
    }

}
