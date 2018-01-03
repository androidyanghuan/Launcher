package cn.sn.zwcx.sound;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    class MySoundPool{
        private SoundPool soundPool=null;
        private int id_short=0;
        private int id_long=0;
        private int id_loop=0;
        private int id_speach=0;
        public MySoundPool(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SoundPool.Builder sb = new SoundPool.Builder();
                sb.setMaxStreams(30);
                sb.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build()
                );
                soundPool = sb.build();
            } else {
                soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
            }
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(android.media.SoundPool soundPool, int sampleId, int status) {
                    Log.e("XXX onLoadComplete", "sampleId: "+sampleId + " status:"+status);
                }
            });
            id_short=soundPool.load(MainActivity.this, R.raw.sfx_short, 1);
            id_speach=soundPool.load(MainActivity.this, R.raw.sfx_speach, 1);
            id_long=soundPool.load(MainActivity.this, R.raw.sfx_long, 1);
            id_loop=soundPool.load(MainActivity.this, R.raw.sfx_loop, 1);
        }
        public void playSFXShort(){
            soundPool.play(id_short,0.2f,0.2f, 0, 0, 1);
        }
        public void playSFXLong(){
            soundPool.play(id_long,0.2f,0.2f, 0, 0, 1);
        }
        public void playSFXLoop(){
            soundPool.play(id_loop,0.2f,0.2f, 1, -1, 1);
        }
        public void playSpeachShort(){
            soundPool.play(id_speach,0.2f,0.2f, 0, 0, 1);
        }

        public void stop(){
            if (soundPool != null) soundPool.release();
        }
    }

    MySoundPool mSoundPool=null;
    Timer mTimer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSoundPool = new MySoundPool();


        //Button1:高频播放音效(DING)
        final Button testButton1 = findViewById(R.id.Button1);
        if(testButton1!=null){
            testButton1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mSoundPool.playSFXShort();
                        }
                    }, 300, 300);
                    testButton1.setEnabled(false);
                }
            });
        }

        //Button2:放循环音效(BGM)
        final Button testButton2 = findViewById(R.id.Button2);
        if(testButton2!=null){
            testButton2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    mSoundPool.playSFXLoop();
                    testButton2.setEnabled(false);
                }
            });
        }

        //Button3:
        final Button testButton3 = findViewById(R.id.Button3);
        if(testButton3!=null){
            testButton3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    mSoundPool.playSpeachShort();
                //    diyBackgroundAndFontToast(R.string.app_name);
                }
            });
        }

        //Button4:退出
        final Button testButton4 = findViewById(R.id.Button4);
        if(testButton4!=null){
            testButton4.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });
        }

    }

    /** 自定义背景和字体的长时Toast */
    public void diyBackgroundAndFontToast(int resource){
        View view = View.inflate(this, R.layout.toast_layout,null);
        TextView txt = (TextView) view.findViewById(R.id.toast_tv);
        txt.setText(resource);
        Toast toast = Toast.makeText(this,null,Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) mTimer.cancel();
        if (mSoundPool != null) mSoundPool.stop();
    }

//	private void toast(String str) {
//		Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
//	}
}
