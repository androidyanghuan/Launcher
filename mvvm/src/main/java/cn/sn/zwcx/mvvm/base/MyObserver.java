package cn.sn.zwcx.mvvm.base;

import android.accounts.NetworkErrorException;
import android.content.Context;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import cn.sn.zwcx.mvvm.utils.ProgressDialog;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by on 2018/3/15 14:48.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class MyObserver<T> implements Observer<T> {

    /**
     * 返回失败
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;

    /**
     * 返回成功
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccees(T t) throws Exception;

    protected Context mContext;
    protected Disposable disposable;
    protected boolean isShowDialog;

    public MyObserver(Context context,boolean isShowDialog) {
        mContext = context;
        this.isShowDialog = isShowDialog;
    }



    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        onRequestStart();
    }

    @Override
    public void onNext(T t) {
        onRequestEnd();
        if (t != null) {
            try {
                onSuccees(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (disposable != null && disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public void onComplete() {
        onRequestEnd();
    }

    protected void onRequestStart() {
        if (isShowDialog)
        showProgressDialog();
    }

    protected void onRequestEnd() {
        closeProgressDialog();
    }

    public void showProgressDialog() {
        ProgressDialog.show(mContext, false, "请稍后...");
    }

    public void closeProgressDialog() {
        ProgressDialog.cancel();
    }

}
