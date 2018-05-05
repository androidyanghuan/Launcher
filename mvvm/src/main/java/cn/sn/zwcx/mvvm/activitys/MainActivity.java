package cn.sn.zwcx.mvvm.activitys;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.bean.douban.HotMovieBean;
import cn.sn.zwcx.mvvm.constants.Constant;

import cn.sn.zwcx.mvvm.databinding.ActivityMainBinding;
import cn.sn.zwcx.mvvm.fragments.GankIoFragment;
import cn.sn.zwcx.mvvm.fragments.HomeFragment;
import cn.sn.zwcx.mvvm.fragments.MeFragment;
import cn.sn.zwcx.mvvm.fragments.MovieFragment;
import cn.sn.zwcx.mvvm.fragments.TopMovieFragment;
import cn.sn.zwcx.mvvm.utils.BottomNavigationViewHelper;
import cn.sn.zwcx.mvvm.utils.SPUtils;
import cn.sn.zwcx.mvvm.utils.ToastUtil;
import cn.sn.zwcx.mvvm.views.CustomPopupWindow;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnOpenDrawerLayoutListener,
        MovieFragment.OnFragmentCreated,TopMovieFragment.OnFragmentDismiss,
        MeFragment.OnUserPhotoChangeListener {

    private final String TAG = MainActivity.class.getSimpleName();
    //  https://www.jianshu.com/p/8ff81be83101

    private SimpleDraweeView mUserIcon;

    private ActivityMainBinding mBinding;
    private long TOUCH_TIME = 0;

    private Fragment mCurFragment;
    private HomeFragment mHomeFragment;
    private GankIoFragment mGoodFragment;
    private MovieFragment mMoveFragment;
    private MeFragment mMeFragment;
    private TopMovieFragment mTopMovieFragment;
    private boolean mShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermission();
        initView();

    }

    private void requestPermission() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean)
                            ToastUtil.showToast(R.string.permission_info);
                    }
                });
    }

    /** 初始化控件 */
    private void initView() {
        mCurFragment = new Fragment();
        mHomeFragment = HomeFragment.newInstance(getString(R.string.main_home_page));
        mGoodFragment = GankIoFragment.newInstance(getString(R.string.main_goods_page));
        mMoveFragment = MovieFragment.newInstance(getString(R.string.movie_fragment_title));
        mMeFragment = MeFragment.newInstance(getString(R.string.main_me_page));
        mTopMovieFragment = TopMovieFragment.newInstance(getString(R.string.douban_movie_top));
        mUserIcon = mBinding.mainNavView.getHeaderView(0).findViewById(R.id.main_user_sdv_icon);

        BottomNavigationViewHelper.disableShiftMode(mBinding.mainBottomNavigation);
        mBinding.mainBottomNavigation.setOnNavigationItemSelectedListener(this);
        mBinding.mainNavView.setNavigationItemSelectedListener(this);
        mMoveFragment.setOnFragmentCreated(this);
        mTopMovieFragment.setOnFragmentDismiss(this);

     //   mUserIcon.setImageURI(Uri.parse(iconUrl));
        String photo = SPUtils.getInstance(this).getUserPhoto();
        if (TextUtils.isEmpty(photo)) {
            ImageLoader.loadImage(mUserIcon, Constant.MAIN_NV_USER_ICON);
        } else {
            ImageLoader.loadImage(mUserIcon,photo);
        }

        switchFragment(mHomeFragment);
        mMeFragment.setOnUserPhotoChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.main_bnav_home:
                switchFragment(mHomeFragment);
                break;
            case R.id.main_bnav_goods:
                switchFragment(mGoodFragment);
                break;
            case R.id.main_bnav_books:
                if (mShowing)
                    switchFragment(mTopMovieFragment);
                else
                    switchFragment(mMoveFragment);
                break;
            case R.id.main_bnav_me:
                switchFragment(mMeFragment);
                break;
            case R.id.main_nav_home:
                Log.e(TAG,"I am home pager" + item.getTitle());
                if (mBinding.mainDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                mBinding.mainBottomNavigation.getMenu().getItem(0).setChecked(true);
                break;
            case R.id.main_nav_goods:
                Log.e(TAG,"I am goods pager" + item.getTitle());
                break;
            case R.id.main_nav_books:

                break;
            case R.id.main_nav_me:
                if (mBinding.mainDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                switchFragment(mMeFragment);
                mBinding.mainBottomNavigation.getMenu().getItem(3).setChecked(true);
                break;
                default:
                switchFragment(mHomeFragment);
                    break;
        }
        return true;
    }

    /**
     * 切换Fragment
     * @param targetFragment 目标Fragment
     */
    private void switchFragment(Fragment targetFragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mCurFragment);
        if (!targetFragment.isAdded())
            transaction.add(R.id.main_fl, targetFragment, targetFragment.getClass().getName());
        else
            transaction.show(targetFragment);
        transaction.commitAllowingStateLoss();
        // 更新当前Fragment为targetFragment;
        mCurFragment = targetFragment;
    }

    @Override
    public void onOpen() {
        if (!mBinding.mainDrawerLayout.isDrawerOpen(GravityCompat.START))
            mBinding.mainDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
        if (mBinding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (System.currentTimeMillis() - TOUCH_TIME > 2000L) {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtil.showToast(R.string.main_press_again);
        }else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onUserPhotoChange() {
        String photo = SPUtils.getInstance(this).getUserPhoto();
        if (TextUtils.isEmpty(photo)) {
            ImageLoader.loadImage(mUserIcon, Constant.MAIN_NV_USER_ICON);
        } else {
            ImageLoader.loadImage(mUserIcon,photo);
        }
    }

    @Override
    public void onFragmentCreate(boolean isShowing) {
        mShowing = true;
        if (isShowing)
            switchFragment(mTopMovieFragment);
    }

    @Override
    public void onFragmentDismiss(boolean isShowing) {
        mShowing = false;
        if (isShowing)
            switchFragment(mMoveFragment);
    }

}
