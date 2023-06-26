package com.fimi.app.x8s.controls.fcsettting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.fimi.android.app.R;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckCheckIMUException;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8IMUCheckController {
    AckCheckIMUException checkIMUException1;
    OnCheckIMULisenter checkIMULisenter;
    AckCheckIMUException checkIMUMxception2;
    FcCtrlManager fcCtrlManager;
    Context mContext;
    volatile int reqestCount = 0;
    private X8DoubleCustomDialog checkIMUDialog;

    public X8IMUCheckController(Context mContext, FcCtrlManager fcCtrlManager, OnCheckIMULisenter checkIMULisenter) {
        this.mContext = mContext;
        this.fcCtrlManager = fcCtrlManager;
        this.checkIMULisenter = checkIMULisenter;
    }

    public void startCheckIMUStatus() {
        this.reqestCount = 0;
        this.fcCtrlManager.openCheckIMU(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
            }
        });
        this.checkIMULisenter.startCheck();
        this.mHandler.sendEmptyMessageDelayed(0, 2000L);
    }

    public void showImuDialog() {
        if (this.checkIMUDialog == null) {
            this.checkIMUDialog = new X8DoubleCustomDialog(this.mContext, this.mContext.getString(R.string.x8_fc_item_imu_check), this.mContext.getString(R.string.x8_fc_item_imu_dialog), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8IMUCheckController.this.startCheckIMUStatus();
                }
            });
        }
        this.checkIMUDialog.show();
    }

    @SuppressLint({"StringFormatMatches"})
    public void getCheckIMUResult() {
        if (this.checkIMUException1 == null || this.checkIMUException1.getSensorMaintainSta() != 4) {
            this.fcCtrlManager.checkIMUException(1, new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    X8IMUCheckController.this.checkIMUException1 = (AckCheckIMUException) o;
                }
            });
        }
        if (this.checkIMUMxception2 == null || this.checkIMUMxception2.getSensorMaintainSta() != 4) {
            this.fcCtrlManager.checkIMUException(2, new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    X8IMUCheckController.this.checkIMUMxception2 = (AckCheckIMUException) o;
                }
            });
        }
        if (this.checkIMUException1 != null && this.checkIMUException1.getSensorMaintainSta() == 4 && this.checkIMUMxception2 != null && this.checkIMUMxception2.getSensorMaintainSta() == 4) {
            if (this.checkIMUException1.getErrCode() == 0 && this.checkIMUMxception2.getErrCode() == 0) {
                this.checkIMULisenter.checkFinish(1, "");
            } else {
                this.checkIMULisenter.checkFinish(2, String.format(this.mContext.getString(R.string.x8_fc_item_imu_error_code), ByteUtil.int2HexString(this.checkIMUException1.getErrCode()), ByteUtil.int2HexString(this.checkIMUMxception2.getErrCode())));
            }
            this.checkIMUMxception2 = null;
            this.checkIMUException1 = null;
            removeCheckIMUMessage();
            return;
        }
        this.mHandler.sendEmptyMessageDelayed(0, 1000L);
    }

    public void stopCheckIMUChck() {
        removeCheckIMUMessage();
    }    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                X8IMUCheckController.this.reqestCount++;
                if (!StateManager.getInstance().getX8Drone().isConnect()) {
                    X8IMUCheckController.this.checkIMULisenter.checkFinish(0, "");
                    X8IMUCheckController.this.removeCheckIMUMessage();
                    return;
                } else if (X8IMUCheckController.this.reqestCount >= 65) {
                    X8IMUCheckController.this.checkIMULisenter.checkFinish(0, "");
                    X8IMUCheckController.this.removeCheckIMUMessage();
                    return;
                } else {
                    X8IMUCheckController.this.checkIMULisenter.checkProgress();
                    X8IMUCheckController.this.getCheckIMUResult();
                    return;
                }
            }
        }
    };

    public void removeCheckIMUMessage() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
        }
    }

    public interface OnCheckIMULisenter {
        void checkFinish(int i, String str);

        void checkProgress();

        void startCheck();
    }




}
