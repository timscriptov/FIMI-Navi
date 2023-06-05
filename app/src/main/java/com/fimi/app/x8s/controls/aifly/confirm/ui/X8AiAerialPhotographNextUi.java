package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.view.View;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.widget.X8CustomSeekBar;
import com.fimi.app.x8s.widget.X8PlusMinusSeekBar;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8AiAerialPhotographNextUi implements X8CustomSeekBar.onSeekValueSetListener {
    private final View rootView;
    private final X8PlusMinusSeekBar sbBraking;
    private final X8PlusMinusSeekBar sbYaw;
    private final TextView tvBrakingValue;
    private final TextView tvTitle;
    private final TextView tvYawValue;
    private int type;

    public X8AiAerialPhotographNextUi(View rootView) {
        this.rootView = rootView;
        this.tvTitle = rootView.findViewById(R.id.tv_ai_aerail_graph_title);
        this.tvBrakingValue = rootView.findViewById(R.id.tv_braking_sensity_value);
        this.tvYawValue = rootView.findViewById(R.id.tv_yaw_sensity_value);
        this.sbBraking = rootView.findViewById(R.id.sb_braking);
        this.sbYaw = rootView.findViewById(R.id.sb_yaw);
        this.sbBraking.setListener(this);
        this.sbYaw.setListener(this);
    }

    public void setTitle(String s) {
        this.tvTitle.setText(s);
    }

    @Override
    public void onSeekValueSet(int viewId, int value) {
        if (viewId == R.id.sb_braking) {
            this.tvBrakingValue.setText("" + value);
        } else if (viewId == R.id.sb_yaw) {
            this.tvYawValue.setText("" + value);
        }
    }

    public void setSensity() {
        this.sbBraking.setProgress(StateManager.getInstance().getX8Drone().getFcBrakeSenssity());
        this.sbYaw.setProgress(StateManager.getInstance().getX8Drone().getFcYAWSenssity());
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSaveData() {
        return this.type == 1;
    }

    public int getBrakingSensity() {
        return this.sbBraking.getProgress();
    }

    public int getYawSensity() {
        return this.sbYaw.getProgress();
    }
}
