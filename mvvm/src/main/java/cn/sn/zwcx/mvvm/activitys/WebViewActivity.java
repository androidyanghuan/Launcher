package cn.sn.zwcx.mvvm.activitys;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.fresco.helper.ImageLoader;

import java.io.File;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.apis.ZhihuApi;
import cn.sn.zwcx.mvvm.base.MyObserver;
import cn.sn.zwcx.mvvm.bean.zhihu.ZhihuDailyDetailBean;
import cn.sn.zwcx.mvvm.databinding.ActivityWebViewBinding;
import cn.sn.zwcx.mvvm.utils.NetworkUtil;
import cn.sn.zwcx.mvvm.utils.RetrofitFactory;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class WebViewActivity extends AppCompatActivity {

    private final String TAG = WebViewActivity.class.getSimpleName();

    private ActivityWebViewBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_web_view);

        initViews();

        mBinding.webViewPb.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        if (!TextUtils.isEmpty(id)) {
            RetrofitFactory.getInstance().createApi(ZhihuApi.class, ZhihuApi.HOST)
                    .getZhihuDailyDetail(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<ZhihuDailyDetailBean>(this, true) {
                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            if (isNetWorkError)
                                ToastUtil.showToast(e.toString());
                        }

                        @Override
                        protected void onSuccees(ZhihuDailyDetailBean zhihuDailyDetailBean) throws Exception {
                            String url = zhihuDailyDetailBean.getShareUrl();
                            mBinding.webView.loadUrl(url);
                            mBinding.toolbar.setTitle(zhihuDailyDetailBean.getTitle());
                            ImageLoader.loadImage(mBinding.sdv, zhihuDailyDetailBean.getImage());
                        }
                    });
        } else {
            String imgsrc = extras.getString("imgsrc");
            String url = extras.getString("url");
            String title = extras.getString("source");
            if (TextUtils.isEmpty(imgsrc) || TextUtils.isEmpty(url))
                return;
            mBinding.webView.loadUrl(url);
            mBinding.toolbar.setTitle(title);
            ImageLoader.loadImage(mBinding.sdv,imgsrc);
        }


    }

    private void initViews() {
        // 声明WebSettings的子类
        WebSettings settings = mBinding.webView.getSettings();
        // 设置WebView支持javaScript
        settings.setJavaScriptEnabled(true);
        // 设置屏幕自适应
        settings.setUseWideViewPort(true);  // 将图片调整到合适webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕大小
        // 缩放操作
        settings.setSupportZoom(true);  // 支持缩放，默认true,是下面那个的前提
        settings.setBuiltInZoomControls(true);  // 设置内置缩放控件，若为false,则webView不可缩放
        settings.setDisplayZoomControls(false);  // 隐藏原生缩放控件
        // 其他细节操作
     //   settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 关闭WebView中的缓存
        settings.setAllowFileAccess(true);  // 设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true);    // 支持js打开新窗口
        settings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");   // 设置编码格式
        if (NetworkUtil.isAvailableByPing())
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        else
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + File.separator + "cacheData");    // 设置App的缓存目录

        mBinding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(String.valueOf(request.getUrl()));
                return true;
            }
        });

        mBinding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100)
                    mBinding.webViewPb.setVisibility(View.GONE);
                else
                    mBinding.webViewPb.setProgress(newProgress);

                Log.e(TAG,"load progress:" + newProgress + " %");
            }
        });

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mBinding.webView != null) {
            mBinding.webView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            mBinding.webView.clearHistory();
            ((ViewGroup)mBinding.webView.getParent()).removeView(mBinding.webView);
            mBinding.webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
            return true;
        }else
            finish();
        return true;
    }
}
