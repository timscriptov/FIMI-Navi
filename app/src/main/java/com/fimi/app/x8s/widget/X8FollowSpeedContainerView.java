package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.tools.X8NumberUtil;

public class X8FollowSpeedContainerView extends RelativeLayout implements View.OnClickListener, X8FollowSpeedView.OnChangeListener {
    private final X8FollowSpeedView fsv;
    private final ImageView imgAntiClockwise;
    private final ImageView imgClockwise;
    private final TextView tvSpeed;
    private int MAX;
    private int MIN;
    private int accuracy;
    private OnSendSpeedListener listener;
    private String prex;
    private int speed;

    public X8FollowSpeedContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.MIN = 0;
        this.MAX = 40;
        this.accuracy = 10;
        LayoutInflater.from(context).inflate(R.layout.x8_ai_follow_speed_containt_layout, this, true);
        this.fsv = findViewById(R.id.v_speed);
        this.tvSpeed = findViewById(R.id.tv_speed);
        this.imgAntiClockwise = findViewById(R.id.img_anti_clockwise);
        this.imgClockwise = findViewById(R.id.img_clockwise);
        this.fsv.setOnSpeedChangeListener(this);
        this.imgAntiClockwise.setOnClickListener(this);
        this.imgClockwise.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        int s;
        int s2;
        int id = v.getId();
        if (id == R.id.img_anti_clockwise) {
            int s3 = this.speed;
            if (s3 >= 0) {
                s2 = s3 - this.MIN;
            } else {
                s2 = s3 + this.MIN;
            }
            this.fsv.setLeftClick(s2, this.MAX, this.MIN);
        } else if (id == R.id.img_clockwise) {
            int s4 = this.speed;
            if (s4 >= 0) {
                s = s4 - this.MIN;
            } else {
                s = s4 + this.MIN;
            }
            this.fsv.setRightClick(s, this.MAX, this.MIN);
        }
    }

    @Override
    public void onChange(float percent, boolean isRight) {
        this.speed = (int) (((this.MAX - this.MIN) * percent) + this.MIN);
        float s = (this.speed * 1.0f) / 10.0f;
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(s, 1, true));
        if (!isRight) {
            this.speed = -this.speed;
        }
    }

    @Override
    public void onSendData() {
        if (this.listener != null) {
            this.listener.onSendSpeed(this.speed * this.accuracy);
        }
    }

    public void setOnSendSpeedListener(OnSendSpeedListener listener) {
        this.listener = listener;
    }

    public void setSpeed(int s) {
        this.fsv.setSpeed(s / 10, this.MAX - this.MIN);
    }

    public void setMaxMin(int max, int min, int accuracy) {
        this.MAX = max;
        this.MIN = min;
        this.accuracy = accuracy;
    }

    public void setSpeed2(int s) {
        int s2;
        if (s >= 0) {
            s2 = s - this.MIN;
        } else {
            s2 = s + this.MIN;
        }
        this.fsv.setSpeed(s2, this.MAX - this.MIN);
    }

    public void switchUnity() {
        float s = (this.speed * 1.0f) / 10.0f;
        this.tvSpeed.setText(X8NumberUtil.getSpeedNumberString(s, 1, true));
    }

    public interface OnSendSpeedListener {
        void onSendSpeed(int i);
    }
}
