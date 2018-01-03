package cn.sn.zwcx.welcome.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by on 2017/12/28 17:31.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * 针对电视端定制的RecycleView,具备以下功能
 * 1.响应五向键，上下左右会跟随移动并获取焦点，获取焦点时会抬高
 * 2.
 */

public class CustomRecyclerView extends RecyclerView {
    private final String TAG = "Yang Huan:" + CustomRecyclerView.class.getSimpleName();

    public CustomRecyclerView(Context context) {
        this(context,null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
