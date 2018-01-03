package cn.sn.zwcx.sdk.base;

import android.support.annotation.NonNull;

/**
 * Created by on 2017/12/18 15:18.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface IBaseView {
    /**
     * 初始化presenter
     * @return presenter对象不可为空
     */
    @NonNull
    BasePresenter initPresenter();

    /** 显示Toast */
    void showToast(String msg);

    /** 显示等待对话框 */
    void showWaitDialog(String msg);

    /** 隐藏等待对话框 */
    void hideWaitDialog();

    /** 隐藏键盘 */
    void hideKeyboard();

    /** 回退 */
    void back();
}
