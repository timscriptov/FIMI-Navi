package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.LongClickListener;

public class X8CustomSeekBar extends LinearLayout implements View.OnClickListener, X8SeekBarView.SlideChangeListener {
    private final String TAG;
    private final LongClickListener longClickListener;
    private final RelativeLayout rlAdd;
    private final RelativeLayout rlReduce;
    private final X8SeekBarView seekBar;
    private final TextView tvParam;
    private int curValue;
    private onSeekValueSetListener listener;
    private String name;
    private int seekBarMax;
    private int seekBarMin;

    @SuppressLint({"ClickableViewAccessibility"})
    public X8CustomSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "DDLog";
        this.name = "name";
        this.seekBarMax = 100;
        this.seekBarMin = 10;
        this.curValue = 10;
        this.longClickListener = new LongClickListener() {
            @Override
            public void longClickCallback(int viewId) {
                if (viewId == R.id.rl_add) {
                    if (X8CustomSeekBar.this.curValue < X8CustomSeekBar.this.seekBarMax) {
                        X8CustomSeekBar.this.curValue += 1;
                        X8CustomSeekBar.this.setProgress(X8CustomSeekBar.this.curValue);
                    }
                } else if (viewId == R.id.rl_reduce && X8CustomSeekBar.this.curValue > X8CustomSeekBar.this.seekBarMin) {
                    X8CustomSeekBar.this.curValue -= 1;
                    X8CustomSeekBar.this.setProgress(X8CustomSeekBar.this.curValue);
                }
            }

            @Override
            public void onFingerUp(int viewId) {
                if (X8CustomSeekBar.this.listener != null) {
                    X8CustomSeekBar.this.listener.onSeekValueSet(X8CustomSeekBar.this.getId(), X8CustomSeekBar.this.curValue);
                }
            }
        };
        View view = LayoutInflater.from(context).inflate(R.layout.x8_view_custom_seekbar, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        view.setLayoutParams(layoutParams);
        addView(view);
        this.rlAdd = view.findViewById(R.id.rl_add);
        this.rlReduce = view.findViewById(R.id.rl_reduce);
        this.rlAdd.setOnClickListener(this);
        this.rlReduce.setOnClickListener(this);
        this.rlAdd.setOnTouchListener(this.longClickListener);
        this.rlReduce.setOnTouchListener(this.longClickListener);
        this.tvParam = view.findViewById(R.id.tv_param);
        this.seekBar = view.findViewById(R.id.sb_value);
        this.seekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.seekBar.setOnSlideChangeListener(this);
        setProgress(this.curValue);
    }

    public void initData(String name, int seekBarMin, int seekBarMax) {
        this.name = name;
        this.seekBarMin = seekBarMin;
        this.seekBarMax = seekBarMax;
        setProgress(this.curValue);
    }

    public void setProgress(int value) {
        if (value > this.seekBarMax) {
            value = this.seekBarMax;
        }
        if (value < this.seekBarMin) {
            value = this.seekBarMin;
        }
        this.curValue = value;
        this.seekBar.setProgress(value - this.seekBarMin);
        this.tvParam.setText(this.name + "\u3000" + this.curValue + "%");
    }

    public int getCurValue() {
        return this.curValue;
    }

    @Override
    public void onClick(@NonNull View v) {
        int i = v.getId();
        if (i == R.id.rl_add) {
            if (this.curValue < this.seekBarMax) {
                this.curValue++;
                if (this.listener != null) {
                    this.listener.onSeekValueSet(getId(), this.curValue);
                }
            }
        } else if (i == R.id.rl_reduce && this.curValue > this.seekBarMin) {
            this.curValue--;
            if (this.listener != null) {
                this.listener.onSeekValueSet(getId(), this.curValue);
            }
        }
    }

    public void setOnSeekChangedListener(onSeekValueSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart(X8SeekBarView slideView, int progress) {
    }

    @Override
    public void onProgress(X8SeekBarView slideView, int progress) {
        this.curValue = this.seekBarMin + progress;
    }

    @Override
    public void onStop(X8SeekBarView slideView, int progress) {
        if (this.listener != null) {
            this.listener.onSeekValueSet(getId(), this.curValue);
        }
    }

    public interface onSeekValueSetListener {
        void onSeekValueSet(int i, int i2);
    }
}
