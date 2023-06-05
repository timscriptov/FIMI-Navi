package com.fimi.app.x8s.controls.fcsettting;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.interfaces.AbsX8Controllers;
import com.fimi.app.x8s.interfaces.IX8FcItemListener;
import com.fimi.app.x8s.interfaces.IX8MainCoverListener;
import com.fimi.app.x8s.interfaces.IX8ValueSeakBarViewListener;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8SingleCustomDialog;
import com.fimi.app.x8s.widget.X8TabHost;
import com.fimi.app.x8s.widget.X8ValueSeakBarView;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.widget.SwitchButton;
import com.fimi.widget.X8ToastUtil;
import com.fimi.x8sdk.cmdsenum.X8Task;
import com.fimi.x8sdk.common.GlobalConfig;
import com.fimi.x8sdk.controller.FcCtrlManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckAccurateLanding;
import com.fimi.x8sdk.dataparser.AckAiFollowGetEnableBack;
import com.fimi.x8sdk.dataparser.AckGetFcParam;
import com.fimi.x8sdk.dataparser.AckGetLostAction;
import com.fimi.x8sdk.dataparser.AckGetPilotMode;
import com.fimi.x8sdk.dataparser.AckGetRetHeight;
import com.fimi.x8sdk.dataparser.AckGetSportMode;
import com.fimi.x8sdk.dataparser.AutoFcSignalState;
import com.fimi.x8sdk.dataparser.cmd.AckGetAutoHome;
import com.fimi.x8sdk.entity.X8AppSettingLog;
import com.fimi.x8sdk.modulestate.StateManager;


public class X8FcItemController extends AbsX8Controllers implements View.OnClickListener {
    public final int RC_LOST_ACTION_HOVER;
    public final int RC_LOST_ACTION_LAND;
    public final int RC_LOST_ACTION_RETURN;
    IX8MainCoverListener coverListener;
    X8DoubleCustomDialog dialog;
    FcManager fcManager;
    X8ValueSeakBarView.OnProgressConfirmListener flyHeightConfirmListener;
    X8ValueSeakBarView.OnProgressConfirmListener heightConfirmListener;
    X8ValueSeakBarView.OnProgressConfirmListener limitConfirmListener;
    X8MapVideoController mapVideoController;
    X8ValueSeakBarView.OnProgressConfirmListener speedConfirmListener;
    private Button btnCompassCalibration;
    private FcCtrlManager fcCtrlManager;
    private ImageView fcExpSetting;
    private ImageView fcSensitivitySetting;
    private ImageButton iBtnReturnDrone;
    private ImageButton iBtnReturnPerson;
    private ImageView imgMagneticField;
    private boolean isRequest;
    private X8DoubleCustomDialog limitDialog;
    private IX8FcItemListener listener;
    private LinearLayout llFeelingSetting;
    private Context mConext;
    private int reGetLostActionTime;
    private View rlFcItem;
    private ViewStub stubFcItem;
    private SwitchButton swbAccurateLanding;
    private SwitchButton swbAutoSetHome;
    private SwitchButton swbFollowReturn;
    private SwitchButton swbNoviceMode;
    private SwitchButton swbSportMode;
    private X8TabHost thDisconnectMeasure;
    private TextView tvAutoSetHome;
    private X8ValueSeakBarView vsbDistanceLimit;
    private X8ValueSeakBarView vsbFlyHeightLimit;
    private X8ValueSeakBarView vsbLightLimit;
    private X8ValueSeakBarView vsbRTHeightLimit;
    private X8ValueSeakBarView vsbSpeedLimit;
    private X8DoubleCustomDialog x8DoubleCustomDialog;
    private Button x8FcResetParams;

    public X8FcItemController(View rootView) {
        super(rootView);
        this.RC_LOST_ACTION_RETURN = 0;
        this.RC_LOST_ACTION_HOVER = 1;
        this.RC_LOST_ACTION_LAND = 2;
        this.speedConfirmListener = new X8ValueSeakBarView.OnProgressConfirmListener() {
            @Override
            public void onConfirm(final float value) {
                X8FcItemController.this.fcCtrlManager.setGpsSpeed(new UiCallBackListener() {
                    @Override
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            X8FcItemController.this.vsbSpeedLimit.setImbConfirmEnable(false);
                            StateManager.getInstance().getX8Drone().setGpsSpeed(value);
                        }
                    }
                }, value);
            }
        };
        this.limitConfirmListener = new X8ValueSeakBarView.OnProgressConfirmListener() {
            @Override
            public void onConfirm(float value) {
                X8FcItemController.this.setFlyDistance(value);
            }
        };
        this.heightConfirmListener = new X8ValueSeakBarView.OnProgressConfirmListener() {
            @Override
            public void onConfirm(final float value) {
                X8FcItemController.this.fcCtrlManager.setReturnHome(new UiCallBackListener() {
                    @Override
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getX8Drone().setReturnHomeHight(value);
                            X8FcItemController.this.vsbRTHeightLimit.setImbConfirmEnable(false);
                        }
                    }
                }, value);
            }
        };
        this.flyHeightConfirmListener = new X8ValueSeakBarView.OnProgressConfirmListener() {
            @Override
            public void onConfirm(float value) {
                if (value > 120.0f) {
                    X8FcItemController.this.showFlyHeightDialog(value);
                } else {
                    X8FcItemController.this.sendFlyHeight(value);
                }
            }
        };
        this.dialog = null;
    }

    static /* synthetic */ int access$3108(X8FcItemController x0) {
        int i = x0.reGetLostActionTime;
        x0.reGetLostActionTime = i + 1;
        return i;
    }

    public void setFlyDistance(final float distance) {
        this.fcCtrlManager.setFlyDistanceParam(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    StateManager.getInstance().getX8Drone().setFlyDistance(distance);
                    X8FcItemController.this.vsbDistanceLimit.setProgress(distance, true);
                    X8FcItemController.this.vsbDistanceLimit.setImbConfirmEnable(false);
                }
            }
        }, distance);
    }

    public void setFcCtrlManager(FcCtrlManager mFcCtrlManager) {
        this.fcCtrlManager = mFcCtrlManager;
    }

    @Override
    public void initViews(View rootView) {
        this.stubFcItem = rootView.findViewById(R.id.stub_fc_item);
        this.mConext = rootView.getContext();
    }

    @Override
    public void initActions() {
        if (this.rlFcItem != null) {
            this.thDisconnectMeasure.setOnSelectListener(new X8TabHost.OnSelectListener() {
                @Override
                public void onSelect(int index, String text, int last) {
                    int value;
                    X8FcItemController.this.thDisconnectMeasure.setSelect(last);
                    if (index == 0) {
                        value = 1;
                    } else if (index == 1) {
                        value = 2;
                    } else {
                        value = 0;
                    }
                    X8FcItemController.this.showFailsafeDialog(last, value, index);
                }
            });
            this.btnCompassCalibration.setOnClickListener(this);
            this.vsbSpeedLimit.setListener(new IX8ValueSeakBarViewListener() {
                @Override
                public void onSelect(boolean b) {
                    X8FcItemController.this.vsbRTHeightLimit.onOtherSelect();
                    X8FcItemController.this.vsbLightLimit.onOtherSelect();
                    X8FcItemController.this.vsbFlyHeightLimit.onOtherSelect();
                    if (X8FcItemController.this.vsbDistanceLimit.isEnableClick()) {
                        X8FcItemController.this.vsbDistanceLimit.onOtherSelect();
                    }
                }
            });
            this.vsbRTHeightLimit.setListener(new IX8ValueSeakBarViewListener() {
                @Override
                public void onSelect(boolean b) {
                    X8FcItemController.this.vsbSpeedLimit.onOtherSelect();
                    X8FcItemController.this.vsbLightLimit.onOtherSelect();
                    X8FcItemController.this.vsbFlyHeightLimit.onOtherSelect();
                    if (X8FcItemController.this.vsbDistanceLimit.isEnableClick()) {
                        X8FcItemController.this.vsbDistanceLimit.onOtherSelect();
                    }
                }
            });
            this.vsbLightLimit.setListener(new IX8ValueSeakBarViewListener() {
                @Override
                public void onSelect(boolean b) {
                    X8FcItemController.this.vsbRTHeightLimit.onOtherSelect();
                    X8FcItemController.this.vsbSpeedLimit.onOtherSelect();
                    X8FcItemController.this.vsbFlyHeightLimit.onOtherSelect();
                    if (X8FcItemController.this.vsbDistanceLimit.isEnableClick()) {
                        X8FcItemController.this.vsbDistanceLimit.onOtherSelect();
                    }
                }
            });
            this.vsbFlyHeightLimit.setListener(new IX8ValueSeakBarViewListener() {
                @Override
                public void onSelect(boolean b) {
                    X8FcItemController.this.vsbRTHeightLimit.onOtherSelect();
                    X8FcItemController.this.vsbSpeedLimit.onOtherSelect();
                    X8FcItemController.this.vsbLightLimit.onOtherSelect();
                    if (X8FcItemController.this.vsbDistanceLimit.isEnableClick()) {
                        X8FcItemController.this.vsbDistanceLimit.onOtherSelect();
                    }
                }
            });
            this.vsbDistanceLimit.setListener(new IX8ValueSeakBarViewListener() {
                @Override
                public void onSelect(boolean b) {
                    X8FcItemController.this.vsbRTHeightLimit.onOtherSelect();
                    X8FcItemController.this.vsbSpeedLimit.onOtherSelect();
                    X8FcItemController.this.vsbLightLimit.onOtherSelect();
                    X8FcItemController.this.vsbFlyHeightLimit.onOtherSelect();
                }
            });
            this.swbAccurateLanding.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
                @Override
                public void onSwitch(View view, boolean on) {
                    X8FcItemController.this.sendAccurateLandingCmd(on);
                }
            });
            this.swbNoviceMode.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
                @Override
                public void onSwitch(View view, boolean on) {
                    X8FcItemController.this.getDroneState();
                    if (X8FcItemController.this.isInSky) {
                        X8ToastUtil.showToast(X8FcItemController.this.mConext, X8FcItemController.this.mConext.getString(R.string.x8_fc_item_novice_mode_disable_message), 1);
                    } else if (on) {
                        X8FcItemController.this.setNoviceMode((byte) 2, false);
                    } else {
                        X8FcItemController.this.setNoviceMode((byte) 0, false);
                        X8FcItemController.this.setSportMode(0, true);
                    }
                }
            });
            this.iBtnReturnDrone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    X8FcItemController.this.showChangeHomeDialog(0);
                }
            });
            this.iBtnReturnPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    X8FcItemController.this.showChangeHomeDialog(1);
                }
            });
            this.swbSportMode.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
                @Override
                public void onSwitch(View view, boolean on) {
                    if (X8FcItemController.this.isInSky && X8FcItemController.this.swbNoviceMode.getToggleOn()) {
                        X8ToastUtil.showToast(X8FcItemController.this.mConext, X8FcItemController.this.mConext.getString(R.string.x8_fc_item_novice_mode_disable_message), 1);
                    } else if (on) {
                        X8FcItemController.this.setSportMode(0, false);
                    } else {
                        X8FcItemController.this.showModeSeniorDialog();
                    }
                }
            });
            this.tvAutoSetHome.setOnClickListener(this);
            this.swbAutoSetHome.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
                @Override
                public void onSwitch(View view, boolean on) {
                    if (on) {
                        X8FcItemController.this.fcCtrlManager.setAutoHomePoint(0, new UiCallBackListener() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (cmdResult.isSuccess()) {
                                    X8FcItemController.this.swbAutoSetHome.onSwitch(false);
                                    X8AppSettingLog.noChangeFollowRP(false);
                                }
                            }
                        });
                    } else {
                        X8FcItemController.this.fcCtrlManager.setAutoHomePoint(1, new UiCallBackListener() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (cmdResult.isSuccess()) {
                                    X8FcItemController.this.swbAutoSetHome.onSwitch(true);
                                    X8AppSettingLog.noChangeFollowRP(true);
                                }
                            }
                        });
                    }
                }
            });
            this.swbFollowReturn.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
                @Override
                public void onSwitch(View view, boolean on) {
                    if (on) {
                        X8FcItemController.this.fcCtrlManager.setAiFollowEnableBack(0, new UiCallBackListener() {
                            @Override
                            public void onComplete(CmdResult cmdResult, Object o) {
                                if (cmdResult.isSuccess()) {
                                    StateManager.getInstance().getX8Drone().setFollowReturn(0);
                                    X8FcItemController.this.swbFollowReturn.onSwitch(false);
                                }
                            }
                        });
                    } else {
                        X8FcItemController.this.showFollowDialog(on);
                    }
                }
            });
            this.vsbDistanceLimit.setOnSwitchListener(new SwitchButton.OnSwitchListener() {
                @Override
                public void onSwitch(View view, boolean on) {
                    if (!on) {
                        X8FcItemController.this.openSetFlyDistance(1000.0f);
                    } else {
                        X8FcItemController.this.showDistanceDialog();
                    }
                }
            });
        }
    }

    public void setSportMode(final int mode, final boolean isPilotModePrimary) {
        this.fcCtrlManager.setSportMode(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    if (mode == 0) {
                        X8FcItemController.this.swbSportMode.onSwitch(false);
                        StateManager.getInstance().getX8Drone().setSportMode(false);
                        X8FcItemController.this.changeMotorPattern(false, 0, isPilotModePrimary);
                        if (!isPilotModePrimary) {
                            X8FcItemController.this.resetSpeedLimit();
                            return;
                        }
                        return;
                    }
                    X8FcItemController.this.swbSportMode.onSwitch(true);
                    StateManager.getInstance().getX8Drone().setSportMode(true);
                    X8FcItemController.this.swbNoviceMode.onSwitch(false);
                    X8FcItemController.this.changeMotorPattern(true, 0, isPilotModePrimary);
                    X8FcItemController.this.resetViewBySport();
                }
            }
        }, mode);
    }

    public void handNewMode(int type) {
        switch (type) {
            case 0:
                GlobalConfig.getInstance().setNewHand(true);
                this.vsbSpeedLimit.setEnabled(false);
                this.vsbSpeedLimit.setEnableClick(false);
                this.vsbSpeedLimit.onOtherSelect();
                this.vsbSpeedLimit.setImgMenuVisiable(8);
                this.vsbRTHeightLimit.setEnabled(false);
                this.vsbRTHeightLimit.setEnableClick(false);
                this.vsbRTHeightLimit.onOtherSelect();
                this.vsbRTHeightLimit.setImgMenuVisiable(8);
                this.vsbFlyHeightLimit.setEnabled(false);
                this.vsbFlyHeightLimit.setEnableClick(false);
                this.vsbFlyHeightLimit.onOtherSelect();
                this.vsbFlyHeightLimit.setImgMenuVisiable(8);
                viewEnabled(this.llFeelingSetting, false);
                this.vsbSpeedLimit.setProgress(6);
                X8ValueSeakBarView x8ValueSeakBarView = this.vsbRTHeightLimit;
                StateManager.getInstance().getX8Drone().getClass();
                x8ValueSeakBarView.setProgress(30);
                this.vsbFlyHeightLimit.setProgress(50);
                this.vsbDistanceLimit.setProgress(100);
                this.vsbDistanceLimit.setEnabled(false);
                this.vsbDistanceLimit.setEnableClick(false);
                this.vsbDistanceLimit.setSwitchButtonByNew();
                this.swbNoviceMode.setSwitchState(true);
                return;
            case 1:
            default:
                return;
            case 2:
                GlobalConfig.getInstance().setNewHand(false);
                this.vsbSpeedLimit.setEnabled(true);
                this.vsbSpeedLimit.setEnableClick(true);
                this.vsbSpeedLimit.setImgMenuVisiable(0);
                this.vsbRTHeightLimit.setEnabled(true);
                this.vsbRTHeightLimit.setEnableClick(true);
                this.vsbRTHeightLimit.setImgMenuVisiable(0);
                this.vsbRTHeightLimit.setVisibility(0);
                this.vsbFlyHeightLimit.setEnabled(true);
                this.vsbFlyHeightLimit.setEnableClick(true);
                this.vsbFlyHeightLimit.setImgMenuVisiable(0);
                this.vsbFlyHeightLimit.setVisibility(0);
                viewEnabled(this.llFeelingSetting, true);
                resetView();
                this.swbNoviceMode.setSwitchState(false);
        }
    }

    public void noHandNewMode() {
        GlobalConfig.getInstance().setNewHand(false);
        this.vsbSpeedLimit.setEnabled(true);
        this.vsbSpeedLimit.setEnableClick(true);
        this.vsbSpeedLimit.setImgMenuVisiable(0);
        this.vsbRTHeightLimit.setEnabled(true);
        this.vsbRTHeightLimit.setEnableClick(true);
        this.vsbRTHeightLimit.setImgMenuVisiable(0);
        this.vsbRTHeightLimit.setVisibility(0);
        this.vsbFlyHeightLimit.setEnabled(true);
        this.vsbFlyHeightLimit.setEnableClick(true);
        this.vsbFlyHeightLimit.setImgMenuVisiable(0);
        this.vsbFlyHeightLimit.setVisibility(0);
        viewEnabled(this.llFeelingSetting, true);
        if (StateManager.getInstance().getX8Drone().isSportMode()) {
            resetViewBySport();
        } else {
            resetView();
        }
        this.swbNoviceMode.setSwitchState(false);
    }

    public void changeMotorPattern(boolean isMotorPattern, int type, boolean isPilotModePrimary) {
        if (type == 0) {
            if (!StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
                if (isMotorPattern) {
                    this.vsbSpeedLimit.setProgress(16);
                    this.vsbSpeedLimit.setEnabled(false);
                    this.vsbSpeedLimit.setImgMenuVisiable(8);
                    this.vsbSpeedLimit.setEnableClick(false);
                    this.vsbSpeedLimit.onOtherSelect();
                    viewEnabled(this.llFeelingSetting, false);
                } else if (!isPilotModePrimary) {
                    resetView();
                    this.vsbSpeedLimit.setEnabled(true);
                    this.vsbSpeedLimit.setImgMenuVisiable(0);
                    this.vsbSpeedLimit.setEnableClick(true);
                    viewEnabled(this.llFeelingSetting, true);
                }
            }
        } else if (!StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
            if (isMotorPattern) {
                this.vsbSpeedLimit.setProgress(18);
                this.vsbSpeedLimit.setEnabled(false);
                this.vsbSpeedLimit.setImgMenuVisiable(8);
                this.vsbSpeedLimit.setEnableClick(false);
                this.vsbSpeedLimit.onOtherSelect();
                viewEnabled(this.llFeelingSetting, false);
                return;
            }
            this.vsbSpeedLimit.setEnabled(true);
            this.vsbSpeedLimit.setImgMenuVisiable(0);
            this.vsbSpeedLimit.setEnableClick(true);
            viewEnabled(this.llFeelingSetting, true);
        }
    }

    public void setNoviceMode(final byte type, final boolean isSportMode) {
        this.fcCtrlManager.setPilotMode(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    boolean isOn = type == 0;
                    X8FcItemController.this.swbNoviceMode.onSwitch(isOn);
                    X8AppSettingLog.onChangePilotMode(isOn);
                    if (!isSportMode) {
                        X8FcItemController.this.handNewMode(type);
                    }
                    if (!isOn && isSportMode) {
                        X8FcItemController.this.resetViewBySport();
                    }
                }
            }
        }, type);
    }

    public void onSetHomeEvent(final int type) {
        double lat;
        double lng;
        if (!StateManager.getInstance().getX8Drone().isGPSMode()) {
            X8ToastUtil.showToast(this.rootView.getContext(), getString(R.string.x8_general_return_gps_failed), 0);
        } else if (this.mapVideoController.getFimiMap().hasHomeInfo()) {
            float h = StateManager.getInstance().getX8Drone().getHomeInfo().getHeight();
            float accuracy = this.mapVideoController.getFimiMap().getAccuracy();
            if (type == 0) {
                lat = StateManager.getInstance().getX8Drone().getLatitude();
                lng = StateManager.getInstance().getX8Drone().getLongitude();
            } else {
                double[] latLng = this.mapVideoController.getFimiMap().getManLatLng();
                if (latLng == null) {
                    X8ToastUtil.showToast(this.rootView.getContext(), getString(R.string.x8_general_return_person_failed), 0);
                    return;
                } else {
                    lat = latLng[0];
                    lng = latLng[1];
                }
            }
            this.fcManager.setHomePoint(h, lat, lng, type, accuracy, new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (type == 0) {
                        if (cmdResult.isSuccess()) {
                            X8ToastUtil.showToast(X8FcItemController.this.rootView.getContext(), X8FcItemController.this.getString(R.string.x8_general_return_drone), 0);
                        } else if (o == null) {
                            X8ToastUtil.showToast(X8FcItemController.this.rootView.getContext(), X8FcItemController.this.getString(R.string.x8_general_return_failed), 0);
                        }
                    } else if (cmdResult.isSuccess()) {
                        X8ToastUtil.showToast(X8FcItemController.this.rootView.getContext(), X8FcItemController.this.getString(R.string.x8_general_return_person), 0);
                    } else if (o == null) {
                        X8ToastUtil.showToast(X8FcItemController.this.rootView.getContext(), X8FcItemController.this.getString(R.string.x8_general_return_failed), 0);
                    }
                }
            });
        }
    }

    @Override
    public void defaultVal() {
        setViewEnable(false);
    }

    public void setMapVideoController(X8MapVideoController mapVideoController) {
        this.mapVideoController = mapVideoController;
    }

    public void setFcManager(FcManager fcManager) {
        this.fcManager = fcManager;
    }

    @Override
    public void onDroneConnected(boolean b) {
        boolean z = true;
        if (this.isShow) {
            if (this.rlFcItem != null) {
                if (b) {
                    reuqestUiValue();
                    if (StateManager.getInstance().getX8Drone().getCtrlMode() == 7 || StateManager.getInstance().getX8Drone().getCtrlMode() == 8) {
                        this.vsbRTHeightLimit.setEnabled(false);
                        this.vsbRTHeightLimit.setViewEnableByMode(false);
                    } else {
                        this.vsbRTHeightLimit.setEnabled(true);
                        this.vsbRTHeightLimit.setViewEnableByMode(true);
                    }
                } else {
                    defaultVal();
                }
            }
            if (!b) {
                this.isRequest = false;
            }
            if (StateManager.getInstance().getX8Drone().getFcSingal() != null) {
                showSingal(StateManager.getInstance().getX8Drone().getFcSingal());
            }
            getDroneState();
            if (this.isConect && this.isInSky) {
                this.iBtnReturnDrone.setAlpha(1.0f);
                this.iBtnReturnDrone.setEnabled(true);
                this.iBtnReturnPerson.setAlpha(1.0f);
                this.iBtnReturnPerson.setEnabled(true);
            } else {
                this.iBtnReturnDrone.setAlpha(0.4f);
                this.iBtnReturnDrone.setEnabled(false);
                this.iBtnReturnPerson.setAlpha(0.4f);
                this.iBtnReturnPerson.setEnabled(false);
            }
            if (this.vsbRTHeightLimit != null) {
                int currentMode = StateManager.getInstance().getX8Drone().getTaskMode();
                if (currentMode == X8Task.VCM_RTH.ordinal()) {
                    this.vsbRTHeightLimit.setEnableClick(false);
                } else this.vsbRTHeightLimit.setEnableClick(this.swbNoviceMode != null && !this.swbNoviceMode.getToggleOn());
            }
            boolean isOngroud = StateManager.getInstance().getX8Drone().isOnGround();
            Button button = this.x8FcResetParams;
            if (!isOngroud || !b) {
                z = false;
            }
            button.setEnabled(z);
            this.x8FcResetParams.setAlpha((isOngroud && b) ? 1.0f : 0.4f);
        }
    }

    @Override
    public void showItem() {
        if (this.rlFcItem == null) {
            View view = this.stubFcItem.inflate();
            this.rlFcItem = view.findViewById(R.id.x8_rl_main_fc_item);
            this.vsbSpeedLimit = this.rlFcItem.findViewById(R.id.vsb_speed_limit);
            this.vsbSpeedLimit.switchUnityWithSpeedLimit();
            this.vsbSpeedLimit.setConfirmListener(this.speedConfirmListener);
            this.vsbDistanceLimit = this.rlFcItem.findViewById(R.id.vsb_distance_limit);
            this.vsbDistanceLimit.switchUnityWithSpeedLimit();
            this.vsbDistanceLimit.setConfirmListener(this.limitConfirmListener);
            this.vsbDistanceLimit.setSwitchButtonVisibility(0);
            this.swbNoviceMode = this.rlFcItem.findViewById(R.id.swb_novice_mode);
            this.swbSportMode = this.rlFcItem.findViewById(R.id.swb_sport_mode);
            this.llFeelingSetting = this.rlFcItem.findViewById(R.id.ll_feeling_setting);
            this.vsbRTHeightLimit = this.rlFcItem.findViewById(R.id.vsb_return_height_limit);
            this.vsbRTHeightLimit.setConfirmListener(this.heightConfirmListener);
            this.vsbFlyHeightLimit = this.rlFcItem.findViewById(R.id.vsb_height_limit);
            this.vsbFlyHeightLimit.setConfirmListener(this.flyHeightConfirmListener);
            this.imgMagneticField = this.rlFcItem.findViewById(R.id.img_magnetic_field);
            this.btnCompassCalibration = this.rlFcItem.findViewById(R.id.btn_compass_calibration);
            this.vsbLightLimit = this.rlFcItem.findViewById(R.id.vsb_device_light);
            this.thDisconnectMeasure = this.rlFcItem.findViewById(R.id.th_disconnect_measure);
            this.swbAccurateLanding = this.rlFcItem.findViewById(R.id.swb_accurate_landing);
            this.fcExpSetting = this.rlFcItem.findViewById(R.id.fc_rocker_exp_setting);
            this.fcSensitivitySetting = this.rlFcItem.findViewById(R.id.fc_rocker_sensitivity_setting);
            this.iBtnReturnDrone = this.rlFcItem.findViewById(R.id.btn_return_drone);
            this.iBtnReturnPerson = this.rlFcItem.findViewById(R.id.btn_return_person);
            this.fcExpSetting.setOnClickListener(this);
            this.fcSensitivitySetting.setOnClickListener(this);
            this.tvAutoSetHome = this.rlFcItem.findViewById(R.id.tv_auto_set_home);
            this.swbAutoSetHome = this.rlFcItem.findViewById(R.id.swb_auto_set_home);
            this.swbFollowReturn = this.rlFcItem.findViewById(R.id.swb_set_follow_return);
            this.x8FcResetParams = this.rlFcItem.findViewById(R.id.x8_fc_btn_rest_params);
            this.x8FcResetParams.setOnClickListener(this);
            initActions();
        }
        this.isShow = true;
        switchUnity();
        this.rlFcItem.setVisibility(0);
        getDroneState();
        if (this.isConect) {
            if (StateManager.getInstance().getX8Drone().getFcSingal() != null) {
                showSingal(StateManager.getInstance().getX8Drone().getFcSingal());
            }
            if (StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
                this.swbNoviceMode.onSwitch(true);
                handNewMode(0);
                this.swbSportMode.onSwitch(false);
            } else if (StateManager.getInstance().getX8Drone().isSportMode()) {
                resetViewBySport();
                this.swbSportMode.onSwitch(true);
            } else {
                resetView();
            }
            if (this.isConect) {
                if (isGpsOrVpu()) {
                    this.swbAccurateLanding.setEnabled(true);
                    this.swbAccurateLanding.setAlpha(1.0f);
                } else {
                    this.swbAccurateLanding.setEnabled(false);
                    this.swbAccurateLanding.setAlpha(0.4f);
                }
            }
            this.swbAccurateLanding.setSwitchState(StateManager.getInstance().getX8Drone().isOpenAccurateLanding());
        } else {
            this.imgMagneticField.setImageLevel(1);
            setViewEnable(false);
        }
        if (this.isConect && this.isInSky) {
            this.iBtnReturnDrone.setAlpha(1.0f);
            this.iBtnReturnDrone.setEnabled(true);
            this.iBtnReturnPerson.setAlpha(1.0f);
            this.iBtnReturnPerson.setEnabled(true);
            return;
        }
        this.iBtnReturnDrone.setAlpha(0.4f);
        this.iBtnReturnDrone.setEnabled(false);
        this.iBtnReturnPerson.setAlpha(0.4f);
        this.iBtnReturnPerson.setEnabled(false);
    }

    public void reuqestUiValue() {
        if (!this.isRequest) {
            this.isRequest = true;
            getDroneState();
            setViewEnable(true);
            if (this.isConect) {
                initParams();
                requestNewHand();
            }
        }
    }

    public void initParams() {
        this.reGetLostActionTime = 0;
        this.fcCtrlManager.getLostAction(new UiCallBackListener<AckGetLostAction>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetLostAction obj) {
                if (!cmdResult.isSuccess()) {
                    X8FcItemController.this.thDisconnectMeasure.setAlpha(0.4f);
                    X8FcItemController.this.reGetLostAction();
                    return;
                }
                int value = obj.getAction();
                if (value == 0) {
                    X8FcItemController.this.thDisconnectMeasure.setSelect(2);
                } else if (value == 2) {
                    X8FcItemController.this.thDisconnectMeasure.setSelect(1);
                } else {
                    X8FcItemController.this.thDisconnectMeasure.setSelect(0);
                }
                X8FcItemController.this.thDisconnectMeasure.setAlpha(1.0f);
            }
        });
        this.fcCtrlManager.getReturnHomeHeight(new UiCallBackListener<AckGetRetHeight>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetRetHeight obj) {
                if (cmdResult.isSuccess()) {
                    if (!StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
                        X8FcItemController.this.vsbRTHeightLimit.setProgress(obj.getHeight());
                    }
                    X8FcItemController.this.vsbRTHeightLimit.setImbConfirmEnable(false);
                }
            }
        });
        this.fcCtrlManager.getSportMode(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                AckGetSportMode ackGetSportMode = (AckGetSportMode) o;
                if (cmdResult.isSuccess()) {
                    if (ackGetSportMode.getVehicleControlType() == 1) {
                        StateManager.getInstance().getX8Drone().setSportMode(true);
                        X8FcItemController.this.swbSportMode.onSwitch(true);
                        X8FcItemController.this.changeMotorPattern(true, 1, false);
                        return;
                    }
                    StateManager.getInstance().getX8Drone().setSportMode(false);
                    X8FcItemController.this.swbSportMode.onSwitch(false);
                    X8FcItemController.this.changeMotorPattern(false, 1, false);
                }
            }
        });
        this.fcCtrlManager.getFlyHeight(new UiCallBackListener<AckGetFcParam>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetFcParam obj) {
                if (cmdResult.isSuccess() && obj.getParamIndex() == 5) {
                    StateManager.getInstance().getX8Drone().setFlyHeight(obj.getParamData());
                    if (!StateManager.getInstance().getX8Drone().isPilotModePrimary()) {
                        X8FcItemController.this.vsbFlyHeightLimit.setProgress(obj.getParamData());
                    }
                }
            }
        });
        this.fcCtrlManager.getGpsSpeedParam(new UiCallBackListener<AckGetFcParam>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetFcParam obj) {
                if (cmdResult.isSuccess() && obj.getParamIndex() == 3) {
                    X8FcItemController.this.vsbSpeedLimit.setImbConfirmEnable(false);
                    StateManager.getInstance().getX8Drone().setGpsSpeed(obj.getParamData());
                    if (!StateManager.getInstance().getX8Drone().isPilotModePrimary() && StateManager.getInstance().getX8Drone().isSportMode()) {
                    }
                }
            }
        });
        this.fcCtrlManager.getFlyDistanceParam(new UiCallBackListener<AckGetFcParam>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetFcParam obj) {
                if (cmdResult.isSuccess() && obj.getParamIndex() == 7) {
                    StateManager.getInstance().getX8Drone().setFlyDistance(obj.getParamData());
                    obj.getParamData();
                }
            }
        });
        this.fcCtrlManager.getAutoHomePoint(new UiCallBackListener<AckGetAutoHome>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetAutoHome obj) {
                if (cmdResult.isSuccess()) {
                    X8FcItemController.this.swbAutoSetHome.onSwitch(obj.getState() == 1);
                }
            }
        });
        this.swbFollowReturn.onSwitch(StateManager.getInstance().getX8Drone().getFollowReturn() == 1);
        this.fcCtrlManager.getAiFollowEnableBack(new UiCallBackListener<AckAiFollowGetEnableBack>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckAiFollowGetEnableBack obj) {
                if (cmdResult.isSuccess()) {
                    X8FcItemController.this.swbFollowReturn.onSwitch(obj.getEnable() == 1);
                }
            }
        });
        this.fcCtrlManager.getAccurateLanding(new UiCallBackListener<AckAccurateLanding>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckAccurateLanding ackAccurateLanding) {
                if (cmdResult.isSuccess()) {
                    X8FcItemController.this.swbAccurateLanding.setSwitchState(ackAccurateLanding.getState() == 1);
                }
            }
        });
    }

    @Override
    public void closeItem() {
        if (this.rlFcItem != null) {
            this.rlFcItem.setVisibility(8);
            this.isShow = false;
            reuqestUiValue();
            requestNewHand();
        }
    }

    public void showSingal(AutoFcSignalState signalState) {
        getDroneState();
        if (this.isConect) {
            if (this.imgMagneticField != null) {
                int magnetic = signalState.getMagnetic();
                if (magnetic >= 0 && magnetic <= 20) {
                    this.imgMagneticField.setImageLevel(4);
                    return;
                } else if (magnetic >= 21 && magnetic <= 40) {
                    this.imgMagneticField.setImageLevel(3);
                    return;
                } else {
                    this.imgMagneticField.setImageLevel(2);
                    return;
                }
            }
            return;
        }
        this.imgMagneticField.setImageLevel(1);
    }

    public void setViewEnable(boolean b) {
        this.vsbSpeedLimit.setEnabled(b);
        this.vsbSpeedLimit.setViewEnable(b);
        this.swbNoviceMode.setEnabled(b);
        if (StateManager.getInstance().getX8Drone().getCtrlMode() == 7 || StateManager.getInstance().getX8Drone().getCtrlMode() == 8) {
            this.vsbRTHeightLimit.setEnabled(false);
            this.vsbRTHeightLimit.setViewEnableByMode(false);
        } else {
            this.vsbRTHeightLimit.setEnabled(b);
            this.vsbRTHeightLimit.setViewEnable(b);
        }
        this.vsbFlyHeightLimit.setEnabled(b);
        this.vsbFlyHeightLimit.setViewEnable(b);
        this.imgMagneticField.setEnabled(b);
        this.btnCompassCalibration.setEnabled(b);
        this.vsbLightLimit.setEnabled(b);
        this.vsbLightLimit.setViewEnable(b);
        this.vsbDistanceLimit.setEnabled(b);
        this.vsbDistanceLimit.setViewEnable(b);
        this.swbSportMode.setEnabled(b);
        this.thDisconnectMeasure.setEnabled(b);
        this.swbAutoSetHome.setEnabled(b);
        this.swbFollowReturn.setEnabled(b);
        if (b) {
            this.swbNoviceMode.setAlpha(1.0f);
            this.imgMagneticField.setAlpha(1.0f);
            this.btnCompassCalibration.setAlpha(1.0f);
            this.swbSportMode.setAlpha(1.0f);
            this.swbAutoSetHome.setAlpha(1.0f);
            this.swbFollowReturn.setAlpha(1.0f);
            setMagneticField(3);
            boolean v = StateManager.getInstance().getX8Drone().getFlyDistance() > 5000.0f || StateManager.getInstance().getX8Drone().isPilotModePrimary();
            this.vsbDistanceLimit.setValueVisibily(0, v);
            if (isGpsOrVpu()) {
                this.swbAccurateLanding.setEnabled(true);
                this.swbAccurateLanding.setAlpha(1.0f);
                return;
            }
            this.swbAccurateLanding.setEnabled(false);
            this.swbAccurateLanding.setAlpha(0.4f);
            return;
        }
        this.swbNoviceMode.setAlpha(0.4f);
        this.imgMagneticField.setAlpha(0.4f);
        this.btnCompassCalibration.setAlpha(0.4f);
        this.thDisconnectMeasure.setAlpha(0.4f);
        this.swbSportMode.setAlpha(0.4f);
        this.swbAccurateLanding.setAlpha(0.4f);
        setMagneticField(1);
        this.swbAutoSetHome.setAlpha(0.4f);
        this.swbFollowReturn.setAlpha(0.4f);
        boolean v2 = StateManager.getInstance().getX8Drone().getFlyDistance() > 5000.0f || StateManager.getInstance().getX8Drone().isPilotModePrimary();
        this.vsbDistanceLimit.setValueVisibily(8, v2);
        this.vsbDistanceLimit.setSwitchButtonVisibility();
    }

    public void setMagneticField(int type) {
        switch (type) {
            case 1:
                this.imgMagneticField.setImageLevel(1);
                return;
            case 2:
                this.imgMagneticField.setImageLevel(2);
                return;
            case 3:
                this.imgMagneticField.setImageLevel(3);
                return;
            case 4:
                this.imgMagneticField.setImageLevel(4);
                return;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_compass_calibration) {
            this.listener.onCalibrationItemClick();
        } else if (id == R.id.fc_rocker_exp_setting) {
            this.listener.onFcExpSettingClick();
        } else if (id == R.id.fc_rocker_sensitivity_setting) {
            this.listener.onFcSensitivitySettingClick();
        } else if (id == R.id.tv_auto_set_home) {
            showDialog();
        } else if (id == R.id.x8_fc_btn_rest_params) {
            showRestParamDialog();
        }
    }

    public void restFcSystemParams() {
        GlobalConfig.getInstance().setLowReturn(true);
        GlobalConfig.getInstance().setLowLanding(true);
        this.fcCtrlManager.restSystemParams(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8FcItemController.this.initParams();
                    X8FcItemController.this.requestNewHand();
                    X8ToastUtil.showToast(X8FcItemController.this.mConext, X8FcItemController.this.mConext.getString(R.string.x8_general_rest_paramters_success), 1);
                    X8FcItemController.this.rootView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            X8FcItemController.this.resetView();
                        }
                    }, 1000L);
                    return;
                }
                X8ToastUtil.showToast(X8FcItemController.this.mConext, X8FcItemController.this.mConext.getString(R.string.x8_fc_reset_params_hint_failed), 1);
            }
        });
    }

    public void showRestParamDialog() {
        if (this.x8DoubleCustomDialog == null) {
            this.x8DoubleCustomDialog = new X8DoubleCustomDialog(this.rootView.getContext(), this.rootView.getContext().getString(R.string.x8_fc_reset_params), this.rootView.getContext().getString(R.string.x8_fc_reset_params_hint), this.rootView.getContext().getString(R.string.x8_general_rest), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8FcItemController.this.restFcSystemParams();
                }
            });
        }
        this.x8DoubleCustomDialog.show();
    }

    public void setListener(IX8FcItemListener listener) {
        this.listener = listener;
    }

    public void setCoverListener(IX8MainCoverListener coverListener) {
        this.coverListener = coverListener;
    }

    @Override
    public String getString(int id) {
        return this.rootView.getContext().getString(id);
    }

    public void switchUnity() {
        if (StateManager.getInstance().getX8Drone().isConnect()) {
            this.vsbSpeedLimit.switchUnityWithSpeedLimit();
            this.vsbRTHeightLimit.switchUnityWithDistanceLimit();
            this.vsbFlyHeightLimit.switchUnityWithDistanceLimit();
        }
    }

    public void requestNewHand() {
        this.fcCtrlManager.getPilotMode(new UiCallBackListener<AckGetPilotMode>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetPilotMode obj) {
                if (cmdResult.isSuccess()) {
                    int value = obj.getPilotMode();
                    if (value == 0) {
                        X8FcItemController.this.swbNoviceMode.onSwitch(true);
                        X8FcItemController.this.handNewMode(0);
                        X8FcItemController.this.swbSportMode.onSwitch(false);
                        return;
                    }
                    X8FcItemController.this.swbNoviceMode.onSwitch(false);
                    X8FcItemController.this.noHandNewMode();
                }
            }
        });
    }

    @Override
    public boolean onClickBackKey() {
        return false;
    }

    public void showDialog() {
        X8SingleCustomDialog dialog = null;
        if (0 == 0) {
            String t = this.rootView.getContext().getString(R.string.x8_fc_item_auto_set_home_title);
            String m = this.rootView.getContext().getString(R.string.x8_fc_item_auto_set_home_tip);
            dialog = new X8SingleCustomDialog(this.rootView.getContext(), t, m, new X8SingleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8SingleCustomDialog.onDialogButtonClickListener
                public void onSingleButtonClick() {
                }
            });
        }
        dialog.show();
    }

    public void showChangeHomeDialog(final int type) {
        if (type == 0) {
            String t = this.rootView.getContext().getString(R.string.x8_switch_home2_title);
            String m = this.rootView.getContext().getString(R.string.x8_switch_home2_drone_msg);
            X8DoubleCustomDialog dialog = new X8DoubleCustomDialog(this.rootView.getContext(), t, m, new X8DoubleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8FcItemController.this.onSetHomeEvent(type);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else if (type == 1) {
            String t2 = this.rootView.getContext().getString(R.string.x8_switch_home2_title);
            String m2 = this.rootView.getContext().getString(R.string.x8_switch_home2_phone_title);
            X8DoubleCustomDialog dialog2 = new X8DoubleCustomDialog(this.rootView.getContext(), t2, m2, new X8DoubleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8FcItemController.this.onSetHomeEvent(type);
                }
            });
            dialog2.setCanceledOnTouchOutside(false);
            dialog2.show();
        }
    }

    public void showModeSeniorDialog() {
        if (this.dialog == null) {
            String title = this.rootView.getContext().getString(R.string.x8_open_motor_pattern_title);
            String hint = this.rootView.getContext().getString(R.string.x8_open_motor_pattern_hint);
            this.dialog = new X8DoubleCustomDialog(this.rootView.getContext(), title, hint, new X8DoubleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onLeft() {
                    X8FcItemController.this.dialog.dismiss();
                }

                @Override
                // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                public void onRight() {
                    X8FcItemController.this.setSportMode(1, false);
                    X8FcItemController.this.dialog.dismiss();
                }
            });
        }
        this.dialog.show();
    }

    public void showFollowDialog(boolean on) {
        String title = this.rootView.getContext().getString(R.string.x8_fc_item_follow_return_title);
        String hint = this.rootView.getContext().getString(R.string.x8_fc_item_follow_return_msg);
        X8DoubleCustomDialog dialog1 = new X8DoubleCustomDialog(this.rootView.getContext(), title, hint, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                X8FcItemController.this.fcCtrlManager.setAiFollowEnableBack(1, new UiCallBackListener() {
                    @Override
                    public void onComplete(CmdResult cmdResult, Object o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getX8Drone().setFollowReturn(1);
                            X8FcItemController.this.swbFollowReturn.onSwitch(true);
                        }
                    }
                });
            }
        });
        dialog1.show();
    }

    public void resetView() {
        if (StateManager.getInstance().getX8Drone().getReturnHomeHight() > 0.0f) {
            this.vsbRTHeightLimit.setProgress(StateManager.getInstance().getX8Drone().getReturnHomeHight());
        }
        if (StateManager.getInstance().getX8Drone().getFlyHeight() > 0.0f) {
            this.vsbFlyHeightLimit.setProgress(StateManager.getInstance().getX8Drone().getFlyHeight());
        }
        if (StateManager.getInstance().getX8Drone().getGpsSpeed() > 0.0f) {
            this.vsbSpeedLimit.setProgress(StateManager.getInstance().getX8Drone().getGpsSpeed());
        }
        if (StateManager.getInstance().getX8Drone().getFlyDistance() > 0.0f) {
            if (StateManager.getInstance().getX8Drone().getFlyDistance() > 5000.0f) {
                this.vsbDistanceLimit.setSwitchButtonState(false);
                this.vsbDistanceLimit.setNoLimit();
            } else {
                this.vsbDistanceLimit.setSwitchButtonState(true);
                this.vsbDistanceLimit.setProgress(StateManager.getInstance().getX8Drone().getFlyDistance(), true);
            }
            this.vsbDistanceLimit.setSwitchButtonVisibility();
        }
    }

    public void resetSpeedLimit() {
        this.vsbSpeedLimit.setProgress(StateManager.getInstance().getX8Drone().getGpsSpeed());
        this.vsbSpeedLimit.setEnabled(true);
        this.vsbSpeedLimit.setEnableClick(true);
        this.vsbSpeedLimit.onOtherSelect();
        this.vsbSpeedLimit.setImgMenuVisiable(0);
    }

    public void resetViewBySport() {
        if (StateManager.getInstance().getX8Drone().getReturnHomeHight() > 0.0f) {
            this.vsbRTHeightLimit.setProgress(StateManager.getInstance().getX8Drone().getReturnHomeHight());
        }
        if (StateManager.getInstance().getX8Drone().getFlyHeight() > 0.0f) {
            this.vsbFlyHeightLimit.setProgress(StateManager.getInstance().getX8Drone().getFlyHeight());
        }
        this.vsbSpeedLimit.setProgress(18);
        this.vsbSpeedLimit.setEnabled(false);
        this.vsbSpeedLimit.setEnableClick(false);
        this.vsbSpeedLimit.onOtherSelect();
        this.vsbSpeedLimit.setImgMenuVisiable(8);
        this.vsbRTHeightLimit.setEnabled(true);
        this.vsbRTHeightLimit.setEnableClick(true);
        this.vsbRTHeightLimit.setImgMenuVisiable(0);
        this.vsbRTHeightLimit.setVisibility(0);
        this.vsbFlyHeightLimit.setEnabled(true);
        this.vsbFlyHeightLimit.setEnableClick(true);
        this.vsbFlyHeightLimit.setImgMenuVisiable(0);
        this.vsbFlyHeightLimit.setVisibility(0);
        viewEnabled(this.llFeelingSetting, true);
        if (StateManager.getInstance().getX8Drone().getFlyDistance() > 0.0f) {
            if (StateManager.getInstance().getX8Drone().getFlyDistance() > 5000.0f) {
                this.vsbDistanceLimit.setSwitchButtonState(false);
                this.vsbDistanceLimit.setNoLimit();
            } else {
                this.vsbDistanceLimit.setSwitchButtonState(true);
                this.vsbDistanceLimit.setProgress(StateManager.getInstance().getX8Drone().getFlyDistance(), true);
            }
            this.vsbDistanceLimit.setSwitchButtonVisibility();
        }
    }

    public void sendFlyHeight(final float value) {
        this.fcCtrlManager.setFlyHeight(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    StateManager.getInstance().getX8Drone().setFlyHeight(value);
                    X8FcItemController.this.vsbFlyHeightLimit.setImbConfirmEnable(false);
                }
            }
        }, value);
    }

    public void showFlyHeightDialog(final float value) {
        String title = this.rootView.getContext().getString(R.string.x8_fc_item_height_limit);
        String hint = this.rootView.getContext().getString(R.string.x8_fc_fly_height_limit_tip);
        X8DoubleCustomDialog dialog1 = new X8DoubleCustomDialog(this.rootView.getContext(), title, hint, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                X8FcItemController.this.sendFlyHeight(value);
            }
        });
        dialog1.show();
    }

    public void showFailsafeDialog(final int last, final int value, final int index) {
        String hint;
        String title = this.rootView.getContext().getString(R.string.x8_setting_fc_loastaction_tips_title);
        if (value == 1) {
            hint = this.rootView.getContext().getString(R.string.x8_setting_fc_loastaction_tips_content_hover);
        } else if (value == 2) {
            hint = this.rootView.getContext().getString(R.string.x8_setting_fc_loastaction_tips_content_leading);
        } else {
            hint = this.rootView.getContext().getString(R.string.x8_setting_fc_loastaction_tips_content_back);
        }
        X8DoubleCustomDialog dialog1 = new X8DoubleCustomDialog(this.rootView.getContext(), title, hint, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                X8FcItemController.this.setFailsafe(last, value, index);
            }
        });
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.show();
    }

    public void setFailsafe(int last, final int value, final int index) {
        this.fcCtrlManager.setLostAction(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8FcItemController.this.thDisconnectMeasure.setSelect(index);
                    X8AppSettingLog.onChangeLostAciton(value);
                }
            }
        }, value);
    }

    public void setLimitDistance(float distance) {
    }

    public void showDistanceDialog() {
        String title = this.rootView.getContext().getString(R.string.x8_fc_item_distance_limit);
        String hint = this.rootView.getContext().getString(R.string.x8_fc_fly_distance_limit_msg);
        X8DoubleCustomDialog dialog1 = new X8DoubleCustomDialog(this.rootView.getContext(), title, hint, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                X8FcItemController.this.closeSetFlyDistance(99000.0f);
            }
        });
        dialog1.show();
    }

    public void closeSetFlyDistance(final float distance) {
        this.fcCtrlManager.setFlyDistanceParam(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    StateManager.getInstance().getX8Drone().setFlyDistance(distance);
                    X8FcItemController.this.vsbDistanceLimit.setSwitchButtonState(false);
                    X8FcItemController.this.vsbDistanceLimit.setNoLimit();
                }
            }
        }, distance);
    }

    public void openSetFlyDistance(final float distance) {
        this.fcCtrlManager.setFlyDistanceParam(new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    StateManager.getInstance().getX8Drone().setFlyDistance(distance);
                    X8FcItemController.this.vsbDistanceLimit.setSwitchButtonState(true);
                    X8FcItemController.this.vsbDistanceLimit.setProgress(distance, true);
                }
            }
        }, distance);
    }

    public void sendAccurateLandingCmd(boolean on) {
        if (!on) {
            this.fcCtrlManager.openAccurateLanding(new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        X8FcItemController.this.swbAccurateLanding.setSwitchState(true);
                        X8AppSettingLog.onChangeAccurateLanding(true);
                    }
                }
            });
        } else {
            this.fcCtrlManager.closeAccurateLanding(new UiCallBackListener() {
                @Override
                public void onComplete(CmdResult cmdResult, Object o) {
                    if (cmdResult.isSuccess()) {
                        X8FcItemController.this.swbAccurateLanding.setSwitchState(false);
                        X8AppSettingLog.onChangeAccurateLanding(false);
                    }
                }
            });
        }
    }

    public void showNewHandDialog() {
        X8SingleCustomDialog dialog = null;
        if (0 == 0) {
            String t = this.rootView.getContext().getString(R.string.x8_fc_item_novice_mode_disable);
            String m = this.rootView.getContext().getString(R.string.x8_fc_item_novice_mode_disable_message);
            dialog = new X8SingleCustomDialog(this.rootView.getContext(), t, m, new X8SingleCustomDialog.onDialogButtonClickListener() {
                @Override
                // com.fimi.app.x8s.widget.X8SingleCustomDialog.onDialogButtonClickListener
                public void onSingleButtonClick() {
                }
            });
        }
        dialog.show();
    }

    public void viewEnabled(View view, boolean isEnabled) {
        this.fcSensitivitySetting.setEnabled(isEnabled);
        this.fcExpSetting.setEnabled(isEnabled);
        view.setAlpha(isEnabled ? 1.0f : 0.4f);
    }

    public void reGetLostAction() {
        this.fcCtrlManager.getLostAction(new UiCallBackListener<AckGetLostAction>() {
            @Override
            public void onComplete(CmdResult cmdResult, AckGetLostAction obj) {
                if (!cmdResult.isSuccess()) {
                    X8FcItemController.this.thDisconnectMeasure.setAlpha(0.4f);
                    X8FcItemController.access$3108(X8FcItemController.this);
                    if (X8FcItemController.this.reGetLostActionTime < 3) {
                        X8FcItemController.this.reGetLostAction();
                        return;
                    }
                    return;
                }
                int value = obj.getAction();
                if (value == 0) {
                    X8FcItemController.this.thDisconnectMeasure.setSelect(2);
                } else if (value == 2) {
                    X8FcItemController.this.thDisconnectMeasure.setSelect(1);
                } else {
                    X8FcItemController.this.thDisconnectMeasure.setSelect(0);
                }
                X8FcItemController.this.thDisconnectMeasure.setAlpha(1.0f);
            }
        });
    }
}
