package com.fimi.soul.media.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FermiSystemMediaPlayer implements IFermiMediaPlayer, SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener {
    private Context context;
    private String dataSourceUrl;
    private boolean isAutoPlay;
    private OnMediaSizeChangedListener onMediaSizeChangedListener;
    private OnProgressChangedListener onProgressChangedListener;
    private SeekBar seekBar;
    private SurfaceView surfaceView;
    private MediaPlayer player = null;
    private int startPosition = 0;
    private boolean ischanging = false;
    private List<OnPlayerStateChangedListener> onPlayerStateChangedListenerArray = new ArrayList();
    private Timer timer = new Timer();
    private Handler handler = new InnerHandler();
    private TimerTask timerTask = new TimerTask() { // from class: com.fimi.soul.media.player.FermiSystemMediaPlayer.1
        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (!FermiSystemMediaPlayer.this.ischanging && FermiSystemMediaPlayer.this.player != null && FermiSystemMediaPlayer.this.seekBar != null && !FermiSystemMediaPlayer.this.ischanging) {
                FermiSystemMediaPlayer.this.seekBar.setProgress(FermiSystemMediaPlayer.this.player.getCurrentPosition());
                Message msg = new Message();
                msg.arg1 = FermiSystemMediaPlayer.this.player.getCurrentPosition();
                msg.arg2 = FermiSystemMediaPlayer.this.player.getDuration();
                FermiSystemMediaPlayer.this.handler.sendMessage(msg);
            }
        }
    };

    public FermiSystemMediaPlayer(Context context) {
        this.context = context;
        this.timer.schedule(this.timerTask, 0L, 1000L);
    }

    private Context getContext() {
        return this.context;
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void play() {
        if (this.player != null) {
            try {
                if (this.player != null && !this.player.isPlaying()) {
                    this.player.stop();
                }
                this.player.prepare();
                if (!isAutoPlay()) {
                    this.player.start();
                    onPlayerStateChange(IFermiMediaPlayer.FermiPlyaerState.Playing);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void prepare() {
        if (this.player != null) {
            try {
                this.player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void pause() {
        if (this.player != null && this.player.isPlaying()) {
            this.player.pause();
            onPlayerStateChange(IFermiMediaPlayer.FermiPlyaerState.Pause);
        }
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void stop() {
        if (this.player != null) {
            this.player.stop();
            onPlayerStateChange(IFermiMediaPlayer.FermiPlyaerState.Stop);
        }
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setMediaUri(String uri, String user, String password) {
        this.dataSourceUrl = uri;
        if (this.player != null) {
            try {
                this.player.setDataSource(this.dataSourceUrl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                e3.printStackTrace();
            } catch (SecurityException e4) {
                e4.printStackTrace();
            }
        }
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setMediaUri(String uri) {
        setMediaUri(uri, null, null);
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setSurfaceView(SurfaceView surfaceView) {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setSeekBar(SeekBar sb) {
        this.seekBar = sb;
        sb.setOnSeekBarChangeListener(this);
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.onProgressChangedListener = listener;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.player = new MediaPlayer();
        this.player.setAudioStreamType(3);
        this.player.setDisplay(surfaceHolder);
        this.player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() { // from class: com.fimi.soul.media.player.FermiSystemMediaPlayer.2
            @Override // android.media.MediaPlayer.OnSeekCompleteListener
            public void onSeekComplete(MediaPlayer mp) {
                FermiSystemMediaPlayer.this.ischanging = false;
                mp.start();
            }
        });
        try {
            this.player.setDataSource(this.dataSourceUrl);
            this.player.prepare();
            this.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.fimi.soul.media.player.FermiSystemMediaPlayer.3
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mp) {
                    if (FermiSystemMediaPlayer.this.seekBar != null) {
                        FermiSystemMediaPlayer.this.seekBar.setMax(mp.getDuration());
                    }
                    if (FermiSystemMediaPlayer.this.isAutoPlay) {
                        FermiSystemMediaPlayer.this.seekTo(FermiSystemMediaPlayer.this.startPosition);
                        mp.start();
                        FermiSystemMediaPlayer.this.onPlayerStateChange(IFermiMediaPlayer.FermiPlyaerState.Playing);
                    }
                }
            });
            this.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.fimi.soul.media.player.FermiSystemMediaPlayer.4
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mp) {
                }
            });
            this.player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.fimi.soul.media.player.FermiSystemMediaPlayer.5
                @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    if (FermiSystemMediaPlayer.this.onMediaSizeChangedListener != null) {
                        Log.d("Good", width + ":" + height);
                        FermiSystemMediaPlayer.this.onMediaSizeChangedListener.onMediaSizeChanged(FermiSystemMediaPlayer.this, width, height);
                    }
                }
            });
            this.player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (this.player.isPlaying()) {
            this.player.stop();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
        if (fromUser) {
            this.player.seekTo(progress);
            if (this.onProgressChangedListener != null) {
                this.onProgressChangedListener.onProgressChanged(progress, this.player.getDuration());
            }
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar arg0) {
        this.ischanging = true;
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar arg0) {
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public boolean isAutoPlay() {
        return this.isAutoPlay;
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public boolean isPlaying() {
        if (this.player != null) {
            return this.player.isPlaying();
        }
        return false;
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void addOnPlayerStateChangedListener(OnPlayerStateChangedListener listener) {
        if (listener != null) {
            this.onPlayerStateChangedListenerArray.add(listener);
        }
    }

    public void onPlayerStateChange(IFermiMediaPlayer.FermiPlyaerState state) {
        if (this.onPlayerStateChangedListenerArray.size() > 0) {
            for (OnPlayerStateChangedListener item : this.onPlayerStateChangedListenerArray) {
                item.OnPlayerStateChanged(state, this);
            }
        }
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public int getPosition() {
        return this.player.getCurrentPosition();
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public long getDuration() {
        return this.player.getDuration();
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void seekTo(long position) {
        this.player.seekTo((int) position);
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public long getCurrentPosition() {
        return this.player.getCurrentPosition();
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setPlayPosition(int position) {
        this.startPosition = position;
    }

    @Override // com.fimi.soul.media.player.IFermiMediaPlayer
    public void setOnMediaSizeChangedListener(OnMediaSizeChangedListener listener) {
        this.onMediaSizeChangedListener = listener;
    }

    /* loaded from: classes.dex */
    class InnerHandler extends Handler {
        InnerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (FermiSystemMediaPlayer.this.onProgressChangedListener != null) {
                FermiSystemMediaPlayer.this.onProgressChangedListener.onProgressChanged(msg.arg1, msg.arg2);
            }
            super.handleMessage(msg);
        }
    }
}
