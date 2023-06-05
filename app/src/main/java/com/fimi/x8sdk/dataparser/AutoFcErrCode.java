package com.fimi.x8sdk.dataparser;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;


public class AutoFcErrCode extends X8BaseMessage {
    public static long p = ((long) Math.pow(2.0d, 32.0d)) - 1;
    long systemStatusCodA;
    long systemStatusCodB;
    long systemStatusCodC;

    @Override
    public void unPacket(LinkPacket4 packet) {
        super.decrypt(packet);
        this.systemStatusCodA = packet.getPayLoad4().getInt();
        if (this.systemStatusCodA < 0) {
            this.systemStatusCodA &= p;
        }
        this.systemStatusCodB = packet.getPayLoad4().getInt();
        if (this.systemStatusCodB < 0) {
            this.systemStatusCodB &= p;
        }
        this.systemStatusCodC = packet.getPayLoad4().getInt();
        if (this.systemStatusCodC < 0) {
            this.systemStatusCodC &= p;
        }
    }

    public long getSystemStatusCodA() {
        return this.systemStatusCodA;
    }

    public void setSystemStatusCodA(int systemStatusCodA) {
        this.systemStatusCodA = systemStatusCodA;
    }

    public long getSystemStatusCodB() {
        return this.systemStatusCodB;
    }

    public void setSystemStatusCodB(int systemStatusCodB) {
        this.systemStatusCodB = systemStatusCodB;
    }

    public long getSystemStatusCodC() {
        return this.systemStatusCodC;
    }

    public void setSystemStatusCodC(int systemStatusCodC) {
        this.systemStatusCodC = systemStatusCodC;
    }

    @Override
    public String toString() {
        return "AutoFcErrCode{systemStatusCodA=" + this.systemStatusCodA + ", systemStatusCodB=" + this.systemStatusCodB + ", systemStatusCodC=" + this.systemStatusCodC + '}';
    }
}
