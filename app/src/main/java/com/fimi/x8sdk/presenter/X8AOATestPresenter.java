package com.fimi.x8sdk.presenter;

import android.os.Handler;
import android.os.Message;

import com.fimi.kernel.connect.session.VideodDataListener;
import com.fimi.x8sdk.command.AoaTestColletion;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.ivew.IAOATestView;

/* loaded from: classes2.dex */
public class X8AOATestPresenter extends BasePresenter {
    IAOATestView iaoaTestView;
    private boolean isInterrupt;
    private boolean isRunning;
    private long recvDataLen = 0;
    VideodDataListener videodDataListener = new VideodDataListener() { // from class: com.fimi.x8sdk.presenter.X8AOATestPresenter.1
        @Override // com.fimi.kernel.connect.session.VideodDataListener
        public void onRawdataCallBack(byte[] bytes) {
            if (X8AOATestPresenter.this.iaoaTestView != null) {
                X8AOATestPresenter.this.recvDataLen += bytes.length;
                X8AOATestPresenter.this.iaoaTestView.vedeoData(bytes);
            }
        }
    };
    private long sendDataLen = 0;

    public X8AOATestPresenter(IAOATestView iaoaTestView) {
        this.iaoaTestView = iaoaTestView;
        addNoticeListener(this.videodDataListener);
        this.mHandler.sendEmptyMessageDelayed(0, 1000L);
    }

    public void clearData() {
        this.recvDataLen = 0L;
        this.sendDataLen = 0L;
    }

    public void killDataChanel() {
        this.isInterrupt = false;
        this.isRunning = false;
    }    Handler mHandler = new Handler() { // from class: com.fimi.x8sdk.presenter.X8AOATestPresenter.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                X8AOATestPresenter.this.iaoaTestView.showSendDataLen(X8AOATestPresenter.this.sendDataLen);
                X8AOATestPresenter.this.iaoaTestView.showRecDataLen(X8AOATestPresenter.this.recvDataLen);
                X8AOATestPresenter.this.mHandler.sendEmptyMessageDelayed(0, 1000L);
            }
        }
    };

    public boolean isRunning() {
        return this.isRunning;
    }

    public void startReceivedData() {
        byte[] data = {-1, -1, -1, -1, 0, 0, 0, 10, 0, 1};
        sendCmd(new AoaTestColletion().getTestContent(data));
    }

    public void sendContent(final String content, boolean isLoop, final long time) {
        this.isInterrupt = isLoop;
        if (isLoop) {
            this.isRunning = true;
        }
        new Thread(new Runnable() { // from class: com.fimi.x8sdk.presenter.X8AOATestPresenter.3
            @Override // java.lang.Runnable
            public void run() {
                do {
                    X8AOATestPresenter.this.sendCmd(new AoaTestColletion().getTestContent(content));
                    X8AOATestPresenter.this.sendDataLen += content.getBytes().length;
                    if (time > 0) {
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } while (X8AOATestPresenter.this.isInterrupt);
            }
        }).start();
    }




}
