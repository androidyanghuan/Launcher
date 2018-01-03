package cn.sn.zwcx.entertainment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Launcher extends Activity implements View.OnClickListener {

	private final static String TAG = "MediaBoxLauncher";
	private final String net_change_action = "android.net.conn.CONNECTIVITY_CHANGE";
	private final String wifi_signal_action = "android.net.wifi.RSSI_CHANGED";
	private final String outputmode_change_action = "android.amlogic.settings.CHANGE_OUTPUT_MODE";
	private final String bluetooth_connect_ok_action = "bluetooth.connect.ok";
	private final String bluetooth_connect_startGuid_action = "bluetooth.connect.startGuid";
	private final String bluetooth_connect_GetStatus_action = "bluetooth.connect.GetStatus";
	private final String bluetooth_connect_2disconnect_action = "bluetooth.connect.2disconnect";

	public final static String VIDEO_SHORTCUT = "Video_Shortcut:";
	public final static String RECOMMEND_SHORTCUT = "Recommend_Shortcut:";
	public final static String MUSIC_SHORTCUT = "Music_shortcut:";
	private static final String BOOT_ACTION = "boot_wizard_setting";

	private static int time_count = 0;
	private final int time_freq = 180;
	public static String REAL_OUTPUT_MODE;
	public static int SCREEN_HEIGHT;
	public static int SCREEN_WIDTH;
	public static boolean isRealOutputMode;
	public static boolean isNative4k2k;
	public static boolean isNative720;
	public static String current_shortcutHead = "Home_Shortcut:";

	public static View prevFocusedView;
	public static RelativeLayout layoutScaleShadow;
//	public static View trans_frameView;
//	public static View frameView;
	public static View viewHomePage = null;
	public static MyViewFlipper viewMenu = null;
	public static View pressedAddButton = null;
	public static View APPView;

	public static boolean isShowHomePage;
	public static boolean dontRunAnim;
	public static boolean dontDrawFocus;
	public static boolean ifChangedShortcut;
	public static boolean IntoCustomActivity;
	public static boolean IntoApps;
	public static boolean isAddButtonBeTouched;
	public static boolean isInTouchMode;
	public static boolean animIsRun;
	public static boolean cantGetDrawingCache;
	public static int accessBoundaryCount = 0;
	public static int HOME_SHORTCUT_COUNT = 9;
	public static View saveHomeFocusView = null;
	public static MyGridLayout homeShortcutView = null;
	public static MyGridLayout videoShortcutView = null;
	public static MyGridLayout recommendShortcutView = null;
	public static MyGridLayout appShortcutView = null;
	public static MyGridLayout musicShortcutView = null;
	public static MyGridLayout localShortcutView = null;
	public static TextView tx_video_count = null;
	public static TextView tx_recommend_count = null;
	public static TextView tx_app_count = null;
	public static TextView tx_music_count = null;
	public static TextView tx_local_count = null;
	private TextView tx_video_allcount = null;
	private TextView tx_recommend_allcount = null;
	private TextView tx_app_allcount = null;
	private TextView tx_music_allcount = null;
	private TextView tx_local_allcount = null;

	public static Bitmap screenShot;
	public static Bitmap screenShot_keep;
	private IntentFilter wifiIntentFilter;
	private String[] list_homeShortcut;
	private String[] list_videoShortcut;
	private String[] list_recommendShortcut;
	private String[] list_musicShortcut;
	private String[] list_localShortcut;
	static WifiManager mWm;
	private boolean is24hFormart = false;
	private int popWindow_top = -1;
	private int popWindow_bottom = -1;
	public static float startX;
	private static boolean updateAllShortcut;
	private int numberInGrid = -1;
	private int numberInGridOfShortcut = -1;
	private ImageView wifi,ethernet;
	private static int wifi_level;
	private boolean dsp_btremote_onoff = false;
	private boolean btremote_connect_ok = false;

	private static Context mContext;

	public static TextView showLauncher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = getApplicationContext();
		sp = mContext.getSharedPreferences("launcher_info",MODE_PRIVATE);

		boolean isFirst = sp.getBoolean("isFirst", true);
		if (isFirst) {
			Intent bootIntent = new Intent();
			ComponentName cn = new ComponentName("com.google.android.setupwizard","com.google.android.setupwizard.OOBE");
			bootIntent.setComponent(cn);
		}

		IntentFilter bootFilter = new IntentFilter(BOOT_ACTION);
		registerReceiver(bootReceiver,bootFilter);
		if (mWm == null) {
			mWm = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
		}



	//	APPView = findViewById(R.id.layout_app);
		showLauncher = findViewById(R.id.tx_show_launcher);
		wifi = findViewById(R.id.wifisetting);
		ethernet = findViewById(R.id.ethsetting);
		initStaticVariable();
		initChildViews();
		displayShortcuts();
		updateWifiLevel();
		setRectOnKeyListener();



		IntentFilter filter = new IntentFilter();
		filter.addAction(net_change_action);
		filter.addAction(wifi_signal_action);
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(outputmode_change_action);
		filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
		filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(netReceiver, filter);

		handler.postDelayed(runnable, 4000);
		wifiIntentFilter = new IntentFilter();
		wifiIntentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
		wifiIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		wifiIntentFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
		registerReceiver(wifiIntentReceiver, wifiIntentFilter);
		filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(appReceiver, filter);

		filter = new IntentFilter();
		filter.addAction(bluetooth_connect_ok_action);
		filter.addAction(bluetooth_connect_startGuid_action);
		filter.addAction(bluetooth_connect_2disconnect_action);
		registerReceiver(bluetooth_rebmote_Receiver, filter);
		handler_bluetoothshell.postDelayed(runnable_bluetoothshell, 500);

		Intent mIntent = new Intent();
		ComponentName comp = new ComponentName("com.android.settings",
				"com.android.settings.bluetooth.BluetoothAutoConnectService");
		mIntent.setComponent(comp);
		this.startService(mIntent);

		Intent i_msg = new Intent();
		i_msg.setAction(bluetooth_connect_GetStatus_action);
		this.sendBroadcast(i_msg);

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			dir = getFilesDir();
		}

		String app = sp.getString("app_pkg", "");
		PackageManager pm = getPackageManager();
		if (!TextUtils.isEmpty(app)){
			try {
				startActivity(new Intent(pm.getLaunchIntentForPackage(app)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			File path = new File(dir + "/pkg.txt");
			if (path.exists()) {
				InputStreamReader isr;
				StringBuffer sb = new StringBuffer(1024);
				try {
					isr = new InputStreamReader(new FileInputStream(new File(dir + "/pkg.txt")),"UTF-8");
					Scanner s = new Scanner(isr);
					while (s.hasNext()) {
						sb.append(s.next());
					}
					String txt = sb.toString();
					String[] split = txt.split(",");
					if (split.length > 0) {
						String pkg = split[0];
						if (!TextUtils.isEmpty(pkg)) {
							startActivity(new Intent(pm.getLaunchIntentForPackage(pkg)));
							SharedPreferences.Editor editor = sp.edit();
							editor.putString("app_pkg", pkg);
							String name = split[1];
							editor.putString("app_name", name);
							editor.putBoolean("isChecked", true);
							editor.commit();
						}
						appName = split[1];
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Runnable runnable = new Runnable() {
		public void run() {
			checkwifistate();
			handler.postDelayed(this, 4000);
		}
	};

	public void checkwifistate(){
		int mWifiState = mWm.getWifiState();
		if (mWifiState == WifiManager.WIFI_STATE_ENABLED) {
			myHandler.sendEmptyMessage(1);
		} else {
			myHandler.sendEmptyMessage(2);
		}
	}
	public Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				switch (obtainWifiInfo()) {
				case 0:
					wifi.setBackgroundResource(R.drawable.wifi);
					break;
				case 1:
					wifi.setBackgroundResource(R.drawable.wifi_1);
					break;
				case 2:
					wifi.setBackgroundResource(R.drawable.wifi_2);
					break;
				case 3:
					wifi.setBackgroundResource(R.drawable.wifi_3);
					break;
				case 4:
					wifi.setBackgroundResource(R.drawable.wifi_4);
					break;
				case 5:
					wifi.setBackgroundResource(R.drawable.wifi_4);
					break;
				default:
					break;
				}
				break;
			case 2:
				wifi.setBackgroundResource(R.drawable.wifi);
				break;
			default:
				break;

			}
		}

	};
	private void displayDate() {
		is24hFormart = DateFormat.is24HourFormat(this);
		TextView time = findViewById(R.id.time_txt);
		TextView date = findViewById(R.id.date_txt);
		time.setText(getTime());
		date.setText(getDate());
	}
	private int obtainWifiInfo() {
		WifiInfo info = mWm.getConnectionInfo();
		int speed = 0;
		if (info.getBSSID() != null) {
			// 链接信号强度
			speed = WifiManager.calculateSignalLevel(info.getRssi(), 5);
		//	info.getLinkSpeed();
		//	info.getSSID();
			if (isEthernetOn()) {
			    speed = 0;
			}
		}
		return speed;
	}
	private boolean isEthernetOn() {
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

		if (info != null && info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private BroadcastReceiver wifiIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Math.abs(((WifiManager) context.getSystemService(WIFI_SERVICE)).getConnectionInfo().getRssi());
			if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.CONNECTED)) {

				}
			} else if (intent.getAction().equals("android.net.ethernet.ETHERNET_STATE_CHANGED")) {
				int state = intent.getIntExtra("ethernet_state", 0);
				if (state == 2) {
					ethernet.setBackgroundResource(R.drawable.etherneton);
				} else {
					ethernet.setBackgroundResource(R.drawable.ethernet);
				}
			}
		};
	};

	public static String appName;

	Handler handler_bluetoothshell = new Handler();
	Runnable runnable_bluetoothshell = new Runnable() {
		public void run() {
			if (btremote_connect_ok == false) {
				dsp_btremote_onoff = !dsp_btremote_onoff;
			}
		}
	};

	private BroadcastReceiver bluetooth_rebmote_Receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String action = intent.getAction();
			if (action.equals(bluetooth_connect_ok_action)) {
				btremote_connect_ok = true;
				dsp_btremote_onoff = true;
			} else if (action.equals(bluetooth_connect_startGuid_action)) {

			} else if (action.equals(bluetooth_connect_2disconnect_action)) {
				if (btremote_connect_ok) {
					btremote_connect_ok = false;
					handler_bluetoothshell.postDelayed(runnable_bluetoothshell,
							500);
				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		if (isInTouchMode
				|| (IntoCustomActivity && isShowHomePage && ifChangedShortcut)) {
			Launcher.dontRunAnim = true;
			layoutScaleShadow.setVisibility(View.INVISIBLE);
		//	frameView.setVisibility(View.INVISIBLE);
		}

		displayShortcuts();
		displayDate();
		setHeight();

		if (cantGetDrawingCache) {
			resetShadow();
		}

		IntoCustomActivity = false;

	}


	@Override
	protected void onPause() {
		super.onPause();
		prevFocusedView = null;
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(netReceiver);
		unregisterReceiver(appReceiver);
		unregisterReceiver(bluetooth_rebmote_Receiver);
		super.onDestroy();

		unregisterReceiver(bootReceiver);
		unregisterReceiver(wifiIntentReceiver);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (Intent.ACTION_MAIN.equals(intent.getAction())) {
			viewMenu.setVisibility(View.GONE);
			viewHomePage.setVisibility(View.VISIBLE);
		//	trans_frameView.setVisibility(View.INVISIBLE);
			layoutScaleShadow.setVisibility(View.INVISIBLE);
		//	frameView.setVisibility(View.INVISIBLE);
			isShowHomePage = true;
			IntoCustomActivity = false;
			updateAllShortcut = true;
			MyRelativeLayout videoView = findViewById(R.id.layout_video);
			dontRunAnim = true;
			videoView.requestFocus();
			videoView.setSurface();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			startX = event.getX();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (pressedAddButton != null && isAddButtonBeTouched
					&& !IntoCustomActivity) {
				Rect rect = new Rect();
				pressedAddButton.getGlobalVisibleRect(rect);

				popWindow_top = rect.top - 10;
				popWindow_bottom = rect.bottom + 10;
				new Thread(new Runnable() {
					public void run() {
						mHandler.sendEmptyMessage(1);
					}
				}).start();
				Intent intent = new Intent();
				intent.putExtra("top", popWindow_top);
				intent.putExtra("bottom", popWindow_bottom);
				intent.putExtra("left", rect.left);
				intent.putExtra("right", rect.right);
				intent.setClass(this, CustomAppsActivity.class);
				startActivity(intent);
				IntoCustomActivity = true;
				isAddButtonBeTouched = false;
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public static boolean isShowMenu = false;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isShowHomePage && !animIsRun) {
				viewMenu.setVisibility(View.GONE);
				viewMenu.clearFocus();
				viewHomePage.setVisibility(View.VISIBLE);
				isShowHomePage = true;
				IntoCustomActivity = false;
				if (saveHomeFocusView != null && !isInTouchMode) {
					prevFocusedView = null;
					dontRunAnim = true;
					saveHomeFocusView.clearFocus();
					dontRunAnim = true;
					saveHomeFocusView.requestFocus();
				}
			}
			if (isShowMenu){
				isShowMenu = !isShowMenu;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_ENTER) {
			ViewGroup view = (ViewGroup) getCurrentFocus();
			if (view.getChildAt(0) instanceof ImageView) {
				ImageView img = (ImageView) view.getChildAt(0);
				if (img != null
						&& img.getDrawable()
								.getConstantState()
								.equals(getResources().getDrawable(R.drawable.item_img_add).getConstantState()))
				{
					Rect rect = new Rect();
					view.getGlobalVisibleRect(rect);

					setHeight();
					popWindow_top = rect.top - 10;
					popWindow_bottom = rect.bottom + 10;
					setPopWindow(popWindow_top, popWindow_bottom);
					Intent intent = new Intent();
					intent.putExtra("top", popWindow_top);
					intent.putExtra("bottom", popWindow_bottom);
					intent.putExtra("left", rect.left);
					intent.putExtra("right", rect.right);
					intent.setClass(this, CustomAppsActivity.class);
					startActivity(intent);
					IntoCustomActivity = true;
				}
			}
		} else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			ComponentName globalSearchActivity = searchManager
					.getGlobalSearchActivity();
			if (globalSearchActivity == null) {
				return false;
			}
			Intent intent = new Intent(
					SearchManager.INTENT_ACTION_GLOBAL_SEARCH);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(globalSearchActivity);
			Bundle appSearchData = new Bundle();
			appSearchData.putString("source", "launcher-search");
			intent.putExtra(SearchManager.APP_DATA, appSearchData);
			startActivity(intent);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_MENU && isShowMenu) {

			 showMenuDialog();

			return true;

		}else if (keyCode == KeyEvent.KEYCODE_MENU){
			if(Launcher.current_shortcutHead.equals(RECOMMEND_SHORTCUT)
				||Launcher.current_shortcutHead.equals(VIDEO_SHORTCUT)
				||Launcher.current_shortcutHead.equals(MUSIC_SHORTCUT))
				{
				new Thread(new Runnable() {
					public void run() {
						mHandler.sendEmptyMessage(1);
					}
				}).start();
				Intent intent = new Intent();
				intent.putExtra("top", popWindow_top);
				intent.putExtra("bottom", popWindow_bottom);
				intent.setClass(this, CustomAppsActivity.class);
				startActivity(intent);
				IntoCustomActivity = true;
				isAddButtonBeTouched = false;
				}else{
			if (!isShowHomePage && !animIsRun) {
				viewMenu.setVisibility(View.GONE);
				viewMenu.clearFocus();
				viewHomePage.setVisibility(View.VISIBLE);
				isShowHomePage = true;
				IntoCustomActivity = false;
				if (saveHomeFocusView != null && !isInTouchMode) {
					prevFocusedView = null;
					dontRunAnim = true;
					saveHomeFocusView.clearFocus();
					dontRunAnim = true;
					saveHomeFocusView.requestFocus();
				}
			}
			return true;
		}

		}

		return super.onKeyDown(keyCode, event);
	}

	private void getThirdApplication(){
		PackageManager pm = getPackageManager();
		List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
		packages = new ArrayList<>();
		int size = packageInfos.size();
		for (int i = 0; i < size; i++){
			PackageInfo packageInfo = packageInfos.get(i);
			if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0){
				packages.add(packageInfo.packageName);
			}
		}
	}

	private void getAllApplication(){
		PackageManager pm = getPackageManager();
		List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
		allPackages = new ArrayList<>();
		int size = packageInfos.size();
		for (int i = 0; i < size; i++){
			PackageInfo packageInfo = packageInfos.get(i);
			allPackages.add(packageInfo.packageName);
		}
	}

	private List<String> allPackages;
	private List<String> packages;
	private AlertDialog dialog = null;
	private String launcherApp;
	private File dir = null;


	private void showMenuDialog() {
		getThirdApplication();
		String index = tx_app_count.getText().toString().trim();
		int value = Integer.valueOf(index).intValue();
		Map<String, Object> stringObjectMap = appsInfo.get(value - 1);
		final String name = (String) stringObjectMap.get("item_name");
		Drawable icon = (Drawable) stringObjectMap.get("item_type");
		final String pak = (String) stringObjectMap.get("item_pck");
		launcherApp = name;

		View content = View.inflate(this,R.layout.menu_dialog_layout,null);
		dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setLayout(650, ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setContentView(content);
		window.setBackgroundDrawable(new ColorDrawable());
		TextView appName = content.findViewById(R.id.dialog_menu_name_txt);
		ImageView appIcon = content.findViewById(R.id.dialog_menu_icon_img);
		CheckBox cb = content.findViewById(R.id.dialog_menu_launcher_cb);
		Button uninstal = content.findViewById(R.id.dialog_menu_uninstal_btn);
		Button clear = content.findViewById(R.id.dialog_menu_clear_btn);
		final TextView showHint = content.findViewById(R.id.dialog_menu_show_txt);

		appName.setText(name);
		appIcon.setImageDrawable(icon);

		uninstal.setOnClickListener(this);
		clear.setOnClickListener(this);

		if (packages.contains(pak)){
			uninstal.setVisibility(View.VISIBLE);
		}else {
			uninstal.setVisibility(View.GONE);
		}

		boolean checked = sp.getBoolean("isChecked", false);
		String app_name = sp.getString("app_name", "");
		cb.setChecked(checked);
		if (checked){
			cb.setText(R.string.cancel_launcher);
			showHint.setText(R.string.click_cancel_launcher);
		}else{
			cb.setText(R.string.set_launcher);
			showHint.setText(R.string.click_set_launcher);

		}
		if (!TextUtils.isEmpty(app_name)){
			showLauncher.setText(app_name + " " + getResources().getString(R.string.set_launcher_success));
			showLauncher.setVisibility(View.VISIBLE);
		} else if (!TextUtils.isEmpty(Launcher.appName)) {
			showLauncher.setText(Launcher.appName + " " + mContext.getResources().getString(R.string.set_launcher_success));
			showLauncher.setVisibility(View.VISIBLE);
		} else {
			showLauncher.setVisibility(View.GONE);
		}

		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				CheckBox box = (CheckBox) buttonView;
				SharedPreferences.Editor editor = sp.edit();
				OutputStreamWriter osw;
				// 保存数据到sdcard
				if (isChecked) {
					box.setText(R.string.cancel_launcher);
					showHint.setText(R.string.click_cancel_launcher);
					showLauncher.setText(launcherApp + " " + getResources().getString(R.string.set_launcher_success));
					showLauncher.setVisibility(View.VISIBLE);
					editor.putString("app_pkg",pak);
					editor.putString("app_name",name);
					editor.putBoolean("isChecked",true);
						try {
							osw = new OutputStreamWriter(new FileOutputStream(new File(dir + "/pkg.txt")),"UTF-8");
							osw.append(pak);
							osw.append(",");
							osw.append(name);
							osw.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
				} else {
					box.setText(R.string.set_launcher);
					showHint.setText(R.string.click_set_launcher);
					showLauncher.setVisibility(View.GONE);
					editor.putString("app_pkg","");
					editor.putString("app_name","");
					editor.putBoolean("isChecked",false);

					try {
						osw = new OutputStreamWriter(new FileOutputStream(new File(dir + "/pkg.txt")),"UTF-8");
						osw.append("");
						osw.append(",");
						osw.append("");
						osw.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				editor.commit();
			}
		});
	}

	public static SharedPreferences sp;
	private String mCacheSize;
	@Override
	public void onClick(View v) {
		String index = tx_app_count.getText().toString().trim();
		int value = Integer.valueOf(index).intValue();
		Map<String, Object> stringObjectMap = appsInfo.get(value - 1);
		String pck = (String) stringObjectMap.get("item_pck");
		int id = v.getId();
		switch (id) {
		    case R.id.dialog_menu_uninstal_btn:
				Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
				uninstallIntent.setData(Uri.parse("package:" + pck));
				startActivity(uninstallIntent);
				if (dialog != null && dialog.isShowing()){
					dialog.cancel();
					dialog = null;
				}
				break;

			case R.id.dialog_menu_clear_btn:
				getCacheSize(pck);
				break;

		}
	}

	/** 清空指定包名下的应用程序数据 */
	private void clearAppCache(String pck) {
		try {
			Method clear = PackageManager.class.getDeclaredMethod("clearApplicationUserData",String.class, IPackageDataObserver.class);
			clear.invoke(getPackageManager(),pck,new MyDataObserver());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取某个包名对应的应用程序的缓存大小
	 * @param pkg  应用程序的包名
	 */
	private void getCacheSize(String pkg) {

		try {
			//通过反射获取到当前的方法。
			Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo",String.class,IPackageStatsObserver.class);
			/**
			 * 第一个参数：调用该方法的对象
			 * 第二个参数：应用包名
			 * 第三个参数：IPackageStatsObserver类型的aidl对象。
			 */
			method.invoke(getPackageManager(),pkg,new MyPackObserver(pkg));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private class MyPackObserver extends IPackageStatsObserver.Stub{

		private String pkg;

		public MyPackObserver(String pkg){
			this.pkg = pkg;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			long cacheSize = pStats.cacheSize;
			if (cacheSize > 0) {
				mCacheSize = Formatter.formatFileSize(getApplicationContext(), cacheSize);
				clearAppCache(pkg);
			}else {
				handler.sendEmptyMessage(207);
			}
		}
	}


	private class MyDataObserver extends IPackageDataObserver.Stub{

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
			if (succeeded){
				handler.sendEmptyMessage(205);
			} else {
				handler.sendEmptyMessage(206);
			}
		}
	}

	private void initStaticVariable() {
		isShowHomePage = true;
		dontRunAnim = false;
		dontDrawFocus = false;
		ifChangedShortcut = true;
		IntoCustomActivity = false;
		IntoApps = true;
		isAddButtonBeTouched = false;
		isInTouchMode = false;
		animIsRun = false;
		updateAllShortcut = true;
		animIsRun = false;
		cantGetDrawingCache = false;

		setHeight();
	}

	private void initChildViews() {
		layoutScaleShadow = findViewById(R.id.layout_focus_unit);
	//	frameView = findViewById(R.id.img_frame);
	//	trans_frameView = findViewById(R.id.img_trans_frame);

		viewHomePage = findViewById(R.id.layout_homepage);
		viewMenu = findViewById(R.id.menu_flipper);

		homeShortcutView = findViewById(R.id.gv_shortcut);
		videoShortcutView = findViewById(R.id.gv_shortcut_video);
		recommendShortcutView = findViewById(R.id.gv_shortcut_recommend);
		appShortcutView = findViewById(R.id.gv_shortcut_app);
		musicShortcutView = findViewById(R.id.gv_shortcut_music);
		localShortcutView = findViewById(R.id.gv_shortcut_local);

		tx_video_count = findViewById(R.id.tx_video_count);
		tx_video_allcount = findViewById(R.id.tx_video_allcount);
		tx_recommend_count = findViewById(R.id.tx_recommend_count);
		tx_recommend_allcount = findViewById(R.id.tx_recommend_allcount);
		tx_app_count = findViewById(R.id.tx_app_count);
		tx_app_allcount = findViewById(R.id.tx_app_allcount);
		tx_music_count = findViewById(R.id.tx_music_count);
		tx_music_allcount = findViewById(R.id.tx_music_allcount);
		tx_local_count = findViewById(R.id.tx_local_count);
		tx_local_allcount = findViewById(R.id.tx_local_allcount);
	}

	private void displayShortcuts() {
		if (ifChangedShortcut == true) {
			loadApplications();
			ifChangedShortcut = false;

			if (!isShowHomePage) {
				if (numberInGrid == -1) {
					new Thread(new Runnable() {
						public void run() {
							try {
								ViewGroup findGridLayout = null;
								while (findGridLayout == null) {
                                    findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
                                            .getCurrentView()).getChildAt(5))
                                            .getChildAt(0));
                                }
								mHandler.sendEmptyMessage(3);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				} else {
					new Thread(new Runnable() {
						public void run() {
							ViewGroup findGridLayout = null;
							try {
								while (findGridLayout == null) {
									findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
											.getCurrentView()).getChildAt(5)).getChildAt(0));

								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							mHandler.sendEmptyMessage(4);
						}
					}).start();
				}
			} else if (numberInGridOfShortcut != -1) {
				new Thread(new Runnable() {
					public void run() {
						while (homeShortcutView
								.getChildAt(numberInGridOfShortcut) == null) {
						}
						mHandler.sendEmptyMessage(5);
					}
				}).start();

			} else if (IntoCustomActivity) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(200);
						} catch (Exception e) {
							e.printStackTrace();
						}
						mHandler.sendEmptyMessage(6);
					}
				}).start();
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void updateWifiLevel() {
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi != null && mWifi.isConnected()) {
			WifiManager mWifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
			WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
			int wifi_rssi = mWifiInfo.getRssi();
			if (wifi_rssi <= -100)
				wifi_level = -1;
			else
				wifi_level = WifiManager.calculateSignalLevel(wifi_rssi, 4);
		} else {
			wifi_level = -1;
		}
	}

	private String mCity = null;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int type = msg.what;
			switch (type) {
			    case 205:
					diyBackgroundAndFontToast(getResources().getString(R.string.clear_success) + mCacheSize);
					mCacheSize = "";
					if (dialog != null && dialog.isShowing()) dialog.dismiss();
			        break;

				case 206:
					diyBackgroundAndFontToast(R.string.clear_fail);
					mCacheSize = "";
					if (dialog != null && dialog.isShowing()) dialog.dismiss();
					break;

				case 207:
					diyBackgroundAndFontToast(R.string.no_data);
					if (dialog != null && dialog.isShowing()) dialog.dismiss();
					break;

			}



		}
	};

	public String getTime() {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		is24hFormart = DateFormat.is24HourFormat(this);
		if (!is24hFormart && hour >= 12) {
			hour = hour - 12;
		}

		String time = "";
		if (hour >= 10) {
			time += Integer.toString(hour);
		} else {
			time += "0" + Integer.toString(hour);
		}
		time += ":";

		if (minute >= 10) {
			time += Integer.toString(minute);
		} else {
			time += "0" + Integer.toString(minute);
		}
		return time;
	}

	private String getDate() {
		final Calendar c = Calendar.getInstance();
		int int_Month = c.get(Calendar.MONTH);
		String mDay = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		int int_Week = c.get(Calendar.DAY_OF_WEEK) - 1;
		String str_week = this.getResources().getStringArray(R.array.week)[int_Week];
		String mMonth = this.getResources().getStringArray(R.array.month)[int_Month];

		String date;
		if (Locale.getDefault().getLanguage().equals("zh")) {
			date = str_week + ", " + mMonth + mDay
					+ this.getResources().getString(R.string.str_day);
		} else {
			date = "   " + str_week + ", " + mMonth + " " + mDay;
		}
		return date;
	}


	@SuppressWarnings("unchecked")
	public void getShortcutFromDefault(int srcPath, String desPath) {
		File desFile = new File(desPath);
		if (!desFile.exists()) {
			try {
				desFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(getResources()
					.openRawResource(srcPath)));
			String str = null;
			@SuppressWarnings("rawtypes")
			List list = new ArrayList();

			while ((str = br.readLine()) != null) {
				list.add(str);
			}
			bw = new BufferedWriter(new FileWriter(desFile));
			for (int i = 0; i < list.size(); i++) {
				bw.write(list.get(i).toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (bw != null)
					bw.close();
			} catch (IOException e) {

			}
		}
	}
	private void loadCustomApps(String path) {
		getAllApplication();
		File mFile = new File(path);
		if (!mFile.exists()) {
			getShortcutFromDefault(CustomAppsActivity.DEFAULT_SHORTCUR_PATH,
					CustomAppsActivity.SHORTCUT_PATH);
		} else {
			try {
				BufferedReader b = new BufferedReader(new FileReader(mFile));
				if (b.read() == -1) {
					getShortcutFromDefault(
							CustomAppsActivity.DEFAULT_SHORTCUR_PATH,
							CustomAppsActivity.SHORTCUT_PATH);
				}
				if (b != null)
					b.close();
			} catch (IOException e) {
			}
		}
		String savepkg;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(mFile));
			String str = null;
			while ((str = br.readLine()) != null) {
				if (str.startsWith(CustomAppsActivity.HOME_SHORTCUT_HEAD)) {
					str = str.replaceAll(CustomAppsActivity.HOME_SHORTCUT_HEAD,
							"");
					list_homeShortcut = str.split(";");
				} /*else if (str
						.startsWith(CustomAppsActivity.VIDEO_SHORTCUT_HEAD)) {
						str = str.replaceAll(
								CustomAppsActivity.VIDEO_SHORTCUT_HEAD, "");
					int length = str.length();
					savepkg = str.substring(0,length - 1);
					if (!allPackages.contains(savepkg)){
						str = "com.android.rockchip;";
					}
					list_videoShortcut = str.split(";");
				} else if (str
						.startsWith(CustomAppsActivity.RECOMMEND_SHORTCUT_HEAD)) {
						str = str.replaceAll(
								CustomAppsActivity.RECOMMEND_SHORTCUT_HEAD, "");
					int length = str.length();
					savepkg = str.substring(0,length - 1);
					if (!allPackages.contains(savepkg)){
						str = "com.android.chrome;";
					}
					list_recommendShortcut = str.split(";");
				} else if (str
						.startsWith(CustomAppsActivity.MUSIC_SHORTCUT_HEAD)) {
						str = str.replaceAll(
								CustomAppsActivity.MUSIC_SHORTCUT_HEAD, "");
					int length = str.length();
					savepkg = str.substring(0,length - 1);
					if (!allPackages.contains(savepkg)){
						str = "android.rk.RockVideoPlayer;";
					}
					list_musicShortcut = str.split(";");
				} else if (str
						.startsWith(CustomAppsActivity.LOCAL_SHORTCUT_HEAD)) {
					str = str.replaceAll(
							CustomAppsActivity.LOCAL_SHORTCUT_HEAD, "");
					list_localShortcut = str.split(";");
				}*/
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
			}
		}
	}

	private List<Map<String, Object>> loadShortcutList(PackageManager manager,
			final List<ResolveInfo> apps, String[] list_custom_apps) {
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<>();

		if (list_custom_apps != null) {
			for (int i = 0; i < list_custom_apps.length; i++) {
				if (apps != null) {
					final int count = apps.size();
					for (int j = 0; j < count; j++) {
						ApplicationInfo application = new ApplicationInfo();
						ResolveInfo info = apps.get(j);

						application.title = info.loadLabel(manager);
						application
								.setActivity(
										new ComponentName(
												info.activityInfo.applicationInfo.packageName,
												info.activityInfo.name),
										Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						application.icon = info.activityInfo.loadIcon(manager);
						if (application.componentName.getPackageName().equals(list_custom_apps[i])) {
						//	if (application.componentName.getPackageName().equals("cn.sn.zwcx.entertainment")) continue;
						//	12345657890
							map = new HashMap<>();
							map.put("item_name", application.title.toString());
							map.put("file_path", application.intent);
							map.put("item_type", application.icon);
							map.put("item_symbol", application.componentName);
							list.add(map);
							break;
						}
					}
				}
			}
		}

		return list;
	}

	private Map<String, Object> getAddMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		return map;
	}

	private List<Map<String,Object>> appsInfo;
	private void loadApplications() {
		List<Map<String, Object>> HomeShortCutList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> videoShortCutList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> recommendShortCutList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> appShortCutList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> musicShortCutList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> localShortCutList = new ArrayList<Map<String, Object>>();

		PackageManager manager = getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		HomeShortCutList.clear();
		videoShortCutList.clear();
		recommendShortCutList.clear();
		appShortCutList.clear();
		musicShortCutList.clear();
		localShortCutList.clear();

		loadCustomApps(CustomAppsActivity.SHORTCUT_PATH);

		if (updateAllShortcut == true) {
			HomeShortCutList = loadShortcutList(manager, apps,
					list_homeShortcut);
			videoShortCutList = loadShortcutList(manager, apps,
					list_videoShortcut);
			recommendShortCutList = loadShortcutList(manager, apps,
					list_recommendShortcut);
			musicShortCutList = loadShortcutList(manager, apps,
					list_musicShortcut);
			localShortCutList = loadShortcutList(manager, apps,
					list_localShortcut);

			if (apps != null) {
				final int count = apps.size();
				for (int i = 0; i < count; i++) {
					ApplicationInfo application = new ApplicationInfo();
					ResolveInfo info = apps.get(i);

					application.title = info.loadLabel(manager);
					application
							.setActivity(
									new ComponentName(
											info.activityInfo.applicationInfo.packageName,
											info.activityInfo.name),
									Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					application.icon = info.activityInfo.loadIcon(manager);
					
					if (application.componentName.getPackageName().equals("cn.sn.zwcx.entertainment") )
						continue;

					Map<String, Object> map = new HashMap<>();
					map.put("item_name", application.title.toString());
					map.put("file_path", application.intent);
					map.put("item_type", application.icon);
					map.put("item_symbol", application.componentName);
					map.put("item_pck",info.activityInfo.packageName);

					appShortCutList.add(map);
				}
				appsInfo = appShortCutList;
			}

			Map<String, Object> map = getAddMap();
			HomeShortCutList.add(map);

			homeShortcutView.setLayoutView(HomeShortCutList, 0);
			videoShortcutView.setLayoutView(videoShortCutList, 3);
			recommendShortcutView.setLayoutView(recommendShortCutList, 4);
			appShortcutView.setLayoutView(appShortCutList, 1);
			musicShortcutView.setLayoutView(musicShortCutList, 4);
			localShortcutView.setLayoutView(localShortCutList, 1);
			tx_video_allcount.setText("/"
					+ Integer.toString(videoShortCutList.size()));
			tx_recommend_allcount.setText("/"
					+ Integer.toString(recommendShortCutList.size()));
			tx_app_allcount.setText("/"
					+ Integer.toString(appShortCutList.size()));
			tx_music_allcount.setText("/"
					+ Integer.toString(musicShortCutList.size()));
			tx_local_allcount.setText("/"
					+ Integer.toString(localShortCutList.size()));

			updateAllShortcut = false;
		} else if (Launcher.current_shortcutHead
				.equals(CustomAppsActivity.VIDEO_SHORTCUT_HEAD)) {
			videoShortCutList = loadShortcutList(manager, apps,
					list_videoShortcut);
			videoShortcutView.setLayoutView(videoShortCutList, 3);
			tx_video_allcount.setText("/"
					+ Integer.toString(videoShortCutList.size()));
		} else if (Launcher.current_shortcutHead
				.equals(CustomAppsActivity.RECOMMEND_SHORTCUT_HEAD)) {
			recommendShortCutList = loadShortcutList(manager, apps,
					list_recommendShortcut);
			recommendShortcutView.setLayoutView(recommendShortCutList, 4);
			tx_recommend_allcount.setText("/"
					+ Integer.toString(recommendShortCutList.size()));
		} else if (Launcher.current_shortcutHead
				.equals(CustomAppsActivity.MUSIC_SHORTCUT_HEAD)) {
			musicShortCutList = loadShortcutList(manager, apps,
					list_musicShortcut);
			musicShortcutView.setLayoutView(musicShortCutList, 4);
			tx_music_allcount.setText("/"
					+ Integer.toString(musicShortCutList.size()));
		} else if (Launcher.current_shortcutHead
				.equals(CustomAppsActivity.LOCAL_SHORTCUT_HEAD)) {
			localShortCutList = loadShortcutList(manager, apps,
					list_localShortcut);
			Map<String, Object> map = getAddMap();
			localShortCutList.add(map);
			localShortcutView.setLayoutView(localShortCutList, 1);
			tx_local_allcount.setText("/"
					+ Integer.toString(localShortCutList.size()));
		} else {
			HomeShortCutList = loadShortcutList(manager, apps,
					list_homeShortcut);
			Map<String, Object> map = getAddMap();
			HomeShortCutList.add(map);
			homeShortcutView.setLayoutView(HomeShortCutList, 0);
		}

		getThirdApplication();
		String pkg = sp.getString("app_pkg", "");
		mCustompkg = pkg;
		if (!packages.contains(pkg)) {
			SharedPreferences.Editor editor = sp.edit();
			showLauncher.setVisibility(View.GONE);
			editor.putString("app_pkg", "");
			editor.putString("app_name", "");
			editor.putBoolean("isChecked", false);
			editor.commit();
		}

		loadCustomApps(CustomAppsActivity.SHORTCUT_PATH);
	}

	private String mCustompkg;

	private void setRectOnKeyListener() {
	//	findViewById(R.id.layout_time).setOnKeyListener(new MyOnKeyListener(this, null));
		findViewById(R.id.layout_video).setOnKeyListener(
				new MyOnKeyListener(this, null));
		findViewById(R.id.layout_apps).setOnKeyListener(new MyOnKeyListener(this,null));
		findViewById(R.id.layout_entertainment).setOnKeyListener(new MyOnKeyListener(this,null));
		findViewById(R.id.layout_speed_test).setOnKeyListener(new MyOnKeyListener(this,null));
	//	findViewById(R.id.layout_video_f).setOnKeyListener(new MyOnKeyListener(this, null));
	//	findViewById(R.id.layout_recommend).setOnKeyListener(new MyOnKeyListener(this, null));
		findViewById(R.id.layout_setting).setOnKeyListener(
				new MyOnKeyListener(this, null));
	//	findViewById(R.id.layout_app).setOnKeyListener(new MyOnKeyListener(this, null));
	//	findViewById(R.id.layout_music).setOnKeyListener(new MyOnKeyListener(this, null));
	//	findViewById(R.id.layout_cleanup).setOnKeyListener(new MyOnKeyListener(this, null));
		findViewById(R.id.layout_local).setOnKeyListener(
				new MyOnKeyListener(this, null));
	//	findViewById(R.id.layout_about).setOnKeyListener(new MyOnKeyListener(this, null));

	//	findViewById(R.id.layout_time).setOnTouchListener(new MyOnTouchListener(this, null));
	//	findViewById(R.id.layout_time).setOnTouchListener(new MyOnTouchListener(this, null));
	//	findViewById(R.id.layout_video_f).setOnTouchListener(new MyOnTouchListener(this, null));
		findViewById(R.id.layout_video).setOnTouchListener(
				new MyOnTouchListener(this, null));
		findViewById(R.id.layout_apps).setOnTouchListener(new MyOnTouchListener(this,null));
		findViewById(R.id.layout_entertainment).setOnTouchListener(new MyOnTouchListener(this,null));
		findViewById(R.id.layout_speed_test).setOnTouchListener(new MyOnTouchListener(this,null));
	//	findViewById(R.id.layout_recommend).setOnTouchListener(new MyOnTouchListener(this, null));
		findViewById(R.id.layout_setting).setOnTouchListener(
				new MyOnTouchListener(this, null));
	//	findViewById(R.id.layout_app).setOnTouchListener(new MyOnTouchListener(this, null));
	//	findViewById(R.id.layout_music).setOnTouchListener(new MyOnTouchListener(this, null));
	//	findViewById(R.id.layout_cleanup).setOnTouchListener(new MyOnTouchListener(this, null));
		findViewById(R.id.layout_local).setOnTouchListener(
				new MyOnTouchListener(this, null));
	//	findViewById(R.id.layout_about).setOnTouchListener(new MyOnTouchListener(this, null));
	}


	private void setHeight() {
		if (isNative4k2k) {
			REAL_OUTPUT_MODE = "4k2knative";
			CustomAppsActivity.CONTENT_HEIGHT = 900;
		} else if (isRealOutputMode && !isNative720) {
			REAL_OUTPUT_MODE = "1080p";
			CustomAppsActivity.CONTENT_HEIGHT = 450;
		} else {
			REAL_OUTPUT_MODE = "720p";
			CustomAppsActivity.CONTENT_HEIGHT = 450;
		}
		Display display = this.getWindowManager().getDefaultDisplay();
		Point p = new Point();
		display.getRealSize(p);
		SCREEN_HEIGHT = p.y;
		SCREEN_WIDTH = p.x;
	}

	public void setPopWindow(int top, int bottom) {
		View view = this.getWindow().getDecorView();
		view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		view.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		view.destroyDrawingCache();

		screenShot = null;
		screenShot_keep = null;

		if (bottom > SCREEN_HEIGHT / 2) {
			if (top + 3 - CustomAppsActivity.CONTENT_HEIGHT > 0) {
				screenShot = Bitmap
						.createBitmap(bmp, 0, 0, bmp.getWidth(), top);
				screenShot_keep = Bitmap.createBitmap(bmp, 0,
						CustomAppsActivity.CONTENT_HEIGHT, bmp.getWidth(), top
								+ 3 - CustomAppsActivity.CONTENT_HEIGHT);
			} else {
				screenShot = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						CustomAppsActivity.CONTENT_HEIGHT);
				screenShot_keep = null;
			}
		} else {
			screenShot = Bitmap.createBitmap(bmp, 0, bottom, bmp.getWidth(),
					SCREEN_HEIGHT - bottom);
			screenShot_keep = Bitmap.createBitmap(bmp, 0, bottom,
					bmp.getWidth(), SCREEN_HEIGHT
							- (bottom + CustomAppsActivity.CONTENT_HEIGHT));
		}
	}

	public static int parseItemIcon(String packageName) {
			if (packageName.equals("com.android.music")) {
			return R.drawable.img_music;
		}
		return -1;
	}

	private void resetShadow() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(2);
			}
		}).start();
	}

	private void updateAppList(Intent intent) {
		intent.getBooleanExtra(
				Intent.EXTRA_REPLACING, false);
		boolean isShortcutIndex = false;
		String packageName = null;

		if (intent.getData() != null) {
			packageName = intent.getData().getSchemeSpecificPart();
			if (packageName == null || packageName.length() == 0) {
				// they sent us a bad intent
				return;
			}
			if (packageName.equals("com.android.provision"))
				return;
		}

		if (getCurrentFocus() != null
				&& getCurrentFocus().getParent() instanceof MyGridLayout) {
			int parentId = ((MyGridLayout) getCurrentFocus().getParent())
					.getId();
			dontRunAnim = true;
			if (parentId != View.NO_ID) {
				String name = getResources().getResourceEntryName(parentId);
				if (name.equals("gv_shortcut")) {
					numberInGridOfShortcut = ((MyGridLayout) getCurrentFocus()
							.getParent()).indexOfChild(getCurrentFocus());
					isShortcutIndex = true;
				}
			}
			if (!isShortcutIndex) {
				numberInGrid = ((MyGridLayout) getCurrentFocus().getParent())
						.indexOfChild(getCurrentFocus());
			}
		} else {
			numberInGrid = -1;
		}

		updateAllShortcut = true;
		ifChangedShortcut = true;
		displayShortcuts();

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				MyRelativeLayout view = (MyRelativeLayout) getCurrentFocus();
				view.setSurface();
				break;
			case 3:
				ViewGroup findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
						.getCurrentView()).getChildAt(5)).getChildAt(0));
				int count = findGridLayout.getChildCount();
				Launcher.dontRunAnim = true;
				findGridLayout.getChildAt(count - 1).requestFocus();
				Launcher.dontRunAnim = false;
				break;
			case 4:
				if (numberInGrid != -1) {
					try {
						findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
                                .getCurrentView()).getChildAt(5)).getChildAt(0));
						Launcher.dontRunAnim = true;
						findGridLayout.getChildAt(numberInGrid).requestFocus();
						Launcher.dontRunAnim = false;
						numberInGrid = -1;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case 5:
				if (numberInGridOfShortcut != -1) {
					Launcher.dontRunAnim = true;
					saveHomeFocusView = homeShortcutView
							.getChildAt(numberInGridOfShortcut);
					saveHomeFocusView.requestFocus();
					Launcher.dontRunAnim = false;
					numberInGridOfShortcut = -1;
				}
				break;
			case 6:
				int i = homeShortcutView.getChildCount();
				Launcher.dontRunAnim = true;
				homeShortcutView.getChildAt(i - 1).requestFocus();
				Launcher.dontRunAnim = false;
				if (!isInTouchMode) {
					layoutScaleShadow.setVisibility(View.VISIBLE);
				//	frameView.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	};

	private BroadcastReceiver netReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action == null)
				return;


			if (action.equals(outputmode_change_action)) {
				setHeight();
			}

			if (action.equals(Intent.ACTION_TIME_TICK)) {
				displayDate();

				time_count++;
				if (time_count >= time_freq) {
					time_count = 0;
				}
			} else if (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE
					.equals(action)
					|| Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE
							.equals(action)) {
				updateAppList(intent);
			} else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
					if (TextUtils.isEmpty(mCity)) {
					//	getLocalCity();
					}
				}

			} else {
				updateWifiLevel();
			}
		}
	};

	private BroadcastReceiver appReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			final String action = intent.getAction();
			if (Intent.ACTION_PACKAGE_CHANGED.equals(action)
					|| Intent.ACTION_PACKAGE_REMOVED.equals(action)
					|| Intent.ACTION_PACKAGE_ADDED.equals(action)) {

				updateAppList(intent);

			}
		}
	};

    public static void diyBackgroundAndFontToast(int resource){
        View view = View.inflate(mContext,R.layout.toast_layout,null);
        TextView txt = view.findViewById(R.id.toast_tv);
        txt.setText(resource);
        Toast toast = Toast.makeText(mContext,null,Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

	public static void diyBackgroundAndFontToast(String str){
		View view = View.inflate(mContext,R.layout.toast_layout,null);
		TextView txt = view.findViewById(R.id.toast_tv);
		txt.setText(str);
		Toast toast = Toast.makeText(mContext,null,Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
	
	private BroadcastReceiver bootReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				if (action.equals(BOOT_ACTION)) {
					SharedPreferences.Editor edit = sp.edit();
					edit.putBoolean("isFirst",false);
					edit.apply();
				}
			}		
		}
	};
	
}
