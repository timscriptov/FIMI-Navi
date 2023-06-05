package com.fimi.app.x8s.controls.aifly.confirm.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fimi.android.app.R;
import com.fimi.app.x8s.config.X8AiConfig;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.widget.X8SeekBarView;
import com.fimi.app.x8s.widget.X8TabHost;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckAiGetGravitationPrameter;
import com.fimi.x8sdk.dataparser.AckAiSetGravitationPrameter;
import com.fimi.x8sdk.modulestate.StateManager;

/* loaded from: classes.dex */
public class X8AiGravitationExcuteComfirmUi implements View.OnClickListener {
    private final View contentView;
    private X8sMainActivity activity;
    private AckAiGetGravitationPrameter mAckAiGetGravitationPrameter;
    private Button mBtnExcuteAdvancedSetting;
    private Button mBtnExcuteDefault;
    private Button mBtnExcuteOk;
    private Button mBtnExcuteSave;
    private CameraManager mCameraManager;
    private ImageView mExcuteReturn;
    private TextView mExcuteTitle;
    private FcManager mFcManager;
    private IX8NextViewListener mIX8NextViewListener;
    private LinearLayout mLlAdvancedSetting;
    private RelativeLayout mRlExcuteSetting;
    private RelativeLayout mRlHightMinus;
    private RelativeLayout mRlHightPlus;
    private RelativeLayout mRlLevelMinus;
    private RelativeLayout mRlLevelPlus;
    private X8SeekBarView mSbHeight;
    private X8SeekBarView mSbLevel;
    private TextView mTvHeight;
    private TextView mTvLevel;
    private X8TabHost mXthRotate;
    private X8TabHost mXthVidotape;
    private int LEVEL_MIN = 20;
    private int LEVEL_MAX = 30;
    private int LEVEL_MAX_PROGRESS = (this.LEVEL_MAX - this.LEVEL_MIN) * 10;
    private int HIGHT_MIN = 5;
    private int HIGHT_MAX = 10;
    private int HIGHT_MAX_PROGRESS = (this.HIGHT_MAX - this.HIGHT_MIN) * 20;
    private ExcuteComfirmState mComfirmState = ExcuteComfirmState.MAIN;
    private X8SeekBarView.SlideChangeListener seekBarLevelListener = new X8SeekBarView.SlideChangeListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi.6
        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onProgress(X8SeekBarView slideView, int progress) {
            float levelValue = X8AiGravitationExcuteComfirmUi.this.LEVEL_MIN + (progress / 10);
            String d = X8NumberUtil.getDistanceNumberString(levelValue, 0, true);
            X8AiGravitationExcuteComfirmUi.this.mTvLevel.setText(d);
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStop(X8SeekBarView slideView, int progress) {
            float levelValue = X8AiGravitationExcuteComfirmUi.this.LEVEL_MIN + (progress / 10);
            X8AiGravitationExcuteComfirmUi.this.changeEllipse(levelValue);
        }
    };
    private X8SeekBarView.SlideChangeListener seekBarHightListener = new X8SeekBarView.SlideChangeListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi.7
        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStart(X8SeekBarView slideView, int progress) {
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onProgress(X8SeekBarView slideView, int progress) {
            float levelValue = X8AiGravitationExcuteComfirmUi.this.HIGHT_MIN + (progress / 20);
            String d = X8NumberUtil.getDistanceNumberString(levelValue, 0, true);
            X8AiGravitationExcuteComfirmUi.this.mTvHeight.setText(d);
        }

        @Override // com.fimi.app.x8s.widget.X8SeekBarView.SlideChangeListener
        public void onStop(X8SeekBarView slideView, int progress) {
        }
    };

    public X8AiGravitationExcuteComfirmUi(X8sMainActivity activity, View parent, CameraManager cameraManager) {
        this.activity = activity;
        this.mCameraManager = cameraManager;
        this.contentView = activity.getLayoutInflater().inflate(R.layout.x8_ai_gravitation_excute_confirm_layout, (ViewGroup) parent, true);
        initViews(this.contentView);
        initActions();
    }

    private void initActions() {
        if (this.contentView != null) {
            this.mExcuteReturn.setOnClickListener(this);
            this.mBtnExcuteOk.setOnClickListener(this);
            this.mBtnExcuteAdvancedSetting.setOnClickListener(this);
            this.mRlLevelMinus.setOnClickListener(this);
            this.mRlLevelPlus.setOnClickListener(this);
            this.mSbLevel.setOnSlideChangeListener(this.seekBarLevelListener);
            this.mRlHightMinus.setOnClickListener(this);
            this.mRlHightPlus.setOnClickListener(this);
            this.mSbHeight.setOnSlideChangeListener(this.seekBarHightListener);
            this.mBtnExcuteDefault.setOnClickListener(this);
            this.mBtnExcuteSave.setOnClickListener(this);
        }
    }

    private void initViews(View contentView) {
        this.mExcuteReturn = (ImageView) contentView.findViewById(R.id.img_ai_gravitation_excute_return);
        this.mExcuteTitle = (TextView) contentView.findViewById(R.id.tv_ai_gravitation_excute_title);
        this.mRlExcuteSetting = (RelativeLayout) contentView.findViewById(R.id.rl_gravitation_excute_setting);
        this.mXthVidotape = (X8TabHost) contentView.findViewById(R.id.x8_ai_gravitation_excute_videotape);
        this.mXthVidotape.setSelect(0);
        this.mBtnExcuteAdvancedSetting = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_advanced_setting);
        this.mBtnExcuteOk = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_ok);
        this.mBtnExcuteDefault = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_default);
        this.mBtnExcuteSave = (Button) contentView.findViewById(R.id.btn_ai_gravitation_excute_save);
        this.mLlAdvancedSetting = (LinearLayout) contentView.findViewById(R.id.ll_gravitation_excute_advanced_setting);
        this.mTvLevel = (TextView) contentView.findViewById(R.id.tv_ai_gravitation_excute_level);
        this.mRlLevelMinus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_level_minus);
        this.mSbLevel = (X8SeekBarView) contentView.findViewById(R.id.sb_ai_gravitation_excute_level);
        this.mRlLevelPlus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_level_plus);
        this.mTvHeight = (TextView) contentView.findViewById(R.id.tv_ai_gravitation_excute_height);
        this.mRlHightMinus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_heigth_minus);
        this.mSbHeight = (X8SeekBarView) contentView.findViewById(R.id.sb_ai_gravitation_excute_heigth);
        this.mRlHightPlus = (RelativeLayout) contentView.findViewById(R.id.rl_ai_gravitation_excute_heigth_plus);
        this.mXthRotate = (X8TabHost) contentView.findViewById(R.id.x8_ai_gravitation_excute_rotate);
    }

    public void setFcHeart(boolean isInSky, boolean isLowPower) {
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.img_ai_gravitation_excute_return) {
            if (this.mComfirmState == ExcuteComfirmState.MAIN) {
                this.mIX8NextViewListener.onBackClick();
            } else {
                returnExcuteSetting();
            }
        } else if (id == R.id.btn_ai_gravitation_excute_ok) {
            setGravitationPrameter();
        } else if (id == R.id.btn_ai_gravitation_excute_advanced_setting) {
            showAdvancedSetting();
        } else if (id == R.id.btn_ai_gravitation_excute_default) {
            recoverDefaultValue();
        } else if (id == R.id.btn_ai_gravitation_excute_save) {
            saveValue();
        } else if (id == R.id.rl_ai_gravitation_excute_level_minus) {
            if (this.mSbLevel.getProgress() != 0) {
                int l = this.mSbLevel.getProgress() - 10;
                if (l < 0) {
                    l = 0;
                }
                this.mSbLevel.setProgress(l);
                this.mSbLevel.setProgress(l);
                float levelValue = this.LEVEL_MIN + (this.mSbLevel.getProgress() / 10);
                changeEllipse(levelValue);
            }
        } else if (id == R.id.rl_ai_gravitation_excute_level_plus) {
            if (this.mSbLevel.getProgress() != this.LEVEL_MAX_PROGRESS) {
                int l2 = this.mSbLevel.getProgress() + 10;
                if (l2 > this.LEVEL_MAX_PROGRESS) {
                    l2 = this.LEVEL_MAX_PROGRESS;
                }
                this.mSbLevel.setProgress(l2);
                float levelValue2 = this.LEVEL_MIN + (this.mSbLevel.getProgress() / 10);
                changeEllipse(levelValue2);
            }
        } else if (id == R.id.rl_ai_gravitation_excute_heigth_minus) {
            if (this.mSbHeight.getProgress() != 0) {
                int h = this.mSbHeight.getProgress() - 20;
                if (h < 0) {
                    h = 0;
                }
                this.mSbHeight.setProgress(h);
            }
        } else if (id == R.id.rl_ai_gravitation_excute_heigth_plus && this.mSbHeight.getProgress() != this.HIGHT_MAX_PROGRESS) {
            int h2 = this.mSbHeight.getProgress() + 20;
            if (h2 > this.HIGHT_MAX_PROGRESS) {
                h2 = this.HIGHT_MAX_PROGRESS;
            }
            this.mSbHeight.setProgress(h2);
        }
    }

    private void setGravitationPrameter() {
        AckAiSetGravitationPrameter prameter = new AckAiSetGravitationPrameter();
        prameter.setRotateDirecetion(this.mXthRotate.getSelectIndex());
        prameter.setRotateSpeed(1);
        int level = this.LEVEL_MIN + (X8AiConfig.getInstance().getAiFlyGravitationLevel() / 10);
        prameter.setHorizontalDistance(level);
        int height = this.HIGHT_MIN + (X8AiConfig.getInstance().getAiFlyGravitationHeight() / 20);
        prameter.setRiseHeight(height);
        prameter.setEllipseInclinal(10);
        prameter.setEccentricWheel(70);
        if (this.mXthVidotape.getSelectIndex() == 0) {
            prameter.setAutoVideo(1);
            this.mCameraManager.setCameraKeyParams("normal_record", CameraJsonCollection.KEY_RECORD_MODE, new JsonUiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi.1
                @Override // com.fimi.kernel.dataparser.usb.JsonUiCallBackListener
                public void onComplete(JSONObject rt, Object o) {
                }
            });
        } else if (this.mXthVidotape.getSelectIndex() == 1) {
            prameter.setAutoVideo(1);
            this.mCameraManager.setCameraKeyParams("5s", CameraJsonCollection.KEY_VIDEO_TIMELAPSE, new JsonUiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi.2
                @Override // com.fimi.kernel.dataparser.usb.JsonUiCallBackListener
                public void onComplete(JSONObject rt, Object o) {
                }
            });
        } else if (this.mXthVidotape.getSelectIndex() == 0) {
            prameter.setAutoVideo(0);
        }
        this.mFcManager.setGravitationPrameter(prameter, new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi.3
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess) {
                    X8AiGravitationExcuteComfirmUi.this.getParamer();
                    return;
                }
                String s = X8AiGravitationExcuteComfirmUi.this.contentView.getContext().getString(R.string.x8_ai_fly_gravitation_set_prameter_error);
                X8ToastUtil.showToast(X8AiGravitationExcuteComfirmUi.this.activity.getApplicationContext(), s, 0);
            }
        });
    }

    public void startGravitation() {
        this.mFcManager.setGravitationStart(new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi.4
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess) {
                    if (X8AiGravitationExcuteComfirmUi.this.mIX8NextViewListener != null) {
                        X8AiGravitationExcuteComfirmUi.this.mIX8NextViewListener.onExcuteClick();
                        X8AiGravitationExcuteComfirmUi.this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().addEllipse(StateManager.getInstance().getX8Drone().getLatitude(), StateManager.getInstance().getX8Drone().getLongitude(), X8AiGravitationExcuteComfirmUi.this.mAckAiGetGravitationPrameter.getHorizontalDistance(), StateManager.getInstance().getX8Drone().getDeviceAngle() + 90.0f);
                        return;
                    }
                    return;
                }
                String s = X8AiGravitationExcuteComfirmUi.this.contentView.getContext().getString(R.string.x8_ai_fly_gravitation_start_error);
                X8ToastUtil.showToast(X8AiGravitationExcuteComfirmUi.this.activity.getApplicationContext(), s, 0);
            }
        });
    }

    public void getParamer() {
        this.mFcManager.getGravitationPrameter(new UiCallBackListener() { // from class: com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiGravitationExcuteComfirmUi.5
            @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess) {
                    X8AiGravitationExcuteComfirmUi.this.mAckAiGetGravitationPrameter = (AckAiGetGravitationPrameter) o;
                    X8AiGravitationExcuteComfirmUi.this.startGravitation();
                }
            }
        });
    }

    private void saveValue() {
        returnExcuteSetting();
        X8AiConfig.getInstance().setAiFlyGravitationHeight(this.mSbHeight.getProgress());
        X8AiConfig.getInstance().setAiFlyGravitationLevel(this.mSbLevel.getProgress());
        X8AiConfig.getInstance().setAiFlyGravitationRotate(this.mXthRotate.getSelectIndex());
    }

    private void recoverDefaultValue() {
        this.mSbLevel.setProgress(0);
        this.mTvLevel.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_gravitation_excute_advanced_setting_level));
        this.mSbHeight.setProgress(0);
        this.mTvHeight.setText(this.contentView.getContext().getString(R.string.x8_ai_fly_gravitation_excute_advanced_setting_height));
        this.mXthRotate.setSelect(0);
        changeEllipse(this.LEVEL_MIN);
        X8AiConfig.getInstance().setAiFlyGravitationHeight(0);
        X8AiConfig.getInstance().setAiFlyGravitationLevel(0);
        X8AiConfig.getInstance().setAiFlyGravitationRotate(0);
    }

    private void returnExcuteSetting() {
        this.mComfirmState = ExcuteComfirmState.MAIN;
        this.mRlExcuteSetting.setVisibility(0);
        this.mBtnExcuteDefault.setVisibility(8);
        this.mBtnExcuteSave.setVisibility(8);
        this.mBtnExcuteOk.setVisibility(0);
        this.mLlAdvancedSetting.setVisibility(8);
    }

    private void showAdvancedSetting() {
        this.mComfirmState = ExcuteComfirmState.ADVANCED;
        this.mExcuteReturn.setVisibility(0);
        this.mRlExcuteSetting.setVisibility(8);
        this.mBtnExcuteDefault.setVisibility(0);
        this.mBtnExcuteSave.setVisibility(0);
        this.mBtnExcuteOk.setVisibility(8);
        this.mLlAdvancedSetting.setVisibility(0);
        this.mTvHeight.setText(X8NumberUtil.getDistanceNumberString(this.HIGHT_MIN + (X8AiConfig.getInstance().getAiFlyGravitationHeight() / 20), 0, true));
        this.mSbHeight.setProgress(X8AiConfig.getInstance().getAiFlyGravitationHeight());
        this.mTvLevel.setText(X8NumberUtil.getDistanceNumberString(this.LEVEL_MIN + (X8AiConfig.getInstance().getAiFlyGravitationLevel() / 10), 0, true));
        this.mSbLevel.setProgress(X8AiConfig.getInstance().getAiFlyGravitationLevel());
        this.mXthRotate.setSelect(X8AiConfig.getInstance().getAiFlyGravitationRotate());
    }

    public void changeEllipse(float levelValue) {
        this.activity.getmMapVideoController().getFimiMap().getAiSurroundManager().addEllipse(StateManager.getInstance().getX8Drone().getLatitude(), StateManager.getInstance().getX8Drone().getLongitude(), levelValue, StateManager.getInstance().getX8Drone().getDeviceAngle() + 90.0f);
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager) {
        this.mIX8NextViewListener = listener;
        this.mFcManager = fcManager;
    }

    /* loaded from: classes.dex */
    public enum ExcuteComfirmState {
        MAIN,
        ADVANCED
    }
}
