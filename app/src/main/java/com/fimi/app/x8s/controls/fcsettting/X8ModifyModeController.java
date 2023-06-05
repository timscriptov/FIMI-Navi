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

/* loaded from: classes.dex */
public class X8ModifyModeController extends AbsX8MenuBoxControllers implements X8DoubleCustomDialog.onDialogButtonClickListener {
    private X8AircraftCalibrationController aircraftCalibrationController;
    private ApkVersionManager apkVersionManager;
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
    private Handler mHandler;
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
        this.mHandler = new Handler() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.1
            @Override // android.os.Handler
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

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_main_general_item_modify_layout, (ViewGroup) rootView, true);
        this.back_btn = (ImageView) this.handleView.findViewById(R.id.btn_return);
        this.sensorLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_sensor_layout);
        this.mGimbalSensorLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_gimbal_sensor_layout);
        this.calibration_btn = (Button) this.handleView.findViewById(R.id.x8_btn_calibration);
        this.checkLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_check_layout);
        this.blackBoxLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_blackbox_layout);
        this.topLayout = (PercentRelativeLayout) this.handleView.findViewById(R.id.layout_top);
        this.itemLayout = (PercentRelativeLayout) this.handleView.findViewById(R.id.x8_modify_item_layout);
        this.blackBoxLogLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_blackbox_layout1);
        this.x8AircraftCalibrationLayout = (RelativeLayout) this.handleView.findViewById(R.id.x8_aircraft_calibration_layout);
        this.mFormatStorage = (RelativeLayout) this.handleView.findViewById(R.id.x8_format_storage_layout);
        this.mIvFormatStorage = (ImageView) this.handleView.findViewById(R.id.iv_format_storage);
        this.mTvFormatDec = (TextView) this.handleView.findViewById(R.id.tv_format_storage_dec);
        this.mTvFormatTitle = (TextView) this.handleView.findViewById(R.id.tv_format_storage_title);
        this.mPbFormat = (ProgressBar) this.handleView.findViewById(R.id.pb_format_storage);
        this.nextContentLayout = (RelativeLayout) this.handleView.findViewById(R.id.rl_next_content);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
        this.calibration_btn.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
                X8ModifyModeController.this.modeControllerListener.onOpen();
                if (X8ModifyModeController.this.calibrationViewStub == null) {
                    X8ModifyModeController.this.calibrationViewStub = (ViewStub) X8ModifyModeController.this.handleView.findViewById(R.id.x8_calibration_view);
                    X8ModifyModeController.this.calibrationView = X8ModifyModeController.this.calibrationViewStub.inflate();
                    X8ModifyModeController.this.aircraftCalibrationController = new X8AircraftCalibrationController(X8ModifyModeController.this.calibrationView);
                }
                X8ModifyModeController.this.aircraftCalibrationController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.aircraftCalibrationController.showItem();
                X8ModifyModeController.this.aircraftCalibrationController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.2.1
                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(0);
                        X8ModifyModeController.this.itemLayout.setVisibility(0);
                    }

                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void onOpen() {
                    }

                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void onClose() {
                        X8ModifyModeController.this.modeControllerListener.onClose();
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
            }
        });
        this.back_btn.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                X8ModifyModeController.this.handleView.setVisibility(8);
                X8ModifyModeController.this.modeControllerListener.returnBack();
            }
        });
        this.mGimbalSensorLayout.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (X8ModifyModeController.this.gimbalSensorViewStub == null) {
                    X8ModifyModeController.this.gimbalSensorViewStub = (ViewStub) X8ModifyModeController.this.handleView.findViewById(R.id.x8_gimbal_sensor_view);
                    X8ModifyModeController.this.gimbalSensorView = X8ModifyModeController.this.gimbalSensorViewStub.inflate();
                }
                X8ModifyModeController.this.mGimbalSensorController = new X8modifyGimbalSensorController(X8ModifyModeController.this.gimbalSensorView);
                X8ModifyModeController.this.mGimbalSensorController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.mGimbalSensorController.setX8GimbalManager(X8ModifyModeController.this.mX8GimbalManager);
                X8ModifyModeController.this.mGimbalSensorController.showItem();
                X8ModifyModeController.this.mGimbalSensorController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.4.1
                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(0);
                        X8ModifyModeController.this.itemLayout.setVisibility(0);
                    }

                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void onOpen() {
                    }

                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void onClose() {
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
            }
        });
        this.sensorLayout.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (X8ModifyModeController.this.sensorViewStub == null) {
                    X8ModifyModeController.this.sensorViewStub = (ViewStub) X8ModifyModeController.this.handleView.findViewById(R.id.x8_sensor_view);
                    X8ModifyModeController.this.sensorView = X8ModifyModeController.this.sensorViewStub.inflate();
                    X8ModifyModeController.this.sensorController = new X8ModifySensorController(X8ModifyModeController.this.sensorView);
                }
                X8ModifyModeController.this.sensorController.setFcManager(X8ModifyModeController.this.fcCtrlManager);
                X8ModifyModeController.this.sensorController.showItem();
                X8ModifyModeController.this.sensorController.setModeControllerListener(new IX8GeneraModifyModeControllerListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.5.1
                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void returnBack() {
                        X8ModifyModeController.this.topLayout.setVisibility(0);
                        X8ModifyModeController.this.itemLayout.setVisibility(0);
                    }

                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void onOpen() {
                    }

                    @Override // com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener
                    public void onClose() {
                    }
                });
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
            }
        });
        this.checkLayout.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
            }
        });
        this.blackBoxLayout.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
            }
        });
        this.blackBoxLogLayout.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                X8ModifyModeController.this.topLayout.setVisibility(8);
                X8ModifyModeController.this.itemLayout.setVisibility(8);
                X8ModifyModeController.this.mX8BlackBoxController = new X8BlackBoxController(X8ModifyModeController.this.nextContentLayout, X8ModifyModeController.this);
                X8ModifyModeController.this.modeControllerListener.onOpen();
            }
        });
        this.mFormatStorage.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.9
            @Override // android.view.View.OnClickListener
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

    @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
    public void onLeft() {
    }

    @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
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
        this.mIvFormatStorage.setVisibility(8);
        this.mTvFormatDec.setVisibility(0);
        this.mPbFormat.setVisibility(0);
    }

    public void recoverFormatState() {
        this.mTvFormatTitle.setText(this.rootView.getContext().getString(R.string.x8_modify_format_storage));
        this.mIvFormatStorage.setVisibility(0);
        this.mTvFormatDec.setVisibility(8);
        this.mPbFormat.setVisibility(8);
    }

    public void setFormatLogStorage() {
        this.fcCtrlManager.setFormatStorage(X8Format.FormatType.FORMAT_SENIOR.ordinal(), X8Format.FormatDevid.FORMAT_DEVID_LOG.ordinal(), new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.10
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
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
        this.fcCtrlManager.setFormatStorage(X8Format.FormatType.FORMAT_SENIOR.ordinal(), X8Format.FormatDevid.FORMAT_DEVID_UPGRADE.ordinal(), new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.11
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
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
        this.fcCtrlManager.getFormatStorage(X8Format.FormatDevid.FORMAT_DEVID_ALL.ordinal(), new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.12
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
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

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void showItem() {
        super.showItem();
        this.apkVersionManager.getAppSetting(new ApkVersionManager.AppSettingListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController.13
            @Override // com.fimi.network.ApkVersionManager.AppSettingListener
            public void onAppSettingListener() {
                ApkVersionManager apkVersionManager = X8ModifyModeController.this.apkVersionManager;
                X8ModifyModeController.this.apkVersionManager.getClass();
                if (apkVersionManager.isOpen("open_Formatted_memory")) {
                    X8ModifyModeController.this.mFormatStorage.setVisibility(0);
                } else {
                    X8ModifyModeController.this.mFormatStorage.setVisibility(8);
                }
                ApkVersionManager apkVersionManager2 = X8ModifyModeController.this.apkVersionManager;
                X8ModifyModeController.this.apkVersionManager.getClass();
                if (apkVersionManager2.isOpen("open_sixpoint_calibrate")) {
                    X8ModifyModeController.this.x8AircraftCalibrationLayout.setVisibility(0);
                } else {
                    X8ModifyModeController.this.x8AircraftCalibrationLayout.setVisibility(8);
                }
            }
        });
        this.handleView.setVisibility(0);
        if (this.sensorView != null) {
            this.sensorView.setVisibility(8);
        }
        this.topLayout.setVisibility(0);
        this.itemLayout.setVisibility(0);
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
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

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void closeItem() {
        super.closeItem();
        if (this.sensorController != null) {
            this.sensorController.closeItem();
        }
    }

    public void onBlackBoxBack() {
        this.topLayout.setVisibility(0);
        this.itemLayout.setVisibility(0);
        this.modeControllerListener.onClose();
        this.mX8BlackBoxController = null;
        this.nextContentLayout.removeAllViews();
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers
    public boolean isRunningTask() {
        if (this.mX8BlackBoxController == null || !this.mX8BlackBoxController.isRunningTask()) {
            return true;
        }
        return false;
    }

    /* loaded from: classes.dex */
    public enum FormatState {
        IDLE,
        SEND_FORMAT_LOG,
        SEND_FORMAT_UPGRADE,
        GET_FORMAT
    }

    /* renamed from: com.fimi.app.x8s.controls.fcsettting.X8ModifyModeController$14 */
    /* loaded from: classes.dex */
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
