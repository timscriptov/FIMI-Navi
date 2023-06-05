package com.fimi.kernel.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;

import com.fimi.kernel.FimiAppContext;

import java.io.File;
import java.util.List;
import java.util.Locale;


public class SystemParamUtil {
    public static String getModelName() {
        return Build.MODEL;
    }

    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    public static String getAndroidId(Context ctx) {
        String andoridId = "" + Settings.Secure.getString(ctx.getContentResolver(), "android_id");
        return andoridId;
    }

    public static String getDeviceID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        return Build.MODEL + "," + telephonyManager.getDeviceId();
    }

    public static String getLocalLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getVersionName() {
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return null;
        }
        return info.versionName;
    }

    public static int getVersionCode() {
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return 0;
        }
        return info.versionCode;
    }

    public static PackageInfo getPackageInfo() {
        Context mContext = FimiAppContext.getContext();
        PackageManager manager = mContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPackageName() {
        PackageInfo info = getPackageInfo();
        if (info == null) {
            return null;
        }
        return info.packageName;
    }

    public static boolean isWifiNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            return type.equalsIgnoreCase("WIFI");
        }
        return false;
    }

    public static boolean isSDFreeSize(int maxSize) {
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long freeBlocks = sf.getAvailableBlocks();
        return ((freeBlocks * blockSize) / 1024) / 1024 <= ((long) maxSize);
    }

    public static boolean isTopActivy(String cmdName, Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService("activity");
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;
        if (runningTaskInfos != null) {
            cmpNameTemp = runningTaskInfos.get(0).topActivity.getClassName();
        }
        if (cmpNameTemp == null) {
            return false;
        }
        return cmpNameTemp.equals(cmdName);
    }

    public static String getLocalVersionName(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            String localVersion = packageInfo.versionName;
            return localVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Nullable
    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper wrapper) {
            return findActivity(wrapper.getBaseContext());
        }
        return null;
    }
}
