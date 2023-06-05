package com.fimi.x8sdk.ivew;

import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.x8sdk.presenter.FiveKeyDefinePresenter;

import java.util.List;

/* loaded from: classes2.dex */
public interface IFiveKeyAction {
    void isSetCameraContrast();

    void isSetCameraSaturation();

    void restoreUpDownKey(boolean z);

    String setCameraContrast(String str, int i, FiveKeyDefinePresenter.ParameterType parameterType, JsonUiCallBackListener jsonUiCallBackListener);

    String setCameraSaturation(String str, int i, FiveKeyDefinePresenter.ParameterType parameterType, JsonUiCallBackListener jsonUiCallBackListener);

    String setFiveKeyCameraKeyParams(String str, List list, String str2, JsonUiCallBackListener jsonUiCallBackListener);
}
