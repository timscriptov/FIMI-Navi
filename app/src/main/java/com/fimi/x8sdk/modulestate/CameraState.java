package com.fimi.x8sdk.modulestate;

import com.fimi.kernel.utils.BitUtil;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.entity.CameraSystemState;

/* loaded from: classes2.dex */
public class CameraState extends BaseState {
    AutoCameraStateADV autoCameraStateADV;
    CameraSystemState cameraSystemState = new CameraSystemState();
    private boolean takingPanoramicPhotos;
    private int token = -1;

    @Override // com.fimi.x8sdk.modulestate.BaseState
    public boolean isAvailable() {
        return getLoginState() == 1;
    }

    public void setCameraStatus(int cameraStatus) {
        int camearStatus = BitUtil.getBitByByte(cameraStatus, 2);
        setLoginState(camearStatus);
    }

    public int getToken() {
        return this.token;
    }

    public void setToken(int token) {
        this.token = token;
        if (token == -1) {
            StateManager.getInstance().getVersionState().setModuleCameraVersion(null);
            StateManager.getInstance().getVersionState().setModuleCvVersion(null);
        }
    }

    public AutoCameraStateADV getAutoCameraStateADV() {
        return this.autoCameraStateADV;
    }

    public void setAutoCameraStateADV(AutoCameraStateADV autoCameraStateADV) {
        this.autoCameraStateADV = autoCameraStateADV;
    }

    public boolean isDelayedPhotography() {
        if (this.autoCameraStateADV == null) {
            return false;
        }
        return this.autoCameraStateADV.isDelayedPhotography();
    }

    public boolean isTakingPanoramicPhotos() {
        return this.takingPanoramicPhotos;
    }

    public void setTakingPanoramicPhotos(boolean takingPanoramicPhotos) {
        this.takingPanoramicPhotos = takingPanoramicPhotos;
    }

    public CameraSystemState getCameraSystemState() {
        return this.cameraSystemState;
    }

    public boolean isConnect() {
        return this.token >= 0;
    }
}
