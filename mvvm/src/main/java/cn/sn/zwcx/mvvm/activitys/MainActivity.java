package cn.sn.zwcx.mvvm.activitys;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.ImageLoader;

import cn.sn.zwcx.mvvm.R;
import cn.sn.zwcx.mvvm.constants.Constant;

import cn.sn.zwcx.mvvm.databinding.ActivityMainBinding;
import cn.sn.zwcx.mvvm.fragments.GankIoFragment;
import cn.sn.zwcx.mvvm.fragments.HomeFragment;
import cn.sn.zwcx.mvvm.utils.BottomNavigationViewHelper;
import cn.sn.zwcx.mvvm.utils.ToastUtil;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnOpenDrawerLayoutListener{

    private final String TAG = MainActivity.class.getSimpleName();
    //  https://www.jianshu.com/p/8ff81be83101

    private SimpleDraweeView mUserIcon;

    private ActivityMainBinding mBinding;
    private long TOUCH_TIME = 0;

    private Fragment mCurFragment;
    private HomeFragment mHomeFragment,mBookFragment,mMeFragment;
    private GankIoFragment mGoodFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

    /*      cn.sn.zwcx.mvvm.databinding.MainActivityBinding contentView = DataBindingUtil.setContentView(this, R.layout.main_activity);
          contentView.setUser(new User("yanghuan","男",23));
         contentView.setVariable(BR.presenter,new MainPresenter());*/

        initView();

    }

    /** 初始化控件 */
    private void initView() {
        mCurFragment = new Fragment();
        mHomeFragment = HomeFragment.newInstance(getResources().getString(R.string.main_home_page));
        mGoodFragment = GankIoFragment.newInstance(getResources().getString(R.string.main_goods_page));
        mBookFragment = HomeFragment.newInstance(getResources().getString(R.string.main_books_page));
        mMeFragment = HomeFragment.newInstance(getResources().getString(R.string.main_me_page));
        mUserIcon = mBinding.mainNavView.getHeaderView(0).findViewById(R.id.main_user_sdv_icon);

        BottomNavigationViewHelper.disableShiftMode(mBinding.mainBottomNavigation);
        mBinding.mainBottomNavigation.setOnNavigationItemSelectedListener(this);
        mBinding.mainNavView.setNavigationItemSelectedListener(this);

     //   mUserIcon.setImageURI(Uri.parse(iconUrl));

        ImageLoader.loadImage(mUserIcon, Constant.MAIN_NV_USER_ICON);

        switchFragment(mHomeFragment);

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
                switchFragment(mBookFragment);
                break;
            case R.id.main_bnav_me:
                switchFragment(mMeFragment);
                break;
            case R.id.main_nav_home:
                Log.e(TAG,"I am home pager" + item.getTitle());
                if (mBinding.mainDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.main_nav_goods:
                Log.e(TAG,"I am goods pager" + item.getTitle());
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
        }else
            finish();
    }

}
