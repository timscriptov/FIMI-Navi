package com.fimi.app.x8s.controls.aifly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiAerialPhotographNextUi;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AerialGraphListener;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetSensitivity;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiAerialPhotographExcuteController extends AbsX8AiController implements View.OnClickListener, X8DoubleCustomDialog.onDialogButtonClickListener {
    protected int MAX_WIDTH;
    protected boolean isShow;
    protected int width;
    private final X8sMainActivity activity;
    private View blank;
    private Button btnOk;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgBack;
    private ImageView imgChangeAngle;
    private ImageView imgEdit;
    private ImageView imgLockAngle;
    private ImageView imgLockBg;
    private boolean isNextShow;
    private IX8AerialGraphListener listener;
    private FcCtrlManager mFcCtrlManager;
    private X8AiAerialPhotographNextUi mX8AiAerialPhotographNextUi;
    private View nextRootView;
    private String prex;
    private TextView tvAngle;
    private TextView tvFlag;
    private TextView tvTitle;
    private View vSensity;
    private View vSetAngle;

    public X8AiAerialPhotographExcuteController(X8sMainActivity activity, View rootView) {
        super(rootView);
        this.width = X8Application.ANIMATION_WIDTH;
        this.activity = activity;
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        setTypeEnable();
    }

    public void setListener(IX8AerialGraphListener listener) {
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
            showExitDialog();
        } else if (id == R.id.img_change_angle) {
            openNextUi(2);
        } else if (id == R.id.x8_head_lock_next_blank) {
            closeNextUi();
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            closeNextUi();
        } else if (id == R.id.img_edit) {
            openNextUi(1);
        } else if (id == R.id.rl_flag_small) {
            if (this.tvFlag.getVisibility() == 0) {
                this.tvFlag.setVisibility(8);
            } else {
                this.tvFlag.setVisibility(0);
            }
        }
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8AiController, com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_aerial_photograph_excute_layout, (ViewGroup) this.rootView, true);
        this.imgBack = this.handleView.findViewById(R.id.img_ai_follow_back);
        this.nextRootView = this.rootView.findViewById(R.id.v_x8_head_lock_next);
        this.blank = this.rootView.findViewById(R.id.x8_head_lock_next_blank);
        this.imgChangeAngle = this.handleView.findViewById(R.id.img_change_angle);
        this.imgLockBg = this.rootView.findViewById(R.id.img_lock_bg);
        this.imgLockAngle = this.rootView.findViewById(R.id.img_lock_angle);
        this.imgLockBg.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_img_head_lock_bg));
        this.imgLockAngle.setImageBitmap(ImageUtils.getBitmapByPath(this.rootView.getContext(), R.drawable.x8_img_head_lock_arrow));
        this.btnOk = this.rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.vSensity = this.rootView.findViewById(R.id.rl_aerail_sensity);
        this.vSetAngle = this.rootView.findViewById(R.id.rl_head_lock_setangle);
        this.imgEdit = this.rootView.findViewById(R.id.img_edit);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.tvFlag = this.handleView.findViewById(R.id.tv_task_tip);
        this.flagSmall.setOnClickListener(this);
        this.tvAngle = this.rootView.findViewById(R.id.tv_lock_angle);
        this.prex = this.rootView.getContext().getString(R.string.x8_ai_heading_lock_tip3);
        this.tvAngle.setText(String.format(this.prex, Float.valueOf(60.0f)));
        this.imgLockAngle.setRotation(60.0f);
        this.listener.onAerialGraphRunning();
        this.imgBack.setOnClickListener(this);
        this.imgChangeAngle.setOnClickListener(this);
        this.blank.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.imgEdit.setOnClickListener(this);
        this.mX8AiAerialPhotographNextUi = new X8AiAerialPhotographNextUi(this.rootView);
        super.openUi();
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8AiController, com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    public void openNextUi(int type) {
        this.nextRootView.setVisibility(0);
        this.blank.setVisibility(0);
        if (!this.isNextShow) {
            this.mX8AiAerialPhotographNextUi.setType(type);
            if (type == 1) {
                this.vSensity.setVisibility(0);
                this.vSetAngle.setVisibility(8);
                this.mX8AiAerialPhotographNextUi.setTitle(this.activity.getString(R.string.x8_ai_aerail_graph_next_title));
                this.btnOk.setText(this.activity.getString(R.string.x8_save));
                this.mX8AiAerialPhotographNextUi.setSensity();
            } else {
                this.vSensity.setVisibility(8);
                this.vSetAngle.setVisibility(0);
                this.mX8AiAerialPhotographNextUi.setTitle(this.activity.getString(R.string.x8_ai_heading_lock_update));
                this.btnOk.setText(this.activity.getString(R.string.x8_ai_fly_lines_point_action_update));
            }
            this.isNextShow = true;
            this.width = X8Application.ANIMATION_WIDTH;
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(this.nextRootView, "translationX", this.width, 0.0f);
            animatorY.setDuration(300L);
            animatorY.start();
        }
        this.activity.taskFullScreen(true);
    }

    public void closeNextUi() {
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
                    X8AiAerialPhotographExcuteController.this.nextRootView.setVisibility(8);
                }
            });
        }
        this.activity.taskFullScreen(false);
        if (this.mX8AiAerialPhotographNextUi.isSaveData()) {
            setSensity();
        }
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.mFcCtrlManager = fcManager;
        this.mFcCtrlManager.getBrakeSens(new UiCallBackListener<AckGetSensitivity>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
            }
        });
        this.mFcCtrlManager.getYawTrip(new UiCallBackListener<AckGetSensitivity>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetSensitivity sensitivity) {
            }
        });
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
        String m = this.rootView.getContext().getString(R.string.x8_ai_aerail_graph_exite_tip);
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete();
    }

    @Override
    public void cancleByModeChange() {
        onTaskComplete();
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow && !b) {
            ononDroneDisconnectedTaskComplete();
        }
    }

    private void onTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onAerialGraphComplete(true);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onAerialGraphComplete(false);
        }
    }

    private void closeFixedwing() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAerialGraphBackClick();
        }
    }

    public void showSportState(AutoFcSportState state) {
        float angle = state.getDeviceAngle();
        this.tvAngle.setText(String.format(this.prex, Float.valueOf(angle)));
        this.imgLockAngle.setRotation(angle);
    }

    public void setTypeEnable() {
        this.mFcCtrlManager.setEnableAerailShot(0, new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAerialPhotographExcuteController.this.taskExit();
                }
            }
        });
    }

    public void setSensity() {
        final int braking = this.mX8AiAerialPhotographNextUi.getBrakingSensity();
        final int yaw = this.mX8AiAerialPhotographNextUi.getYawSensity();
        this.mFcCtrlManager.setBrakeSens(new UiCallBackListener<Object>() {
            @Override
            public void onComplete(CmdResult cmdResult, Object resp) {
                if (cmdResult.isSuccess()) {
                    StateManager.getInstance().getX8Drone().setFcBrakeSenssity(braking);
                }
            }
        }, braking, braking);
        this.mFcCtrlManager.setYawSensitivity(new UiCallBackListener<Object>() {
            @Override
            public void onComplete(CmdResult cmdResult, Object respCode) {
                if (cmdResult.isSuccess()) {
                    StateManager.getInstance().getX8Drone().setFcYAWSenssity(yaw);
                }
            }
        }, yaw);
    }
}
