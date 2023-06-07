package com.fimi.app.x8s.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;

import com.fimi.host.HostConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;


public class LogSaveLocalHelper {
    private static LogSaveLocalHelper instance = null;
    private static String tag = null;
    private final int appid = Process.myPid();
    private final String dirPath;
    private Thread logThread;

    private LogSaveLocalHelper(Context mContext, String path) {
        if (TextUtils.isEmpty(path)) {
            this.dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "seeker" + File.separator + mContext.getPackageName();
        } else {
            this.dirPath = path;
        }
        File dir = new File(this.dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static LogSaveLocalHelper getInstance(Context mContext, String path) {
        if (instance == null) {
            instance = new LogSaveLocalHelper(mContext, path);
        }
        return instance;
    }

    public void setTag(String tag2) {
        tag = tag2;
    }

    public void start() {
        if (this.logThread == null) {
            this.logThread = new Thread(new LogRunnable(this.appid, this.dirPath));
        }
        this.logThread.start();
    }

    public static class LogRunnable implements Runnable {
        private final String cmds;
        private FileOutputStream fos;
        private final String mPid;
        private java.lang.Process mProcess;
        private BufferedReader mReader;

        public LogRunnable(int pid, String dirPath) {
            this.mPid = "" + pid;
            try {
                File file = new File(dirPath, FormatDate.getFormatDate() + ".txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                this.fos = new FileOutputStream(file, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.cmds = "logcat *:v | grep \"(" + this.mPid + ")\"";
        }

        @Override
        public void run() {
            try {
                try {
                    this.mProcess = Runtime.getRuntime().exec(this.cmds);
                    this.mReader = new BufferedReader(new InputStreamReader(this.mProcess.getInputStream()), 1024);
                    while (true) {
                        String line = this.mReader.readLine();
                        if (line == null) {
                            break;
                        } else if (line.length() != 0 && this.fos != null && line.contains(this.mPid) && (LogSaveLocalHelper.tag == null || (LogSaveLocalHelper.tag != null && line.contains(LogSaveLocalHelper.tag)))) {
                            this.fos.write((FormatDate.getFormatTime() + "\t" + line + "\r\n").getBytes());
                        }
                    }
                    if (this.mProcess != null) {
                        this.mProcess.destroy();
                        this.mProcess = null;
                    }
                    try {
                        if (this.mReader != null) {
                            this.mReader.close();
                            this.mReader = null;
                        }
                        if (this.fos != null) {
                            this.fos.close();
                            this.fos = null;
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (this.mProcess != null) {
                        this.mProcess.destroy();
                        this.mProcess = null;
                    }
                    try {
                        if (this.mReader != null) {
                            this.mReader.close();
                            this.mReader = null;
                        }
                        if (this.fos != null) {
                            this.fos.close();
                            this.fos = null;
                        }
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (this.mProcess != null) {
                    this.mProcess.destroy();
                    this.mProcess = null;
                }
                try {
                    if (this.mReader != null) {
                        this.mReader.close();
                        this.mReader = null;
                    }
                    if (this.fos != null) {
                        this.fos.close();
                        this.fos = null;
                    }
                } catch (Exception e23) {
                    e23.printStackTrace();
                }
                throw th;
            }
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public static class FormatDate {
        private FormatDate() {
        }

        public static String getFormatDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
            return sdf.format(System.currentTimeMillis());
        }

        public static String getFormatTime() {
            SimpleDateFormat sdf = new SimpleDateFormat(HostConstants.FORMATDATE);
            return sdf.format(System.currentTimeMillis());
        }
    }
}
