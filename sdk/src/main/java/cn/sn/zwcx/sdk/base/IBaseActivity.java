package cn.sn.zwcx.sdk.base;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by on 2017/12/18 17:37.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface IBaseActivity extends IBaseView {

    /**
     * 跳转新的Activity
     * @param clz
     */
    void startNewActivity(@NonNull Class<?> clz);

    /**
     * 携带数据跳转新的Activity
     * @param clz
     * @param bundle
     */
    void startNewActivity(@NonNull Class<?> clz, Bundle bundle);

    /**
     * 携带数据跳转新的Activity并返回数据
     * @param clz
     * @param bundle
     * @param requestCode
     */
    void startNewActivityForResult(@NonNull Class<?> clz,Bundle bundle,int requestCode);

}
