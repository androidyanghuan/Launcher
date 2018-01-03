package cn.sn.zwcx.welcome.activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.sn.zwcx.welcome.R;
import cn.sn.zwcx.welcome.adapters.HomeAdapter;
import cn.sn.zwcx.welcome.adapters.HomeAppAdapter;
import cn.sn.zwcx.welcome.app.App;
import cn.sn.zwcx.welcome.bean.Application;
import cn.sn.zwcx.welcome.widgets.TVRecyclerView;

public class MainActivity extends Activity implements View.OnFocusChangeListener,View.OnClickListener{
    private final String TAG = "Yang Huan:" + MainActivity.class.getSimpleName();

    /** 内容视图 */
    private RelativeLayout mFirst,mSecond,mThird,mLast;
    private ImageView mWifi,mUsb;
    private TextView mMac;

    /** wifi管理类 */
    private WifiManager mWifiManager;

    /** 全局的上下文 */
    private Context mContext;

    /** 底部导航栏 */
    private TVRecyclerView mTab;

    private HomeAdapter mHomeAdapter;

    /** 存放快捷方式的List */
    private  List<Application> mApplications = new ArrayList<>();

    private StaggeredGridLayoutManager mLayoutManager;

    /** Handler处理类 */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    int type = msg.arg1;
                    switch (type){
                        case -1:
                            mWifi.setBackgroundResource(R.mipmap.ethernet);
                            break;
                        case 0:
                            mWifi.setBackgroundResource(R.mipmap.wifi);
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            mWifi.setBackgroundResource(R.mipmap.wifi_on);
                            break;
                    }
                    break;
            }
        }
    };

    /** 网络状态改变的广播 */
    private BroadcastReceiver netReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        //    Math.abs(((WifiManager)context.getSystemService(WIFI_SERVICE)).getConnectionInfo().getRssi());
            if (intent != null){
                String action = intent.getAction();
                Log.e(TAG,"action:" + action);
                if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION) || action.equals("android.net.ethernet.ETHERNET_STATE_CHANGED")
                || action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                    initWifiState();
                }
            }
        }
    };

    /** usb状态改变的监听 */
    private BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_MEDIA_EJECT) || action.equals(Intent.ACTION_MEDIA_MOUNTED) || action.equals(Intent.ACTION_MEDIA_UNMOUNTED)){
                    initUsbState();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new GetShortcutTask().execute();
        initViews();

        initWifiState();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        registerReceiver(netReceiver,intentFilter);

        initUsbState();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addDataScheme("file");
        registerReceiver(mediaReceiver,intentFilter);

        String mac = initMacInfo();
        if (!TextUtils.isEmpty(mac))
            mMac.setText("MAC:" + mac);
        else
            mMac.setText("MAC:");

      //  initBottomTab();

    }

    /** 初始化底部列表 */
    private void initBottomTab() {
     //   new GetShortcutTask().execute();
        mHomeAdapter = new HomeAdapter(mContext,mApplications);
     //   mHomeAdapter = new HomeAppAdapter(mApplications);
        mTab.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mTab.setLayoutManager(mLayoutManager);

        mHomeAdapter.setOnItemClickListener(new TVRecyclerView.CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e(TAG,"你点了：" + mApplications.get(position).name);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    /*    mHomeAdapter.setOnItemClickListener(new HomeAppAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Log.e(TAG,"你点了：" + mApplications.get(position).name);
            }
        });*/
        mTab.setAdapter(mHomeAdapter);
    }

    /** 异步获取app快捷方式的任务栈 */
    private class GetShortcutTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mApplications = getAppShortcut();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          //  mHomeAdapter.notifyDataSetChanged();
            Log.e(TAG,"****************onPostExecute***************" + mApplications.size());
            initBottomTab();
        }
    }


    /** 获取app的快捷方式 */
    private List<Application> getAppShortcut() {
        List<Application> apps = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        int size = installedPackages.size();
        int count = 0;
        for (int i = 0; i < size; i++){
            PackageInfo packageInfo = installedPackages.get(i);
            Application app = new Application();
            // 过滤掉系统应用
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0)
                continue;
            if (packageInfo.applicationInfo.loadIcon(pm) == null || TextUtils.isEmpty(packageInfo.applicationInfo.loadIcon(pm).toString()))
                continue;
            app.icon = packageInfo.applicationInfo.loadIcon(pm);
            app.name = packageInfo.applicationInfo.loadLabel(pm).toString();
            if (count == 9) break;
            apps.add(app);
            count ++;
            Log.e(TAG,"name:" + app.name);
        }
        return apps;
    }

    /** 初始化Mac地址信息 */
    private String initMacInfo() {
        LineNumberReader lnr = null;
        String mac = null;
        try {
            java.lang.Process process = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address");
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            lnr = new LineNumberReader(isr);
            while ((mac = lnr.readLine()) != null){
                mac = mac.trim().toUpperCase();
                if (!TextUtils.isEmpty(mac))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (lnr != null)
                    lnr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mac;
    }

    /** 初始化Usb状态 */
    private void initUsbState() {
        StorageManager sm = (StorageManager) getSystemService(STORAGE_SERVICE);
        try {
            Method method = StorageManager.class.getMethod("getVolumeList",new Class[0]);
            StorageVolume[] volumes = (StorageVolume[]) method.invoke(sm, new Object[]{});
            if (volumes != null){
                for (StorageVolume volume : volumes){
                    Method isRemovable = volume.getClass().getMethod("isRemovable", new Class[0]);
                    boolean removable = (boolean) isRemovable.invoke(volume, new Object[]{});
                    if (removable){
                        Method getPath = volume.getClass().getMethod("getPath", new Class[0]);
                        String path = (String) getPath.invoke(volume, new Object[]{});
                        Method getVolumeState = sm.getClass().getMethod("getVolumeState", String.class);
                        String state = (String) getVolumeState.invoke(sm, path);
                        if (state.equals(Environment.MEDIA_MOUNTED)){
                            Log.e(TAG,"path:" + path);
                            mUsb.setBackgroundResource(R.mipmap.usb_on);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                String description = volume.getDescription(mContext);
                                Log.e(TAG,"description:" + description);
                            } else {
                                int i = path.lastIndexOf("/");
                                String substring = path.substring(i);
                                Log.e(TAG,"desription:" + substring);
                            }
                        } else {
                            mUsb.setBackgroundResource(R.mipmap.usb);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 设置wifi状态 */
    private void initWifiState() {
        int type = obtainWifiInfo();
        Message message = Message.obtain();
        message.what = 200;
        message.arg1 = type;
        mHandler.sendMessage(message);
    }

    /** 获取wifi信息 */
    private int obtainWifiInfo() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int speed = 0;
        if (!TextUtils.isEmpty(wifiInfo.getBSSID())){
            speed = WifiManager.calculateSignalLevel(wifiInfo.getRssi(),5);
        }
        if (isEthernetOn())
            speed = -1;
        return speed;
    }

    /** 有线是否连接 */
    private boolean isEthernetOn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }

    /** 初始化控件 */
    private void initViews() {
        mFirst = findViewById(R.id.one_layout);
        mSecond = findViewById(R.id.two_layout);
        mThird = findViewById(R.id.three_layout);
        mLast = findViewById(R.id.four_layout);

        mFirst.setOnFocusChangeListener(this);
        mSecond.setOnFocusChangeListener(this);
        mThird.setOnFocusChangeListener(this);
        mLast.setOnFocusChangeListener(this);

        mFirst.setOnClickListener(this);
        mSecond.setOnClickListener(this);
        mThird.setOnClickListener(this);
        mLast.setOnClickListener(this);

        mFirst.setFocusable(true);
        mFirst.setFocusableInTouchMode(true);

        mWifi = findViewById(R.id.activity_main_wifi_img);
        mUsb = findViewById(R.id.activity_main_usb_img);
        mMac = findViewById(R.id.activity_main_mac_txt);
        mTab = findViewById(R.id.activity_main_tvrv);

        mContext = App.me.getContext();
        mWifiManager = (WifiManager)mContext.getSystemService(WIFI_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.one_layout:
                Log.e(TAG,"===============first================");
                break;
            case R.id.two_layout:
                Log.e(TAG,"===============second================");
                break;
            case R.id.three_layout:
                Log.e(TAG,"===============third================");
                break;
            case R.id.four_layout:
                Log.e(TAG,"===============last================");
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            v.bringToFront();
            int id = v.getId();
            switch (id){
                case R.id.one_layout:
                    ViewCompat.animate(v).scaleX(1.1f).scaleY(1.05f).start();
                    break;
                case R.id.two_layout:
                    ViewCompat.animate(v).scaleX(1.15f).scaleY(1.05f).start();
                    break;
                case R.id.three_layout:
                    ViewCompat.animate(v).scaleX(1.15f).scaleY(1.05f).start();
                    break;
                case R.id.four_layout:
                    ViewCompat.animate(v).scaleX(1.1f).scaleY(1.05f).start();
                    break;
            }
        }else {
            ViewCompat.animate(v).scaleX(1.0f).scaleY(1.0f).start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.e(TAG,"===============key code left================");
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.e(TAG,"===============key code right================");
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.e(TAG,"===============key code down================");
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.e(TAG,"===============key code up================");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netReceiver != null)
            unregisterReceiver(netReceiver);
        if (mediaReceiver != null)
            unregisterReceiver(mediaReceiver);
    }
}
