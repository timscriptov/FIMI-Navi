package com.fimi.soul.media.player.widget;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fimi.soul.media.player.FimiMediaPlayer;
import com.fimi.soul.media.player.IMediaPlayer;
import com.twitter.sdk.android.core.internal.VineCardUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class FimiVideoView extends FrameLayout implements FmMediaController.MediaPlayerControl {
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int[] s_allAspectRatio = {0, 1, 2, 3, 4, 5};
    Handler mHandler;
    IMediaPlayer.OnPreparedListener mPreparedListener;
    IRenderView.IRenderCallback mSHCallback;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    OutputStream outputStream;
    private String TAG;
    private int decodeType;
    private boolean isLog;
    private List<Integer> mAllRenders;
    private Context mAppContext;
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    private int mCurrentAspectRatio;
    private int mCurrentAspectRatioIndex;
    private int mCurrentBufferPercentage;
    private int mCurrentRender;
    private int mCurrentRenderIndex;
    private int mCurrentState;
    private IMediaPlayer.OnErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private IMediaPlayer.OnInfoListener mInfoListener;
    private IMediaController mMediaController;
    private IMediaPlayer mMediaPlayer;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IRenderView mRenderView;
    private int mSeekWhenPrepared;
    private int mSurfaceHeight;
    private IRenderView.ISurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;

    public FimiVideoView(Context context) {
        super(context);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.decodeType = 0;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.1
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                FimiVideoView.this.mVideoSarNum = mp.getVideoSarNum();
                FimiVideoView.this.mVideoSarDen = mp.getVideoSarDen();
                if (FimiVideoView.this.mVideoWidth != 0 && FimiVideoView.this.mVideoHeight != 0) {
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                        FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    }
                    FimiVideoView.this.requestLayout();
                }
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.2
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 2;
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onPrepared(FimiVideoView.this.mMediaPlayer);
                }
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.setEnabled(true);
                }
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                int seekToPosition = FimiVideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    FimiVideoView.this.seekTo(seekToPosition);
                }
                if (FimiVideoView.this.mVideoWidth == 0 || FimiVideoView.this.mVideoHeight == 0) {
                    if (FimiVideoView.this.mTargetState == 3) {
                        FimiVideoView.this.start();
                    }
                } else if (FimiVideoView.this.mRenderView != null) {
                    FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                    FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    if (!FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mSurfaceWidth == FimiVideoView.this.mVideoWidth && FimiVideoView.this.mSurfaceHeight == FimiVideoView.this.mVideoHeight)) {
                        if (FimiVideoView.this.mTargetState == 3) {
                            FimiVideoView.this.start();
                            if (FimiVideoView.this.mMediaController != null) {
                                FimiVideoView.this.mMediaController.show();
                            }
                        } else if (!FimiVideoView.this.isPlaying() && ((seekToPosition != 0 || FimiVideoView.this.getCurrentPosition() > 0) && FimiVideoView.this.mMediaController != null)) {
                            FimiVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.3
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.4
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                switch (arg1) {
                    case 10001:
                        FimiVideoView.this.mVideoRotationDegree = arg2;
                        Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                        if (FimiVideoView.this.mRenderView != null) {
                            FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                            return true;
                        }
                        return true;
                    default:
                        return true;
                }
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.5
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                Log.d(FimiVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
                FimiVideoView.this.mCurrentState = -1;
                FimiVideoView.this.mTargetState = -1;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 1);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
                return FimiVideoView.this.mOnErrorListener != null && FimiVideoView.this.mOnErrorListener.onError(FimiVideoView.this.mMediaPlayer, framework_err, impl_err);
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.6
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.7
            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceWidth = w;
                FimiVideoView.this.mSurfaceHeight = h;
                boolean isValidState = FimiVideoView.this.mTargetState == 3;
                boolean hasValidSize = !FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mVideoWidth == w && FimiVideoView.this.mVideoHeight == h);
                if (FimiVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (FimiVideoView.this.mSeekWhenPrepared != 0) {
                        FimiVideoView.this.seekTo(FimiVideoView.this.mSeekWhenPrepared);
                    }
                    FimiVideoView.this.start();
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                } else {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = null;
                FimiVideoView.this.releaseWithoutStop();
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.isLog = false;
        this.mHandler = new Handler() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.8
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        initVideoView(context);
    }

    public FimiVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.decodeType = 0;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.1
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                FimiVideoView.this.mVideoSarNum = mp.getVideoSarNum();
                FimiVideoView.this.mVideoSarDen = mp.getVideoSarDen();
                if (FimiVideoView.this.mVideoWidth != 0 && FimiVideoView.this.mVideoHeight != 0) {
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                        FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    }
                    FimiVideoView.this.requestLayout();
                }
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.2
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 2;
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onPrepared(FimiVideoView.this.mMediaPlayer);
                }
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.setEnabled(true);
                }
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                int seekToPosition = FimiVideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    FimiVideoView.this.seekTo(seekToPosition);
                }
                if (FimiVideoView.this.mVideoWidth == 0 || FimiVideoView.this.mVideoHeight == 0) {
                    if (FimiVideoView.this.mTargetState == 3) {
                        FimiVideoView.this.start();
                    }
                } else if (FimiVideoView.this.mRenderView != null) {
                    FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                    FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    if (!FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mSurfaceWidth == FimiVideoView.this.mVideoWidth && FimiVideoView.this.mSurfaceHeight == FimiVideoView.this.mVideoHeight)) {
                        if (FimiVideoView.this.mTargetState == 3) {
                            FimiVideoView.this.start();
                            if (FimiVideoView.this.mMediaController != null) {
                                FimiVideoView.this.mMediaController.show();
                            }
                        } else if (!FimiVideoView.this.isPlaying() && ((seekToPosition != 0 || FimiVideoView.this.getCurrentPosition() > 0) && FimiVideoView.this.mMediaController != null)) {
                            FimiVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.3
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.4
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                switch (arg1) {
                    case 10001:
                        FimiVideoView.this.mVideoRotationDegree = arg2;
                        Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                        if (FimiVideoView.this.mRenderView != null) {
                            FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                            return true;
                        }
                        return true;
                    default:
                        return true;
                }
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.5
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                Log.d(FimiVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
                FimiVideoView.this.mCurrentState = -1;
                FimiVideoView.this.mTargetState = -1;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 1);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
                return FimiVideoView.this.mOnErrorListener != null && FimiVideoView.this.mOnErrorListener.onError(FimiVideoView.this.mMediaPlayer, framework_err, impl_err);
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.6
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.7
            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceWidth = w;
                FimiVideoView.this.mSurfaceHeight = h;
                boolean isValidState = FimiVideoView.this.mTargetState == 3;
                boolean hasValidSize = !FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mVideoWidth == w && FimiVideoView.this.mVideoHeight == h);
                if (FimiVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (FimiVideoView.this.mSeekWhenPrepared != 0) {
                        FimiVideoView.this.seekTo(FimiVideoView.this.mSeekWhenPrepared);
                    }
                    FimiVideoView.this.start();
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                } else {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = null;
                FimiVideoView.this.releaseWithoutStop();
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.isLog = false;
        this.mHandler = new Handler() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.8
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        initVideoView(context);
    }

    public FimiVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.decodeType = 0;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.1
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                FimiVideoView.this.mVideoSarNum = mp.getVideoSarNum();
                FimiVideoView.this.mVideoSarDen = mp.getVideoSarDen();
                if (FimiVideoView.this.mVideoWidth != 0 && FimiVideoView.this.mVideoHeight != 0) {
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                        FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    }
                    FimiVideoView.this.requestLayout();
                }
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.2
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 2;
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onPrepared(FimiVideoView.this.mMediaPlayer);
                }
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.setEnabled(true);
                }
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                int seekToPosition = FimiVideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    FimiVideoView.this.seekTo(seekToPosition);
                }
                if (FimiVideoView.this.mVideoWidth == 0 || FimiVideoView.this.mVideoHeight == 0) {
                    if (FimiVideoView.this.mTargetState == 3) {
                        FimiVideoView.this.start();
                    }
                } else if (FimiVideoView.this.mRenderView != null) {
                    FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                    FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    if (!FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mSurfaceWidth == FimiVideoView.this.mVideoWidth && FimiVideoView.this.mSurfaceHeight == FimiVideoView.this.mVideoHeight)) {
                        if (FimiVideoView.this.mTargetState == 3) {
                            FimiVideoView.this.start();
                            if (FimiVideoView.this.mMediaController != null) {
                                FimiVideoView.this.mMediaController.show();
                            }
                        } else if (!FimiVideoView.this.isPlaying() && ((seekToPosition != 0 || FimiVideoView.this.getCurrentPosition() > 0) && FimiVideoView.this.mMediaController != null)) {
                            FimiVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.3
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.4
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                switch (arg1) {
                    case 10001:
                        FimiVideoView.this.mVideoRotationDegree = arg2;
                        Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                        if (FimiVideoView.this.mRenderView != null) {
                            FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                            return true;
                        }
                        return true;
                    default:
                        return true;
                }
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.5
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                Log.d(FimiVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
                FimiVideoView.this.mCurrentState = -1;
                FimiVideoView.this.mTargetState = -1;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 1);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
                return FimiVideoView.this.mOnErrorListener != null && FimiVideoView.this.mOnErrorListener.onError(FimiVideoView.this.mMediaPlayer, framework_err, impl_err);
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.6
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.7
            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceWidth = w;
                FimiVideoView.this.mSurfaceHeight = h;
                boolean isValidState = FimiVideoView.this.mTargetState == 3;
                boolean hasValidSize = !FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mVideoWidth == w && FimiVideoView.this.mVideoHeight == h);
                if (FimiVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (FimiVideoView.this.mSeekWhenPrepared != 0) {
                        FimiVideoView.this.seekTo(FimiVideoView.this.mSeekWhenPrepared);
                    }
                    FimiVideoView.this.start();
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                } else {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = null;
                FimiVideoView.this.releaseWithoutStop();
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.isLog = false;
        this.mHandler = new Handler() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.8
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        initVideoView(context);
    }

    public FimiVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.decodeType = 0;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.1
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnVideoSizeChangedListener
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                FimiVideoView.this.mVideoSarNum = mp.getVideoSarNum();
                FimiVideoView.this.mVideoSarDen = mp.getVideoSarDen();
                if (FimiVideoView.this.mVideoWidth != 0 && FimiVideoView.this.mVideoHeight != 0) {
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                        FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    }
                    FimiVideoView.this.requestLayout();
                }
            }
        };
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.2
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnPreparedListener
            public void onPrepared(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 2;
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onPrepared(FimiVideoView.this.mMediaPlayer);
                }
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.setEnabled(true);
                }
                FimiVideoView.this.mVideoWidth = mp.getVideoWidth();
                FimiVideoView.this.mVideoHeight = mp.getVideoHeight();
                int seekToPosition = FimiVideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    FimiVideoView.this.seekTo(seekToPosition);
                }
                if (FimiVideoView.this.mVideoWidth == 0 || FimiVideoView.this.mVideoHeight == 0) {
                    if (FimiVideoView.this.mTargetState == 3) {
                        FimiVideoView.this.start();
                    }
                } else if (FimiVideoView.this.mRenderView != null) {
                    FimiVideoView.this.mRenderView.setVideoSize(FimiVideoView.this.mVideoWidth, FimiVideoView.this.mVideoHeight);
                    FimiVideoView.this.mRenderView.setVideoSampleAspectRatio(FimiVideoView.this.mVideoSarNum, FimiVideoView.this.mVideoSarDen);
                    if (!FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mSurfaceWidth == FimiVideoView.this.mVideoWidth && FimiVideoView.this.mSurfaceHeight == FimiVideoView.this.mVideoHeight)) {
                        if (FimiVideoView.this.mTargetState == 3) {
                            FimiVideoView.this.start();
                            if (FimiVideoView.this.mMediaController != null) {
                                FimiVideoView.this.mMediaController.show();
                            }
                        } else if (!FimiVideoView.this.isPlaying() && ((seekToPosition != 0 || FimiVideoView.this.getCurrentPosition() > 0) && FimiVideoView.this.mMediaController != null)) {
                            FimiVideoView.this.mMediaController.show(0);
                        }
                    }
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.3
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnCompletionListener
            public void onCompletion(IMediaPlayer mp) {
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.4
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnInfoListener
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                switch (arg1) {
                    case 10001:
                        FimiVideoView.this.mVideoRotationDegree = arg2;
                        Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                        if (FimiVideoView.this.mRenderView != null) {
                            FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                            return true;
                        }
                        return true;
                    default:
                        return true;
                }
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.5
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnErrorListener
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                Log.d(FimiVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
                FimiVideoView.this.mCurrentState = -1;
                FimiVideoView.this.mTargetState = -1;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                String url = mp.getDataSource();
                if (url.contains("rtsp:")) {
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 1);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
                return FimiVideoView.this.mOnErrorListener != null && FimiVideoView.this.mOnErrorListener.onError(FimiVideoView.this.mMediaPlayer, framework_err, impl_err);
            }
        };
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.6
            @Override // com.fimi.soul.media.player.IMediaPlayer.OnBufferingUpdateListener
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.7
            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceWidth = w;
                FimiVideoView.this.mSurfaceHeight = h;
                boolean isValidState = FimiVideoView.this.mTargetState == 3;
                boolean hasValidSize = !FimiVideoView.this.mRenderView.shouldWaitForResize() || (FimiVideoView.this.mVideoWidth == w && FimiVideoView.this.mVideoHeight == h);
                if (FimiVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (FimiVideoView.this.mSeekWhenPrepared != 0) {
                        FimiVideoView.this.seekTo(FimiVideoView.this.mSeekWhenPrepared);
                    }
                    FimiVideoView.this.start();
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                } else {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                }
            }

            @Override // com.fimi.soul.media.player.widget.IRenderView.IRenderCallback
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = null;
                FimiVideoView.this.releaseWithoutStop();
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.isLog = false;
        this.mHandler = new Handler() { // from class: com.fimi.soul.media.player.widget.FimiVideoView.8
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        initVideoView(context);
    }

    public int getmCurrentState() {
        return this.mCurrentState;
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    public void setRenderView(IRenderView renderView) {
        if (this.mRenderView != null) {
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.setDisplay(null);
            }
            View renderUIView = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(renderUIView);
        }
        if (renderView != null) {
            this.mRenderView = renderView;
            renderView.setAspectRatio(this.mCurrentAspectRatio);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                renderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }
            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                renderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }
            View renderUIView2 = this.mRenderView.getView();
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2, 17);
            renderUIView2.setLayoutParams(lp);
            addView(renderUIView2);
            this.mRenderView.addRenderCallback(this.mSHCallback);
            this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    public void setZOrderMediaOverlay(boolean isM) {
        if (this.mRenderView instanceof SurfaceRenderView) {
            ((SurfaceRenderView) this.mRenderView).setZOrderMediaOverlay(isM);
        }
    }

    public void setHideSurfaceView(boolean isVisiable) {
        if (this.mRenderView instanceof SurfaceRenderView) {
            ((SurfaceRenderView) this.mRenderView).getHolder().setFormat(isVisiable ? -2 : 4);
        }
    }

    public void setRender(int render) {
        switch (render) {
            case 0:
                setRenderView(null);
                return;
            case 1:
                setRenderView(new SurfaceRenderView(getContext()));
                return;
            case 2:
                TextureRenderView renderView = new TextureRenderView(getContext());
                if (this.mMediaPlayer != null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                    renderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
                    renderView.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
                    renderView.setAspectRatio(this.mCurrentAspectRatio);
                }
                setRenderView(renderView);
                return;
            default:
                Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", Integer.valueOf(render)));
                return;
        }
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo(this.decodeType);
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
            AudioManager am = (AudioManager) this.mAppContext.getSystemService("audio");
            am.abandonAudioFocus(null);
        }
    }

    public void setDecodeType(int decodeType) {
        this.decodeType = decodeType;
    }

    public void openVideo(int decodeType) {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            release(false);
            AudioManager am = (AudioManager) this.mAppContext.getSystemService("audio");
            am.requestAudioFocus(null, 3, 1);
            FimiMediaPlayer ijkMediaPlayer = null;
            try {
                if (this.mUri != null) {
                    ijkMediaPlayer = FimiMediaPlayer.getIntance();
                    FimiMediaPlayer.native_setLogLevel(3);
                    ijkMediaPlayer.setOption(4, "mediacodec", decodeType);
                    ijkMediaPlayer.setOption(4, "framedrop", 20L);
                    ijkMediaPlayer.setOption(4, "start-on-prepared", 0L);
                    ijkMediaPlayer.setOption(1, "http-detect-range-support", 0L);
                    ijkMediaPlayer.setOption(2, "skip_loop_filter", 48L);
                    ijkMediaPlayer.setOption(1, "analyzeduration", "20000");
                    ijkMediaPlayer.setOption(1, "probsize", "4096");
                }
                this.mMediaPlayer = ijkMediaPlayer;
                getContext();
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mCurrentBufferPercentage = 0;
                if (Build.VERSION.SDK_INT > 14) {
                    this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
                } else {
                    this.mMediaPlayer.setDataSource(this.mUri.toString());
                }
                bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync(2, 2);
                this.mCurrentState = 1;
                attachMediaController();
            } catch (IOException ex) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException ex2) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex2);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setMediaController(IMediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            this.mMediaController.setMediaPlayer(this);
            View anchorView = getParent() instanceof View ? (View) getParent() : this;
            this.mMediaController.setAnchorView(anchorView);
            this.mMediaController.setEnabled(isInPlaybackState());
        }
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    public void bindSurfaceHolder(IMediaPlayer mp, IRenderView.ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setDisplay(null);
            } else {
                holder.bindToMediaPlayer(mp);
            }
        }
    }

    public void releaseWithoutStop() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setDisplay(null);
        }
    }

    public void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
            AudioManager am = (AudioManager) this.mAppContext.getSystemService("audio");
            am.abandonAudioFocus(null);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
            return false;
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
            return false;
        }
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 164 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == 126) {
                if (this.mMediaPlayer.isPlaying()) {
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == 86 || keyCode == 127) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                return true;
            } else {
                toggleMediaControlsVisiblity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public void start() {
        if (this.mCurrentState == 5) {
            setVideoURI(this.mUri);
        }
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public void pause() {
        Log.d(VineCardUtils.PLAYER_CARD, "pause");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.pause();
            if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.pause();
                this.mCurrentState = 4;
            }
            this.mTargetState = 4;
        }
    }

    public void suspend() {
        Log.d("Good", "suspend");
        release(false);
    }

    public void resume() {
        Log.d("Good", "resume");
        openVideo(this.decodeType);
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public int getDuration() {
        Log.d(VineCardUtils.PLAYER_CARD, "getDuration");
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public void seekTo(int msec) {
        Log.d(VineCardUtils.PLAYER_CARD, "seekto");
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(msec);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        Log.d(VineCardUtils.PLAYER_CARD, "onFinishInflate");
        super.onFinishInflate();
        if (isPlaying()) {
            pause();
        }
    }

    private boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public boolean canPause() {
        return this.mCanPause;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    @Override // com.fimi.soul.media.player.widget.FmMediaController.MediaPlayerControl
    public int getAudioSessionId() {
        return 0;
    }

    public int toggleAspectRatio() {
        this.mCurrentAspectRatioIndex++;
        this.mCurrentAspectRatioIndex %= s_allAspectRatio.length;
        this.mCurrentAspectRatio = 1;
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
        }
        return this.mCurrentAspectRatio;
    }

    public int toggleAspectRatioPOi() {
        this.mCurrentAspectRatioIndex++;
        this.mCurrentAspectRatioIndex %= s_allAspectRatio.length;
        this.mCurrentAspectRatio = 3;
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
        }
        return this.mCurrentAspectRatio;
    }

    private void initRenders() {
        this.mAllRenders.clear();
        this.mAllRenders.add(1);
        if (this.mAllRenders.isEmpty()) {
            this.mAllRenders.add(1);
        }
        this.mCurrentRender = this.mAllRenders.get(this.mCurrentRenderIndex).intValue();
        setRender(this.mCurrentRender);
    }

    public int toggleRender() {
        this.mCurrentRenderIndex++;
        this.mCurrentRenderIndex %= this.mAllRenders.size();
        this.mCurrentRender = this.mAllRenders.get(this.mCurrentRenderIndex).intValue();
        setRender(this.mCurrentRender);
        return this.mCurrentRender;
    }

    public void onPreviewInputNetData(byte[] data, int nPos, int len) {
        if (this.mMediaPlayer != null) {
            if (data != null) {
                this.mMediaPlayer.PlayerPreviewInputNetData(data, nPos, len);
            } else {
                Toast.makeText(getContext(), "FimiView onPreviewInputNetData data = null", 1).show();
            }
        }
    }

    public String getLogString() {
        if (this.mMediaPlayer == null) {
            return "";
        }
        int[] network_info_int = new int[64];
        this.mMediaPlayer.PlayerGetNetworkInfo(network_info_int);
        String log = network_info_int[0] + " (kbps)\n" + network_info_int[1] + " (receive)\n" + network_info_int[2] + " (lose) \n " + network_info_int[3] + " (35ms)\n" + network_info_int[4] + " (40ms)\n";
        return log;
    }
}
