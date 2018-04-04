package cn.sn.zwcx.mvvm.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.adapters.HomeFragmentAdapter;
import cn.sn.zwcx.mvvm.databinding.GankIoFragmentBinding;
import cn.sn.zwcx.mvvm.fragments.gank.GankIoCustomFragment;
import cn.sn.zwcx.mvvm.fragments.gank.GankIoDayFragment;

/**
 * Created by on 2018/3/27 17:47.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoFragment extends Fragment implements GankIoDayFragment.SetCurrentPage {
    private final String TAG = GankIoFragment.class.getSimpleName();

    private Context mContext;

    private String mTitle;

    private GankIoFragmentBinding mBinding;

    private List<Fragment> mPages;

    private HomeFragmentAdapter mAdapter;

    private GankIoDayFragment gankIoDayFragment;

    private GankIoCustomFragment gankIoCustomFragment;

    public static GankIoFragment newInstance(String title){
        GankIoFragment fragment = new GankIoFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mTitle = getArguments().getString("title");
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gank_io_fragment,container,false);
        mBinding.gankIoTitle.setText(mTitle);
        initViews();

        return mBinding.getRoot();
    }

    private void initViews() {
        mPages = new ArrayList<>();
        gankIoDayFragment = GankIoDayFragment.newInstance();
        gankIoCustomFragment = GankIoCustomFragment.newInstance();
        mPages.add(gankIoDayFragment);
        mPages.add(gankIoCustomFragment);
        mPages.add(GankIoCustomFragment.newInstance());
        mAdapter = new HomeFragmentAdapter(getActivity().getSupportFragmentManager(),mPages,mContext,R.array.gank_io_fragment_tab);
        mBinding.gankIoVp.setAdapter(mAdapter);
        mBinding.gankIoVp.setCurrentItem(View.SCROLLBAR_POSITION_DEFAULT);
        mBinding.gankIoVp.setOffscreenPageLimit(View.SCROLLBAR_POSITION_RIGHT);
        mBinding.gankIoTl.setupWithViewPager(mBinding.gankIoVp);
        gankIoDayFragment.setCurrentPage(this);
    }

    @Override
    public void setCurrentPageIndex(int index, String type) {
        Bundle args = new Bundle();
        args.putString("type",type);
        gankIoCustomFragment.setArguments(args);
        mBinding.gankIoVp.setCurrentItem(index);
     //   mAdapter.notifyDataSetChanged();

    }

}
