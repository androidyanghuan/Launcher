package cn.sn.zwcx.yizi.activitys.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import cn.sn.zwcx.sdk.base.BaseMVPCompatActivity;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.DisplayUtil;
import cn.sn.zwcx.sdk.utils.NetworkConnectUtil;
import cn.sn.zwcx.sdk.widgets.NestedScrollWebView;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.activitys.pic.ImageBrowseActivity;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.contract.detail.BaseWebViewLoadContract;
import cn.sn.zwcx.yizi.widgets.WebViewLongClickedPopWindow;

/**
 * Created by on 2018/1/5 14:59.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BaseWebViewLoadActivity <P extends BaseWebViewLoadContract.BaseWebViewLoadPresenter, M extends BaseWebViewLoadContract.IBaseWebViewLoadModel>
    extends BaseMVPCompatActivity<P,M> implements BaseWebViewLoadContract.IBaseWebViewLoadView{

    @BindView(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tv_detail_copyright)
    TextView tvDetailCopyright;
    @BindView(R.id.nswv_detail_content)
    NestedScrollWebView nswvDetailContent;
    @BindView(R.id.fl_net_view)
    FrameLayout flNetView;
    @BindView(R.id.v_network_error)
    View vNetworkError;
    @BindView(R.id.progressBar)
    ProgressBar pvWeb;

    private String mImgurl;

    private int downX, downY;

    private WebViewLongClickedPopWindow popWindow;

    @Override
    public void showNetworkError() {
        vNetworkError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPopupWindow() {
        popWindow.showAtLocation(nswvDetailContent, Gravity.TOP|Gravity.LEFT,downX,downY + 10);
    }

    @Override
    public void hidePopupWindow() {
        popWindow.dismiss();
    }

    @Override
    public boolean popupWindowIsShowing() {
        return popWindow.isShowing();
    }

    @Override
    public void gotoImageBrowse(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl))
            return;
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeyConstant.ARG_KEY_IMAGE_BROWSE_URL,imgUrl);
        startActivity(ImageBrowseActivity.class,bundle);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolbar);
        nswvDetailContent = findViewById(R.id.nswv_detail_content);
        vNetworkError = findViewById(R.id.v_network_error);
        appBar = findViewById(R.id.app_bar);
        flNetView = findViewById(R.id.fl_net_view);
        pvWeb = findViewById(R.id.pb_web);

        initTitleBar(toolbar,getResources().getString(R.string.skiping_str));
        initWebSetting(nswvDetailContent.getSettings());
        initWebView();

        vNetworkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetail();
            }
        });
        popWindow = new WebViewLongClickedPopWindow(context,
                WebViewLongClickedPopWindow.IMAGE_VIEW_POPUPWINDOW,
                DisplayUtil.dp2px(120),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setOnItemClickListener(new WebViewLongClickedPopWindow.OnItemClickListener() {
            @Override
            public void onShowPicClicked() {
                mPresenter.gotoImageBrowse(mImgurl);
            }

            @Override
            public void onSavePicClicked() {
                mPresenter.saveImageClicked(BaseWebViewLoadActivity.this,mImgurl);
            }
        });
        loadDetail();
    }

    @Override
    public void onBackPressedSupport() {
        if (nswvDetailContent.canGoBack()){
            // 获取WebView的浏览记录
            WebBackForwardList wbfl = nswvDetailContent.copyBackForwardList();
            // 这里判断如果有上一个页面就返回到上一个页面而不是退出当前Activity
            if (wbfl.getCurrentIndex() > 0){
                nswvDetailContent.goBack();
                return;
            }
        }
        super.onBackPressedSupport();
    }

    /**
     * 加载详情交由子类实现
     */
    protected abstract void loadDetail();

    public class SupportJavascriptInterface{
        private Context context;

        public SupportJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void openImage(final String imgUrl){
            AppUtil.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    gotoImageBrowse(imgUrl);
                }
            });
        }
    }

    /** 初始化WebView */
    protected void initWebView() {
        // 添加js交互接口,并起名为imageListener
        nswvDetailContent.addJavascriptInterface(new SupportJavascriptInterface(context), "imageListener");
     //   nswvDetailContent.setWebChromeClient(new WebChromeClient(){ });
        nswvDetailContent.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.e("******************","url:" + url);
                if (TextUtils.isEmpty(url))
                    return false;
                try {
                    if (url.startsWith("weixin://")
                            || url.startsWith("alipays://")
                            || url.startsWith("mailto://")
                            || url.startsWith("tel://")
                            || url.startsWith("dianping://")
                            || url.startsWith("http://www.bilibili")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        Log.e("***************","8888888888888888**************");
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                // 页面加载完成，添加图片点击的js函数
                addImageClickListener(view);
                toolbar.setTitle(getToolbarTitle());

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(view, url, favicon);
            }

            /**
             * js注入的监听函数
             * @param view
             */
            protected void addImageClickListener(WebView view) {
                // 遍历所有的img节点，并添加onClick函数，函数的功能是在点击图片的时候调用本地java接口并传递url过去
                view.loadUrl("javascript:(function() {"
                        + "var objs = document.getElementsByTagName(\"img\"); "
                        + "for(var i=0;i<objs.length;i++)  "
                        + "{"
                        + "    objs[i].onclick=function()  "
                        + "    {  "
                        + "        window.imagelistner.openImage(this.src);  "
                        + "    }  "
                        + "}"
                        + "})()");
            }

        });

        nswvDetailContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100)
                    // 加载完成隐藏进度条
                    pvWeb.setVisibility(View.GONE);
                else {
                    // 显示进度条，加载进度值
                    pvWeb.setVisibility(View.VISIBLE);
                    pvWeb.setProgress(newProgress);
                }
            }
        });

        nswvDetailContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (null == result)
                    return false;
                mPresenter.imageLongClicked(result);
                mImgurl = result.getExtra();
                return true;
            }
        });

        nswvDetailContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                downX = (int) event.getX();
                downY = (int) event.getY();
                return false;
            }
        });
    }

    /**
     * 返回title,交由子类实现
     * @return
     */
    protected abstract String getToolbarTitle();

    /** 初始化WebSetting */
    protected void initWebSetting(WebSettings settings) {
        // 缩放至屏幕大小
        settings.setLoadWithOverviewMode(true);
        // 保存表单数据
        settings.setSaveFormData(true);
        // 支持手势缩放
        settings.setSupportZoom(true);
        // 启用缓存
        settings.setAppCacheEnabled(true);
        // 排版适应屏幕只显示一列
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 页面加载好后再放开图片
        settings.setBlockNetworkImage(false);
        // 使用localStorage必须打开
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        // 支持JavaScript
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (NetworkConnectUtil.isConnected(context))
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        else
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }
}
