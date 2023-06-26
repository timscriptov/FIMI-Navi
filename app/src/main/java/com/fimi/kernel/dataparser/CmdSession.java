package com.fimi.kernel.dataparser;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;

import java.util.concurrent.LinkedBlockingDeque;


public class CmdSession {
    private final BaseConnect connect;
    private final long startTime = System.currentTimeMillis();
    public int sendCount;
    public int seq;
    public LinkedBlockingDeque<Object> cmdQue = new LinkedBlockingDeque<>();
    private BaseCommand cmd;

    public CmdSession(BaseCommand cmd, BaseConnect connect) {
        this.cmd = cmd;
        this.connect = connect;
        new CheckThread().start();
    }

    public BaseCommand getCmd() {
        return this.cmd;
    }

    public void reSend() {
        if (this.cmd != null) {
            this.connect.sendCmd(this.cmd);
        }
    }

    public void release() {
        this.cmd = null;
    }


    class CheckThread extends Thread {
        CheckThread() {
        }

        @Override
        public void run() {
            while (CmdSession.this.cmd != null && System.currentTimeMillis() - CmdSession.this.startTime >= CmdSession.this.cmd.getOutTime() && CmdSession.this.sendCount <= CmdSession.this.cmd.getReSendNum()) {
                CmdSession.this.reSend();
                CmdSession.this.sendCount++;
            }
        }
    }
}
