
package cn.sn.zwcx.entertainment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;


public class MyRelativeLayout extends RelativeLayout {
    private final String TAG = "Yang Huan " + MyRelativeLayout.class.getSimpleName();

    private Context mContext = null;
    private static Rect imgRect;
    private float scalePara = 1.0f;
    private float shortcutScalePara = 1.1f;
    private float framePara = 1.09f;
    private int animDuration;
    private int animDelay = 0;
    private final int MODE_HOME_RECT = 0;
    private final int MODE_HOME_SHORTCUT = 1;
    private final int MODE_CHILD_SHORTCUT = 2;
    private final int MODE_CHILD_TIMEDATE = 3;
    private final int MODE_CHILD_DATE = 4;
    private final int MODE_CHILD_SHORTCUT_FILE = 5;
    private final int MODE_CHILD_SHORTCUT_MUSIC = 6;
    private final int MODE_CHILD_SHORTCUT_WEATHER = 7;

    private boolean is24hFormart = false;

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (Launcher.isRealOutputMode)
            animDuration = 70;
        else
            animDuration = 90;
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setAddShortcutHead();
        setNumberOfScreen();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Launcher.startX = -1f;

            setSurface();

            if (this.getChildAt(0) instanceof ImageView) {
                ImageView img = (ImageView) this.getChildAt(0);
                if (img != null && img.getDrawable() != null &&
                        img.getDrawable().getConstantState().equals(mContext.getResources().getDrawable(R.drawable.item_img_add).getConstantState())) {
                    Launcher.isAddButtonBeTouched = true;
                    Launcher.pressedAddButton = this;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }
        return true;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        setAddShortcutHead();

        if (gainFocus == true && !Launcher.isInTouchMode && !Launcher.dontDrawFocus) {
            setNumberOfScreen();
            if (Launcher.prevFocusedView != null && (isParentSame(this, Launcher.prevFocusedView)
                    || Launcher.isShowHomePage)) {
                if (!Launcher.dontRunAnim && !Launcher.IntoCustomActivity) {
               //     Launcher.frameView.setVisibility(View.INVISIBLE);
                    Launcher.layoutScaleShadow.setVisibility(View.INVISIBLE);

                    Rect preRect = new Rect();
                    Launcher.prevFocusedView.getGlobalVisibleRect(preRect);

                    setShadowEffect();
                    startFrameAnim(preRect);
                } else if (!(Launcher.IntoCustomActivity && Launcher.isShowHomePage && Launcher.ifChangedShortcut)) {
                    Launcher.dontRunAnim = false;
                    setSurface();
                }

            } else {
                if (Launcher.isShowHomePage || Launcher.dontRunAnim || Launcher.IntoCustomActivity) {
                    Launcher.IntoCustomActivity = false;
                    setSurface();
                }
            }
        } else if (!Launcher.isInTouchMode) {
            Launcher.prevFocusedView = this;
            if (!Launcher.dontRunAnim) {
                ScaleAnimation anim = new ScaleAnimation(1.1f, 1f, 1.1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setZAdjustment(Animation.ZORDER_TOP);
                anim.setDuration(animDuration);
                anim.setStartTime(animDelay);
                if (!(this.getParent() instanceof MyGridLayout)) {
                    this.bringToFront();
                    ((View) this.getParent()).bringToFront();
                    Launcher.viewHomePage.bringToFront();
                }
                this.startAnimation(anim);
            }
        }
    }

    private void setAddShortcutHead() {
        View parent = (View) this.getParent();
        if (parent == Launcher.videoShortcutView) {
            Launcher.current_shortcutHead = CustomAppsActivity.VIDEO_SHORTCUT_HEAD;
        } else if (parent == Launcher.recommendShortcutView) {
            Launcher.current_shortcutHead = CustomAppsActivity.RECOMMEND_SHORTCUT_HEAD;
        } else if (parent == Launcher.musicShortcutView) {
            Launcher.current_shortcutHead = CustomAppsActivity.MUSIC_SHORTCUT_HEAD;
        } else if (parent == Launcher.localShortcutView) {
            Launcher.current_shortcutHead = CustomAppsActivity.LOCAL_SHORTCUT_HEAD;
        } else {
            Launcher.current_shortcutHead = CustomAppsActivity.HOME_SHORTCUT_HEAD;
        }
    }


    private void setNumberOfScreen() {
        if (this.getParent() instanceof MyGridLayout) {
            MyGridLayout parent = (MyGridLayout) this.getParent();
            if (parent == Launcher.videoShortcutView) {
                Launcher.tx_video_count.setText(Integer.toString(Launcher.videoShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.recommendShortcutView) {
                Launcher.tx_recommend_count.setText(Integer.toString(Launcher.recommendShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.appShortcutView) {
                Launcher.tx_app_count.setText(Integer.toString(Launcher.appShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.musicShortcutView) {
                Launcher.tx_music_count.setText(Integer.toString(Launcher.musicShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.localShortcutView) {
                Launcher.tx_local_count.setText(Integer.toString(Launcher.localShortcutView.indexOfChild(this) + 1));
            }
        }
    }

    public class TransAnimationListener implements AnimationListener {
        private Animation scaleAnim;
        private ViewGroup mView;

        public TransAnimationListener(Context context, ViewGroup view, Animation anim) {
            scaleAnim = anim;
            mView = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            scaleAnim.reset();
            if (!Launcher.animIsRun) {
                Launcher.layoutScaleShadow.setVisibility(View.VISIBLE);
             //   Launcher.frameView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    public class ScaleAnimationListener implements AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (!Launcher.animIsRun) {
                Launcher.layoutScaleShadow.setVisibility(View.VISIBLE);
             //   Launcher.frameView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    private void startFrameAnim(Rect preRect) {
        imgRect = new Rect();
        this.getGlobalVisibleRect(imgRect);
        setTransFramePosition(preRect);
        ScaleAnimation shadowAnim = new ScaleAnimation(0.9f, 1f, 0.9f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shadowAnim.setDuration(animDuration);
        shadowAnim.setStartTime(animDelay);
        shadowAnim.setAnimationListener(new ScaleAnimationListener());
        Launcher.layoutScaleShadow.startAnimation(shadowAnim);
    }

    public void setSurface() {
        setShadowEffect();
        if (!Launcher.animIsRun) {
         //   Launcher.frameView.setVisibility(View.VISIBLE);
            Launcher.layoutScaleShadow.setVisibility(View.VISIBLE);
        }
    }

    public void setShadowEffect() {
        float bgScalePara;
        Rect layoutRect;
        Bitmap scaleBitmap;
        Bitmap shadowBitmap;
        ViewGroup mView = this;
        ImageView scaleImage;
        TextView scaleText;
        TextView scaleText_date;
        int screen_mode;
        String text = null;
        String range = "";

    //    Launcher.trans_frameView.bringToFront();
        Launcher.layoutScaleShadow.bringToFront();
    //    Launcher.frameView.bringToFront();

        screen_mode = getScreenMode(mView);

        imgRect = new Rect();
        mView.getGlobalVisibleRect(imgRect);
        setFramePosition(imgRect);

        scaleImage = Launcher.layoutScaleShadow.findViewById(R.id.img_focus_unit);
        scaleText_date = Launcher.layoutScaleShadow.findViewById(R.id.tx_focus_unit_date);
        scaleText = Launcher.layoutScaleShadow.findViewById(R.id.tx_focus_unit);

        if (screen_mode == MODE_HOME_SHORTCUT) {
            bgScalePara = shortcutScalePara;
        } else if (screen_mode == MODE_CHILD_SHORTCUT){
            bgScalePara = 1.084f;
        }else {
            bgScalePara = scalePara;
        }

        ImageView img = (ImageView) (mView.getChildAt(0));
        img.buildDrawingCache();
        Bitmap bmp = img.getDrawingCache();
        if (bmp == null) {
            Launcher.cantGetDrawingCache = true;
            return;
        } else {
            Launcher.cantGetDrawingCache = false;
        }

        scaleBitmap = zoomBitmap(bmp, (int) (imgRect.width() * bgScalePara), (int) (imgRect.height() * bgScalePara));
        img.destroyDrawingCache();

        if (mView.getChildAt(1) instanceof TextView) {
            text = ((TextView) mView.getChildAt(1)).getText().toString();
        }
        if (mView.getChildAt(2) instanceof TextView) {
            range = ((TextView) mView.getChildAt(2)).getText().toString();
        }

        shadowBitmap = BitmapFactory.decodeResource(mContext.getResources(), getShadow(mView.getChildAt(0), screen_mode));
        int layout_width = (shadowBitmap.getWidth() - imgRect.width()) / 2;
        int layout_height = (shadowBitmap.getHeight() - imgRect.height()) / 2;
        layoutRect = new Rect(imgRect.left - layout_width, imgRect.top - layout_height, imgRect.right + layout_width, imgRect.bottom + layout_height);

        scaleImage.setImageBitmap(scaleBitmap);

        if ((text != null) && (screen_mode != MODE_CHILD_TIMEDATE) && screen_mode != MODE_CHILD_SHORTCUT_WEATHER) {
            scaleText_date.setVisibility(View.INVISIBLE);
            setTextMarginAndSize(scaleText, screen_mode);
            scaleText.setText(text);
        } else if ((text != null) && (screen_mode == MODE_CHILD_TIMEDATE)) {
            //	TextView time = findViewById(R.id.tx_time);
            //	TextView date = findViewById(R.id.tx_date);
            String time_text = getTime();
            String date_text = getDate();

            setTextMarginAndSize(scaleText, screen_mode);
            scaleText.setText(time_text);
            screen_mode = MODE_CHILD_DATE;
            scaleText_date.setVisibility(View.VISIBLE);
            setTextMarginAndSize(scaleText_date, screen_mode);
            scaleText_date.setText(date_text);

        } else if (!TextUtils.isEmpty(text) && screen_mode == MODE_CHILD_SHORTCUT_WEATHER) {
            setTextMarginAndSize(scaleText, scaleText_date);
            scaleText.setText(text);
            scaleText_date.setText(range);
            scaleText_date.setVisibility(VISIBLE);

        } else {
            scaleText.setText(null);
        }
        Launcher.layoutScaleShadow.setBackgroundResource(getShadow(mView.getChildAt(0), screen_mode));
        setViewPosition(Launcher.layoutScaleShadow, layoutRect);
    }

    private void setTextMarginAndSize(TextView scaleText, TextView scaleText_date) {
        LayoutParams para = new LayoutParams(180, LayoutParams.WRAP_CONTENT);
        para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        para.setMargins(415, 0, 0, 250);
        scaleText.setLayoutParams(para);
        scaleText.setTextSize(30);
        scaleText.setFocusable(true);
        scaleText.setFocusableInTouchMode(true);


        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.setMargins(415, 0, 0, 160);
        scaleText_date.setLayoutParams(lp);
        scaleText_date.setTextSize(28);


    }

    private void setTextMarginAndSize(TextView text, int screen_mode) {
        LayoutParams para;
        para = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        para.addRule(RelativeLayout.CENTER_HORIZONTAL);
        if (screen_mode == MODE_HOME_RECT) {
            if (Launcher.REAL_OUTPUT_MODE.equals("4k2knative")) {
                para.setMargins(0, 0, 0, 284);
            } else if (Launcher.REAL_OUTPUT_MODE.equals("720p")) {
                para.setMargins(0, 0, 0, 135);

            } else
                para.setMargins(0, 0, 0, 180);
            text.setLayoutParams(para);
            text.setTextSize(25);
        } else if (screen_mode == MODE_CHILD_TIMEDATE) {
            LayoutParams para_time;
            para_time = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            para_time.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //    TextView time = findViewById(R.id.tx_time);
            TextView text_time = text;
            if (Launcher.REAL_OUTPUT_MODE.equals("4k2knative")) {
                para_time.setMargins(260, 0, 0, 284);
            } else if (Launcher.REAL_OUTPUT_MODE.equals("720p")) {
                para_time.setMargins(240, 0, 0, 130);
            } else
                para_time.setMargins(260, 0, 0, 190);
            text.setLayoutParams(para_time);
            text.setTextSize(36);
        } else if (screen_mode == MODE_CHILD_DATE) {
            LayoutParams para_date;
            para_date = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            para_date.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            TextView text_date = null;
            if (Launcher.REAL_OUTPUT_MODE.equals("4k2knative")) {
                para_date.setMargins(270, 0, 0, 284);
                text.setTextSize(17);
            } else if (Launcher.REAL_OUTPUT_MODE.equals("720p")) {
                para_date.setMargins(400, 0, 0, 145);
                text.setTextSize(20);
            } else {
                para_date.setMargins(420, 0, 0, 180);
            }
            text.setLayoutParams(para_date);
            text.setTextSize(17);
        } else if (screen_mode == MODE_CHILD_SHORTCUT_FILE) {
            para.setMargins(140, 0, 0, 100);
            text.setLayoutParams(para);
            text.setTextSize(25);
        } else {
            if (Launcher.REAL_OUTPUT_MODE.equals("4k2knative")) {
                para.setMargins(150, 0, 150, 154);
            } else if (Launcher.REAL_OUTPUT_MODE.equals("720p"))
                para.setMargins(140, 0, 40, 55);
            else
                para.setMargins(165, 0, 65, 107);
            text.setLayoutParams(para);
            text.setTextSize(25);
        }
    }

    public String getTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        is24hFormart = DateFormat.is24HourFormat(mContext);
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

    private void setTransFramePosition(Rect rect) {
        android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
        int rectWidth = rect.right - rect.left;
        int rectHeight = rect.bottom - rect.top;

        lp.width = (int) (rectWidth * framePara);
        lp.height = (int) (rectHeight * framePara);
        lp.x = rect.left + (int) ((rectWidth - lp.width) / 2);
        lp.y = rect.top + (int) ((rectHeight - lp.height) / 2);
    //    Launcher.trans_frameView.setLayoutParams(lp);
    }

    private void setFramePosition(Rect rect) {
        android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
        int rectWidth = rect.right - rect.left;
        int rectHeight = rect.bottom - rect.top;

        lp.width = (int) (rectWidth * framePara);
        lp.height = (int) (rectHeight * framePara);
        lp.x = rect.left + (int) ((rectWidth - lp.width) / 2);
        lp.y = rect.top + (int) ((rectHeight - lp.height) / 2);
     //   Launcher.frameView.setLayoutParams(lp);
    }

    private void setViewPosition(View view, Rect rect) {
        android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);

        lp.width = rect.width();
        lp.height = rect.height();
        lp.x = rect.left;
        lp.y = rect.top;
        view.setLayoutParams(lp);
    }

    private int getScreenMode(ViewGroup view) {
        View img = view.getChildAt(0);
        View tx = view.getChildAt(1);
        String path = img.getResources().getResourceName(img.getId());
        String vName = path.substring(path.indexOf("/") + 1);

        if (vName.equals("img_entertainment") || vName.equals("img_video") || vName.equals("img_setting")
                || vName.equals("img_apps") || vName.equals("img_speed_test") || vName.equals("img_local")) {
            return MODE_HOME_RECT;
        } else if (vName.equals("img_time_t")) {
            return MODE_CHILD_TIMEDATE;
        } else if (vName.equals("img_video_v")) {
            return MODE_CHILD_SHORTCUT_WEATHER;
        } else if (tx != null) {
            if (Launcher.current_shortcutHead.equals(CustomAppsActivity.VIDEO_SHORTCUT_HEAD)) {
                framePara = 1.10f;
                return MODE_CHILD_SHORTCUT_FILE;
            } else if (Launcher.current_shortcutHead.equals(CustomAppsActivity.MUSIC_SHORTCUT_HEAD) ||
                    Launcher.current_shortcutHead.equals(CustomAppsActivity.RECOMMEND_SHORTCUT_HEAD)) {
                framePara = 1.10f;
                return MODE_CHILD_SHORTCUT_MUSIC;
            } else {
                framePara = 1.06f;
                return MODE_CHILD_SHORTCUT;
            }
        } else {
            return MODE_HOME_SHORTCUT;
        }
    }

    private boolean isParentSame(View view1, View view2) {
        if (((ViewGroup) view1.getParent()).indexOfChild(view2) == -1) {
            return false;
        } else {
            return true;
        }
    }

    private int getShadow(View img, int mode) {
        String path = img.getResources().getResourceName(img.getId());
        String vName = path.substring(path.indexOf("/") + 1);
        if (vName.equals("img_video")) {
            return R.drawable.focus_video;
        } else if (vName.equals("img_setting")) {
            return R.drawable.focus_setting;
        } else if (vName.equals("img_apps")) {
            return R.drawable.focus_apps;
        } else if (vName.equals("img_local")) {
            return R.drawable.focus_local;
        } else if (vName.equals("img_entertainment")) {
            return R.drawable.focus_entertainment;
        } else if (vName.equals("img_speed_test")) {
            return R.drawable.focus_speed_test;
        } else if (mode == MODE_CHILD_SHORTCUT) {
            return R.drawable.shadow_child_shortcut;
        } else if (mode == MODE_CHILD_SHORTCUT_FILE) {
            return R.drawable.shadow_vedio_f;
        } else if (mode == MODE_CHILD_SHORTCUT_MUSIC) {
            return R.drawable.shadow_music_child_shortcut;
        } else {
            return R.drawable.shadow_shortcut;
        }
    }

    public int getStringLength(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;

        }
        return length;

    }

    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }
}
