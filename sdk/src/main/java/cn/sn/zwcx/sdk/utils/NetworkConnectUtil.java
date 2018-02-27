package cn.sn.zwcx.sdk.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * Created by on 2018/1/6 10:55.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class NetworkConnectUtil {
    private static final String TAG = NetworkConnectUtil.class.getSimpleName();

    /**
     * 连接指定wifi
     * @param wifiManager
     * @param wifiSSID
     * @return
     */
    public static boolean connectToWifi(WifiManager wifiManager, String wifiSSID){
        Log.d(TAG,"要连接的wifi ========>" + wifiSSID);
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + wifiSSID + "\"";
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        // 小米MIUI7/华为EMUI4.1,需要webkey
        wc.wepKeys[0] = "\"" + "\"";
        int networkId = wifiManager.addNetwork(wc);
        if (networkId != -1){
            wifiManager.enableNetwork(networkId,true);
            Log.d(TAG,"wifi连接成功 success...");
            return true;
        } else {
            Log.d(TAG,"尝试第二次连接 ---------->");
            WifiConfiguration configuration = new WifiConfiguration();
            configuration.SSID = "\"" + wifiSSID + "\"";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            // 小米MIUI8 不能有webkey
            //  configuration.wepKeys[0] = "\"" + "\"";
            int network = wifiManager.addNetwork(configuration);
            if (network != -1){
                Log.e(TAG,"第二次连接成功 success full...........");
                wifiManager.enableNetwork(network,true);
                return true;
            }else
                Log.e(TAG,"第二次连接失败............");
        }
        return false;
    }

    /**
     * 获取连接wifi热点的配置选项和加密类型
     * @param manager
     * @param ssid
     * @param password
     * @return
     */
    public static WifiConfiguration getWifiConfiguration(WifiManager manager, String ssid, String password){
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + ssid + "\"";
        List<ScanResult> scanResults = manager.getScanResults();
        for (ScanResult result : scanResults){
            if (ssid.equals(result.SSID)) {
                String capabilities = result.capabilities;
                Log.d(TAG,"capabilities:" + capabilities);
                if (capabilities.contains("WEP") || capabilities.contains("wep")){
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                    wc.preSharedKey = "\"" + password + "\"";
                } else if (capabilities.contains("WPA") || capabilities.contains("wpa")){
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wc.preSharedKey = "\"" + password + "\"";
                } else
                    wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
        }
        return wc;
    }

    /**
     * 连接温控器连接的wifi热点
     * @param context
     * @param manager
     * @param ssid
     * @param password
     */
    public static void connectWifiSSID(Context context,WifiManager manager,String ssid, String password){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            new WifiAutoConnectManager(manager).connect(ssid,password, WifiAutoConnectManager.getCipherType(context,ssid));
        else {
            int addNetwork = manager.addNetwork(getWifiConfiguration(manager, ssid, password));
            if (addNetwork != -1)
                manager.enableNetwork(addNetwork,true);
        }
    }

    /**
     * 格式化当前连接Router的ssid
     * @param routerSSID
     * @return
     */
    public static String formatRouterSSID(String routerSSID){
        String formatRouter = null;
        if (routerSSID.contains("\""))
            formatRouter = routerSSID.replaceAll("\"","");
        return formatRouter;
    }

    /**
     * 用于确定设备是否连接上指定设备ip地址
     * @param ip
     * @return
     */
    public static boolean pingTest(String ip){
        boolean success = false;
        int state = -1;
        String result = "failed";
        try {
            Process exec = Runtime.getRuntime().exec("ping -c 1 " + ip);
            state = exec.waitFor();
            if (state == 0){
                result = "success";
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isConnected(@NonNull Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == cm)
            return false;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.isConnected()){
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }

    /**
     * 是否有网络
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(@NonNull Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (null != networkInfo)
            return networkInfo.isAvailable();
        return false;
    }

    /**
     * 是否wifi连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(@NonNull Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return true;
        return false;
    }

    /**
     * 是否Ethernet连接
     * @param context
     * @return
     */
    public static boolean isEthernetConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)
            return true;
        return false;
    }

    /**
     * 是否移动数据连接
     * @param context
     * @return
     */
    public static boolean is4GConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            return true;
        return false;
    }

    /**
     * 打开网络设置界面
     * @param activity
     * @param requestCode
     */
    public static void openSetting(Activity activity,int requestCode){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings","com.android.settings.WirelessSettings"));
        intent.setAction(Intent.ACTION_VIEW);
        activity.startActivityForResult(intent,requestCode);
    }

}
