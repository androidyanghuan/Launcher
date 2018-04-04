package cn.sn.zwcx.mvvm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by on 2018/3/14 10:12.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    private NetworkUtil(){
        throw new UnsupportedOperationException("You can't instance " + NetworkUtil.class.getSimpleName());
    }

    private static final int NETWORK_TYPE_GSM      = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN    = 18;

    public enum NetworkType{
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO,
        NETWORK_ETHERNET
    }

    /**
     * 打开无线设置界面
     * @param context
     */
    public static void openWirelessSetting(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        else
            context.startActivity(new Intent(Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 获取活动的网络信息
     * @param context
     * @return
     */
    public static NetworkInfo getActiveNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 判断网络是否可用
     * @return
     */
    public static boolean isAvailableByPing(){
        ShellUtil.CommandResult result = ShellUtil.execCommand("ping -c 1 -w 1 223.5.5.5",false);
        boolean ret = result.result == 0;
        if (!TextUtils.isEmpty(result.errorMsg))
          //  Log.e(TAG,"is available by ping error message: " + result.errorMsg);
            ret = false;
        if (!TextUtils.isEmpty(result.successMsg))
            ret = true;
         //   Log.e(TAG,"is available by ping success message: " + result.successMsg);
        return ret;
    }

    /**
     * 判断移动数据是否打开
     * @param context
     * @return
     */
    public static boolean getDataEnabled(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.isDataEnabled();
        } else {
            try {
                Method dataEnabled = tm.getClass().getDeclaredMethod("getDataEnabled");
                if (null != dataEnabled)
                    return (boolean) dataEnabled.invoke(tm);
            } catch (Exception e) {
                e.printStackTrace();
            }
              return false;
        }
    }

    /**
     * 打开或关闭移动数据
     * @param context
     * @param enabled
     */
    public static void setDataEnabled(Context context, boolean enabled){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method setDataEnabled = tm.getClass().getDeclaredMethod("setDataEnabled",boolean.class);
            if (null != setDataEnabled)
                setDataEnabled.invoke(tm,enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前网络是否是4G
     * @param context
     * @return
     */
    public static boolean is4G(Context context){
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否打开
     * @param context
     * @return
     */
    public static boolean getWifiEnabled(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.isWifiEnabled();
    }

    /**
     * 打开或关闭wifi
     * @param context
     * @param enabled
     */
    public static void setWifiEnabled(Context context, boolean enabled){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (enabled) {
            if (!wm.isWifiEnabled())
                wm.setWifiEnabled(true);
        } else {
            if (wm.isWifiEnabled())
                wm.setWifiEnabled(false);
        }
    }

    /**
     * 判断wifi是否连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断wifi连接是否可用
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context){
        return getWifiEnabled(context) && isAvailableByPing();
    }

    /**
     * 获取网络运营商的名称
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : "Unknown";
    }

    /**
     * 获取当前网络类型
     * @param context
     * @return
     */
    public static NetworkType getNetworkType(Context context){
        NetworkType networkType = NetworkType.NETWORK_NO;
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                networkType = NetworkType.NETWORK_WIFI;
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (networkInfo.getSubtype()) {
                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        networkType = NetworkType.NETWORK_2G;
                        break;
                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        networkType = NetworkType.NETWORK_3G;
                        break;
                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        networkType = NetworkType.NETWORK_4G;
                        break;
                        default:
                            String name = networkInfo.getSubtypeName();
                            if (name.equalsIgnoreCase("TD-SCDMA")
                                    || name.equalsIgnoreCase("WCDMA")
                                    || name.equalsIgnoreCase("CDMA2000"))
                                networkType = NetworkType.NETWORK_3G;
                            else
                                networkType = NetworkType.NETWORK_UNKNOWN;
                            break;
                }
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)
                networkType = NetworkType.NETWORK_ETHERNET;
            else
                networkType = NetworkType.NETWORK_UNKNOWN;
        }
        return networkType;
    }

    /**
     * 获取ip地址
     * @param userIpv4
     * @return
     */
    public static String getIpAddress(boolean userIpv4){
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();nis.hasMoreElements();) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> address = ni.getInetAddresses();address.hasMoreElements();) {
                    InetAddress ia = address.nextElement();
                    if (!ia.isLoopbackAddress()) {
                        String hostAddress = ia.getHostAddress();
                        boolean isIpv4 = hostAddress.indexOf(':') < 0;
                        if (userIpv4) {
                            if (isIpv4)
                                return hostAddress;
                        } else {
                            if (!isIpv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0,index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取域名地址
     * @param domain
     * @return
     */
    public static String getDomainAddress(final String domain) {
        try {
            ExecutorService es = Executors.newCachedThreadPool();
            Future<String> fs = es.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    InetAddress inetAddress;
                    try {
                        inetAddress = InetAddress.getByName(domain);
                        return inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            return fs.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
