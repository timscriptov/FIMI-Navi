package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.IX8ErrorTextSwitchView;

import java.util.ArrayList;
import java.util.List;

public class X8ErrorTextSwitchView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private final int colorRes;
    private final Context context;
    private int index;
    private final Handler mHandler;
    private IX8ErrorTextSwitchView mRotationText;
    private TextView mTv;
    private List<String> resString;

    @SuppressLint("HandlerLeak")
    public X8ErrorTextSwitchView(Context context) {
        super(context);
        this.index = -1;
        this.resString = new ArrayList<>();
        this.colorRes = R.color.white_100;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                X8ErrorTextSwitchView.this.updateText();
            }
        };
        this.context = context;
        init();
    }

    @SuppressLint("HandlerLeak")
    public X8ErrorTextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.index = -1;
        this.resString = new ArrayList<>();
        this.colorRes = R.color.white_100;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                X8ErrorTextSwitchView.this.updateText();
            }
        };
        this.context = context;
        init();
    }

    private void init() {
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(this.context, R.anim.in_animation));
        setOutAnimation(AnimationUtils.loadAnimation(this.context, R.anim.out_animation));
    }

    public void setResources(List<String> res, IX8ErrorTextSwitchView rotationText) {
        this.resString.clear();
        this.resString = res;
        this.mRotationText = rotationText;
        this.mHandler.sendEmptyMessage(0);
    }

    public void updateText() {
        this.index++;
        if (this.index >= this.resString.size() && this.mRotationText != null) {
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages(null);
            }
            this.mRotationText.isRotationFinish();
            this.index = -1;
        }
        if (this.index >= 0 && this.index < this.resString.size()) {
            setText(this.resString.get(this.index));
            this.mHandler.sendEmptyMessageDelayed(0, 4000L);
        }
    }

    @Override
    public View makeView() {
        this.mTv = new TextView(this.context);
        this.mTv.setTextSize(12.0f);
        this.mTv.setTextColor(getResources().getColor(this.colorRes));
        return this.mTv;
    }

    public void setTextColor(int resColor) {
        this.mTv.setTextColor(getResources().getColor(resColor));
    }
}
