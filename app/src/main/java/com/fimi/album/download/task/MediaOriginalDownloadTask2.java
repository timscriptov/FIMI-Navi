package com.fimi.album.download.task;

import android.annotation.SuppressLint;

import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.kernel.connect.tcp.SocketOption;

import org.apache.mina.proxy.handlers.http.HttpProxyConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MediaOriginalDownloadTask2 implements Runnable {
    private final OnDownloadListener listener;
    private final MediaModel model;
    private int finished = 0;

    public MediaOriginalDownloadTask2(MediaModel model, OnDownloadListener listener) {
        this.model = model;
        this.listener = listener;
        model.setTotal(0L);
        model.setDownloading(true);
    }

    @Override
    public void run() {
        startDownload();
    }

    private void startDownload() {
        getLength();
        String path = this.model.getLocalFileDir();
        String urlPath = this.model.getFileUrl();
        this.model.setDownloadName(String.valueOf(urlPath.hashCode()));
        String fileName = this.model.getDownloadName();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path, fileName);
        if (file.exists()) {
            this.listener.onSuccess(this.model);
            return;
        }
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestProperty("Range", "bytes=0-" + (this.model.getFileSize() - 1));
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            CopyStream(is, os);
            save(this.model);
            os.close();
            conn.disconnect();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        if (this.model.isDownloadFinish()) {
            this.listener.onSuccess(this.model);
        } else {
            this.listener.onFailure(this.model);
        }
    }

    public void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[SocketOption.RECEIVE_BUFFER_SIZE];
            while (true) {
                int len = is.read(buffer);
                if (len != -1) {
                    os.write(buffer, 0, len);
                    this.finished += len;
                    this.model.setTotal(this.finished);
                    this.listener.onProgress(this.model, this.model.getTotal(), this.model.getFileSize());
                    if (this.model.isStop()) {
                        this.listener.onStop(this.model);
                        this.model.setDownloading(false);
                        break;
                    }
                } else {
                    this.model.setDownloadFinish(true);
                    this.model.setDownloading(false);
                    this.model.setDownLoadOriginalFile(true);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean save(MediaModel model) {
        String name = model.getName();
        String localPath = model.getLocalFileDir();
        File file = new File(localPath, name);
        File tmpFile = new File(localPath, model.getDownloadName());
        boolean b = tmpFile.renameTo(file);
        model.setFileLocalPath(file.getAbsolutePath());
        return b;
    }

    @SuppressLint("RestrictedApi")
    private int getLength() {
        HttpURLConnection connection = null;
        int length = -1;
        try {
            try {
                URL url = new URL(this.model.getFileUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(HttpProxyConstants.GET);
                connection.setConnectTimeout(PathInterpolatorCompat.MAX_NUM_POINTS);
                length = connection.getResponseCode() == 200 ? connection.getContentLength() : -1;
            } catch (Exception e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            if (length <= 0) {
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                return length;
            }
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            return length;
        } catch (Throwable th) {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
            }
            throw th;
        }
    }
}
