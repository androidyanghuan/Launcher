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
import cn.sn.zwcx.yizi.adapters.WeixinAdapter;
import cn.sn.zwcx.yizi.api.WeixinApi;
import cn.sn.zwcx.yizi.contract.home.tabs.WeixinContract;
import cn.sn.zwcx.yizi.model.bean.weixin.WeixinChoiceItemBean;
import cn.sn.zwcx.yizi.presenter.home.tabs.WeixinPresenter;

/**
 * Created by on 2018/1/15 15:36.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WeixinFragment extends BaseRecycleFragment<WeixinContract.WeixinPresenter,WeixinContract.IWeixinModel>
        implements WeixinContract.IWeixinView, BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.rv_weixin)
    RecyclerView rvWeixin;

    private WeixinAdapter mWeixinAdapter;

    public static WeixinFragment newInstance(){
        Bundle args = new Bundle();
        WeixinFragment wf = new WeixinFragment();
        wf.setArguments(args);
        return wf;
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return WeixinPresenter.newInstance();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //第一次显示时请求最新的list
        mPresenter.loadLatestList();
    }

    @Override
    public void updateContentList(List<WeixinChoiceItemBean> list) {
        if (mWeixinAdapter.getData().size() == 0)
            initRecycleView(list);
        else
            mWeixinAdapter.addData(list);
    }

    /** 初始化数据 */
    private void initRecycleView(List<WeixinChoiceItemBean> list) {
        mWeixinAdapter = new WeixinAdapter(R.layout.item_recycle_home,list);
        mWeixinAdapter.setOnLoadMoreListener(this,rvWeixin);
        mWeixinAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.onItemClick(position, (WeixinChoiceItemBean) adapter.getItem(position));
            }
        });
        rvWeixin.setAdapter(mWeixinAdapter);
    }

    @Override
    public void itemNotifyChanged(int position) {
        mWeixinAdapter.notifyItemChanged(position);
    }

    @Override
    public void showNetworkError() {
        mWeixinAdapter.setEmptyView(errorView);
    }

    @Override
    public void showLoadMoreError() {
        mWeixinAdapter.loadMoreFail();
    }

    @Override
    public void showNoMoreDate() {
        mWeixinAdapter.loadMoreEnd(false);
    }

    @Override
    protected void onErrorViewClick(View v) {
        mPresenter.loadLatestList();
    }

    @Override
    protected void showLoading() {
        mWeixinAdapter.setEmptyView(loadingView);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        //初始化一个空list的adapter，网络错误时使用，第一次加载到数据时重新初始化adapter并绑定recycleview
        mWeixinAdapter = new WeixinAdapter(R.layout.item_recycle_home);
        rvWeixin.setLayoutManager(new LinearLayoutManager(mContext));
        rvWeixin.setAdapter(mWeixinAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_weixin;
    }

    @Override
    public void onLoadMoreRequested() {
        mWeixinAdapter.loadMoreComplete();
        mPresenter.loadLatestList();
    }
}
