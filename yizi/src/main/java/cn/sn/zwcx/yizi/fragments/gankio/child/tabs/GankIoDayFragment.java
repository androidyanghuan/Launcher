package cn.sn.zwcx.yizi.fragments.gankio.child.tabs;

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
import cn.sn.zwcx.sdk.rx.RxBus;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.adapters.GankIoDayAdapter;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoDayContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoDayItemBean;
import cn.sn.zwcx.yizi.presenter.gankio.tabs.GankIoDayPresenter;

/**
 * Created by on 2018/1/23 9:08.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDayFragment extends BaseRecycleFragment<GankIoDayContract.GankIoDayPresenter,GankIoDayContract.IGankIoDayModel>
            implements GankIoDayContract.IGankIoDayView{

    @BindView(R.id.rv_gankio_day)
    RecyclerView gankioView;

    private GankIoDayAdapter mGankIoDayAdapter;

    public static GankIoDayFragment newInstance(){
        Bundle args = new Bundle();
        GankIoDayFragment gidf = new GankIoDayFragment();
        gidf.setArguments(args);
        return gidf;
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

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return GankIoDayPresenter.newInstance();
    }

    @Override
    protected void onErrorViewClick(View v) {
        mPresenter.loadLatestList();
    }

    @Override
    protected void showLoading() {
        mGankIoDayAdapter.setEmptyView(loadingView);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        //初始化一个空list的adapter，网络错误时使用，第一次加载到数据时重新初始化adapter并绑定recycleview
        mGankIoDayAdapter = new GankIoDayAdapter(null);
        gankioView.setLayoutManager(new LinearLayoutManager(mContext));
        gankioView.setAdapter(mGankIoDayAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //第一次显示时请求最新的list
        mPresenter.loadLatestList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gank_io_day;
    }

    @Override
    public void updateContentList(List<GankIoDayItemBean> list) {
        if (mGankIoDayAdapter.getData().size() == 0)
            initRecycleView(list);
        else
            mGankIoDayAdapter.addData(list);
    }

    /** 重新初始化数据 */
    private void initRecycleView(List<GankIoDayItemBean> list) {
        mGankIoDayAdapter = new GankIoDayAdapter(list);
        mGankIoDayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.onItemClick(position,(GankIoDayItemBean) adapter.getItem(position));
            }
        });
        mGankIoDayAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_more:
                        mPresenter.onMoreClick(position, (GankIoDayItemBean) adapter.getItem(position));
                        break;
                    case R.id.ll_refesh:
                        mPresenter.onRefeshClick(position, (GankIoDayItemBean) adapter.getItem(position));
                        break;
                }
            }
        });
        gankioView.setAdapter(mGankIoDayAdapter);
    }

    @Override
    public void itemNotifyChanged(int position) {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showLoadMoreError() {

    }

    @Override
    public void showNoMoreDate() {

    }

    @Override
    public void itemNotifyChanged(GankIoDayItemBean bean, int position) {

    }
}
