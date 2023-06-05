package com.fimi.kernel.connect.retransmission;

import android.os.SystemClock;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.session.NoticeManager;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: classes.dex */
public class RetransmissionJsonThread extends Thread {
    public LinkedBlockingDeque<BaseCommand> mListReSend = new LinkedBlockingDeque<>();
    private boolean isLoop = true;

    public void addToSendList(BaseCommand mBaseCommand) {
        this.mListReSend.add(mBaseCommand);
    }

    public BaseCommand removeFromListByCmdID(int cmdId, String camKey) {
        BaseCommand removeCmd = null;
        synchronized (this.mListReSend) {
            Iterator<BaseCommand> it = this.mListReSend.iterator();
            while (it.hasNext()) {
                BaseCommand msg = it.next();
                if ((msg.getMsgId() == cmdId && camKey == null) || (msg.getMsgId() == cmdId && camKey.equals(msg.getCamKey()))) {
                    removeCmd = msg;
                    break;
                }
            }
            if (removeCmd != null) {
                this.mListReSend.remove(removeCmd);
            }
        }
        return removeCmd;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.isLoop) {
            if (!this.mListReSend.isEmpty()) {
                BaseCommand removeData = null;
                Iterator<BaseCommand> it = this.mListReSend.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    BaseCommand bcd = it.next();
                    if (SystemClock.uptimeMillis() - bcd.getLastSendTime() >= bcd.getOutTime()) {
                        removeData = bcd;
                        break;
                    }
                }
                if (removeData != null) {
                    NoticeManager.getInstance().onSendTimeOut(removeData.getMsgGroudId(), removeData.getMsgId(), removeData);
                    this.mListReSend.remove(removeData);
                }
            } else {
                try {
                    Thread.sleep(400L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exit() {
        this.isLoop = false;
        interrupt();
    }
}
