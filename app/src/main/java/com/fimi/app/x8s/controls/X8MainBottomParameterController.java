package com.fimi.app.x8s.controls;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.internal.view.SupportMenu;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.camera.CameraParamStatus;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.utils.NumberUtil;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CameraCurParamsJson;
import com.fimi.x8sdk.jsonResult.CurParamsJson;
import com.fimi.x8sdk.modulestate.GimbalState;
import com.fimi.x8sdk.modulestate.StateManager;

import java.util.List;

/* loaded from: classes.dex */
public class X8MainBottomParameterController extends AbsX8Controllers {
    public final String VALUE_VIDEO_RESOLUTION_1080P;
    public final String VALUE_VIDEO_RESOLUTION_2K;
    public final String VALUE_VIDEO_RESOLUTION_4K;
    public final String VALUE_VIDEO_RESOLUTION_720P;
    AutoCameraStateADV cameraStateADV;
    int changeShowStatus;
    Handler mHandler;
    PercentRelativeLayout root_layout;
    String tfCardCapt;
    private X8sMainActivity activity;
    private Context context;
    private ImageView mImgColor;
    private ImageView mIvCloud;
    private ImageView mIvEv;
    private ImageView mIvISO;
    private ImageView mIvRecord;
    private ImageView mIvSDK;
    private ImageView mIvShutter;
    private TextView mTvCloud;
    private TextView mTvColor;
    private TextView mTvEv;
    private TextView mTvISO;
    private TextView mTvRecord;
    private TextView mTvSDK;
    private TextView mTvShutter;
    private CurParamsJson paramsValue;

    public X8MainBottomParameterController(View rootView, X8sMainActivity activity) {
        super(rootView);
        this.tfCardCapt = "";
        this.changeShowStatus = 0;
        this.mHandler = new Handler() { // from class: com.fimi.app.x8s.controls.X8MainBottomParameterController.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (X8MainBottomParameterController.this.changeShowStatus == 0) {
                    X8MainBottomParameterController.this.changeShowStatus = 1;
                } else {
                    X8MainBottomParameterController.this.changeShowStatus = 0;
                }
                X8MainBottomParameterController.this.showTFCardStatus(X8MainBottomParameterController.this.changeShowStatus);
                X8MainBottomParameterController.this.mHandler.sendEmptyMessageDelayed(0, 2000L);
            }
        };
        this.VALUE_VIDEO_RESOLUTION_4K = "3840x2160";
        this.VALUE_VIDEO_RESOLUTION_2K = "2560x1440";
        this.VALUE_VIDEO_RESOLUTION_1080P = "1920x1080";
        this.VALUE_VIDEO_RESOLUTION_720P = "1280x720";
        this.paramsValue = X8CameraParamsValue.getInstance().getCurParamsJson();
        this.activity = activity;
    }

    public void showTFCardStatus(int changeShowStatus) {
        int imgRes;
        int textColor;
        String text;
        if (this.cameraStateADV != null && StateManager.getInstance().getCamera() != null && StateManager.getInstance().getCamera().isConnect()) {
            switch (this.cameraStateADV.getInfo()) {
                case 0:
                    imgRes = R.drawable.x8_tf_card_nomal;
                    textColor = -1;
                    text = this.tfCardCapt;
                    break;
                case 1:
                    imgRes = R.drawable.x8_tf_card_low_fulling;
                    textColor = -1;
                    if (changeShowStatus == 0) {
                        text = getString(R.string.x8_tf_low);
                        break;
                    } else {
                        text = this.tfCardCapt;
                        break;
                    }
                case 2:
                    imgRes = R.drawable.x8_tf_card_exception;
                    textColor = SupportMenu.CATEGORY_MASK;
                    if (changeShowStatus == 0 || this.tfCardCapt.equalsIgnoreCase(getString(R.string.x8_na))) {
                        text = getString(R.string.x8_tf_exception);
                    } else {
                        text = this.tfCardCapt;
                    }
                    break;
                case 3:
                    imgRes = R.drawable.x8_tf_card_no;
                    textColor = SupportMenu.CATEGORY_MASK;
                    text = getString(R.string.x8_tf_no_exit);
                    break;
                case 4:
                    imgRes = R.drawable.x8_tf_card_low_fulling;
                    textColor = SupportMenu.CATEGORY_MASK;
                    text = this.tfCardCapt;
                    break;
                case 5:
                    imgRes = R.drawable.x8_tf_fulled;
                    textColor = SupportMenu.CATEGORY_MASK;
                    text = this.tfCardCapt;
                    break;
                case 6:
                    imgRes = R.drawable.x8_tf_card_exception;
                    textColor = SupportMenu.CATEGORY_MASK;
                    if (changeShowStatus == 0 || this.tfCardCapt.equalsIgnoreCase(getString(R.string.x8_na))) {
                        text = getString(R.string.x8_tf_exception);
                    } else {
                        text = this.tfCardCapt;
                    }
                    break;
                default:
                    imgRes = R.drawable.x8_tf_card_nomal;
                    textColor = -1;
                    text = this.tfCardCapt;
                    break;
            }
            setTFCardStatus(imgRes, textColor, text);
        }
    }

    private void setTFCardStatus(int imgRes, int textColor, String text) {
        this.mIvSDK.setBackgroundResource(imgRes);
        this.mTvSDK.setTextColor(textColor);
        this.mTvSDK.setText(text);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initActions() {
    }

    public void showSportState(GimbalState state) {
        int pitchAngle = state.getPitchAngle();
        double angle = pitchAngle / 100.0d;
        String angleStr = NumberUtil.decimalPointStr(angle, 1) + "°";
        this.mTvCloud.setText(angleStr);
        this.mIvCloud.setBackgroundResource(R.drawable.x8_main_cloud_angle);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void defaultVal() {
        this.mIvCloud.setBackgroundResource(R.drawable.x8_main_cloud_angle_unconnect);
        this.mIvEv.setBackgroundResource(R.drawable.x8_main_ev_unconnect);
        this.mIvISO.setBackgroundResource(R.drawable.x8_main_iso_unconnect);
        this.mIvShutter.setBackgroundResource(R.drawable.x8_main_shutter_unconnect);
        this.mIvSDK.setBackgroundResource(R.drawable.x8_main_sdk_unconnect);
        this.mImgColor.setBackgroundResource(R.drawable.x8_main_bottom_camera_color_unconnect);
        this.mIvRecord.setSelected(false);
        this.mTvCloud.setText(R.string.x8_na);
        this.mTvEv.setText(R.string.x8_na);
        this.mTvISO.setText(R.string.x8_na);
        this.mTvShutter.setText(R.string.x8_na);
        this.mTvColor.setText(R.string.x8_na);
        this.mTvSDK.setTextColor(-1);
        this.mTvSDK.setText(R.string.x8_na);
        this.mTvRecord.setText(R.string.x8_na);
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void initViews(View rootView) {
        this.context = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.main_bottom_parameter);
        this.root_layout = (PercentRelativeLayout) this.handleView;
        this.mIvCloud = (ImageView) rootView.findViewById(R.id.iv_bottom_cloud);
        this.mTvCloud = (TextView) rootView.findViewById(R.id.tv_bottom_cloud);
        this.mIvEv = (ImageView) rootView.findViewById(R.id.iv_bottom_ev);
        this.mTvEv = (TextView) rootView.findViewById(R.id.tv_bottom_ev);
        this.mIvISO = (ImageView) rootView.findViewById(R.id.iv_bottom_iso);
        this.mTvISO = (TextView) rootView.findViewById(R.id.tv_bottom_iso);
        this.mIvShutter = (ImageView) rootView.findViewById(R.id.iv_bottom_shutter);
        this.mTvShutter = (TextView) rootView.findViewById(R.id.tv_bottom_shutter);
        this.mImgColor = (ImageView) rootView.findViewById(R.id.iv_bottom_color);
        this.mTvColor = (TextView) rootView.findViewById(R.id.tv_bottom_color);
        this.mIvSDK = (ImageView) rootView.findViewById(R.id.iv_bottom_sdk);
        this.mTvSDK = (TextView) rootView.findViewById(R.id.tv_bottom_sdk);
        this.mIvRecord = (ImageView) rootView.findViewById(R.id.iv_bottom_record);
        this.mTvRecord = (TextView) rootView.findViewById(R.id.tv_bottom_record);
    }

    public void showCameraStatus(AutoCameraStateADV cameraStateADV) {
        if (cameraStateADV != null) {
            if (cameraStateADV.isCardInfo()) {
                updateTFCardStorage(cameraStateADV);
            } else {
                this.cameraStateADV = cameraStateADV;
            }
            if (this.mHandler != null && !this.mHandler.hasMessages(0)) {
                this.mHandler.sendEmptyMessageDelayed(0, 100L);
            }
        }
    }

    private void updateTFCardStorage(AutoCameraStateADV cameraStateADV) {
        String freespace = NumberUtil.decimalPointStr((cameraStateADV.getFreeSpace() / 1024.0d) / 1024.0d, 1);
        String totalSpace = NumberUtil.decimalPointStr((cameraStateADV.getTotalSpace() / 1024.0d) / 1024.0d, 1);
        if (totalSpace.equals("0.0")) {
            this.tfCardCapt = getString(R.string.x8_na);
        } else {
            this.tfCardCapt = freespace + "/" + totalSpace + "G";
        }
    }

    public void initCameraParam(CameraCurParamsJson paramsJson) {
        if (paramsJson != null) {
            List<CurParamsJson> plist = paramsJson.getParam();
            if (plist != null && plist.size() > 0) {
                for (CurParamsJson curParams : plist) {
                    if (curParams.getAe_bias() != null) {
                        updateEvTextValue(curParams.getAe_bias());
                    } else if (curParams.getShutter_time() != null) {
                        updateShutter(curParams.getShutter_time());
                    } else if (curParams.getIso() != null) {
                        updateISOTextValue(curParams.getIso());
                    } else {
                        if (curParams.getDigital_effect() != null) {
                            String color = curParams.getDigital_effect();
                            setColor(color);
                        }
                        if (curParams.getVideo_resolution() != null) {
                            this.paramsValue.setVideo_resolution(curParams.getVideo_resolution());
                            StateManager.getInstance().setIs4KResolution(curParams.getVideo_resolution());
                        }
                        if (curParams.getPhoto_size() != null) {
                            this.paramsValue.setPhoto_size(curParams.getPhoto_size());
                        }
                    }
                }
            }
            updateCameraModelValue();
        }
    }

    private void setColor(String paramValue) {
        if (paramValue.equals("General")) {
            String paramValue2 = this.context.getResources().getString(R.string.x8_colours_general);
            this.mImgColor.setBackgroundResource(R.drawable.x8_main_bottom_camera_color);
            this.mTvColor.setText(paramValue2);
            return;
        }
        if (paramValue.equals("Vivid")) {
            paramValue = this.context.getResources().getString(R.string.x8_colours_vivid);
        } else if (paramValue.equals("art")) {
            paramValue = this.context.getResources().getString(R.string.x8_colours_art);
        } else if (paramValue.equals("black/white")) {
            paramValue = this.context.getResources().getString(R.string.x8_colours_black_white);
        } else if (paramValue.equals("film")) {
            paramValue = this.context.getResources().getString(R.string.x8_colours_film);
        } else if (paramValue.equals("sepia")) {
            paramValue = this.context.getResources().getString(R.string.x8_colours_sepia);
        } else if (paramValue.equals("F-LOG")) {
            paramValue = this.context.getResources().getString(R.string.x8_colours_flog);
        } else if (paramValue.equals("punk")) {
            paramValue = this.context.getResources().getString(R.string.x8_colours_punk);
        }
        this.mImgColor.setBackgroundResource(R.drawable.x8_main_bottom_camera_color_not_default);
        this.mTvColor.setText(paramValue);
    }

    public void updateEvTextValue(String ev) {
        this.mTvEv.setText(replaceEv(ev));
        float evValue = Float.parseFloat(replaceEv(ev));
        if (evValue == 0.0f) {
            this.mIvEv.setBackgroundResource(R.drawable.x8_main_ev_connect);
        } else {
            this.mIvEv.setBackgroundResource(R.drawable.x8_main_ev_connect_not_default);
        }
    }

    private String replaceEv(String ev) {
        if (ev.contains("EV")) {
            return ev.replace("EV", "");
        }
        return ev;
    }

    public void updateISOTextValue(String iso) {
        if (iso != null) {
            if (!iso.isEmpty() || !"".equals(iso)) {
                this.mTvISO.setText(iso);
                if (iso.equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
                    this.mIvISO.setBackgroundResource(R.drawable.x8_main_iso_connect);
                } else {
                    this.mIvISO.setBackgroundResource(R.drawable.x8_main_iso_connect_not_defualt);
                }
            }
        }
    }

    public void updateColoreTextValue(String text) {
        setColor(text);
    }

    public void updateShutter(String shutter) {
        if (shutter.equalsIgnoreCase(CameraJsonCollection.KEY_DE_CONTROL_AUTO)) {
            this.mTvShutter.setText(shutter);
            this.mIvShutter.setBackgroundResource(R.drawable.x8_main_shutter_connect);
            return;
        }
        this.mTvShutter.setText(shutter.substring(0, shutter.length() - 1));
        this.mIvShutter.setBackgroundResource(R.drawable.x8_main_shutter_connect_not_default);
    }

    public void updateCameraModelValue() {
        String modelValue;
        if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.record || CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.recording) {
            modelValue = X8CameraParamsValue.getInstance().getCurParamsJson().getVideo_resolution();
            if (modelValue != null && !"".equals(modelValue)) {
                String[] values = modelValue.split("\\s+");
                values[1] = values[1].replace("P", "FPS");
                if (values[0].equals("3840x2160")) {
                    modelValue = "4K/" + values[1];
                } else if (values[0].equals("2560x1440")) {
                    modelValue = "2.5K/" + values[1];
                } else if (values[0].equals("1920x1080")) {
                    modelValue = "1080P/" + values[1];
                } else if (values[0].equals("1280x720")) {
                    modelValue = "720P/" + values[1];
                }
                this.mIvRecord.setBackgroundResource(R.drawable.x8_bottom_record_btn_select);
            } else {
                return;
            }
        } else {
            String modelValue2 = X8CameraParamsValue.getInstance().getCurParamsJson().getPhoto_size();
            if (modelValue2 != null && !"".equals(modelValue2)) {
                modelValue = modelValue2.split("\\s+")[2].split("\\u0029")[0];
                this.mIvRecord.setBackgroundResource(R.drawable.x8_bottom_photo_btn_select);
            } else {
                return;
            }
        }
        this.mIvRecord.setSelected(true);
        this.mTvRecord.setText(modelValue);
    }

    @Override // com.fimi.app.x8s.interfaces.AbsX8Controllers
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        updateViewEnable(b, this.root_layout);
    }

    private void updateViewEnable(boolean enable, ViewGroup... parent) {
        if (parent != null && parent.length > 0) {
            for (ViewGroup group : parent) {
                int len = group.getChildCount();
                for (int j = 0; j < len; j++) {
                    View subView = group.getChildAt(j);
                    if (subView instanceof ViewGroup) {
                        updateViewEnable(enable, (ViewGroup) subView);
                    } else {
                        subView.setEnabled(enable);
                        if (subView instanceof TextView) {
                            subView.setAlpha(enable ? 1.0f : 0.9f);
                        }
                    }
                }
            }
        }
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public boolean onClickBackKey() {
        return false;
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        if (this.activity.getmMapVideoController().isFullVideo()) {
            super.openUi();
        }
    }

    public void openUiByMapChange() {
        if (!this.activity.getmMapVideoController().isFullVideo() && this.activity.getTaskManger().isTaskCanChangeBottom()) {
            super.openUi();
        }
    }

    public String getEvText() {
        return this.mTvEv.getText().toString();
    }

    public String getISOText() {
        return this.mTvISO.getText().toString();
    }
}
