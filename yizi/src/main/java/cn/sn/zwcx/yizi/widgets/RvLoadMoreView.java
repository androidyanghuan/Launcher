package cn.sn.zwcx.yizi.widgets;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

import cn.sn.zwcx.yizi.R;

/**
 * Created by on 2018/1/10 10:39.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RvLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.item_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
