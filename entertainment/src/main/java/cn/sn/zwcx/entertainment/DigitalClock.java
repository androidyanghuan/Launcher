package cn.sn.zwcx.entertainment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DigitalClock extends LinearLayout {
	private static Context mContext;

	static SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
	static SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy", // "yyyy/MM/dd",
			Locale.getDefault());
	static String smPmStr = DateUtils.getAMPMString(Calendar.getInstance().get(Calendar.AM_PM));
	static Calendar cal = Calendar.getInstance();
	private TextView textViewTime, textViewDate, textViewWeek, textViewAMPM;

	// public DigitalClock(Context context, AttributeSet attrs, int defStyle) {
	// super(context, attrs, defStyle);
	// }
	public DigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.digitalcolck_layout, this);
		textViewAMPM = (TextView) findViewById(R.id.textViewAMPM);
		textViewTime = (TextView) findViewById(R.id.textViewTime);
		textViewDate = (TextView) findViewById(R.id.textViewDate);
		textViewWeek = (TextView) findViewById(R.id.textViewWeek);
		startThread();

	}

	public static String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;
	}

	public static String getCurrentTime(Date date) {

		sdf_time.format(date);
		return sdf_time.format(date);
	}

	public static String getCurrentDate(Date date) {

		sdf_date.format(date);
		return sdf_date.format(date);
	}

	public  String getCurrentWeekDay(Date dt) {
		String[] weekDays = { getResources().getString(R.string.sun), getResources().getString(R.string.mon),
				getResources().getString(R.string.tue), getResources().getString(R.string.wed),
				getResources().getString(R.string.thu), getResources().getString(R.string.fri),
				getResources().getString(R.string.sat) };
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public static String getCurrentAMPM() {
		String ampmValues;
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		if (calendar.get(Calendar.AM_PM) == 0) {
			ampmValues = "AM";
		} else {
			ampmValues = "PM";
		}
		return ampmValues;
	};

	private void startThread() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					handler.sendEmptyMessage(12);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 12) {
				Calendar c = Calendar.getInstance();
				String time12 = formatTime(c.get(Calendar.HOUR)) + ":" + formatTime(c.get(Calendar.MINUTE)) + ":"
						+ formatTime(c.get(Calendar.SECOND));

				String time24 = formatTime(c.get(Calendar.HOUR_OF_DAY)) + ":" + formatTime(c.get(Calendar.MINUTE)) + ":"
						+ formatTime(c.get(Calendar.SECOND));

				Date date = new Date();

				ContentResolver cv = getContext().getContentResolver();
				String strTimeFormat = android.provider.Settings.System.getString(cv,
						android.provider.Settings.System.TIME_12_24);
				textViewAMPM.setText(getCurrentAMPM());
//				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
//					textViewTime.setText(time12);
//				} else {
					if (!TextUtils.isEmpty(strTimeFormat) && strTimeFormat.equals("24")) {
						textViewTime.setText(time24);
					} else {
						textViewTime.setText(time12);
					}
//				}
				textViewDate.setText(getCurrentDate(date));
				textViewWeek.setText(getCurrentWeekDay(date));
			}
		}
	};

}