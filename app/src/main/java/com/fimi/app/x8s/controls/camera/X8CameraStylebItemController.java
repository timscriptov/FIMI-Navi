package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.x8sdk.controller.CameraManager;


public class X8CameraStylebItemController extends AbsX8Controllers implements SeekBar.OnSeekBarChangeListener {
    private SeekBar awbSeekBar;
    private CameraManager cameraManager;
    private Context context;
    private boolean isUser;

    public X8CameraStylebItemController(View rootView) {
        super(rootView);
        this.isUser = false;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.awbSeekBar = rootView.findViewById(R.id.awb_seekBar);
        this.awbSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void initActions() {
    }

    @Override
    public void defaultVal() {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.isUser = fromUser;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }
}
