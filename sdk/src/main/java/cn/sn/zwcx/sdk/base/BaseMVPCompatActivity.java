package cn.sn.zwcx.sdk.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import cn.sn.zwcx.sdk.base.BaseCompatActivity;
import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.base.IBaseActivity;
import cn.sn.zwcx.sdk.base.IBaseModel;
import cn.sn.zwcx.sdk.global.GlobalApplication;
import cn.sn.zwcx.sdk.utils.ToastUtil;

/**
 * Created by on 2017/12/15 20:25.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BaseMVPCompatActivity<P extends BasePresenter,M extends IBaseModel> extends BaseCompatActivity implements IBaseActivity{

    /** Presenter 具体的Presenter由子类确定 */
    protected P mPresenter;

    /** Mode 具体的Mode由子类确定 */
    private M mIMode;

    @Override
    protected void initData() {
        super.initData();
        mPresenter = (P) initPresenter();
        if (mPresenter != null) {
            mIMode = (M) mPresenter.getModel();
            if (mIMode != null) {
                mPresenter.attachMV(mIMode,this);
            }
        }
    }

    @Override
    protected void onDestroy() {
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
        ToastUtil.showToast(msg);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz) {
        startActivity(clz);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
        startActivity(clz,bundle);
    }

    @Override
    public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
        startActivityForResult(clz,bundle,requestCode);
    }

    @Override
    public void hideKeyboard() {
        hiddenKeyboard();
    }

    @Override
    public void back() {
        super.onBackPressedSupport();
    }
}
