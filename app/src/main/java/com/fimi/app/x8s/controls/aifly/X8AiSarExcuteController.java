package com.fimi.app.x8s.controls.aifly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiSarListener;
import com.fimi.app.x8s.manager.X8ScreenShotManager;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8VerticalSeekBarValueLayout;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.X8CameraSettings;

/* loaded from: classes.dex */
public class X8AiSarExcuteController extends AbsX8AiController implements View.OnClickListener, X8DoubleCustomDialog.onDialogButtonClickListener {
    protected int MAX_WIDTH;
    protected boolean isShow;
    protected int width;
    private X8sMainActivity activity;
    private X8DoubleCustomDialog dialog;
    private View flagSmall;
    private ImageView imgBack;
    private ImageView imgShot;
    private ImageView imgSwith;
    private IX8AiSarListener listener;
    private X8AiTipWithCloseView mContentTip;
    private FcCtrlManager mFcCtrlManager;
    private X8VerticalSeekBarValueLayout sbLayout;
    private TextView tvFlag;
    private TextView tvLatlng;
    private TextView tvTime;

    public X8AiSarExcuteController(X8sMainActivity activity, View rootView) {
        super(rootView);
        this.width = X8Application.ANIMATION_WIDTH;
        this.activity = activity;
    }

    @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
    public void onLeft() {
    }

    @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
    public void onRight() {
        taskExit();
    }

    public void setListener(IX8AiSarListener listener) {
        this.listener = listener;
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
    }

    public void initViewStubViews(View view) {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public boolean onClickBackKey() {
        return false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_back) {
            showExitDialog();
        } else if (id == R.id.img_ai_map_switch) {
            int mapType = GlobalConfig.getInstance().getMapStyle();
            if (mapType == Constants.X8_GENERAL_MAP_STYLE_NORMAL) {
                GlobalConfig.getInstance().setMapStyle(Constants.X8_GENERAL_MAP_STYLE_SATELLITE);
                this.activity.getmMapVideoController().getFimiMap().switchMapStyle(Constants.X8_GENERAL_MAP_STYLE_SATELLITE);
                return;
            }
            GlobalConfig.getInstance().setMapStyle(Constants.X8_GENERAL_MAP_STYLE_NORMAL);
            this.activity.getmMapVideoController().getFimiMap().switchMapStyle(Constants.X8_GENERAL_MAP_STYLE_NORMAL);
        } else if (id == R.id.img_ai_screen_shot) {
            if (!X8ScreenShotManager.isBusy) {
                new X8ScreenShotManager().starThread(this.activity);
            }
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
        this.handleView = inflater.inflate(R.layout.x8_ai_sar_excute_layout, (ViewGroup) this.rootView, true);
        this.tvLatlng = (TextView) this.handleView.findViewById(R.id.tv_latlng);
        this.tvTime = (TextView) this.handleView.findViewById(R.id.tv_time);
        this.mContentTip = (X8AiTipWithCloseView) this.handleView.findViewById(R.id.v_sar_content_tip);
        this.mContentTip.setTipText(this.rootView.getContext().getString(R.string.x8_ai_fly_sar_content_tip));
        this.imgBack = (ImageView) this.handleView.findViewById(R.id.img_ai_follow_back);
        this.imgSwith = (ImageView) this.handleView.findViewById(R.id.img_ai_map_switch);
        this.imgShot = (ImageView) this.handleView.findViewById(R.id.img_ai_screen_shot);
        this.sbLayout = (X8VerticalSeekBarValueLayout) this.handleView.findViewById(R.id.sb_switch_focus);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.tvFlag = (TextView) this.handleView.findViewById(R.id.tv_task_tip);
        this.flagSmall.setOnClickListener(this);
        this.listener.onAiSarRunning();
        this.imgBack.setOnClickListener(this);
        this.imgSwith.setOnClickListener(this);
        this.imgShot.setOnClickListener(this);
        if (this.activity.getmMapVideoController().isFullVideo()) {
            this.sbLayout.setVisibility(0);
            this.imgSwith.setVisibility(8);
        } else {
            this.imgSwith.setVisibility(0);
            this.sbLayout.setVisibility(8);
        }
        this.sbLayout.setMinMax(X8CameraSettings.getMinMaxFocuse(), this.activity.getCameraManager());
        setProgress();
        super.openUi();
    }

    public void setProgress() {
        if (this.sbLayout != null) {
            this.sbLayout.setProgress(X8CameraSettings.getFocuse());
        }
    }

    public void changeProcessByRc(boolean b) {
        if (this.sbLayout != null) {
            this.sbLayout.changeProcess(b);
        }
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8AiController, com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.mFcCtrlManager = fcManager;
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public boolean isShow() {
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 4) {
            return false;
        }
        return this.isShow;
    }

    public void showExitDialog() {
        String t = this.rootView.getContext().getString(R.string.x8_ai_fixedwing_exite_title);
        String m = this.rootView.getContext().getString(R.string.x8_ai_fly_sar_exite_tip);
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete(true);
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void cancleByModeChange() {
        onTaskComplete(false);
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void onDroneConnected(boolean b) {
        double[] position;
        if (this.isShow) {
            if (!b) {
                ononDroneDisconnectedTaskComplete();
            }
            if (b && this.tvTime != null && (position = this.activity.getmMapVideoController().getFimiMap().getDevicePosition()) != null) {
                this.tvLatlng.setText("" + position[0] + "," + position[1]);
                this.tvTime.setText(DateUtil.getStringByFormat(System.currentTimeMillis(), "yyyyMMdd HH:mm:ss"));
            }
        }
    }

    private void onTaskComplete(boolean b) {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onAiSarComplete(b);
        }
    }

    public void ononDroneDisconnectedTaskComplete() {
        StateManager.getInstance().getX8Drone().resetCtrlMode();
        closeFixedwing();
        if (this.listener != null) {
            this.listener.onAiSarComplete(false);
        }
    }

    private void closeFixedwing() {
        closeUi();
        if (this.listener != null) {
            this.listener.onAiSarBackClick();
        }
    }

    public void setTypeEnable() {
        this.mFcCtrlManager.setEnableTripod(0, new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.X8AiSarExcuteController.1
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiSarExcuteController.this.taskExit();
                }
            }
        });
    }

    public void switchMapVideo(boolean sightFlag) {
        if (this.handleView != null && this.isShow) {
            if (!sightFlag) {
                this.sbLayout.setVisibility(0);
                this.imgSwith.setVisibility(8);
                return;
            }
            this.imgSwith.setVisibility(0);
            this.sbLayout.setVisibility(8);
        }
    }

    public void setd() {
    }
}
