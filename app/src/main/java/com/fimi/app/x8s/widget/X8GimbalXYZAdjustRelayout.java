package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.android.app.R;

public class X8GimbalXYZAdjustRelayout extends RelativeLayout {
    Context mContext;
    Button x8BtnGimbalXyzAdd;
    Button x8BtnGimbalXyzSubtract;
    TextView x8TvGimbalXyzName;
    TextView x8TvGimbalXyzValue;

    public X8GimbalXYZAdjustRelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.x8_gimable_xyz_adjust_item, this);
        this.x8BtnGimbalXyzAdd = findViewById(R.id.x8_btn_gimbal_xyz_add);
        this.x8BtnGimbalXyzSubtract = findViewById(R.id.x8_btn_gimbal_xyz_subtract);
        this.x8TvGimbalXyzName = findViewById(R.id.x8_tv_gimbal_xyz_name);
        this.x8TvGimbalXyzValue = findViewById(R.id.x8_tv_gimbal_xyz_value);
    }

    public TextView getTvGimbalXyzName() {
        return this.x8TvGimbalXyzName;
    }

    public TextView getTvGimbalXyzValue() {
        return this.x8TvGimbalXyzValue;
    }

    public Button getBtnGimbalXyzSubtract() {
        return this.x8BtnGimbalXyzSubtract;
    }

    public Button getBtnGimbalXyzAdd() {
        return this.x8BtnGimbalXyzAdd;
    }
}
