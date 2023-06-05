package com.fimi.kernel.connect.retransmission;

import android.os.SystemClock;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.milink.LinkPacket;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: classes.dex */
public class TimerSendQueueThread extends Thread {
    private final int sleepTime = 500;
    public LinkedBlockingDeque<BaseCommand> mListReSend = new LinkedBlockingDeque<>();
    private boolean isLoop = true;

    public void addToSendList(BaseCommand mBaseCommand) {
        this.mListReSend.add(mBaseCommand);
    }

    public boolean removeFromListByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        BaseCommand remove = null;
        Iterator<BaseCommand> it = this.mListReSend.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            BaseCommand msg = it.next();
            if (msg.getMsgGroudId() == groupId && msg.getMsgId() == cmdId && msg.getMsgSeq() == seq) {
                remove = msg;
                break;
            }
        }
        if (remove == null) {
            return false;
        }
        packet.setPersonalDataCallBack(remove.getPersonalDataCallBack());
        this.mListReSend.remove(remove);
        return true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.isLoop) {
            if (!this.mListReSend.isEmpty()) {
                BaseCommand removeData = null;
                Iterator<BaseCommand> it = this.mListReSend.iterator();
                while (it.hasNext()) {
                    BaseCommand bcd = it.next();
                    if (SystemClock.uptimeMillis() - bcd.getLastSendTime() >= bcd.getOutTime()) {
                        removeData = bcd;
                    }
                }
                if (removeData != null) {
                    this.mListReSend.remove(removeData);
                }
            } else {
                try {
                    Thread.sleep(3000L);
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
