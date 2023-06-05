package com.fimi.kernel.connect.retransmission;

import android.os.SystemClock;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IDataTransfer;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.LinkPacket;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: classes.dex */
public class RetransmissionThread extends Thread {
    private final int sleepTime = 500;
    public LinkedBlockingDeque<BaseCommand> mListReSend = new LinkedBlockingDeque<>();
    private IDataTransfer mDataTransfer;
    private boolean isLoop = true;

    public RetransmissionThread(IDataTransfer mDataTransfer) {
        this.mDataTransfer = mDataTransfer;
    }

    public void addToSendList(BaseCommand mBaseCommand) {
        this.mListReSend.add(mBaseCommand);
    }

    public boolean removeFromListByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        boolean ret = false;
        synchronized (this.mListReSend) {
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
            if (remove != null) {
                ret = true;
                this.mListReSend.remove(remove);
            }
        }
        return ret;
    }

    public boolean removeFromListByCmdIDLinkPacket4(int groupId, int cmdId, int seq, LinkPacket4 packet) {
        boolean ret = false;
        synchronized (this.mListReSend) {
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
            if (remove != null) {
                ret = true;
                packet.setPersonalDataCallBack(remove.getPersonalDataCallBack());
                packet.setUiCallBack(remove.getUiCallBack());
                this.mListReSend.remove(remove);
            }
        }
        return ret;
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
                        int seq = bcd.getCurrentSendNum();
                        if (seq >= bcd.getReSendNum()) {
                            removeData = bcd;
                            break;
                        }
                        bcd.setCurrentSendNum(seq + 1);
                        this.mDataTransfer.sendRestransmissionData(bcd);
                        bcd.setLastSendTime(SystemClock.uptimeMillis());
                    }
                }
                if (removeData != null) {
                    this.mDataTransfer.onSendTimeOut(removeData.getMsgGroudId(), removeData.getMsgId(), removeData);
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
