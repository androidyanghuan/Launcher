package cn.sn.zwcx.mvvm.fragments.gank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.adapters.GankIoDayAdapter;
import cn.sn.zwcx.mvvm.apis.GankioApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.gankio.GankDate;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoDayBean;
import cn.sn.zwcx.mvvm.bean.gankio.GankIoItemBean;
import cn.sn.zwcx.mvvm.databinding.GankIoFragmentDayBinding;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.SPUtils;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by on 2018/3/27 22:00.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoDayFragment extends Fragment implements GankIoDayAdapter.OnMoreClickListener {
    private final String TAG = GankIoDayFragment.class.getSimpleName();

    private GankIoFragmentDayBinding mBinding;

    private Context mContext;

    private String mYear,mMonth,mDay;

    private List<GankIoItemBean> mItems;
    private GankIoDayAdapter mAdapter;

    private List<GankIoItemBean> android;
    private List<GankIoItemBean> app;
    private List<GankIoItemBean> ios;
    private List<GankIoItemBean> restMove;
    private List<GankIoItemBean> web;
    private List<GankIoItemBean> resource;
    private List<GankIoItemBean> recommend;
    private List<GankIoItemBean> welfare;
    private RefreshLayout refresh;

    public interface SetCurrentPage{
        void setCurrentPageIndex(int index, String type);
    }

    private SetCurrentPage mSetCurrentPage;

    public void setCurrentPage(SetCurrentPage setCurrentPage){
        this.mSetCurrentPage = setCurrentPage;
    }

    public static GankIoDayFragment newInstance(){
        GankIoDayFragment fragment = new GankIoDayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gank_io_fragment_day,container,false);

        initViews();
        getDate();
        getData(true);
        return mBinding.getRoot();
    }

    private void initViews() {
        mItems = new ArrayList<>();
        android = new ArrayList<>();
        app = new ArrayList<>();
        ios = new ArrayList<>();
        restMove = new ArrayList<>();
        web = new ArrayList<>();
        resource = new ArrayList<>();
        recommend = new ArrayList<>();
        welfare = new ArrayList<>();
        mAdapter = new GankIoDayAdapter(mItems,android,app,ios,restMove,web,resource,recommend,welfare);
        mBinding.gankIoRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.gankIoRv.setItemAnimator(new DefaultItemAnimator());
        mBinding.gankIoRv.setAdapter(mAdapter);
        mBinding.gankIoSrl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh = refreshLayout;
                getCurrentDate();
                getData(false);
            }
        });
        mAdapter.setOnMoreClickListener(this);
    }

    private void getData(boolean isShowDialog) {
        RetrofitFactory.getInstance().createApi(GankioApi.class,GankioApi.HOST)
                .getGankIoDay(mYear,mMonth,mDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<GankIoDayBean>(mContext,isShowDialog) {
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError)
                            ToastUtil.showToast(R.string.network_error);
                        if (refresh != null) {
                            refresh.finishRefresh(false);
                            refresh = null;
                        }
                    }

                    @SuppressLint("CommitPrefEdits")
                    @Override
                    protected void onSuccees(GankIoDayBean gankIoDayBean) throws Exception {
                        GankIoDayBean.Results results = gankIoDayBean.getResults();
                        if (results.getAndroid() == null || results.getAndroid().size() == 0) {
                            int day = Integer.parseInt(mDay);
                            if (day > 01) {
                                day--;
                                mDay = String.valueOf(day);
                            } else {
                                mDay = "31";
                                int month = Integer.parseInt(mMonth);
                                month--;
                                mMonth = String.valueOf(month);

                            }
                            getData(true);
                        } else {
                            SPUtils.getInstance(mContext).saveGankDate(mYear,mMonth,mDay);
                            getGankIoItemData(gankIoDayBean, results);
                            mAdapter.notifyDataSetChanged();
                            if (refresh != null) {
                                refresh.finishRefresh(true);
                                refresh = null;
                            }
                        }
                    }
                });
    }

    /** 把数据转换成干货Item的数据 */
    private void getGankIoItemData(GankIoDayBean gankIoDayBean, GankIoDayBean.Results results) {
        List<String> category = gankIoDayBean.getCategory();
        mItems.clear();
        int categorySize = category.size();
        for (int i = 0; i < categorySize; i++) {
            String categoryName = category.get(i);
            switch (categoryName) {
                case "Android":
                    android.addAll(results.getAndroid());
                    if (android != null && android.size() > 0)
                    mItems.add(android.get(0));
                    break;
                case "App":
                    app.addAll(results.getApp());
                    if (app != null && app.size() > 0)
                    mItems.add(app.get(0));
                    break;
                case "iOS":
                    ios.addAll(results.getIos());
                    if (ios != null && ios.size() > 0)
                    mItems.add(ios.get(0));
                    break;
                case "休息视频":
                    restMove.addAll(results.getRestMove());
                    if (restMove != null && restMove.size() > 0)
                    mItems.add(restMove.get(0));
                    break;
                case "前端":
                    web.addAll(results.getWeb());
                    if (web != null && web.size() > 0)
                    mItems.add(web.get(0));
                    break;
                case "拓展资源":
                    resource.addAll(results.getResource());
                    if (resource != null && resource.size() > 0)
                    mItems.add(resource.get(0));
                    break;
                case "瞎推荐":
                    recommend.addAll(results.getRecommend());
                    if (recommend != null && recommend.size() > 0)
                    mItems.add(recommend.get(0));
                    break;
                case "福利":
                    welfare.addAll(results.getWelfare());
                    if (welfare != null && welfare.size() > 0)
                    mItems.add(welfare.get(0));
                    break;
            }
        }
    }

    /** 获取日期 */
    private void getDate() {
        GankDate gankData = SPUtils.getInstance(mContext).getGankData();
        mYear = gankData.getYear();
        mMonth = gankData.getMonth();
        mDay = gankData.getDay();
        if (TextUtils.isEmpty(mYear) || TextUtils.isEmpty(mMonth) || TextUtils.isEmpty(mDay)) {
            getCurrentDate();
        }
    }

    /** 获取当前日期 */
    private void getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());
        String[] split = date.split("/");
        mYear = split[0];
        mMonth = split[1];
        mDay = split[2];
    }

    @Override
    public void setOnMoreClick(View view, GankIoItemBean itemBean) {
        if (mSetCurrentPage != null) {
            if (itemBean.getType().equals("福利"))
                mSetCurrentPage.setCurrentPageIndex(View.SCROLLBAR_POSITION_RIGHT,itemBean.getType());
            else
                mSetCurrentPage.setCurrentPageIndex(View.SCROLLBAR_POSITION_LEFT, itemBean.getType());
        }
    }

}
