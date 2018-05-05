package cn.sn.zwcx.mvvm.fragments.gank;

import android.app.Service;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.BR;
import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.adapters.RecycleItemTouchHelper;
import cn.sn.zwcx.mvvm.adapters.RecycleAdapter;
import cn.sn.zwcx.mvvm.apis.GankioApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoCustomListBean;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoItemBean;
import cn.sn.zwcx.mvvm.databinding.GankIoFragmentWelfareBinding;
import cn.sn.zwcx.mvvm.utils.ACache;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/4/10 20:41.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WelfareFragment extends Fragment {
    private final String TAG = WelfareFragment.class.getSimpleName();

    private Context mContext;

    private GankIoFragmentWelfareBinding mBinding;

 //   private BaseAdapter mAdapter;
    private RecycleAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private List<GankIoItemBean> mItems;

    private int mPage = 1;

    private RefreshLayout mRefresh,mLoadMore;

    public static WelfareFragment newInstance(){
        Bundle args = new Bundle();
        WelfareFragment fragment = new WelfareFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gank_io_fragment_welfare,container,false);
        mContext = getActivity();
        initViews();
     //   getData(true,mPage);
        return mBinding.getRoot();
    }

    private void getData(boolean isShowDialog,int page) {
        RetrofitFactory.getInstance().createApi(GankioApi.class,GankioApi.HOST)
                .getGankIoCustomList("福利",10,mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<GankIoCustomListBean>(mContext,isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                        if (mRefresh != null)
                            mRefresh.finishRefresh(false);
                        if (mLoadMore != null)
                            mLoadMore.finishLoadMore(false);
                    }

                    @Override
                    protected void onSuccees(GankIoCustomListBean gankIoCustomListBean) throws Exception {
                        if (mRefresh != null) {
                            mRefresh.finishRefresh(true);
                            mRefresh = null;
                        }
                        if (mLoadMore != null) {
                            mLoadMore.finishLoadMore(true);
                            mLoadMore = null;
                        } else
                            mItems.clear();
                        mItems.addAll(gankIoCustomListBean.getResults());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initViews() {
        mItems = new ArrayList<>();
        //读取
        List<GankIoItemBean> items = (List<GankIoItemBean>) ACache.get(getActivity()).getAsObject("items");
        if (items != null)
            mItems.addAll(items);
        else
            getData(true,mPage);
        //    mAdapter = new BaseAdapter(R.layout.gank_io_fragment_welfare_item, BR.welfare,mItems);
        mAdapter = new RecycleAdapter(mItems,R.layout.gank_io_fragment_welfare_item,BR.welfare);
        mBinding.gankIoWelfareRv.setLayoutManager(new GridLayoutManager(mContext,2));
        mBinding.gankIoWelfareRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemLongClickListener(new RecycleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(RecycleAdapter.RecycleItemViewHolder holder, int positon) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(70);
                if (positon != mItems.size() - 1)
                    mItemTouchHelper.startDrag(holder);
            }
        });
        mBinding.gankIoWelfareRv.setAdapter(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(new RecycleItemTouchHelper(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mBinding.gankIoWelfareRv);
        mBinding.gankIoWelfareSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mLoadMore = refreshLayout;
                mPage ++;
                getData(false,mPage);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh = refreshLayout;
                mPage = 1;
                getData(false,mPage);
            }
        });
    }

}
