package com.fimi.album.download.manager;

import android.os.Handler;
import android.os.Message;

import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.download.task.MediaOriginalDownloadTask;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.IMediaDownload;
import com.fimi.album.interfaces.IMediaFileDownloadObserver;
import com.fimi.album.interfaces.IMediaFileDownloadObserverable;
import com.fimi.album.interfaces.OnDownloadUiListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MediaFileDownloadManager<T extends MediaModel> implements OnDownloadListener, IMediaDownload, IMediaFileDownloadObserverable {
    private static final int DOWNLOAD_FAIL = 2;
    private static final int DOWNLOAD_PROGRESS = 0;
    private static final int DOWNLOAD_STOP = 3;
    private static final int DOWNLOAD_SUCCESS = 1;
    private static final String TAG = "MediaFileDownloadManage";
    private static final MediaFileDownloadManager mMediaFileDownloadManager = new MediaFileDownloadManager();
    private OnDownloadUiListener mUiDownloadListener;
    private IMediaFileDownloadObserver observer;
    private final List<MediaModel> data = new ArrayList();
    private final List<MediaModel> dataAll = new ArrayList();
    private final List<MediaModel> dataResult = new ArrayList();
    private int mLastPos = -1;
    private final Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (MediaFileDownloadManager.this.mUiDownloadListener != null) {
                switch (msg.what) {
                    case 0:
                        MediaFileDownloadManager.this.mUiDownloadListener.onProgress((MediaModel) msg.obj, msg.arg1);
                        return;
                    case 1:
                        MediaFileDownloadManager.this.mUiDownloadListener.onSuccess((MediaModel) msg.obj);
                        return;
                    case 2:
                        MediaFileDownloadManager.this.mUiDownloadListener.onFailure((MediaModel) msg.obj);
                        return;
                    case 3:
                        MediaFileDownloadManager.this.mUiDownloadListener.onStop((MediaModel) msg.obj);
                        return;
                    default:
                }
            }
        }
    };
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static MediaFileDownloadManager getInstance() {
        return mMediaFileDownloadManager;
    }

    public void setUiDownloadListener(OnDownloadUiListener mUiDownloadListener) {
        this.mUiDownloadListener = mUiDownloadListener;
    }

    public void addList(List<MediaModel> selectList) {
        this.data.clear();
        this.data.addAll(selectList);
    }

    @Override
    public void addData(MediaModel m) {
        this.data.add(m);
    }

    @Override
    public void stopDownload() {
        this.data.clear();
    }

    @Override
    public void startDownload() {
    }

    public void startDownload(MediaModel model) {
        model.setDownloadFail(false);
        model.setDownloading(true);
        model.setStop(false);
        downloadFile(model);
    }

    public void startDownload(List<MediaModel> selectList) {
        addList(selectList);
        for (int i = 0; i < this.data.size(); i++) {
            MediaModel model = this.data.get(i);
            if (!model.isDownLoadOriginalFile() && !model.isDownloading()) {
                downloadFile(model);
            }
        }
    }

    private void downloadFile(MediaModel model) {
        if (!this.dataAll.contains(model)) {
            this.dataAll.add(model);
        }
        MediaOriginalDownloadTask d = new MediaOriginalDownloadTask(model, this);
        Future<?> task = this.executorService.submit(d);
        model.setTaskFutrue(task);
        notityObserver(this.dataAll.size(), this.dataResult.size());
    }

    @Override
    public void onProgress(Object responseObj, long progrss, long currentLength) {
        int pos = (int) (progrss / (currentLength / 100));
        if (this.mLastPos != pos) {
            this.mHanler.obtainMessage(0, pos, pos, responseObj).sendToTarget();
            this.mLastPos = pos;
        }
    }

    @Override
    public void onSuccess(Object responseObj) {
        this.mLastPos = -1;
        this.dataResult.add((MediaModel) responseObj);
        this.mHanler.obtainMessage(1, responseObj).sendToTarget();
    }

    @Override
    public void onFailure(Object reasonObj) {
        this.mLastPos = -1;
        this.mHanler.obtainMessage(2, reasonObj).sendToTarget();
    }

    @Override
    public void onStop(MediaModel reasonObj) {
        this.mLastPos = -1;
        this.mHanler.obtainMessage(3, reasonObj).sendToTarget();
    }

    @Override
    public void addObserver(IMediaFileDownloadObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notityObserver(int count, int downloadSize) {
        if (this.observer != null) {
            this.observer.onMediaFileDownloadUpdate(count, downloadSize);
        }
    }
}
