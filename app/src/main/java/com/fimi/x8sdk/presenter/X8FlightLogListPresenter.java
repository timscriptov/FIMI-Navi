package com.fimi.x8sdk.presenter;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.fimi.kernel.fds.FdsCount;
import com.fimi.kernel.fds.FdsManager;
import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.fds.IFdsCountListener;
import com.fimi.kernel.fds.IFdsFileModel;
import com.fimi.kernel.fds.IFdsUiListener;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.network.DownFwService;
import com.fimi.network.DownNoticeMananger;
import com.fimi.network.IDownProgress;
import com.fimi.network.entity.NetModel;
import com.fimi.network.entity.X8PlaybackLogEntity;
import com.fimi.x8sdk.controller.X8FlightPlayBackManager;
import com.fimi.x8sdk.entity.X8FlightLogFile;
import com.fimi.x8sdk.ivew.IX8FlightLogListAction;
import com.fimi.x8sdk.service.DownFlightPlaybackService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class X8FlightLogListPresenter implements IDownProgress, IFdsCountListener, IFdsUiListener {
    public static List<String> fileNames = new ArrayList();
    private final Context context;
    private FdsCount fdsCount;
    private boolean isShow;
    private final IX8FlightLogListAction ix8FlightLogListAction;
    private volatile float lastProgrss;
    private volatile int progrssSum;
    private long time;
    private List<X8PlaybackLogEntity> x8PlaybackLogEntityList;
    private final List<X8FlightLogFile> x8FlightLogFiles = new ArrayList();
    private final List<X8FlightLogFile> x8FlightLogFilesTemp = new ArrayList();
    private final List<X8FlightLogFile> x8FlightLogFilesUpload = new ArrayList();
    private final List<X8PlaybackLogEntity> x8PlaybackLogEntitiesDownload = new ArrayList();
    private boolean isUpDownload = false;
    private volatile float uploadProgrss = 0.0f;

    public X8FlightLogListPresenter(Context context, IX8FlightLogListAction ix8FlightLogListAction) {
        this.context = context;
        this.ix8FlightLogListAction = ix8FlightLogListAction;
        FdsManager.getInstance().setFdsCountListener(this);
        FdsManager.getInstance().setUiListener(this);
        DownNoticeMananger.getDownNoticManger().addDownNoticeList(this);
    }

    public void synFlightPlaybackData(int synType) {
        this.isShow = false;
        fileNames.clear();
        String startTime = "";
        if (synType == 0) {
            this.time = (System.currentTimeMillis() / 1000) - 604800;
            startTime = DateUtil.getStringByFormat(this.time * 1000, "yyyy-MM-dd-HH-mm-ss");
        } else if (synType == 1) {
            this.time = (System.currentTimeMillis() / 1000) - 2592000;
            startTime = DateUtil.getStringByFormat(this.time * 1000, "yyyy-MM-dd-HH-mm-ss");
        } else if (synType == 2) {
            this.time = (System.currentTimeMillis() / 1000) - 15552000;
            startTime = DateUtil.getStringByFormat(this.time * 1000, "yyyy-MM-dd-HH-mm-ss");
        } else if (synType == 3) {
            this.time = -1L;
            startTime = "2018-01-01-00-00-00";
        }
        String endTime = DateUtil.getStringByFormat(System.currentTimeMillis(), "yyyy-MM-dd-HH-mm-ss");
        X8FlightPlayBackManager.getX8FlightPlayBackManagerInstans().getX8FlightPlaybackLog(startTime, endTime, new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                NetModel netModel = JSON.parseObject(responseObj.toString(), NetModel.class);
                if (netModel.isSuccess()) {
                    if (netModel.getData() != null) {
                        X8FlightLogListPresenter.this.x8PlaybackLogEntitiesDownload.clear();
                        X8FlightLogListPresenter.this.x8FlightLogFilesUpload.clear();
                        X8FlightLogListPresenter.this.isUpDownload = false;
                        X8FlightLogListPresenter.this.x8PlaybackLogEntityList = JSON.parseArray(netModel.getData().toString(), X8PlaybackLogEntity.class);
                        Iterator<X8FlightLogFile> x8FlightLogFileIterator = X8FlightLogListPresenter.this.x8FlightLogFiles.iterator();
                        while (x8FlightLogFileIterator.hasNext()) {
                            X8FlightLogFile x8FlightFile = x8FlightLogFileIterator.next();
                            if (X8FlightLogListPresenter.this.time == -1) {
                                if (!x8FlightFile.isUpload()) {
                                    for (X8PlaybackLogEntity x8PlaybackLogEntity : X8FlightLogListPresenter.this.x8PlaybackLogEntityList) {
                                        if (x8PlaybackLogEntity.getLogFileUrl().contains(x8FlightFile.getFileName())) {
                                            try {
                                                x8FlightLogFileIterator.remove();
                                            } catch (IllegalStateException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    x8FlightLogFileIterator.remove();
                                }
                            } else if (!x8FlightFile.isUpload() && !X8FlightLogListPresenter.this.isCollectAndTime(x8FlightFile)) {
                            } else {
                                x8FlightLogFileIterator.remove();
                            }
                        }
                        Iterator<X8PlaybackLogEntity> x8PlaybackLogEntityIterator = X8FlightLogListPresenter.this.x8PlaybackLogEntityList.iterator();
                        while (x8PlaybackLogEntityIterator.hasNext()) {
                            X8PlaybackLogEntity x8PlaybackLogEntity2 = x8PlaybackLogEntityIterator.next();
                            for (X8FlightLogFile x8FlightFile2 : X8FlightLogListPresenter.this.x8FlightLogFilesTemp) {
                                if (x8PlaybackLogEntity2.getLogFileUrl().contains(x8FlightFile2.getFileName())) {
                                    try {
                                        x8PlaybackLogEntityIterator.remove();
                                    } catch (IllegalStateException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    X8FlightLogListPresenter.this.x8FlightLogFilesUpload.addAll(X8FlightLogListPresenter.this.x8FlightLogFiles);
                    X8FlightLogListPresenter.this.x8PlaybackLogEntitiesDownload.addAll(X8FlightLogListPresenter.this.x8PlaybackLogEntityList);
                    X8FlightLogListPresenter.this.isUpDownload = X8FlightLogListPresenter.this.x8FlightLogFilesUpload.size() > 0 && X8FlightLogListPresenter.this.x8PlaybackLogEntitiesDownload.size() > 0;
                    if (X8FlightLogListPresenter.this.x8PlaybackLogEntitiesDownload.size() > 0) {
                        X8FlightLogListPresenter.this.ix8FlightLogListAction.startSyn();
                        DownFlightPlaybackService.checkingTaskCount = 0;
                        Intent intent = new Intent(X8FlightLogListPresenter.this.context, DownFlightPlaybackService.class);
                        intent.putExtra("listDownloadFlightPlayback", (Serializable) X8FlightLogListPresenter.this.x8PlaybackLogEntitiesDownload);
                        X8FlightLogListPresenter.this.context.startService(intent);
                        return;
                    }
                    X8FlightLogListPresenter.this.uploadFlightLogFiles();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        }));
    }


    public void uploadFlightLogFiles() {
        if (this.x8FlightLogFilesUpload == null || this.x8FlightLogFilesUpload.size() <= 0) {
            this.ix8FlightLogListAction.noDataHint();
            return;
        }
        if (!this.isUpDownload) {
            this.ix8FlightLogListAction.startSyn();
        }
        int listSize = this.x8FlightLogFilesUpload.size();
        int position = 1;
        for (int i = 0; i < listSize; i++) {
            X8FlightLogFile x8FlightLogFile = this.x8FlightLogFilesUpload.get(i);
            if (x8FlightLogFile.getState() == FdsUploadState.IDLE || x8FlightLogFile.getState() == FdsUploadState.STOP || x8FlightLogFile.getState() == FdsUploadState.FAILED) {
                x8FlightLogFile.setSectionPostion(i);
                x8FlightLogFile.setItemPostion(position);
                FdsManager.getInstance().startDownload(x8FlightLogFile);
            }
            position++;
        }
    }

    @Override
    public void onProgress(DownFwService.DownState downState, int progrss, String fileName) {
        if (progrss >= 100) {
            if (this.isUpDownload) {
                progrss /= 2;
                uploadFlightLogFiles();
            } else {
                synCompleteRefresh(true);
            }
            this.ix8FlightLogListAction.synTotalProgress(progrss);
            return;
        }
        if (this.isUpDownload) {
            progrss /= 2;
        }
        this.ix8FlightLogListAction.synTotalProgress(progrss);
    }

    @Override
    public void onProgress(IFdsFileModel model, int progrss) {
        if (this.x8FlightLogFilesUpload != null && this.x8FlightLogFilesUpload.size() > 0) {
            if (progrss > 0) {
                if (progrss >= 100) {
                    this.progrssSum++;
                    this.uploadProgrss = 0.0f;
                    fileNames.add(model.getPlaybackFile().getName());
                } else {
                    float deviv = this.x8FlightLogFilesUpload.size() * 100 * 1.0f;
                    this.uploadProgrss = progrss / deviv;
                }
            }
            if (this.isUpDownload) {
                this.lastProgrss = (((this.progrssSum / (this.x8FlightLogFilesUpload.size() * 1.0f)) + this.uploadProgrss) * 50.0f) + 50.0f;
            } else {
                this.lastProgrss = ((this.progrssSum / (this.x8FlightLogFilesUpload.size() * 1.0f)) + this.uploadProgrss) * 100.0f;
            }
            if (this.lastProgrss > 100.0f) {
                this.lastProgrss = 100.0f;
            }
            this.ix8FlightLogListAction.synTotalProgress((int) this.lastProgrss);
        }
    }

    @Override
    public void onSuccess(IFdsFileModel responseObj) {
        this.fdsCount.completeIncrease();
        FdsManager.getInstance().remove(responseObj);
        synCompleteRefresh(true);
    }

    @Override
    public void onFailure(IFdsFileModel responseObj) {
        FdsManager.getInstance().remove(responseObj);
        synCompleteRefresh(false);
    }

    @Override
    public void onStop(IFdsFileModel reasonObj) {
        FdsManager.getInstance().remove(reasonObj);
    }

    @Override
    public void onUploadingCountChange(int uploading) {
        if (this.fdsCount.getTotal() - this.fdsCount.getComplete() == uploading) {
            if (uploading == 0) {
                this.fdsCount.setState(2);
                return;
            } else {
                this.fdsCount.setState(1);
                return;
            }
        }
        this.fdsCount.setState(0);
    }

    public void remioveDownNoticeList() {
        DownNoticeMananger.getDownNoticManger().remioveDownNoticeListAll();
    }

    public void setX8FlightLogFiles(List<X8FlightLogFile> x8FlightLogFiles) {
        this.x8FlightLogFiles.clear();
        this.x8FlightLogFilesTemp.clear();
        if (x8FlightLogFiles != null) {
            this.x8FlightLogFiles.addAll(x8FlightLogFiles);
            this.x8FlightLogFilesTemp.addAll(x8FlightLogFiles);
        }
    }

    public void setFdsCount(FdsCount fdsCount) {
        this.fdsCount = fdsCount;
    }

    private void synCompleteRefresh(boolean isSucceed) {
        if (!this.isShow) {
            this.isShow = true;
            this.ix8FlightLogListAction.synCompleteRefreshUI(isSucceed);
        }
    }

    public boolean isCollectAndTime(X8FlightLogFile x8FlightFile) {
        return x8FlightFile.getDateSecond() <= this.time && !x8FlightFile.isFileLogCollect();
    }
}
