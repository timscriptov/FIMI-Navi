package com.fimi.soul.media.player.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.twitter.sdk.android.core.internal.VineCardUtils;

import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.Locale;



/* loaded from: classes.dex */
public class FmMediaController extends FrameLayout {
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int sDefaultTimeout = 3000;
    private static final int updatePause = 0;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private View mAnchor;
    private Context mContext;
    private TextView mCurrentTime;
    private View mDecor;
    private WindowManager.LayoutParams mDecorLayoutParams;
    private boolean mDragging;
    private TextView mEndTime;
    private ImageButton mFfwdButton;
    private View.OnClickListener mFfwdListener;
    private boolean mFromXml;
    private Handler mHandler;
    private View.OnLayoutChangeListener mLayoutChangeListener;
    private boolean mListenersSet;
    private ImageButton mNextButton;
    private View.OnClickListener mNextListener;
    private ImageButton mPauseButton;
    private View.OnClickListener mPauseListener;
    private MediaPlayerControl mPlayer;
    private ImageButton mPrevButton;
    private View.OnClickListener mPrevListener;
    private SeekBar mProgress;
    private ImageButton mRewButton;
    private View.OnClickListener mRewListener;
    private View mRoot;
    private SeekBar.OnSeekBarChangeListener mSeekListener;
    private boolean mShowing;
    private View.OnTouchListener mTouchListener;
    private boolean mUseFastForward;
    private Window mWindow;
    private WindowManager mWindowManager;

    public FmMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                FmMediaController.this.updateFloatingWindowLayout();
                if (FmMediaController.this.mShowing) {
                    FmMediaController.this.mWindowManager.updateViewLayout(FmMediaController.this.mDecor, FmMediaController.this.mDecorLayoutParams);
                }
            }
        };
        this.mTouchListener = new View.OnTouchListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0 && FmMediaController.this.mShowing) {
                    FmMediaController.this.hide();
                    return false;
                }
                return false;
            }
        };
        this.mHandler = new Handler() { // from class: com.fimi.soul.media.player.widget.FmMediaController.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (FmMediaController.this.mPlayer.isPlaying()) {
                            FmMediaController.this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
                            return;
                        } else {
                            FmMediaController.this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
                            return;
                        }
                    case 1:
                        FmMediaController.this.hide();
                        return;
                    case 2:
                        Log.d(VineCardUtils.PLAYER_CARD, "handle SHOW_PROGRESS");
                        int pos = FmMediaController.this.setProgress();
                        if (!FmMediaController.this.mDragging && FmMediaController.this.mShowing && FmMediaController.this.mPlayer.isPlaying()) {
                            Message msg2 = obtainMessage(2);
                            sendMessageDelayed(msg2, 1000 - (pos % 1000));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mPauseListener = new View.OnClickListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FmMediaController.this.doPauseResume();
                FmMediaController.this.show(3000);
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar bar) {
                Log.d(VineCardUtils.PLAYER_CARD, "onStartTrackingTouch");
                FmMediaController.this.show((int) 3600000);
                FmMediaController.this.mDragging = true;
                FmMediaController.this.mHandler.removeMessages(2);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                Log.d(VineCardUtils.PLAYER_CARD, "onProgressChanged");
                if (fromuser) {
                    long duration = FmMediaController.this.mPlayer.getDuration();
                    long newposition = (progress * duration) / 1000;
                    FmMediaController.this.mPlayer.seekTo((int) newposition);
                    if (FmMediaController.this.mCurrentTime != null) {
                        FmMediaController.this.mCurrentTime.setText(FmMediaController.this.stringForTime((int) newposition));
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar bar) {
                Log.d(VineCardUtils.PLAYER_CARD, "onStopTrackingTouch");
                FmMediaController.this.mDragging = false;
                FmMediaController.this.setProgress();
                FmMediaController.this.updatePausePlay();
                FmMediaController.this.show(3000);
                FmMediaController.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mRewListener = new View.OnClickListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int pos = FmMediaController.this.mPlayer.getCurrentPosition();
                FmMediaController.this.mPlayer.seekTo(pos - 5000);
                FmMediaController.this.setProgress();
                FmMediaController.this.show(3000);
            }
        };
        this.mFfwdListener = new View.OnClickListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int pos = FmMediaController.this.mPlayer.getCurrentPosition();
                FmMediaController.this.mPlayer.seekTo(pos + 15000);
                FmMediaController.this.setProgress();
                FmMediaController.this.show(3000);
            }
        };
        this.mRoot = this;
        this.mContext = context;
        this.mUseFastForward = true;
        this.mFromXml = true;
    }

    public FmMediaController(Context context, boolean useFastForward) {
        super(context);
        this.mLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                FmMediaController.this.updateFloatingWindowLayout();
                if (FmMediaController.this.mShowing) {
                    FmMediaController.this.mWindowManager.updateViewLayout(FmMediaController.this.mDecor, FmMediaController.this.mDecorLayoutParams);
                }
            }
        };
        this.mTouchListener = new View.OnTouchListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0 && FmMediaController.this.mShowing) {
                    FmMediaController.this.hide();
                    return false;
                }
                return false;
            }
        };
        this.mHandler = new Handler() { // from class: com.fimi.soul.media.player.widget.FmMediaController.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (FmMediaController.this.mPlayer.isPlaying()) {
                            FmMediaController.this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
                            return;
                        } else {
                            FmMediaController.this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
                            return;
                        }
                    case 1:
                        FmMediaController.this.hide();
                        return;
                    case 2:
                        Log.d(VineCardUtils.PLAYER_CARD, "handle SHOW_PROGRESS");
                        int pos = FmMediaController.this.setProgress();
                        if (!FmMediaController.this.mDragging && FmMediaController.this.mShowing && FmMediaController.this.mPlayer.isPlaying()) {
                            Message msg2 = obtainMessage(2);
                            sendMessageDelayed(msg2, 1000 - (pos % 1000));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.mPauseListener = new View.OnClickListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FmMediaController.this.doPauseResume();
                FmMediaController.this.show(3000);
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar bar) {
                Log.d(VineCardUtils.PLAYER_CARD, "onStartTrackingTouch");
                FmMediaController.this.show((int) 3600000);
                FmMediaController.this.mDragging = true;
                FmMediaController.this.mHandler.removeMessages(2);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                Log.d(VineCardUtils.PLAYER_CARD, "onProgressChanged");
                if (fromuser) {
                    long duration = FmMediaController.this.mPlayer.getDuration();
                    long newposition = (progress * duration) / 1000;
                    FmMediaController.this.mPlayer.seekTo((int) newposition);
                    if (FmMediaController.this.mCurrentTime != null) {
                        FmMediaController.this.mCurrentTime.setText(FmMediaController.this.stringForTime((int) newposition));
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar bar) {
                Log.d(VineCardUtils.PLAYER_CARD, "onStopTrackingTouch");
                FmMediaController.this.mDragging = false;
                FmMediaController.this.setProgress();
                FmMediaController.this.updatePausePlay();
                FmMediaController.this.show(3000);
                FmMediaController.this.mHandler.sendEmptyMessage(2);
            }
        };
        this.mRewListener = new View.OnClickListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int pos = FmMediaController.this.mPlayer.getCurrentPosition();
                FmMediaController.this.mPlayer.seekTo(pos - 5000);
                FmMediaController.this.setProgress();
                FmMediaController.this.show(3000);
            }
        };
        this.mFfwdListener = new View.OnClickListener() { // from class: com.fimi.soul.media.player.widget.FmMediaController.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int pos = FmMediaController.this.mPlayer.getCurrentPosition();
                FmMediaController.this.mPlayer.seekTo(pos + 15000);
                FmMediaController.this.setProgress();
                FmMediaController.this.show(3000);
            }
        };
        this.mContext = context;
        this.mUseFastForward = useFastForward;
        initFloatingWindowLayout();
        initFloatingWindow();
    }

    public FmMediaController(Context context) {
        this(context, true);
    }

    public static void main(String[] argv) {
        System.out.println(9);
    }

    @Override // android.view.View
    @SuppressLint({"MissingSuperCall"})
    public void onFinishInflate() {
        if (this.mRoot != null) {
            initControllerView(this.mRoot);
        }
    }

    private void initWindow() {
        try {
            Class policyClass = Class.forName("com.android.internal.policy.PolicyManager");
            Method[] meths = policyClass.getMethods();
            Method makenewwindow = null;
            for (int i = 0; i < meths.length; i++) {
                if (meths[i].getName().endsWith("makeNewWindow")) {
                    makenewwindow = meths[i];
                }
            }
            this.mWindow = (Window) makenewwindow.invoke(null, this.mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFloatingWindow() {
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        initWindow();
        this.mWindow.setWindowManager(this.mWindowManager, null, null);
        this.mWindow.requestFeature(1);
        this.mDecor = this.mWindow.getDecorView();
        this.mDecor.setOnTouchListener(this.mTouchListener);
        this.mWindow.setContentView(this);
        this.mWindow.setBackgroundDrawableResource(17170445);
        this.mWindow.setVolumeControlStream(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(262144);
        requestFocus();
    }

    private void initFloatingWindowLayout() {
        this.mDecorLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams p = this.mDecorLayoutParams;
        p.gravity = 51;
        p.height = -2;
        p.x = 0;
        p.format = -3;
        p.type = 1000;
        p.flags |= 8519712;
        p.token = null;
        p.windowAnimations = 0;
    }

    public void updateFloatingWindowLayout() {
        int[] anchorPos = new int[2];
        this.mAnchor.getLocationOnScreen(anchorPos);
        this.mDecor.measure(View.MeasureSpec.makeMeasureSpec(this.mAnchor.getWidth(), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(this.mAnchor.getHeight(), Integer.MIN_VALUE));
        WindowManager.LayoutParams p = this.mDecorLayoutParams;
        p.width = this.mAnchor.getWidth();
        p.x = anchorPos[0] + ((this.mAnchor.getWidth() - p.width) / 2);
        p.y = (anchorPos[1] + this.mAnchor.getHeight()) - this.mDecor.getMeasuredHeight();
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        this.mPlayer = player;
        updatePausePlay();
    }

    public void setAnchorView(View view) {
        if (this.mAnchor != null) {
            this.mAnchor.removeOnLayoutChangeListener(this.mLayoutChangeListener);
        }
        this.mAnchor = view;
        if (this.mAnchor != null) {
            this.mAnchor.addOnLayoutChangeListener(this.mLayoutChangeListener);
        }
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(-1, -1);
        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        this.mRoot = inflate.inflate(R.layout.x8_fm_media_controller, (ViewGroup) null);
        initControllerView(this.mRoot);
        return this.mRoot;
    }

    private void initControllerView(View v) {
        this.mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (this.mPauseButton != null) {
            this.mPauseButton.requestFocus();
            this.mPauseButton.setOnClickListener(this.mPauseListener);
        }
        this.mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
        if (this.mFfwdButton != null) {
            this.mFfwdButton.setOnClickListener(this.mFfwdListener);
            if (!this.mFromXml) {
                this.mFfwdButton.setVisibility(this.mUseFastForward ? 0 : 8);
            }
        }
        this.mRewButton = (ImageButton) v.findViewById(R.id.rew);
        if (this.mRewButton != null) {
            this.mRewButton.setOnClickListener(this.mRewListener);
            if (!this.mFromXml) {
                this.mRewButton.setVisibility(this.mUseFastForward ? 0 : 8);
            }
        }
        this.mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (this.mNextButton != null && !this.mFromXml && !this.mListenersSet) {
            this.mNextButton.setVisibility(8);
        }
        this.mPrevButton = (ImageButton) v.findViewById(R.id.prev);
        if (this.mPrevButton != null && !this.mFromXml && !this.mListenersSet) {
            this.mPrevButton.setVisibility(8);
        }
        this.mProgress = (SeekBar) v.findViewById(R.id.fmmediacontroller_progress);
        if (this.mProgress != null) {
            if (this.mProgress instanceof SeekBar) {
                SeekBar seeker = this.mProgress;
                seeker.setOnSeekBarChangeListener(this.mSeekListener);
            }
            this.mProgress.setMax(1000);
        }
        this.mEndTime = (TextView) v.findViewById(R.id.time);
        this.mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        installPrevNextListeners();
    }

    public void show() {
        show(3000);
    }

    private void disableUnsupportedButtons() {
        try {
            if (this.mPauseButton != null && !this.mPlayer.canPause()) {
                this.mPauseButton.setEnabled(false);
            }
            if (this.mRewButton != null && !this.mPlayer.canSeekBackward()) {
                this.mRewButton.setEnabled(false);
            }
            if (this.mFfwdButton != null && !this.mPlayer.canSeekForward()) {
                this.mFfwdButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError e) {
        }
    }

    public void show(int timeout) {
        Log.d(VineCardUtils.PLAYER_CARD, "show");
        if (!this.mShowing && this.mAnchor != null) {
            setProgress();
            if (this.mPauseButton != null) {
                this.mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();
            updateFloatingWindowLayout();
            try {
                this.mWindowManager.addView(this.mDecor, this.mDecorLayoutParams);
            } catch (Exception e) {
                Log.e(VineCardUtils.PLAYER_CARD, "mWindowManager addview error");
            }
            this.mShowing = true;
        }
        updatePausePlay();
        this.mHandler.sendEmptyMessage(2);
        Message msg = this.mHandler.obtainMessage(1);
        if (timeout != 0) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void hide() {
        if (this.mAnchor != null && this.mShowing) {
            try {
                this.mHandler.removeMessages(2);
                this.mWindowManager.removeView(this.mDecor);
            } catch (IllegalArgumentException e) {
                Log.w("MediaController", "already removed");
            }
            this.mShowing = false;
        }
    }

    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        return hours > 0 ? this.mFormatter.format("%d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)).toString() : this.mFormatter.format("%02d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds)).toString();
    }

    public int setProgress() {
        Log.d(VineCardUtils.PLAYER_CARD, "setProgress");
        if (this.mPlayer == null || this.mDragging) {
            return 0;
        }
        int position = this.mPlayer.getCurrentPosition();
        int duration = this.mPlayer.getDuration();
        Log.d(VineCardUtils.PLAYER_CARD, "position" + position + " duration" + duration);
        if (this.mProgress != null && duration > 0) {
            if (duration / 1000 > 0) {
                long pos = (1000 * (position / 1000)) / (duration / 1000);
                this.mProgress.setProgress((int) pos);
            } else {
                this.mProgress.setProgress(0);
            }
        }
        if (this.mEndTime != null) {
            this.mEndTime.setText(stringForTime(duration));
        }
        if (this.mCurrentTime != null && !this.mCurrentTime.getText().toString().equals(this.mEndTime.getText().toString())) {
            this.mCurrentTime.setText(stringForTime(position));
        }
        if (position == 0) {
            this.mCurrentTime.setText(stringForTime(position));
            return position;
        }
        return position;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        show(3000);
        return true;
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        show(3000);
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        boolean uniqueDown = event.getRepeatCount() == 0 && event.getAction() == 0;
        if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
            if (uniqueDown) {
                doPauseResume();
                show(3000);
                if (this.mPauseButton != null) {
                    this.mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == 126) {
            if (uniqueDown && !this.mPlayer.isPlaying()) {
                this.mPlayer.start();
                updatePausePlay();
                show(3000);
            }
            return true;
        } else if (keyCode == 86 || keyCode == 127) {
            if (uniqueDown && this.mPlayer.isPlaying()) {
                this.mPlayer.pause();
                updatePausePlay();
                show(3000);
            }
            return true;
        } else if (keyCode == 25 || keyCode == 24 || keyCode == 164 || keyCode == 27) {
            return super.dispatchKeyEvent(event);
        } else {
            if (keyCode == 4) {
                if (uniqueDown) {
                    hide();
                    ((Activity) this.mContext).finish();
                }
                return true;
            } else if (keyCode == 82) {
                if (uniqueDown) {
                    hide();
                }
                return true;
            } else {
                show(3000);
                return super.dispatchKeyEvent(event);
            }
        }
    }

    public void updatePausePlay() {
        if (this.mRoot != null && this.mPauseButton != null) {
            if (this.mPlayer.isPlaying()) {
                this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
            } else {
                this.mPauseButton.setBackgroundResource(R.drawable.ic_launcher);
            }
            this.mHandler.sendEmptyMessageDelayed(0, 200L);
        }
    }

    public void update() {
        updatePausePlay();
    }

    public void doPauseResume() {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        } else {
            this.mPlayer.start();
        }
        updatePausePlay();
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        boolean z = true;
        if (this.mPauseButton != null) {
            this.mPauseButton.setEnabled(enabled);
        }
        if (this.mFfwdButton != null) {
            this.mFfwdButton.setEnabled(enabled);
        }
        if (this.mRewButton != null) {
            this.mRewButton.setEnabled(enabled);
        }
        if (this.mNextButton != null) {
            this.mNextButton.setEnabled(enabled && this.mNextListener != null);
        }
        if (this.mPrevButton != null) {
            ImageButton imageButton = this.mPrevButton;
            if (!enabled || this.mPrevListener == null) {
                z = false;
            }
            imageButton.setEnabled(z);
        }
        if (this.mProgress != null) {
            this.mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(FmMediaController.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(FmMediaController.class.getName());
    }

    private void installPrevNextListeners() {
        if (this.mNextButton != null) {
            this.mNextButton.setOnClickListener(this.mNextListener);
            this.mNextButton.setEnabled(this.mNextListener != null);
        }
        if (this.mPrevButton != null) {
            this.mPrevButton.setOnClickListener(this.mPrevListener);
            this.mPrevButton.setEnabled(this.mPrevListener != null);
        }
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {
        this.mNextListener = next;
        this.mPrevListener = prev;
        this.mListenersSet = true;
        if (this.mRoot != null) {
            installPrevNextListeners();
            if (this.mNextButton != null && !this.mFromXml) {
                this.mNextButton.setVisibility(0);
            }
            if (this.mPrevButton != null && !this.mFromXml) {
                this.mPrevButton.setVisibility(0);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface MediaPlayerControl {
        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        int getAudioSessionId();

        int getBufferPercentage();

        int getCurrentPosition();

        int getDuration();

        boolean isPlaying();

        void pause();

        void seekTo(int i);

        void start();
    }
}
