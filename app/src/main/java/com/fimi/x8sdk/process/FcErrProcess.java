package com.fimi.x8sdk.process;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.fimi.x8sdk.entity.ErrCodeEntity;
import com.fimi.x8sdk.listener.ErrcodeListener;

import java.util.ArrayList;
import java.util.List;


public class FcErrProcess {
    private static final int WHATID_SEND_ERRCODE = 0;
    private static final int WHATID_SEND_VC_ERRCODE = 1;
    private static final FcErrProcess fcErrProcess = new FcErrProcess();
    List<ErrcodeListener> listenerList = new ArrayList();
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj != null && FcErrProcess.this.listenerList.size() > 0) {
                        for (ErrcodeListener listener : FcErrProcess.this.listenerList) {
                            listener.showErrCode((List) msg.obj);
                        }
                        return;
                    }
                    return;
                case 1:
                    if (FcErrProcess.this.listenerList.size() > 0) {
                        for (ErrcodeListener listener2 : FcErrProcess.this.listenerList) {
                            listener2.showVcErrCode(msg.arg1);
                        }
                        return;
                    }
                    return;
                default:
            }
        }
    };
    List<ErrCodeEntity> errCodeEntities = new ArrayList();

    public static FcErrProcess getFcErrProcess() {
        return fcErrProcess;
    }

    public void registerErrListener(ErrcodeListener listener) {
        if (listener != null) {
            this.listenerList.add(listener);
        }
    }

    public void removeErrListener(ErrcodeListener listener) {
        if (listener != null) {
            this.listenerList.remove(listener);
        }
    }

    public void removeAllErrList() {
        this.listenerList.clear();
    }

    public void setErrCode(int... errCodes) {
        this.errCodeEntities.clear();
        if (!this.errCodeEntities.isEmpty()) {
            this.mHandler.obtainMessage(0, this.errCodeEntities).sendToTarget();
        }
    }

    public void setVcErrorCode(int error) {
        this.mHandler.obtainMessage(1, error, 0).sendToTarget();
    }

    public List<ErrCodeEntity> getErrCodeEntities() {
        return this.errCodeEntities;
    }
}
