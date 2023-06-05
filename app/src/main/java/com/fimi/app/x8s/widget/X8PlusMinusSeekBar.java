package com.fimi.app.x8s.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;

public class X8PlusMinusSeekBar extends RelativeLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final int defaultValue;
    private final View imgReset;
    private final SeekBar mSeekBar;
    private final View rlMinus;
    private final View rlPlus;
    private final int seekBarMax;
    private final int seekBarMin;
    private int curValue;
    private X8CustomSeekBar.onSeekValueSetListener listener;
    private X8CustomSeekBar.onSeekValueSetListener onSeekChangedListener;

    public X8PlusMinusSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.seekBarMax = 100;
        this.seekBarMin = 10;
        this.curValue = 10;
        this.defaultValue = 0;
        LayoutInflater.from(context).inflate(R.layout.x8_plus_minus_seekbar_layout, this, true);
        this.mSeekBar = findViewById(R.id.sb_value);
        this.rlMinus = findViewById(R.id.rl_minus);
        this.rlPlus = findViewById(R.id.rl_plus);
        this.imgReset = findViewById(R.id.img_reset);
        this.rlMinus.setOnClickListener(this);
        this.rlPlus.setOnClickListener(this);
        this.imgReset.setOnClickListener(this);
        this.mSeekBar.setMax(this.seekBarMax - this.seekBarMin);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        setProgress(this.curValue);
    }

    public void setListener(X8CustomSeekBar.onSeekValueSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(@NonNull View v) {
        int i = v.getId();
        if (i == R.id.rl_plus) {
            if (this.mSeekBar.getProgress() != this.mSeekBar.getMax()) {
                this.mSeekBar.setProgress(this.mSeekBar.getProgress() + 1);
            }
        } else if (i == R.id.rl_minus) {
            if (this.mSeekBar.getProgress() != 0) {
                this.mSeekBar.setProgress(this.mSeekBar.getProgress() - 1);
            }
        } else if (i == R.id.img_reset) {
            this.mSeekBar.setProgress(this.defaultValue);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.curValue = this.seekBarMin + progress;
        if (this.listener != null) {
            this.listener.onSeekValueSet(getId(), this.curValue);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
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
}
