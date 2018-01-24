package cn.sn.zwcx.yizi.contract.detail;

import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

import cn.sn.zwcx.sdk.base.BasePresenter;
import cn.sn.zwcx.sdk.base.IBaseActivity;
import cn.sn.zwcx.sdk.base.IBaseModel;

/**
 * Created by on 2018/1/5 14:30.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public interface BaseWebViewLoadContract {
    abstract class BaseWebViewLoadPresenter<M extends IBaseWebViewLoadModel, V extends IBaseWebViewLoadView> extends BasePresenter<M,V>{
        /**
         * 保存图片点击
         * @param activity
         * @param imgUrl
         */
        public abstract void saveImageClicked(FragmentActivity activity,String imgUrl);

        /**
         * 点击跳转图片详情页面
         * @param imgUrl
         */
        public abstract void gotoImageBrowse(String imgUrl);

        /**
         * 长按图片事件
         * @param hitTestResult
         */
        public abstract void imageLongClicked(WebView.HitTestResult hitTestResult);

    }

    interface IBaseWebViewLoadModel extends IBaseModel{}

    interface IBaseWebViewLoadView extends IBaseActivity{
        /**
         * 显示网络错误
         */
        void showNetworkError();

        /**
         * 显示PopupWindow
         */
        void showPopupWindow();

        /**
         * 隐藏PopupWindow
         */
        void hidePopupWindow();

        /**
         * 返回PopupWindow显示状态
         */
        boolean popupWindowIsShowing();

        /**
         * 跳转到图片详情页
         * @param imgUrl
         */
        void gotoImageBrowse(String imgUrl);
    }
}
