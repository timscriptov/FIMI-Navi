package com.fimi.x8sdk.modulestate;

/* loaded from: classes2.dex */
public class TimeStampState {
    private static TimeStampState timeStampState = new TimeStampState();
    private long timeStamp;

    public static TimeStampState getInstance() {
        return timeStampState;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
