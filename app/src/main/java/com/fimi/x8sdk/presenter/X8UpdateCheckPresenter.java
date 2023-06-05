package com.fimi.x8sdk.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.fimi.android.app.R;
import com.fimi.host.HostConstants;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.CmdResult;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.command.FwUpdateCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckUpdateRequest;
import com.fimi.x8sdk.dataparser.AckUpdateSystemStatus;
import com.fimi.x8sdk.dataparser.AutoCameraStateADV;
import com.fimi.x8sdk.ivew.IUpdateCheckAction;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.update.UpdateUtil;

import java.util.Timer;
import java.util.TimerTask;


public class X8UpdateCheckPresenter extends BasePresenter {
    private static final int CHECK_UPDATE = 1;
    private static final int CHECK_UPDATE_ERR = 2;
    private AckUpdateRequest ackUpdateRequest;
    private AckUpdateSystemStatus ackUpdateSystemStatus;
    private Context context;
    private IUpdateCheckAction iUpdateCheckAction;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                X8UpdateCheckPresenter.this.iUpdateCheckAction.showIsUpdate(msg.arg1 == 0, msg.arg1);
            } else if (msg.what == 1) {
                X8UpdateCheckPresenter.this.iUpdateCheckAction.checkUpdate();
            }
            super.handleMessage(msg);
        }
    };
    private boolean haveLockMotor = false;
    private Timer checkTimer = new Timer();
    private UpdateCheckState updateCheckState = UpdateCheckState.updateInit;
    private final byte[] updateStates = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    public X8UpdateCheckPresenter() {
        addNoticeListener();
        intCheckUpdateStatus();
    }

    public void setIUpdateCheckAction(Context context, IUpdateCheckAction iUpdateCheckAction) {
        this.context = context;
        this.iUpdateCheckAction = iUpdateCheckAction;
    }

    public void intCheckUpdateStatus() {
        if (this.checkTimer == null) {
            this.checkTimer = new Timer();
        }
        this.checkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (X8UpdateCheckPresenter.this.updateCheckState != UpdateCheckState.updating) {
                    X8UpdateCheckPresenter.this.queryCurSystemStatus();
                    X8UpdateCheckPresenter.this.checkUpdateVersion();
                }
            }
        }, 0L, 2000L);
    }

    @Override
    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    public void reponseCmd(boolean isAck, int group, int msgId, ILinkMessage packet, BaseCommand bcd) {
        super.reponseCmd(isAck, group, msgId, packet, bcd);
        if (group == 16) {
            if (msgId == 5) {
                if (this.updateCheckState == UpdateCheckState.updateInit) {
                    this.ackUpdateSystemStatus = (AckUpdateSystemStatus) packet;
                    checkCameraState();
                }
            } else if (msgId == 2) {
                this.ackUpdateRequest = (AckUpdateRequest) packet;
                if (this.ackUpdateRequest != null) {
                    int isCheckUpdate = SPStoreManager.getInstance().getInt(HostConstants.SP_KEY_UPDATE_CHECK, 2);
                    if (isCheckUpdate == 2 || UpdateUtil.isForceUpdate()) {
                        this.handler.sendEmptyMessage(1);
                    }
                }
            }
        }
    }

    private void checkCameraState() {
        int status = this.ackUpdateSystemStatus.getStatus();
        if (status == 0) {
            if (this.haveLockMotor) {
                setPresenterLockMotor(0);
            }
            this.updateCheckState = UpdateCheckState.readyUpgrade;
            requestStartUpdate(null);
        } else if (checkUpdatingState(status)) {
            if (!this.haveLockMotor) {
                setPresenterLockMotor(1);
            }
            this.updateCheckState = UpdateCheckState.updating;
        } else {
            this.updateCheckState = UpdateCheckState.upgradeEnd;
        }
    }

    public void checkUpdateVersion() {
        AutoCameraStateADV stateADV = StateManager.getInstance().getCamera().getAutoCameraStateADV();
        int resId = 0;
        if (!StateManager.getInstance().getX8Drone().isConnect()) {
            resId = R.string.x8_update_err_connect;
        } else if (StateManager.getInstance().getX8Drone().isInSky()) {
            resId = R.string.x8_update_err_insky;
        } else if (StateManager.getInstance().getCamera().getToken() <= 0) {
            resId = R.string.x8_update_err_a12ununited;
        } else if (this.updateCheckState == UpdateCheckState.updating) {
            resId = R.string.x8_update_err_updating;
            this.updateCheckState = UpdateCheckState.upgradeEnd;
        } else if (stateADV != null && stateADV.getInfo() == 3) {
            resId = R.string.x8_error_code_update_3;
        } else if (this.ackUpdateRequest != null && !this.ackUpdateRequest.isResultSucceed()) {
            resId = UpdateUtil.getErrorCodeString(this.context, this.ackUpdateRequest.getMsgRpt());
        }
        Message msg = new Message();
        msg.what = 2;
        msg.arg1 = resId;
        this.handler.sendMessage(msg);
    }

    public void requestStartUpdate(UiCallBackListener callBackListener) {
        FwUpdateCollection fwUpdateCollection = new FwUpdateCollection(this, callBackListener);
        BaseCommand cmd = fwUpdateCollection.requestStartUpdate();
        sendCmd(cmd);
    }

    public void queryCurSystemStatus() {
        this.updateCheckState = UpdateCheckState.updateInit;
        BaseCommand command = new FwUpdateCollection().queryCurSystemStatus();
        sendCmd(command);
    }

    public void setPresenterLockMotor(final int lock) {
        FcCollection fcCollection = new FcCollection(this, new UiCallBackListener() {
            @Override
            public void onComplete(CmdResult cmdResult, Object o) {
                if (cmdResult.isSuccess()) {
                    X8UpdateCheckPresenter.this.haveLockMotor = lock != 0;
                }
            }
        });
        BaseCommand baseCommand = fcCollection.setLockMotor(lock);
        sendCmd(baseCommand);
    }

    private boolean checkUpdatingState(int updateState) {
        boolean isUpdateState = false;
        for (int i = 0; i < this.updateStates.length; i++) {
            if (updateState == this.updateStates[i]) {
                return true;
            }
            isUpdateState = false;
        }
        return isUpdateState;
    }


    public enum UpdateCheckState {
        updateInit,
        readyUpgrade,
        updating,
        upgradeEnd,
        notUpgrade
    }
}
