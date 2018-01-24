package cn.sn.zwcx.yizi.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sn.zwcx.yizi.widgets.RvLoadMoreView;

/**
 * Created by on 2018/1/10 10:31.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BaseCompatAdapter<T,K extends BaseViewHolder> extends BaseQuickAdapter<T,K>{

    public BaseCompatAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
        init();
    }

    public BaseCompatAdapter(@Nullable List<T> data) {
        super(data);
        init();
    }

    public BaseCompatAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        init();
    }

    /** 初始化 */
    private void init() {
        setLoadMoreView(new RvLoadMoreView());
        setEnableLoadMore(true);
        // 开启默认动画载入(加载新item时开启动画载入)
        openLoadAnimation();
    }
}
