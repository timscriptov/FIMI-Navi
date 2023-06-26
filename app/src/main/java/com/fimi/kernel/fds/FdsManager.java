package com.fimi.kernel.fds;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FdsManager implements IFdsUploadListener {
    private static final int DOWNLOAD_FAIL = 2;
    private static final int DOWNLOAD_PROGRESS = 0;
    private static final int DOWNLOAD_STOP = 3;
    private static final int DOWNLOAD_SUCCESS = 1;
    private static final FdsManager fdsManager = new FdsManager();
    private final List<IFdsFileModel> dataAll = new ArrayList();
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private IFdsCountListener countListener;
    private IFdsUiListener uiListener;
    private final Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (FdsManager.this.uiListener != null) {
                switch (msg.what) {
                    case 0:
                        FdsManager.this.uiListener.onProgress((IFdsFileModel) msg.obj, msg.arg1);
                        return;
                    case 1:
                        FdsManager.this.uiListener.onSuccess((IFdsFileModel) msg.obj);
                        return;
                    case 2:
                        FdsManager.this.uiListener.onFailure((IFdsFileModel) msg.obj);
                        return;
                    case 3:
                        FdsManager.this.uiListener.onStop((IFdsFileModel) msg.obj);
                        return;
                    default:
                }
            }
        }
    };

    public static FdsManager getInstance() {
        return fdsManager;
    }

    public void setUiListener(IFdsUiListener uiListener) {
        this.uiListener = uiListener;
    }

    public void setFdsCountListener(IFdsCountListener countListener) {
        this.countListener = countListener;
    }

    public void startDownload(IFdsFileModel model) {
        model.setState(FdsUploadState.WAIT);
        downloadFile(model);
    }

    private void downloadFile(IFdsFileModel model) {
        if (!this.dataAll.contains(model)) {
            FdsUploadTask d = new FdsUploadTask(model, this);
            Future<?> task = this.executorService.submit(d);
            model.setTaskFutrue(task);
            this.dataAll.add(model);
            notityDataSetChange();
        }
    }

    public void stopAll() {
        for (IFdsFileModel model : this.dataAll) {
            if (model.getTaskFutrue() != null) {
                if (model.getRunable() != null) {
                    model.getRunable().stopUpload();
                }
                model.getTaskFutrue().cancel(true);
            }
            model.setState(FdsUploadState.STOP);
            model.setRunable(null);
            model.setTaskFutrue(null);
        }
        this.dataAll.clear();
        notityDataSetChange();
    }

    public boolean hasUpload() {
        return this.dataAll.size() > 0;
    }

    public void remove(Object model) {
        this.dataAll.remove(model);
        notityDataSetChange();
    }

    public void notityDataSetChange() {
        this.countListener.onUploadingCountChange(this.dataAll.size());
    }

    @Override
    public void onProgress(Object responseObj, long progrss, long currentLength) {
        int p = (int) ((100 * progrss) / currentLength);
        this.mHanler.obtainMessage(0, p, p, responseObj).sendToTarget();
    }

    @Override
    public void onSuccess(Object responseObj) {
        this.mHanler.obtainMessage(1, responseObj).sendToTarget();
    }

    @Override
    public void onFailure(Object reasonObj) {
        this.mHanler.obtainMessage(2, reasonObj).sendToTarget();
    }

    @Override
    public void onStop(Object reasonObj) {
        this.mHanler.obtainMessage(3, reasonObj).sendToTarget();
    }
}
