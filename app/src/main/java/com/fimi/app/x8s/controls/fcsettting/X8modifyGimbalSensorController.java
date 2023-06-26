package com.fimi.app.x8s.controls.fcsettting;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8GeneraModifyModeControllerListener;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.X8GimbalManager;
import com.fimi.x8sdk.dataparser.AckGetGimbalSensorInfo;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8modifyGimbalSensorController extends AbsX8MenuBoxControllers {
    Handler mHandler;
    private ImageView back_btn;
    private FcCtrlManager fcManager;
    private TextView mAccelerationK;
    private TextView mAccelerationX;
    private TextView mAccelerationY;
    private TextView mAccelerationZ;
    private TextView mEleMachinery1K;
    private TextView mEleMachinery1X;
    private TextView mEleMachinery1Y;
    private TextView mEleMachinery1Z;
    private TextView mEleMachinery2K;
    private TextView mEleMachinery2X;
    private TextView mEleMachinery2Y;
    private TextView mEleMachinery2Z;
    private TextView mEleMachinery3K;
    private TextView mEleMachinery3X;
    private TextView mEleMachinery3Y;
    private TextView mEleMachinery3Z;
    private TextView mGyroK;
    private TextView mGyroVarianceK;
    private TextView mGyroVarianceX;
    private TextView mGyroVarianceY;
    private TextView mGyroVarianceZ;
    private TextView mGyroX;
    private TextView mGyroY;
    private TextView mGyroZ;
    private X8GimbalManager mX8GimbalManager;
    private IX8GeneraModifyModeControllerListener modeControllerListener;

    public X8modifyGimbalSensorController(View rootView) {
        super(rootView);
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                X8modifyGimbalSensorController.this.updateView();
                X8modifyGimbalSensorController.this.mHandler.sendEmptyMessageDelayed(0, 500L);
            }
        };
    }

    public void setX8GimbalManager(X8GimbalManager x8GimbalManager) {
        this.mX8GimbalManager = x8GimbalManager;
    }

    public void setFcManager(FcCtrlManager fcManager) {
        this.fcManager = fcManager;
    }

    public void setModeControllerListener(IX8GeneraModifyModeControllerListener modeControllerListener) {
        this.modeControllerListener = modeControllerListener;
    }

    @Override
    public void initViews(View rootView) {
        this.handleView = rootView.findViewById(R.id.x8_rl_main_mdify_gimbal_sensor_layout);
        this.back_btn = this.handleView.findViewById(R.id.btn_return);
        this.mGyroX = this.handleView.findViewById(R.id.gimbal_gyro_x);
        this.mGyroY = this.handleView.findViewById(R.id.gimbal_gyro_y);
        this.mGyroZ = this.handleView.findViewById(R.id.gimbal_gyro_z);
        this.mGyroK = this.handleView.findViewById(R.id.gimbal_gyro_k);
        this.mAccelerationX = this.handleView.findViewById(R.id.gimbal_acceleration_x);
        this.mAccelerationY = this.handleView.findViewById(R.id.gimbal_acceleration_y);
        this.mAccelerationZ = this.handleView.findViewById(R.id.gimbal_acceleration_z);
        this.mAccelerationK = this.handleView.findViewById(R.id.gimbal_acceleration_k);
        this.mGyroVarianceX = this.handleView.findViewById(R.id.gimbal_gyro_variance_x);
        this.mGyroVarianceY = this.handleView.findViewById(R.id.gimbal_gyro_variance_y);
        this.mGyroVarianceZ = this.handleView.findViewById(R.id.gimbal_gyro_variance_z);
        this.mGyroVarianceK = this.handleView.findViewById(R.id.gimbal_gyro_variance_k);
        this.mEleMachinery1X = this.handleView.findViewById(R.id.gimbal_electric_machinery1_x);
        this.mEleMachinery1Y = this.handleView.findViewById(R.id.gimbal_electric_machinery1_y);
        this.mEleMachinery1Z = this.handleView.findViewById(R.id.gimbal_electric_machinery1_z);
        this.mEleMachinery1K = this.handleView.findViewById(R.id.gimbal_electric_machinery1_k);
        this.mEleMachinery2X = this.handleView.findViewById(R.id.gimbal_electric_machinery2_x);
        this.mEleMachinery2Y = this.handleView.findViewById(R.id.gimbal_electric_machinery2_y);
        this.mEleMachinery2Z = this.handleView.findViewById(R.id.gimbal_electric_machinery2_z);
        this.mEleMachinery2K = this.handleView.findViewById(R.id.gimbal_electric_machinery2_k);
        this.mEleMachinery3X = this.handleView.findViewById(R.id.gimbal_electric_machinery3_x);
        this.mEleMachinery3Y = this.handleView.findViewById(R.id.gimbal_electric_machinery3_y);
        this.mEleMachinery3Z = this.handleView.findViewById(R.id.gimbal_electric_machinery3_z);
        this.mEleMachinery3K = this.handleView.findViewById(R.id.gimbal_electric_machinery3_k);
    }

    @Override
    public void initActions() {
        this.back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8modifyGimbalSensorController.this.handleView.setVisibility(View.GONE);
                X8modifyGimbalSensorController.this.modeControllerListener.returnBack();
                X8modifyGimbalSensorController.this.closeItem();
            }
        });
    }

    @Override
    public void defaultVal() {
        this.mGyroX.setText("N/A");
        this.mGyroY.setText("N/A");
        this.mGyroZ.setText("N/A");
        this.mGyroK.setText("N/A");
        this.mAccelerationX.setText("N/A");
        this.mAccelerationY.setText("N/A");
        this.mAccelerationZ.setText("N/A");
        this.mAccelerationK.setText("N/A");
        this.mGyroVarianceX.setText("N/A");
        this.mGyroVarianceY.setText("N/A");
        this.mGyroVarianceZ.setText("N/A");
        this.mGyroVarianceK.setText("N/A");
        this.mEleMachinery1X.setText("N/A");
        this.mEleMachinery1Y.setText("N/A");
        this.mEleMachinery1Z.setText("N/A");
        this.mEleMachinery1K.setText("N/A");
        this.mEleMachinery2X.setText("N/A");
        this.mEleMachinery2Y.setText("N/A");
        this.mEleMachinery2Z.setText("N/A");
        this.mEleMachinery2K.setText("N/A");
        this.mEleMachinery3X.setText("N/A");
        this.mEleMachinery3Y.setText("N/A");
        this.mEleMachinery3Z.setText("N/A");
        this.mEleMachinery3K.setText("N/A");
    }

    public void updateView() {
        if (!StateManager.getInstance().getConectState().isConnectDrone()) {
            defaultVal();
        } else if (this.mX8GimbalManager != null) {
            this.mX8GimbalManager.getGimbalSensorInfo(new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (!cmdResult.isSuccess) {
                        X8modifyGimbalSensorController.this.defaultVal();
                    } else if (o != null) {
                        AckGetGimbalSensorInfo gimbalSensorInfo = (AckGetGimbalSensorInfo) o;
                        X8modifyGimbalSensorController.this.mGyroX.setText(String.valueOf(gimbalSensorInfo.getGyroX()));
                        X8modifyGimbalSensorController.this.mGyroY.setText(String.valueOf(gimbalSensorInfo.getGyroY()));
                        X8modifyGimbalSensorController.this.mGyroZ.setText(String.valueOf(gimbalSensorInfo.getGyroZ()));
                        int gyroMode = (int) Math.sqrt(Math.pow(gimbalSensorInfo.getGyroY(), 2.0d) + Math.pow(gimbalSensorInfo.getGyroX(), 2.0d) + Math.pow(gimbalSensorInfo.getGyroZ(), 2.0d));
                        X8modifyGimbalSensorController.this.mGyroK.setText(String.valueOf(gyroMode));
                        X8modifyGimbalSensorController.this.mAccelerationX.setText(String.valueOf(gimbalSensorInfo.getAccelerationX()));
                        X8modifyGimbalSensorController.this.mAccelerationY.setText(String.valueOf(gimbalSensorInfo.getAccelerationY()));
                        X8modifyGimbalSensorController.this.mAccelerationZ.setText(String.valueOf(gimbalSensorInfo.getAccelerationZ()));
                        X8modifyGimbalSensorController.this.mAccelerationK.setText(String.valueOf(gimbalSensorInfo.getAccelMagnitude()));
                        X8modifyGimbalSensorController.this.mGyroVarianceX.setText(String.valueOf(gimbalSensorInfo.getGyroVarianceX()));
                        X8modifyGimbalSensorController.this.mGyroVarianceY.setText(String.valueOf(gimbalSensorInfo.getGyroVarianceY()));
                        X8modifyGimbalSensorController.this.mGyroVarianceZ.setText(String.valueOf(gimbalSensorInfo.getGyroVarianceZ()));
                        X8modifyGimbalSensorController.this.mGyroVarianceK.setText(String.valueOf(gimbalSensorInfo.getTemp()));
                        X8modifyGimbalSensorController.this.mEleMachinery1X.setText(String.valueOf(gimbalSensorInfo.getMotor1IU()));
                        X8modifyGimbalSensorController.this.mEleMachinery1Y.setText(String.valueOf(gimbalSensorInfo.getMotor1IV()));
                        X8modifyGimbalSensorController.this.mEleMachinery1Z.setText(String.valueOf(gimbalSensorInfo.getMotor1HallX()));
                        X8modifyGimbalSensorController.this.mEleMachinery1K.setText(String.valueOf(gimbalSensorInfo.getMotor1HallY()));
                        X8modifyGimbalSensorController.this.mEleMachinery2X.setText(String.valueOf(gimbalSensorInfo.getMotor2IU()));
                        X8modifyGimbalSensorController.this.mEleMachinery2Y.setText(String.valueOf(gimbalSensorInfo.getMotor2IV()));
                        X8modifyGimbalSensorController.this.mEleMachinery2Z.setText(String.valueOf(gimbalSensorInfo.getMotor2HallX()));
                        X8modifyGimbalSensorController.this.mEleMachinery2K.setText(String.valueOf(gimbalSensorInfo.getMotor2HallY()));
                        X8modifyGimbalSensorController.this.mEleMachinery3X.setText(String.valueOf(gimbalSensorInfo.getMotor3IU()));
                        X8modifyGimbalSensorController.this.mEleMachinery3Y.setText(String.valueOf(gimbalSensorInfo.getMotor3IV()));
                        X8modifyGimbalSensorController.this.mEleMachinery3Z.setText(String.valueOf(gimbalSensorInfo.getMotor3HallX()));
                        X8modifyGimbalSensorController.this.mEleMachinery3K.setText(String.valueOf(gimbalSensorInfo.getMotor3HallY()));
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
    }
}
