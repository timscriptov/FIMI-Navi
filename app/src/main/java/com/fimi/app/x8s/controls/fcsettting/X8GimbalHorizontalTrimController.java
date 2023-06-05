package com.fimi.app.x8s.controls.fcsettting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8GimbalHorizontalTrimListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8HorizontalTrimView;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8GimbalHorizontalTrimController extends AbsX8Controllers {
    private final float MAX_ANGLE;
    private final float MIN_ANGLE;
    private final X8sMainActivity activity;
    private X8HorizontalTrimView horizontalTrimView;
    private IX8GimbalHorizontalTrimListener listener;

    public X8GimbalHorizontalTrimController(View rootView, X8sMainActivity activity) {
        super(rootView);
        this.MAX_ANGLE = 10.0f;
        this.MIN_ANGLE = -10.0f;
        this.activity = activity;
    }

    @Override
    public void initViews(View rootView) {
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        this.isShow = true;
        LayoutInflater inflater = LayoutInflater.from(this.rootView.getContext());
        this.handleView = inflater.inflate(R.layout.x8_view_horizontal_trim_layout, (ViewGroup) this.rootView, true);
        this.horizontalTrimView = this.handleView.findViewById(R.id.x8_horizontal_trim_view);
        if (this.listener != null) {
            this.horizontalTrimView.setListener(this.listener);
        }
        this.horizontalTrimView.setEnabled(false);
        initData();
        super.openUi();
    }

    public void setListener(IX8GimbalHorizontalTrimListener listener) {
        this.listener = listener;
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        this.isShow = false;
        super.closeUi();
    }

    private void initData() {
        if (this.activity.getmX8GimbalManager() != null && StateManager.getInstance().getX8Drone().isConnect()) {
            this.activity.getmX8GimbalManager().getHorizontalAdjust(new UiCallBackListener<Float>() {
                @Override
                public void onComplete(CmdResult cmdResult, Float value) {
                    if (cmdResult.isSuccess()) {
                        if (value.floatValue() > 10.0f) {
                            value = Float.valueOf(10.0f);
                        } else if (value.floatValue() < -10.0f) {
                            value = Float.valueOf(-10.0f);
                        }
                        X8GimbalHorizontalTrimController.this.horizontalTrimView.setCurrValue((int) (value.floatValue() * 10.0f));
                    }
                }
            });
        }
    }

    @Override
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        if (b && this.horizontalTrimView != null) {
            this.horizontalTrimView.setEnabled(b);
        }
    }

    public void onSettingReady() {
        DroneState droneState = StateManager.getInstance().getX8Drone();
        if (droneState.isConnect() && this.activity.getmX8GimbalManager() != null) {
            float value = Float.valueOf(NumberUtil.decimalPointStr(this.horizontalTrimView.getCurrValue(), 1)).floatValue();
            this.activity.getmX8GimbalManager().setHorizontalAdjust(value, new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                    }
                }
            });
        }
    }

    @Override
    public boolean onClickBackKey() {
        if (this.isShow) {
            closeUi();
            return false;
        }
        return false;
    }
}
