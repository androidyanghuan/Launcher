package cn.sn.zwcx.yizi.fragments.home.child.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.fragments.BaseRecycleFragment;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.adapters.WangyiAdapter;
import cn.sn.zwcx.yizi.contract.home.tabs.WangyiContract;
import cn.sn.zwcx.yizi.model.bean.wangyi.WangyiNewsItemBean;
import cn.sn.zwcx.yizi.presenter.home.tabs.WangyiPresenter;

/**
 * Created by on 2018/1/10 20:19.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WangyiFragment extends BaseRecycleFragment<WangyiContract.WangyiPresenter,WangyiContract.IWangyiModel>
        implements WangyiContract.IWangyiView, BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.rv_wangyi)
    RecyclerView rvWangyi;

    private WangyiAdapter mWangyiAdapter;

    public static WangyiFragment newInstance(){
        Bundle bundle = new Bundle();
        WangyiFragment wf = new WangyiFragment();
        wf.setArguments(bundle);
        return wf;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //第一次显示时请求最新的list
        mPresenter.loadLatestList();
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return WangyiPresenter.newInstance();
    }

    @Override
    public void updateContentList(List<WangyiNewsItemBean> list) {
        if (mWangyiAdapter.getData().size() == 0)
            initRecycleView(list);
        else
            mWangyiAdapter.addData(list);
    }

    /** 初始化数据 */
    private void initRecycleView(List<WangyiNewsItemBean> list) {
        mWangyiAdapter = new WangyiAdapter(R.layout.item_recycle_home,list);
        mWangyiAdapter.setOnLoadMoreListener(this,rvWangyi);
        mWangyiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.onItemClick(position, (WangyiNewsItemBean) adapter.getItem(position));
            }
        });
        rvWangyi.setAdapter(mWangyiAdapter);
    }

    @Override
    public void itemNotifyChanged(int position) {
        mWangyiAdapter.notifyItemChanged(position);
    }

    @Override
    public void showNetworkError() {
        mWangyiAdapter.setEmptyView(errorView);
    }

    @Override
    public void showLoadMoreError() {
        mWangyiAdapter.loadMoreFail();
    }

    @Override
    public void showNoMoreDate() {
        mWangyiAdapter.loadMoreEnd(false);
    }

    @Override
    protected void onErrorViewClick(View v) {
        mPresenter.loadLatestList();
    }

    @Override
    protected void showLoading() {
        mWangyiAdapter.setEmptyView(loadingView);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        //初始化一个空list的adapter，网络错误时使用，第一次加载到数据时重新初始化adapter并绑定recycleview
        mWangyiAdapter = new WangyiAdapter(R.layout.item_recycle_home);
        rvWangyi.setLayoutManager(new LinearLayoutManager(mContext));
        rvWangyi.setAdapter(mWangyiAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_wangyi;
    }

    @Override
    public void onLoadMoreRequested() {
        //这里loadMoreComplete要放在前面，避免在Presenter.loadMoreNewsList处理中直接showNoMoreData，出现无限显示加载item
        mWangyiAdapter.loadMoreComplete();
        mPresenter.loadMoreList();
    }
}
