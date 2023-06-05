package com.fimi.kernel.permission;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fimi.kernel.base.BaseAppManager;
import com.github.moduth.blockcanary.internal.BlockInfo;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PermissionManager {
    public static final int ACTION_LOCATION_SOURCE_SETTINGS = 1314;
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 2;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 3;
    public static final int REQUEST_CAMERA = 7;
    public static final int REQUEST_RECORD_AUDIO = 8;
    public static final int REQUEST_X9_PERMISSIONS = 9;
    private static final int REQUEST_ACCESS_EXTERNAL_STORAGE = 4;
    private static final int REQUEST_BLUETOOTH = 5;
    private static final int REQUEST_BLUETOOTH_ADMIN = 6;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] permissionsArray = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION"};
    public static String[] PERMISSIONS_COARSE_LOCATION = {"android.permission.ACCESS_COARSE_LOCATION"};
    public static String[] PERMISSIONS_EXTERNAL_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static String[] PERMISSIONS_CAMERA = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO"};
    public static String[] PERMISSIONS_RECORD_AUDIO = {"android.permission.RECORD_AUDIO"};
    private static String[] PERMISSIONS_INTERNET = {"android.permission.INTERNET", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    private static List<String> permissionsList = new ArrayList();

    public static void requestStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    public static void requestCoarseLocationPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_COARSE_LOCATION");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 2);
        }
    }

    public static boolean hasLocaltionPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_COARSE_LOCATION");
        if (permission == 0) {
            return true;
        }
        return false;
    }

    public static boolean shouldShowLocaltionPermissions() {
        return ActivityCompat.shouldShowRequestPermissionRationale(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_COARSE_LOCATION");
    }

    public static void requestWritePermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
        }
    }

    public static void requestFind_LocationPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.ACCESS_FINE_LOCATION");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 3);
        }
    }

    public static void requestNetPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.INTERNET");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), PERMISSIONS_INTERNET, 1);
        }
    }

    public static void requestBluetoothPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.BLUETOOTH");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.BLUETOOTH"}, 5);
        }
    }

    public static void requestBluetoothAdminPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.BLUETOOTH_ADMIN");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.BLUETOOTH_ADMIN"}, 6);
        }
    }

    public static void requestCameraPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.CAMERA");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.CAMERA"}, 7);
        }
    }

    public static void requestRecordAudioPermissions() {
        int permission = ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), "android.permission.RECORD_AUDIO");
        if (permission != 0) {
            ActivityCompat.requestPermissions(BaseAppManager.getInstance().getForwardActivity(), new String[]{"android.permission.RECORD_AUDIO"}, 8);
        }
    }

    public static boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private static boolean lacksPermission(String permission) {
        return ActivityCompat.checkSelfPermission(BaseAppManager.getInstance().getForwardActivity(), permission) == -1;
    }

    public static final boolean isLocationEnable(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            boolean networkProvider = locationManager.isProviderEnabled(BlockInfo.KEY_NETWORK);
            boolean gpsProvider = locationManager.isProviderEnabled("gps");
            return networkProvider || gpsProvider;
        }
        return true;
    }

    public static boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                int locationMode = Settings.Secure.getInt(context.getContentResolver(), "location_mode");
                return locationMode != 0;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        String locationProviders = Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed");
        return !TextUtils.isEmpty(locationProviders);
    }

    public static void checkRequiredPermission(Activity activity) {
        String[] strArr;
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != 0) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, (String[]) permissionsList.toArray(new String[permissionsList.size()]), 9);
        }
    }
}
