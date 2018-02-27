package cn.sn.zwcx.yizi.fragments.gankio.child;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.sn.zwcx.sdk.adapters.FragmentAdapter;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.fragments.BaseMVPCompatFragment;
import cn.sn.zwcx.sdk.rx.RxBus;
import cn.sn.zwcx.sdk.rx.Subscribe;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.constants.RxBusCode;
import cn.sn.zwcx.yizi.contract.gankio.GankIoMainContract;
import cn.sn.zwcx.yizi.fragments.gankio.child.tabs.GankIoCustomFragment;
import cn.sn.zwcx.yizi.fragments.gankio.child.tabs.GankIoDayFragment;
import cn.sn.zwcx.yizi.fragments.gankio.child.tabs.GankIoWelfareFragment;
import cn.sn.zwcx.yizi.presenter.gankio.GankIoMainPresenter;

/**
 * Created by on 2018/1/22 17:15.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoFragmnet extends BaseMVPCompatFragment<GankIoMainContract.GankIoMainPresenter,GankIoMainContract.IGankIoMainModel>
        implements GankIoMainContract.IGankIoMainView {

    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tl_tabs)
    TabLayout tabLayout;
    @BindView(R.id.vp_fragment)
    ViewPager fragmentPager;
    @BindView(R.id.fab_classify)
    FloatingActionButton classify;

    private List<Fragment> fragments;

    public static GankIoFragmnet newInstance(){
        Bundle args = new Bundle();
        GankIoFragmnet gif = new GankIoFragmnet();
        gif.setArguments(args);
        return gif;
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
        return GankIoMainPresenter.newInstance();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getTabList();
    }

    @Override
    public void showTabList(String[] tabs) {
        Log.e(TAG,"tabs:" + Arrays.toString(tabs));
        int length = tabs.length;
        for (int i = 0; i < length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs[i]));
            switch (i) {
                case 0:
                    fragments.add(GankIoDayFragment.newInstance());
                    break;
                case 1:
                    fragments.add(GankIoCustomFragment.newInstance());
                    break;
                case 2:
                    fragments.add(GankIoWelfareFragment.newInstance());
                    break;
                default:
                    fragments.add(GankIoDayFragment.newInstance());
                    break;
            }
        }
        fragmentPager.setAdapter(new FragmentAdapter(getChildFragmentManager(),fragments));
        fragmentPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(fragmentPager);
        tabLayout.setVerticalScrollbarPosition(0);
        for (int i = 0; i < length; i++)
            tabLayout.getTabAt(i).setText(tabs[i]);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragments = new ArrayList<>();
    }

    @Subscribe(code = RxBusCode.RX_BUS_CODE_GANKIO_SELECT_TO_CHILD)
    public void rxBusEvent(Integer index) {
        Log.e(TAG,"index:" + index);
        toolbar.setVerticalScrollbarPosition(index);
        fragmentPager.setCurrentItem(index);
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (fragmentPager.getCurrentItem() == 1) {
                    if (verticalOffset == 0)
                        classify.show();
                    else
                        classify.hide();
                }
            }
        });
        classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.get().send(RxBusCode.RX_BUS_CODE_GANKIO_PARENT_FAB_CLICK);
            }
        });
        fragmentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                classify.setVisibility(View.GONE);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    classify.setVisibility(View.VISIBLE);
                else
                    classify.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gankio;
    }
}
