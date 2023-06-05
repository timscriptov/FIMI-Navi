package com.fimi.x8sdk.presenter;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.common.Constants;
import com.fimi.x8sdk.controller.CameraManager;
import com.fimi.x8sdk.dataparser.AckFiveKeyDefine;
import com.fimi.x8sdk.ivew.IFiveKeyAction;
import com.fimi.x8sdk.listener.IX8FiveKeyDefine;

import java.util.List;


public class FiveKeyDefinePresenter extends BasePresenter implements IFiveKeyAction {
    private final int SATURATION_INDEX = 5;
    private final int CONTRASTRATIO_INDEX = 6;
    private final int CANCEL_PARAMETER_SETTING_INDEX = 9;
    private final CameraManager cameraManager;
    private boolean isFiveKeyContrastRatioAdd;
    private boolean isFiveKeySaturationAdd;
    private final IX8FiveKeyDefine ix8FiveKeyDefine;

    public FiveKeyDefinePresenter(IX8FiveKeyDefine ix8FiveKeyDefine, CameraManager cameraManager) {
        this.ix8FiveKeyDefine = ix8FiveKeyDefine;
        this.cameraManager = cameraManager;
        addNoticeListener();
    }

    @Override
    // com.fimi.x8sdk.common.BasePresenter, com.fimi.kernel.connect.interfaces.IDataCallBack
    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        super.onDataCallBack(groupId, msgId, packet);
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    public void reponseCmd(boolean isAck, int group, int msgId, ILinkMessage packet, BaseCommand bcd) {
        if (group == 11 && msgId == 16) {
            AckFiveKeyDefine ackFiveKeyDefine = (AckFiveKeyDefine) packet;
            if (this.ix8FiveKeyDefine != null) {
                if (ackFiveKeyDefine.getAdckeyIndex() == 1) {
                    if (this.isFiveKeySaturationAdd) {
                        fiveKeySwitchUI(5, ackFiveKeyDefine.getAdckeyAction() == 2, true);
                    } else if (this.isFiveKeyContrastRatioAdd) {
                        fiveKeySwitchUI(6, ackFiveKeyDefine.getAdckeyAction() == 2, true);
                    } else {
                        fiveKeySwitchUI(SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_UP_KEY), ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    }
                } else if (ackFiveKeyDefine.getAdckeyIndex() == 2) {
                    if (this.isFiveKeySaturationAdd) {
                        fiveKeySwitchUI(5, ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    } else if (this.isFiveKeyContrastRatioAdd) {
                        fiveKeySwitchUI(6, ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    } else {
                        fiveKeySwitchUI(SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_DOWN_KEY, 1), ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    }
                } else if (ackFiveKeyDefine.getAdckeyIndex() == 3) {
                    if (!this.isFiveKeySaturationAdd && !this.isFiveKeyContrastRatioAdd) {
                        fiveKeySwitchUI(SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_LEFT_KEY, 2), ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    }
                } else if (ackFiveKeyDefine.getAdckeyIndex() == 4) {
                    if (!this.isFiveKeySaturationAdd && !this.isFiveKeyContrastRatioAdd) {
                        fiveKeySwitchUI(SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_RIGHT_KEY, 3), ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    }
                } else if (ackFiveKeyDefine.getAdckeyIndex() == 5) {
                    if (!this.isFiveKeyContrastRatioAdd && !this.isFiveKeySaturationAdd) {
                        fiveKeySwitchUI(SPStoreManager.getInstance().getInt(Constants.FIVE_KEY_CENTRE_KEY, 4), ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    } else {
                        fiveKeySwitchUI(9, ackFiveKeyDefine.getAdckeyAction() == 2, false);
                    }
                }
            }
        }
    }

    private void fiveKeySwitchUI(int index, boolean isLongPress, boolean isAdd) {
        if (index == 0) {
            this.ix8FiveKeyDefine.mapFPVSwitch(isLongPress);
        } else if (index == 1) {
            this.ix8FiveKeyDefine.backCenterDownSwitch(isLongPress);
        } else if (index == 2) {
            this.ix8FiveKeyDefine.batteryFrame(isLongPress);
        } else if (index == 3) {
            this.ix8FiveKeyDefine.selfCheckFrame(isLongPress);
        } else if (index == 4) {
            this.ix8FiveKeyDefine.mediaFrame(isLongPress);
        } else if (index == 5) {
            this.ix8FiveKeyDefine.setSaturation(isLongPress, isAdd);
        } else if (index == 6) {
            this.ix8FiveKeyDefine.setContrastRatio(isLongPress, isAdd);
        } else if (index == 7) {
            this.ix8FiveKeyDefine.shootModeSwitch(isLongPress);
        } else if (index == 8) {
            this.ix8FiveKeyDefine.meteringSwitch(isLongPress);
        } else if (index == 9) {
            this.ix8FiveKeyDefine.cancelParameterSetting();
        }
    }

    public void setCameraKeyParams(String paramKey, String param, JsonUiCallBackListener jsonUiCallBackListener) {
        this.cameraManager.setCameraKeyParams(param, paramKey, jsonUiCallBackListener);
    }

    @Override
    public String setCameraContrast(String paramKey, int param, ParameterType parameterType, JsonUiCallBackListener jsonUiCallBackListener) {
        if (parameterType == ParameterType.ADD_VALUE) {
            param++;
        } else if (parameterType == ParameterType.DECREASE_VALUE) {
            param--;
        }
        if (param < 0) {
            param = 0;
        } else if (param > 256) {
            param = 256;
        }
        String currentParam = String.valueOf(param);
        setCameraKeyParams(paramKey, currentParam, jsonUiCallBackListener);
        return currentParam;
    }

    @Override
    public String setCameraSaturation(String paramKey, int param, ParameterType parameterType, JsonUiCallBackListener jsonUiCallBackListener) {
        if (parameterType == ParameterType.ADD_VALUE) {
            param++;
        } else if (parameterType == ParameterType.DECREASE_VALUE) {
            param--;
        }
        if (param < 0) {
            param = 0;
        } else if (param > 256) {
            param = 256;
        }
        String currentParam = String.valueOf(param);
        setCameraKeyParams(paramKey, currentParam, jsonUiCallBackListener);
        return currentParam;
    }

    @Override
    public String setFiveKeyCameraKeyParams(String paramKey, List list, String currentParams, JsonUiCallBackListener jsonUiCallBackListener) {
        int currentIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().equalsIgnoreCase(currentParams) && (currentIndex = i + 1) > list.size() - 1) {
                currentIndex = 0;
            }
        }
        setCameraKeyParams(paramKey, list.get(currentIndex).toString(), jsonUiCallBackListener);
        return list.get(currentIndex).toString();
    }

    @Override
    public void restoreUpDownKey(boolean isRestore) {
        this.isFiveKeySaturationAdd = isRestore;
        this.isFiveKeyContrastRatioAdd = isRestore;
    }

    @Override
    public void isSetCameraContrast() {
        this.isFiveKeyContrastRatioAdd = true;
    }

    @Override
    public void isSetCameraSaturation() {
        this.isFiveKeySaturationAdd = true;
    }


    public enum ParameterType {
        ORIGINAL_VALUE,
        ADD_VALUE,
        DECREASE_VALUE
    }
}
