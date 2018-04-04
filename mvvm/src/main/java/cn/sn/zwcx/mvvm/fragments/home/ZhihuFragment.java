package cn.sn.zwcx.mvvm.fragments.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.adapters.BaseAdapter;
import cn.sn.zwcx.mvvm.apis.ZhihuApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.zhihu.ZhihuDailyItemBean;
import cn.sn.zwcx.mvvm.bean.zhihu.ZhihuDailyListBean;
import cn.sn.zwcx.mvvm.databinding.ZhihuFragmentBinding;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/3/20 10:42.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuFragment extends Fragment {
    private final String TAG = ZhihuFragment.class.getSimpleName();

    private ZhihuFragmentBinding mBinding;

    private List<ZhihuDailyItemBean> items;

    private String data;

    private BaseAdapter mAdapter;

    private RefreshLayout refresh,loadMore;

    public static ZhihuFragment newInstance(){
        Bundle args = new Bundle();
        ZhihuFragment fragment = new ZhihuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.zhihu_fragment, container, false);

        items = new ArrayList<>();
        mAdapter = new BaseAdapter(R.layout.home_fragment_zhihu_item, BR.zhihuDaily,items);
        mBinding.zhihuFragmentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.zhihuFragmentRv.setItemAnimator(new DefaultItemAnimator());
        mBinding.zhihuFragmentRv.setAdapter(mAdapter);
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore = refreshLayout;
                loadMoreData(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh = refreshLayout;
                getDate(false);
            }
        });
        getDate(true);
        return mBinding.getRoot();
    }

    private void loadMoreData(boolean isShowDialog) {
        int mData = Integer.parseInt(data);
        mData--;
        RetrofitFactory.getInstance().createApi(ZhihuApi.class,ZhihuApi.HOST)
                .getDailyListWithDate(String.valueOf(mData))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<ZhihuDailyListBean>(getActivity(),isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (loadMore != null)
                            loadMore.finishLoadMore(false);
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                    }

                    @Override
                    protected void onSuccees(ZhihuDailyListBean zhihuDailyListBean) throws Exception {
                        data = zhihuDailyListBean.getDate();
                        items.addAll(zhihuDailyListBean.getStories());
                        mAdapter.notifyDataSetChanged();
                        if (loadMore != null)
                            loadMore.finishLoadMore(true);
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getDate(boolean isShowDialog) {
        RetrofitFactory.getInstance().createApi(ZhihuApi.class,ZhihuApi.HOST)
                .getLastDailyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<ZhihuDailyListBean>(getActivity(),isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (refresh != null)
                            refresh.finishRefresh(false);
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                    }

                    @Override
                    protected void onSuccees(ZhihuDailyListBean zhihuDailyListBean) throws Exception {
                        data = zhihuDailyListBean.getDate();
                        items.clear();
                        items.addAll(zhihuDailyListBean.getStories());
                        mAdapter.notifyDataSetChanged();
                        if (refresh != null)
                            refresh.finishRefresh(true);
                    }

                });

    }
}
