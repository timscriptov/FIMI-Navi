package com.fimi.kernel.connect.retransmission;

import android.os.SystemClock;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IDataTransfer;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;


public class RetransmissionUsbThread extends Thread {
    public LinkedBlockingDeque<BaseCommand> mListReSend = new LinkedBlockingDeque<>();
    private final IDataTransfer mDataTransfer;
    private boolean isLoop = true;

    public RetransmissionUsbThread(IDataTransfer mDataTransfer) {
        this.mDataTransfer = mDataTransfer;
    }

    public void addToSendList(BaseCommand mBaseCommand) {
        this.mListReSend.add(mBaseCommand);
    }

    public boolean removeFromListByUsbCmdKey(int cmdKey) {
        boolean ret = false;
        synchronized (this.mListReSend) {
            BaseCommand remove = null;
            Iterator<BaseCommand> it = this.mListReSend.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BaseCommand msg = it.next();
                if (msg.getUsbCmdKey() == cmdKey) {
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

    public boolean removeFromListByOffset(int fileOffset) {
        boolean ret = false;
        synchronized (this.mListReSend) {
            BaseCommand remove = null;
            Iterator<BaseCommand> it = this.mListReSend.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BaseCommand msg = it.next();
                if (msg.getFileOffset() == fileOffset) {
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

    @Override
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
