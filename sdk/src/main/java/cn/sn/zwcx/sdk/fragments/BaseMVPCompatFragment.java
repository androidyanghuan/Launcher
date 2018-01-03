package cn.sn.zwcx.sdk.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import cn.sn.zwcx.sdk.base.BaseCompatActivity;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.base.IBaseFragment;
import cn.sn.zwcx.sdk.base.IBaseModel;
import cn.sn.zwcx.sdk.utils.ToastUtil;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by on 2017/12/27 11:03.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 * <p>
 *     MVP Fragment基类
 * </p>
 * 实现IBaseView方法，绑定butterknife
 */

public abstract class BaseMVPCompatFragment<P extends BasePresenter,M extends IBaseModel> extends BaseCompatFragment implements IBaseFragment {

    public P mPresenter;
    public M mModel;

    /**
     * 在监听器之前把数据准备好
     */
    @Override
    public void initData() {
        super.initData();
        mPresenter = (P) initPresenter();
        if (mPresenter != null) {
            mModel = (M) mPresenter.getModel();
            if (mModel != null) {
                mPresenter.attachMV(mModel,this);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachMV();
    }

    @Override
    public void showWaitDialog(String msg) {
        showProgressDialog(msg);
    }

    @Override
    public void hideWaitDialog() {
        hidenProgressDialog();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void back() {
        this.onBackPressedSupport();
    }

    @Override
    public void startNewFragment(@NonNull SupportFragment supportFragment) {
        start(supportFragment);
    }

    @Override
    public void startNewFragmentWithPop(@NonNull SupportFragment supportFragment) {
        startNewFragment(supportFragment);
    }

    @Override
    public void startNewFragmentForResult(@NonNull SupportFragment supportFragment, int requestCode) {
        startNewFragmentForResult(supportFragment,requestCode);
    }

    @Override
    public void popToFragment(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        popToFragment(targetFragmentClass,includeTargetFragment);
    }

    @Override
    public void hideKeyboard() {
        hideSoftInput();
    }

    @Override
    public void setOnFragmentResult(int requestCode, Bundle bundle) {
        setOnFragmentResult(requestCode,bundle);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz) {
        ((BaseCompatActivity) mActivity).startActivity(clz);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
        ((BaseCompatActivity) mActivity).startActivity(clz,bundle);
    }

    @Override
    public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
        ((BaseCompatActivity) mActivity).startActivityForResult(clz,bundle,requestCode);
    }

    @Override
    public boolean isVisiable() {
        return isSupportVisible();
    }

    @Override
    public Activity getBindActivity() {
        return mActivity;
    }
}
