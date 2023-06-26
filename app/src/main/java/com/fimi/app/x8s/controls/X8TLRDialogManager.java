package com.fimi.app.x8s.controls;

import com.fimi.android.app.R;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8TLRDialog;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8TLRDialogManager {
    private final X8RightIconForMapController x8RightIconForMapController;
    private X8TLRDialog mX8LandingDialog;
    private X8TLRDialog mX8ReturnDialog;
    private X8TLRDialog mX8TakeOffDialog;

    public X8TLRDialogManager(X8RightIconForMapController x8RightIconForMapController) {
        this.x8RightIconForMapController = x8RightIconForMapController;
    }

    public void onDroneConnected(boolean b) {
        if (!b) {
            if (this.mX8ReturnDialog != null && this.mX8ReturnDialog.isShowing()) {
                this.mX8ReturnDialog.dismiss();
            }
            if (this.mX8TakeOffDialog != null && this.mX8TakeOffDialog.isShowing()) {
                this.mX8TakeOffDialog.dismiss();
            }
            if (this.mX8LandingDialog != null && this.mX8LandingDialog.isShowing()) {
                this.mX8LandingDialog.dismiss();
            }
        }
    }

    public void showReturnDialog() {
        this.mX8ReturnDialog = new X8TLRDialog(this.x8RightIconForMapController.getActivity(), this.x8RightIconForMapController.getString(R.string.x8_ai_fly_return_home_title), "", true, 0, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                onRetureHomeClick();
            }
        });
        this.mX8ReturnDialog.setOnDismissListener(dialog -> mX8ReturnDialog = null);
        this.mX8ReturnDialog.setCanceledOnTouchOutside(false);
        this.mX8ReturnDialog.show();
    }

    public void showTakeOffDialog(boolean isVPUMode) {
        String number;
        if (isVPUMode) {
            number = X8NumberUtil.getDistanceNumberString(1.2f, 1, false);
        } else {
            number = X8NumberUtil.getDistanceNumberString(4.0f, 1, false);
        }
        this.mX8TakeOffDialog = new X8TLRDialog(this.x8RightIconForMapController.getActivity(), this.x8RightIconForMapController.getString(R.string.x8_ai_fly_take_off_title), String.format(this.x8RightIconForMapController.getString(R.string.x8_ai_fly_take_off_tip), number), false, R.drawable.x8_img_take_off_flag, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                onTakeOffClick();
            }
        });
        this.mX8TakeOffDialog.setOnDismissListener(dialog -> mX8TakeOffDialog = null);
        this.mX8TakeOffDialog.setCanceledOnTouchOutside(false);
        this.mX8TakeOffDialog.show();
    }

    public void showLandingDialog() {
        this.mX8LandingDialog = new X8TLRDialog(this.x8RightIconForMapController.getActivity(), this.x8RightIconForMapController.getString(R.string.x8_ai_fly_land_title), this.x8RightIconForMapController.getString(R.string.x8_ai_fly_land_tip), false, R.drawable.x8_img_landing_flag, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                onLandingClick();
            }
        });
        this.mX8LandingDialog.setOnDismissListener(dialog -> mX8LandingDialog = null);
        this.mX8LandingDialog.setCanceledOnTouchOutside(false);
        this.mX8LandingDialog.show();
    }

    public void showSportState(AutoFcSportState state) {
        if (this.mX8ReturnDialog != null) {
            this.mX8ReturnDialog.showSportState(state);
            if (StateManager.getInstance().getX8Drone().getCtrlMode() != 1 || StateManager.getInstance().getX8Drone().isOnGround()) {
                this.mX8ReturnDialog.dismiss();
            }
        }
        if (this.mX8TakeOffDialog != null && (StateManager.getInstance().getX8Drone().getCtrlMode() != 1 || StateManager.getInstance().getX8Drone().isInSky())) {
            this.mX8TakeOffDialog.dismiss();
        }
        if (this.mX8LandingDialog != null) {
            if (StateManager.getInstance().getX8Drone().getCtrlMode() != 1 || StateManager.getInstance().getX8Drone().isOnGround()) {
                this.mX8LandingDialog.dismiss();
            }
        }
    }

    public void onRetureHomeClick() {
        this.x8RightIconForMapController.getActivity().getFcManager().setAiRetureHome((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
            }
        });
    }

    public void onTakeOffClick() {
        this.x8RightIconForMapController.getActivity().getFcManager().takeOff((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
            }
        });
    }

    public void onLandingClick() {
        this.x8RightIconForMapController.getActivity().getFcManager().land((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
            }
        });
    }
}
