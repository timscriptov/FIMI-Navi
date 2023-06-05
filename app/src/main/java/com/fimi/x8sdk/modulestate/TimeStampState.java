package com.fimi.x8sdk.modulestate;


public class TimeStampState {
    private static final TimeStampState timeStampState = new TimeStampState();
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
