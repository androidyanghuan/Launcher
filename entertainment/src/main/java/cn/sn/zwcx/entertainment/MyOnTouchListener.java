package cn.sn.zwcx.entertainment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Locale;


public class MyOnTouchListener implements OnTouchListener {
	private final static String TAG = "MyOnTouchListener";

	private int NUM_VIDEO = 0;
	private int NUM_RECOMMEND = 1;
	private int NUM_APP = 2;
	private int NUM_MUSIC = 3;
	private int NUM_LOCAL = 4;

	private Context mContext;
	private Object appPath;

	public MyOnTouchListener(Context context, Object path) {
		mContext = context;
		appPath = path;
	}

	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		Launcher.isInTouchMode = true;
		if (event.getAction() == MotionEvent.ACTION_UP) {
			ImageView img = (ImageView) ((ViewGroup) view).getChildAt(0);
			String path = img.getResources().getResourceName(img.getId());
			String vName = path.substring(path.indexOf("/") + 1);
			if (vName.equals("img_setting")) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.android.tv.settings",
						"com.android.tv.settings.MainSettings"));
				mContext.startActivity(intent);
				return true;
			} else if (vName.equals("img_entertainment")) {
			//	mContext.startActivity(new Intent(mContext,RocketActivity.class));
				try {
				//	Launcher.saveHomeFocusView = view;
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("org.xbmc.kodi", "org.xbmc.kodi.Splash"));
					mContext.startActivity(intent);
				//	Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else if (vName.equals("img_video")) {
				//showMenuView(NUM_VIDEO, view);
			/*	Intent intent = new Intent();
				intent.setComponent(new ComponentName("cn.sn.zwcx.weather",	"cn.sn.zwcx.weather.activitys.MainActivity"));
				mContext.startActivity(intent);*/
				try {
				//	Launcher.saveHomeFocusView = view;
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("android.rk.RockVideoPlayer","android.rk.RockVideoPlayer.RockVideoPlayer"));
					mContext.startActivity(intent);
				//	Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else if (vName.equals("img_time")) {
				//showMenuView(NUM_VIDEO, view);	
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.android.settings",
						"com.android.settings.Settings$DateTimeSettingsActivity"));
				mContext.startActivity(intent);
			} else if (vName.equals("img_speed_test")) {
			//	Launcher.isShowMenu = false;
			//	showMenuView(NUM_RECOMMEND, view);
				try {
					Intent intent = new Intent();
				//	Launcher.saveHomeFocusView = view;
					intent.setComponent(new ComponentName("com.netflix.Speedtest", "com.netflix.Speedtest.MainActivity"));
					mContext.startActivity(intent);
				//	Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else if (vName.equals("img_apps")) {
				Launcher.isShowMenu = true;
				showMenuView(NUM_APP, view);
			} else if (vName.equals("img_music")) {
				Launcher.isShowMenu = false;
				showMenuView(NUM_MUSIC, view);
			} else if (vName.equals("img_about")) {
				//showMenuView(NUM_LOCAL, view);
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.android.settings","com.android.settings.Settings$DeviceInfoSettingsActivity"));
				mContext.startActivity(intent);	
			} else if (vName.equals("img_local")) {
				//showMenuView(NUM_LOCAL, view);
			/*	String language = Locale.getDefault().getLanguage();
				Intent intent = new Intent();
				if (language.equals("zh")) {
					try{
						intent.setComponent(new ComponentName("com.shafa.market", "com.shafa.market.ShafaHomeAct"));
						mContext.startActivity(intent);
					} catch (Exception e) {
					//	Launcher.diyBackgroundAndFontToast(R.string.install_shafa);
					}
				}else{
					try{
						intent.setComponent(new ComponentName("cm.aptoidetv.pt.sonicway", "cm.aptoidetv.pt.activity.MainActivity"));
						mContext.startActivity(intent);
					}catch (Exception e) {
					//	Launcher.diyBackgroundAndFontToast(R.string.intstall_mobdro);
					}
				}

			} else {
				if (appPath != null) {
					mContext.startActivity((Intent) appPath);
				}
			}*/
				try {
				//	Launcher.saveHomeFocusView = view;
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("com.android.chrome", "com.google.android.apps.chrome.Main"));
					mContext.startActivity(intent);
				//	Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			ImageView img = (ImageView) ((ViewGroup) view).getChildAt(0);
			String path = img.getResources().getResourceName(img.getId());
			String vName = path.substring(path.indexOf("/") + 1);

			if (vName.equals("img_video") || vName.equals("img_setting")
					|| vName.equals("img_apps") || vName.equals("img_entertainment")|| vName.equals("img_speed_test")
					|| vName.equals("img_local")) {
				return view.onTouchEvent(event);
			}
		}

		return false;
	}

	private void showMenuView(int num, View view) {
	/*	String name = Launcher.sp.getString("app_name", "");
		if (Launcher.isShowMenu && !TextUtils.isEmpty(name)){
			showLauncher.setText(name + " " + mContext.getResources().getString(R.string.set_launcher_success));
			showLauncher.setVisibility(View.VISIBLE);
		} else {
			if (Launcher.isShowMenu && !TextUtils.isEmpty(Launcher.appName)) {
				showLauncher.setText(Launcher.appName + " " + mContext.getResources().getString(R.string.set_launcher_success));
				showLauncher.setVisibility(View.VISIBLE);
			} else {
				showLauncher.setVisibility(View.GONE);
			}
		}*/
		Launcher.saveHomeFocusView = view;
		Launcher.isShowHomePage = false;
		Launcher.layoutScaleShadow.setVisibility(View.INVISIBLE);
	//	Launcher.frameView.setVisibility(View.INVISIBLE);

		Rect rect = new Rect();
		view.getGlobalVisibleRect(rect);
		// ScaleAnimation scaleAnimationIn = new ScaleAnimation(0.0f, 1.0f,
		// 0.0f, 1.0f, getPiovtX(rect), getPiovtY(rect));
		// ScaleAnimation scaleAnimationOut = new ScaleAnimation(1.0f, 0.0f,
		// 1.0f, 0.0f, getPiovtX(rect), getPiovtY(rect));
		// scaleAnimationIn.setDuration(400);
		// scaleAnimationOut.setDuration(400);
		Launcher.viewMenu.setInAnimation(null);
		Launcher.viewMenu.setOutAnimation(null);

		Launcher.viewHomePage.setVisibility(View.GONE);
		Launcher.viewMenu.setVisibility(View.VISIBLE);
		Launcher.viewMenu.setDisplayedChild(num);
		Launcher.viewMenu.setFocusableInTouchMode(true);
	}

	private float getPiovtX(Rect rect) {
		return (float) ((rect.left + rect.width() / 2));
	}

	private float getPiovtY(Rect rect) {
		return (float) ((rect.top + rect.height() / 2));
	}

}
