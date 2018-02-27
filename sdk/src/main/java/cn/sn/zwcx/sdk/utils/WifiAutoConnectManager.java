package cn.sn.zwcx.sdk.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.List;

import cn.sn.zwcx.sdk.R;

/**
 * Created by on 2018/1/6 11:42.
 * Created by author YangHuan
 * e-mail:435025168@qq.com
 */

public class WifiAutoConnectManager {
    private final String TAG = WifiAutoConnectManager.class.getSimpleName();

    /** WifiManager对象 */
    private WifiManager wifiManager;

    /**
     * 定义几种加密方式
     * WPA
     * WEP
     * none
     * invalid
     */
    enum WifiCipherType{
        WIFI_CIPHER_WPA,
        WIFI_CIPHER_WEP,
        WIFI_CIPHER_NONE,
        WIFI_CIPHER_INVALID
    }

    public WifiAutoConnectManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    /**
     * 连接指定的wifi热点
     * @param ssid
     * @param password
     * @param type
     */
    public void connect(String ssid, String password,WifiCipherType type){
        new Thread(new ConnectRunnable(ssid,password,type)).start();
    }

    class ConnectRunnable implements Runnable{
        private String ssid,password;
        private WifiCipherType type;

        public ConnectRunnable(String ssid, String password, WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        public void run() {
            openWifi();
            // wifi开启需要一段时间，所以要等到wifi状态改变成WIFI_STATE_ENABLED,才能执行下面的语句
            while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            WifiConfiguration tempConfig = isExists(ssid);
            if (tempConfig != null)
                wifiManager.enableNetwork(tempConfig.networkId, true);
            else {
                WifiConfiguration wifiConfig = createWifiInfo(ssid,password,type);
                if (wifiConfig == null) return;
                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.enableNetwork(netId,true);
                wifiManager.reconnect();
            }
        }
    }

    /**
     * 创建wifi信息
     * @param ssid
     * @param password
     * @param type
     * @return
     */
    private WifiConfiguration createWifiInfo(String ssid, String password, WifiCipherType type) {
        WifiConfiguration wc = new WifiConfiguration();
        wc.allowedAuthAlgorithms.clear();
        wc.allowedGroupCiphers.clear();
        wc.allowedKeyManagement.clear();
        wc.allowedPairwiseCiphers.clear();
        wc.allowedProtocols.clear();
        wc.SSID = "\"" + ssid + "\"";
        switch (type){
            case WIFI_CIPHER_NONE:
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
            case WIFI_CIPHER_WEP:
                if (!TextUtils.isEmpty(password)){
                    if (isHexWepKey(password))
                        wc.wepKeys[0] = password;
                    else
                        wc.wepKeys[0] = "\"" + password + "\"";
                }
                wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wc.wepTxKeyIndex = 0;
                break;
            case WIFI_CIPHER_WPA:
                wc.preSharedKey = "\"" + password + "\"";
                wc.hiddenSSID = true;
                wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wc.status = WifiConfiguration.Status.ENABLED;
                break;
            case WIFI_CIPHER_INVALID:
                ToastUtil.showToast(R.string.network_invalid);
                break;
        }
        return wc;
    }

    /** 是否是16进制WepKey */
    private boolean isHexWepKey(String password) {
        int len = password.length();
        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58)
            return false;
        return isHex(password);
    }

    /** 是否是16进制字符串 */
    private boolean isHex(String password) {
        int len = password.length();
        for (int i = len - 1; i >= 0; i--){
            char c = password.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f'))
                return false;
        }
        return true;
    }

    /**
     * 查看是否配置过指定网络
     * @param ssid
     * @return
     */
    private WifiConfiguration isExists(String ssid) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration wc : configuredNetworks){
            if (wc.SSID.equals("\"" + ssid + "\""))
                return wc;
        }
        return null;
    }

    /** 打开wifi */
    private boolean openWifi() {
        boolean isOpen = true;
        if (!wifiManager.isWifiEnabled())
            isOpen = wifiManager.setWifiEnabled(true);
        return isOpen;
    }

    /** 关闭wifi */
    public void closeWifi(){
        if (wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);
    }

    /**
     * 获取ssid的加密方式
     * @param context
     * @param ssid
     * @return
     */
    public static WifiCipherType getCipherType(Context context,String ssid){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanResults = wm.getScanResults();
        for (ScanResult result : scanResults){
            String capabilities = result.capabilities;
            if (!TextUtils.isEmpty(capabilities)){
                if (capabilities.contains("wpa") || capabilities.contains("WPA"))
                    return WifiCipherType.WIFI_CIPHER_WPA;
                else if (capabilities.contains("wep") || capabilities.contains("WEP"))
                    return WifiCipherType.WIFI_CIPHER_WEP;
                else
                    return WifiCipherType.WIFI_CIPHER_NONE;
            }
        }
        return WifiCipherType.WIFI_CIPHER_INVALID;
    }

}
