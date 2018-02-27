package cn.sn.zwcx.yizi.fragments.gankio;

import android.os.Bundle;
import android.view.View;

import cn.sn.zwcx.sdk.fragments.BaseCompatFragment;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.fragments.gankio.child.GankIoFragmnet;

/**
 * Created by on 2018/1/22 17:08.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class GankIoRootFragment extends BaseCompatFragment {

    public static GankIoRootFragment newInstance(){
        Bundle args = new Bundle();
        GankIoRootFragment girf = new GankIoRootFragment();
        girf.setArguments(args);
        return girf;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        if (findChildFragment(GankIoFragmnet.class) == null)
            loadRootFragment(R.id.fl_container,GankIoFragmnet.newInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gankio_root;
    }
}
