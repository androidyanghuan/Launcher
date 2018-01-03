/*-------------------------------------------------------------------------

-------------------------------------------------------------------------*/
package cn.sn.zwcx.entertainment;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map; 

public class MyGridLayout extends GridLayout {
	private final static String TAG = "MyGridLayout";
	private Context mContext;
	private final static int FLAG_HOME = 0;
	private final static int FLAG_CHILD_VIEW = 1;

	public MyGridLayout(Context context) {
		this(context,null);
	}

	public MyGridLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		mContext = context;
	}

	public MyGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	public void setLayoutView(List<Map<String, Object>> list, int flag) {
		int count = 0;

		if (this.getChildCount() > 0)
			this.removeAllViews();

		for (Map<String, Object> m : list) {
			count++;

			ViewGroup view;
			if (flag == FLAG_HOME) {
				view = (ViewGroup) View.inflate(mContext, R.layout.homegrid_item, null);

			} else {
				view = (ViewGroup) View.inflate(mContext, R.layout.childgrid_item, null);
				((TextView) view.getChildAt(1)).setText((String) m.get("item_name"));
			}

			ImageView img_bg = (ImageView) view.getChildAt(0);
			img_bg.setBackgroundResource(parseItemBackground(count, flag));
			if (m.get("item_type") instanceof Drawable) {
				int resId = Launcher.parseItemIcon(((ComponentName) m.get("item_symbol")).getPackageName());

				if (resId != -1) {
					img_bg.setImageResource(resId);
				} else {
					img_bg.setImageDrawable((Drawable) (m.get("item_type")));
				}
			} else {
				img_bg.setImageResource(R.drawable.item_img_add);
				img_bg.setContentDescription("img_add");
			}
			view.setOnKeyListener(new MyOnKeyListener(mContext, m.get("file_path")));
			view.setOnTouchListener(new MyOnTouchListener(mContext, m.get("file_path")));
			view.setFocusable(true);
			view.setFocusableInTouchMode(true);
			view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
			// Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@ " + m.get("item_type"));
			this.addView(view);
		}
	}

	private int parseItemBackground(int num, int flag) {
		// if (flag == FLAG_HOME) {
		switch (num % 5) {
			case 0:
				return R.drawable.item_child_5;
			case 1:
				return R.drawable.item_child_1;
			case 2:
				return R.drawable.item_child_2;
			case 3:
				return R.drawable.item_child_3;
			case 4:
				return R.drawable.item_child_4;
		default:
			return R.drawable.item_child_4;
		}
	}

}
