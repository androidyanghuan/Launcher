package cn.sn.zwcx.mvvm.fragments.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import cn.sn.zwcx.mvvm.apis.WangyiApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.wangyi.WangyiNewsItemBean;
import cn.sn.zwcx.mvvm.bean.wangyi.WangyiNewsListBean;
import cn.sn.zwcx.mvvm.databinding.ZhihuFragmentBinding;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/3/21 17:30.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiFragment extends Fragment {

    private final String TAG = WangyiFragment.class.getSimpleName();

    private ZhihuFragmentBinding mBinding;

    private List<WangyiNewsItemBean> mItems;

    private BaseAdapter mAdapter;

    private RefreshLayout refresh,loadMore;

    private int size = 0;

    public static WangyiFragment newInstance(){
        Bundle args = new Bundle();
        WangyiFragment fragment = new WangyiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.zhihu_fragment,container,false);
        initViews();
        getData(true);
        return mBinding.getRoot();
    }

    private void getData(boolean isShowDialog) {
        RetrofitFactory.getInstance().createApi(WangyiApi.class,WangyiApi.HOST)
                .getNewsList(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<WangyiNewsListBean>(getActivity(),isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                        ToastUtil.showToast(R.string.network_error);
                        if (refresh != null)
                            refresh.finishRefresh(false);
                    }

                    @Override
                    protected void onSuccees(WangyiNewsListBean wangyiNewsListBean) throws Exception {
                        mItems.clear();
                        ArrayList<WangyiNewsItemBean> newsList = wangyiNewsListBean.getNewsList();
                        int size = newsList.size();
                        for (int i = 0; i < size; i++) {
                            String url = newsList.get(i).getUrl();
                            if (!TextUtils.isEmpty(url))
                                mItems.add(newsList.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        if (refresh != null)
                            refresh.finishRefresh(true);
                    }
                });
    }

    private void initViews() {
        mItems = new ArrayList<>();
        mAdapter = new BaseAdapter(R.layout.home_fragment_wangyi_item, BR.wangyi,mItems);
        mBinding.zhihuFragmentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.zhihuFragmentRv.setItemAnimator(new DefaultItemAnimator());
        mBinding.zhihuFragmentRv.setAdapter(mAdapter);

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore = refreshLayout;
                size += 20;
                loadMoreData(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh = refreshLayout;
                getData(false);
            }
        });

    }

    private void loadMoreData(boolean isShowDialog) {
        RetrofitFactory.getInstance().createApi(WangyiApi.class,WangyiApi.HOST)
                .getNewsList(size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<WangyiNewsListBean>(getActivity(),isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                        if (loadMore != null)
                            loadMore.finishLoadMore(false);
                    }

                    @Override
                    protected void onSuccees(WangyiNewsListBean wangyiNewsListBean) throws Exception {
                        ArrayList<WangyiNewsItemBean> newsList = wangyiNewsListBean.getNewsList();
                        int size = newsList.size();
                        for (int i = 0; i < size; i++) {
                            if (!TextUtils.isEmpty(newsList.get(i).getUrl()))
                                mItems.add(newsList.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        if (loadMore != null)
                            loadMore.finishLoadMore(true);
                    }
                });
    }
}
