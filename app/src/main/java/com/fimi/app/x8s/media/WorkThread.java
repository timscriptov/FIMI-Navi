package com.fimi.app.x8s.media;

import android.os.Environment;

import com.fimi.app.x8s.X8Application;

import java.io.FileInputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class WorkThread extends Thread {
    private final FimiH264Video mFimiH264Video;
    boolean isLoop;
    private String filePath = Environment.getExternalStorageDirectory() + "/test.h264";

    public WorkThread(FimiH264Video mFimiH264Video) {
        this.mFimiH264Video = mFimiH264Video;
    }

    public void stopThread() {
        this.isLoop = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (!X8Application.Type2) {
            readType1();
        } else {
            readType2();
        }
    }

    public void readType1() {
        int len;
        try {
            this.filePath = Environment.getExternalStorageDirectory() + "/test.h264";
            InputStream in = new FileInputStream(this.filePath);
            if (in != null) {
                byte[] buf = new byte[1024];
                while (!this.isLoop && (len = in.read(buf)) > 0) {
                    byte[] data = new byte[len];
                    System.arraycopy(buf, 0, data, 0, len);
                    this.mFimiH264Video.onRawdataCallBack(data);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readType2() {
        try {
            this.filePath = Environment.getExternalStorageDirectory() + "/zdy.h264";
            InputStream in = new FileInputStream(this.filePath);
            byte[] data = new byte[2];
            int availableLen = in.available();
            int count = 0;
            while (!this.isLoop && count < availableLen) {
                in.read(data);
                int len = ((data[0] & 255) << 8) | (data[1] & 255);
                byte[] cmd = new byte[len];
                in.read(cmd);
                count = count + 2 + len;
                this.mFimiH264Video.onRawdataCallBack(cmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
