package cn.sn.zwcx.yizi.fragments.home.child.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.fragments.BaseRecycleFragment;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.adapters.ZhihuAdapter;
import cn.sn.zwcx.yizi.contract.home.tabs.ZhihuContract;
import cn.sn.zwcx.yizi.model.bean.zhihu.ZhihuDailyItemBean;
import cn.sn.zwcx.yizi.presenter.home.tabs.ZhihuPresenter;

/**
 * Created by on 2018/1/10 9:50.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class ZhihuFragment extends BaseRecycleFragment<ZhihuContract.ZhihuPresenter,ZhihuContract.IZhihuModel>
            implements ZhihuContract.IZhihuView, BaseQuickAdapter
        .RequestLoadMoreListener {

    private final String TAG = ZhihuFragment.class.getSimpleName();

    @BindView(R.id.rv_zhihu)
    RecyclerView rvZhihu;

    private ZhihuAdapter mZhihuAdapter;

    public static ZhihuFragment newInstance() {
        Bundle bundle = new Bundle();
        ZhihuFragment fragment = new ZhihuFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return ZhihuPresenter.newInstance();
    }

    @Override
    public void updateContentList(List<ZhihuDailyItemBean> list) {
        if (mZhihuAdapter.getData().size() == 0)
            initRecycleView(list);
        else
            mZhihuAdapter.addData(list);
    }


    private void initRecycleView(List<ZhihuDailyItemBean> list) {
        mZhihuAdapter = new ZhihuAdapter(R.layout.item_recycle_home,list);
        mZhihuAdapter.setOnLoadMoreListener(this,rvZhihu);
        mZhihuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.onItemClick(position, (ZhihuDailyItemBean) adapter.getItem(position));
            }
        });
        rvZhihu.setAdapter(mZhihuAdapter);
    }

    @Override
    public void itemNotifyChanged(int position) {
        mZhihuAdapter.notifyItemChanged(position);
    }

    @Override
    public void showNetworkError() {
        mZhihuAdapter.setEmptyView(errorView);
    }

    @Override
    public void showLoadMoreError() {
        mZhihuAdapter.loadMoreFail();
    }

    @Override
    public void showNoMoreDate() {
        mZhihuAdapter.loadMoreEnd(false);
    }

    @Override
    protected void onErrorViewClick(View v) {
        mPresenter.loadLatestList();
    }

    @Override
    protected void showLoading() {
        mZhihuAdapter.setEmptyView(loadingView);
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        //初始化一个空list的adapter，网络错误时使用，第一次加载到数据时重新初始化adapter并绑定recycleview
        mZhihuAdapter = new ZhihuAdapter(R.layout.item_recycle_home);
        rvZhihu.setLayoutManager(new LinearLayoutManager(mContext));
        rvZhihu.setAdapter(mZhihuAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //第一次显示时请求最新的list
        mPresenter.loadLatestList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_zhihu;
    }

    @Override
    public void onLoadMoreRequested() {
        mZhihuAdapter.loadMoreComplete();
        mPresenter.loadMoreList();
    }
}
