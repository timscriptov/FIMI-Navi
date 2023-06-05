package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.fimi.android.app.R;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.jsonResult.CameraParamsJson;
import com.fimi.x8sdk.modulestate.X8CameraSettings;

import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class X8VerticalSeekBarValueLayout extends RelativeLayout implements X8VerticalSeekBar.SlideChangeListener {
    private final Handler mHandler;
    private final TextView tvValue;
    private final X8VerticalSeekBar verticalSeekBar;
    private final View view;
    private CameraManager cameraManager;
    private int curValue;
    private int lastValue;
    private PublishSubject<Integer> mSearchResultsSubject;
    private Subscription mTextWatchSubscription;
    private String prex;
    private int seekBarMax;
    private int seekBarMin;

    @SuppressLint("HandlerLeak")
    public X8VerticalSeekBarValueLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.seekBarMax = 30;
        this.seekBarMin = 10;
        this.curValue = 10;
        this.lastValue = 0;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        this.view = LayoutInflater.from(context).inflate(R.layout.x8_vertical_seek_bar_value_layout, this, true);
        this.verticalSeekBar = findViewById(R.id.verticalSeekBar);
        this.tvValue = findViewById(R.id.tv_value);
        this.verticalSeekBar.setProgress(0);
        this.verticalSeekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.verticalSeekBar.setOrientation(0);
        this.view.measure(0, 0);
        this.tvValue.measure(0, 0);
        this.verticalSeekBar.setTextHeight(this.view.getMeasuredHeight(), this.tvValue.getMeasuredHeight());
        this.verticalSeekBar.setOnSlideChangeListener(this);
        this.prex = "x";
        runRxAnroid();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTextWatchSubscription != null && !this.mTextWatchSubscription.isUnsubscribed()) {
            this.mTextWatchSubscription.unsubscribe();
        }
    }

    public void setMinMax(@NonNull int[] minMax, CameraManager cameraManager) {
        this.seekBarMax = minMax[1];
        this.seekBarMin = minMax[0];
        this.verticalSeekBar.setMaxProgress(this.seekBarMax - this.seekBarMin);
        this.cameraManager = cameraManager;
    }

    public void setProgress(int value) {
        if (value > this.seekBarMax) {
            value = this.seekBarMax;
        }
        if (value < this.seekBarMin) {
            value = this.seekBarMin;
        }
        this.curValue = value;
        this.lastValue = value;
        this.prex = this.prex;
        this.verticalSeekBar.setProgress(value - this.seekBarMin);
    }

    public void changeProcess(boolean isDown) {
        int progess;
        int progess2 = this.verticalSeekBar.getProcess();
        if (isDown) {
            progess = progess2 - 1;
            if (progess < 0) {
                progess = 0;
            }
        } else {
            progess = progess2 + 1;
            if (progess > this.seekBarMax - this.seekBarMin) {
                progess = this.seekBarMax - this.seekBarMin;
            }
        }
        setProgress(progess + this.seekBarMin);
        sendJsonCmdSetFocuse(this.lastValue);
    }

    @Override
    public void onStart(X8VerticalSeekBar slideView, int progress) {
    }

    @Override
    public void onProgress(X8VerticalSeekBar slideView, int progress) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.tvValue.getLayoutParams());
        lp.setMargins(this.verticalSeekBar.getDestX(), this.verticalSeekBar.getDestY(), 0, 0);
        this.tvValue.setLayoutParams(lp);
        this.curValue = this.seekBarMin + progress;
        this.tvValue.setText("" + (this.curValue / 10.0f) + this.prex);
        this.mSearchResultsSubject.onNext(Integer.valueOf(this.curValue));
    }

    @Override
    public void onStop(X8VerticalSeekBar slideView, int progress) {
        this.mHandler.postDelayed(() -> {
            X8VerticalSeekBarValueLayout.this.mSearchResultsSubject.onNext(Integer.valueOf(X8VerticalSeekBarValueLayout.this.curValue));
        }, 510L);
    }

    public String getCurrentProcess() {
        return "" + (this.curValue / 10.0f);
    }

    public void sendJsonCmdSetFocuse(int param) {
        final String s = "" + (param / 10.0f);
        this.cameraManager.setCameraFocuse(s, (rt, o) -> {
            if (rt != null) {
                CameraParamsJson paramsJson = JSON.parseObject(rt.toString(), CameraParamsJson.class);
                if (paramsJson != null) {
                    X8CameraSettings.setFocusSetting(s);
                } else {
                    X8VerticalSeekBarValueLayout.this.setProgress(X8CameraSettings.getFocuse());
                }
                return;
            }
            X8VerticalSeekBarValueLayout.this.setProgress(X8CameraSettings.getFocuse());
        });
    }

    private void runRxAnroid() {
        this.mSearchResultsSubject = PublishSubject.create();
        this.mTextWatchSubscription = this.mSearchResultsSubject.throttleLast(500L, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Integer cities) {
                if (X8VerticalSeekBarValueLayout.this.lastValue != cities) {
                    X8VerticalSeekBarValueLayout.this.lastValue = cities;
                    X8VerticalSeekBarValueLayout.this.sendJsonCmdSetFocuse(X8VerticalSeekBarValueLayout.this.lastValue);
                }
            }
        });
    }
}
