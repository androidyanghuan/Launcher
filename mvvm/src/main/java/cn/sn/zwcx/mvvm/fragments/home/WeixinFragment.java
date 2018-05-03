package cn.sn.zwcx.mvvm.fragments.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import cn.sn.zwcx.mvvm.apis.WeixinApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.weixin.WeixinChoiceItemBean;
import cn.sn.zwcx.mvvm.bean.weixin.WeixinChoiceListBean;
import cn.sn.zwcx.mvvm.constants.Constant;
import cn.sn.zwcx.mvvm.databinding.ZhihuFragmentBinding;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/3/23 20:07.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinFragment extends Fragment {
    private final String TAG = WeixinFragment.class.getSimpleName();

    private ZhihuFragmentBinding mBinding;

    private BaseAdapter mAdapter;

    private List<WeixinChoiceItemBean> mItems;

    private RefreshLayout mRefresh,mLoadMore;
    private int mPage = 1;

    public static WeixinFragment newInstance(){
        Bundle args = new Bundle();
        WeixinFragment fragment = new WeixinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.zhihu_fragment,container,false);
        initView();
        getData(true);
        return mBinding.getRoot();
    }

    private void initView() {
        mItems = new ArrayList<>();
        mBinding.zhihuFragmentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.zhihuFragmentRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new BaseAdapter(R.layout.home_fragment_weixin_item, BR.weixin,mItems);
        mBinding.zhihuFragmentRv.setAdapter(mAdapter);

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mLoadMore = refreshLayout;
                mPage++;
                loadMoreData(mPage,false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh = refreshLayout;
                getData(false);
            }
        });
    }

    private void loadMoreData(int page, boolean isShowDialog) {
        RetrofitFactory.getInstance().createApi(WeixinApi.class,WeixinApi.HOST)
                .getWeixinChoiceList(page,20,null,Constant.JU_HE_APP_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<WeixinChoiceListBean>(getActivity(),isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                        if (mLoadMore != null)
                            mLoadMore.finishLoadMore(false);
                    }

                    @Override
                    protected void onSuccees(WeixinChoiceListBean weixinChoiceListBean) throws Exception {
                        WeixinChoiceListBean.Result result = weixinChoiceListBean.getResult();
                        if (result != null) {
                            List<WeixinChoiceItemBean> itemBeans = result.getList();
                            mItems.addAll(itemBeans);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            String reason = weixinChoiceListBean.getReason();
                            ToastUtil.showToast(reason);
                        }
                            if (mLoadMore != null)
                                mLoadMore.finishLoadMore(true);


                    }
                });
    }

    private void getData(boolean isShowDialog) {
        RetrofitFactory.getInstance().createApi(WeixinApi.class,WeixinApi.HOST)
                .getWeixinChoiceList(1,20,null, Constant.JU_HE_APP_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<WeixinChoiceListBean>(getActivity(),isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                        if (mRefresh != null)
                            mRefresh.finishRefresh(false);
                    }

                    @Override
                    protected void onSuccees(WeixinChoiceListBean weixinChoiceListBean) throws Exception {
                        WeixinChoiceListBean.Result result = weixinChoiceListBean.getResult();
                        List<WeixinChoiceItemBean> itemBeans = new ArrayList<>();
                        if (result != null)
                            itemBeans = result.getList();
                        else{
                            String reason = weixinChoiceListBean.getReason();
                            ToastUtil.showToast(reason);
                        }
                        if (itemBeans != null && itemBeans.size() > 0)
                            mItems.clear();
                        mItems.addAll(itemBeans);
                        mAdapter.notifyDataSetChanged();
                        if (mRefresh != null)
                            mRefresh.finishRefresh(true);
                    }
                });
    }
}
