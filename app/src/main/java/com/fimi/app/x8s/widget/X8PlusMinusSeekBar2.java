package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

public class X8PlusMinusSeekBar2 extends RelativeLayout implements View.OnClickListener, X8SeekBarView.SlideChangeListener {
    private final X8SeekBarView mSeekBar;
    private final View rlMinus;
    private final View rlPlus;
    private int curValue;
    private onSeekValueSetListener listener;
    private int seekBarMax;
    private int seekBarMin;

    public X8PlusMinusSeekBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.seekBarMax = 100;
        this.seekBarMin = 10;
        this.curValue = 10;
        LayoutInflater.from(context).inflate(R.layout.x8_plus_minus_seekbar_layout2, this, true);
        this.mSeekBar = findViewById(R.id.sb_value);
        this.rlMinus = findViewById(R.id.rl_minus);
        this.rlPlus = findViewById(R.id.rl_plus);
        this.rlMinus.setOnClickListener(this);
        this.rlPlus.setOnClickListener(this);
        this.mSeekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.mSeekBar.setOnSlideChangeListener(this);
        setProgress(this.curValue);
    }

    public void setListener(onSeekValueSetListener listener) {
        this.listener = listener;
    }

    public void initData(int seekBarMin, int seekBarMax) {
        this.seekBarMin = seekBarMin;
        this.seekBarMax = seekBarMax;
        this.mSeekBar.setMaxProgress(seekBarMax - seekBarMin);
        setProgress(this.curValue);
    }

    @Override
    public void onClick(@NonNull View v) {
        int i = v.getId();
        if (i == R.id.rl_plus) {
            if (this.mSeekBar.getProgress() != this.mSeekBar.getMaxProgress()) {
                int s = this.mSeekBar.getProgress() + 1;
                if (s > this.mSeekBar.getMaxProgress()) {
                    s = this.mSeekBar.getMaxProgress();
                }
                this.mSeekBar.setProgress(s);
            }
        } else if (i == R.id.rl_minus && this.mSeekBar.getProgress() != 0) {
            int s2 = this.mSeekBar.getProgress() - 1;
            if (s2 < 0) {
                s2 = 0;
            }
            this.mSeekBar.setProgress(s2);
        }
    }

    public int getProgress() {
        return this.curValue;
    }

    public void setProgress(int value) {
        if (value > this.seekBarMax) {
            value = this.seekBarMax;
        }
        if (value < this.seekBarMin) {
            value = this.seekBarMin;
        }
        this.curValue = value;
        this.mSeekBar.setProgress(value - this.seekBarMin);
    }

    @Override
    public void onStart(@NonNull X8SeekBarView slideView, int progress) {
        this.listener.onStart(slideView.getId(), progress);
    }

    @Override
    public void onProgress(X8SeekBarView slideView, int progress) {
        this.curValue = this.seekBarMin + progress;
        if (this.listener != null) {
            this.listener.onSeekValueSet(getId(), this.curValue);
        }
    }

    @Override
    public void onStop(@NonNull X8SeekBarView slideView, int progress) {
        this.listener.onStop(slideView.getId(), progress);
    }

    public interface onSeekValueSetListener {
        void onSeekValueSet(int i, int i2);

        void onStart(int i, int i2);

        void onStop(int i, int i2);
    }
}
