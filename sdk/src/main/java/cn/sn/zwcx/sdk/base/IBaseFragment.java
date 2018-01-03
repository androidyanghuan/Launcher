package cn.sn.zwcx.sdk.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by on 2017/12/27 10:30.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface IBaseFragment extends IBaseView {
    /**
     * 出栈到目标fragment
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包涵目标fragment
     *                              true    目标fragment也出栈
     *                              false   出栈到目标fragment，目标fragment不出栈
     */
    void popToFragment(Class<?> targetFragmentClass, boolean includeTargetFragment);

    /**
     * 跳转到新的fragment
     * @param supportFragment 新的fragment继承SupportFragment
     */
    void startNewFragment(@NonNull SupportFragment supportFragment);

    /**
     * 跳转到新的fragment并出栈当前fragment
     * @param supportFragment
     */
    void startNewFragmentWithPop(@NonNull SupportFragment supportFragment);

    /**
     * 跳转到新的fragment并返回结果
     * @param supportFragment
     * @param requestCode
     */
    void startNewFragmentForResult(@NonNull SupportFragment supportFragment,int requestCode);

    /**
     * 设置fragment返回的result
     * @param requestCode
     * @param bundle
     */
    void setOnFragmentResult(int requestCode, Bundle bundle);

    /**
     * 跳转到新的Activity
     * @param clz
     */
    void startNewActivity(@NonNull Class<?> clz);

    /**
     * 携带数据跳转到新的Activity
     * @param clz
     * @param bundle
     */
    void startNewActivity(@NonNull Class<?> clz,Bundle bundle);

    /**
     * 携带数据跳转到新的Activity并返回结果
     * @param clz
     * @param bundle
     * @param requestCode
     */
    void startNewActivityForResult(@NonNull Class<?> clz,Bundle bundle,int requestCode);

    /**
     * 当前Fragment是否可见
     * @return  true 可见，false 不可见
     */
    boolean isVisiable();

    /**
     * 获取当前fragment绑定的activity
     * @return
     */
    Activity getBindActivity();

}
