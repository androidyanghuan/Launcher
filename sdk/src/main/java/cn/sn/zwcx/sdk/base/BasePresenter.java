package cn.sn.zwcx.sdk.base;

import android.support.annotation.NonNull;

import cn.sn.zwcx.sdk.rx.RxManager;

/**
 * Created by on 2017/12/16 8:09.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BasePresenter<M,V> {
    public M mIModel;
    public V mIView;

    /** RxManager管理类 */
    protected RxManager mRxManager = new RxManager();

    /**
     * 获取presenter想持有的Model引用
     * @return
     */
    public abstract M getModel();

    /**
     * 绑定IModel和IView的引用
     * @param m
     * @param v
     */
    public void attachMV(@NonNull M m,@NonNull V v){
        this.mIModel = m;
        this.mIView = v;
        this.onStart();
    }

    /**
     * 解除IModel和IView的引用
     */
    public void detachMV(){
        mRxManager.unSubscribe();
        mIModel = null;
        mIView = null;
    }

    /**
     * IModel和IView绑定完成立即执行
     * 实现类实现绑定后的逻辑，例如数据初始化，界面初始化，更新等
     */
    public abstract void onStart();

}
