package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.X8AiAutoPhototExcuteController;
import com.fimi.app.x8s.interfaces.IX8AiAutoPhototExcuteListener;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8SeekBarView;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.widget.SwitchButton;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.cmd.CmdAiAutoPhoto;
import com.fimi.x8sdk.modulestate.StateManager;

/* loaded from: classes.dex */
public class X8AiAutoPhotoExcuteConfirmUi implements View.OnClickListener, SwitchButton.OnSwitchListener {
    private int angle;
    private View btnOk;
    private View contentView;
    private float currentDroneHeight;
    private int distanceMax;
    private FcManager fcManager;
    private View imgReturn;
    private int item;
    private IX8NextViewListener listener;
    private Activity mActivity;
    private int mType;
    private X8MainAiFlyController mX8MainAiFlyController;
    private X8SeekBarView sbDistance;
    private X8SeekBarView sbSpeed;
    private SwitchButton swbAutoReturn;
    private TextView tvContent;
    private TextView tvDistance;
    private TextView tvSpeed;
    private TextView tvTime;
    private TextView tvTitle;
    private View vDistanceMinus;
    private View vDistancePlus;
    private View vSpeedMinus;
    private View vSpeedPlus;
    private int verticalMax;
    private X8AiAutoPhototExcuteController x8AiAutoPhototExcuteController;
    private float SPEED_MIN = 1.0f;
    private float SPEED_MAX = 10.0f;
    private int SPEED_MAX_PROGRESS = (int) ((this.SPEED_MAX - this.SPEED_MIN) * 10.0f);
    private float SPEED_DEFALOUT = 3.0f - this.SPEED_MIN;
    private float DISTANCE_MIN = 1.0f;
    private float DISTANCE_MAX = 300.0f;
    private int DISTANCE_MAX_PROGRESS = (int) ((this.DISTANCE_MAX - this.DISTANCE_MIN) * 10.0f);
    private float DISTANCE_DEFALOUT = 30.0f - this.DISTANCE_MIN;
    private float DISTANCE_VERTIIVAL_MAX = 120.0f;
    private int DISTANCE_VERTIIVAL_MAX_PROGRESS = (int) ((this.DISTANCE_VERTIIVAL_MAX - this.DISTANCE_MIN) * 10.0f);
    private X8SeekBarView.SlideChangeListener speedListener = new X8SeekBarView.SlideChangeListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiAutoPhotoExcuteConfirmUi.1
        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onProgress(X8SeekBarView slideView, int progress) {
            float speed = X8AiAutoPhotoExcuteConfirmUi.this.SPEED_MIN + (progress / 10.0f);
            String strSpeed = X8NumberUtil.getSpeedNumberString(speed, 1, true);
            X8AiAutoPhotoExcuteConfirmUi.this.tvSpeed.setText(strSpeed);
            X8AiAutoPhotoExcuteConfirmUi.this.setValue();
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStop(X8SeekBarView slideView, int progress) {
        }
    };
    private X8SeekBarView.SlideChangeListener distanceListener = new X8SeekBarView.SlideChangeListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiAutoPhotoExcuteConfirmUi.2
        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onProgress(X8SeekBarView slideView, int progress) {
            if (X8AiAutoPhotoExcuteConfirmUi.this.mType == 1) {
                float distance = X8AiAutoPhotoExcuteConfirmUi.this.DISTANCE_MIN + (progress / 10.0f);
                String d = X8NumberUtil.getDistanceNumberString(distance, 1, true);
                X8AiAutoPhotoExcuteConfirmUi.this.tvDistance.setText(d);
            } else {
                float distance2 = X8AiAutoPhotoExcuteConfirmUi.this.DISTANCE_MIN + (progress / 10.0f);
                String d2 = X8NumberUtil.getDistanceNumberString(distance2, 1, true);
                X8AiAutoPhotoExcuteConfirmUi.this.tvDistance.setText(d2);
            }
            X8AiAutoPhotoExcuteConfirmUi.this.setValue();
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStop(X8SeekBarView slideView, int progress) {
        }
    };

    public X8AiAutoPhotoExcuteConfirmUi(Activity activity, View parent, int type, int angle) {
        this.mActivity = activity;
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_auto_photo_excute_confirm_layout, (ViewGroup) parent, true);
        this.mType = type;
        if (type == 0) {
            this.angle = Math.abs(angle);
        } else {
            this.angle = Math.abs(9000);
        }
        initViews(this.contentView);
        initActions();
    }

    public void initViews(View rootView) {
        this.imgReturn = rootView.findViewById(R.id.img_ai_return);
        this.btnOk = rootView.findViewById(R.id.btn_ai_confirm_ok);
        this.tvTitle = (TextView) rootView.findViewById(R.id.tv_ai_title);
        this.tvContent = (TextView) rootView.findViewById(R.id.tv_ai_next_content1);
        this.tvTime = (TextView) rootView.findViewById(R.id.tv_ai_time);
        this.tvSpeed = (TextView) rootView.findViewById(R.id.tv_ai_speed);
        this.tvDistance = (TextView) rootView.findViewById(R.id.tv_ai_distance);
        this.vSpeedMinus = rootView.findViewById(R.id.rl_speed_minus);
        this.vSpeedPlus = rootView.findViewById(R.id.rl_speed_plus);
        this.sbSpeed = (X8SeekBarView) rootView.findViewById(R.id.sb_speed);
        this.sbSpeed.setMaxProgress(this.SPEED_MAX_PROGRESS);
        this.vDistanceMinus = rootView.findViewById(R.id.rl_distance_minus);
        this.vDistancePlus = rootView.findViewById(R.id.rl_distance_plus);
        this.sbDistance = (X8SeekBarView) rootView.findViewById(R.id.sb_distance);
        this.currentDroneHeight = StateManager.getInstance().getX8Drone().getHeight();
        if (this.mType == 1) {
            this.verticalMax = ((int) ((this.DISTANCE_VERTIIVAL_MAX - this.DISTANCE_MIN) - this.currentDroneHeight)) * 10;
            this.sbDistance.setMaxProgress(this.verticalMax);
        } else {
            double pitchAngle = this.angle / 100.0d;
            if (this.angle == 0) {
                this.distanceMax = this.DISTANCE_MAX_PROGRESS;
            } else {
                float diffDistance = this.DISTANCE_VERTIIVAL_MAX - this.currentDroneHeight;
                float hypotenuseDistanst = (float) (diffDistance / Math.sin((pitchAngle / 180.0d) * 3.141592653589793d));
                if (hypotenuseDistanst > this.DISTANCE_MAX) {
                    hypotenuseDistanst = this.DISTANCE_MAX;
                }
                this.distanceMax = (int) ((hypotenuseDistanst - this.DISTANCE_MIN) * 10.0f);
            }
            this.sbDistance.setMaxProgress(this.distanceMax);
        }
        this.swbAutoReturn = (SwitchButton) rootView.findViewById(R.id.swb_ai_auto_return);
        this.swbAutoReturn.setEnabled(true);
    }

    public void initActions() {
        this.imgReturn.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.swbAutoReturn.setOnSwitchListener(this);
        this.vSpeedMinus.setOnClickListener(this);
        this.vSpeedPlus.setOnClickListener(this);
        this.sbSpeed.setOnSlideChangeListener(this.speedListener);
        this.vDistanceMinus.setOnClickListener(this);
        this.vDistancePlus.setOnClickListener(this);
        this.sbDistance.setOnSlideChangeListener(this.distanceListener);
        int progress = (int) (this.SPEED_DEFALOUT * 10.0f);
        this.sbSpeed.setProgress(progress);
        int progress2 = (int) (this.DISTANCE_DEFALOUT * 10.0f);
        this.sbDistance.setProgress(progress2);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id != R.id.img_ai_return) {
            if (id == R.id.btn_ai_confirm_ok) {
                this.listener.onExcuteClick();
            } else if (id == R.id.rl_speed_minus) {
                if (this.sbSpeed.getProgress() != 0) {
                    int s = this.sbSpeed.getProgress() - 10;
                    if (s < 0) {
                        s = 0;
                    }
                    this.sbSpeed.setProgress(s);
                }
            } else if (id == R.id.rl_speed_plus) {
                if (this.sbSpeed.getProgress() != this.SPEED_MAX_PROGRESS) {
                    int s2 = this.sbSpeed.getProgress() + 10;
                    if (s2 > this.SPEED_MAX_PROGRESS) {
                        s2 = this.SPEED_MAX_PROGRESS;
                    }
                    this.sbSpeed.setProgress(s2);
                }
            } else if (id == R.id.rl_distance_minus) {
                if (this.sbDistance.getProgress() != 0) {
                    int s3 = this.sbDistance.getProgress() - 10;
                    if (s3 < 0) {
                        s3 = 0;
                    }
                    this.sbDistance.setProgress(s3);
                }
            } else if (id == R.id.rl_distance_plus) {
                int s4 = this.sbDistance.getProgress() + 10;
                if (this.mType == 1) {
                    if (s4 > this.verticalMax) {
                        s4 = this.verticalMax;
                    }
                    if (this.sbDistance.getProgress() != this.verticalMax) {
                        this.sbDistance.setProgress(s4);
                        return;
                    }
                    return;
                }
                if (s4 > this.distanceMax) {
                    s4 = this.distanceMax;
                }
                if (this.sbDistance.getProgress() != this.distanceMax) {
                    this.sbDistance.setProgress(s4);
                }
            }
        }
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8AiAutoPhototExcuteController mX8AiAutoPhototExcuteController) {
        this.listener = listener;
        this.fcManager = fcManager;
        this.x8AiAutoPhototExcuteController = mX8AiAutoPhototExcuteController;
    }

    public void setValue() {
        if (this.mType == 0) {
            this.item = this.mType;
            double pitchAngle = this.angle / 100.0d;
            String angleStr = "" + NumberUtil.decimalPointStr(pitchAngle, 1);
            String s = this.contentView.getResources().getString(R.string.x8_ai_auto_photo_tip4);
            this.tvContent.setText(String.format(s, "" + angleStr));
            float speed = this.SPEED_MIN + (this.sbSpeed.getProgress() / 10.0f);
            float distance = this.DISTANCE_MIN + (this.sbDistance.getProgress() / 10.0f);
            String time = "" + String.format("%.2f", Float.valueOf(distance / speed)) + "S";
            this.tvTime.setText(time);
            this.tvTitle.setText(this.contentView.getResources().getString(R.string.x8_ai_auto_photo_title));
            return;
        }
        this.item = this.mType;
        String s2 = this.contentView.getResources().getString(R.string.x8_ai_auto_photo_vertical_next_tip1);
        this.tvContent.setText(s2);
        float speed2 = this.SPEED_MIN + (this.sbSpeed.getProgress() / 10.0f);
        float distance2 = this.DISTANCE_MIN + (this.sbDistance.getProgress() / 10.0f);
        String time2 = "" + String.format("%.2f", Float.valueOf(distance2 / speed2)) + "S";
        this.tvTime.setText(time2);
        this.tvTitle.setText(this.contentView.getResources().getString(R.string.x8_ai_auto_photo_vertical_title));
    }

    @Override // com.fimi.widget.SwitchButton.OnSwitchListener
    public void onSwitch(View view, boolean on) {
        if (on) {
            this.swbAutoReturn.setSwitchState(false);
        } else {
            this.swbAutoReturn.setSwitchState(true);
        }
    }

    public void setAiAutoPhotoValueCmd(final IX8AiAutoPhototExcuteListener excuteListener) {
        CmdAiAutoPhoto cmd = new CmdAiAutoPhoto();
        cmd.speed = (int) ((this.SPEED_MIN * 10.0f) + this.sbSpeed.getProgress());
        cmd.config = this.swbAutoReturn.getToggleOn() ? 1 : 0;
        cmd.angle = this.angle;
        cmd.mode = this.item != 0 ? 0 : 1;
        cmd.routeLength = (int) ((this.DISTANCE_MIN * 10.0f) + this.sbDistance.getProgress());
        this.fcManager.setAiAutoPhotoValue(cmd, new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiAutoPhotoExcuteConfirmUi.3
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8AiAutoPhotoExcuteConfirmUi.this.startAiAutoPhoto(excuteListener);
                } else {
                    excuteListener.autoPhototExcute(false);
                }
            }
        });
    }

    public void startAiAutoPhoto(final IX8AiAutoPhototExcuteListener excuteListener) {
        this.fcManager.setAiAutoPhotoExcute(new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiAutoPhotoExcuteConfirmUi.4
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    excuteListener.autoPhototExcute(true);
                } else {
                    excuteListener.autoPhototExcute(false);
                }
            }
        });
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        if (isInSky && isLowPower) {
            this.btnOk.setEnabled(true);
        } else {
            this.btnOk.setEnabled(false);
        }
    }
}
