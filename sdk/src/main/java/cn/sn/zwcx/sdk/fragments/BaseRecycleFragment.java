package cn.sn.zwcx.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.sn.zwcx.sdk.R;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.base.IBaseModel;

/**
 * Created by on 2018/1/9 19:39.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * <p>
 * 带RecycleView加载状态view的fragment，主要用于显示加载中、空界面、加载失败等状态界面显示
 */

public abstract class BaseRecycleFragment<P extends BasePresenter,M extends IBaseModel> extends BaseMVPCompatFragment<P,M>{

    /** 网络加载异常的View */
    protected View errorView;

    /** 加载中的View */
    protected View loadingView;

    /** 空界面的View */
    protected View emptyView;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        showLoading();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        errorView = inflater.inflate(R.layout.view_network_error,container,false);
        loadingView = inflater.inflate(R.layout.view_loading,container,false);
        emptyView = inflater.inflate(R.layout.view_empty,container,false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                onErrorViewClick(v);
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 网络异常view被点击时触发,交由子类实现
     * @param v
     */
    protected abstract void onErrorViewClick(View v);

    /**
     * 显示加载中的抽象方法交由子类实现
     */
    protected abstract void showLoading();
}
