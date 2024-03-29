package com.fimi.album.presenter;

import android.os.Handler;
import android.os.Message;

import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IVideoPlayer;


public class VideoPlayerPresneter implements Handler.Callback {
    private static final int TIME = 1;
    private final IVideoPlayer mIVideoPlayer;
    private final Handler mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);

    public VideoPlayerPresneter(IVideoPlayer mIVideoPlayer) {
        this.mIVideoPlayer = mIVideoPlayer;
    }

    public void startPlay() {
        this.mainHandler.sendEmptyMessage(1);
    }

    public void removeCallBack() {
        this.mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            if (this.mIVideoPlayer != null) {
                this.mIVideoPlayer.timeFunction();
            }
            this.mainHandler.sendEmptyMessageDelayed(1, 1000L);
        }
        return true;
    }
}
