package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.InputDeviceCompat;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.IX8ValueSeakBarViewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.widget.SwitchButton;
import com.fimi.x8sdk.modulestate.StateManager;

public class X8ValueSeakBarWithTip extends RelativeLayout implements View.OnClickListener, X8SeekBarView.SlideChangeListener {
    private static final int DISABLED_ALPHA = 102;
    private static final int ENABLED_ALPHA = 255;
    private final int MIN;
    private final ImageButton imbConfirm;
    private final ImageView imgMenu;
    private final X8SeekBarView mSeekBar;
    private final TextView mTvTitle;
    private final TextView mTvValue;
    private final String na;
    private final View rlFlagMenu;
    private final View rlMinus;
    private final View rlPlus;
    private final View rlSeekBar;
    private final SwitchButton switchButton;
    private final View vMinus;
    private final View vPlus;
    OnProgressConfirmListener confirmListener;
    RelativeLayout rlTittle;
    private int MAX;
    private int accuracy;
    private int closeColor;
    private int currentProgress;
    private float currentValue;
    private int historyProgress;
    private boolean isEnableClick;
    private boolean isFloat;
    private IX8ValueSeakBarViewListener listener;
    private int openColor;
    private float seekBarDefault;
    private float seekBarMax;
    private float seekBarMin;
    private String suffix;
    private String title;

    public X8ValueSeakBarWithTip(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.suffix = "";
        this.title = "";
        this.currentProgress = 0;
        this.currentValue = 0.0f;
        this.MAX = 0;
        this.MIN = 0;
        this.accuracy = 1;
        this.isEnableClick = true;
        readAttr(context, attrs);
        this.na = context.getResources().getString(R.string.x8_na);
        LayoutInflater.from(context).inflate(R.layout.x8_value_seekbar_with_tip_layout, this, true);
        this.mTvTitle = findViewById(R.id.tv_title);
        this.mTvValue = findViewById(R.id.tv_value);
        this.imgMenu = findViewById(R.id.img_flag_menu);
        this.vMinus = findViewById(R.id.view_minus);
        this.vPlus = findViewById(R.id.view_plus);
        this.imbConfirm = findViewById(R.id.imb_confirm);
        this.mSeekBar = findViewById(R.id.sb_value);
        this.rlTittle = findViewById(R.id.rl_title);
        this.rlFlagMenu = findViewById(R.id.rl_flag_menu);
        this.rlSeekBar = findViewById(R.id.rl_seekbar);
        this.rlMinus = findViewById(R.id.rl_minus);
        this.rlPlus = findViewById(R.id.rl_plus);
        this.imbConfirm.setOnClickListener(this);
        this.rlMinus.setOnClickListener(this);
        this.rlPlus.setOnClickListener(this);
        this.rlFlagMenu.setOnClickListener(this);
        this.mSeekBar.setOnSlideChangeListener(this);
        this.rlSeekBar.setVisibility(View.GONE);
        this.mTvValue.setTextColor(this.closeColor);
        this.mTvTitle.setText(this.title);
        this.mSeekBar.setMaxProgress(this.MAX);
        this.imbConfirm.setEnabled(false);
        this.switchButton = findViewById(R.id.swb_toggle);
    }

    @Override
    public void onStart(X8SeekBarView slideView, int progress) {
    }

    @Override
    public void onProgress(X8SeekBarView slideView, int progress) {
        this.mTvValue.setText(getValueString(progress));
        this.historyProgress = progress;
        this.imbConfirm.setEnabled(this.currentProgress != progress);
    }

    @Override
    public void onStop(X8SeekBarView slideView, int progress) {
    }

    public boolean isEnableClick() {
        return this.isEnableClick;
    }

    public void setEnableClick(boolean enableClick) {
        this.isEnableClick = enableClick;
    }

    public void setConfirmListener(OnProgressConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public void setRelayoutHeightParam(int height) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.rlTittle.getLayoutParams();
        layoutParams.height = height;
    }

    public void setImgMenuVisiable(int visiable) {
        this.imgMenu.setVisibility(visiable);
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.rl_flag_menu) {
            if (this.isEnableClick) {
                if (this.rlSeekBar.getVisibility() == View.GONE) {
                    this.rlSeekBar.setVisibility(View.VISIBLE);
                    this.mTvValue.setTextColor(this.openColor);
                    this.imgMenu.setSelected(true);
                    if (this.listener != null) {
                        this.listener.onSelect(true);
                        return;
                    }
                    return;
                }
                onOtherSelect();
            }
        } else if (id == R.id.rl_minus) {
            if (this.mSeekBar.getProgress() != this.MIN) {
                int s = this.mSeekBar.getProgress() - this.accuracy;
                if (s < this.MIN) {
                    s = this.MIN;
                }
                this.mSeekBar.setProgress(s);
            }
        } else if (id == R.id.rl_plus) {
            if (this.mSeekBar.getProgress() != this.MAX) {
                int s2 = this.mSeekBar.getProgress() + this.accuracy;
                if (s2 > this.MAX) {
                    s2 = this.MAX;
                }
                this.mSeekBar.setProgress(s2);
            }
        } else if (id == R.id.imb_confirm && this.confirmListener != null) {
            this.confirmListener.onConfirm(this.currentValue / this.accuracy);
        }
    }

    public void setImbConfirmEnable(boolean enable) {
        this.currentProgress = this.historyProgress;
        this.imbConfirm.setEnabled(enable);
    }

    public void setViewEnableByMode(boolean b) {
        if (b) {
            this.imbConfirm.setEnabled(this.currentProgress != this.mSeekBar.getProgress());
        } else {
            this.imbConfirm.setEnabled(b);
        }
        this.mSeekBar.setEnabled(b);
        this.vMinus.setEnabled(b);
        this.vPlus.setEnabled(b);
        this.rlMinus.setEnabled(b);
        this.rlPlus.setEnabled(b);
        this.switchButton.setEnabled(b);
        if (b) {
            this.mSeekBar.setAlpha(1.0f);
            this.switchButton.setAlpha(1.0f);
            this.vMinus.getBackground().setAlpha(255);
            this.vPlus.getBackground().setAlpha(255);
            return;
        }
        this.mSeekBar.setAlpha(0.4f);
        this.switchButton.setAlpha(0.4f);
        this.vMinus.getBackground().setAlpha(102);
        this.vPlus.getBackground().setAlpha(102);
    }

    public void setViewEnable(boolean b) {
        this.imbConfirm.setEnabled(b);
        this.mSeekBar.setEnabled(b);
        this.vMinus.setEnabled(b);
        this.vPlus.setEnabled(b);
        this.rlMinus.setEnabled(b);
        this.rlPlus.setEnabled(b);
        this.switchButton.setEnabled(b);
        if (b) {
            this.mSeekBar.setAlpha(1.0f);
            this.switchButton.setAlpha(1.0f);
            this.vMinus.getBackground().setAlpha(255);
            this.vPlus.getBackground().setAlpha(255);
            return;
        }
        this.mSeekBar.setAlpha(0.4f);
        this.switchButton.setAlpha(0.4f);
        this.vMinus.getBackground().setAlpha(102);
        this.vPlus.getBackground().setAlpha(102);
        this.mTvValue.setText(this.na);
    }

    private void readAttr(@NonNull Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.X8ValueSeakBar);
        this.accuracy = a.getInteger(R.styleable.X8ValueSeakBar_x8_value_accuracy, 1);
        this.title = a.getString(R.styleable.X8ValueSeakBar_x8_value_title);
        this.suffix = a.getString(R.styleable.X8ValueSeakBar_x8_value_suffix);
        this.closeColor = a.getColor(R.styleable.X8ValueSeakBar_x8_value_close_color, -1);
        this.openColor = a.getColor(R.styleable.X8ValueSeakBar_x8_value_open_color, InputDeviceCompat.SOURCE_ANY);
        this.seekBarMax = a.getFloat(R.styleable.X8ValueSeakBar_x8_value_seekbar_max, 0.0f) * this.accuracy;
        this.seekBarMin = a.getFloat(R.styleable.X8ValueSeakBar_x8_value_seekbar_min, 0.0f) * this.accuracy;
        this.seekBarDefault = a.getFloat(R.styleable.X8ValueSeakBar_x8_value_seekbar_default, 0.0f);
        this.isFloat = a.getBoolean(R.styleable.X8ValueSeakBar_x8_value_seekbar_float, false);
        a.recycle();
        if (this.isFloat) {
            this.MAX = (int) (this.seekBarMax - this.seekBarMin);
        } else {
            this.MAX = (int) (this.seekBarMax - this.seekBarMin);
        }
    }

    public void switchUnityWithSpeedLimit() {
        this.mTvValue.setText(X8NumberUtil.getSpeedNumberString(this.currentValue / this.accuracy, 1, true));
    }

    public void switchUnityWithDistanceLimit() {
        this.mTvValue.setText(X8NumberUtil.getDistanceNumberString(this.currentValue / this.accuracy, 1, true));
    }

    public String getValueString(int progress) {
        String s;
        String s2;
        if (this.isFloat) {
            float p = progress + this.seekBarMin;
            this.currentValue = p;
            if (this.suffix.equals("M")) {
                s2 = X8NumberUtil.getDistanceNumberString(this.currentValue / this.accuracy, 1, true);
            } else if (this.suffix.equals("M/S")) {
                s2 = X8NumberUtil.getSpeedNumberString(this.currentValue / this.accuracy, 1, true);
            } else {
                s2 = p + this.suffix;
            }
            return s2;
        }
        int p2 = (int) (progress + this.seekBarMin);
        this.currentValue = p2;
        if (this.suffix.equals("M")) {
            s = X8NumberUtil.getDistanceNumberString(this.currentValue / this.accuracy, 1, true);
        } else if (this.suffix.equals("M/S")) {
            s = X8NumberUtil.getSpeedNumberString(this.currentValue / this.accuracy, 1, true);
        } else {
            s = p2 + this.suffix;
        }
        return s;
    }

    public void setProgress(float progress) {
        this.currentValue = this.accuracy * progress;
        int p = (int) (this.currentValue - this.seekBarMin);
        this.mSeekBar.setProgress(p);
        this.mTvValue.setText(getValueString(this.mSeekBar.getProgress()));
        this.currentProgress = p;
    }

    public void setProgress(int progress) {
        this.currentValue = this.accuracy * progress;
        int p = (int) (this.currentValue - this.seekBarMin);
        this.mSeekBar.setProgress(p);
        this.currentProgress = p;
    }

    public float getCurrentValue() {
        return this.currentValue;
    }

    private void resetView() {
        if (this.isFloat) {
            setProgress(this.currentValue);
        } else {
            setProgress((int) this.currentValue);
        }
    }

    public void onOtherSelect() {
        if (StateManager.getInstance().getConectState().isConnectDrone()) {
            this.mTvValue.setText(getValueString(this.currentProgress));
            this.mSeekBar.setProgress(this.currentProgress);
        }
        this.rlSeekBar.setVisibility(View.GONE);
        this.mTvValue.setTextColor(this.closeColor);
        this.imgMenu.setSelected(false);
    }

    public void setListener(IX8ValueSeakBarViewListener ix8ValueSeakBarViewListener) {
        this.listener = ix8ValueSeakBarViewListener;
    }

    public void setSwitchButtonVisibility(int visibility) {
        this.switchButton.setVisibility(visibility);
    }

    public void setOnSwitchListener(SwitchButton.OnSwitchListener listener) {
        this.switchButton.setOnSwitchListener(listener);
    }

    public void setSwitchButtonState(boolean b) {
        this.switchButton.setSwitchState(b);
    }

    public void setNoLimit() {
        this.imgMenu.setVisibility(View.GONE);
        setEnableClick(false);
        onOtherSelect();
        this.mTvValue.setText(getResources().getString(R.string.x8_fc_fly_distance_limit_tip));
    }

    public void setProgress(float progress, boolean b) {
        setProgress(progress);
        setEnableClick(b);
        if (b) {
            this.imgMenu.setVisibility(View.VISIBLE);
        } else {
            this.imgMenu.setVisibility(View.GONE);
        }
    }

    public void setSwitchButtonByNew() {
        onOtherSelect();
        setImgMenuVisiable(8);
        this.switchButton.setVisibility(View.GONE);
    }

    public void setSwitchButtonVisibility() {
        this.switchButton.setVisibility(View.VISIBLE);
    }

    public void setValueVisibily(int v, boolean b) {
        this.mTvValue.setVisibility(v);
        if (b) {
            this.imgMenu.setVisibility(View.GONE);
        } else {
            this.imgMenu.setVisibility(v);
        }
    }

    public interface OnProgressConfirmListener {
        void onConfirm(float f);
    }
}
