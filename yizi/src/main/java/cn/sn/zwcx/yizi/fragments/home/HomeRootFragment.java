package cn.sn.zwcx.yizi.fragments.home;

import android.os.Bundle;
import android.view.View;

import cn.sn.zwcx.sdk.fragments.BaseCompatFragment;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.fragments.home.child.HomeFragment;

/**
 * Created by on 2018/1/22 16:53.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class HomeRootFragment extends BaseCompatFragment {

    public static HomeRootFragment newInstance(){
        Bundle args = new Bundle();
        HomeRootFragment hrf = new HomeRootFragment();
        hrf.setArguments(args);
        return hrf;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        if (findChildFragment(HomeFragment.class) == null)
            loadRootFragment(R.id.fl_container,HomeFragment.newInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_root;
    }
}
