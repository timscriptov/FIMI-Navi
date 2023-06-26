package com.fimi.app.x8s.controls.fcsettting;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.fcsettting.maintain.X8BlackBoxController;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.network.ApkVersionManager;
import com.fimi.widget.CustomLoadManage;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.cmdsenum.X8Format;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AckGetFormatStorageState;
import com.fimi.x8sdk.entity.ConectState;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8ModifyModeController extends AbsX8MenuBoxControllers implements X8DoubleCustomDialog.onDialogButtonClickListener {
    private final ApkVersionManager apkVersionManager;
    private final Handler mHandler;
    private X8AircraftCalibrationController aircraftCalibrationController;
    private ImageView back_btn;
    private RelativeLayout blackBoxLayout;
    private RelativeLayout blackBoxLogLayout;
    private View blackBoxView;
    private View calibrationView;
    private ViewStub calibrationViewStub;
    private Button calibration_btn;
    private RelativeLayout checkLayout;
    private View checkView;
    private X8DoubleCustomDialog dialog;
    private FcCtrlManager fcCtrlManager;
    private View gimbalSensorView;
    private ViewStub gimbalSensorViewStub;
    private PercentRelativeLayout itemLayout;
    private FormatState mFormatState;
    private RelativeLayout mFormatStorage;
    private X8modifyGimbalSensorController mGimbalSensorController;
    private RelativeLayout mGimbalSensorLayout;
    private ImageView mIvFormatStorage;
    private CustomLoadManage mLoadManage;
    private ProgressBar mPbFormat;
    private TextView mTvFormatDec;
    private TextView mTvFormatTitle;
    private X8BlackBoxController mX8BlackBoxController;
    private X8GimbalManager mX8GimbalManager;
    private IX8GeneraModifyModeControllerListener modeControllerListener;
    private RelativeLayout nextContentLayout;
    private X8ModifySensorController sensorController;
    private RelativeLayout sensorLayout;
    private View sensorView;
    private ViewStub sensorViewStub;
    private PercentRelativeLayout topLayout;
    private RelativeLayout x8AircraftCalibrationLayout;

    public X8ModifyModeController(View rootView) {
        super(rootView);
        this.mFormatState = FormatState.IDLE;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (AnonymousClass14.$SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8ModifyModeController$FormatState[X8ModifyModeController.this.mFormatState.ordinal()]) {
                    case 2:
                        X8ModifyModeController.this.setFormatLogStorage();
                        break;
                    case 3:
                        X8ModifyModeController.this.setFormatUpgradeStorage();
                        break;
                    case 4:
                        X8ModifyModeController.this.getFormatState();
                        break;
                }
                X8ModifyModeController.this.mHandler.sendEmptyMessageDelayed(0, 1000L);
            }
        };
        this.apkVersionManager = new ApkVersionManager();
    }

    public void setX8GimbalManager(X8GimbalManager x8GimbalManager) {
        this.mX8GimbalManager = x8GimbalManager;
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setModeControllerListener(IX8GeneraModifyModeControllerListener modeControllerListener) {
        this.modeControllerListener = modeControllerListener;
    }

    @Override
    public void initViews(View rootView) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_main_general_item_modify_layout, (ViewGroup) rootView, true);
        this.back_btn = this.handleView.findViewById(R.id.btn_return);
        this.sensorLayout = this.handleView.findViewById(R.id.x8_sensor_layout);
        this.mGimbalSensorLayout = this.handleView.findViewById(R.id.x8_gimbal_sensor_layout);
        this.calibration_btn = this.handleView.findViewById(R.id.x8_btn_calibration);
        this.checkLayout = this.handleView.findViewById(R.id.x8_check_layout);
        this.blackBoxLayout = this.handleView.findViewById(R.id.x8_blackbox_layout);
        this.topLayout = this.handleView.findViewById(R.id.layout_top);
        this.itemLayout = this.handleView.findViewById(R.id.x8_modify_item_layout);
        this.blackBoxLogLayout = this.handleView.findViewById(R.id.x8_blackbox_layout1);
        this.x8AircraftCalibrationLayout = this.handleView.findViewById(R.id.x8_aircraft_calibration_layout);
        this.mFormatStorage = this.handleView.findViewById(R.id.x8_format_storage_layout);
        this.mIvFormatStorage = this.handleView.findViewById(R.id.iv_format_storage);
        this.mTvFormatDec = this.handleView.findViewById(R.id.tv_format_storage_dec);
        this.mTvFormatTitle = this.handleView.findViewById(R.id.tv_format_storage_title);
        this.mPbFormat = this.handleView.findViewById(R.id.pb_format_storage);
        this.nextContentLayout = this.handleView.findViewById(R.id.rl_next_content);
    }

    @Override
    public void initActions() {
        this.calibration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8ModifyModeController.this.topLayout.setVisibility(View.GONE);
                X8ModifyModeController.this.itemLayout.setVisibility(View.GONE);
                X8ModifyModeController.this.modeControllerListener.onOpen();
                if (X8ModifyModeController.this.calibrationViewStub == null) {
                    X8ModifyModeController.this.calibrationViewStub = X8ModifyModeController.this.handleView.findViewById(R.id.x8_calibration_view);
                    X8ModifyModeController.this.calibrationView = X8ModifyModeController.this.calibrationViewStub.inflate();
                    X8ModifyModeController.this.aircraftCalibrationController = new X8AircraftCalibrationController(X8ModifyModeController.this.calibrationView);
                }
                X8ModifyModeController.this.aircraftCalibrationController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.aircraftCalibrationController.showItem();
                X8ModifyModeController.this.aircraftCalibrationController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() {
                    @Override
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(View.VISIBLE);
                        X8ModifyModeController.this.itemLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onOpen() {
                    }

                    @Override
                    public void onClose() {
                        X8ModifyModeController.this.modeControllerListener.onClose();
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(View.GONE);
                X8ModifyModeController.this.itemLayout.setVisibility(View.GONE);
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                X8ModifyModeController.this.handleView.setVisibility(View.GONE);
                X8ModifyModeController.this.modeControllerListener.returnBack();
            }
        });
        this.mGimbalSensorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (X8ModifyModeController.this.gimbalSensorViewStub == null) {
                    X8ModifyModeController.this.gimbalSensorViewStub = X8ModifyModeController.this.handleView.findViewById(R.id.x8_gimbal_sensor_view);
                    X8ModifyModeController.this.gimbalSensorView = X8ModifyModeController.this.gimbalSensorViewStub.inflate();
                }
                X8ModifyModeController.this.mGimbalSensorController = new X8modifyGimbalSensorController(X8ModifyModeController.this.gimbalSensorView);
                X8ModifyModeController.this.mGimbalSensorController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.mGimbalSensorController.setX8GimbalManager(X8ModifyModeController.this.mX8GimbalManager);
                X8ModifyModeController.this.mGimbalSensorController.showItem();
                X8ModifyModeController.this.mGimbalSensorController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() {
                    @Override
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(View.VISIBLE);
                        X8ModifyModeController.this.itemLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onOpen() {
                    }

                    @Override
                    public void onClose() {
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(View.GONE);
                X8ModifyModeController.this.itemLayout.setVisibility(View.GONE);
            }
        });
        this.sensorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (X8ModifyModeController.this.sensorViewStub == null) {
                    X8ModifyModeController.this.sensorViewStub = X8ModifyModeController.this.handleView.findViewById(R.id.x8_sensor_view);
                    X8ModifyModeController.this.sensorView = X8ModifyModeController.this.sensorViewStub.inflate();
                    X8ModifyModeController.this.sensorController = new X8ModifySensorController(X8ModifyModeController.this.sensorView);
                }
                X8ModifyModeController.this.sensorController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.sensorController.showItem();
                X8ModifyModeController.this.sensorController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() {
                    @Override
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(View.VISIBLE);
                        X8ModifyModeController.this.itemLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onOpen() {
                    }

                    @Override
                    public void onClose() {
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(View.GONE);
                X8ModifyModeController.this.itemLayout.setVisibility(View.GONE);
            }
        });
        this.checkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.blackBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.blackBoxLogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                X8ModifyModeController.this.topLayout.setVisibility(View.GONE);
                X8ModifyModeController.this.itemLayout.setVisibility(View.GONE);
                X8ModifyModeController.this.mX8BlackBoxController = new X8BlackBoxController(X8ModifyModeController.this.nextContentLayout, X8ModifyModeController.this);
                X8ModifyModeController.this.modeControllerListener.onOpen();
            }
        });
        this.mFormatStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8ModifyModeController.this.showExitDialog();
            }
        });
    }

    public void showExitDialog() {
        if (this.dialog == null) {
            String t = this.rootView.getContext().getString(R.string.x8_modify_format_storage_dialog_title);
            String m = this.rootView.getContext().getString(R.string.x8_modify_format_storage_dialog_dec);
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, this);
        }
        this.dialog.show();
    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        this.mFormatState = FormatState.SEND_FORMAT_LOG;
        this.mHandler.sendEmptyMessage(0);
        if (this.mLoadManage == null) {
            this.mLoadManage = new CustomLoadManage();
        }
        CustomLoadManage customLoadManage = this.mLoadManage;
        CustomLoadManage.showNoClickWithOutProgressBar(this.rootView.getContext(), true);
        onFormatStorage();
    }

    private void onFormatStorage() {
        this.mTvFormatTitle.setText(this.rootView.getContext().getString(R.string.x8_modify_format_storage1));
        this.mIvFormatStorage.setVisibility(View.GONE);
        this.mTvFormatDec.setVisibility(View.VISIBLE);
        this.mPbFormat.setVisibility(View.VISIBLE);
    }

    public void recoverFormatState() {
        this.mTvFormatTitle.setText(this.rootView.getContext().getString(R.string.x8_modify_format_storage));
        this.mIvFormatStorage.setVisibility(View.VISIBLE);
        this.mTvFormatDec.setVisibility(View.GONE);
        this.mPbFormat.setVisibility(View.GONE);
    }

    public void setFormatLogStorage() {
        this.fcCtrlManager.setFormatStorage(X8Format.FormatType.FORMAT_SENIOR.ordinal(), X8Format.FormatDevid.FORMAT_DEVID_LOG.ordinal(), new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess) {
                    X8ToastUtil.showToast(X8ModifyModeController.this.rootView.getContext(), X8ModifyModeController.this.rootView.getContext().getString(R.string.x8_modify_format_storage_fail), 1);
                    X8ModifyModeController.this.mFormatState = FormatState.IDLE;
                    CustomLoadManage unused = X8ModifyModeController.this.mLoadManage;
                    CustomLoadManage.dismiss();
                    X8ModifyModeController.this.recoverFormatState();
                    return;
                }
                X8ModifyModeController.this.mFormatState = FormatState.SEND_FORMAT_UPGRADE;
            }
        });
    }

    public void setFormatUpgradeStorage() {
        this.fcCtrlManager.setFormatStorage(X8Format.FormatType.FORMAT_SENIOR.ordinal(), X8Format.FormatDevid.FORMAT_DEVID_UPGRADE.ordinal(), new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess) {
                    X8ToastUtil.showToast(X8ModifyModeController.this.rootView.getContext(), X8ModifyModeController.this.rootView.getContext().getString(R.string.x8_modify_format_storage_fail), 1);
                    X8ModifyModeController.this.mFormatState = FormatState.IDLE;
                    CustomLoadManage unused = X8ModifyModeController.this.mLoadManage;
                    CustomLoadManage.dismiss();
                    X8ModifyModeController.this.recoverFormatState();
                    return;
                }
                X8ModifyModeController.this.mFormatState = FormatState.GET_FORMAT;
            }
        });
    }

    public void getFormatState() {
        this.fcCtrlManager.getFormatStorage(X8Format.FormatDevid.FORMAT_DEVID_ALL.ordinal(), new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess) {
                    X8ToastUtil.showToast(X8ModifyModeController.this.rootView.getContext(), X8ModifyModeController.this.rootView.getContext().getString(R.string.x8_modify_format_storage_fail), 1);
                    X8ModifyModeController.this.mFormatState = FormatState.IDLE;
                    CustomLoadManage unused = X8ModifyModeController.this.mLoadManage;
                    CustomLoadManage.dismiss();
                    X8ModifyModeController.this.recoverFormatState();
                }
                if (o != null) {
                    AckGetFormatStorageState state = (AckGetFormatStorageState) o;
                    if (state.getProcess() == 100) {
                        X8ToastUtil.showToast(X8ModifyModeController.this.rootView.getContext(), X8ModifyModeController.this.rootView.getContext().getString(R.string.x8_modify_format_storage_success), 1);
                        X8ModifyModeController.this.mFormatState = FormatState.IDLE;
                        CustomLoadManage unused2 = X8ModifyModeController.this.mLoadManage;
                        CustomLoadManage.dismiss();
                        X8ModifyModeController.this.recoverFormatState();
                    }
                }
            }
        });
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void showItem() {
        super.showItem();
        this.apkVersionManager.getAppSetting(new ApkVersionManager.AppSettingListener() {
            @Override
            public void onAppSettingListener() {
                ApkVersionManager apkVersionManager = X8ModifyModeController.this.apkVersionManager;
                X8ModifyModeController.this.apkVersionManager.getClass();
                if (apkVersionManager.isOpen("open_Formatted_memory")) {
                    X8ModifyModeController.this.mFormatStorage.setVisibility(View.VISIBLE);
                } else {
                    X8ModifyModeController.this.mFormatStorage.setVisibility(View.GONE);
                }
                ApkVersionManager apkVersionManager2 = X8ModifyModeController.this.apkVersionManager;
                X8ModifyModeController.this.apkVersionManager.getClass();
                if (apkVersionManager2.isOpen("open_sixpoint_calibrate")) {
                    X8ModifyModeController.this.x8AircraftCalibrationLayout.setVisibility(View.VISIBLE);
                } else {
                    X8ModifyModeController.this.x8AircraftCalibrationLayout.setVisibility(View.GONE);
                }
            }
        });
        this.handleView.setVisibility(View.VISIBLE);
        if (this.sensorView != null) {
            this.sensorView.setVisibility(View.GONE);
        }
        this.topLayout.setVisibility(View.VISIBLE);
        this.itemLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (b) {
            DroneState droneState = StateManager.getInstance().getX8Drone();
            if (droneState.isOnGround()) {
                this.mFormatStorage.setClickable(true);
                this.mIvFormatStorage.setBackgroundResource(R.drawable.x8_param_goto_icon_seletor);
                this.mTvFormatTitle.setAlpha(1.0f);
            } else {
                this.mFormatStorage.setClickable(false);
                this.mIvFormatStorage.setBackgroundResource(R.drawable.x8_camera_goto_icon_press);
                this.mTvFormatTitle.setAlpha(0.4f);
            }
        } else {
            this.mFormatStorage.setClickable(false);
            this.mIvFormatStorage.setBackgroundResource(R.drawable.x8_camera_goto_icon_press);
            this.mTvFormatTitle.setAlpha(0.4f);
        }
        super.onDroneConnected(b);
    }

    public void onConnectedState(ConectState state) {
    }

    @Override
    public void closeItem() {
        super.closeItem();
        if (this.sensorController != null) {
            this.sensorController.closeItem();
        }
    }

    public void onBlackBoxBack() {
        this.topLayout.setVisibility(View.VISIBLE);
        this.itemLayout.setVisibility(View.VISIBLE);
        this.modeControllerListener.onClose();
        this.mX8BlackBoxController = null;
        this.nextContentLayout.removeAllViews();
    }

    @Override
    public boolean isRunningTask() {
        return this.mX8BlackBoxController == null || !this.mX8BlackBoxController.isRunningTask();
    }


    public enum FormatState {
        IDLE,
        SEND_FORMAT_LOG,
        SEND_FORMAT_UPGRADE,
        GET_FORMAT
    }

    /* renamed from: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController$14 */

    static /* synthetic */ class AnonymousClass14 {
        static final /* synthetic */ int[] $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8ModifyModeController$FormatState = new int[FormatState.values().length];

        static {
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8ModifyModeController$FormatState[FormatState.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8ModifyModeController$FormatState[FormatState.SEND_FORMAT_LOG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8ModifyModeController$FormatState[FormatState.SEND_FORMAT_UPGRADE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$controls$fcsettting$X8ModifyModeController$FormatState[FormatState.GET_FORMAT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }
}
