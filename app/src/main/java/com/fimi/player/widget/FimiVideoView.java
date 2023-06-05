package com.fimi.player.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.fimi.player.FimiMediaPlayer;
import com.fimi.player.IMediaPlayer;
import com.fimi.player.MediaPlayerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


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
    public static int Video_Decode_Type_Soft = 0;
    public IMediaPlayer.OnLiveVideoListener liveVideoListener;
    IMediaPlayer.OnPreparedListener mPreparedListener;
    IRenderView.IRenderCallback mSHCallback;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private final String TAG;
    private int decodeType;
    private String liveUrl;
    private final List<Integer> mAllRenders;
    private Context mAppContext;
    private final IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;
    private final boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private final IMediaPlayer.OnCompletionListener mCompletionListener;
    private int mCurrentAspectRatio;
    private int mCurrentAspectRatioIndex;
    private int mCurrentBufferPercentage;
    private int mCurrentRender;
    private int mCurrentRenderIndex;
    private int mCurrentState;
    private final IMediaPlayer.OnErrorListener mErrorListener;
    private OnFimiFileCallBackListener mFimiFileCallBackListener;
    private Map<String, String> mHeaders;
    private final IMediaPlayer.OnInfoListener mInfoListener;
    private IMediaController mMediaController;
    private IMediaPlayer mMediaPlayer;
    private IMediaPlayer.MediaQualityListener mMediaQualityListener;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private OnFimiPreviewCallBackListener mPreviewCallBackListener;
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
    private Paint paint;

    public FimiVideoView(Context context) {
        super(context);
        this.TAG = "IjkVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.decodeType = 0;
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
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
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            @Override
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
                if (url.contains("rtsp:") && FimiVideoView.this.getHandler() != null) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }

            @Override
            public void onStartStream() {
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onStartStream();
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp, int code) {
                Log.d("moweiru", "mCurrentState");
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    Log.d("moweiru", "mCurrentState  onCompletion");
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer, code);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onCompletion(code);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                if (arg1 == 10001) {
                    FimiVideoView.this.mVideoRotationDegree = arg2;
                    Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                        return true;
                    }
                    return true;
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            @Override
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
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() {
            @Override
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
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                    FimiVideoView.this.start();
                }
            }

            @Override
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                if (FimiVideoView.this.mCurrentState == 0) {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                    if (FimiVideoView.this.mMediaPlayer != null) {
                        FimiVideoView.this.mMediaPlayer.setPreview(1);
                    }
                } else if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(1);
                }
                if (FimiVideoView.this.mFimiFileCallBackListener != null) {
                    FimiVideoView.this.mFimiFileCallBackListener.onSurfaceState(1);
                }
            }

            @Override
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(0);
                }
                FimiVideoView.this.mSurfaceHolder = null;
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(0);
                }
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.liveVideoListener = new IMediaPlayer.OnLiveVideoListener() {
            @Override
            public void onRtmpStatusCB(int type, int status1, int status2) {
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
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
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
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            @Override
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
                if (url.contains("rtsp:") && FimiVideoView.this.getHandler() != null) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }

            @Override
            public void onStartStream() {
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onStartStream();
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp, int code) {
                Log.d("moweiru", "mCurrentState");
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    Log.d("moweiru", "mCurrentState  onCompletion");
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer, code);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onCompletion(code);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                if (arg1 == 10001) {
                    FimiVideoView.this.mVideoRotationDegree = arg2;
                    Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                        return true;
                    }
                    return true;
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            @Override
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
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() {
            @Override
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
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                    FimiVideoView.this.start();
                }
            }

            @Override
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                if (FimiVideoView.this.mCurrentState == 0) {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                    if (FimiVideoView.this.mMediaPlayer != null) {
                        FimiVideoView.this.mMediaPlayer.setPreview(1);
                    }
                } else if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(1);
                }
                if (FimiVideoView.this.mFimiFileCallBackListener != null) {
                    FimiVideoView.this.mFimiFileCallBackListener.onSurfaceState(1);
                }
            }

            @Override
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(0);
                }
                FimiVideoView.this.mSurfaceHolder = null;
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(0);
                }
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.liveVideoListener = new IMediaPlayer.OnLiveVideoListener() {
            @Override
            public void onRtmpStatusCB(int type, int status1, int status2) {
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
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
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
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            @Override
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
                if (url.contains("rtsp:") && FimiVideoView.this.getHandler() != null) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }

            @Override
            public void onStartStream() {
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onStartStream();
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp, int code) {
                Log.d("moweiru", "mCurrentState");
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    Log.d("moweiru", "mCurrentState  onCompletion");
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer, code);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onCompletion(code);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                if (arg1 == 10001) {
                    FimiVideoView.this.mVideoRotationDegree = arg2;
                    Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                        return true;
                    }
                    return true;
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            @Override
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
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() {
            @Override
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
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                    FimiVideoView.this.start();
                }
            }

            @Override
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                if (FimiVideoView.this.mCurrentState == 0) {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                    if (FimiVideoView.this.mMediaPlayer != null) {
                        FimiVideoView.this.mMediaPlayer.setPreview(1);
                    }
                } else if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(1);
                }
                if (FimiVideoView.this.mFimiFileCallBackListener != null) {
                    FimiVideoView.this.mFimiFileCallBackListener.onSurfaceState(1);
                }
            }

            @Override
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(0);
                }
                FimiVideoView.this.mSurfaceHolder = null;
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(0);
                }
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.liveVideoListener = new IMediaPlayer.OnLiveVideoListener() {
            @Override
            public void onRtmpStatusCB(int type, int status1, int status2) {
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
        this.mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
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
        this.mPreparedListener = new IMediaPlayer.OnPreparedListener() {
            @Override
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
                if (url.contains("rtsp:") && FimiVideoView.this.getHandler() != null) {
                    FimiVideoView.this.getHandler().sendEmptyMessage(0);
                    Intent resetRstp = new Intent("resetRTSP");
                    resetRstp.putExtra("rtsp", 0);
                    FimiVideoView.this.mAppContext.sendBroadcast(resetRstp);
                }
            }

            @Override
            public void onStartStream() {
                if (FimiVideoView.this.mOnPreparedListener != null) {
                    FimiVideoView.this.mOnPreparedListener.onStartStream();
                }
            }
        };
        this.mCompletionListener = new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp, int code) {
                Log.d("moweiru", "mCurrentState");
                FimiVideoView.this.mCurrentState = 5;
                FimiVideoView.this.mTargetState = 5;
                if (FimiVideoView.this.mMediaController != null) {
                    FimiVideoView.this.mMediaController.hide();
                }
                if (FimiVideoView.this.mOnCompletionListener != null) {
                    Log.d("moweiru", "mCurrentState  onCompletion");
                    FimiVideoView.this.mOnCompletionListener.onCompletion(FimiVideoView.this.mMediaPlayer, code);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onCompletion(code);
                }
            }
        };
        this.mInfoListener = new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                if (FimiVideoView.this.mOnInfoListener != null) {
                    FimiVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
                }
                if (arg1 == 10001) {
                    FimiVideoView.this.mVideoRotationDegree = arg2;
                    Log.d(FimiVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    if (FimiVideoView.this.mRenderView != null) {
                        FimiVideoView.this.mRenderView.setVideoRotation(arg2);
                        return true;
                    }
                    return true;
                }
                return true;
            }
        };
        this.mErrorListener = new IMediaPlayer.OnErrorListener() {
            @Override
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
        this.mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                FimiVideoView.this.mCurrentBufferPercentage = percent;
            }
        };
        this.mSHCallback = new IRenderView.IRenderCallback() {
            @Override
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
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                    FimiVideoView.this.start();
                }
            }

            @Override
            public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                    return;
                }
                FimiVideoView.this.mSurfaceHolder = holder;
                FimiVideoView.this.bindSurfaceHolder(FimiVideoView.this.mMediaPlayer, holder);
                if (FimiVideoView.this.mCurrentState == 0) {
                    FimiVideoView.this.openVideo(FimiVideoView.this.decodeType);
                    if (FimiVideoView.this.mMediaPlayer != null) {
                        FimiVideoView.this.mMediaPlayer.setPreview(1);
                    }
                } else if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(1);
                }
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(1);
                }
                if (FimiVideoView.this.mFimiFileCallBackListener != null) {
                    FimiVideoView.this.mFimiFileCallBackListener.onSurfaceState(1);
                }
            }

            @Override
            public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
                if (holder.getRenderView() != FimiVideoView.this.mRenderView) {
                    Log.e(FimiVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                    return;
                }
                if (FimiVideoView.this.mMediaPlayer != null) {
                    FimiVideoView.this.mMediaPlayer.setPreview(0);
                }
                FimiVideoView.this.mSurfaceHolder = null;
                if (FimiVideoView.this.mPreviewCallBackListener != null) {
                    FimiVideoView.this.mPreviewCallBackListener.onSurfaceState(0);
                }
            }
        };
        this.mCurrentAspectRatioIndex = 0;
        this.mCurrentAspectRatio = s_allAspectRatio[0];
        this.mAllRenders = new ArrayList();
        this.mCurrentRenderIndex = 0;
        this.mCurrentRender = 0;
        this.liveVideoListener = new IMediaPlayer.OnLiveVideoListener() {
            @Override
            public void onRtmpStatusCB(int type, int status1, int status2) {
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
        }
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void clearUri() {
        this.mUri = null;
    }

    public void setUri(String uri) {
        this.mUri = Uri.parse(uri);
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        if (this.mCurrentState == 0 && this.mMediaPlayer == null) {
            openVideo(this.decodeType);
        }
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

    public synchronized void restart() {
        if (this.mMediaPlayer != null) {
            release(false);
        }
        openVideo(this.decodeType);
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setPreview(1);
            start();
        }
    }

    public void startPreview() {
        if (this.mMediaPlayer != null && this.mSurfaceHolder != null) {
            this.mMediaPlayer.setPreview(1);
            start();
        }
    }

    public void startFlightVideo(String path, int count, int time) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.recStart(path, count, time);
        }
    }

    public void stopFlightVideo() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.recStop();
        }
    }

    public void saveEmergencyFlightVideo(String path, int count) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.recEmergencySave(path, count);
        }
    }

    public void stopPlay() {
        if (this.mMediaPlayer != null) {
            release(true);
        }
    }

    public void startPlay() {
        if (this.mMediaPlayer == null) {
            openVideo(this.decodeType);
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.setPreview(1);
                start();
            }
        }
    }

    public void openVideo(int decodeType) {
        if (this.mUri != null && this.mSurfaceHolder != null && this.mMediaPlayer == null) {
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
                    ijkMediaPlayer.setOption(2, "skip_loop_filter", 0L);
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
                this.mMediaPlayer.setMediaQualityListener(this.mMediaQualityListener);
                this.mCurrentBufferPercentage = 0;
                this.mMediaPlayer.setOnLiveVideoListener(this.liveVideoListener);
                if (Build.VERSION.SDK_INT > 14) {
                    this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
                } else {
                    this.mMediaPlayer.setDataSource(this.mUri.toString());
                }
                bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
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

    public void setmMediaQualityListener(IMediaPlayer.MediaQualityListener mMediaQualityListener) {
        this.mMediaQualityListener = mMediaQualityListener;
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

    public void drawBackground(IRenderView.ISurfaceHolder holder) {
        if (this.paint == null) {
            this.paint = new Paint();
        }
        Canvas canvas = holder.getSurfaceHolder().lockCanvas();
        if (canvas != null) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            Log.i("peter", "get canvas");
            this.paint.setAntiAlias(true);
            this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            int h = getHeight();
            int w = getWidth();
            RectF rectF = new RectF(0.0f, 0.0f, w, h);
            canvas.drawRect(rectF, this.paint);
            holder.getSurfaceHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void releaseWithoutStop() {
        if (this.mMediaPlayer != null) {
            release(true);
        }
    }

    public void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
            Log.d("moweiru", "cleartargetstate==");
            AudioManager am = (AudioManager) this.mAppContext.getSystemService("audio");
            am.abandonAudioFocus(null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
            return false;
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
            return false;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = keyCode != 4 && keyCode != 24 && keyCode != 25 && keyCode != 164 && keyCode != 82 && keyCode != 5 && keyCode != 6;
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

    public void setPreview() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setPreview(1);
        }
    }

    @Override
    public void start() {
        if (isInPlaybackState() && this.mMediaPlayer != null) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    @Override
    public void pause() {
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
        release(false);
    }

    public void resume() {
        openVideo(this.decodeType);
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(msec);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (isPlaying()) {
            pause();
        }
    }

    private boolean isInPlaybackState() {
        return this.mMediaPlayer != null && this.mCurrentState != -1 && this.mCurrentState != 0 && this.mCurrentState != 1;
    }

    @Override
    public boolean canPause() {
        return this.mCanPause;
    }

    @Override
    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    @Override
    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    @Override
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

    public void startLiveVideo(String url) {
        this.liveUrl = url;
        if (this.liveUrl != null && this.mMediaPlayer != null) {
            this.mMediaPlayer.playerRtmpStart(this.liveUrl, 1);
            this.liveUrl = null;
        }
    }

    public void stopLiveVideo() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.playerRtmpStop(0);
            this.liveUrl = null;
        }
    }

    public void setOnPreviewCallBackListener(OnFimiPreviewCallBackListener listener) {
        this.mPreviewCallBackListener = listener;
    }

    public void setOnFileCallBackListener(OnFimiFileCallBackListener listener) {
        this.mFimiFileCallBackListener = listener;
    }

    public void stopBackgroundPlay() {
        MediaPlayerService.setMediaPlayer(null);
    }

    public void enterBackground() {
        MediaPlayerService.setMediaPlayer(this.mMediaPlayer);
    }


    public interface OnFimiFileCallBackListener {
        void onSurfaceState(int i);
    }


    public interface OnFimiPreviewCallBackListener {
        void onCompletion(int i);

        void onSurfaceState(int i);
    }
}
