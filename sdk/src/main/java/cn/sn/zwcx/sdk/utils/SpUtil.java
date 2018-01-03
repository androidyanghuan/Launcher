package cn.sn.zwcx.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.sdk.global.GlobalApplication;

/**
 * Created by on 2017/12/12 16:53.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class SpUtil {
    /** SharedPreference对象 */
    private static SharedPreferences sp;

    /** SharedPreference名称 */
    private static String mName = "shared_preference_default";

    /** 设置Preference名称 */
    private void setPreferencesName(String name){
        mName = name;
    }

    /** 写入boolean值 */
    public static void putBoolean(Context context,String key,boolean value){
        if (sp == null){
            sp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).apply();
    }

    /** 读取boolean值 */
    public static boolean getBoolean(Context context,String key,boolean defValue){
        if (sp == null) sp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    /** 写入String值 */
    public static void putString(Context context,String key,String value){
        if (sp == null) sp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }

    /** 读取String值 */
    public static String getString(Context context,String key,String defValue){
        if (sp == null) sp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

    /** 写入int值 */
    public static void putInt(Context context,String key,int value){
        if (sp == null) sp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).apply();
    }

    /** 读取int值 */
    public static int getInt(Context context,String key,int defValue){
        if (sp == null) sp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }

    /** 移除指定节点 */
    public static void remove(Context context,String key){
        if (sp == null) sp = context.getSharedPreferences(mName,Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    /** 写入List集合 */
    public static <T> void setDataList(String key, List<T> datas){
        if (null == datas || datas.size() <= 0) return;
        Gson gson = new Gson();
        String json = gson.toJson(datas);
        putString(GlobalApplication.getInstance().getContext(),key,json);
    }

    /** 读取List集合 */
    public static <T> List<T> getDataList(String key,Class<T> cls){
        List<T> datas = new ArrayList<>();
        String str = getString(GlobalApplication.getInstance().getContext(), key, null);
        if (null == str) return datas;
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonParser().parse(str).getAsJsonArray();
        for (JsonElement element : jsonArray){
            datas.add(gson.fromJson(element,cls));
        }
        return datas;
    }

    /** 获取主题的下标 */
    public static int getThemeIndex(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt("IhemeIndex",5);
    }

    /** 设置主题的下标 */
    public static void setThemeIndex(Context context,int index){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("ThemeIndex",index).apply();
    }

    /** 获取夜间模式 */
    public static boolean getNightModel(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("pNightModel",false);
    }

    /** 设置夜间模式 */
    public static void setNightModel(Context context,boolean nightModel){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("pNightModel",nightModel).apply();
    }

}
