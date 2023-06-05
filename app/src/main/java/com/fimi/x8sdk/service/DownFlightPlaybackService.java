package com.fimi.x8sdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDownloadListener;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.ZipTool;
import com.fimi.network.DownFwService;
import com.fimi.network.DownNoticeMananger;
import com.fimi.network.DownloadManager;
import com.fimi.network.IDownProgress;
import com.fimi.network.entity.X8PlaybackLogEntity;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.util.X8FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes2.dex */
public class DownFlightPlaybackService extends Service {
    private static final int PROGRESS_MAX = 100;
    public static volatile int checkingTaskCount = 0;
    static DownFwService.DownState state = DownFwService.DownState.UnStart;
    List<X8PlaybackLogEntity> needDownDto;
    DownloadManager downloadManager = new DownloadManager();
    private String filePath = "";

    public static DownFwService.DownState getState() {
        return state;
    }

    public static void setState(DownFwService.DownState state2) {
        state = state2;
        DisposeDataHandle.isStop = state2 == DownFwService.DownState.StopDown;
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }    public DisposeDataHandle dataHandle = new DisposeDataHandle(new DisposeDownloadListener() { // from class: com.fimi.x8sdk.service.DownFlightPlaybackService.1
        @Override // com.fimi.kernel.network.okhttp.listener.DisposeDownloadListener
        public void onProgress(int progrss, int currentLength) {
            int pro = ((DownFlightPlaybackService.checkingTaskCount * 100) + progrss) / DownFlightPlaybackService.this.needDownDto.size();
            if (progrss == 100) {
                if (DownFlightPlaybackService.checkingTaskCount < DownFlightPlaybackService.this.needDownDto.size()) {
                    String unZipFileName = DirectoryPath.getX8LoginFlightPlaybackPath("") + "/" + DownFlightPlaybackService.this.needDownDto.get(DownFlightPlaybackService.checkingTaskCount).getFlieName().replace(".zip", "");
                    File unZipFile = new File(unZipFileName);
                    if (unZipFile.exists()) {
                        unZipFile.delete();
                    }
                    int zipState = ZipTool.unzip(DirectoryPath.getX8LoginFlightPlaybackPath("") + "/" + DownFlightPlaybackService.this.needDownDto.get(DownFlightPlaybackService.checkingTaskCount).getFlieName(), unZipFileName);
                    if (zipState == 3) {
                        List<File> list = X8FileHelper.flightLogListDirs(unZipFileName);
                        for (File file : list) {
                            DownFlightPlaybackService.this.filePath = file.getAbsolutePath();
                            if (DownFlightPlaybackService.this.filePath.contains(X8FcLogManager.getInstance().prexCollect)) {
                                DownFlightPlaybackService.this.filePath = DownFlightPlaybackService.this.filePath.replace(X8FcLogManager.getInstance().prexCollect, "");
                            }
                            DownFlightPlaybackService.this.filePath = DownFlightPlaybackService.this.filePath.replace(".", X8FcLogManager.prexSD + ".");
                            File tempFile = new File(DownFlightPlaybackService.this.filePath);
                            file.renameTo(tempFile);
                        }
                    }
                } else {
                    return;
                }
            } else {
                DownFlightPlaybackService.state = DownFwService.DownState.Downing;
            }
            if (pro == 100) {
                DownFlightPlaybackService.state = DownFwService.DownState.Finish;
            }
            if (DisposeDataHandle.isStop) {
                DownFlightPlaybackService.state = DownFwService.DownState.StopDown;
            }
            DownFlightPlaybackService.this.reportProgress(DownFlightPlaybackService.state, pro, "");
            if (progrss == 100) {
                DownFlightPlaybackService.this.startCheckingTask();
                DownFlightPlaybackService.this.downFirmware();
            }
        }

        @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
        public void onSuccess(Object responseObj) {
            if (DownFlightPlaybackService.this.needDownDto == null || DownFlightPlaybackService.this.needDownDto.size() < 1 || DownFlightPlaybackService.checkingTaskCount == DownFlightPlaybackService.this.needDownDto.size()) {
                DownFlightPlaybackService.state = DownFwService.DownState.Finish;
            }
            int pro = (DownFlightPlaybackService.checkingTaskCount * 100) / DownFlightPlaybackService.this.needDownDto.size();
            DownFlightPlaybackService.this.reportProgress(DownFlightPlaybackService.state, pro, "");
        }

        @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
        public void onFailure(Object reasonObj) {
            int pro = (DownFlightPlaybackService.checkingTaskCount * 100) / DownFlightPlaybackService.this.needDownDto.size();
            DownFlightPlaybackService.state = DownFwService.DownState.DownFail;
            DownFlightPlaybackService.this.reportProgress(DownFlightPlaybackService.state, pro, "");
            DownFlightPlaybackService.this.startCheckingTask();
            DownFlightPlaybackService.this.downFirmware();
        }
    });

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            DisposeDataHandle.isStop = false;
            this.needDownDto = (ArrayList) intent.getSerializableExtra("listDownloadFlightPlayback");
            state = DownFwService.DownState.Start;
            downFirmware();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public synchronized void startCheckingTask() {
        checkingTaskCount++;
    }

    public void downFirmware() {
        if (this.needDownDto != null && this.needDownDto.size() >= 1 && checkingTaskCount < this.needDownDto.size()) {
            this.downloadManager.downLoadFlightPlayback(this.needDownDto.get(checkingTaskCount), this.dataHandle);
        }
    }

    public void reportProgress(DownFwService.DownState state2, int progress, String name) {
        CopyOnWriteArrayList<IDownProgress> list;
        if (!state.equals(DownFwService.DownState.StopDown) && (list = DownNoticeMananger.getDownNoticManger().getNoticeList()) != null && list.size() > 0) {
            Iterator<IDownProgress> it = list.iterator();
            while (it.hasNext()) {
                IDownProgress i = it.next();
                i.onProgress(state2, progress, name);
            }
        }
    }




}
