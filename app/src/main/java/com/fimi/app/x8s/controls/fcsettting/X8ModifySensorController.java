package com.fimi.app.x8s.controls.fcsettting;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener;
import com.fimi.app.x8s.widget.X8IMUCustomCheckingDialog;
import com.fimi.app.x8s.widget.X8IMUCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetIMUInfo;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8ModifySensorController extends AbsX8MenuBoxControllers {
    Handler mHandler;
    X8IMUCheckController.OnCheckIMULisenter mOncheckImuLisenter;
    private ImageView back_btn;
    private Button btnImuCheck;
    private FcCtrlManager fcManager;
    private boolean isCheck;
    private boolean isConnectDrone;
    private IX8GeneraModifyModeControllerListener modeControllerListener;
    private TextView tvAccelMode;
    private TextView tvAccelMode2;
    private TextView tvAccelX;
    private TextView tvAccelX2;
    private TextView tvAccelY;
    private TextView tvAccelY2;
    private TextView tvAccelZ;
    private TextView tvAccelZ2;
    private TextView tvGyroX;
    private TextView tvGyroX2;
    private TextView tvGyroY;
    private TextView tvGyroY2;
    private TextView tvGyroZ;
    private TextView tvGyroZ2;
    private TextView tvGyroxMode;
    private TextView tvGyroxMode2;
    private TextView tvMagMode;
    private TextView tvMagX;
    private TextView tvMagY;
    private TextView tvMagZ;
    private X8IMUCheckController x8IMUCheckController;
    private X8IMUCustomCheckingDialog x8IMUCustomCheckingDialog;
    private X8IMUCustomDialog x8IMUCustomDialog;

    public X8ModifySensorController(View rootView) {
        super(rootView);
        this.mOncheckImuLisenter = new X8IMUCheckController.OnCheckIMULisenter() {
            @Override
            // com.fimi.app.x8s.controls.fcsettting.X8IMUCheckController.OnCheckIMULisenter
            public void startCheck() {
                X8ModifySensorController.this.x8IMUCustomCheckingDialog = new X8IMUCustomCheckingDialog(X8ModifySensorController.this.handleView.getContext());
                X8ModifySensorController.this.x8IMUCustomCheckingDialog.show();
            }

            @Override
            // com.fimi.app.x8s.controls.fcsettting.X8IMUCheckController.OnCheckIMULisenter
            public void checkProgress() {
            }

            @Override
            // com.fimi.app.x8s.controls.fcsettting.X8IMUCheckController.OnCheckIMULisenter
            public void checkFinish(int result, String erreoCode) {
                X8ModifySensorController.this.setImuState(result, erreoCode);
            }
        };
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                X8ModifySensorController.this.updateView();
                X8ModifySensorController.this.isConnectDrone = StateManager.getInstance().getConectState().isConnectDrone();
                X8ModifySensorController.this.isCheck = X8ModifySensorController.this.isConnectDrone && StateManager.getInstance().getX8Drone().isOnGround();
                X8ModifySensorController.this.btnImuCheck.setEnabled(X8ModifySensorController.this.isCheck);
                X8ModifySensorController.this.btnImuCheck.setAlpha(X8ModifySensorController.this.isCheck ? 1.0f : 0.4f);
                X8ModifySensorController.this.mHandler.sendEmptyMessageDelayed(0, 500L);
            }
        };
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.fcManager = fcManager;
    }

    public void setModeControllerListener(IX8GeneraModifyModeControllerListener modeControllerListener) {
        this.modeControllerListener = modeControllerListener;
    }

    @Override
    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.x8_rl_main_mdify_sensor_layout);
        this.back_btn = this.handleView.findViewById(R.id.btn_return);
        this.tvGyroX = this.handleView.findViewById(R.id.gyro_x);
        this.tvGyroY = this.handleView.findViewById(R.id.gyro_y);
        this.tvGyroZ = this.handleView.findViewById(R.id.gyro_z);
        this.tvAccelX = this.handleView.findViewById(R.id.accel_x);
        this.tvAccelY = this.handleView.findViewById(R.id.accel_y);
        this.tvAccelZ = this.handleView.findViewById(R.id.accel_z);
        this.tvMagX = this.handleView.findViewById(R.id.magx_x);
        this.tvMagY = this.handleView.findViewById(R.id.magx_y);
        this.tvMagZ = this.handleView.findViewById(R.id.magx_z);
        this.tvGyroxMode = this.handleView.findViewById(R.id.gyro_mode);
        this.tvAccelMode = this.handleView.findViewById(R.id.accel_mode);
        this.tvMagMode = this.handleView.findViewById(R.id.magx_mode);
        this.tvGyroX2 = this.handleView.findViewById(R.id.gyro2_x);
        this.tvGyroY2 = this.handleView.findViewById(R.id.gyro2_y);
        this.tvGyroZ2 = this.handleView.findViewById(R.id.gyro2_z);
        this.tvAccelX2 = this.handleView.findViewById(R.id.accel2_x);
        this.tvAccelY2 = this.handleView.findViewById(R.id.accel2_y);
        this.tvAccelZ2 = this.handleView.findViewById(R.id.accel2_z);
        this.tvGyroxMode2 = this.handleView.findViewById(R.id.gyro2_mode);
        this.tvAccelMode2 = this.handleView.findViewById(R.id.accel2_mode);
        this.btnImuCheck = this.handleView.findViewById(R.id.btn_imu_check);
    }

    @Override
    public void initActions() {
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                X8ModifySensorController.this.handleView.setVisibility(View.GONE);
                X8ModifySensorController.this.modeControllerListener.returnBack();
                X8ModifySensorController.this.closeItem();
            }
        });
        this.btnImuCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (X8ModifySensorController.this.fcManager != null) {
                    X8ModifySensorController.this.x8IMUCheckController = new X8IMUCheckController(X8ModifySensorController.this.rootView.getContext(), X8ModifySensorController.this.fcManager, X8ModifySensorController.this.mOncheckImuLisenter);
                    X8ModifySensorController.this.x8IMUCheckController.showImuDialog();
                }
            }
        });
    }

    public void setImuState(int type, String erreoCode) {
        if (this.x8IMUCustomCheckingDialog != null && this.x8IMUCustomCheckingDialog.isShowing()) {
            this.x8IMUCustomCheckingDialog.dismiss();
        }
        switch (type) {
            case 0:
                this.x8IMUCustomDialog = new X8IMUCustomDialog(this.handleView.getContext(), getString(R.string.x8_fc_item_dialog_title_two), null, getString(R.string.x8_fc_item_imu_err), true, new X8IMUCustomDialog.onDialogButtonClickListener() {
                    @Override
                    // com.fimi.app.x8s.widget.X8IMUCustomDialog.onDialogButtonClickListener
                    public void onSingleButtonClick() {
                        if (X8ModifySensorController.this.x8IMUCustomDialog != null && X8ModifySensorController.this.x8IMUCustomDialog.isShowing()) {
                            X8ModifySensorController.this.x8IMUCustomDialog.dismiss();
                        }
                    }
                });
                this.x8IMUCustomDialog.show();
                return;
            case 1:
                this.x8IMUCustomDialog = new X8IMUCustomDialog(this.handleView.getContext(), getString(R.string.x8_fc_item_dialog_title), getString(R.string.x8_fc_item_imu_normal), null, false, new X8IMUCustomDialog.onDialogButtonClickListener() {
                    @Override
                    // com.fimi.app.x8s.widget.X8IMUCustomDialog.onDialogButtonClickListener
                    public void onSingleButtonClick() {
                        if (X8ModifySensorController.this.x8IMUCustomDialog != null && X8ModifySensorController.this.x8IMUCustomDialog.isShowing()) {
                            X8ModifySensorController.this.x8IMUCustomDialog.dismiss();
                        }
                    }
                });
                this.x8IMUCustomDialog.show();
                return;
            case 2:
                this.x8IMUCustomDialog = new X8IMUCustomDialog(this.handleView.getContext(), getString(R.string.x8_fc_item_dialog_title), getString(R.string.x8_fc_item_imu_abnormal), erreoCode, false, new X8IMUCustomDialog.onDialogButtonClickListener() {
                    @Override
                    // com.fimi.app.x8s.widget.X8IMUCustomDialog.onDialogButtonClickListener
                    public void onSingleButtonClick() {
                        if (X8ModifySensorController.this.x8IMUCustomDialog != null && X8ModifySensorController.this.x8IMUCustomDialog.isShowing()) {
                            X8ModifySensorController.this.x8IMUCustomDialog.dismiss();
                        }
                    }
                });
                this.x8IMUCustomDialog.show();
                return;
            default:
        }
    }

    @Override
    public void defaultVal() {
        this.tvGyroX.setText("N/A");
        this.tvGyroY.setText("N/A");
        this.tvGyroZ.setText("N/A");
        this.tvAccelX.setText("N/A");
        this.tvAccelY.setText("N/A");
        this.tvAccelZ.setText("N/A");
        this.tvMagX.setText("N/A");
        this.tvMagY.setText("N/A");
        this.tvMagZ.setText("N/A");
        this.tvGyroxMode.setText("N/A");
        this.tvAccelMode.setText("N/A");
        this.tvMagMode.setText("N/A");
        this.tvGyroX2.setText("N/A");
        this.tvGyroY2.setText("N/A");
        this.tvGyroZ2.setText("N/A");
        this.tvAccelX2.setText("N/A");
        this.tvAccelY2.setText("N/A");
        this.tvAccelZ2.setText("N/A");
        this.tvGyroxMode2.setText("N/A");
        this.tvAccelMode2.setText("N/A");
    }

    public void updateView() {
        if (!StateManager.getInstance().getConectState().isConnectDrone()) {
            defaultVal();
        } else if (this.fcManager != null) {
            this.fcManager.getIUMInfo(1, new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    AckGetIMUInfo imuInfo = (AckGetIMUInfo) o;
                    if (imuInfo != null) {
                        X8ModifySensorController.this.tvGyroX.setText(String.valueOf(imuInfo.getGyroX() / 100.0f));
                        X8ModifySensorController.this.tvGyroY.setText(String.valueOf(imuInfo.getGyroY() / 100.0f));
                        X8ModifySensorController.this.tvGyroZ.setText(String.valueOf(imuInfo.getGyroZ() / 100.0f));
                        X8ModifySensorController.this.tvAccelX.setText(String.valueOf(imuInfo.getAccelX() / 100.0f));
                        X8ModifySensorController.this.tvAccelY.setText(String.valueOf(imuInfo.getAccelY() / 100.0f));
                        X8ModifySensorController.this.tvAccelZ.setText(String.valueOf(imuInfo.getAccelZ() / 100.0f));
                        X8ModifySensorController.this.tvMagX.setText(String.valueOf(imuInfo.getMagX() / 100.0f));
                        X8ModifySensorController.this.tvMagY.setText(String.valueOf(imuInfo.getMagY() / 100.0f));
                        X8ModifySensorController.this.tvMagZ.setText(String.valueOf(imuInfo.getMagZ() / 100.0f));
                        int gyroMode = (int) Math.sqrt(Math.pow(imuInfo.getGyroY(), 2.0d) + Math.pow(imuInfo.getGyroX(), 2.0d) + Math.pow(imuInfo.getGyroZ(), 2.0d));
                        int accelMode = (int) Math.sqrt(Math.pow(imuInfo.getAccelX(), 2.0d) + Math.pow(imuInfo.getAccelY(), 2.0d) + Math.pow(imuInfo.getAccelZ(), 2.0d));
                        int magMode = (int) Math.sqrt(Math.pow(imuInfo.getMagX(), 2.0d) + Math.pow(imuInfo.getMagY(), 2.0d) + Math.pow(imuInfo.getMagZ(), 2.0d));
                        X8ModifySensorController.this.tvGyroxMode.setText(String.valueOf(gyroMode / 100.0f));
                        X8ModifySensorController.this.tvAccelMode.setText(String.valueOf(accelMode / 100.0f));
                        X8ModifySensorController.this.tvMagMode.setText(String.valueOf(magMode / 100.0f));
                    }
                }
            });
            this.fcManager.getIUMInfo(2, new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    AckGetIMUInfo imuInfo = (AckGetIMUInfo) o;
                    if (imuInfo != null) {
                        X8ModifySensorController.this.tvGyroX2.setText(String.valueOf(imuInfo.getGyroX() / 100.0f));
                        X8ModifySensorController.this.tvGyroY2.setText(String.valueOf(imuInfo.getGyroY() / 100.0f));
                        X8ModifySensorController.this.tvGyroZ2.setText(String.valueOf(imuInfo.getGyroZ() / 100.0f));
                        X8ModifySensorController.this.tvAccelX2.setText(String.valueOf(imuInfo.getAccelX() / 100.0f));
                        X8ModifySensorController.this.tvAccelY2.setText(String.valueOf(imuInfo.getAccelY() / 100.0f));
                        X8ModifySensorController.this.tvAccelZ2.setText(String.valueOf(imuInfo.getAccelZ() / 100.0f));
                        int gyroMode = (int) Math.sqrt(Math.pow(imuInfo.getGyroY(), 2.0d) + Math.pow(imuInfo.getGyroX(), 2.0d) + Math.pow(imuInfo.getGyroZ(), 2.0d));
                        int accelMode = (int) Math.sqrt(Math.pow(imuInfo.getAccelX(), 2.0d) + Math.pow(imuInfo.getAccelY(), 2.0d) + Math.pow(imuInfo.getAccelZ(), 2.0d));
                        X8ModifySensorController.this.tvGyroxMode2.setText(String.valueOf(gyroMode / 100.0f));
                        X8ModifySensorController.this.tvAccelMode2.setText(String.valueOf(accelMode / 100.0f));
                    }
                }
            });
        }
    }

    @Override
    public void showItem() {
        super.showItem();
        this.handleView.setVisibility(View.VISIBLE);
        this.mHandler.sendEmptyMessageDelayed(0, 10L);
    }

    @Override
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
    }

    @Override
    public void closeItem() {
        super.closeItem();
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
        }
        if (this.x8IMUCheckController != null) {
            this.x8IMUCheckController.stopCheckIMUChck();
        }
    }
}
