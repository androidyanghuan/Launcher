package cn.sn.zwcx.yizi.fragments.gankio.child.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.fragments.BaseRecycleFragment;
import cn.sn.zwcx.sdk.rx.RxBus;
import cn.sn.zwcx.sdk.rx.Subscribe;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.adapters.GankIoWelfareAdapter;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoWelfareContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoWelfareItemBean;
import cn.sn.zwcx.yizi.presenter.gankio.tabs.GankIoWelfarePresenter;

import static cn.sn.zwcx.yizi.constants.RxBusCode.RX_BUS_CODE_GANKIO_WELFARE_TYPE;

/**
 * Created by on 2018/1/27 17:06.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoWelfareFragment extends BaseRecycleFragment<GankIoWelfareContract.GankIoWelfarePresenter,GankIoWelfareContract.IGankIoWelfareModel>
        implements GankIoWelfareContract.IGankIoWelfareView,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.rv_gankio_welfare)
    RecyclerView welfareView;

    private GankIoWelfareAdapter welfareAdapter;

    public static GankIoWelfareFragment newInstance(){
        Bundle args = new Bundle();
        GankIoWelfareFragment giwf = new GankIoWelfareFragment();
        giwf.setArguments(args);
        return giwf;
    }

    @Override
    public void initData() {
        super.initData();
        RxBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.loadLatestList();
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return GankIoWelfarePresenter.newInstance();
    }

    @Override
    public void updateContentList(List<GankIoWelfareItemBean> list) {
        if (welfareAdapter.getData().size() == 0)
            initRecycleView(list);
        else
            welfareAdapter.addData(list);
    }


    private void initRecycleView(List<GankIoWelfareItemBean> list) {
        welfareAdapter = new GankIoWelfareAdapter(R.layout.item_gankio_welfare,list);
        welfareAdapter.setOnLoadMoreListener(this,welfareView);
        welfareAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.onItemClick(position, (GankIoWelfareItemBean) adapter.getItem(position));
            }
        });
        welfareView.setAdapter(welfareAdapter);
        //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
        welfareView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void itemNotifyChanged(int position) {

    }

    @Override
    public void showNetworkError() {
        welfareAdapter.setEmptyView(errorView);
    }

    @Override
    public void showLoadMoreError() {
        welfareAdapter.loadMoreFail();
    }

    @Override
    public void showNoMoreDate() {
        welfareAdapter.loadMoreEnd(false);
    }

    @Override
    protected void onErrorViewClick(View v) {
        mPresenter.loadLatestList();
    }

    @Override
    protected void showLoading() {
        welfareAdapter.setEmptyView(loadingView);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        //初始化一个空list的adapter，网络错误时使用，第一次加载到数据时重新初始化adapter并绑定recycleview
        welfareAdapter = new GankIoWelfareAdapter(R.layout.item_gankio_welfare);
        welfareView.setLayoutManager(new LinearLayoutManager(mContext));
        welfareView.setAdapter(welfareAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gankio_welfare;
    }

    @Override
    public void onLoadMoreRequested() {
        //这里loadMoreComplete要放在前面，避免在Presenter.loadMoreNewsList处理中直接showNoMoreData，出现无限显示加载item
        welfareAdapter.loadMoreComplete();
        mPresenter.loadMoreList();
    }

    /**
     * day页面查看更多事件触发
     */
    @Subscribe(code = RX_BUS_CODE_GANKIO_WELFARE_TYPE)
    public void rxBusEvent() {
        welfareView.smoothScrollToPosition(0);
    }

}
