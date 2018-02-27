package cn.sn.zwcx.yizi.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sn.zwcx.sdk.base.BaseCompatActivity;
import cn.sn.zwcx.sdk.helper.BottomNavigationViewHelper;
import cn.sn.zwcx.sdk.rx.RxBus;
import cn.sn.zwcx.sdk.rx.Subscribe;
import cn.sn.zwcx.sdk.utils.FileUtil;
import cn.sn.zwcx.sdk.utils.SpUtil;
import cn.sn.zwcx.sdk.utils.ToastUtil;
import cn.sn.zwcx.sdk.widgets.MovingImageView;
import cn.sn.zwcx.sdk.widgets.MovingViewAnimator;
import cn.sn.zwcx.yizi.R;
import cn.sn.zwcx.yizi.activitys.detail.WebViewLoadActivity;
import cn.sn.zwcx.yizi.constants.BundleKeyConstant;
import cn.sn.zwcx.yizi.fragments.gankio.GankIoRootFragment;
import cn.sn.zwcx.yizi.fragments.home.HomeRootFragment;
import cn.sn.zwcx.yizi.fragments.home.child.HomeFragment;
import cn.sn.zwcx.yizi.model.bean.rxbus.RxEventHeadBean;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.SupportFragment;

import static cn.sn.zwcx.yizi.constants.RxBusCode.RX_BUS_CODE_HEAD_IMAGE_URI;

public class MainActivity extends BaseCompatActivity implements HomeFragment.OnOpenDrawerLayoutListener {
    private final String TAG = MainActivity.class.getSimpleName();

    private NavigationView nvMenu;

    private DrawerLayout dlRoot;

    private BottomNavigationView bnvBar;


    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    public static final int FIFTH = 4;

    private SupportFragment[] fragments = new SupportFragment[5];

    /** 背景滚动的ImageView */
    private MovingImageView mivView;
    /** 圆形的ImageView */
    private CircleImageView civView;
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (bitmap != null)
                        civView.setImageBitmap(bitmap);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void initData() {
        super.initData();
        RxBus.get().register(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        nvMenu = findViewById(R.id.nv_menu);
        mivView = nvMenu.getHeaderView(0).findViewById(R.id.miv_menu);
        civView = nvMenu.getHeaderView(0).findViewById(R.id.civ_head);
        dlRoot = findViewById(R.id.dl_root);
        bnvBar = findViewById(R.id.bnv_bar);

        if (savedInstanceState == null) {
            fragments[FIRST] = HomeRootFragment.newInstance();
            fragments[SECOND] = GankIoRootFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    fragments[FIRST],
                    fragments[SECOND]);

        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.getFragments()
            // 自行进行判断查找(效率更高些),用下面的方法查找更方便些
            fragments[FIRST] = findFragment(HomeRootFragment.class);
            fragments[SECOND] = findFragment(GankIoRootFragment.class);
        }



        //此处实际应用中替换成服务器拉取图片
    /*    Uri headUri = Uri.fromFile(new File(getCacheDir() + BundleKeyConstant.HEAD_IMAGE_NAME + ".jpg"));
        if (headUri != null){
            String circleImagePath = FileUtil.getRealFilePathFromUri(context, headUri);
            Bitmap bitmap = BitmapFactory.decodeFile(circleImagePath);
            if (bitmap != null)
                civView.setImageBitmap(bitmap);
        }*/
    /*    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlStr = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2263792968,3678829761&fm=27&gp=0.jpg";
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    Message message = Message.obtain();
                    message.what = 200;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        //   Glide.with(context).load(url).crossFade().fitCenter().diskCacheStrategy(DiskCacheStrategy.RESULT).into(civView);
        civView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlRoot.closeDrawer(GravityCompat.START);
                bnvBar.setSelectedItemId(R.id.menu_item_personal);
            }
        });

        BottomNavigationViewHelper.disableShiftMode(bnvBar);
        bnvBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_home:
                        showHideFragment(fragments[FIRST]);
                        break;
                    case R.id.menu_item_goods:
                        showHideFragment(fragments[SECOND]);
                        break;
                    case R.id.menu_item_books:

                        break;
                    case R.id.menu_item_personal:

                        break;
                }
                return true;
            }
        });
        nvMenu.getMenu().findItem(R.id.option_item_mode).setTitle(SpUtil.getNightModel(context)
                ? context.getResources().getString(R.string.day_model)
                : context.getResources().getString(R.string.night_model));
        nvMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = new Bundle();
                switch (item.getItemId()) {
                    case R.id.group_item_github:
                        bundle.putString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_TITLE,BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_TITLE_STR);
                        bundle.putString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_URL,BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_URL_STR);
                        startActivity(WebViewLoadActivity.class,bundle);
                        break;
                    case R.id.group_item_more:
                        bundle.putString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_TITLE,"Cloudnetgo");
                     //   bundle.putString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_URL,"http://blog.csdn.net/oqinyou");
                        bundle.putString(BundleKeyConstant.ARG_KEY_WEB_VIEW_LOAD_URL,"http://www.cloudnetgo.com");
                        startActivity(WebViewLoadActivity.class,bundle);
                        break;
                    case R.id.group_item_qr:
                        startActivity(QRCodeActivity.class);
                        break;
                    case R.id.group_item_share:
                        showShare();
                        break;
                    case R.id.option_item_mode:
                        SpUtil.setNightModel(context,!SpUtil.getNightModel(context));
                        reload();
                        break;
                    case R.id.option_item_about:

                        break;
                }
             //   item.setCheckable(false);
            //    dlRoot.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        dlRoot.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mivView.pauseMoving();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (mivView.getMovingState() == MovingViewAnimator.MovingState.stop)
                    mivView.startMoving();
                else if (mivView.getMovingState() == MovingViewAnimator.MovingState.pause)
                    mivView.resumeMoving();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mivView.stopMoving();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (mivView.getMovingState() == MovingViewAnimator.MovingState.stop)
                    mivView.startMoving();
                else if (mivView.getMovingState() == MovingViewAnimator.MovingState.pause)
                    mivView.resumeMoving();
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressedSupport() {
     //   super.onBackPressedSupport();
        if (dlRoot.isDrawerOpen(GravityCompat.START)) {
            dlRoot.closeDrawer(GravityCompat.START);
            return;
        }
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            //如果当前存在fragment>1，当前fragment出栈
            pop();
        } else {
            //如果已经到root fragment了，2秒内点击2次退出
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                setIsTransAnim(false);
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                ToastUtil.showToast(R.string.press_again);
            }
        }
    }

    @Override
    public void onOpen() {
        if (!dlRoot.isDrawerOpen(GravityCompat.START))
            dlRoot.openDrawer(GravityCompat.START);
    }

    /**
     * RxBus接收图片Uri
     *
     * @param bean RxEventHeadBean
     */
    @Subscribe(code = RX_BUS_CODE_HEAD_IMAGE_URI)
    public void rxBusEvent(RxEventHeadBean bean) {
        Uri uri = bean.getUri();
        if (uri == null)
            return;
        String cropImagePath = FileUtil.getRealFilePathFromUri(context, uri);
        Bitmap bitmap = BitmapFactory.decodeFile(cropImagePath);
        if (bitmap != null) {
            civView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
    }

    /** 显示分享 */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("Yizhi");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("https://fir.im/s4lr");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("每日新闻，精选干货，最新资讯，应有尽有.下载链接：https://fir.im/s4lr");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //由于微信需要注册AppKey才能演示，这里取消微信分享，个人根据自己的需求注册Appkey使用
        oks.setUrl("https://fir.im/s4lr");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("这个App贼好用，快下载体验吧~");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://fir.im/s4lr");
        // 启动分享GUI
        oks.show(this);

    }

}
