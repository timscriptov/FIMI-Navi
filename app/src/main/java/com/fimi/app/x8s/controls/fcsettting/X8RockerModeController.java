package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.app.x8s.interfaces.IX8RcRockerListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8TabItem;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetRcMode;

/* loaded from: classes.dex */
public class X8RockerModeController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    IX8RcRockerListener onRcCtrlModelListener;
    private int curMode;
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private ImageView imgRockerLeft;
    private ImageView imgRockerRight;
    private IX8CalibrationListener listener;
    private Context mContext;
    private X8TabItem thSwitchRocker;
    private TextView tvLeftSideDown;
    private TextView tvLeftSideLeft;
    private TextView tvLeftSideRight;
    private TextView tvLeftSideUp;
    private TextView tvRightSideDown;
    private TextView tvRightSideLeft;
    private TextView tvRightSideRight;
    private TextView tvRightSideUp;

    public X8RockerModeController(View rootView, IX8RcRockerListener onRcCtrlModelListener) {
        super(rootView);
        this.curMode = 0;
        this.onRcCtrlModelListener = onRcCtrlModelListener;
    }

    public void onRcConnected(boolean isConnect) {
        if ((this.thSwitchRocker != null) & this.isShow) {
            this.thSwitchRocker.setEnabled(isConnect);
            this.thSwitchRocker.setAlpha(isConnect ? 1.0f : 0.4f);
        }
    }

    public void showApDialog(final int index) {
        X8DoubleCustomDialog modelDialog = new X8DoubleCustomDialog(this.rootView.getContext(), getString(R.string.x8_rc_setting_model_dialog_title), getString(R.string.x8_rc_setting_model_dialog_content), new X8DoubleCustomDialog.onDialogButtonClickListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RockerModeController.1
            @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
            public void onLeft() {
                X8RockerModeController.this.switchRocker(X8RockerModeController.this.curMode);
            }

            @Override // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
            public void onRight() {
                byte mode = 0;
                switch (index) {
                    case 0:
                        mode = 2;
                        break;
                    case 1:
                        mode = 1;
                        break;
                    case 2:
                        mode = 3;
                        break;
                }
                final byte temp = mode;
                X8RockerModeController.this.fcCtrlManager.setRcCtrlMode(new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RockerModeController.1.1
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8RockerModeController.this.switchRocker(index);
                            if (X8RockerModeController.this.onRcCtrlModelListener != null) {
                                X8RockerModeController.this.onRcCtrlModelListener.onRcCtrlModelListener(temp);
                            }
                            if (X8RockerModeController.this.fcCtrlManager != null) {
                                X8RockerModeController.this.fcCtrlManager.getRcCtrlMode(new UiCallBackListener<AckGetRcMode>() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RockerModeController.1.1.1
                                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                                    public void onComplete(CmdResult cmdResult2, AckGetRcMode obj) {
                                        if (obj == null) {
                                        }
                                    }
                                });
                                return;
                            }
                            return;
                        }
                        X8RockerModeController.this.switchRocker(X8RockerModeController.this.curMode);
                    }
                }, mode);
            }
        });
        modelDialog.show();
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        this.contentView = inflater.inflate(R.layout.x8_main_rc_item_rocker_mode, (ViewGroup) rootView, true);
        this.imgReturn = (ImageView) this.contentView.findViewById(R.id.img_return);
        this.thSwitchRocker = (X8TabItem) this.contentView.findViewById(R.id.th_switch_rockers);
        this.imgRockerLeft = (ImageView) this.contentView.findViewById(R.id.img_rocker_left);
        this.imgRockerRight = (ImageView) this.contentView.findViewById(R.id.img_rocker_right);
        this.tvLeftSideLeft = (TextView) this.contentView.findViewById(R.id.tv_left_side_left);
        this.tvLeftSideRight = (TextView) this.contentView.findViewById(R.id.tv_left_side_right);
        this.tvLeftSideUp = (TextView) this.contentView.findViewById(R.id.tv_left_side_up);
        this.tvLeftSideDown = (TextView) this.contentView.findViewById(R.id.tv_left_side_down);
        this.tvRightSideLeft = (TextView) this.contentView.findViewById(R.id.tv_right_side_left);
        this.tvRightSideRight = (TextView) this.contentView.findViewById(R.id.tv_right_side_right);
        this.tvRightSideUp = (TextView) this.contentView.findViewById(R.id.tv_right_side_up);
        this.tvRightSideDown = (TextView) this.contentView.findViewById(R.id.tv_right_side_down);
        this.mContext = this.contentView.getContext();
        initActions();
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
        if (this.contentView != null) {
            this.imgReturn.setOnClickListener(this);
            this.thSwitchRocker.setOnSelectListener(new X8TabItem.OnSelectListener() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RockerModeController.2
                @Override // com.fimi.app.x8s.widget.X8TabItem.OnSelectListener
                public void onSelect(int index, String text) {
                    if (index != X8RockerModeController.this.curMode) {
                        X8RockerModeController.this.showApDialog(index);
                    }
                }
            });
        }
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void onDroneConnected(boolean b) {
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onCalibrationReturn();
            }
        }
    }

    private void requestDefaultValue() {
        if (this.fcCtrlManager != null) {
            this.fcCtrlManager.getRcCtrlMode(new UiCallBackListener<AckGetRcMode>() { // from class: com.fimi.app.x8s.controls.fcsettting.X8RockerModeController.3
                @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                public void onComplete(CmdResult cmdResult, AckGetRcMode obj) {
                    if (cmdResult.isSuccess()) {
                        int result = obj.getMode();
                        switch (result) {
                            case 1:
                                X8RockerModeController.this.switchRocker(1);
                                return;
                            case 2:
                                X8RockerModeController.this.switchRocker(0);
                                return;
                            case 3:
                                X8RockerModeController.this.switchRocker(2);
                                return;
                            default:
                                return;
                        }
                    }
                }
            });
        }
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(0);
        requestDefaultValue();
        getDroneState();
        onRcConnected(this.isRcConnect);
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void closeItem() {
        this.isShow = false;
        this.contentView.setVisibility(8);
        defaultVal();
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setCalibrationListener(IX8CalibrationListener listener) {
        this.listener = listener;
    }

    public void switchRocker(int rocker) {
        this.curMode = rocker;
        this.thSwitchRocker.setSelect(rocker);
        this.imgRockerLeft.setImageLevel(rocker);
        this.imgRockerRight.setImageLevel(rocker);
        switch (rocker) {
            case 0:
                this.tvLeftSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left_turn);
                this.tvLeftSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right_turn);
                this.tvLeftSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_forward);
                this.tvLeftSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_back_off);
                this.tvRightSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left);
                this.tvRightSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right);
                this.tvRightSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_up);
                this.tvRightSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_down);
                return;
            case 1:
                this.tvLeftSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left_turn);
                this.tvLeftSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right_turn);
                this.tvLeftSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_up);
                this.tvLeftSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_down);
                this.tvRightSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left);
                this.tvRightSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right);
                this.tvRightSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_forward);
                this.tvRightSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_back_off);
                return;
            case 2:
                this.tvLeftSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left);
                this.tvLeftSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right);
                this.tvLeftSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_forward);
                this.tvLeftSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_back_off);
                this.tvRightSideLeft.setText(R.string.x8_rc_setting_rc_rocker_to_left_turn);
                this.tvRightSideRight.setText(R.string.x8_rc_setting_rc_rocker_to_right_turn);
                this.tvRightSideUp.setText(R.string.x8_rc_setting_rc_rocker_to_up);
                this.tvRightSideDown.setText(R.string.x8_rc_setting_rc_rocker_to_down);
                return;
            default:
                return;
        }
    }
}
