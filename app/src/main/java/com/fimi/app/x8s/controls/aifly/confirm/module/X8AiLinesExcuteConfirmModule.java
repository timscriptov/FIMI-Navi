package com.fimi.app.x8s.controls.aifly.confirm.module;

import android.app.Activity;
import android.view.View;

import com.fimi.app.x8s.controls.X8MapVideoController;
import com.fimi.app.x8s.controls.aifly.X8AiLineExcuteController;
import com.fimi.app.x8s.controls.aifly.confirm.ui.X8AiLinesExcuteConfirmUi;
import com.fimi.app.x8s.entity.X8AilinePrameter;
import com.fimi.app.x8s.interfaces.IX8NextViewListener;
import com.fimi.app.x8s.map.model.MapPointLatLng;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.controller.FcManager;

import java.util.List;

/* loaded from: classes.dex */
public class X8AiLinesExcuteConfirmModule extends X8BaseModule {
    private X8AiLinesExcuteConfirmUi mUi;

    public void init(Activity activity, View rootView, CameraManager cameraManager) {
        this.mUi = new X8AiLinesExcuteConfirmUi(activity, rootView, cameraManager);
    }

    @Override // com.fimi.app.x8s.controls.aifly.confirm.module.X8BaseModule
    public void setFcHeart(boolean isInSky, boolean isLowPower) {
        this.mUi.setFcHeart(isInSky, isLowPower);
    }

    public void setListener(IX8NextViewListener listener, FcManager fcManager, X8MapVideoController mapVideoController, X8AilinePrameter mX8AilinePrameter, X8AiLineExcuteController mX8AiLineExcuteController) {
        this.mUi.setListener(listener, fcManager, mapVideoController, mX8AilinePrameter, mX8AiLineExcuteController);
    }

    public void setPointSizeAndDistance(int aiLinePointSize, float aiLineDistance, List<MapPointLatLng> mapPointList, X8AiLineExcuteController.LineModel model) {
        this.mUi.setPointSizeAndDistance(aiLinePointSize, aiLineDistance, mapPointList, model);
    }

    public void setAiLineExcuteMode(int aiLineMode) {
        this.mUi.setAiLineMode(aiLineMode);
    }

    public void setDataByHistory(X8AiLinePointInfo info) {
        this.mUi.setDataByHistory(info);
    }

    public void setDataByHistory(long lineId) {
        this.mUi.setDataByHistory(lineId);
    }
}
