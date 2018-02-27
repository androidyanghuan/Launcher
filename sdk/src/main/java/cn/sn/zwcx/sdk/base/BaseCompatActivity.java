package cn.sn.zwcx.sdk.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import cn.sn.zwcx.sdk.R;
import cn.sn.zwcx.sdk.global.GlobalApplication;
import cn.sn.zwcx.sdk.manager.AppManager;
import cn.sn.zwcx.sdk.utils.AppUtil;
import cn.sn.zwcx.sdk.utils.SpUtil;
import cn.sn.zwcx.sdk.utils.StatusBarUtil;
import cn.sn.zwcx.sdk.utils.ThemeUtil;

import cn.sn.zwcx.sdk.widgets.WaitProgressDialog;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by on 2017/12/12 15:07.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public abstract class BaseCompatActivity extends SupportActivity {
private final String TAG = "Yang Huan " + BaseCompatActivity.class.getSimpleName();

/** 全局Application */
protected GlobalApplication mApp;

/** 等待进度对话框 */
protected WaitProgressDialog mDialog;

/** 上下文 */
protected Context context;

/** 是否转换动画 */
protected boolean isTransAnim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    /** 初始化 */
    private void init(Bundle savedInstanceState) {
        setTheme(ThemeUtil.themeArr[SpUtil.getThemeIndex(this)][SpUtil.getNightModel(this) ? 1 : 0]);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initData();
        initView(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }

    /** 子类实现,绑定控件,初始化视图等内容 */
    protected abstract void initView(Bundle savedInstanceState);

    // 子类可以重写该方法初始化数据
    protected void initData() {
        mApp = (GlobalApplication) getApplication();
        context = AppUtil.getContext();
        mDialog = new WaitProgressDialog(this);
        isTransAnim = true;
    }

    /** 抽象函数获取布局id交由子类实现 */
    protected abstract int getLayoutId();

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // fragment切换使用默认的Vertical动画
        return new DefaultVerticalAnimator();
    }

    public void reload(){
        Intent intent = getIntent();
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    /**
     * 显示提示框
     * @param msg
     */
    protected void showProgressDialog(String msg){
        mDialog.setMessage(msg);
        mDialog.show();
    }

    /**
     * 隐藏提示框
     */
    protected void hidenProgressDialog(){
        if (mDialog != null)
            mDialog.cancel();
    }

    /**
     * 页面跳转
     * @param clz
     */
    public void startActivity(Class<?> clz){
        startActivity(new Intent(this,clz));
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_zoom_in,R.anim.activity_start_zoom_out);
    }

    /**
     * 页面跳转
     * @param clz
     * @param intent
     */
    public void startActivity(Class<?> clz,Intent intent){
        intent.setClass(this,clz);
        startActivity(intent);
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_zoom_in,R.anim.activity_start_zoom_out);
    }

    /**
     * 携带数据的页面跳转
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz,Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(this,clz);
        if (null != bundle){
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_zoom_in,R.anim.activity_start_zoom_out);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     * @param clz
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> clz,Bundle bundle,int requestCode){
        Intent intent = new Intent();
        intent.setClass(this,clz);
        if (null != bundle){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_zoom_in,R.anim.activity_start_zoom_out);
    }

    @Override
    public void finish() {
        super.finish();
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_zoom_in,R.anim.activity_start_zoom_out);
    }

    /**
     * 隐藏键盘
     * @return
     */
    protected boolean hiddenKeyboard(){
        // 点击空白位置隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    /**
     * 初始化TitleBar
     * @param toolbar
     * @param title
     */
    protected void initTitleBar(Toolbar toolbar, String title){
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }

    /**
     * 是否使用overridePendingTransition过渡动画，默认使用
     * @return
     */
    protected boolean isTransAnim(){
        return isTransAnim;
    }

    /**
     * 设置是否使用overridePendingTransition过渡动画
     * @param b
     */
    protected void setIsTransAnim(boolean b){
        isTransAnim = b;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
    }
}
