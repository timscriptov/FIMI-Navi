package com.fimi.app.x8s.media;

import android.util.Log;

import com.fimi.app.x8s.X8Application;

import java.util.concurrent.LinkedBlockingDeque;


public class H264DecoderThread extends Thread implements IH264DataListener {
    public LinkedBlockingDeque<byte[]> cmdQuene = new LinkedBlockingDeque<>();
    H264Decoder mDecoder;
    int startIndex;
    long timeoutUs = 10000;
    H264Frame mH264Frame = new H264Frame();
    int seq = 0;
    int lastSeq = -1;
    private int count;
    private boolean isCount;
    private H264Player mH264Player;
    private final IFrameDataListener mIFrameDataListener;
    private long time;
    private boolean mStopFlag = false;
    private boolean isWait = false;
    private final H264Packet mPacket = new H264Packet(new IH264DataListener() {
        @Override
        public void onH264Frame(byte[] data) {
            if (!H264DecoderThread.this.isCount) {
                H264DecoderThread.this.isCount = true;
                H264DecoderThread.this.time = System.currentTimeMillis();
                H264DecoderThread.access$208(H264DecoderThread.this);
            } else {
                H264DecoderThread.access$208(H264DecoderThread.this);
                if (System.currentTimeMillis() - H264DecoderThread.this.time > 1000) {
                    H264DecoderThread.this.isCount = false;
                    if (H264DecoderThread.this.mIFrameDataListener != null) {
                        H264DecoderThread.this.mIFrameDataListener.onCountFrame(H264DecoderThread.this.count, 0, 0);
                    }
                    H264DecoderThread.this.count = 0;
                }
            }
            H264DecoderThread.this.cmdQuene.offer(data);
        }
    });

    public H264DecoderThread(H264Decoder h264Decoder, IFrameDataListener mIFrameDataListener) {
        this.startIndex = 0;
        this.mDecoder = h264Decoder;
        this.mIFrameDataListener = mIFrameDataListener;
        if (!X8Application.isLocalVideo) {
            this.startIndex = 14;
        }
    }

    public H264DecoderThread(H264Player mH264Player, IFrameDataListener mIFrameDataListener) {
        this.startIndex = 0;
        this.mH264Player = mH264Player;
        this.mIFrameDataListener = mIFrameDataListener;
        if (!X8Application.isLocalVideo) {
            this.startIndex = 14;
        }
    }

    static /* synthetic */ int access$208(H264DecoderThread x0) {
        int i = x0.count;
        x0.count = i + 1;
        return i;
    }

    @Override
    public void run() {
        if (this.mDecoder != null) {
            androidDecode();
        }
        if (this.mH264Player != null) {
            ffmpegDecode();
        }
    }

    public void androidDecode() {
    }

    public void ffmpegDecode() {
        byte[] data;
        while (!this.mStopFlag) {
            if (!this.cmdQuene.isEmpty() && (data = this.cmdQuene.poll()) != null) {
                this.mH264Player.decodeBuffer(data, data.length);
            }
        }
    }

    public void notityDecode(byte[] data) {
        if (!X8Application.Type2) {
            notityDecode1(data);
        } else {
            notityDecode12(data);
        }
    }

    private void notityDecode12(byte[] data) {
        this.mPacket.onPacket(data);
    }

    private void notityDecode1(byte[] data) {
        for (int i = this.startIndex; i < data.length; i++) {
            if (this.mH264Frame.parse(data[i])) {
                if (!this.isCount) {
                    this.isCount = true;
                    this.time = System.currentTimeMillis();
                    this.count++;
                } else {
                    this.count++;
                    if (System.currentTimeMillis() - this.time > 1000) {
                        this.isCount = false;
                        if (this.mIFrameDataListener != null) {
                            this.mIFrameDataListener.onCountFrame(this.count, 0, 0);
                        }
                        this.count = 0;
                    }
                }
                byte[] bytes = this.mH264Frame.getDataBuf();
                this.cmdQuene.offer(bytes);
            }
        }
    }

    public void packetData(byte[] data) {
        if (data.length > 14 && data[0] == 128) {
            this.seq = ((data[2] & 255) << 8) | (data[3] & 255);
            if (this.lastSeq == -1) {
                if ((data[12] & 255) == 124 && data[14] == 0 && data[15] == 0 && data[16] == 0 && data[17] == 1) {
                    this.mH264Frame.setData(data, 14, data.length - 14);
                    this.lastSeq = this.seq;
                }
            } else if (this.seq - this.lastSeq > 1) {
                this.lastSeq = this.seq;
                this.mH264Frame.resetIndex();
            } else if ((data[12] & 255) == 124) {
                if (data[14] == 0 && data[15] == 0 && data[16] == 0 && data[17] == 1) {
                    byte[] bytes = this.mH264Frame.getDataBuf();
                    this.cmdQuene.offer(bytes);
                    this.mH264Frame.setData(data, 14, data.length - 14);
                    if (this.seq == 32767) {
                        this.lastSeq = -1;
                    } else {
                        this.lastSeq = this.seq;
                    }
                }
            } else {
                this.mH264Frame.setData(data, 14, data.length - 14);
                this.lastSeq = this.seq;
            }
        }
    }

    public void decode(byte[] data) {
        this.cmdQuene.offer(data);
    }

    public void sendSignal() {
        synchronized (this.cmdQuene) {
            if (this.isWait) {
                Log.i("istep", "sendSignal notify " + this.cmdQuene.size());
                if (this.cmdQuene.size() > 10) {
                    Log.i("zdy", "sendSignal notify");
                    this.isWait = false;
                    this.cmdQuene.notify();
                } else if (this.cmdQuene.size() == 0) {
                    try {
                        Log.i("zdy", "sendSignal wait");
                        this.cmdQuene.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                this.isWait = true;
                try {
                    Log.i("zdy", "sendSignal wait");
                    this.cmdQuene.wait();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void release() {
        this.mStopFlag = true;
        interrupt();
    }

    @Override
    public void onH264Frame(byte[] data) {
        decode(data);
    }
}
