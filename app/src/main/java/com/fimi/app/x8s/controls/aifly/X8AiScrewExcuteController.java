package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.aifly.confirm.module.X8AiScrewNextModule;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.interfaces.IX8ScrewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckAiScrewPrameter;
import com.fimi.x8sdk.dataparser.AckAiSurrounds;
import com.fimi.x8sdk.dataparser.AckGetAiSurroundPoint;
import com.fimi.x8sdk.dataparser.AckNormalCmds;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiScrewExcuteController extends AbsX8AiController implements View.OnClickListener, X8DoubleCustomDialog.onDialogButtonClickListener {
    protected int MAX_WIDTH;
    protected boolean isShow;
    protected int width;
    private final X8sMainActivity activity;
    private View blank;
    private double currentLat;
    private double currentLog;
    private boolean cw;
    private X8DoubleCustomDialog dialog;
    private int distance;
    private View flagSmall;
    private float height;
    private ImageView imgBack;
    private ImageView imgSuroundBg;
    private boolean isDraw;
    private boolean isGetDistance;
    private boolean isGetPoint;
    private boolean isGetSpeed;
    private boolean isNextShow;
    private double lastLat;
    private double lastLog;
    private IX8ScrewListener listener;
    private FcCtrlManager mFcCtrlManager;
    private FcManager mFcManager;
    private final IX8NextViewListener mIX8NextViewListener;
    private TextView mTvRadius;
    private X8AiScrewNextModule mX8AiScrewNextModule;
    private View nextRootView;
    private String prex;
    private int r;
    private float radius;
    private ScrewState state;
    private int timeSend;
    private TextView tvP2PTip;
    private TextView tvPoint;
    private X8AiTipWithCloseView tvTip;
    private View vRadiusBg;

    public X8AiScrewExcuteController(X8sMainActivity activity, View rootView, ScrewState state) {
        super(rootView);
        this.width = X8Application.ANIMATION_WIDTH;
        this.timeSend = 0;
        this.state = ScrewState.IDLE;
        this.mIX8NextViewListener = new IX8NextViewListener() {
            @Override
            public void onBackClick() {
                X8AiScrewExcuteController.this.closeNextUi(true);
            }

            @Override
            public void onExcuteClick() {
                X8AiScrewExcuteController.this.closeNextUi(false);
                X8AiScrewExcuteController.this.imgSuroundBg.setVisibility(8);
                X8AiScrewExcuteController.this.vRadiusBg.setVisibility(8);
                X8AiScrewExcuteController.this.tvTip.setVisibility(8);
                X8AiScrewExcuteController.this.state = ScrewState.RUNNING;
                X8AiScrewExcuteController.this.listener.onAiScrewRunning();
            }

            @Override
            public void onSaveClick() {
            }
        };
        this.activity = activity;
        this.state = state;
    }

    public double getLastLng() {
        return this.lastLog;
    }

    public double getLastLat() {
        return this.lastLat;
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        if (this.state == ScrewState.RUNNING) {
            sendExiteCmd();
        }
    }

    public void setListener(IX8ScrewListener listener) {
        this.listener = listener;
    }

    @Override
    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            if (this.state != ScrewState.RUNNING) {
                closeScrewing();
            } else {
                showExitDialog();
            }
        } else if (id == R.id.img_ai_set_dot) {
            if (this.state == ScrewState.IDLE) {
                setCirclePoint();
            } else if (this.state == ScrewState.SETDOT) {
                if (this.radius >= 200.0f) {
                    String s = X8NumberUtil.getDistanceNumberString(200.0f, 0, false);
                    String t = String.format(this.activity.getString(R.string.x8_ai_surround_radius_tip2), s);
                    X8ToastUtil.showToast(this.activity, t, 0);
                } else if (this.radius < 5.0f) {
                    String s2 = X8NumberUtil.getDistanceNumberString(5.0f, 0, false);
                    String t2 = String.format(this.activity.getString(R.string.x8_ai_surround_radius_tip1), s2);
                    X8ToastUtil.showToast(this.activity, t2, 0);
                } else if (this.height < 3.0f) {
                    String tip = String.format(this.activity.getString(R.string.height_tip), X8NumberUtil.getDistanceNumberString(3.0f, 0, true));
                    X8ToastUtil.showToast(this.activity, tip, 0);
                } else {
                    sendCircleCmd();
                }
            } else if (this.state == ScrewState.SETRADIUS) {
                openNextUi();
            }
        } else if (id == R.id.main_ai_ai_screw_next_blank) {
            closeNextUi(true);
            this.state = ScrewState.SETDOT;
        } else if (id == R.id.rl_flag_small) {
            if (this.tvP2PTip.getVisibility() == 0) {
                this.tvP2PTip.setVisibility(8);
            } else {
                this.tvP2PTip.setVisibility(0);
            }
        }
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8AiController, com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_screw_layout, (ViewGroup) this.rootView, true);
        this.imgSuroundBg = this.handleView.findViewById(R.id.img_ai_suround_bg);
        this.imgBack = this.handleView.findViewById(R.id.img_ai_follow_back);
        this.tvP2PTip = this.handleView.findViewById(R.id.img_ai_p2p_tip);
        this.tvPoint = this.handleView.findViewById(R.id.img_ai_set_dot);
        this.vRadiusBg = this.handleView.findViewById(R.id.rl_x8_ai_surround_radius);
        this.mTvRadius = this.handleView.findViewById(R.id.tv_ai_radius);
        this.tvTip = this.handleView.findViewById(R.id.v_content_tip);
        this.tvTip.setTipText(this.handleView.getContext().getString(R.string.x8_ai_surround_select_point));
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.flagSmall.setOnClickListener(this);
        if (this.state != ScrewState.IDLE) {
            this.imgSuroundBg.setVisibility(8);
            this.tvPoint.setVisibility(8);
            this.mTvRadius.setVisibility(8);
            this.tvTip.setVisibility(8);
        }
        this.prex = this.activity.getString(R.string.x8_ai_fly_screw_hight_distance);
        this.nextRootView = this.rootView.findViewById(R.id.x8_main_ai_screw_next_content);
        this.blank = this.rootView.findViewById(R.id.main_ai_ai_screw_next_blank);
        this.mX8AiScrewNextModule = new X8AiScrewNextModule();
        this.imgBack.setOnClickListener(this);
        this.tvPoint.setOnClickListener(this);
        this.blank.setOnClickListener(this);
        if (this.state == ScrewState.RUNNING) {
            this.listener.onAiScrewRunning();
        }
        super.openUi();
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8AiController, com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        this.isShow = false;
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().clearSurroundMarker();
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().resetMapEvent();
        super.closeUi();
    }

    public void openNextUi() {
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        closeIconByNextUi();
        this.mX8AiScrewNextModule.init(this.activity, this.nextRootView);
        if (this.mX8AiScrewNextModule != null) {
            this.mX8AiScrewNextModule.setListener(this.mIX8NextViewListener, this.mFcManager, this, this.radius, this.height);
        }
        if (!this.isNextShow) {
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", this.width, 0.0f);
            animatorY.setDuration(300L);
            animatorY.addListener(new AnimatorListenerAdapter() {
                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                }
            });
            animatorY.start();
        }
    }

    public void closeNextUi(final boolean b) {
        this.blank.setVisibility(8);
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
                    X8AiScrewExcuteController.this.nextRootView.setVisibility(8);
                    ((ViewGroup) X8AiScrewExcuteController.this.nextRootView).removeAllViews();
                    X8AiScrewExcuteController.this.imgBack.setVisibility(0);
                    X8AiScrewExcuteController.this.flagSmall.setVisibility(0);
                    if (b) {
                        X8AiScrewExcuteController.this.tvPoint.setVisibility(0);
                    }
                }
            });
        }
    }

    public void setFcManager(FcCtrlManager mFcCtrlManager, FcManager mFcManager) {
        this.mFcCtrlManager = mFcCtrlManager;
        this.mFcManager = mFcManager;
    }

    @Override
    public boolean isShow() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 4) {
            return false;
        }
        return this.isShow;
    }

    public void showExitDialog() {
        String t = this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_title);
        String m = this.rootView.getContext().getString(R.string.x8_ai_fly_screw_exte);
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete(false);
    }

    public void cancleByModeChange(int mode) {
        onTaskComplete(mode == 1);
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow) {
            if (!b) {
                ononDroneDisconnectedTaskComplete();
            } else {
                sysAiVcCtrlMode();
            }
        }
    }

    private void onTaskComplete(boolean showText) {
        closeScrewing();
        if (this.listener != null) {
            this.listener.onAiScrewComplete(showText);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        closeScrewing();
        if (this.listener != null) {
            this.listener.onAiScrewComplete(false);
        }
    }

    private void closeScrewing() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAiScrewBackClick();
        }
    }

    public void sendExiteCmd() {
        this.mFcManager.setScrewExite(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.taskExit();
                }
            }
        });
    }

    public void switchMapVideo(boolean sightFlag) {
        if (this.handleView != null && this.isShow) {
            if (this.state != ScrewState.RUNNING) {
                this.imgSuroundBg.setVisibility(sightFlag ? 8 : 0);
            } else {
                this.imgSuroundBg.setVisibility(8);
            }
        }
    }

    public void showSportState(AutoFcSportState state1) {
        String h;
        String d;
        if (this.nextRootView != null && this.mX8AiScrewNextModule != null) {
            if (((ViewGroup) this.nextRootView).getChildCount() > 0) {
                this.mX8AiScrewNextModule.showSportState(state1);
            }
            if ((this.state == ScrewState.SETDOT) | (this.state == ScrewState.SETRADIUS)) {
                this.height = state1.getHeight();
                float intH = this.height;
                this.radius = getCurrentDistance();
                float intD = this.radius;
                if (intH >= 3.0f) {
                    h = "<font color='#ffffffff'>" + X8NumberUtil.getDistanceNumberString(intH, 1, false) + "</font>";
                } else {
                    h = "<font color='#F22121'>" + X8NumberUtil.getDistanceNumberString(intH, 1, false) + "</font>";
                }
                if (intD > 5.0f) {
                    d = "<font color='#ffffffff'>" + X8NumberUtil.getDistanceNumberString(intD, 1, false) + "</font>";
                } else {
                    d = "<font color='#F22121'>" + X8NumberUtil.getDistanceNumberString(intD, 1, false) + "</font>";
                }
                if (this.vRadiusBg.getVisibility() != 0) {
                    this.vRadiusBg.setVisibility(0);
                }
                String s = String.format(this.prex, h, d);
                this.mTvRadius.setText(Html.fromHtml(s));
            }
        }
        getParmeter();
    }

    public void setCirclePoint() {
        this.lastLog = StateManager.getInstance().getX8Drone().getLongitude();
        this.lastLat = StateManager.getInstance().getX8Drone().getLatitude();
        this.tvPoint.setText(this.activity.getString(R.string.x8_ai_fly_follow_surround_set_takeoff_point));
        String prex1 = this.rootView.getContext().getString(R.string.x8_ai_fly_screw_tip5);
        String str1 = X8NumberUtil.getDistanceNumberString(3.0f, 1, false);
        String str2 = X8NumberUtil.getDistanceNumberString(5.0f, 1, false);
        String str3 = X8NumberUtil.getDistanceNumberString(200.0f, 1, false);
        this.tvTip.setTipText(String.format(prex1, str1, str2, str3));
        this.state = ScrewState.SETDOT;
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundMark(this.lastLat, this.lastLog);
    }

    public float getCurrentDistance() {
        double currentLng = StateManager.getInstance().getX8Drone().getLongitude();
        double currentLat = StateManager.getInstance().getX8Drone().getLatitude();
        float r = this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().getSurroundRadius(this.lastLog, this.lastLat, currentLng, currentLat);
        return r;
    }

    public void drawScrew(boolean b, int radius, int max) {
        this.isDraw = true;
        this.activity.getmMapVideoController().getFimiMap().onLocationEvnent();
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().addPolylinescircle(b, this.lastLat, this.lastLog, this.currentLat, this.currentLog, radius, max);
    }

    private void sendCircleCmd() {
        this.currentLog = StateManager.getInstance().getX8Drone().getLongitude();
        this.currentLat = StateManager.getInstance().getX8Drone().getLatitude();
        this.mFcManager.setAiSurroundCiclePoint(getLastLng(), getLastLat(), this.height, this.currentLog, this.currentLat, this.height, 2, new UiCallBackListener<AckNormalCmds>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckNormalCmds o) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.state = ScrewState.SETRADIUS;
                    X8AiScrewExcuteController.this.openNextUi();
                }
            }
        });
    }

    public void getParmeter() {
        if (!this.isDraw && this.state == ScrewState.RUNNING) {
            if (!this.isGetPoint) {
                getPoint();
            }
            if (!this.isGetDistance) {
                getMaxDistance();
            }
            if (!this.isGetSpeed) {
                getSpeed();
            }
            if (this.isGetPoint && this.isGetDistance && this.isGetSpeed) {
                drawScrew(this.cw, this.r, this.distance);
            }
        }
    }

    public void getPoint() {
        this.mFcManager.getAiSurroundCiclePoint(new UiCallBackListener<AckGetAiSurroundPoint>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetAiSurroundPoint ackGetAiSurroundPoint) {
                if (!cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.isGetPoint = false;
                    return;
                }
                X8AiScrewExcuteController.this.lastLat = ackGetAiSurroundPoint.getDeviceLatitude();
                X8AiScrewExcuteController.this.lastLog = ackGetAiSurroundPoint.getDeviceLongitude();
                X8AiScrewExcuteController.this.currentLat = ackGetAiSurroundPoint.getDeviceLatitudeTakeoff();
                X8AiScrewExcuteController.this.currentLog = ackGetAiSurroundPoint.getDeviceLongitudeTakeoff();
                X8AiScrewExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().setAiSurroundMark(X8AiScrewExcuteController.this.lastLat, X8AiScrewExcuteController.this.lastLog);
                X8AiScrewExcuteController.this.r = Math.round(X8AiScrewExcuteController.this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().getSurroundRadius(X8AiScrewExcuteController.this.lastLat, X8AiScrewExcuteController.this.lastLog, X8AiScrewExcuteController.this.currentLat, X8AiScrewExcuteController.this.currentLog));
                X8AiScrewExcuteController.this.isGetPoint = true;
            }
        });
    }

    public void getMaxDistance() {
        this.mFcManager.getScrewPrameter(new UiCallBackListener<AckAiScrewPrameter>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckAiScrewPrameter ackAiScrewPrameter) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.isGetDistance = true;
                    X8AiScrewExcuteController.this.distance = ackAiScrewPrameter.getDistance();
                    return;
                }
                X8AiScrewExcuteController.this.isGetDistance = false;
            }
        });
    }

    public void getSpeed() {
        this.mFcManager.getAiSurroundSpeed(new UiCallBackListener<AckAiSurrounds>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckAiSurrounds o) {
                if (cmdResult.isSuccess()) {
                    X8AiScrewExcuteController.this.isGetSpeed = true;
                    o.getSpeed();
                    if (o.getSpeed() > 0) {
                        X8AiScrewExcuteController.this.cw = true;
                        return;
                    } else {
                        X8AiScrewExcuteController.this.cw = false;
                        return;
                    }
                }
                X8AiScrewExcuteController.this.isGetSpeed = false;
            }
        });
    }

    public void closeIconByNextUi() {
        this.tvPoint.setVisibility(8);
        this.imgBack.setVisibility(8);
        this.flagSmall.setVisibility(8);
    }

    public void sysAiVcCtrlMode() {
        if (this.state != ScrewState.RUNNING) {
            if (this.timeSend == 0) {
                this.timeSend = 1;
                this.activity.getFcManager().sysCtrlMode2AiVc(new UiCallBackListener() {
                    @Override
                    public void onComplete(CmdResult cmdResult, Object o) {
                    }
                }, X8Task.VCM_SPIRAL.ordinal());
                return;
            }
            this.timeSend = 0;
        }
    }


    public enum ScrewState {
        IDLE,
        SETDOT,
        SETRADIUS,
        RUNNING
    }
}
