package com.fimi.album.download.task;

import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.kernel.connect.tcp.SocketOption;
import com.fimi.kernel.store.sqlite.entity.MediaDownloadInfo;
import com.fimi.kernel.store.sqlite.helper.MediaDownloadInfoHelper;
import com.fimi.kernel.utils.LogUtil;

import org.apache.mina.proxy.handlers.http.HttpProxyConstants;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes.dex */
public class MediaOriginalDownloadTask implements Runnable {
    private MediaDownloadInfo downloadInfo;
    private long finished = 0;
    private OnDownloadListener listener;
    private MediaModel model;

    public MediaOriginalDownloadTask(MediaModel model, OnDownloadListener listener) {
        this.model = model;
        this.listener = listener;
        model.setStop(false);
        model.setDownloadFail(false);
        model.setDownloading(true);
    }

    @Override // java.lang.Runnable
    public void run() {
        startDownload();
    }

    private void startDownload() {
        String path = this.model.getLocalFileDir();
        String urlPath = this.model.getFileUrl();
        this.model.setDownloadName(String.valueOf(urlPath.hashCode()));
        String fileName = this.model.getDownloadName();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path, fileName);
        this.downloadInfo = MediaDownloadInfoHelper.getIntance().queryMediaDownloadInfo(this.model.getFileUrl());
        if (this.downloadInfo != null) {
            if (file.exists()) {
                this.downloadInfo.setEndPos(this.model.getFileSize());
                this.downloadInfo.setStartPos(this.downloadInfo.getCompeleteZize());
            } else {
                MediaDownloadInfoHelper.getIntance().deleteByUrl(this.model.getFileUrl());
                this.downloadInfo = new MediaDownloadInfo();
                this.downloadInfo.setUrl(this.model.getFileUrl());
                MediaDownloadInfoHelper.getIntance().addMediaDownloadInfo(this.downloadInfo);
            }
        } else {
            this.downloadInfo = new MediaDownloadInfo();
            this.downloadInfo.setUrl(this.model.getFileUrl());
            MediaDownloadInfoHelper.getIntance().addMediaDownloadInfo(this.downloadInfo);
        }
        this.finished = this.downloadInfo.getStartPos();
        this.downloadInfo.setEndPos(this.model.getFileSize());
        downloadFile(file);
        if (this.model.isDownloadFinish()) {
            MediaDownloadInfoHelper.getIntance().deleteMediaDownloadInfo(this.model.getFileUrl());
            save(this.model);
            this.listener.onSuccess(this.model);
        } else if (this.model.isStop()) {
            this.listener.onStop(this.model);
        } else if (this.model.isDownloadFail()) {
            LogUtil.i("download", "MediaOriginalDownloadTask====startDownload: ");
            this.listener.onFailure(this.model);
        }
    }

    public void downloadFile(File file) {
        if (this.model.isStop()) {
            return;
        }
        HttpURLConnection connection = null;
        RandomAccessFile randomAccessFile = null;
        InputStream is = null;
        try {
            try {
                URL url = new URL(this.model.getFileUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(PathInterpolatorCompat.MAX_NUM_POINTS);
                connection.setRequestMethod(HttpProxyConstants.GET);
                connection.setRequestProperty("Range", "bytes=" + this.downloadInfo.getStartPos() + "-");
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rwd");
                try {
                    randomAccessFile2.seek(this.downloadInfo.getStartPos());
                    InputStream is2 = connection.getInputStream();
                    byte[] buffer = new byte[SocketOption.RECEIVE_BUFFER_SIZE];
                    do {
                        int length = is2.read(buffer);
                        if (length == -1) {
                            if (this.finished == this.model.getFileSize()) {
                                this.model.setDownloadFinish(true);
                                this.model.setDownloading(false);
                                this.model.setDownLoadOriginalFile(true);
                            } else {
                                this.model.setDownloadFinish(false);
                                this.model.setDownloadFail(true);
                                this.model.setDownloading(false);
                                this.listener.onFailure(this.model);
                            }
                            try {
                                is2.close();
                                randomAccessFile2.close();
                                connection.disconnect();
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        randomAccessFile2.write(buffer, 0, length);
                        this.finished += length;
                        this.downloadInfo.setCompeleteZize(this.finished);
                        this.model.setTotal(this.finished);
                        this.model.setDownloading(true);
                        this.model.setDownloadFail(false);
                        this.listener.onProgress(this.model, this.model.getTotal(), this.model.getFileSize());
                        MediaDownloadInfoHelper.getIntance().updateMediaDownloadInfo(this.model.getFileUrl(), this.downloadInfo);
                    } while (!this.model.isStop());
                    this.model.setDownloading(false);
                    this.model.setDownloadFinish(false);
                    this.listener.onStop(this.model);
                    try {
                        is2.close();
                        randomAccessFile2.close();
                        connection.disconnect();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e3) {
                    randomAccessFile = randomAccessFile2;
                    this.model.setDownloadFinish(false);
                    this.model.setDownloadFail(true);
                    this.model.setDownloading(false);
                    this.listener.onFailure(this.model);
                    try {
                        is.close();
                        randomAccessFile.close();
                        connection.disconnect();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                } catch (Throwable th) {
                    randomAccessFile = randomAccessFile2;
                    try {
                        is.close();
                        randomAccessFile.close();
                        connection.disconnect();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
            }
        } catch (Exception e6) {
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
}
