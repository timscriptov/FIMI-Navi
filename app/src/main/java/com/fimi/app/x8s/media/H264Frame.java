package com.fimi.app.x8s.media;


public class H264Frame {
    private int index;
    private boolean isFirstFrame;
    private boolean isFrame;
    private State state = State.a1;
    private final byte[] data = new byte[1048576];

    public void onSeqErrorReset() {
        if (!this.isFirstFrame) {
            reset();
            return;
        }
        this.state = State.pload;
        this.index = 0;
    }

    private void reset() {
        this.state = State.a1;
        this.index = 0;
    }

    public byte[] getDataBuf() {
        this.state = State.pload;
        if (!this.isFirstFrame) {
            this.isFirstFrame = true;
            byte[] buf = new byte[this.index - 4];
            System.arraycopy(this.data, 0, buf, 0, this.index - 4);
            this.index = 0;
            return buf;
        }
        byte[] buf2 = new byte[this.index];
        buf2[3] = 1;
        System.arraycopy(this.data, 0, buf2, 4, this.index - 4);
        this.index = 0;
        return buf2;
    }

    public boolean parse(byte b) {
        this.isFrame = false;
        byte[] bArr = this.data;
        int i = this.index;
        this.index = i + 1;
        bArr[i] = b;
        switch (this.state) {
            case a1:
                if (b == 0) {
                    this.state = State.a2;
                    break;
                } else {
                    reset();
                    break;
                }
            case a2:
                if (b == 0) {
                    this.state = State.a3;
                    break;
                } else {
                    reset();
                    break;
                }
            case a3:
                if (b == 0) {
                    this.state = State.B1;
                    break;
                } else {
                    reset();
                    break;
                }
            case B1:
                if (b == 1) {
                    this.state = State.pload;
                    break;
                } else {
                    reset();
                    break;
                }
            case pload:
                if (b == 0) {
                    this.state = State.a4;
                    break;
                }
                break;
            case a4:
                if (b == 0) {
                    this.state = State.a5;
                    break;
                } else {
                    this.state = State.pload;
                    break;
                }
            case a5:
                if (b == 0) {
                    this.state = State.a6;
                    break;
                } else {
                    this.state = State.pload;
                    break;
                }
            case a6:
                if (b == 1) {
                    this.isFrame = true;
                    break;
                } else {
                    this.state = State.pload;
                    break;
                }
        }
        return this.isFrame;
    }

    public void setData(byte[] buffer, int start, int len) {
        System.arraycopy(buffer, start, this.data, this.index, len);
        this.index += len;
    }

    public void resetIndex() {
        this.index = 0;
    }

    public byte[] getDataBuf2() {
        byte[] buf = new byte[this.index];
        System.arraycopy(this.data, 0, buf, 0, this.index);
        this.index = 0;
        return buf;
    }


    public enum State {
        a1,
        a2,
        a3,
        B1,
        pload,
        a4,
        a5,
        a6,
        B2
    }
}
