package com.fimi.app.x8s.controls.fcsettting;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.camera.X8MainCameraSettingController;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.x8sdk.controller.FiveKeyDefineManager;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.modulestate.GimbalState;
import com.fimi.x8sdk.presenter.FiveKeyDefinePresenter;

/* loaded from: classes.dex */
public class X8FiveKeyHintController extends AbsX8MenuBoxControllers implements SeekBar.OnSeekBarChangeListener {
    private double angle;
    private int currentValue;
    private FiveKeyDefineManager fiveKeyDefineManager;
    private View parentView;
    private SeekBar sbarFiveKey;
    private TextView tvFiveKeyShowType;
    private X8GimbalManager x8GimbalManager;
    private X8MainCameraSettingController x8MainCameraSettingController;

    public X8FiveKeyHintController(View rootView) {
        super(rootView);
    }

    public void showSportState(GimbalState state) {
        int pitchAngle = state.getPitchAngle();
        this.angle = pitchAngle / 100.0d;
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
        this.parentView = rootView.findViewById(R.id.main_all_setting_five_key);
        this.tvFiveKeyShowType = (TextView) this.parentView.findViewById(R.id.tv_five_key_show_type);
        this.sbarFiveKey = (SeekBar) this.parentView.findViewById(R.id.sbar_five_key);
        this.sbarFiveKey.setProgress(90);
        this.sbarFiveKey.setOnSeekBarChangeListener(this);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        super.closeUi();
        this.isShow = false;
        this.parentView.setVisibility(8);
        if (this.fiveKeyDefineManager != null) {
            this.fiveKeyDefineManager.restoreUpDownKey(false);
        }
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        super.openUi();
        this.isShow = true;
        this.parentView.setVisibility(0);
        String fiveKeyShowTypeStr = this.tvFiveKeyShowType.getText().toString();
        if (fiveKeyShowTypeStr.contains(getString(R.string.x8_camera_contrast))) {
            this.fiveKeyDefineManager.isSetCameraContrast();
        } else {
            this.fiveKeyDefineManager.isSetCameraSaturation();
        }
    }

    public void setTvFiveKeyShowType(String str) {
        this.tvFiveKeyShowType.setText(str);
    }

    public void setSbarFiveKey(String currentValue) {
        if (!currentValue.equals("")) {
            int currentValueInt = Integer.parseInt(currentValue);
            this.sbarFiveKey.setProgress(currentValueInt);
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (this.currentValue != i) {
            this.currentValue = i;
            if (this.currentValue >= 0 && this.currentValue <= 256) {
                String fiveKeyShowTypeStr = this.tvFiveKeyShowType.getText().toString();
                if (fiveKeyShowTypeStr.contains(getString(R.string.x8_camera_contrast))) {
                    this.tvFiveKeyShowType.setText(String.format(getString(R.string.x8_rc_setting_five_key_show_type), getString(R.string.x8_camera_contrast), Integer.valueOf(i)));
                    if (!AbAppUtil.isFastClick(400)) {
                        this.x8MainCameraSettingController.fiveKeySetContrast(FiveKeyDefinePresenter.ParameterType.ORIGINAL_VALUE, this.currentValue);
                        return;
                    }
                    return;
                }
                this.tvFiveKeyShowType.setText(String.format(getString(R.string.x8_rc_setting_five_key_show_type), getString(R.string.x8_camera_saturation), Integer.valueOf(i)));
                if (!AbAppUtil.isFastClick(400)) {
                    this.x8MainCameraSettingController.fiveKeySetSaturation(FiveKeyDefinePresenter.ParameterType.ORIGINAL_VALUE, this.currentValue);
                }
            }
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (this.currentValue >= 0 && this.currentValue <= 256) {
            String fiveKeyShowTypeStr = this.tvFiveKeyShowType.getText().toString();
            if (fiveKeyShowTypeStr.contains(getString(R.string.x8_camera_contrast))) {
                this.x8MainCameraSettingController.fiveKeySetContrast(FiveKeyDefinePresenter.ParameterType.ORIGINAL_VALUE, this.currentValue);
            } else {
                this.x8MainCameraSettingController.fiveKeySetSaturation(FiveKeyDefinePresenter.ParameterType.ORIGINAL_VALUE, this.currentValue);
            }
        }
    }

    public void setFiveKeyPitchAngle() {
        if (this.angle == 0.0d) {
            this.x8GimbalManager.setAiAutoPhotoPitchAngle(-90, null);
        } else {
            this.x8GimbalManager.setAiAutoPhotoPitchAngle(0, null);
        }
    }

    public void setX8MainCameraSettingController(X8MainCameraSettingController x8MainCameraSettingController) {
        this.x8MainCameraSettingController = x8MainCameraSettingController;
    }

    public void setFiveKeyDefineManager(FiveKeyDefineManager fiveKeyDefineManager) {
        this.fiveKeyDefineManager = fiveKeyDefineManager;
    }

    public void setFiveKeyDefineGimbalManager(X8GimbalManager x8GimbalManager) {
        this.x8GimbalManager = x8GimbalManager;
    }
}
