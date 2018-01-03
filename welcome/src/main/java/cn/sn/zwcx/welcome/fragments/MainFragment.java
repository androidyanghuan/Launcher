package cn.sn.zwcx.welcome.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.util.DisplayMetrics;
import android.util.Log;

import cn.sn.zwcx.welcome.R;

/**
 * Created by on 2017/12/28 19:35.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class MainFragment extends BrowseFragment {
    private final String TAG = "Yang Huan:" + MainFragment.class.getSimpleName();

    /** 背景管理对象 */
    private BackgroundManager mBackgroundManager;

    /** 默认背景 */
    private Drawable mDefaultBackground;

    /** 显示度量对象 */
    private DisplayMetrics mDisplayMetrics;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        loaderRow();

    }

    /** 显示数据 */
    private void loaderRow() {

    }

    /** 准备背景管理 */
    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.mipmap.bg_add_apps);
        mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        setHeadersState(HEADERS_DISABLED);
    }
}
