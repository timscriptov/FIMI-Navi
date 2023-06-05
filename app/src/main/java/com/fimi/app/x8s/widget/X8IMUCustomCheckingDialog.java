package com.fimi.app.x8s.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

public class X8IMUCustomCheckingDialog extends Dialog {
    private final int STATE_PAUSE;
    private final int STATE_PLAYING;
    private final int STATE_STOP;
    private final DialogInterface.OnKeyListener keyListener;
    ImageView x8ViewImuCheckingLoading;
    private ObjectAnimator objectAnimator;
    private int state;

    public X8IMUCustomCheckingDialog(@NonNull Context context) {
        super(context, R.style.fimisdk_custom_dialog);
        this.STATE_PLAYING = 1;
        this.STATE_PAUSE = 2;
        this.STATE_STOP = 3;
        this.keyListener = (dialogInterface, i, keyEvent) -> i == 4 && keyEvent.getRepeatCount() == 0;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.x8_imu_view_custom_checking_dialog);
        this.x8ViewImuCheckingLoading = findViewById(R.id.x8_view_imu_checking_loading);
        init();
        playLoading();
        setOnKeyListener(this.keyListener);
    }

    private void init() {
        this.state = 3;
        this.objectAnimator = ObjectAnimator.ofFloat(this.x8ViewImuCheckingLoading, "rotation", 0.0f, 360.0f);
        this.objectAnimator.setDuration(1500L);
        this.objectAnimator.setInterpolator(new LinearInterpolator());
        this.objectAnimator.setRepeatCount(-1);
        this.objectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    public void playLoading() {
        if (this.state == 3) {
            this.objectAnimator.start();
            this.state = 1;
        } else if (this.state == 2) {
            this.objectAnimator.resume();
            this.state = 1;
        } else if (this.state == 1) {
            this.objectAnimator.pause();
            this.state = 2;
        }
    }

    public void stopLoading() {
        this.objectAnimator.end();
        this.state = 3;
    }
}
