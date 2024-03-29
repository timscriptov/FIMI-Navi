package com.fimi.app.x8s.controls;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.camera.CameraParamStatus;
import com.fimi.app.x8s.entity.X8AiModeState;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8CameraPersonLacationListener;
import com.fimi.app.x8s.interfaces.IX8MainRightMenuListener;
import com.fimi.app.x8s.tools.TimeFormateUtil;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;
import com.fimi.app.x8s.ui.album.x8s.X8MediaActivity;
import com.fimi.app.x8s.widget.X8ModuleSwitcher;
import com.fimi.app.x8s.widget.X8ShutterImageView;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.AbAppUtil;
import com.fimi.widget.StrokeTextView;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.cmdsenum.PanoramaPhotographType;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.entity.X8CameraParamsValue;
import com.fimi.x8sdk.jsonResult.CurParamsJson;
import com.fimi.x8sdk.listener.IX8PanoramicInformationListener;
import com.fimi.x8sdk.modulestate.CameraState;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.rtp.X8Rtp;


public class X8MainRightMenuController extends AbsX8Controllers implements View.OnClickListener {
    private final X8sMainActivity activity;
    private final X8AiModeState mX8AiModeState;
    public IX8PanoramicInformationListener ix8PanoramicInformationListener;
    AutoCameraStateADV autoCameraStateADV;
    CameraManager cameraManager;
    CameraParamStatus.CameraModelStatus curMode;
    private CameraState cameraState;
    private Context context;
    private int curModeType;
    private FcCtrlManager fcCtrlManager;
    private ImageButton imbCameraTools;
    private ImageButton imbMedia;
    private X8ShutterImageView imbPhotoVideo;
    private X8ModuleSwitcher imbSwitchPhotoVideo;
    private ImageView mIvHotDot;
    private StrokeTextView mTvRecordTime;
    private IX8MainRightMenuListener mainRightMenuListener;
    private IX8CameraPersonLacationListener personLacationListener;
    private boolean pivTake;
    private boolean selfShow;
    private StrokeTextView tvPanoramaNumber;

    public X8MainRightMenuController(View rootView, X8sMainActivity activity, X8AiModeState mX8AiModeState) {
        super(rootView);
        this.pivTake = false;
        this.ix8PanoramicInformationListener = ackPanoramaPhotographType -> {
            HostLogBack.getInstance().writeLog("Alanqiu  ============reponseCmd:" + ackPanoramaPhotographType.toString());
            if (ackPanoramaPhotographType.getCurrentNum() >= 1) {
                if (ackPanoramaPhotographType.getCurrentNum() >= ackPanoramaPhotographType.getTotalNum()) {
                    tvPanoramaNumber.postDelayed(() -> {
                        tvPanoramaNumber.setVisibility(View.GONE);
                        StateManager.getInstance().getCamera().setTakingPanoramicPhotos(false);
                    }, 3000L);
                }
                if (!getPanoramicStart()) {
                    StateManager.getInstance().getCamera().setTakingPanoramicPhotos(true);
                    tvPanoramaNumber.setVisibility(View.VISIBLE);
                }
                tvPanoramaNumber.setText(String.format(getString(R.string.x8_panorama_number), ackPanoramaPhotographType.getCurrentNum(), ackPanoramaPhotographType.getTotalNum()));
            }
        };
        this.activity = activity;
        this.mX8AiModeState = mX8AiModeState;
    }

    @Override
    public void initViews(@NonNull View rootView) {
        this.context = rootView.getContext();
        this.handleView = rootView.findViewById(R.id.main_right_menu);
        this.imbCameraTools = rootView.findViewById(R.id.imb_camera_tools);
        this.imbSwitchPhotoVideo = rootView.findViewById(R.id.imb_switch_photo_video_module);
        this.imbPhotoVideo = rootView.findViewById(R.id.imb_photo_video);
        this.imbMedia = rootView.findViewById(R.id.imb_meida);
        this.mTvRecordTime = rootView.findViewById(R.id.tv_record_time);
        this.tvPanoramaNumber = rootView.findViewById(R.id.tv_panorama_number);
        this.mIvHotDot = rootView.findViewById(R.id.iv_record_hot_dot);
    }

    @Override
    public void initActions() {
        this.imbCameraTools.setOnClickListener(this);
        this.imbSwitchPhotoVideo.setOnClickListener(this);
        this.imbPhotoVideo.setOnClickListener(this);
        this.imbMedia.setOnClickListener(this);
        this.cameraState = StateManager.getInstance().getCamera();
    }

    @Override
    public void defaultVal() {
    }

    public void setFcCtrlManager(FcCtrlManager fcCtrlManager) {
        if (fcCtrlManager != null) {
            this.fcCtrlManager = fcCtrlManager;
        }
    }

    public void setCameraManager(CameraManager cameraManager) {
        if (cameraManager != null) {
            this.cameraManager = cameraManager;
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == this.imbCameraTools.getId()) {
            this.mainRightMenuListener.onCameraSettingClick();
        } else if (id == this.imbSwitchPhotoVideo.getId()) {
            this.imbPhotoVideo.setClickable(false);
            onPhotoVideoSwitcher();
        } else if (id == this.imbPhotoVideo.getId()) {
            if (!AbAppUtil.isFastClick(500)) {
                this.imbPhotoVideo.setClickable(false);
                onPhotoVideoShutter();
                this.mainRightMenuListener.onCameraShutterClick();
            }
        } else if (id == this.imbMedia.getId()) {
            if (this.pivTake) {
                if (!AbAppUtil.isFastClick(500)) {
                    this.imbPhotoVideo.setClickable(false);
                    takePhoto();
                    return;
                }
                return;
            }
            this.context.startActivity(new Intent(this.context, X8MediaActivity.class));
        }
    }

    public void showPersonLocation() {
        if (this.personLacationListener != null) {
            this.personLacationListener.showPersonLocation();
        }
    }

    public void onPhotoVideoSwitcher() {
        boolean isConnect = StateManager.getInstance().getCamera().isConnect();
        if (isConnect) {
            if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.takePhoto) {
                this.cameraManager.swithVideoMode((cmdResult, o) -> mainRightMenuListener.onCameraSwitching());
            } else {
                this.cameraManager.swithPhotoMode((cmdResult, o) -> mainRightMenuListener.onCameraSwitching());
            }
        } else if (this.imbSwitchPhotoVideo.getCurrentIndex() == 0) {
            this.imbSwitchPhotoVideo.setCurrentIndex(1);
            this.imbPhotoVideo.setCurrentIndex(1, 0);
        } else {
            this.imbSwitchPhotoVideo.setCurrentIndex(0);
            this.imbPhotoVideo.setCurrentIndex(0, 0);
        }
    }

    public void onPhotoVideoShutter() {
        if (this.autoCameraStateADV == null || this.autoCameraStateADV.getInfo() == 3) {
            X8ToastUtil.showToast(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_camera_rtp8), 1);
        } else if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.takePhoto) {
            takePhoto();
        } else if (this.autoCameraStateADV != null) {
            if (this.autoCameraStateADV.getState() == 2) {
                stopRecord();
            } else {
                startRecord();
            }
        }
    }

    private void takePhoto() {
        if (this.autoCameraStateADV.getMode() == 20) {
            if (StateManager.getInstance().getCamera().isTakingPanoramicPhotos()) {
                this.fcCtrlManager.setPanoramaPhotographState((cmdResult, o) -> {
                    if (cmdResult.isSuccess) {
                        tvPanoramaNumber.setVisibility(View.GONE);
                    }
                    StateManager.getInstance().getCamera().setTakingPanoramicPhotos(false);
                }, FcCollection.MSG_ID_SET_PANORAMA_PHOTOGRAPH_STOP);
                return;
            } else if (StateManager.getInstance().getX8Drone().isInSky()) {
                this.fcCtrlManager.setPanoramaPhotographType((cmdResult, o) -> {
                }, Constants.panoramaType + 1);
                return;
            } else {
                X8ToastUtil.showToast(this.context, this.rootView.getContext().getString(R.string.x8_take_photo_not_insky_tip), 1);
                return;
            }
        }
        this.cameraManager.takePhoto((cmdResult, o) -> {
            imbPhotoVideo.setClickable(true);
            if (!cmdResult.isSuccess()) {
                String rtpCamera = X8Rtp.getRtpStringCamera(rootView.getContext(), cmdResult.getmMsgRpt());
                if (!rtpCamera.equals("")) {
                    X8ToastUtil.showToast(rootView.getContext(), rtpCamera, 1);
                }
            }
        });
    }

    private void startRecord() {
        this.cameraManager.startVideo((cmdResult, o) -> {
            imbPhotoVideo.setClickable(true);
            if (!cmdResult.isSuccess()) {
                String rtpCamera = X8Rtp.getRtpStringCamera(rootView.getContext(), cmdResult.getmMsgRpt());
                if (!rtpCamera.equals("")) {
                    X8ToastUtil.showToast(rootView.getContext(), rtpCamera, 1);
                    return;
                }
                return;
            }
            checkCameraParam();
        });
    }

    private void stopRecord() {
        this.cameraManager.stopVideo((cmdResult, o) -> {
            if (cmdResult.isSuccess()) {
                imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
            }
        });
    }

    public void setListener(IX8MainRightMenuListener mainRightMenuListener) {
        this.mainRightMenuListener = mainRightMenuListener;
    }

    public void setAiFly(boolean visiable) {
    }

    public void setPersonLacationListener(IX8CameraPersonLacationListener personLacationListener) {
        this.personLacationListener = personLacationListener;
    }

    public void showCameraView(boolean isShow) {
        if (!this.mX8AiModeState.isAiModeStateReady()) {
            this.handleView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public void switchByCloseFullScreen(boolean isFullVideo) {
        this.handleView.setVisibility(isFullVideo ? View.VISIBLE : View.GONE);
    }

    public void showCameraState(AutoCameraStateADV cameraStateADV) {
        if (this.curMode != CameraParamStatus.modelStatus) {
            this.imbPhotoVideo.setClickable(true);
        }
        if (cameraStateADV != null) {
            this.autoCameraStateADV = cameraStateADV;
            int status = cameraStateADV.getState();
            if (status == 6) {
                X8ToastUtil.showToast(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_camera_error), 1);
                return;
            }
            if (status == 5) {
                X8ToastUtil.showToast(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_camera_take_success), 1);
            } else if (status == 9) {
                X8ToastUtil.showToast(this.context, getString(R.string.x8_camera_captured_successfully), 1);
                return;
            } else if (status == 16) {
                X8ToastUtil.showToast(this.context, getString(R.string.x8_camera_rtp10), 1);
                return;
            }
            int model = cameraStateADV.getMode();
            if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.takePhoto) {
                if (this.curMode != CameraParamStatus.modelStatus || (this.curModeType != model && !getPanoramicStart())) {
                    this.curMode = CameraParamStatus.modelStatus;
                    this.imbSwitchPhotoVideo.setCurrentIndex(0);
                    this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                    if (this.mainRightMenuListener != null) {
                        this.mainRightMenuListener.turnCameraModel();
                    }
                    if (model == 16) {
                        this.imbPhotoVideo.setCurrentIndex(0, 0);
                        this.curModeType = 16;
                    } else if (model == 19) {
                        this.imbPhotoVideo.setCurrentIndex(3, 0);
                        this.curModeType = 19;
                    } else if (model == 20) {
                        if (Constants.panoramaType == PanoramaPhotographType.PANORAMA_TYPE_LEVEL.ordinal()) {
                            this.imbPhotoVideo.setCurrentIndex(5, 0);
                        } else if (Constants.panoramaType == PanoramaPhotographType.PANORAMA_TYPE_RIGHT_ANGLE.ordinal()) {
                            this.imbPhotoVideo.setCurrentIndex(6, 0);
                        } else {
                            this.imbPhotoVideo.setCurrentIndex(7, 0);
                        }
                    } else {
                        this.imbPhotoVideo.setCurrentIndex(0, 0);
                        this.curModeType = 16;
                    }
                }
                if (this.autoCameraStateADV.isDelayedPhotography() || getPanoramicStart()) {
                    if (this.imbSwitchPhotoVideo.isClickable()) {
                        this.imbSwitchPhotoVideo.setCurrentIndex(2);
                        this.imbSwitchPhotoVideo.setClickable(false);
                        this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_unclickable);
                        this.imbMedia.setClickable(false);
                    }
                } else if (!this.imbSwitchPhotoVideo.isClickable()) {
                    this.imbSwitchPhotoVideo.setCurrentIndex(0);
                    this.imbSwitchPhotoVideo.setClickable(true);
                    this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                    this.imbMedia.setClickable(true);
                }
                this.pivTake = false;
            } else if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.record) {
                if (this.curMode != CameraParamStatus.modelStatus || this.curModeType != model) {
                    this.curMode = CameraParamStatus.modelStatus;
                    this.imbSwitchPhotoVideo.setCurrentIndex(1);
                    if (this.mainRightMenuListener != null) {
                        this.mainRightMenuListener.turnCameraModel();
                    }
                    if (model == 32) {
                        this.imbPhotoVideo.setCurrentIndex(1, 0);
                        this.curModeType = 32;
                    } else if (model == 33) {
                        this.imbPhotoVideo.setCurrentIndex(4, 0);
                        this.curModeType = 33;
                    } else {
                        this.imbPhotoVideo.setCurrentIndex(1, 0);
                        this.curModeType = 32;
                    }
                }
                this.pivTake = false;
            }
            if (CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.recording) {
                if (this.curMode != CameraParamStatus.modelStatus) {
                    this.curMode = CameraParamStatus.modelStatus;
                    this.imbSwitchPhotoVideo.setCurrentIndex(1);
                    this.mIvHotDot.setVisibility(View.VISIBLE);
                    this.mTvRecordTime.setVisibility(View.VISIBLE);
                    this.imbPhotoVideo.setCurrentIndex(1, 1);
                    if (this.mainRightMenuListener != null) {
                        this.mainRightMenuListener.turnCameraModel();
                    }
                    checkCameraParam();
                }
                this.mTvRecordTime.setText(TimeFormateUtil.getRecordTime(cameraStateADV.getRecHour(), cameraStateADV.getRecMinute(), cameraStateADV.getRecSecond()));
                if (this.imbSwitchPhotoVideo.isClickable()) {
                    this.imbSwitchPhotoVideo.setCurrentIndex(3);
                    this.imbSwitchPhotoVideo.setClickable(false);
                    if (!this.pivTake) {
                        this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_unclickable);
                        this.imbMedia.setClickable(false);
                    }
                }
            } else if (!this.autoCameraStateADV.isDelayedPhotography() && !getPanoramicStart()) {
                if (!this.imbSwitchPhotoVideo.isClickable() && this.mIvHotDot.getVisibility() == View.VISIBLE) {
                    this.imbSwitchPhotoVideo.setCurrentIndex(1);
                    this.imbSwitchPhotoVideo.setClickable(true);
                }
                this.mIvHotDot.setVisibility(View.GONE);
                this.mTvRecordTime.setVisibility(View.GONE);
                this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                this.pivTake = false;
                if (!this.pivTake) {
                    this.imbMedia.setClickable(true);
                }
            }
        }
    }

    @Override
    public void openUi() {
        this.selfShow = true;
        super.openUi();
        showCameraState(StateManager.getInstance().getCamera().getAutoCameraStateADV());
        showCameraView(this.activity.getmMapVideoController().isFullVideo());
    }

    @Override
    public void closeUi() {
        this.selfShow = false;
        this.curMode = null;
        super.closeUi();
    }

    public void setOtherShow(boolean show) {
        if (show) {
            this.mX8AiModeState.setAiModeState(X8AiModeState.AiModeState.READY);
        } else {
            this.mX8AiModeState.setAiModeState(X8AiModeState.AiModeState.IDLE);
        }
    }

    public void setOtherStateRunning() {
        this.mX8AiModeState.setAiModeState(X8AiModeState.AiModeState.RUNNING);
    }

    public void setBackGround(int color) {
        this.handleView.setBackgroundColor(color);
    }

    public void checkCameraParam() {
        CurParamsJson object;
        X8CameraParamsValue paramsValue = X8CameraParamsValue.getInstance();
        if (paramsValue != null && (object = paramsValue.getCurParamsJson()) != null) {
            String video_resolution = object.getVideo_resolution();
            String system_type = object.getSystem_type();
            if (video_resolution == null || "".equals(video_resolution) || system_type == null || "".equals(system_type)) {
                this.imbMedia.setBackgroundResource(R.drawable.x8_main_btn_media_select);
                this.pivTake = false;
            } else if (video_resolution.equals("1920x1080 50P 16:9") || video_resolution.equals("1920x1080 25P 16:9")) {
                if (system_type.equals("PAL")) {
                    this.pivTake = true;
                    this.imbMedia.setBackgroundResource(R.drawable.x8_piv_btn_selector);
                }
            } else if ((video_resolution.equals("1920x1080 30P 16:9") || video_resolution.equals("1920x1080 60P 16:9")) && system_type.equals("NTSC")) {
                this.pivTake = true;
                this.imbMedia.setBackgroundResource(R.drawable.x8_piv_btn_selector);
            }
        }
    }

    public boolean getPanoramicStart() {
        return StateManager.getInstance().getCamera().isTakingPanoramicPhotos();
    }

    @Override
    public void onDroneConnected(boolean b) {
        super.onDroneConnected(b);
        this.imbPhotoVideo.setClickable(b);
        if (this.autoCameraStateADV != null && !this.autoCameraStateADV.isDelayedPhotography() && !getPanoramicStart()) {
            this.imbSwitchPhotoVideo.setClickable(b);
        }
        if (!b && CameraParamStatus.modelStatus == CameraParamStatus.CameraModelStatus.recording) {
            this.imbPhotoVideo.setCurrentIndex(1, 0);
            this.curModeType = 32;
            this.mIvHotDot.setVisibility(View.GONE);
            this.mTvRecordTime.setVisibility(View.GONE);
            this.curMode = CameraParamStatus.CameraModelStatus.ideal;
        }
        if (!b && getPanoramicStart()) {
            this.tvPanoramaNumber.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    public int getHandleViewWidth() {
        return this.handleView.getWidth() + 20;
    }
}
