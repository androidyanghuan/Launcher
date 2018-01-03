package cn.sn.zwcx.yizi.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.sn.zwcx.sdk.anim.ToolbarAnimManager;
import cn.sn.zwcx.sdk.base.BaseCompatActivity;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.fragments.BaseMVPCompatFragment;
import cn.sn.zwcx.sdk.utils.SpUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.app.App;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.home.HomeMainContract;
import cn.sn.zwcx.yizi.presenter.home.HomeMainPresenter;

/**
 * Created by on 2017/12/27 15:03.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HomeFragment extends BaseMVPCompatFragment<HomeMainContract.HomeMainPresenter,HomeMainContract.IHomeMainModel> implements HomeMainContract.IHomeMainView {
    private final String TAG = "Yang Huan:" + HomeFragment.class.getSimpleName();

    @BindView(R.id.home_fragment_appbar)
    AppBarLayout appbar;
    @BindView(R.id.home_fragment_toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_fragment_tab)
    TabLayout tab;
    @BindView(R.id.home_fragment_viewpager)
    ViewPager viewPager;
    @BindView(R.id.home_fragment_fab)
    FloatingActionButton fabDownload;

    /** 存放Fragment的List */
    private List<Fragment> fragments;

    /** Fragment打开DrawerLayout监听 */
    public interface OnOpenDrawerLayoutListener{
        void onOpen();
    }

    protected OnOpenDrawerLayoutListener onOpenDrawerLayoutListener;

    public static HomeFragment newInstance(){
        Bundle args = new Bundle();
        HomeFragment hf = new HomeFragment();
        hf.setArguments(args);
        return hf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOpenDrawerLayoutListener){
            onOpenDrawerLayoutListener = (OnOpenDrawerLayoutListener) context;
        }
        fragments = new ArrayList<>();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onOpenDrawerLayoutListener = null;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getTabList();
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return HomeMainPresenter.newInstance();
    }

    @Override
    public void showTabList(String[] tabs) {
        if (App.ISLOG)
            Log.d(TAG,"tabs:" + Arrays.toString(tabs));
        int length = tabs.length;
        for (int i = 0; i < length; i++){
            tab.addTab(tab.newTab().setText(tabs[i]));
            switch (i){
                case 0:
                 //   fragments.add();
                    break;
                case 1:

                    break;
                case 2:

                    break;
            }
        }
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        toolbar.setTitle(R.string.home_page);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOpenDrawerLayoutListener != null)
                    onOpenDrawerLayoutListener.onOpen();
            }
        });
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0)
                    fabDownload.show();
                else
                    fabDownload.hide();
            }
        });
        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_TITLE,BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_TITLE_STR);
                bundle.putString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_URL,BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_URL_STR);
             //   startNewActivity(WebViewLoadActivity.class,bundle);
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.getMenu().findItem(R.id.night).setChecked(SpUtil.getNightModel(mContext));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.night:
                        item.setChecked(!item.isChecked());
                        SpUtil.setNightModel(mContext,item.isChecked());
                        ((BaseCompatActivity) mActivity).reload();
                        break;
                }
                return false;
            }
        });
        ToolbarAnimManager.animIn(mContext,toolbar);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }
}
