package cn.sn.zwcx.myapplication.servers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created on 2017/12/9 15:12
 * Created by author yanghuan.
 * e-mail : 435025168@qq.com
 */

public class LogoServer extends Service {
private final String TAG = LogoServer.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        String mac = getMac();
      //  String replace = mac.replace(':', '');
        String replace = mac.replace(":", "");
        long l = Long.parseLong(replace,16);
        long l1 = Long.parseLong("78C2C099EB90", 16);
        long l2 = Long.parseLong("78C2C099EC80", 16);
        Log.e(TAG,"long:" + (l1 - l));
        if (l2 >= l && l2 <= l1){
            Log.e(TAG,"***************在区间之内***************");
        }else Log.e(TAG,"==============超出区间==============");
        Log.e(TAG,"==================Server onCreate===================" + mac);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String getMac() {
        String macSerial = null;
        String str = "";
        try {
            Process p = Runtime.getRuntime().exec(
                    "cat /sys/class/net/eth0/address ");
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim().toUpperCase();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macSerial;
    }
// d35c - 8924
}
