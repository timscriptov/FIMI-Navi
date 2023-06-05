package com.fimi.kernel.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.kernel.animutils.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AbAppUtil {
    public static List<String[]> mProcessList = null;
    private static long lastClickTime;
    private static int MIN_CLICK_DELAY_TIME = 1000;

    public static void installApk(@NonNull Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void uninstallApk(@NonNull Context context, String packageName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(@NonNull Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service == null) {
            return false;
        }
        return context.stopService(intent_service);
    }

    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(pathname -> Pattern.matches("cpu[0-9]", pathname.getName()));
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null || (info = connectivity.getActiveNetworkInfo()) == null || !info.isConnected()) {
                return false;
            }
            return info.getState() == NetworkInfo.State.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isGpsEnabled(@NonNull Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled("gps");
    }

    public static boolean isMobile(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 0;
    }

    public static boolean importDatabase(Context context, String dbName, int rawRes) {
        InputStream is = null;
        FileOutputStream fos = null;
        boolean flag = false;
        try {
            try {
                String dbPath = "/data/data/" + context.getPackageName() + "/databases/" + dbName;
                File dbfile = new File(dbPath);
                if (!dbfile.exists()) {
                    if (!dbfile.getParentFile().exists()) {
                        dbfile.getParentFile().mkdirs();
                    }
                    dbfile.createNewFile();
                    is = context.getResources().openRawResource(rawRes);
                    FileOutputStream fos2 = new FileOutputStream(dbfile);
                    try {
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int count = is.read(buffer);
                            if (count <= 0) {
                                break;
                            }
                            fos2.write(buffer, 0, count);
                        }
                        fos2.flush();
                        fos = fos2;
                    } catch (Exception e) {
                        e = e;
                        fos = fos2;
                        e.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (Exception e2) {
                            }
                        }
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Exception e3) {
                            }
                        }
                        return flag;
                    } catch (Throwable th) {
                        th = th;
                        fos = fos2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (Exception e4) {
                            }
                        }
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Exception e5) {
                            }
                        }
                        throw th;
                    }
                }
                flag = true;
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e6) {
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e7) {
                    }
                }
            } catch (Exception e8) {
            }
        } catch (Throwable th2) {
        }
        return flag;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();
        } else {
            mResources = context.getResources();
        }
        return mResources.getDisplayMetrics();
    }

    public static void showSoftInput(@NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, 2);
    }

    public static void closeSoftInput(@NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 2);
        }
    }

    @Nullable
    public static PackageInfo getPackageInfo(Context context) {
        try {
            String packageName = context.getPackageName();
            return context.getPackageManager().getPackageInfo(packageName, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ApplicationInfo getApplicationInfo(Context context, String processName) {
        if (processName == null) {
            return null;
        }
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ApplicationInfo> appList = packageManager.getInstalledApplications(8192);
        for (ApplicationInfo appInfo : appList) {
            if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
    }

    public static void killProcesses(@NonNull Context context, int pid, String processName) {
        String packageName;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            if (processName.indexOf(":") == -1) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            activityManager.killBackgroundProcesses(packageName);
            Method forceStopPackage = activityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String runCommand(String[] command, String workdirectory) {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            if (workdirectory != null) {
                builder.directory(new File(workdirectory));
            }
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream in = process.getInputStream();
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                String str = new String(buffer);
                result = result + str;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String runScript(String script) {
        try {
            final Process m_process = Runtime.getRuntime().exec(script);
            final StringBuilder sbread = new StringBuilder();
            Thread tout = new Thread(new Runnable() { // from class: com.fimi.kernel.utils.AbAppUtil.2
                @Override // java.lang.Runnable
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_process.getInputStream()), 8192);
                    while (true) {
                        try {
                            try {
                                String ls_1 = bufferedReader.readLine();
                                if (ls_1 != null) {
                                    sbread.append(ls_1).append(IOUtils.LINE_SEPARATOR_UNIX);
                                } else {
                                    try {
                                        bufferedReader.close();
                                        return;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                            } finally {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return;
                        }
                    }
                }
            });
            tout.start();
            final StringBuilder sberr = new StringBuilder();
            Thread terr = new Thread(new Runnable() { // from class: com.fimi.kernel.utils.AbAppUtil.3
                @Override // java.lang.Runnable
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_process.getErrorStream()), 8192);
                    while (true) {
                        try {
                            try {
                                String ls_1 = bufferedReader.readLine();
                                if (ls_1 != null) {
                                    sberr.append(ls_1).append(IOUtils.LINE_SEPARATOR_UNIX);
                                } else {
                                    try {
                                        bufferedReader.close();
                                        return;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                            } finally {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return;
                        }
                    }
                }
            });
            terr.start();
            m_process.waitFor();
            while (tout.isAlive()) {
                Thread.sleep(50L);
            }
            if (terr.isAlive()) {
                terr.interrupt();
            }
            String stdout = sbread.toString();
            String stderr = sberr.toString();
            String sRet = stdout + stderr;
            return sRet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getRootPermission(Context context) {
        String cmd = null;
        DataOutputStream os = null;
        String packageCodePath = context.getPackageCodePath();
        Process process = null;
        DataOutputStream os2 = null;
        try {
            cmd = "chmod 777 " + packageCodePath;
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
        } catch (Exception e) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            os.writeBytes(cmd + IOUtils.LINE_SEPARATOR_UNIX);
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            process.destroy();
            return true;
        } catch (Exception e3) {
            os2 = os;
            if (os2 != null) {
                try {
                    os2.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return false;
                }
            }
            process.destroy();
            return false;
        } catch (Throwable th2) {
            os2 = os;
            if (os2 != null) {
                try {
                    os2.close();
                } catch (Exception e5) {
                    e5.printStackTrace();
                    throw th2;
                }
            }
            process.destroy();
            throw th2;
        }
    }

    public static List<String[]> getProcessRunningInfo() {
        try {
            String result = runCommandTopN1();
            List<String[]> processList = parseProcessRunningInfo(result);
            return processList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String runCommandTopN1() {
        try {
            String[] args = {"/system/bin/top", "-n", "1"};
            String result = runCommand(args, "/system/bin/");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String[]> parseProcessRunningInfo(String info) {
        List<String[]> processList = new ArrayList<>();
        boolean bIsProcInfo = false;
        String[] rows = info.split("[\n]+");
        for (String tempString : rows) {
            if (tempString.indexOf("PID") == -1) {
                if (bIsProcInfo) {
                    String[] columns = tempString.trim().split("[ ]+");
                    if (columns.length == 10 && !columns[9].startsWith("/system/bin/")) {
                        processList.add(columns);
                    }
                }
            } else {
                bIsProcInfo = true;
            }
        }
        return processList;
    }

    public static long getAvailMemory(@NonNull Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static long getTotalMemory(Context context) {
        long memory = 0;
        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
            String memInfo = bufferedReader.readLine();
            String[] strs = memInfo.split("\\s+");
            memory = Integer.valueOf(strs[1]).intValue() * 1024;
            bufferedReader.close();
            return memory;
        } catch (Exception e) {
            e.printStackTrace();
            return memory;
        }
    }

    public static boolean isFastClick(int minClickDelayTime) {
        MIN_CLICK_DELAY_TIME = minClickDelayTime;
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - lastClickTime <= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
