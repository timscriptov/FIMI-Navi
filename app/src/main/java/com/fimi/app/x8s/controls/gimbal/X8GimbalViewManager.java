package com.fimi.app.x8s.controls.gimbal;

import android.view.View;
import android.view.ViewGroup;

import com.fimi.app.x8s.controls.fcsettting.X8GimbalHorizontalTrimController;
import com.fimi.app.x8s.controls.fcsettting.X8GimbalXYZAdjustController;
import com.fimi.app.x8s.enums.X8FcAllSettingMenuEnum;
import com.fimi.app.x8s.interfaces.IX8GimbalHorizontalTrimListener;
import com.fimi.app.x8s.interfaces.IX8GimbalXYZAdjustListener;
import com.fimi.app.x8s.ui.activity.X8sMainActivity;


public class X8GimbalViewManager {
    private final X8sMainActivity activity;
    private X8GimbalHorizontalTrimController mX8GimbalHorizontalTrimController;
    private final View mainShowView;
    private X8GimbalXYZAdjustController x8GimbalXYZAdjustController;
    private final IX8GimbalHorizontalTrimListener mIX8GimbalHorizontalTrimListener = new IX8GimbalHorizontalTrimListener() {
        @Override
        public void onSettingReady(float value) {
            X8GimbalViewManager.this.mX8GimbalHorizontalTrimController.closeUi();
            X8GimbalViewManager.this.mX8GimbalHorizontalTrimController.onSettingReady();
            X8GimbalViewManager.this.activity.getX8MainFcAllSettingControler().showAllSettingUi(X8FcAllSettingMenuEnum.GIMBAL_ITEM);
            X8GimbalViewManager.this.activity.showTopByGimbalHorizontalTrim();
        }
    };
    private final IX8GimbalXYZAdjustListener ix8GimbalXYZAdjustListener = new IX8GimbalXYZAdjustListener() {
        @Override
        public void gimbalXYZAdjustExit() {
            X8GimbalViewManager.this.x8GimbalXYZAdjustController.closeUi();
            X8GimbalViewManager.this.activity.appFullSceen(false);
        }
    };

    public X8GimbalViewManager(View mainShowView, X8sMainActivity activity) {
        this.mainShowView = mainShowView;
        this.activity = activity;
    }

    public void initHorizontalTrim() {
        this.mX8GimbalHorizontalTrimController = new X8GimbalHorizontalTrimController(this.mainShowView, this.activity);
        this.mX8GimbalHorizontalTrimController.setListener(this.mIX8GimbalHorizontalTrimListener);
    }

    public void initXYZAdjust() {
        this.x8GimbalXYZAdjustController = new X8GimbalXYZAdjustController(this.mainShowView, this.activity);
        this.x8GimbalXYZAdjustController.setListener(this.ix8GimbalXYZAdjustListener, this.activity.mainTopBarListener);
        this.x8GimbalXYZAdjustController.setX8GimbalManager(this.activity.getmX8GimbalManager());
    }

    public void openHorizontalTrimUi() {
        if (this.mX8GimbalHorizontalTrimController == null) {
            initHorizontalTrim();
            this.mX8GimbalHorizontalTrimController.openUi();
        }
    }

    public void openGimbalXYZAdjustUI() {
        if (this.x8GimbalXYZAdjustController == null) {
            initXYZAdjust();
            this.x8GimbalXYZAdjustController.openUi();
        }
    }

    public void closeHorizontalTrimUi() {
        removeAlls();
        if (this.mX8GimbalHorizontalTrimController != null && this.mX8GimbalHorizontalTrimController.isShow()) {
            this.mX8GimbalHorizontalTrimController.closeUi();
        }
        this.mX8GimbalHorizontalTrimController = null;
    }

    public void closeGimbalXYZAdjustUI() {
        removeAlls();
        if (this.x8GimbalXYZAdjustController != null && this.x8GimbalXYZAdjustController.isShow()) {
            this.x8GimbalXYZAdjustController.closeUi();
        }
        this.x8GimbalXYZAdjustController = null;
    }

    public void onDroneConnected(boolean b) {
        if (this.mX8GimbalHorizontalTrimController != null) {
            this.mX8GimbalHorizontalTrimController.onDroneConnected(b);
        }
        if (this.x8GimbalXYZAdjustController != null) {
            this.x8GimbalXYZAdjustController.onDroneConnected(b);
        }
        if (!b && this.mX8GimbalHorizontalTrimController != null) {
            removeAlls();
            this.mX8GimbalHorizontalTrimController.closeUi();
            this.activity.getX8MainFcAllSettingControler().showAllSettingUi(X8FcAllSettingMenuEnum.GIMBAL_ITEM);
            this.activity.showTopByGimbalHorizontalTrim();
            this.mX8GimbalHorizontalTrimController = null;
        }
    }

    public void removeAlls() {
        ((ViewGroup) this.mainShowView).removeAllViews();
    }
}
