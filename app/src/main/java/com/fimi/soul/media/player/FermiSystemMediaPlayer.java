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

public class FermiSystemMediaPlayer implements IFermiMediaPlayer, SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener {
    private final Context context;
    private final List<OnPlayerStateChangedListener> onPlayerStateChangedListenerArray = new ArrayList();
    private final Timer timer = new Timer();
    private final Handler handler = new InnerHandler();
    private String dataSourceUrl;
    private boolean isAutoPlay;
    private OnMediaSizeChangedListener onMediaSizeChangedListener;
    private OnProgressChangedListener onProgressChangedListener;
    private SeekBar seekBar;
    private SurfaceView surfaceView;
    private MediaPlayer player = null;
    private int startPosition = 0;
    private boolean ischanging = false;
    private final TimerTask timerTask = new TimerTask() {
        @Override
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

    @Override
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

    @Override
    public void prepare() {
        if (this.player != null) {
            try {
                this.player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pause() {
        if (this.player != null && this.player.isPlaying()) {
            this.player.pause();
            onPlayerStateChange(IFermiMediaPlayer.FermiPlyaerState.Pause);
        }
    }

    @Override
    public void stop() {
        if (this.player != null) {
            this.player.stop();
            onPlayerStateChange(IFermiMediaPlayer.FermiPlyaerState.Stop);
        }
    }

    @Override
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

    @Override
    public void setMediaUri(String uri) {
        setMediaUri(uri, null, null);
    }

    @Override
    public void setSurfaceView(SurfaceView surfaceView) {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void setSeekBar(SeekBar sb) {
        this.seekBar = sb;
        sb.setOnSeekBarChangeListener(this);
    }

    @Override
    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.onProgressChangedListener = listener;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.player = new MediaPlayer();
        this.player.setAudioStreamType(3);
        this.player.setDisplay(surfaceHolder);
        this.player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                FermiSystemMediaPlayer.this.ischanging = false;
                mp.start();
            }
        });
        try {
            this.player.setDataSource(this.dataSourceUrl);
            this.player.prepare();
            this.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
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
            this.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
            this.player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
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

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (this.player.isPlaying()) {
            this.player.stop();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
        if (fromUser) {
            this.player.seekTo(progress);
            if (this.onProgressChangedListener != null) {
                this.onProgressChangedListener.onProgressChanged(progress, this.player.getDuration());
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
        this.ischanging = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
    }

    @Override
    public boolean isAutoPlay() {
        return this.isAutoPlay;
    }

    @Override
    public void setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    @Override
    public boolean isPlaying() {
        if (this.player != null) {
            return this.player.isPlaying();
        }
        return false;
    }

    @Override
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

    @Override
    public int getPosition() {
        return this.player.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return this.player.getDuration();
    }

    @Override
    public void seekTo(long position) {
        this.player.seekTo((int) position);
    }

    @Override
    public long getCurrentPosition() {
        return this.player.getCurrentPosition();
    }

    @Override
    public void setPlayPosition(int position) {
        this.startPosition = position;
    }

    @Override
    public void setOnMediaSizeChangedListener(OnMediaSizeChangedListener listener) {
        this.onMediaSizeChangedListener = listener;
    }


    class InnerHandler extends Handler {
        InnerHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            if (FermiSystemMediaPlayer.this.onProgressChangedListener != null) {
                FermiSystemMediaPlayer.this.onProgressChangedListener.onProgressChanged(msg.arg1, msg.arg2);
            }
            super.handleMessage(msg);
        }
    }
}
