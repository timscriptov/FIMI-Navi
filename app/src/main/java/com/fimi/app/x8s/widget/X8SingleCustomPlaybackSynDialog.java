package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class X8SingleCustomPlaybackSynDialog extends X8sBaseDialog {
    private final ProgressBar x8PbPlaybackPlan;
    private final TextView x8TvPlaybackProgress;

    public X8SingleCustomPlaybackSynDialog(@NonNull Context context, @Nullable String title, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_view_custom_playback_syn_dialog);
        ImageView x8IvPlaybackAnimation = findViewById(R.id.x8_iv_playback_animation);
        x8IvPlaybackAnimation.setBackgroundResource(R.drawable.x8_calibration_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) x8IvPlaybackAnimation.getBackground();
        animationDrawable.start();
        this.x8PbPlaybackPlan = findViewById(R.id.x8_pb_playback_plan);
        this.x8TvPlaybackProgress = findViewById(R.id.x8_tv_playback_progress);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        Button tvSure = findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(v -> {
            X8SingleCustomPlaybackSynDialog.this.dismiss();
            if (listener != null) {
                listener.onSingleButtonClick();
            }
        });
    }

    @SuppressLint({"SetTextI18n"})
    public void setX8PbPlaybackPlanValue(int plan) {
        this.x8PbPlaybackPlan.setProgress(plan);
        this.x8TvPlaybackProgress.setText(plan + "%");
    }

    public interface onDialogButtonClickListener {
        void onSingleButtonClick();
    }
}
