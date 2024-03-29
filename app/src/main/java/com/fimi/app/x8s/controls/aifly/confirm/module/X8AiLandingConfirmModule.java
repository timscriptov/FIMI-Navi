package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;

import com.fimi.app.x8s.controls.X8MainAiFlyController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLandingConfirmUi;


public class X8AiLandingConfirmModule extends X8BaseModule {
    private X8AiLandingConfirmUi mUi;

    @Override
    public void init(Activity activity, View rootView) {
        this.mUi = new X8AiLandingConfirmUi(activity, rootView);
    }

    @Override
    public void setFcHeart(boolean isInSky, boolean isLopower) {
    }

    public void setListener(X8MainAiFlyController mX8MainAiFlyController) {
        this.mUi.setX8MainAiFlyController(mX8MainAiFlyController);
    }
}
