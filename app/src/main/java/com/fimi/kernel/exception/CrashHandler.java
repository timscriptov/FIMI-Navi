package com.fimi.kernel.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.fimi.android.app.R;
import com.fimi.kernel.animutils.IOUtils;
import com.github.moduth.blockcanary.internal.BlockInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static String CRASH_LOG_FILEPATH = "/FimiLogger/CrashLogger/";
    private static CrashHandler instance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infos = new HashMap();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && this.mDefaultHandler != null) {
            this.mDefaultHandler.uncaughtException(thread, ex);
            return;
        }
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            Log.e(TAG, "error : ", e);
        } finally {
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.fimi.kernel.exception.CrashHandler$1] */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        collectDeviceInfo(this.mContext);
        new Thread() { // from class: com.fimi.kernel.exception.CrashHandler.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Looper.prepare();
                Toast.makeText(CrashHandler.this.mContext, CrashHandler.this.mContext.getString(R.string.app_exception_hint), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        saveCatchInfo2File(ex);
        return true;
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                this.infos.put(BlockInfo.KEY_VERSION_NAME, versionName);
                this.infos.put(BlockInfo.KEY_VERSION_CODE, versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e2) {
                Log.e(TAG, "an error occured when collect crash info", e2);
            }
        }
    }

    private String getFilePath() {
        boolean isSDCardExist = "mounted".equals(Environment.getExternalStorageState());
        boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
        if (!isSDCardExist || !isRootDirExist) {
            return "";
        }
        String file_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + CRASH_LOG_FILEPATH;
        return file_dir;
    }

    private String saveCatchInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : this.infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + IOUtils.LINE_SEPARATOR_UNIX);
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = this.formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".txt";
            String file_dir = getFilePath();
            File dir = new File(file_dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(file_dir + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            sendCrashLog2PM(file_dir + fileName);
            fos.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            return null;
        }
    }

    private void sendCrashLog2PM(String fileName) {
        if (!new File(fileName).exists()) {
            Toast.makeText(this.mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        FileInputStream fis = null;
        BufferedReader reader = null;
        try {
            try {
                FileInputStream fis2 = new FileInputStream(fileName);
                try {
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(fis2, "GBK"));
                    while (true) {
                        try {
                            String s = reader2.readLine();
                            if (s == null) {
                                break;
                            } else {
                                Log.i("info", s.toString());
                            }
                        } catch (FileNotFoundException e2) {
                            reader = reader2;
                            fis = fis2;
                            try {
                                reader.close();
                                fis.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        } catch (IOException e4) {
                            reader = reader2;
                            fis = fis2;
                            try {
                                reader.close();
                                fis.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        } catch (Throwable th) {
                            th = th;
                            reader = reader2;
                            fis = fis2;
                            try {
                                reader.close();
                                fis.close();
                            } catch (IOException e6) {
                                e6.printStackTrace();
                            }
                            throw th;
                        }
                    }
                    reader2.close();
                    fis2.close();
                    reader = reader2;
                    fis = fis2;
                } catch (Throwable e7) {
                    fis = fis2;
                }
            } catch (IOException e10) {
            }
        } catch (Throwable th3) {
        }
    }
}
