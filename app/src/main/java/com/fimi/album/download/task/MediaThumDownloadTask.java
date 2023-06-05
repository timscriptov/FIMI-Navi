package com.fimi.album.download.task;

import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes.dex */
public class MediaThumDownloadTask implements Runnable {
    private int finished = 0;
    private OnDownloadListener listener;
    private MediaModel model;

    public MediaThumDownloadTask(MediaModel model, OnDownloadListener listener) {
        this.model = model;
        this.listener = listener;
        model.setThumDownloading(true);
    }

    @Override // java.lang.Runnable
    public void run() {
        startDownload();
    }

    private void startDownload() {
        String path = this.model.getLocalThumFileDir();
        String urlPath = this.model.getThumFileUrl();
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
        if (this.model.isThumDownloadFinish()) {
            this.listener.onSuccess(this.model);
        } else {
            this.listener.onFailure(this.model);
        }
    }

    public void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[4096];
            while (true) {
                int len = is.read(buffer);
                if (len != -1) {
                    os.write(buffer, 0, len);
                    this.finished += len;
                    this.model.setThumTotal(this.finished);
                    this.listener.onProgress(this.model, this.model.getThumTotal(), this.model.getThumSize());
                    if (this.model.isThumStop()) {
                        this.listener.onStop(this.model);
                        this.model.setThumDownloading(false);
                        break;
                    }
                } else if (this.model.getThumSize() == this.model.getThumTotal()) {
                    this.model.setThumDownloadFinish(true);
                    this.model.setThumDownloading(false);
                    this.model.setDownLoadThum(true);
                } else {
                    this.model.setThumDownloading(false);
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean save(MediaModel model) {
        String name = model.getThumName();
        String localPath = model.getLocalThumFileDir();
        File file = new File(localPath, name);
        File tmpFile = new File(localPath, model.getDownloadName());
        boolean b = tmpFile.renameTo(file);
        model.setFileLocalPath(file.getAbsolutePath());
        return b;
    }
}
