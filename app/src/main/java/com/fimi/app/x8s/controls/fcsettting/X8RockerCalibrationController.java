package com.fimi.app.x8s.controls.fcsettting;

import android.view.View;
import android.widget.ImageView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8MenuBoxControllers;
import com.fimi.app.x8s.interfaces.IX8CalibrationListener;
import com.fimi.x8sdk.controller.FcCtrlManager;


public class X8RockerCalibrationController extends AbsX8MenuBoxControllers implements View.OnClickListener {
    private FcCtrlManager fcCtrlManager;
    private ImageView imgReturn;
    private IX8CalibrationListener listener;

    public X8RockerCalibrationController(View rootView) {
        super(rootView);
    }

    @Override
    public void initViews(View rootView) {
        this.contentView = rootView.findViewById(R.id.x8_rl_main_rc_item_rocker_mode_layout);
        this.imgReturn = this.contentView.findViewById(R.id.img_return);
    }

    @Override
    public void initActions() {
        this.imgReturn.setOnClickListener(this);
    }

    @Override
    public void onDroneConnected(boolean b) {
        if (this.isShow) {
        }
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_return) {
            closeItem();
            if (this.listener != null) {
                this.listener.onCalibrationReturn();
            }
        }
    }

    @Override
    public void showItem() {
        this.isShow = true;
        this.contentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeItem() {
        this.isShow = false;
        this.contentView.setVisibility(View.GONE);
        defaultVal();
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        this.fcCtrlManager = fcCtrlManager;
    }

    public void setCalibrationListener(IX8CalibrationListener listener) {
        this.listener = listener;
    }
}
