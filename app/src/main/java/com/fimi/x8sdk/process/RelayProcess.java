package com.fimi.x8sdk.process;

import android.os.SystemClock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fimi.host.HostConstants;
import com.fimi.host.HostLogBack;
import com.fimi.host.LocalFwEntity;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.session.JsonListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.BitUtil;
import com.fimi.x8sdk.command.CameraJsonCollection;
import com.fimi.x8sdk.command.X8BaseCmd;
import com.fimi.x8sdk.connect.DeviceMonitorThread;
import com.fimi.x8sdk.controller.AllSettingManager;
import com.fimi.x8sdk.controller.FcManager;
import com.fimi.x8sdk.dataparser.AckCamJsonInfo;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.dataparser.AutoRelayHeart;
import com.fimi.x8sdk.listener.RelayHeartListener;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.modulestate.VersionState;
import com.fimi.x8sdk.rtp.X8Rtp;

/* loaded from: classes2.dex */
public class RelayProcess implements JsonListener {
    private static volatile RelayProcess relayProcess = null;
    DeviceMonitorThread monitorThread;
    AutoRelayHeart relayHeart;
    private int cameraState;
    private long curTime;
    private FcManager fcManager;
    private boolean getSetting;
    private int countFw = 0;
    private int MAXFW = 11;
    private boolean isShowUpdateView = true;

    public static RelayProcess getRelayProcess() {
        if (relayProcess == null) {
            synchronized (RelayProcess.class) {
                if (relayProcess == null) {
                    relayProcess = new RelayProcess();
                }
            }
        }
        return relayProcess;
    }

    public boolean isShowUpdateView() {
        return this.isShowUpdateView;
    }

    public void setShowUpdateView(boolean showUpdateView) {
        this.isShowUpdateView = showUpdateView;
    }

    public void registerListener(RelayHeartListener listener) {
        this.monitorThread = new DeviceMonitorThread();
        this.monitorThread.start();
        NoticeManager.getInstance().addJsonListener(this);
    }

    public void removeListener(RelayHeartListener listener) {
        if (this.monitorThread != null) {
            this.monitorThread.exit();
        }
        NoticeManager.getInstance().removeJsonListener(this);
    }

    public AutoRelayHeart getRelayHeart() {
        return this.relayHeart;
    }

    public void setRelayHeart(AutoRelayHeart relayHeart) {
        this.relayHeart = relayHeart;
        int isConnect = BitUtil.getBitByByte(relayHeart.getStatus(), 2);
        int token = StateManager.getInstance().getCamera().getToken();
        boolean isQuestToken = isConnect > 0 && token <= 0;
        if (isQuestToken && SystemClock.uptimeMillis() - this.curTime > 2000) {
            this.curTime = SystemClock.uptimeMillis();
            SessionManager.getInstance().sendCmd(new CameraJsonCollection().startSession());
        }
        if (isConnect <= 0) {
            StateManager.getInstance().getCamera().setToken(-1);
        }
        AckVersion ackVersionCamera = StateManager.getInstance().getVersionState().getModuleCameraVersion();
        AckVersion ackVersionCV = StateManager.getInstance().getVersionState().getModuleCvVersion();
        if (ackVersionCamera == null || ackVersionCV == null) {
            getCameraVersion();
        }
        AckVersion ackVersion = StateManager.getInstance().getX8Drone().getVersion();
        if (ackVersion == null) {
            getAllVersion();
            this.getSetting = false;
        }
        getAllSetting();
    }

    @Override // com.fimi.kernel.connect.session.JsonListener
    public void onProcess(int msgId, JSONObject json) {
        if (json != null) {
            AckCamJsonInfo jsonInfo = (AckCamJsonInfo) JSON.parseObject(json.toJSONString(), AckCamJsonInfo.class);
            int retVal = jsonInfo.getRval();
            jsonInfo.getType();
            String param = jsonInfo.getParam();
            if (retVal == 0) {
                if (msgId == 257) {
                    int token = Integer.valueOf(param).intValue();
                    StateManager.getInstance().getCamera().setToken(token);
                    StateManager.getInstance().setCameraToken(token);
                    return;
                }
                if (msgId == 2) {
                }
            }
        }
    }

    public void sendCmd(BaseCommand cmd) {
        if (cmd != null) {
            SessionManager.getInstance().sendCmd(cmd);
        }
    }

    public void getAllVersion() {
        if (this.fcManager != null) {
            if (StateManager.getInstance().getVersionState().getModuleRepeaterRcVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_REPEATER_RC.ordinal(), (byte) 11, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.1
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleRepeaterRcVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleRepeaterRcVersion(null);
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleRcVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_RC.ordinal(), (byte) 1, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.2
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleRcVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            HostLogBack.getInstance().writeLog("ALanqiu  ============MODULE_RC");
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleRcVersion(null);
                        HostLogBack.getInstance().writeLog("ALanqiu  ============MODULE_RC 失败");
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleRepeaterVehicleVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_REPEATER_VEHICLE.ordinal(), (byte) 12, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.3
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleRepeaterVehicleVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleRepeaterVehicleVersion(null);
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleEscVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_ESC.ordinal(), (byte) 14, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.4
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleEscVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleEscVersion(null);
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleGimbalVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_GIMBAL.ordinal(), (byte) 3, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.5
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleGimbalVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleGimbalVersion(null);
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleBatteryVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_BATTERY.ordinal(), (byte) 5, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.6
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleBatteryVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        HostLogBack.getInstance().writeLog("Alanqiu ==============MODULE_BATTERY:");
                        StateManager.getInstance().getVersionState().setModuleBatteryVersion(null);
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleNfzVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_NFZ.ordinal(), (byte) 10, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.7
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleNfzVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleNfzVersion(null);
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleUltrasonic() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_ULTRASONIC.ordinal(), (byte) 13, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.8
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleUltrasonic(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleUltrasonic(null);
                    }
                });
            }
            if (!X8Rtp.simulationTest) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_FC.ordinal(), (byte) 0, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.9
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleFcAckVersion(o);
                            RelayProcess.this.onGetVersionResult();
                            return;
                        }
                        StateManager.getInstance().getVersionState().setModuleFcAckVersion(null);
                    }
                });
            }
        }
    }

    void getCameraVersion() {
        if (this.fcManager != null) {
            if (StateManager.getInstance().getVersionState().getModuleCameraVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_CAMERA.ordinal(), (byte) 4, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.10
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleCameraVersion(o);
                        } else {
                            StateManager.getInstance().getVersionState().setModuleCameraVersion(null);
                        }
                        RelayProcess.this.onGetVersionResult();
                    }
                });
            }
            if (StateManager.getInstance().getVersionState().getModuleCvVersion() == null) {
                this.fcManager.getFwVersion((byte) X8BaseCmd.X8S_Module.MODULE_CV.ordinal(), (byte) 9, new UiCallBackListener<AckVersion>() { // from class: com.fimi.x8sdk.process.RelayProcess.11
                    @Override // com.fimi.kernel.dataparser.usb.UiCallBackListener
                    public void onComplete(CmdResult cmdResult, AckVersion o) {
                        if (cmdResult.isSuccess()) {
                            StateManager.getInstance().getVersionState().setModuleCvVersion(o);
                        } else {
                            StateManager.getInstance().getVersionState().setModuleCvVersion(null);
                        }
                        RelayProcess.this.onGetVersionResult();
                    }
                });
            }
        }
    }

    public void onGetVersionResult() {
        VersionState versionState = StateManager.getInstance().getVersionState();
        if (versionState.getModuleFcAckVersion() != null) {
            HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleFcAckVersion().getType(), versionState.getModuleFcAckVersion().getModel(), versionState.getModuleFcAckVersion().getSoftVersion(), ""));
            if (versionState.getModuleRcVersion() != null) {
                HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleRcVersion().getType(), versionState.getModuleRcVersion().getModel(), versionState.getModuleRcVersion().getSoftVersion(), ""));
                if (versionState.getModuleCvVersion() != null) {
                    HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleCvVersion().getType(), versionState.getModuleCvVersion().getModel(), versionState.getModuleCvVersion().getSoftVersion(), ""));
                    if (versionState.getModuleRepeaterRcVersion() != null) {
                        HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleRepeaterRcVersion().getType(), versionState.getModuleRepeaterRcVersion().getModel(), versionState.getModuleRepeaterRcVersion().getSoftVersion(), ""));
                        if (versionState.getModuleRepeaterVehicleVersion() != null) {
                            HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleRepeaterVehicleVersion().getType(), versionState.getModuleRepeaterVehicleVersion().getModel(), versionState.getModuleRepeaterVehicleVersion().getSoftVersion(), ""));
                            if (versionState.getModuleEscVersion() != null) {
                                HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleEscVersion().getType(), versionState.getModuleEscVersion().getModel(), versionState.getModuleEscVersion().getSoftVersion(), ""));
                                if (versionState.getModuleGimbalVersion() != null) {
                                    HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleGimbalVersion().getType(), versionState.getModuleGimbalVersion().getModel(), versionState.getModuleGimbalVersion().getSoftVersion(), ""));
                                    if (versionState.getModuleBatteryVersion() != null) {
                                        HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleBatteryVersion().getType(), versionState.getModuleBatteryVersion().getModel(), versionState.getModuleBatteryVersion().getSoftVersion(), ""));
                                        if (versionState.getModuleNfzVersion() != null) {
                                            HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleNfzVersion().getType(), versionState.getModuleNfzVersion().getModel(), versionState.getModuleNfzVersion().getSoftVersion(), ""));
                                            if (versionState.getModuleCameraVersion() != null) {
                                                HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleCameraVersion().getType(), versionState.getModuleCameraVersion().getModel(), versionState.getModuleCameraVersion().getSoftVersion(), ""));
                                                if (versionState.getModuleUltrasonic() != null) {
                                                    HostConstants.saveLocalFirmware(new LocalFwEntity(versionState.getModuleUltrasonic().getType(), versionState.getModuleUltrasonic().getModel(), versionState.getModuleUltrasonic().getSoftVersion(), ""));
                                                    SessionManager.getInstance().onDeviveState(1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setFcManager(FcManager fcManager) {
        this.fcManager = fcManager;
    }

    public void getAllSetting() {
        if (!this.getSetting) {
            AllSettingManager.getInstance().getAllSetting();
            this.getSetting = true;
        }
    }
}
