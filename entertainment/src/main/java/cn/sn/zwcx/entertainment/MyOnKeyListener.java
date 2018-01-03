package cn.sn.zwcx.entertainment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.lang.reflect.Method;
import java.util.Locale;


public class MyOnKeyListener implements OnKeyListener {
	private final static String TAG = "MyOnKeyListener";

	private int NUM_VIDEO = 0;
	private int NUM_RECOMMEND = 1;
	private int NUM_APP = 2;
	private int NUM_MUSIC = 3;
	private int NUM_LOCAL = 4;

	private Context mContext;
	private Object appPath;

	public MyOnKeyListener(Context context, Object path) {
		mContext = context;
		appPath = path;
	}

	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if (Launcher.animIsRun)
			return true;
		if (keyCode != KeyEvent.KEYCODE_BACK)
			Launcher.isInTouchMode = false;
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
			ImageView img = (ImageView) ((ViewGroup) view).getChildAt(0);
			String path = img.getResources().getResourceName(img.getId());
			String vName = path.substring(path.indexOf("/") + 1);
			if (vName.equals("img_setting")) {
				Launcher.saveHomeFocusView = view;
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.android.tv.settings",
						"com.android.tv.settings.MainSettings"));
				mContext.startActivity(intent);
				Launcher.IntoApps = true;
				return true;
			} else if (vName.equals("img_time")) {
				//	showMenuView(NUM_VIDEO, view);
				Launcher.saveHomeFocusView = view;
				Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
				mContext.startActivity(intent);
				Launcher.IntoApps = true;
				return true;
			} else if (vName.equals("img_video")) {
				//	Launcher.isShowMenu = false;
				//	showMenuView(NUM_VIDEO, view);
				try {
					Launcher.saveHomeFocusView = view;
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("android.rk.RockVideoPlayer", "android.rk.RockVideoPlayer.RockVideoPlayer"));
					mContext.startActivity(intent);
					Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else if (vName.equals("img_recommend")) {
				Launcher.isShowMenu = false;
				showMenuView(NUM_RECOMMEND, view);
				return true;
			} else if (vName.equals("img_apps")) {
				//	Launcher.isShowMenu = true;
				showMenuView(NUM_APP, view);
				return true;
			} else if (vName.equals("img_video_v")) {
				//showMenuView(NUM_APP, view);
				Launcher.saveHomeFocusView = view;
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("cn.sn.zwcx.weather", "cn.sn.zwcx.weather.activitys.MainActivity"));
				mContext.startActivity(intent);
				Launcher.IntoApps = true;
				return true;
			} else if (vName.equals("img_speed_test")) {
				//	showMenuView(NUM_LOCAL, view);
			/*	Launcher.saveHomeFocusView = view;
				String language = Locale.getDefault().getLanguage();
				Intent intent = new Intent();
				
				if (language.equals("zh")) {
					try{
						intent.setComponent(new ComponentName("com.shafa.market", "com.shafa.market.ShafaHomeAct"));
						mContext.startActivity(intent);
						Launcher.IntoApps = true;
					}catch (Exception e) {
					//	Launcher.diyBackgroundAndFontToast(R.string.install_shafa);
					}
				}else{
					try{
						intent.setComponent(new ComponentName("cm.aptoidetv.pt.sonicway", "cm.aptoidetv.pt.activity.MainActivity"));
						mContext.startActivity(intent);
						Launcher.IntoApps = true;
					}catch (Exception e){
					//	Launcher.diyBackgroundAndFontToast(R.string.intstall_mobdro);
					}
				}
*/
				try {
					Intent intent = new Intent();
					Launcher.saveHomeFocusView = view;
					intent.setComponent(new ComponentName("com.netflix.Speedtest", "com.netflix.Speedtest.MainActivity"));
					mContext.startActivity(intent);
					Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else if (vName.equals("img_local")) {
				//	Launcher.isShowMenu = false;
				//	showMenuView(NUM_MUSIC, view);
				try {
					Launcher.saveHomeFocusView = view;
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("com.android.chrome", "com.google.android.apps.chrome.Main"));
					mContext.startActivity(intent);
					Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else if (vName.equals("img_entertainment")) {
				//	showMenuView(NUM_LOCAL, view);
			/*	Launcher.saveHomeFocusView = view;
				mContext.startActivity(new Intent(mContext,RocketActivity.class));
				Launcher.IntoApps = true;*/
				try {
					Launcher.saveHomeFocusView = view;
					Intent intent = new Intent();
					intent.setComponent(new ComponentName("org.xbmc.kodi", "org.xbmc.kodi.Splash"));
					mContext.startActivity(intent);
					Launcher.IntoApps = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			} else if (vName.equals("img_about")) {
				//	showMenuView(NUM_MUSIC, view);
				Launcher.saveHomeFocusView = view;
				Intent intent_b = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
				mContext.startActivity(intent_b);
				Launcher.IntoApps = true;
				return true;
			} else {
				if (appPath != null) {
					if (Launcher.isShowHomePage) {
						Launcher.saveHomeFocusView = view;
					}
				/*	if (appPath.toString().contains("com.android.chrome") && Locale.getDefault().getLanguage().equals("zh")) {
						Log.e(TAG, "***********点击了google chrome 中文****************");
						try {
							ClassLoader cl = mContext.getClassLoader();
							Class c = cl.loadClass("android.os.SystemProperties");
							Method method = c.getMethod("set", String.class);
							//	String homepage = (String) method.invoke(c, "ro.rk.homepage_base");
							Object invoke = method.invoke(c, "ro.rk.homepage_base");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}*/
					mContext.startActivity((Intent) appPath);
				//	Log.e(TAG, "============app path=============" + appPath.toString());

					Launcher.IntoApps = true;
				}
			}
		}/*  else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				&& !Launcher.isShowHomePage) {
			ImageView img = (ImageView) ((ViewGroup) view).getChildAt(0);
			String path = img.getResources().getResourceName(img.getId());
			String vName = path.substring(path.indexOf("/") + 1);

			if (checkNextFocusedIsNull(view, View.FOCUS_LEFT)) {
				Launcher.accessBoundaryCount = 0;
				Animation animIn = AnimationUtils.loadAnimation(mContext,
						R.anim.push_left_in);
				Animation animOut = AnimationUtils.loadAnimation(mContext,
						R.anim.push_left_out);
				animIn.setAnimationListener(new MyAnimationListener(0));
				animOut.setAnimationListener(new MyAnimationListener(1));
				Launcher.viewMenu.setInAnimation(animIn);
				Launcher.viewMenu.setOutAnimation(animOut);
				Launcher.viewMenu.showPrevious();
				return true;
			}

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				&& !Launcher.isShowHomePage) {
			ImageView img = (ImageView) ((ViewGroup) view).getChildAt(0);
			String path = img.getResources().getResourceName(img.getId());
			String vName = path.substring(path.indexOf("/") + 1);

			if (checkNextFocusedIsNull(view, View.FOCUS_RIGHT)) {
				Launcher.accessBoundaryCount = 0;
				Animation animIn = AnimationUtils.loadAnimation(mContext,
						R.anim.push_right_in);
				Animation animOut = AnimationUtils.loadAnimation(mContext,
						R.anim.push_right_out);
				animIn.setAnimationListener(new MyAnimationListener(2));
				animOut.setAnimationListener(new MyAnimationListener(3));
				Launcher.viewMenu.setInAnimation(animIn);
				Launcher.viewMenu.setOutAnimation(animOut);
				Launcher.viewMenu.showNext();
				return true;
			}
		}*/ else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		/*	Launcher.saveHomeFocusView = view;
				Intent intent = new Intent();
				intent.setComponent(new ComponentName("com.android.settings",
						"com.android.settings.Settings"));
				mContext.startActivity(intent);
				Launcher.IntoApps = true;
			if (appPath != null) {
					if (Launcher.isShowHomePage) {
						Launcher.saveHomeFocusView = view;
					}
					mContext.startActivity((Intent) appPath);
					Launcher.IntoApps = true;
				}

				
				Intent intent = new Intent();
				intent.setClass(mContext, CustomAppsActivity.class);
				mContext.startActivity(intent);
			//	IntoCustomActivity = true;
				
		 	}*/
			if (keyCode == KeyEvent.KEYCODE_WINDOW) {
				showMenuView(NUM_APP, Launcher.APPView);
				return true;
			}
			return false;

	}

	private void showMenuView(int num, View view) {
	/*	String name = sp.getString("app_name", "");
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
		ScaleAnimation scaleAnimationIn = new ScaleAnimation(1.0f, 1.0f, 1.0f,
				1.0f, getPiovtX(rect), getPiovtY(rect));
		scaleAnimationIn.setDuration(200);
		scaleAnimationIn.setAnimationListener(new MyAnimationListener(5));
		Launcher.viewMenu.setInAnimation(scaleAnimationIn);
		Launcher.viewMenu.setOutAnimation(null);

		Launcher.viewHomePage.setVisibility(View.GONE);
		Launcher.viewMenu.setVisibility(View.VISIBLE);
		Launcher.viewMenu.setDisplayedChild(num);
		Launcher.viewMenu.getChildAt(num).requestFocus();
	}

	private float getPiovtX(Rect rect) {
		return (float) ((rect.left + rect.width() / 2));
	}

	private float getPiovtY(Rect rect) {
		return (float) ((rect.top + rect.height() / 2));
	}

	private boolean checkNextFocusedIsNull(View view, int dec) {
		ViewGroup gridLayout = (ViewGroup) view.getParent();
		if (FocusFinder.getInstance().findNextFocus(gridLayout,
				gridLayout.findFocus(), dec) == null) {
			Launcher.accessBoundaryCount++;
		} else {
			Launcher.accessBoundaryCount = 0;
		}

		if (Launcher.accessBoundaryCount <= 1)
			return false;
		else {
			Launcher.dontRunAnim = true;
			Launcher.animIsRun = true;
			Launcher.layoutScaleShadow.setVisibility(View.INVISIBLE);
		//	Launcher.frameView.setVisibility(View.INVISIBLE);
			return true;
		}
	}

	private class MyAnimationListener implements AnimationListener {
		private int in_or_out;

		public MyAnimationListener(int flag) {
			in_or_out = flag;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			Launcher.animIsRun = true;
			Launcher.layoutScaleShadow.setVisibility(View.INVISIBLE);
		//	Launcher.frameView.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (in_or_out == 1) {
				if (((ViewGroup) Launcher.viewMenu.getCurrentView())
						.getChildAt(4) instanceof MyScrollView) {
					ViewGroup findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) Launcher.viewMenu
							.getCurrentView()).getChildAt(4)).getChildAt(0));
					int count = findGridLayout.getChildCount() < 6 ? findGridLayout
							.getChildCount() - 1 : 5;
					Launcher.dontRunAnim = true;
					findGridLayout.getChildAt(count).requestFocus();
					Launcher.dontRunAnim = false;
				}
			} else if (in_or_out == 3) {
				// Launcher.dontDrawFocus = false;
				Launcher.dontRunAnim = true;
				Launcher.viewMenu.clearFocus();
				Launcher.dontRunAnim = true;
				Launcher.viewMenu.requestFocus();
				Launcher.dontRunAnim = false;
			} else if (in_or_out == 5) {
				Launcher.dontRunAnim = true;
				Launcher.viewMenu.clearFocus();
				Launcher.dontRunAnim = true;
				Launcher.viewMenu.requestFocus();
				Launcher.dontRunAnim = false;
			}
			Launcher.animIsRun = false;
			Launcher.layoutScaleShadow.setVisibility(View.VISIBLE);
		//	Launcher.frameView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

	}

}
