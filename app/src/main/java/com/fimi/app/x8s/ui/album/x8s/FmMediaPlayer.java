package com.fimi.app.x8s.ui.album.x8s;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.fimi.app.x8s.widget.videoview.X8FmMediaInfo;
import com.fimi.player.IMediaPlayer;
import com.fimi.player.widget.FimiVideoView;

/* loaded from: classes.dex */
public class FmMediaPlayer implements IFmMediaPlayer, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener {
    public static final String FM_MEDIA_INFO = "FmMediaPlayer_FmMediaInfo";
    public final int STATE_IDLE = 0;
    public final int STATE_PLAY = 1;
    public final int STATE_PAUSE = 2;
    public final int STATE_COMPLETE = 3;
    public final int STATE_DESTROY = 4;
    String path;
    private X8FmMediaInfo info;
    private OnActivityHander listener;
    private FimiVideoView mainVideo;
    private CustomMediaContoller mediaController;
    private int state = 0;

    public FmMediaPlayer(FimiVideoView mainVideo, X8FmMediaInfo info, OnActivityHander listener) {
        this.path = "http://flv2.bn.netease.com/tvmrepo/2016/4/G/O/EBKQOA8GO/SD/EBKQOA8GO-mobile.mp4";
        this.mainVideo = mainVideo;
        this.listener = listener;
        this.info = info;
        this.path = info.getPath();
    }

    public void initFmPlayer(Context context, View view) {
        this.mainVideo.setVideoPath(this.path);
        initAction();
        CustomMediaContoller mediaController = new CustomMediaContoller(context, view, this);
        mediaController.setNameAndDuration(this.info.getName(), this.info.getDuration());
        this.mediaController = mediaController;
    }

    public void setShowContoller(boolean isShowContoller) {
        this.mediaController.setShowContoller(isShowContoller);
    }

    public void startPlay() {
        this.mediaController.onPlay();
    }

    public void initAction() {
        this.mainVideo.setOnCompletionListener(this);
        this.mainVideo.setOnPreparedListener(this);
        this.mainVideo.setOnErrorListener(this);
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.IFmMediaPlayer
    public void seekTo(int m) {
        this.mainVideo.seekTo(m);
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.IFmMediaPlayer
    public boolean isPlaying() {
        return this.mainVideo.isPlaying();
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.IFmMediaPlayer
    public long getCurrentPosition() {
        return this.mainVideo.getCurrentPosition();
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.IFmMediaPlayer
    public long getDuration() {
        return this.mainVideo.getDuration();
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.IFmMediaPlayer
    public void start() {
        this.mainVideo.start();
        if (this.state != 0) {
            this.mediaController.startSeekbar();
        }
        this.state = 1;
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.IFmMediaPlayer
    public void pause() {
        this.mainVideo.pause();
        this.state = 2;
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.IFmMediaPlayer
    public void onDestroy() {
        if (this.state != 4) {
            this.state = 4;
            this.mediaController.reset();
            this.mainVideo.release(true);
            this.listener.onBack();
        }
    }

    @Override // com.fimi.player.IMediaPlayer.OnPreparedListener
    public void onPrepared(IMediaPlayer mp) {
    }

    @Override // com.fimi.player.IMediaPlayer.OnPreparedListener
    public void onStartStream() {
        this.mediaController.startSeekbar();
    }

    @Override // com.fimi.player.IMediaPlayer.OnErrorListener
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        Log.i("istep", "onError");
        this.mediaController.onError();
        return false;
    }

    @Override // com.fimi.player.IMediaPlayer.OnCompletionListener
    public void onCompletion(IMediaPlayer mp, int code) {
        this.state = 3;
        this.mediaController.reset();
    }

    /* loaded from: classes.dex */
    public interface OnActivityHander {
        void onBack();
    }
}
