package com.fimi.x8sdk.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.fimi.android.app.R;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IConnectResultListener;
import com.fimi.kernel.connect.model.UpdateDateMessage;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.connect.session.UpdateDateListener;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.milink.ByteArrayToIntArray;
import com.fimi.kernel.dataparser.usb.UiCallBackListener;
import com.fimi.kernel.utils.FileUtil;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.x8sdk.command.FwUpdateCollection;
import com.fimi.x8sdk.common.BasePresenter;
import com.fimi.x8sdk.dataparser.AckUpdateCurrentProgress;
import com.fimi.x8sdk.dataparser.AutoNotifyFwFile;
import com.fimi.x8sdk.entity.UpdateCurrentProgressEntity;
import com.fimi.x8sdk.ivew.IUpdateAction;
import com.fimi.x8sdk.ivew.IX8UpdateProgressView;
import com.fimi.x8sdk.modulestate.StateManager;
import com.fimi.x8sdk.update.fwpack.ByteHexHelper;
import com.fimi.x8sdk.update.fwpack.FirmwareBuildPack;
import com.fimi.x8sdk.update.fwpack.FwInfo;
import com.fimi.x8sdk.update.fwpack.IBuildPackInfo;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class X8UpdatePresenter extends BasePresenter implements IUpdateAction, UpdateDateListener {
    private final int EACH_PACKAGE_LEN = 1024;
    private final int CONTINUOUS_MAX_NUMBER = 16;
    private final int WHAT_UPDATE_PROGRESS = 1;
    private final int WHAT_UPDATE_FINISH = 2;
    private final int WAIT_UPDATE_TIMEOUT = 3;
    private final int UPDATE_STATE_END = 255;
    boolean startCheckUpdateTimeOut = false;
    UpdateState updateState = UpdateState.updateInit;
    volatile double offset = 0.0d;
    boolean updateFileEnd = false;
    boolean alreadyExist = false;
    int cameraConnectedState = -1;
    boolean isCameraUpdate = false;
    private int aggregateProgress;
    private final Context context;
    private List<UpdateCurrentProgressEntity> currentProgressEntityList;
    private byte[] fileBytes;
    private int fileProgress;
    private String firewareName;
    private List<FwInfo> fwInfoList;
    private int fwNumber;
    private IX8UpdateProgressView ix8UpdateProgressView;
    private int notifyProgress;
    private int residueNum;
    private int sendPackageNum;
    private int subPackageNum;
    private long total;
    private Thread updateThread;
    private int packNum = 0;
    private boolean updateFailure = false;
    private final List<FwInfo> fwInfos = new ArrayList();
    private boolean hasAccumulate = false;
    private Timer checkUpdateTimeout = new Timer();
    private boolean isLockOffset = false;
    private int callbackOffset = 0;
    private boolean waitSend = false;
    @SuppressLint({"HandlerLeak"})
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(true, msg.arg1, null, X8UpdatePresenter.this.firewareName);
            } else if (msg.what == 2) {
                X8UpdatePresenter.this.checkUpdateTimeout.cancel();
                X8UpdatePresenter.this.updateState = UpdateState.updateEnd;
                X8UpdatePresenter.this.disposeAddedData();
                if (X8UpdatePresenter.this.updateFailure) {
                    X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(false, msg.arg1, null, X8UpdatePresenter.this.firewareName);
                    for (int i = 0; i < X8UpdatePresenter.this.fwInfos.size(); i++) {
                    }
                    return;
                }
                for (int i2 = 0; i2 < X8UpdatePresenter.this.fwInfos.size() - 1; i2++) {
                    for (int j = X8UpdatePresenter.this.fwInfos.size() - 1; j > i2; j--) {
                        if (X8UpdatePresenter.this.fwInfos.get(j).getTypeId() == X8UpdatePresenter.this.fwInfos.get(i2).getTypeId() && X8UpdatePresenter.this.fwInfos.get(j).getModelId() == X8UpdatePresenter.this.fwInfos.get(i2).getModelId()) {
                            X8UpdatePresenter.this.fwInfos.remove(j);
                        }
                    }
                }
                X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(true, msg.arg1, X8UpdatePresenter.this.fwInfos, "");
            } else if (msg.what == 3) {
                X8UpdatePresenter.this.ix8UpdateProgressView.showUpdateProgress(false, msg.arg1, null, X8UpdatePresenter.this.firewareName);
            }
        }
    };
    public IConnectResultListener mIConnectResultListener = new IConnectResultListener() {
        @Override
        public void onConnected(String msg) {
            StateManager.getInstance().startUpdateTimer();
        }

        @Override
        public void onDisconnect(String msg) {
            if (!X8UpdatePresenter.this.containRemoteControl()) {
                X8UpdatePresenter.this.updateFailure = true;
                X8UpdatePresenter.this.firewareName = X8UpdatePresenter.this.context.getString(R.string.x8_update_err_disconnect);
                X8UpdatePresenter.this.updateProgressView(2, 0);
                X8UpdatePresenter.this.updateFileEnd = true;
                if (X8UpdatePresenter.this.updateThread != null) {
                    X8UpdatePresenter.this.updateThread.interrupt();
                }
            }
        }

        @Override
        public void onConnectError(String msg) {
        }

        @Override
        public void onDeviceConnect() {
        }

        @Override
        public void onDeviceDisConnnect() {
        }
    };
    private int cheackUpdateTimeOut = 0;
    private int updateTimeoutProgress = 0;
    private int updateTimeoutAddTime = 0;

    public X8UpdatePresenter(Context context) {
        this.context = context;
        SessionManager.getInstance().add2NoticeList(this.mIConnectResultListener);
        initDate();
    }

    static /* synthetic */ int access$1708(X8UpdatePresenter x0) {
        int i = x0.cheackUpdateTimeOut;
        x0.cheackUpdateTimeOut = i + 1;
        return i;
    }

    static /* synthetic */ int access$2108(X8UpdatePresenter x0) {
        int i = x0.updateTimeoutAddTime;
        x0.updateTimeoutAddTime = i + 1;
        return i;
    }

    void initDate() {
        addNoticeListener(this);
        checkUpdateOutStatus();
    }

    @Override
    public void onPersonalDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    public void onDataCallBack(int groupId, int msgId, ILinkMessage packet) {
        reponseCmd(true, groupId, msgId, packet, null);
    }

    @Override
    public void onPersonalSendTimeOut(int groupId, int msgId, BaseCommand bcd) {
        reponseCmd(false, groupId, msgId, null, bcd);
    }

    public void uploadFwFile() {
        this.fwInfos.clear();
        this.updateFileEnd = false;
        this.updateThread = new Thread(new X8UpdateRunnable());
        this.updateThread.start();
    }

    @Override
    public void reponseCmd(boolean isAck, int groupId, int msgId, ILinkMessage packet, BaseCommand bcd) {
        AckUpdateCurrentProgress ackUpdateCurrentProgress;
        int deviceNumber;
        if (groupId == 16) {
            if (msgId == 3) {
                if (this.updateState == UpdateState.requestUpdate) {
                    this.updateState = UpdateState.sendUploadInformation;
                    uploadFwFile();
                }
            } else if (msgId == 4) {
                AutoNotifyFwFile autoNotifyFwFile = (AutoNotifyFwFile) packet;
                if (autoNotifyFwFile.getNotifyType() == 0) {
                    if (autoNotifyFwFile.getResult() != 0) {
                        this.updateFailure = true;
                        this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                        updateProgressView(2, 0);
                    }
                } else if (autoNotifyFwFile.getNotifyType() == 1) {
                    if (autoNotifyFwFile.getResult() != 0) {
                        this.updateFailure = true;
                        this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                        updateProgressView(2, 0);
                    }
                } else if (autoNotifyFwFile.getNotifyType() == 2) {
                    if (autoNotifyFwFile.getResult() == 0) {
                        this.fwNumber = autoNotifyFwFile.getFwNumber();
                        this.startCheckUpdateTimeOut = true;
                        return;
                    }
                    this.updateFailure = true;
                    this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                    updateProgressView(2, 0);
                } else if (autoNotifyFwFile.getNotifyType() == 3) {
                    this.updateState = UpdateState.fileCheckResults;
                    if (50 != this.fileProgress) {
                        this.fileProgress = 50;
                    }
                    try {
                        int currentValue = autoNotifyFwFile.getSchedule() + this.aggregateProgress;
                        if (currentValue == 0) {
                            this.notifyProgress = 0;
                        } else if (this.fwNumber != 0) {
                            this.notifyProgress = (currentValue / this.fwNumber) / 2;
                        } else {
                            this.notifyProgress = 0;
                        }
                    } catch (Exception e) {
                    }
                    this.firewareName = getSysName((byte) autoNotifyFwFile.getDevModuleId(), (byte) autoNotifyFwFile.getDevTargetId());
                    updateProgressView(1, this.fileProgress + this.notifyProgress);
                    this.hasAccumulate = false;
                } else if (autoNotifyFwFile.getNotifyType() == 4) {
                    FwInfo fwInfo = new FwInfo();
                    fwInfo.setModelId((byte) autoNotifyFwFile.getDevModuleId());
                    fwInfo.setTypeId((byte) autoNotifyFwFile.getDevTargetId());
                    fwInfo.setSchedule(autoNotifyFwFile.getSchedule());
                    fwInfo.setSysName(getSysName(fwInfo.getModelId(), fwInfo.getTypeId()));
                    if (autoNotifyFwFile.getResult() == 0) {
                        this.updateFailure = false;
                        fwInfo.setUpdateResult("0");
                        fwInfo.setSoftwareVer(getSoftwareVer(fwInfo.getModelId(), fwInfo.getTypeId()));
                    } else {
                        fwInfo.setUpdateResult("1");
                        fwInfo.setErrorCode(getErrorCodeString(autoNotifyFwFile.getResult()));
                    }
                    if (!isAdd(fwInfo)) {
                        this.aggregateProgress += 100;
                        this.fwInfos.add(fwInfo);
                    }
                } else if (autoNotifyFwFile.getNotifyType() == 5) {
                    if (!this.hasAccumulate) {
                        FwInfo fwInfo2 = new FwInfo();
                        fwInfo2.setModelId((byte) autoNotifyFwFile.getDevModuleId());
                        fwInfo2.setTypeId((byte) autoNotifyFwFile.getDevTargetId());
                        fwInfo2.setSchedule(autoNotifyFwFile.getSchedule());
                        fwInfo2.setSysName(getSysName(fwInfo2.getModelId(), fwInfo2.getTypeId()));
                        fwInfo2.setUpdateResult("1");
                        if (!isAdd(fwInfo2)) {
                            this.aggregateProgress += 100;
                            this.fwInfos.add(fwInfo2);
                        }
                        this.hasAccumulate = true;
                    }
                } else if (autoNotifyFwFile.getNotifyType() == 6) {
                    if (autoNotifyFwFile.getResult() == 255 || autoNotifyFwFile.getResult() == 0) {
                        updateProgressView(2, 100);
                        return;
                    }
                    this.updateFailure = true;
                    this.firewareName = getErrorCodeString(autoNotifyFwFile.getResult());
                    updateProgressView(2, 0);
                }
            } else if (msgId == 6 && (deviceNumber = (ackUpdateCurrentProgress = (AckUpdateCurrentProgress) packet).getDeviceNumber()) != 0 && this.fwNumber == deviceNumber) {
                if (this.fileProgress == 0) {
                    this.fileProgress = 50;
                }
                this.currentProgressEntityList = ackUpdateCurrentProgress.getUpdateCurrentProgressEntitys();
                for (int i = 0; i < this.currentProgressEntityList.size(); i++) {
                    if ((containCamera(this.currentProgressEntityList.get(i).getDevModuleID(), this.currentProgressEntityList.get(i).getDevTargetID()) || containRemoteControl()) && (this.fileProgress + this.notifyProgress == 100 || this.currentProgressEntityList.get(this.currentProgressEntityList.size() - 1).getSchedule() == 100)) {
                        this.handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                X8UpdatePresenter.this.updateFailure = false;
                                X8UpdatePresenter.this.updateProgressView(2, 100);
                            }
                        }, 2500L);
                    }
                }
                this.hasAccumulate = true;
            }
        }
    }

    public void disposeAddedData() {
        if (this.currentProgressEntityList != null) {
            for (int i = 0; i < this.currentProgressEntityList.size(); i++) {
                FwInfo fwInfo = new FwInfo();
                fwInfo.setModelId((byte) this.currentProgressEntityList.get(i).getDevModuleID());
                fwInfo.setTypeId((byte) this.currentProgressEntityList.get(i).getDevTargetID());
                fwInfo.setSchedule(this.currentProgressEntityList.get(i).getSchedule());
                fwInfo.setSysName(getSysName(fwInfo.getModelId(), fwInfo.getTypeId()));
                if (this.currentProgressEntityList.get(i).getSchedule() == 100) {
                    if (this.currentProgressEntityList.get(i).getResult() == 0) {
                        fwInfo.setUpdateResult("0");
                        fwInfo.setSoftwareVer(getSoftwareVer(fwInfo.getModelId(), fwInfo.getTypeId()));
                        if (!isAdd(fwInfo)) {
                            this.fwInfos.add(fwInfo);
                        }
                    } else {
                        fwInfo.setUpdateResult("1");
                        if (!isAdd(fwInfo)) {
                            this.fwInfos.add(fwInfo);
                        }
                    }
                } else {
                    fwInfo.setUpdateResult("1");
                    if (!isAdd(fwInfo)) {
                        this.aggregateProgress += 100;
                        this.fwInfos.add(fwInfo);
                    }
                }
            }
        }
    }

    public void requestStartUpdate(UiCallBackListener callBackListener) {
        FwUpdateCollection fwUpdateCollection = new FwUpdateCollection(this, callBackListener);
        BaseCommand cmd = fwUpdateCollection.requestStartUpdate();
        sendCmd(cmd);
    }

    public void requestUploadFile() {
        if (this.fileBytes != null) {
            int crc = ByteArrayToIntArray.CRC32Software(this.fileBytes, this.fileBytes.length);
            byte[] byteCrc = ByteHexHelper.intToFourHexBytes(crc);
            byte[] fileSize = ByteHexHelper.intToFourHexBytes(this.fileBytes.length);
            BaseCommand cmd = new FwUpdateCollection().requestUploadFile(fileSize, byteCrc);
            sendCmd(cmd);
        }
    }

    @Override
    public void queryCurUpdateStatus(UiCallBackListener callBackListener) {
        BaseCommand command = new FwUpdateCollection().queryCurUpdateStatus();
        sendCmd(command);
    }

    @Override
    public void firmwareBuildPack(final List<FwInfo> fwInfoList) {
        this.fwInfoList = fwInfoList;
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                IBuildPackInfo pack = new FirmwareBuildPack(new FirmwareBuildPack.MergFileListener() {
                    @Override
                    public void mergResult(int result) {
                        X8UpdatePresenter.this.fileBytes = FileUtil.getFileBytes(FirmwareBuildPack.PKG_UPDATE_FILE);
                        X8UpdatePresenter.this.residueNum = X8UpdatePresenter.this.fileBytes.length % 1024;
                        X8UpdatePresenter.this.subPackageNum = X8UpdatePresenter.this.fileBytes.length / 1024;
                        if (X8UpdatePresenter.this.residueNum == 0) {
                            X8UpdatePresenter.this.packNum = X8UpdatePresenter.this.subPackageNum;
                        } else {
                            X8UpdatePresenter.this.packNum = X8UpdatePresenter.this.subPackageNum + 1;
                        }
                        X8UpdatePresenter.this.requestUploadFile();
                        X8UpdatePresenter.this.updateState = UpdateState.requestUpdate;
                    }
                }, fwInfoList);
                pack.createUpdatePkg();
            }
        });
    }

    private String getSysName(byte modelId, byte typeId) {
        String sysName = "";
        if (this.fwInfoList == null) {
            return "";
        }
        for (FwInfo fwInfo : this.fwInfoList) {
            if (fwInfo.getModelId() == modelId && fwInfo.getTypeId() == typeId) {
                sysName = fwInfo.getSysName();
            }
        }
        return sysName;
    }

    private short getSoftwareVer(byte modelId, byte typeId) {
        short softwareVer = 0;
        if (this.fwInfoList == null) {
            return (short) 0;
        }
        for (FwInfo fwInfo : this.fwInfoList) {
            if (fwInfo.getModelId() == modelId && fwInfo.getTypeId() == typeId) {
                softwareVer = fwInfo.getSoftwareVer();
            }
        }
        return softwareVer;
    }

    public boolean containRemoteControl() {
        boolean tempSteta = false;
        if (this.fwInfoList == null) {
            return false;
        }
        for (FwInfo fwInfo : this.fwInfoList) {
            if (fwInfo.getModelId() == 3 && fwInfo.getTypeId() == 1) {
                return true;
            }
            tempSteta = false;
        }
        return tempSteta;
    }

    private boolean containCamera(int modelID, int typeID) {
        return modelID == 2 && typeID == 4;
    }

    @Override
    public void setOnUpdateProgress(IX8UpdateProgressView ix8UpdateProgressView) {
        this.ix8UpdateProgressView = ix8UpdateProgressView;
    }

    @Override
    public void onUpdateDateCallBack(UpdateDateMessage updateDateMessage) {
        this.callbackOffset = updateDateMessage.getFileOffset();
        try {
            Thread.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.waitSend = false;
        if (this.updateState == UpdateState.updateFile) {
            if (this.callbackOffset == 0) {
                this.isLockOffset = true;
                this.offset = 0.0d;
            } else if (this.total == this.callbackOffset) {
                this.fileProgress = 50;
                updateProgressView(1, this.fileProgress);
                this.updateFileEnd = true;
                this.updateThread.interrupt();
            } else if (this.callbackOffset >= 1024 && this.total != this.callbackOffset) {
                this.isLockOffset = true;
                this.offset = this.callbackOffset - 1024;
            }
        }
    }

    public void updateProgressView(int what, int arg1) {
        if (this.updateState != UpdateState.updateEnd) {
            Message msg = new Message();
            msg.what = what;
            if (100 < arg1) {
                arg1 = 100;
            }
            msg.arg1 = arg1;
            if (msg.arg1 >= 0) {
                this.handler.sendMessage(msg);
            }
        }
    }

    public void checkUpdateOutStatus() {
        if (this.checkUpdateTimeout == null) {
            this.checkUpdateTimeout = new Timer();
        }
        this.checkUpdateTimeout.schedule(new TimerTask() {
            @Override
            public void run() {
                if (X8UpdatePresenter.this.updateState != UpdateState.updateEnd) {
                    if (X8UpdatePresenter.this.startCheckUpdateTimeOut) {
                        X8UpdatePresenter.this.queryCurUpdateStatus(null);
                        X8UpdatePresenter.access$1708(X8UpdatePresenter.this);
                        if (X8UpdatePresenter.this.cheackUpdateTimeOut > 960) {
                            X8UpdatePresenter.this.updateFailure = true;
                            X8UpdatePresenter.this.firewareName = X8UpdatePresenter.this.context.getString(R.string.x8_error_code_update_25);
                            X8UpdatePresenter.this.updateProgressView(2, 0);
                            X8UpdatePresenter.this.startCheckUpdateTimeOut = false;
                        }
                    }
                    if (X8UpdatePresenter.this.fileProgress + X8UpdatePresenter.this.notifyProgress != X8UpdatePresenter.this.updateTimeoutProgress) {
                        X8UpdatePresenter.this.updateTimeoutProgress = X8UpdatePresenter.this.fileProgress + X8UpdatePresenter.this.notifyProgress;
                        X8UpdatePresenter.this.updateTimeoutAddTime = 0;
                        return;
                    }
                    X8UpdatePresenter.access$2108(X8UpdatePresenter.this);
                    if (X8UpdatePresenter.this.updateTimeoutAddTime > 180) {
                        X8UpdatePresenter.this.updateFailure = true;
                        X8UpdatePresenter.this.firewareName = X8UpdatePresenter.this.context.getString(R.string.x8_error_code_update_25);
                        X8UpdatePresenter.this.updateProgressView(2, 0);
                    }
                }
            }
        }, 0L, 1000L);
    }

    private boolean isAdd(FwInfo fwInfo) {
        for (FwInfo info : this.fwInfos) {
            this.alreadyExist = info.getModelId() == fwInfo.getModelId() && info.getTypeId() == fwInfo.getTypeId();
        }
        return this.alreadyExist;
    }

    public String getErrorCodeString(int result) {
        switch ((byte) result) {
            case -1:
                String str = this.context.getString(R.string.x8_error_code_update_255);
                return str;
            case 0:
                String str2 = this.context.getString(R.string.x8_error_code_update_0);
                return str2;
            case 1:
                String str3 = this.context.getString(R.string.x8_error_code_update_1);
                return str3;
            case 2:
                String str4 = this.context.getString(R.string.x8_error_code_update_2);
                return str4;
            case 3:
                String str5 = this.context.getString(R.string.x8_error_code_update_3);
                return str5;
            case 4:
                String str6 = this.context.getString(R.string.x8_error_code_update_4);
                return str6;
            case 5:
                String str7 = this.context.getString(R.string.x8_error_code_update_5);
                return str7;
            case 6:
                String str8 = this.context.getString(R.string.x8_error_code_update_6);
                return str8;
            case 7:
                String str9 = this.context.getString(R.string.x8_error_code_update_7);
                return str9;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            default:
                return "";
            case 33:
                String str10 = this.context.getString(R.string.x8_error_code_update_21);
                return str10;
            case 34:
                String str11 = this.context.getString(R.string.x8_error_code_update_22);
                return str11;
            case 35:
                String str12 = this.context.getString(R.string.x8_error_code_update_23);
                return str12;
            case 36:
                String str13 = this.context.getString(R.string.x8_error_code_update_24);
                return str13;
            case 37:
                String str14 = this.context.getString(R.string.x8_error_code_update_25);
                return str14;
            case 38:
                String str15 = this.context.getString(R.string.x8_error_code_update_26);
                return str15;
            case 39:
                String str16 = this.context.getString(R.string.x8_error_code_update_27);
                return str16;
            case 40:
                String str17 = this.context.getString(R.string.x8_error_code_update_28);
                return str17;
            case 41:
                String str18 = this.context.getString(R.string.x8_error_code_update_29);
                return str18;
        }
    }

    @Override
    public void removeNoticeList() {
        SessionManager.getInstance().removeNoticeList(this.mIConnectResultListener);
        removeNoticeListener();
    }


    public enum UpdateState {
        updateInit,
        requestUpdate,
        sendUploadInformation,
        updateFile,
        fileCheckResults,
        updateEnd
    }


    public class X8UpdateRunnable implements Runnable {
        private X8UpdateRunnable() {
        }

        @Override
        public void run() {
            try {
                X8UpdatePresenter.this.offset = 0.0d;
                RandomAccessFile randomFile = new RandomAccessFile(new File(FirmwareBuildPack.PKG_UPDATE_FILE), "r");
                X8UpdatePresenter.this.total = (int) randomFile.length();
                int packageNum = (int) (X8UpdatePresenter.this.total / 1024);
                int packageMod = (int) (X8UpdatePresenter.this.total % 1024);
                X8UpdatePresenter.this.updateState = UpdateState.updateFile;
                while (!X8UpdatePresenter.this.updateFileEnd && !Thread.interrupted()) {
                    if (!X8UpdatePresenter.this.waitSend) {
                        if (X8UpdatePresenter.this.offset <= X8UpdatePresenter.this.total) {
                            if (X8UpdatePresenter.this.offset / 1024.0d != packageNum) {
                                randomFile.seek((long) X8UpdatePresenter.this.offset);
                                byte[] bytes = new byte[1024];
                                randomFile.read(bytes, 0, 1024);
                                X8UpdatePresenter.this.sendCmd(new FwUpdateCollection().sendFwFileContent((int) X8UpdatePresenter.this.offset, bytes));
                                if (X8UpdatePresenter.this.isLockOffset) {
                                    X8UpdatePresenter.this.isLockOffset = false;
                                } else {
                                    X8UpdatePresenter.this.offset += 1024.0d;
                                    if ((X8UpdatePresenter.this.offset * 50.0d) / X8UpdatePresenter.this.total >= X8UpdatePresenter.this.fileProgress) {
                                        X8UpdatePresenter.this.fileProgress = (int) ((X8UpdatePresenter.this.offset * 50.0d) / X8UpdatePresenter.this.total);
                                        X8UpdatePresenter.this.updateProgressView(1, X8UpdatePresenter.this.fileProgress);
                                    }
                                }
                            } else {
                                byte[] bytes2 = new byte[packageMod];
                                randomFile.seek(packageNum * 1024);
                                randomFile.read(bytes2, 0, packageMod);
                                X8UpdatePresenter.this.sendCmd(new FwUpdateCollection().sendFwFileContent((int) X8UpdatePresenter.this.offset, bytes2));
                                if (X8UpdatePresenter.this.isLockOffset) {
                                    X8UpdatePresenter.this.isLockOffset = false;
                                } else {
                                    X8UpdatePresenter.this.offset += packageMod;
                                    if ((X8UpdatePresenter.this.offset * 50.0d) / X8UpdatePresenter.this.total >= X8UpdatePresenter.this.fileProgress) {
                                        X8UpdatePresenter.this.fileProgress = (int) ((X8UpdatePresenter.this.offset * 50.0d) / X8UpdatePresenter.this.total);
                                        X8UpdatePresenter.this.updateProgressView(1, X8UpdatePresenter.this.fileProgress);
                                    }
                                }
                            }
                            Thread.sleep(2L);
                        } else {
                            byte[] bytes22 = new byte[packageMod];
                            randomFile.seek(packageNum * 1024);
                            randomFile.read(bytes22, 0, packageMod);
                            X8UpdatePresenter.this.sendCmd(new FwUpdateCollection().sendFwFileContent((int) X8UpdatePresenter.this.offset, bytes22));
                            Thread.sleep(5L);
                        }
                    } else {
                        return;
                    }
                }
                if (randomFile != null) {
                    randomFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                X8UpdatePresenter.this.updateThread.interrupt();
            }
        }
    }
}
