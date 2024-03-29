package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.X8AiTrackController;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiAutoPhotoExcuteConfirmModule;
import com.fimi.app.x8s.enums.X8AiAutoPhotoState;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiAutoPhotoExcuteControllerListener;
import com.fimi.app.x8s.interfaces.IX8AiAutoPhototExcuteListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.modulestate.GimbalState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiAutoPhototExcuteController extends AbsX8AiController implements View.OnClickListener, X8DoubleCustomDialog.onDialogButtonClickListener, X8AiTrackController.OnAiTrackControllerListener, IX8AiAutoPhototExcuteListener {
    private final X8sMainActivity activity;
    private final IX8NextViewListener mIX8NextViewListener;
    private final TimeHandler mTimeHandler;
    private final int type;
    protected boolean isNextShow;
    protected boolean isShow;
    protected int width;
    private View blank;
    private int count;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgAutoBg;
    private ImageView imgBack;
    private ImageView imgCloud;
    private ImageView imgNext;
    private IX8AiAutoPhotoExcuteControllerListener listener;
    private View mAngle;
    private X8AiAutoPhotoExcuteConfirmModule mX8AiAutoPhotoExcuteConfirmModule;
    private View nextRootView;
    private int pitchAngle;
    private X8AiAutoPhotoState state;
    private int timeSend;
    private TextView tvCloud;
    private TextView tvP2PTip;
    private TextView tvTime;
    private X8AiTipWithCloseView tvTip;

    public X8AiAutoPhototExcuteController(X8sMainActivity activity, View rootView, X8AiAutoPhotoState state, int type) {
        super(rootView);
        this.width = X8Application.ANIMATION_WIDTH;
        this.state = X8AiAutoPhotoState.IDLE;
        this.mTimeHandler = new TimeHandler();
        this.count = 3;
        this.timeSend = 0;
        this.mIX8NextViewListener = new IX8NextViewListener() {
            @Override
            public void onBackClick() {
                X8AiAutoPhototExcuteController.this.closeNextUi(true);
            }

            @Override
            public void onExcuteClick() {
                closeNextUi(false);
                mX8AiAutoPhotoExcuteConfirmModule.startAiAutoPhoto(X8AiAutoPhototExcuteController.this);
            }

            @Override
            public void onSaveClick() {
            }
        };
        this.activity = activity;
        this.state = state;
        this.type = type;
    }

    @Override
    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    @Override
    public void initActions() {
        if (this.handleView != null) {
            this.imgNext.setOnClickListener(this);
            this.imgBack.setOnClickListener(this);
            this.blank.setOnClickListener(this);
        }
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        if (this.state == X8AiAutoPhotoState.RUNNING) {
            stopAutoPhoto();
        } else {
            taskExit();
        }
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            String t = this.rootView.getContext().getString(R.string.x8_ai_fly_auto_photo);
            String m = this.rootView.getContext().getString(R.string.x8_ai_auto_photo_exite);
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        }
        this.dialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.state == X8AiAutoPhotoState.RUNNING) {
                showExitDialog();
                return;
            }
            closeUi();
            this.listener.onAutoPhotoBackClick();
        } else if (id == R.id.img_ai_follow_next) {
            openNextUi();
        } else if (id == R.id.x8_main_ai_auto_photo_next_blank) {
            closeNextUi(true);
        } else if (id == R.id.rl_flag_small) {
            if (this.tvP2PTip.getVisibility() == View.VISIBLE) {
                this.tvP2PTip.setVisibility(View.GONE);
            } else {
                this.tvP2PTip.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_auto_photo_layout, (ViewGroup) this.rootView, true);
        this.tvP2PTip = this.handleView.findViewById(R.id.img_ai_p2p_tip);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.flagSmall.setOnClickListener(this);
        this.imgNext = this.handleView.findViewById(R.id.img_ai_follow_next);
        this.imgBack = this.handleView.findViewById(R.id.img_ai_follow_back);
        this.tvTip = this.handleView.findViewById(R.id.v_content_tip);
        this.imgAutoBg = this.handleView.findViewById(R.id.img_ai_auto_photo_bg);
        this.mAngle = this.handleView.findViewById(R.id.rl_angle);
        this.imgCloud = this.handleView.findViewById(R.id.img_cloud);
        this.tvCloud = this.handleView.findViewById(R.id.tv_cloud);
        this.tvTime = this.handleView.findViewById(R.id.img_ai_time);
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_auto_photo_next_content);
        this.blank = this.rootView.findViewById(R.id.x8_main_ai_auto_photo_next_blank);
        this.mX8AiAutoPhotoExcuteConfirmModule = new X8AiAutoPhotoExcuteConfirmModule();
        if (this.state == X8AiAutoPhotoState.IDLE) {
            this.imgNext.setEnabled(false);
            if (this.type == 0) {
                String s = this.rootView.getContext().getString(R.string.x8_ai_auto_photo_tip5);
                if (this.activity.getmMapVideoController().isFullVideo()) {
                    this.imgAutoBg.setVisibility(View.VISIBLE);
                } else {
                    this.imgAutoBg.setVisibility(View.GONE);
                }
                this.tvTip.setTipText(s);
            } else {
                String s2 = this.rootView.getContext().getString(R.string.x8_ai_auto_photo_tip5);
                this.imgAutoBg.setVisibility(View.GONE);
                this.tvTip.setTipText(s2);
            }
        } else if (this.state == X8AiAutoPhotoState.RUNNING) {
            this.imgNext.setVisibility(View.GONE);
            this.imgAutoBg.setVisibility(View.GONE);
            this.mAngle.setVisibility(View.GONE);
            this.tvTip.setVisibility(View.GONE);
            if (this.listener != null) {
                this.listener.onAutoPhotoRunning();
            }
        }
        this.activity.getmX8AiTrackController().setOnAiTrackControllerListener(this);
        initActions();
        this.activity.getmX8AiTrackController().openUi();
        super.openUi();
    }

    @Override
    public void closeUi() {
        this.isShow = false;
        this.activity.getmX8AiTrackController().closeUi();
        this.state = X8AiAutoPhotoState.IDLE;
        this.mTimeHandler.removeMessages(0);
        this.mTimeHandler.removeMessages(1);
        setAiVcClose();
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(View.VISIBLE);
        this.blank.setVisibility(View.VISIBLE);
        closeIconByNextUi();
        this.mX8AiAutoPhotoExcuteConfirmModule.init(this.activity, this.nextRootView, this.type, this.pitchAngle);
        this.mX8AiAutoPhotoExcuteConfirmModule.setListener(this.mIX8NextViewListener, this.activity.getFcManager(), this);
        this.mX8AiAutoPhotoExcuteConfirmModule.setValue();
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", this.width, 0.0f);
            animatorY.setDuration(300L);
            animatorY.start();
        }
    }

    public void closeNextUi(final boolean b) {
        this.blank.setVisibility(View.GONE);
        if (this.isNextShow) {
            this.isNextShow = false;
            ObjectAnimator translationRight = ObjectAnimator.ofFloat(this.nextRootView, "translationX", 0.0f, this.width);
            translationRight.setDuration(300L);
            translationRight.start();
            translationRight.addListener(new AnimatorListenerAdapter() {
                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    X8AiAutoPhototExcuteController.this.nextRootView.setVisibility(View.GONE);
                    ((ViewGroup) X8AiAutoPhototExcuteController.this.nextRootView).removeAllViews();
                    X8AiAutoPhototExcuteController.this.imgBack.setVisibility(View.VISIBLE);
                    X8AiAutoPhototExcuteController.this.flagSmall.setVisibility(View.VISIBLE);
                    if (b) {
                        X8AiAutoPhototExcuteController.this.imgNext.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    public void setListener(IX8AiAutoPhotoExcuteControllerListener listener) {
        this.listener = listener;
    }

    public void showGimbalState() {
        GimbalState state = StateManager.getInstance().getGimbalState();
        if (state != null && this.handleView != null && this.isShow) {
            this.pitchAngle = state.getPitchAngle();
            double angle = this.pitchAngle / 100.0d;
            String angleStr = NumberUtil.decimalPointStr(angle, 1) + "°";
            this.tvCloud.setText(angleStr);
        }
    }

    public void taskExit() {
        onTaskComplete(false);
    }

    public void stopAutoPhoto() {
        this.state = X8AiAutoPhotoState.STOP;
        this.activity.getFcManager().setAiAutoPhotoExit(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAutoPhototExcuteController.this.taskExit();
                }
            }
        });
    }

    public void setAiVcClose() {
        this.activity.getFcManager().setAiVcClose(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAutoPhototExcuteController.this.activity.getmX8AiTrackController().closeUi();
                }
            }
        });
    }

    public void setAiVcNotityFc() {
        this.activity.getFcManager().setAiVcNotityFc(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                }
            }
        });
    }

    @Override
    public void autoPhototExcute(boolean isExcute) {
        if (isExcute) {
            this.imgAutoBg.setVisibility(View.GONE);
            this.mAngle.setVisibility(View.GONE);
            this.tvTip.setVisibility(View.GONE);
            this.state = X8AiAutoPhotoState.RUNNING;
            this.listener.onAutoPhotoRunning();
            this.mTimeHandler.sendEmptyMessage(1);
            this.count = 3;
            this.tvTime.setVisibility(View.VISIBLE);
            return;
        }
        taskExit();
    }

    @Override
    public void onChangeGoLocation(float left, float right, float top, float bottom, int w, int h) {
    }

    @Override
    public void setGoEnabled(boolean b) {
        if (b) {
            this.imgNext.setEnabled(true);
            setAiVcNotityFc();
        }
    }

    @Override
    public void onTouchActionDown() {
    }

    @Override
    public void onTouchActionUp() {
    }

    @Override
    public void onTracking() {
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            if (!b) {
                onDisconnectTaskComplete();
                return;
            }
            showGimbalState();
            sysAiVcCtrlMode();
        }
    }

    public void cancleByModeChange(int taskMode) {
        onTaskComplete(taskMode == 1);
    }

    private void onTaskComplete(boolean b) {
        closeAutoPhoto();
        if (this.listener != null) {
            this.listener.onAutoPhotoComplete(b);
        }
    }

    private void onDisconnectTaskComplete() {
        closeAutoPhoto();
        if (this.listener != null) {
            this.listener.onAutoPhotoComplete(false);
        }
    }

    private void closeAutoPhoto() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAutoPhotoBackClick();
        }
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    public void switchMapVideo(boolean isVideo) {
        if (this.state == X8AiAutoPhotoState.IDLE && this.type == 0) {
            if (!isVideo) {
                this.imgAutoBg.setVisibility(View.VISIBLE);
            } else {
                this.imgAutoBg.setVisibility(View.GONE);
            }
        }
    }

    public void closeIconByNextUi() {
        this.imgNext.setVisibility(View.GONE);
        this.imgBack.setVisibility(View.GONE);
        this.flagSmall.setVisibility(View.GONE);
    }

    public void sysAiVcCtrlMode() {
        if (this.state == X8AiAutoPhotoState.IDLE) {
            if (this.timeSend == 0) {
                this.timeSend = 1;
                this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                    @Override
                    public void onComplete(CmdResult cmdResult, Object o) {
                    }
                }, X8Task.VCM_SELFIE.ordinal());
                return;
            }
            this.timeSend = 0;
        }
    }


    @SuppressLint("HandlerLeak")
    public class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (count >= 0) {
                    if (count != 0) {
                        count--;
                        tvTime.setText("" + count);
                    } else {
                        count--;
                        tvTime.setText("GO");
                    }
                    sendEmptyMessageDelayed(1, 1000L);
                    return;
                }
                tvTime.setVisibility(View.GONE);
            }
        }
    }
}
