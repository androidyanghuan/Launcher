package cn.sn.zwcx.yizi.presenter.detail;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import cn.sn.zwcx.sdk.utils.FileUtil;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.contract.detail.BaseWebViewLoadContract;

/**
 * Created by on 2018/1/9 14:10.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BaseWebViewLoadPresenter<M extends BaseWebViewLoadContract.IBaseWebViewLoadModel, V extends BaseWebViewLoadContract.IBaseWebViewLoadView>
                extends BaseWebViewLoadContract.BaseWebViewLoadPresenter<M,V>{
    @Override
    public void saveImageClicked(final FragmentActivity activity, final String imgUrl) {
        if (mIView.popupWindowIsShowing())
            mIView.hidePopupWindow();
        Glide.with(activity).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                FileUtil.saveBitmap(activity, imgUrl, resource, new FileUtil.SaveResultCallback() {
                    @Override
                    public void onSavedSuccess() {
                        if (mIView != null)
                            mIView.showToast(activity.getResources().getString(R.string.save_success));
                    }

                    @Override
                    public void onSavedFailed() {
                        if (mIView != null)
                            mIView.showToast(activity.getResources().getString(R.string.save_failed));
                    }
                });
            }
        });
    }

    @Override
    public void gotoImageBrowse(String imgUrl) {
        if (mIView == null)
            return;
        if (mIView.popupWindowIsShowing())
            mIView.hidePopupWindow();
        mIView.gotoImageBrowse(imgUrl);
    }

    @Override
    public void imageLongClicked(WebView.HitTestResult hitTestResult) {
        if (null == hitTestResult)
            return;
        int type = hitTestResult.getType();
        switch (type){
            case WebView.HitTestResult.UNKNOWN_TYPE:
                return;
            case WebView.HitTestResult.PHONE_TYPE:
                // 处理拨号
                break;
                case WebView.HitTestResult.EMAIL_TYPE:
                    // 处理Email
                    break;
                case WebView.HitTestResult.GEO_TYPE:
                    break;
                case WebView.HitTestResult.SRC_ANCHOR_TYPE:
                    // 处理超链接
                    break;
                case WebView.HitTestResult.IMAGE_TYPE:
                    // 处理长按图片的菜单项
                    if (mIView != null)
                        mIView.showPopupWindow();
                    break;
                default:
                    break;
        }
    }
}
