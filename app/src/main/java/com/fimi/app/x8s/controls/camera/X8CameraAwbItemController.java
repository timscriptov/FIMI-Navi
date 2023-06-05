package com.fimi.app.x8s.controls.camera;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.x8sdk.controller.CameraManager;

public class X8CameraAwbItemController extends AbsX8Controllers implements SeekBar.OnSeekBarChangeListener {
    private final CameraManager cameraManager;
    private SeekBar awbSeekBar;
    private Context context;
    private boolean isUser;

    public X8CameraAwbItemController(View rootView, CameraManager manager) {
        super(rootView);
        this.isUser = false;
        this.cameraManager = manager;
    }

    @Override
    public void initViews(@NonNull View rootView) {
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
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }
}
