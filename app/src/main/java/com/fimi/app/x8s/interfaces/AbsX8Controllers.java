package com.fimi.app.x8s.interfaces;

import android.view.View;

import com.fimi.x8sdk.modulestate.DroneState;
import com.fimi.x8sdk.modulestate.StateManager;

/* loaded from: classes.dex */
public abstract class AbsX8Controllers implements IControllers {
    protected int ctrlType;
    protected View handleView;
    protected boolean isConect;
    protected boolean isInSky;
    protected boolean isLowpower;
    protected boolean isRcConnect;
    protected boolean isShow;
    protected View rootView;

    public AbsX8Controllers(View rootView) {
        this.rootView = rootView;
        initViews(rootView);
        initActions();
        getDroneState();
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        if (this.handleView != null) {
            this.handleView.setVisibility(0);
        }
    }

    public void getDroneState() {
        if (StateManager.getInstance().getX8Drone() != null) {
            this.isConect = StateManager.getInstance().getX8Drone().isConnect();
            this.isInSky = StateManager.getInstance().getX8Drone().isInSky();
            this.ctrlType = StateManager.getInstance().getX8Drone().getCtrlType();
        }
        if (StateManager.getInstance().getErrorCodeState() != null) {
            this.isLowpower = StateManager.getInstance().getErrorCodeState().isLowPower();
        }
        if (StateManager.getInstance().getConectState() != null) {
            this.isRcConnect = StateManager.getInstance().getConectState().isConnectRelay();
        }
    }

    @Override // com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        if (this.handleView != null) {
            this.handleView.setVisibility(8);
        }
    }

    public void showItem() {
    }

    public void closeItem() {
    }

    public boolean isShow() {
        return this.isShow;
    }

    public void onDroneConnected(boolean b) {
    }

    public String getString(int res) {
        return this.rootView != null ? this.rootView.getContext().getResources().getString(res) : "";
    }

    public boolean isTaskTimeOut() {
        long currentTimeMillis = System.currentTimeMillis();
        StateManager.getInstance().getX8Drone();
        return currentTimeMillis - DroneState.taskMadeChangeTime >= 600;
    }

    public void cancleByModeChange() {
    }

    public boolean isGpsOrVpu() {
        if (this.ctrlType != 2 && this.ctrlType != 3) {
            return false;
        }
        return true;
    }

    public boolean isAtti() {
        getDroneState();
        if (this.ctrlType != 1) {
            return false;
        }
        return true;
    }

    public boolean isVpu() {
        getDroneState();
        if (this.ctrlType != 3) {
            return false;
        }
        return true;
    }
}
