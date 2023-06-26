package com.fimi.app.x8s.ui.album.x8s;

import android.os.Handler;
import android.os.Message;

import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.IMediaDownload;
import com.fimi.album.interfaces.IMediaFileDownloadObserver;
import com.fimi.album.interfaces.IMediaFileDownloadObserverable;
import com.fimi.album.interfaces.OnDownloadUiListener;
import com.fimi.host.HostLogBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class X8MediaFileDownloadManager<T extends MediaModel> implements OnDownloadListener, IMediaDownload, IMediaFileDownloadObserverable {
    private static final int DOWNLOAD_FAIL = 2;
    private static final int DOWNLOAD_PROGRESS = 0;
    private static final int DOWNLOAD_STOP = 3;
    private static final int DOWNLOAD_STOP_TIME_OUT = 4;
    private static final int DOWNLOAD_SUCCESS = 1;
    private static final String TAG = "MediaFileDownloadManage";
    private static final X8MediaFileDownloadManager mMediaFileDownloadManager = new X8MediaFileDownloadManager();
    private final List<MediaModel> data = new ArrayList();
    private final List<MediaModel> dataAll = new ArrayList();
    private final List<MediaModel> dataResult = new ArrayList();
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    public HashMap<String, X8MediaOriginalDownloadTask> taskHashMap = new HashMap<>();
    private int index;
    private boolean isDownload;
    private OnDownloadUiListener mUiDownloadListener;
    private final Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (X8MediaFileDownloadManager.this.mUiDownloadListener != null) {
                switch (msg.what) {
                    case 0:
                        X8MediaFileDownloadManager.this.mUiDownloadListener.onProgress((MediaModel) msg.obj, msg.arg1);
                        return;
                    case 1:
                        X8MediaFileDownloadManager.this.mUiDownloadListener.onSuccess((MediaModel) msg.obj);
                        return;
                    case 2:
                        X8MediaFileDownloadManager.this.mUiDownloadListener.onFailure((MediaModel) msg.obj);
                        return;
                    case 3:
                        MediaModel mediaModel = (MediaModel) msg.obj;
                        X8MediaFileDownloadManager.this.mUiDownloadListener.onStop(mediaModel);
                        X8MediaFileDownloadManager.this.sendStopDownload(mediaModel);
                        X8MediaFileDownloadManager.this.next();
                        return;
                    case 4:
                    default:
                }
            }
        }
    };
    private IMediaFileDownloadObserver observer;
    private int mLastPos = -1;

    public static X8MediaFileDownloadManager getInstance() {
        return mMediaFileDownloadManager;
    }

    public boolean isDownload() {
        return this.isDownload;
    }

    public void setUiDownloadListener(OnDownloadUiListener mUiDownloadListener) {
        this.mUiDownloadListener = mUiDownloadListener;
    }

    public void addList(List<MediaModel> selectList) {
        for (MediaModel mediaModel : selectList) {
            addData(mediaModel);
        }
    }

    @Override
    public void addData(MediaModel m) {
        if (!m.isDownLoadOriginalFile() && !m.isDownloading() && !this.data.contains(m)) {
            HostLogBack.getInstance().writeLog("Alanqiu  =================addData:" + m);
            this.data.add(m);
        }
    }

    @Override
    public void stopDownload() {
        this.isDownload = false;
        this.index = 0;
        this.data.clear();
        this.taskHashMap.clear();
    }

    @Override
    public void startDownload() {
    }

    public void startDownload(MediaModel model) {
        addData(model);
        if (!this.dataAll.contains(model)) {
            this.dataAll.add(model);
        }
        notityObserver(this.dataAll.size(), this.dataResult.size());
        X8MediaOriginalDownloadTask d = new X8MediaOriginalDownloadTask(model, this);
        this.taskHashMap.put(model.getMd5(), d);
        if (!this.isDownload) {
            downloadFile();
            model.setDownloadFail(false);
            model.setDownloading(true);
            model.setStop(false);
        }
    }

    public void startDownload(List<MediaModel> selectList) {
        addList(selectList);
        for (int i = 0; i < this.data.size(); i++) {
            MediaModel model = this.data.get(i);
            if (!model.isDownLoadOriginalFile() && !model.isDownloading()) {
                if (!this.dataAll.contains(model)) {
                    this.dataAll.add(model);
                }
                X8MediaOriginalDownloadTask d = new X8MediaOriginalDownloadTask(model, this);
                this.taskHashMap.put(model.getMd5(), d);
                notityObserver(this.dataAll.size(), this.dataResult.size());
            }
        }
        if (!this.isDownload) {
            downloadFile();
        }
    }

    private void downloadFile() {
        if (this.data.size() > 0 && this.index < this.data.size()) {
            MediaModel model = this.data.get(this.index);
            if (this.taskHashMap.get(model.getMd5()) != null) {
                this.isDownload = true;
                Future<?> task = this.executorService.submit(this.taskHashMap.get(model.getMd5()));
                model.setTaskFutrue(task);
                HostLogBack.getInstance().writeLog("Alanqiu  ===================downloadFile:" + model + "index:" + this.index);
            }
        }
    }

    public void next() {
        if (this.isDownload && this.index < this.data.size() - 1) {
            this.index++;
            downloadFile();
            HostLogBack.getInstance().writeLog("Alanqiu  ===========1====next index:" + this.index);
            return;
        }
        stopDownload();
    }

    public void stopAllDownload() {
        this.isDownload = false;
        this.index = 0;
        this.mUiDownloadListener = null;
        for (MediaModel model : this.dataAll) {
            model.setDownloadFail(false);
            model.setStop(true);
        }
        this.dataAll.clear();
        this.data.clear();
        this.taskHashMap.clear();
    }

    public boolean hasDownloading() {
        for (MediaModel model : this.dataAll) {
            if (model.isDownloading()) {
                return true;
            }
        }
        return false;
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
        next();
    }

    @Override
    public void onFailure(Object reasonObj) {
        this.mLastPos = -1;
        this.mHanler.obtainMessage(2, reasonObj).sendToTarget();
        next();
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

    public void sendStopDownload(MediaModel mediaModel) {
        X8MediaOriginalDownloadTask x8MediaOriginalDownloadTask = this.taskHashMap.get(mediaModel.getMd5());
        if (x8MediaOriginalDownloadTask != null) {
            x8MediaOriginalDownloadTask.sendStopDownload();
            this.taskHashMap.remove(mediaModel);
            x8MediaOriginalDownloadTask.removeMediaListener();
        }
    }
}
