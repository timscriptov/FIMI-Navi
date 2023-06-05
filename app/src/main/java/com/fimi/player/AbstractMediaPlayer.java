package com.fimi.player;


public abstract class AbstractMediaPlayer implements IMediaPlayer {
    private IMediaPlayer.MediaQualityListener mMediaQualityListener;
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnLiveVideoListener mOnLiveVideoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    @Override
    public void setMediaQualityListener(IMediaPlayer.MediaQualityListener listener) {
        this.mMediaQualityListener = listener;
    }

    @Override
    public final void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    @Override
    public final void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    @Override
    public final void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    @Override
    public final void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    @Override
    public final void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public void showMediaQuality(int arg1, int arg2) {
        if (this.mMediaQualityListener != null) {
            this.mMediaQualityListener.showMediaQuality(arg1, arg2);
        }
    }

    public final void setmMediaQualityListener(IMediaPlayer.MediaQualityListener listener) {
        this.mMediaQualityListener = listener;
    }

    @Override
    public final void setOnErrorListener(IMediaPlayer.OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    @Override
    public final void setOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public void resetListeners() {
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mOnLiveVideoListener = null;
    }

    public final void notifyOnPrepared() {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(this);
        }
    }

    public final void notifyOnCompletion(int code) {
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this, code);
        }
    }

    public final void notifyOnBufferingUpdate(int percent) {
        if (this.mOnBufferingUpdateListener != null) {
            this.mOnBufferingUpdateListener.onBufferingUpdate(this, percent);
        }
    }

    public final void notifyOnSeekComplete() {
        if (this.mOnSeekCompleteListener != null) {
            this.mOnSeekCompleteListener.onSeekComplete(this);
        }
    }

    public final void notifyOnVideoSizeChanged(int width, int height, int sarNum, int sarDen) {
        if (this.mOnVideoSizeChangedListener != null) {
            this.mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height, sarNum, sarDen);
        }
    }

    public final boolean notifyOnError(int what, int extra) {
        if (this.mOnErrorListener != null) {
            return this.mOnErrorListener.onError(this, what, extra);
        }
        return false;
    }

    public final boolean notifyOnInfo(int what, int extra) {
        if (this.mOnInfoListener != null) {
            return this.mOnInfoListener.onInfo(this, what, extra);
        }
        return false;
    }

    @Override
    public void setOnLiveVideoListener(IMediaPlayer.OnLiveVideoListener listener) {
        this.mOnLiveVideoListener = listener;
    }

    public void notifyRtmpStatusCBOnLiveVideoListener(int type, int status1, int status2) {
        if (this.mOnLiveVideoListener != null) {
            this.mOnLiveVideoListener.onRtmpStatusCB(type, status1, status2);
        }
    }

    public final void notifyOnStartStream() {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onStartStream();
        }
    }
}
