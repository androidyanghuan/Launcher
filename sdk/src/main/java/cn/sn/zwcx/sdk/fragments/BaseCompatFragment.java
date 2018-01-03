package cn.sn.zwcx.sdk.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sn.zwcx.sdk.global.GlobalApplication;
import cn.sn.zwcx.sdk.widgets.WaitProgressDialog;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by on 2017/12/15 20:28.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BaseCompatFragment extends SupportFragment {
    protected String TAG;
    protected Context mContext;
    protected Activity mActivity;
    protected GlobalApplication mApplication;
    protected WaitProgressDialog mWaitDialog;
    private Unbinder binder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != getLayoutView()) return getLayoutView();
        else return inflater.inflate(getLayoutId(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TAG = getClass().getSimpleName();
        binder = ButterKnife.bind(this,view);
        initData();
        initView(view,savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binder != null) binder.unbind();
    }

    /**
     * 处理回退事件
     * @return
     * true:事件消费
     * false:向上传递
     */
    @Override
    public boolean onBackPressedSupport() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            // 如果当前存在fragment > 1,当前fragment出栈
            pop();
        } else {
            // 已经退回到root fragment,交由Activity处理回退事件
            return false;
        }
        return true;
    }

    /** 初始化视图交由子类实现 */
    public abstract void initView(View view, Bundle savedInstanceState);

    /** 在监听器之前把数据准备好 */
    public void initData() {
        mApplication = GlobalApplication.getInstance();
        mWaitDialog = new WaitProgressDialog(mContext);
    }

    // 获取布局视图
    public View getLayoutView(){
        return null;
    }

    @LayoutRes
    public abstract int getLayoutId();

    /** 显示等待对话框 */
    protected void showProgressDialog(String msg){
        if (mWaitDialog.isShowing())
            mWaitDialog.cancel();
        mWaitDialog.setMessage(msg);
        mWaitDialog.show();
    }

    /** 隐藏提示对话框 */
    protected void hidenProgressDialog(){
        if (mWaitDialog != null)
            mWaitDialog.cancel();
    }
}
