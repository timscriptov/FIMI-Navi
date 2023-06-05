package com.fimi.kernel.fds;

import android.util.Log;

import java.io.IOException;


public class FdsUploadTask implements Runnable {
    private final IFdsUploadListener listener;
    private final FDSClient mFdsService;
    private final IFdsFileModel model;

    public FdsUploadTask(IFdsFileModel model, IFdsUploadListener listener) {
        this.model = model;
        this.model.setRunable(this);
        this.listener = listener;
        this.mFdsService = new FDSClient();
    }

    @Override
    public void run() {
        try {
            this.mFdsService.startUpload(this.model, this.listener);
        } catch (IOException e) {
            e.printStackTrace();
            this.model.setState(FdsUploadState.FAILED);
            Log.i("istep", "" + e);
        }
        if (this.model.getState() == FdsUploadState.SUCCESS) {
            this.listener.onSuccess(this.model);
        } else if (this.model.getState() == FdsUploadState.FAILED) {
            this.listener.onFailure(this.model);
        } else if (this.model.getState() == FdsUploadState.STOP) {
            this.listener.onStop(this.model);
        }
    }

    public void stopUpload() {
        this.mFdsService.stopUpload(this.model, this.listener);
    }
}
