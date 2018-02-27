package cn.sn.zwcx.yizi.fragments.gankio.child.tabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cocosw.bottomsheet.BottomSheet;

import java.util.List;

import butterknife.BindView;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.fragments.BaseRecycleFragment;
import cn.sn.zwcx.sdk.rx.RxBus;
import cn.sn.zwcx.sdk.rx.Subscribe;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.adapters.GankIoCustomAdapter;
import cn.sn.zwcx.yizi.contract.gankio.tabs.GankIoCustomContract;
import cn.sn.zwcx.yizi.model.bean.gankio.GankIoCustomItemBean;
import cn.sn.zwcx.yizi.presenter.gankio.tabs.GankIoCustomPresenter;

import static cn.sn.zwcx.yizi.constants.RxBusCode.RX_BUS_CODE_GANKIO_CUSTOM_TYPE;
import static cn.sn.zwcx.yizi.constants.RxBusCode.RX_BUS_CODE_GANKIO_PARENT_FAB_CLICK;

/**
 * Created by on 2018/1/24 8:50.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoCustomFragment extends BaseRecycleFragment<GankIoCustomContract.GankIoCustomPresenter,GankIoCustomContract.IGankIoCustomModel>
            implements GankIoCustomContract.IGankIoCustomView,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.rv_gankio_custom)
    RecyclerView customView;

    private View headView;
    private String mCustomType = "all";
    private GankIoCustomAdapter mGankIoCustomAdapter;

    public static GankIoCustomFragment newInstance(){
        Bundle args = new Bundle();
        GankIoCustomFragment gicf = new GankIoCustomFragment();
        gicf.setArguments(args);
        return gicf;
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
        return GankIoCustomPresenter.newInstance();
    }

    @Override
    public void updateContentList(List<GankIoCustomItemBean> list) {
        if (mGankIoCustomAdapter.getData().size() == 0)
            initRecycleView(list);
        else
            mGankIoCustomAdapter.addData(list);
    }

    /** 初始化视图 */
    private void initRecycleView(List<GankIoCustomItemBean> list) {
        mGankIoCustomAdapter = new GankIoCustomAdapter(list);
        mGankIoCustomAdapter.setOnLoadMoreListener(this,customView);
        mGankIoCustomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //由于有headview click position需要+1 adapter.getItem返回的是数据list的position，所以不用+1
                mPresenter.onItemClick(position + 1, (GankIoCustomItemBean) adapter.getItem(position));
            }
        });
        initHeadView();
        mGankIoCustomAdapter.addHeaderView(headView);
        customView.setAdapter(mGankIoCustomAdapter);
    }

    /** 初始化头部视图 */
    private void initHeadView() {
        if (headView == null)
            headView = View.inflate(mContext,R.layout.sub_gank_io_custom_head,null);
        TextView headName = headView.findViewById(R.id.tv_type_name);
        headView.findViewById(R.id.ll_choose_catalogue).setOnClickListener(new CatalogueClickListenerImp(headName));
        headName.setText(mCustomType);
    }

    @Override
    public void itemNotifyChanged(int position) {
        mGankIoCustomAdapter.notifyItemChanged(position);
    }

    @Override
    public void showNetworkError() {
        mGankIoCustomAdapter.setEmptyView(errorView);
    }

    @Override
    public void showLoadMoreError() {
        mGankIoCustomAdapter.loadMoreFail();
    }

    @Override
    public void showNoMoreDate() {
        mGankIoCustomAdapter.loadMoreEnd(false);
    }

    @Override
    public String getCustomType() {
        return mCustomType;
    }

    @Override
    public void refreshCustomList(List<GankIoCustomItemBean> list) {
        if (mGankIoCustomAdapter.getData().size() == 0)
            initRecycleView(list);
        else
            mGankIoCustomAdapter.replaceData(list);
        initHeadView();
    }

    @Override
    protected void onErrorViewClick(View v) {
        mPresenter.loadLatestList();
    }

    @Override
    protected void showLoading() {
        mGankIoCustomAdapter.setEmptyView(loadingView);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        //初始化一个空list的adapter，网络错误时使用，第一次加载到数据时重新初始化adapter并绑定recycleview
        mGankIoCustomAdapter = new GankIoCustomAdapter(null);
        customView.setLayoutManager(new LinearLayoutManager(mContext));
        customView.setAdapter(mGankIoCustomAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gankio_custom;
    }

    @Override
    public void onLoadMoreRequested() {
        //这里loadMoreComplete要放在前面，避免在Presenter.loadMoreNewsList处理中直接showNoMoreData，出现无限显示加载item
        mGankIoCustomAdapter.loadMoreComplete();
        mPresenter.loadMoreList();
    }

    public class CatalogueClickListenerImp implements View.OnClickListener{

        private TextView headName;

        public CatalogueClickListenerImp(TextView headName) {
            this.headName = headName;
        }

        @Override
        public void onClick(View v) {
            showBottomSheet(headName);
        }
    }

    /** 显示底部覆盖 */
    private void showBottomSheet(final TextView headName) {
        new BottomSheet.Builder(getActivity(),R.style.BottomSheet_StyleDialog)
                .title(R.string.selected_classify)
                .sheet(R.menu.gank_io_custom_bottom_sheet)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.item_gank_all:
                                mCustomType = "all";
                                break;
                            case R.id.item_gank_app:
                                mCustomType = "App";
                                break;
                            case R.id.item_gank_android:
                                mCustomType = "Android";
                                break;
                            case R.id.item_gank_ios:
                                mCustomType = "IOS";
                                break;
                            case R.id.item_gank_front:
                                mCustomType = "前端";
                                break;
                            case R.id.item_gank_video:
                                mCustomType = "休息视频";
                                break;
                            case R.id.item_gank_tuozhan:
                                mCustomType = "拓展视频";
                                break;

                        }
                        headName.setText(mCustomType);
                        mPresenter.customTypeChange(mCustomType);
                    }
                })
                .show();
    }

    /**
     * parent fab click
     */
    @Subscribe(code = RX_BUS_CODE_GANKIO_PARENT_FAB_CLICK)
    public void rxBusEvent() {
        showBottomSheet((TextView) headView.findViewById(R.id.tv_type_name));
    }

    /**
     * day页面查看更多事件触发
     */
    @Subscribe(code = RX_BUS_CODE_GANKIO_CUSTOM_TYPE)
    public void rxBusEvent(String customType) {
        mCustomType = customType;
        mPresenter.customTypeChange(mCustomType);
    }

}
