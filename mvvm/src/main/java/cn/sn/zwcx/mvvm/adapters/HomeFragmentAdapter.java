package cn.sn.zwcx.mvvm.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import cn.sn.zwcx.mvvm.R;

/**
 * Created by on 2018/3/20 11:07.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private Context mContext;

    private String[] tabs;

    private int tabId;

    public HomeFragmentAdapter(FragmentManager fm, List<Fragment> fragments, Context context,int tabId) {
        super(fm);
        this.fragments = fragments;
        mContext = context;
        this.tabId = tabId;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        tabs = mContext.getResources().getStringArray(tabId);
        return tabs[position];
    }

        @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }
}
