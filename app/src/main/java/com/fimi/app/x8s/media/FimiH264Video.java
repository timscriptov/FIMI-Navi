package com.fimi.app.x8s.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.interfaces.IFimiFpvShot;
import com.fimi.app.x8s.tensortfloow.H264ToBitmap;
import com.fimi.app.x8s.widget.X8AiTrackContainterView;
import com.fimi.app.x8s.widget.X8Camera9GridView;
import com.fimi.kernel.Constants;
import com.fimi.kernel.FimiAppContext;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.modulestate.StateManager;

import java.nio.ByteBuffer;


public class FimiH264Video extends RelativeLayout implements IFrameDataListener, OnX8VideoFrameBufferListener {
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int VIDEO_WIDTH = FimiAppContext.UI_HEIGHT;
    private static final int VIDEO_HEIGHT = FimiAppContext.UI_WIDTH;
    private final int FrameRate;
    private View blackView;
    private IFimiFpvShot fpvShotCallback;
    private int mCurrentState;
    private H264Decoder mH264Decoder;
    private IFrameDataListener mIFrameDataListener;
    private final SurfaceCallback mSurfaceCallback;
    private int mTargetState;
    private int mVideoHeight;
    private int mVideoWidth;
    private WorkThread mWorkThread;
    private X8AiTrackContainterView mX8AiTrackContainterView;
    private X8Camera9GridView mX8Camera9GridView;
    private OnX8VideoFrameBufferListener x8VideoFrameBufferListener;

    public FimiH264Video(Context context) {
        super(context);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.FrameRate = 60;
        this.mSurfaceCallback = new SurfaceCallback();
        init();
    }

    public FimiH264Video(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.FrameRate = 60;
        this.mSurfaceCallback = new SurfaceCallback();
        init();
    }

    public FimiH264Video(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.FrameRate = 60;
        this.mSurfaceCallback = new SurfaceCallback();
        init();
    }

    @RequiresApi(api = 21)
    public FimiH264Video(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.FrameRate = 60;
        this.mSurfaceCallback = new SurfaceCallback();
        init();
    }

    public H264Decoder getmH264Decoder() {
        return this.mH264Decoder;
    }

    public X8AiTrackContainterView getX8AiTrackContainterView() {
        return this.mX8AiTrackContainterView;
    }

    public void init() {
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        TextureView renderUIView = new TextureView(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2, 17);
        renderUIView.setLayoutParams(lp);
        renderUIView.setSurfaceTextureListener(this.mSurfaceCallback);
        this.mX8Camera9GridView = new X8Camera9GridView(getContext());
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(-1, -1, 17);
        this.mX8Camera9GridView.setLayoutParams(lp2);
        this.mX8AiTrackContainterView = new X8AiTrackContainterView(getContext());
        FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams(-1, -1, 17);
        this.mX8AiTrackContainterView.setLayoutParams(lp3);
        this.blackView = new View(getContext());
        FrameLayout.LayoutParams lp4 = new FrameLayout.LayoutParams(-1, -1, 17);
        this.blackView.setLayoutParams(lp4);
        this.blackView.setBackgroundColor(getContext().getResources().getColor(R.color.black));
        addView(renderUIView);
        addView(this.mX8AiTrackContainterView);
        addView(this.blackView);
        addView(this.mX8Camera9GridView);
        showGridLine(GlobalConfig.getInstance().getGridLine());
    }

    public void showGridLine(int type) {
        if (Constants.isFactoryApp()) {
            this.mX8Camera9GridView.setVisibility(0);
            this.mX8Camera9GridView.setType(1);
        } else if (type == 0) {
            this.mX8Camera9GridView.setVisibility(8);
        } else if (type == 3) {
            this.mX8Camera9GridView.setVisibility(0);
            this.mX8Camera9GridView.setType(1);
        } else if (type == 2) {
            this.mX8Camera9GridView.setVisibility(0);
            this.mX8Camera9GridView.setType(2);
        }
    }

    public void change9GridSize(int w, int h) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.mX8Camera9GridView.getLayoutParams();
        lp.width = w;
        lp.height = h;
        this.mX8Camera9GridView.setLayoutParams(lp);
        changeTrackView(w, h);
    }

    public void changeTrackView(int w, int h) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.mX8AiTrackContainterView.getLayoutParams();
        lp.width = w;
        lp.height = h;
        this.mX8AiTrackContainterView.setLayoutParams(lp);
        this.mX8AiTrackContainterView.isFullScreen(w > 800);
    }

    public void setX8VideoFrameBufferListener(OnX8VideoFrameBufferListener x8VideoFrameBufferListener) {
        this.x8VideoFrameBufferListener = x8VideoFrameBufferListener;
    }

    @Override
    public void onFrameBuffer(byte[] rgb) {
        if (this.x8VideoFrameBufferListener != null) {
            this.x8VideoFrameBufferListener.onFrameBuffer(rgb);
        }
        if (this.fpvShotCallback != null) {
            onFpvshotReady(rgb);
        }
    }

    @Override
    public void onH264Frame(ByteBuffer buffer) {
    }

    public void showVideoBg(boolean b) {
        if (b && StateManager.getInstance().getCamera().getToken() >= 0) {
            this.blackView.setVisibility(8);
        } else {
            this.blackView.setVisibility(0);
        }
    }

    public void openVideo(SurfaceTexture surface) {
        if (this.mH264Decoder == null) {
            this.mH264Decoder = new H264Decoder();
        }
        if (X8Application.Type2) {
            this.mH264Decoder.startJniThread(new Surface(surface), VIDEO_WIDTH, VIDEO_HEIGHT, this.FrameRate, this);
        } else {
            this.mH264Decoder.startWorkThread(new Surface(surface), VIDEO_WIDTH, VIDEO_HEIGHT, this.FrameRate, this);
        }
        this.mH264Decoder.setX8VideoFrameBufferListener(this);
        if (X8Application.isLocalVideo) {
            this.mWorkThread = new WorkThread(this);
            this.mWorkThread.start();
        }
    }

    public IFrameDataListener getmIFrameDataListener() {
        return this.mIFrameDataListener;
    }

    public void setmIFrameDataListener(IFrameDataListener mIFrameDataListener) {
        this.mIFrameDataListener = mIFrameDataListener;
    }

    public void release(boolean cleartargetstate) {
        if (this.mH264Decoder != null) {
            this.mCurrentState = 0;
            this.mTargetState = 0;
            this.mH264Decoder.stopThread();
            if (X8Application.isLocalVideo) {
                this.mWorkThread.stopThread();
            }
            this.mH264Decoder.setX8VideoFrameBufferListener(null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mH264Decoder != null) {
            this.mH264Decoder.onDestroy();
            this.mH264Decoder = null;
        }
    }

    public void onRawdataCallBack(byte[] data) {
        if (this.mH264Decoder != null) {
            this.mH264Decoder.setH264StreamData(data);
        }
    }

    @Override
    public void onCountFrame(int count, int remainder, int fpvSize) {
        if (this.mIFrameDataListener != null) {
            this.mIFrameDataListener.onCountFrame(count, remainder, fpvSize);
        }
    }

    public void setEnableCallback(int enbale) {
        if (this.mH264Decoder != null) {
            this.mH264Decoder.setEnableCallback(enbale);
        }
    }

    public void setX8TrackOverlaVisiable(int visiable) {
        this.mX8AiTrackContainterView.getViewTrackOverlay().setVisibility(visiable);
        if (visiable == 0) {
        }
    }

    public void fpvShot(IFimiFpvShot fpvShotCallback) {
        this.fpvShotCallback = fpvShotCallback;
        this.mH264Decoder.setEnableCallback(1);
    }

    public void onFpvshotReady(byte[] rgb) {
        this.mH264Decoder.setEnableCallback(0);
        Bitmap bitmap = H264ToBitmap.rgb2Bitmap(rgb, VIDEO_WIDTH, VIDEO_HEIGHT);
        this.fpvShotCallback.onFpvshotReady(bitmap);
        this.fpvShotCallback = null;
    }


    public class SurfaceCallback implements TextureView.SurfaceTextureListener {
        private SurfaceCallback() {
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.i("istep", ".....surfaceCreated......." + surface.hashCode());
            FimiH264Video.this.openVideo(surface);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.i("istep", ".....surfaceDestroyed.......");
            FimiH264Video.this.release(true);
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    }
}
