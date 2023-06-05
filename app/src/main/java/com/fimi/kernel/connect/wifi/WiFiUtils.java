package com.fimi.kernel.connect.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;


public class WiFiUtils {
    public static int netId = -1;
    public static ScanResult scanResult;

    public static boolean connectToAP(ScanResult device, String passkey, Context context) {
        if (device == null) {
            return false;
        }
        scanResult = device;
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        String networkSSID = device.SSID;
        String securityMode = getScanResultSecurity(device.capabilities);
        if (securityMode.equalsIgnoreCase("OPEN")) {
            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
            wifiConfiguration.allowedKeyManagement.set(0);
            netId = mWifiManager.addNetwork(wifiConfiguration);
            if (netId == -1) {
                return false;
            }
        } else if (securityMode.equalsIgnoreCase("WEP")) {
            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
            wifiConfiguration.wepKeys[0] = "\"" + passkey + "\"";
            wifiConfiguration.wepTxKeyIndex = 0;
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.allowedGroupCiphers.set(0);
            netId = mWifiManager.addNetwork(wifiConfiguration);
            if (netId == -1) {
                return false;
            }
        } else {
            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
            wifiConfiguration.preSharedKey = "\"" + passkey + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.status = 2;
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedKeyManagement.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(2);
            wifiConfiguration.allowedProtocols.set(1);
            wifiConfiguration.allowedProtocols.set(0);
            netId = mWifiManager.addNetwork(wifiConfiguration);
            if (netId == -1) {
                return false;
            }
        }
        return mWifiManager.enableNetwork(netId, true);
    }

    public static int isConfiguration(ScanResult scanResult2, Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        List<WifiConfiguration> wifiConfigList = wifiManager.getConfiguredNetworks();
        if (wifiConfigList == null || scanResult2 == null) {
            return -1;
        }
        for (int i = 0; i < wifiConfigList.size(); i++) {
            if (isEqualWifi(wifiConfigList.get(i).SSID, scanResult2.SSID)) {
                netId = wifiConfigList.get(i).networkId;
                return netId;
            }
        }
        return -1;
    }

    public static boolean isEqualWifi(String wifi1, String wifi2) {
        if (wifi1 == null || wifi2 == null) {
            return false;
        }
        if (wifi1.startsWith("\"")) {
            wifi1 = wifi1.substring(1, wifi1.length() - 1);
        }
        if (wifi2.startsWith("\"")) {
            wifi2 = wifi2.substring(1, wifi2.length() - 1);
        }
        return wifi1.equalsIgnoreCase(wifi2);
    }

    public static boolean connectWifi(WifiManager wifiManager, int netId2) {
        if (netId2 == -1) {
            return false;
        }
        List<WifiConfiguration> wifiConfigList = wifiManager.getConfiguredNetworks();
        if (wifiConfigList == null || wifiConfigList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < wifiConfigList.size(); i++) {
            WifiConfiguration wifi = wifiConfigList.get(i);
            if (wifi.networkId == netId2) {
                boolean connectOkay = wifiManager.enableNetwork(netId2, true);
                wifiManager.saveConfiguration();
                return connectOkay;
            }
        }
        return false;
    }

    private static String getScanResultSecurity(String capabilities) {
        String[] securityModes = {"WEP", "PSK", "EAP"};
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (capabilities.contains(securityModes[i])) {
                return securityModes[i];
            }
        }
        return "OPEN";
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }

    public static boolean isPhoneNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo mMobileNetworkInfo = connectivityManager.getNetworkInfo(0);
        if (mMobileNetworkInfo != null) {
            return (mMobileNetworkInfo.getState() == NetworkInfo.State.CONNECTED) & mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    public static boolean netOkay(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo mMobileNetworkInfo = connectivityManager.getNetworkInfo(0);
        NetworkInfo wifiWorkInfo = connectivityManager.getNetworkInfo(1);
        if (mMobileNetworkInfo != null) {
            return ((mMobileNetworkInfo.getState() == NetworkInfo.State.CONNECTED) & mMobileNetworkInfo.isAvailable()) | ((wifiWorkInfo.getState() == NetworkInfo.State.CONNECTED) & wifiWorkInfo.isAvailable());
        }
        return false;
    }

    public static void disConnectCurWifi(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wifiManager.disableNetwork(wifiInfo.getNetworkId());
            wifiManager.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getPhoneIp(Context application) {
        WifiManager wifiManager = (WifiManager) application.getSystemService("wifi");
        if (!wifiManager.isWifiEnabled()) {
            try {
                Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                while (en.hasMoreElements()) {
                    NetworkInterface intf = en.nextElement();
                    Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static void removeNetId(Context context, String ssid) {
        if (context != null && ssid != null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks();
            if (configurationList != null) {
                Iterator<WifiConfiguration> it = configurationList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    WifiConfiguration config = it.next();
                    if (isEqualWifi(config.SSID, ssid)) {
                        wifiManager.removeNetwork(config.networkId);
                        wifiManager.saveConfiguration();
                        break;
                    }
                }
            }
            netId = -1;
        }
    }

    public static void removeNetId(Context context) {
        if (context != null && netId > 0) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            wifiManager.removeNetwork(netId);
            wifiManager.saveConfiguration();
            netId = -1;
        }
    }
}
