package cn.sn.zwcx.mvvm.fragments.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by on 2018/4/3 16:16.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoCustomFragment extends Fragment {
    private final String TAG = GankIoCustomFragment.class.getSimpleName();

    private boolean isCreated = false;

    private String mType = "all";

    public static GankIoCustomFragment newInstance(){
        Bundle args = new Bundle();
        GankIoCustomFragment fragment = new GankIoCustomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated)
            return;
        if (isVisibleToUser) {
            mType = getArguments().getString("type");

        }
        Log.e(TAG, "=============setUserVisibleHint=========" + mType);
    }

}
