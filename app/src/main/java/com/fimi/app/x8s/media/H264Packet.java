package com.fimi.app.x8s.media;

import android.util.Log;

import androidx.annotation.NonNull;


public class H264Packet {
    private final IH264DataListener ih264DataListener;
    int len = 0;
    int index = 0;
    int MAX_SHORT = 65535;
    int lastSeq = -1;
    byte[] h264Frame = new byte[1048576];
    private int seq;

    public H264Packet(IH264DataListener ih264DataListener) {
        this.ih264DataListener = ih264DataListener;
    }

    public void onPacket(@NonNull byte[] data) {
        byte[] buffer = new byte[1024];
        this.len = data.length;
        if (this.len >= 14) {
            this.seq = ((data[2] & 255) << 8) | (data[3] & 255);
            if (this.lastSeq == -1) {
                if (data[12] == 124 && data[18] == 39) {
                    System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                    this.index += this.len - 14;
                    this.lastSeq = this.seq;
                }
            } else if (this.seq - this.lastSeq > 1) {
                String error = "error frame" + this.lastSeq + " " + this.seq;
                Log.i("zdy", error);
                System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                System.arraycopy(this.h264Frame, 0, new byte[this.index], 0, this.index);
                Log.i("zdy", "fram =" + buffer.length);
                this.lastSeq = this.seq;
                this.index = 0;
            } else {
                if (this.index == 0) {
                    if (data[12] == 124 && data[18] == 39) {
                        System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                        this.index += this.len - 14;
                        Log.i("zdy", "ccccc frame");
                    }
                } else {
                    if (data[12] == 124 && data[13] != 64 && data[18] == 39) {
                        byte[] buffer2 = new byte[this.index];
                        System.arraycopy(this.h264Frame, 0, buffer2, 0, this.index);
                        Log.i("zdy", "fram =" + buffer2.length);
                        this.ih264DataListener.onH264Frame(buffer2);
                        this.index = 0;
                    }
                    System.arraycopy(data, 14, this.h264Frame, this.index, this.len - 14);
                    this.index += this.len - 14;
                }
                this.lastSeq = this.seq;
            }
        }
    }
}
