package cn.sn.zwcx.entertainment;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class RocketActivity extends Activity {

	protected static final String TAG = "RocketActivity";

	private ImageView iv_rocket;
	private ImageView iv_cloud;
	private ImageView iv_cloud_line;
	private RelativeLayout rl_cloud;

	private AnimationDrawable fireAnimationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rocket);
		findView();
		initView();
		clearMemory();
	}

	private void findView() {
		iv_rocket = (ImageView) findViewById(R.id.iv_rocket);
		iv_cloud = (ImageView) findViewById(R.id.iv_cloud);
		iv_cloud_line = (ImageView) findViewById(R.id.iv_cloud_line);
		rl_cloud = (RelativeLayout) findViewById(R.id.rl_cloud);
	}

	private void initView() {
		iv_rocket.setPressed(true);
		iv_rocket.setFocusable(true);
		iv_rocket.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		iv_rocket.setBackgroundResource(R.drawable.rocket_fire);
		fireAnimationDrawable = (AnimationDrawable) iv_rocket.getBackground();
		fireAnimationDrawable.start();

		fly();
	}

	private void fly() {
		Log.e(TAG, "fly....");
		Animation animation = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.rocket_up);
		animation.setFillAfter(true);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

				AlphaAnimation cloudAppearAnimation = new AlphaAnimation(0.1f, 1.0f);
				cloudAppearAnimation.setDuration(500);

				Animation appearAnimation = new AlphaAnimation(0.0f, 1.0f);
				appearAnimation.setDuration(500);
				appearAnimation.setStartOffset(250);

				iv_cloud.startAnimation(cloudAppearAnimation);
				iv_cloud_line.startAnimation(appearAnimation);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				removeClound();
			}
		});

		iv_rocket.startAnimation(animation);

		createClound();
	}

	private void createClound() {
		iv_cloud.setVisibility(View.VISIBLE);
		iv_cloud_line.setVisibility(View.VISIBLE);
	}

	private void removeClound() {
		AlphaAnimation disappearAnimation = new AlphaAnimation(1.0f, 0.0f);
		disappearAnimation.setDuration(1000);
		disappearAnimation.setFillAfter(true);
		disappearAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

		disappearAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				iv_rocket.setVisibility(View.GONE);
				iv_cloud.setVisibility(View.GONE);
				iv_cloud_line.setVisibility(View.GONE);

				finish();
			}
		});
		rl_cloud.startAnimation(disappearAnimation);
	}

	private void clearMemory() {
		// To change body of implemented methods use File | Settings |
		// File Templates.
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
		am.getRunningServices(100);

		long beforeMem = getAvailMemory(this);
		Log.d(TAG, "-----------before memory info : " + beforeMem);
		int count = 0;
		if (infoList != null) {
			for (int i = 0; i < infoList.size(); ++i) {
				RunningAppProcessInfo appProcessInfo = infoList.get(i);
				Log.d(TAG, "process name : " + appProcessInfo.processName);
				// importance 该进程的重要程度 分为几个级别，数值越低就越重要�??
				Log.d(TAG, "importance : " + appProcessInfo.importance);

				// �?般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程�?
				// �?般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
				if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
					String[] pkgList = appProcessInfo.pkgList;
					for (int j = 0; j < pkgList.length; ++j) {
						Log.d(TAG, "It will be killed, package name : " + pkgList[j]);
						am.killBackgroundProcesses(pkgList[j]);
						count++;
					}
				}

			}
		}

		long afterMem = getAvailMemory(this);
		Log.d(TAG, "----------- after memory info : " + afterMem);
		Toast.makeText(this, "clear " + count + " process, " + (afterMem - beforeMem) + "M", Toast.LENGTH_LONG).show();

	}

	private long getAvailMemory(Context context) {
		// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内�?
		// return Formatter.formatFileSize(context, mi.availMem);//
		// 将获取的内存大小规格�?
		return mi.availMem / (1024 * 1024);
	}
}
