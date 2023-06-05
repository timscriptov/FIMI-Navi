package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8GimbalSettingListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8ValueSeakBarView;
import com.fimi.host.HostConstants;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AckGetPitchSpeed;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8GimbalItemController extends AbsX8Controllers implements View.OnClickListener {
    Button btnRestParams;
    ProgressBar pbRestsystemLoading;
    X8DoubleCustomDialog x8DoubleCustomDialog;
    LinearLayout x8LlAdvancedSetting;
    private Button btnGimbalCalibration;
    private Button btnHorizontalTrim;
    private FcCtrlManager fcCtrlManager;
    private X8GimbalManager gimbalManager;
    private boolean isConnected;
    private boolean isRequested;
    private IX8GimbalSettingListener listener;
    private final Context mContext;
    private View rlFcItem;
    private X8ValueSeakBarView sbPitchSpeed;
    private ViewStub stubFcItem;

    public X8GimbalItemController(View rootView) {
        super(rootView);
        this.isRequested = false;
        this.isConnected = false;
        this.mContext = rootView.getContext();
    }

    @Override
    public void initViews(View rootView) {
        this.stubFcItem = rootView.findViewById(R.id.stub_gimbal_item);
    }

    @Override
    public void initActions() {
        if (this.gimbalManager != null && this.sbPitchSpeed != null) {
            this.sbPitchSpeed.setConfirmListener(new X8ValueSeakBarView.OnProgressConfirmListener() {
                @Override
                public void onConfirm(float value) {
                    if (X8GimbalItemController.this.gimbalManager != null && StateManager.getInstance().getX8Drone().isConnect()) {
                        X8GimbalItemController.this.gimbalManager.setPitchSpeed((int) X8GimbalItemController.this.sbPitchSpeed.getCurrentValue(), new UiCallBackListener() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object o) {
                                ILinkMessage packet;
                                if (o != null && (packet = (ILinkMessage) o) != null && packet.getMsgRpt() != 16) {
                                    X8GimbalItemController.this.sbPitchSpeed.setImbConfirmEnable(false);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isConnected != b) {
            this.isConnected = b;
            if (this.isShow && this.rlFcItem != null) {
                setViewEnabled(b);
                if (b && !this.isRequested) {
                    requestValue();
                    this.isRequested = true;
                }
            }
        }
    }

    @Override
    public void showItem() {
        if (this.rlFcItem == null) {
            View view = this.stubFcItem.inflate();
            this.rlFcItem = view.findViewById(R.id.x8_rl_main_gimbal_item);
            this.btnGimbalCalibration = view.findViewById(R.id.btn_gimbal_calibration);
            this.btnHorizontalTrim = view.findViewById(R.id.btn_horizontal_trim);
            this.sbPitchSpeed = view.findViewById(R.id.vsb_pitching_speed_limit);
            this.btnRestParams = view.findViewById(R.id.btn_rest_params);
            this.pbRestsystemLoading = view.findViewById(R.id.pb_restsystem_loading);
            this.x8LlAdvancedSetting = view.findViewById(R.id.x8_ll_advanced_setting);
            this.btnRestParams.setOnClickListener(this);
            this.btnGimbalCalibration.setOnClickListener(this);
            this.btnHorizontalTrim.setOnClickListener(this);
            this.x8LlAdvancedSetting.setOnClickListener(this);
            requestValue();
            initActions();
        }
        this.rlFcItem.setVisibility(0);
        this.isShow = true;
    }

    public void requestValue() {
        boolean isConnect = StateManager.getInstance().getX8Drone().isConnect();
        setViewEnabled(isConnect);
        if (isConnect && this.gimbalManager != null) {
            this.gimbalManager.getPitchSpeed(new UiCallBackListener<AckGetPitchSpeed>() {
                @Override
                public void onComplete(CmdResult cmdResult, AckGetPitchSpeed obj) {
                    if (cmdResult.isSuccess()) {
                        X8GimbalItemController.this.sbPitchSpeed.setProgress(obj.getSpeed());
                        X8GimbalItemController.this.sbPitchSpeed.setImbConfirmEnable(false);
                    }
                }
            });
        }
    }

    public void setViewEnabled(boolean isEnabled) {
        if (this.rlFcItem != null) {
            this.btnHorizontalTrim.setEnabled(isEnabled);
            this.sbPitchSpeed.setViewEnable(isEnabled);
            boolean isOngroud = StateManager.getInstance().getX8Drone().isOnGround();
            this.btnRestParams.setEnabled(isOngroud && isEnabled);
            this.btnRestParams.setAlpha((isOngroud && isEnabled) ? 1.0f : 0.4f);
            if (isEnabled) {
                this.btnHorizontalTrim.setAlpha(1.0f);
            } else {
                this.btnHorizontalTrim.setAlpha(0.4f);
            }
        }
    }

    @Override
    public void closeItem() {
        if (this.rlFcItem != null) {
            this.rlFcItem.setVisibility(8);
            this.isShow = false;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_gimbal_calibration) {
            if (this.listener != null) {
                this.listener.onGimbalCalibrationClick();
            }
        } else if (i == R.id.btn_horizontal_trim) {
            if (this.listener != null) {
                this.listener.onHorizontalTrimClick();
            }
        } else if (i == R.id.btn_rest_params) {
            showRestParamDialog();
        } else if (i == R.id.x8_ll_advanced_setting) {
            this.listener.onAdvancedSetup();
        }
    }

    public void setListener(IX8GimbalSettingListener listener) {
        this.listener = listener;
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setGimbalManager(X8GimbalManager gimbalManager) {
        this.gimbalManager = gimbalManager;
    }

    public void showRestParamDialog() {
        if (this.x8DoubleCustomDialog == null) {
            this.x8DoubleCustomDialog = new X8DoubleCustomDialog(this.mContext, this.mContext.getString(R.string.x8_gimbal_setting_gimbal_reset_params), this.mContext.getString(R.string.x8_gimbale_settting_rest_params_content), this.mContext.getString(R.string.x8_general_rest), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8GimbalItemController.this.resetGimbalSystemParams();
                    SPStoreManager.getInstance().saveObject(HostConstants.SP_KEY_NOT_TIPS, false);
                }
            });
        }
        this.x8DoubleCustomDialog.show();
    }

    public void resetGimbalSystemParams() {
        this.gimbalManager.resetGCParams(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8ToastUtil.showToast(X8GimbalItemController.this.mContext, X8GimbalItemController.this.getString(R.string.x8_gimbale_settting_rest_params_result_success), 0);
                } else {
                    X8ToastUtil.showToast(X8GimbalItemController.this.mContext, X8GimbalItemController.this.getString(R.string.x8_gimbale_settting_rest_params_result_failed), 0);
                }
                X8GimbalItemController.this.requestValue();
            }
        });
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }
}
