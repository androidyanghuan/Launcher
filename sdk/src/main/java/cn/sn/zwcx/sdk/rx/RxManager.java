package cn.sn.zwcx.sdk.rx;



import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by on 2017/12/16 8:17.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class RxManager {
    /** 管理订阅者 */
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * 注册订阅
     * @param disposable
     */
    public void register(Disposable disposable){
        mCompositeDisposable.add(disposable);
    }

    public void unSubscribe(){
        mCompositeDisposable.dispose();
    }
}
