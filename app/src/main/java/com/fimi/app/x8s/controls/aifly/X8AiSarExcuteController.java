package com.fimi.app.x8s.controls.aifly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.X8Application;
import com.fimi.app.x8s.interfaces.AbsX8AiController;
import com.fimi.app.x8s.interfaces.IX8AiSarListener;
import com.fimi.app.x8s.manager.X8ScreenShotManager;
import com.fimi.app.x8s.tools.WGS84ToSK42;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.DeviceNorthView;
import com.fimi.app.x8s.widget.X8AiTipWithCloseView;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8VerticalSeekBarValueLayout;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.X8CameraSettings;

public class X8AiSarExcuteController extends AbsX8AiController implements View.OnClickListener, X8DoubleCustomDialog.onDialogButtonClickListener {
    private final X8sMainActivity activity;
    protected int MAX_WIDTH;
    protected boolean isShow;
    protected int width;
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

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        taskExit();
    }

    public void setListener(IX8AiSarListener listener) {
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
    public void onClick(@NonNull View v) {
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
            if (this.tvFlag.getVisibility() == View.VISIBLE) {
                this.tvFlag.setVisibility(View.GONE);
            } else {
                this.tvFlag.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_ai_sar_excute_layout, (ViewGroup) this.rootView, true);
        this.tvLatlng = this.handleView.findViewById(R.id.tv_latlng);
        this.tvTime = this.handleView.findViewById(R.id.tv_time);
        this.mContentTip = this.handleView.findViewById(R.id.v_sar_content_tip);
        this.mContentTip.setTipText(this.rootView.getContext().getString(R.string.x8_ai_fly_sar_content_tip));
        this.imgBack = this.handleView.findViewById(R.id.img_ai_follow_back);
        this.imgSwith = this.handleView.findViewById(R.id.img_ai_map_switch);
        this.imgShot = this.handleView.findViewById(R.id.img_ai_screen_shot);
        this.sbLayout = this.handleView.findViewById(R.id.sb_switch_focus);
        this.flagSmall = this.handleView.findViewById(R.id.rl_flag_small);
        this.tvFlag = this.handleView.findViewById(R.id.tv_task_tip);
        this.flagSmall.setOnClickListener(this);
        this.listener.onAiSarRunning();
        this.imgBack.setOnClickListener(this);
        this.imgSwith.setOnClickListener(this);
        this.imgShot.setOnClickListener(this);
        if (this.activity.getmMapVideoController().isFullVideo()) {
            this.sbLayout.setVisibility(View.VISIBLE);
            this.imgSwith.setVisibility(View.GONE);
        } else {
            this.imgSwith.setVisibility(View.VISIBLE);
            this.sbLayout.setVisibility(View.GONE);
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
    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.mFcCtrlManager = fcManager;
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
        String m = this.rootView.getContext().getString(R.string.x8_ai_fly_sar_exite_tip);
        this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        this.dialog.show();
    }

    public void taskExit() {
        onTaskComplete(true);
    }

    @Override
    public void cancleByModeChange() {
        onTaskComplete(false);
    }

    @Override
    public void onDroneConnected(boolean b) {
        double[] position;
        if (this.isShow) {
            if (!b) {
                ononDroneDisconnectedTaskComplete();
            }
            if (b && this.tvTime != null && (position = this.activity.getmMapVideoController().getFimiMap().getDevicePosition()) != null) {
//                this.tvLatlng.setText("" + position[0] + "," + position[1]);
                TextView textView = this.tvLatlng;
                double[] skTarget = WGS84ToSK42.WGS84ToSK42Meters(position[0], position[1], 0.0d);
                textView.setText("X:" + ((int) skTarget[0]) + ", Y:" + ((int) skTarget[1]));
                DeviceNorthView deviceNorthView = this.activity.mX8MainDeviceNorthView;
                if (deviceNorthView != null) {
                    double[] skTarget2 = WGS84ToSK42.WGS84ToSK42MetersWithShift(position[0], position[1], 0.0d, deviceNorthView.aircraftAzimuth, deviceNorthView.distance);
                    String oldText = textView.getText().toString();
                    if (oldText.isEmpty()) {
                        textView.setText("✜ X:" + ((int) skTarget2[0]) + ", Y:" + ((int) skTarget2[1]));
                    } else {
                        textView.setText(oldText + "\n✜ X:" + ((int) skTarget2[0]) + ", Y:" + ((int) skTarget2[1]));
                    }
                }
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
        this.mFcCtrlManager.setEnableTripod(0, (cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                taskExit();
            }
        });
    }

    public void switchMapVideo(boolean sightFlag) {
        if (this.handleView != null && this.isShow) {
            if (!sightFlag) {
                this.sbLayout.setVisibility(View.VISIBLE);
                this.imgSwith.setVisibility(View.GONE);
                return;
            }
            this.imgSwith.setVisibility(View.VISIBLE);
            this.sbLayout.setVisibility(View.GONE);
        }
    }

    public void setd() {
    }
}
