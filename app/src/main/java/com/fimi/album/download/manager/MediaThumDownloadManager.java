package com.fimi.album.download.manager;

import android.os.Handler;
import android.os.Message;

import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.download.task.MediaThumDownloadTask;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.IMediaDownload;
import com.fimi.album.interfaces.OnDownloadUiListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MediaThumDownloadManager implements OnDownloadListener, IMediaDownload {
    private int count;
    private int index;
    private boolean isDownload;
    private final OnDownloadUiListener mUiDownloadListener;
    private final List<MediaModel> data = new ArrayList();
    private final Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MediaThumDownloadManager.this.mUiDownloadListener.onProgress((MediaModel) msg.obj, msg.arg1);
                    return;
                case 1:
                    MediaThumDownloadManager.access$108(MediaThumDownloadManager.this);
                    MediaThumDownloadManager.this.mUiDownloadListener.onSuccess((MediaModel) msg.obj);
                    return;
                case 2:
                    MediaThumDownloadManager.this.mUiDownloadListener.onFailure((MediaModel) msg.obj);
                    return;
                default:
            }
        }
    };
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public MediaThumDownloadManager(OnDownloadUiListener mUiDownloadListener) {
        this.mUiDownloadListener = mUiDownloadListener;
    }

    static /* synthetic */ int access$108(MediaThumDownloadManager x0) {
        int i = x0.count;
        x0.count = i + 1;
        return i;
    }

    @Override
    public void addData(MediaModel m) {
        if (!this.data.contains(m)) {
            this.data.add(m);
        }
    }

    @Override
    public void stopDownload() {
        this.isDownload = false;
        this.count = 0;
        this.index = 0;
        this.data.clear();
    }

    public int getCount() {
        return this.data.size();
    }

    @Override
    public void startDownload() {
        this.isDownload = true;
        if (this.data.size() > 0 && this.count < this.data.size()) {
            MediaModel info = this.data.get(this.index);
            MediaThumDownloadTask d = new MediaThumDownloadTask(info, this);
            this.executorService.submit(d);
        }
    }

    public void next() {
        if (this.isDownload) {
            this.index++;
            startDownload();
        }
    }

    @Override
    public void onProgress(Object responseObj, long progrss, long currentLength) {
        int pos = (int) (progrss / (currentLength / 100));
        this.mHanler.obtainMessage(0, pos, pos, responseObj).sendToTarget();
    }

    @Override
    public void onSuccess(Object responseObj) {
        this.mHanler.obtainMessage(1, responseObj).sendToTarget();
        next();
    }

    @Override
    public void onFailure(Object reasonObj) {
        this.mHanler.obtainMessage(2, reasonObj).sendToTarget();
        next();
    }

    @Override
    public void onStop(MediaModel reasonObj) {
    }
}
