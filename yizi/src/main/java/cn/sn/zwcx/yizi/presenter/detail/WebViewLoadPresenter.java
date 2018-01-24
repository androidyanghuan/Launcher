package cn.sn.zwcx.yizi.presenter.detail;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;

import cn.sn.zwcx.yizi.contract.detail.WebViewLoadConaract;
import cn.sn.zwcx.yizi.model.detail.WebViewLoadModel;

/**
 * Created by on 2018/1/9 17:33.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WebViewLoadPresenter extends WebViewLoadConaract.WebViewLoadPresenter {

    @NonNull
    public static WebViewLoadPresenter newInstance() {
        return new WebViewLoadPresenter();
    }

    @Override
    public void loadUrl(String url) {
        if (mIView == null)
            return;
        try {
            mIView.showUrlDetail(url);
        } catch (Exception e) {
            mIView.showNetworkError();
            e.printStackTrace();
        }
    }

    @Override
    public WebViewLoadConaract.IWebViewLoadModel getModel() {
        return WebViewLoadModel.newInstance();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void saveImageClicked(FragmentActivity activity, String imgUrl) {

    }

    @Override
    public void gotoImageBrowse(String imgUrl) {

    }

    @Override
    public void imageLongClicked(WebView.HitTestResult hitTestResult) {

    }
}
