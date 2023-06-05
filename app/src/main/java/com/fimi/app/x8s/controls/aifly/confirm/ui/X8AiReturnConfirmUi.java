package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.tools.ImageUtils;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;

/* loaded from: classes.dex */
public class X8AiReturnConfirmUi implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    float temp = 0.0f;
    int res = 0;
    int tmpRes = 0;
    private int MAX;
    private View btnOk;
    private View contentView;
    private ImageView imgFlag;
    private View imgReturn;
    private X8MainAiFlyController listener;
    private FcCtrlManager mFcCtrlManager;
    private SeekBar mSeekBar;
    private X8MainAiFlyController mX8MainAiFlyController;
    private String prex;
    private String prex2;
    private View rlMinus;
    private View rlPlus;
    private TextView tvCuurentHeight;
    private TextView tvHeight;
    private int MIN = 0;
    private int accuracy = 10;
    private float seekBarMax = 120.0f * this.accuracy;
    private float seekBarMin = 30.0f * this.accuracy;

    public X8AiReturnConfirmUi(Activity activity, View parent) {
        this.MAX = 0;
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_return_layout, (ViewGroup) parent, true);
        this.MAX = (int) (this.seekBarMax - this.seekBarMin);
        initViews(this.contentView);
        initActions();
    }

    public void setX8MainAiFlyController(X8MainAiFlyController mX8MainAiFlyController, FcCtrlManager mFcCtrlManager) {
        this.mX8MainAiFlyController = mX8MainAiFlyController;
        this.mFcCtrlManager = mFcCtrlManager;
        getHeight();
    }

    public void initViews(View rootView) {
        this.imgReturn = rootView.findViewById(R.id.img_ai_follow_return);
        this.btnOk = rootView.findViewById(R.id.btn_ai_follow_confirm_ok);
        this.prex = rootView.getContext().getString(R.string.x8_ai_fly_return_home_tip);
        this.prex2 = rootView.getContext().getString(R.string.x8_ai_fly_return_home_tip2);
        this.tvCuurentHeight = (TextView) rootView.findViewById(R.id.tv_ai_follow_confirm_title1);
        this.tvHeight = (TextView) rootView.findViewById(R.id.tv_limit_height);
        this.rlMinus = rootView.findViewById(R.id.rl_minus);
        this.rlPlus = rootView.findViewById(R.id.rl_plus);
        this.mSeekBar = (SeekBar) rootView.findViewById(R.id.sb_value);
        this.mSeekBar.setMax(this.MAX);
        this.imgFlag = (ImageView) rootView.findViewById(R.id.img_ai_return_flag);
        AutoFcSportState state = StateManager.getInstance().getX8Drone().getFcSportState();
        if (state != null) {
            showSportState(state);
        }
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.rlMinus.setOnClickListener(this);
        this.rlPlus.setOnClickListener(this);
        this.mSeekBar.setOnSeekBarChangeListener(this);
    }

    public void getHeight() {
        setProgress((int) (StateManager.getInstance().getX8Drone().getReturnHomeHight() * this.accuracy));
        this.mFcCtrlManager.getReturnHomeHeight(new UiCallBackListener<AckGetRetHeight>() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiReturnConfirmUi.1
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, AckGetRetHeight aFloat) {
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_ai_follow_return) {
            this.mX8MainAiFlyController.onCloseConfirmUi();
        } else if (id == R.id.btn_ai_follow_confirm_ok) {
            onComfirnClick();
        } else if (id == R.id.rl_minus) {
            if (this.mSeekBar.getProgress() != this.MIN) {
                this.mSeekBar.setProgress(this.mSeekBar.getProgress() - (this.accuracy * 1));
                setHeightLimit();
            }
        } else if (id == R.id.rl_plus && this.mSeekBar.getProgress() != this.MAX) {
            this.mSeekBar.setProgress(this.mSeekBar.getProgress() + (this.accuracy * 1));
            setHeightLimit();
        }
    }

    public void setProgress(int progress) {
        int p = (int) (progress - this.seekBarMin);
        this.mSeekBar.setProgress(p);
        String s = this.prex2 + X8NumberUtil.getDistanceNumberString((progress * 1.0f) / this.accuracy, 1, true);
        this.tvHeight.setText(s);
    }

    public void setProgress1(float progress) {
        float p = progress + this.seekBarMin;
        String s = this.prex2 + X8NumberUtil.getDistanceNumberString(p / this.accuracy, 1, true);
        this.tvHeight.setText(s);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setProgress1(progress);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        setHeightLimit();
    }

    public void onComfirnClick() {
        this.mX8MainAiFlyController.onRetureHomeClick();
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnOk.setEnabled(true);
        } else {
            this.btnOk.setEnabled(false);
        }
    }

    public void showSportState(AutoFcSportState state) {
        float h = state.getHeight();
        float d = state.getHomeDistance();
        if (d <= 10.0f) {
            if (h <= 3.0f) {
                this.temp = 3.0f;
                this.tmpRes = R.drawable.x8_img_ai_return_1;
            } else {
                this.temp = h;
                this.tmpRes = R.drawable.x8_img_ai_return_2;
            }
        } else if (h <= StateManager.getInstance().getX8Drone().getReturnHomeHight()) {
            this.temp = StateManager.getInstance().getX8Drone().getReturnHomeHight();
            this.tmpRes = R.drawable.x8_img_ai_return_3;
        } else {
            this.temp = h;
            this.tmpRes = R.drawable.x8_img_ai_return_4;
        }
        String s = String.format(this.prex, X8NumberUtil.getDistanceNumberString(this.temp, 1, true));
        this.tvCuurentHeight.setText(s);
        if (this.tmpRes != 0 && this.tmpRes != this.res) {
            this.res = this.tmpRes;
            this.imgFlag.setImageBitmap(ImageUtils.getBitmapByPath(this.contentView.getContext(), this.res));
        }
    }

    public void setHeightLimit() {
        final float h = (this.mSeekBar.getProgress() + this.seekBarMin) / this.accuracy;
        this.mFcCtrlManager.setReturnHome(new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiReturnConfirmUi.2
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (!cmdResult.isSuccess()) {
                    X8AiReturnConfirmUi.this.setProgress((int) (StateManager.getInstance().getX8Drone().getReturnHomeHight() * X8AiReturnConfirmUi.this.accuracy));
                } else {
                    StateManager.getInstance().getX8Drone().setReturnHomeHight(h);
                }
            }
        }, h);
    }
}
