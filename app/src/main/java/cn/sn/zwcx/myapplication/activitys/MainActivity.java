package cn.sn.zwcx.myapplication.activitys;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.sn.zwcx.myapplication.R;
import cn.sn.zwcx.myapplication.receivers.LauncherReceiver;
import cn.sn.zwcx.myapplication.servers.LogoServer;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    private Button mCancel;

    private LauncherReceiver lr = new LauncherReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
      //  startService(new Intent(this, LogoServer.class));
        registerReceiver(lr,new IntentFilter("123456"));

        sendBroadcast(new Intent("123456"));
    }

    /** 初始化视图 */
    private void initViews() {
        mCancel = findViewById(R.id.main_btn);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"*********关闭该界面*************");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(lr);
    }
}
