package com.fimi.x8sdk.entity;

import android.support.v4.media.session.PlaybackStateCompat;

import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.fds.FdsUploadTask;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.presenter.X8FlightLogListPresenter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

/* loaded from: classes2.dex */
public class X8FlightLogFile extends X8FdsFile {
    private long dateSecond;
    private FdsUploadTask fdsUploadTask;
    private File file;
    private String fileName;
    private boolean isUpload;
    private String nameShow;
    private File playbackFile;
    private String playbackFileName;
    private String showLen;
    private Future<?> taskFutrue;
    private File zipFile;
    private String fileLogCollectState = "1";
    private String flightMileage = "0.0";
    private int flightDuration = 0;
    private String[] zipFileSuffix = {X8FcLogManager.FLIGHT_PLAYBACK};

    public String getShowLen() {
        return this.showLen;
    }

    public void setShowLen(String showLen) {
        this.showLen = showLen;
    }

    public boolean isUpload() {
        return this.isUpload;
    }

    public void setUpload(boolean upload) {
        this.isUpload = upload;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public Future<?> getTaskFutrue() {
        return this.taskFutrue;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setTaskFutrue(Future<?> taskFutrue) {
        this.taskFutrue = taskFutrue;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void stopTask() {
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public String getObjectName() {
        return this.objectName;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public String getFileFdsUrl() {
        return this.filefdsUrl;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setFileFdsUrl(String fileUrl) {
        this.filefdsUrl = fileUrl;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public String getFileSuffix() {
        return this.fileSuffix;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public String getFileSuffixCollect() {
        return this.fileSuffixCollect;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setFileSuffixCollect(String fileSuffixCollect) {
        this.fileSuffixCollect = fileSuffixCollect;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public String[] getNeedZipFileBySuffix() {
        return this.zipFileSuffix;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public FdsUploadTask getRunable() {
        return this.fdsUploadTask;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setRunable(FdsUploadTask fdsUploadTask) {
        this.fdsUploadTask = fdsUploadTask;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public File getZipFile() {
        return this.zipFile;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setZipFile(File f) {
        this.zipFile = f;
    }

    public String getNameShow() {
        return this.nameShow;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public FdsUploadState getState() {
        return this.state;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setState(FdsUploadState state) {
        this.state = state;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void resetFile(File file) {
        this.file = file;
    }

    public String calculationLen() {
        long fileS = this.playbackFile.length();
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileS < 1024) {
            if (fileS == 0) {
                return "0B";
            }
            String size = fileS + "B";
            return size;
        } else if (fileS < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            String size2 = (fileS / 1024) + "K";
            return size2;
        } else if (fileS < 1073741824) {
            String size3 = df.format(fileS / 1048576.0d) + "M";
            return size3;
        } else {
            String size4 = df.format(fileS / 1.073741824E9d) + "G";
            return size4;
        }
    }

    public String getFileLogCollectState() {
        return this.fileLogCollectState;
    }

    public void setFileLogCollectState(String fileLogCollectState) {
        this.fileLogCollectState = fileLogCollectState;
    }

    public boolean isFileLogCollect() {
        return this.fileLogCollectState.equalsIgnoreCase("0");
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public String getFlightMileage() {
        return this.flightMileage;
    }

    public void setFlightMileage(String flightMileage) {
        this.flightMileage = flightMileage;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public int getFlightDuration() {
        return this.flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public File getPlaybackFile() {
        return this.playbackFile;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void resetPlaybackFile(File file) {
        this.playbackFile = file;
    }

    public boolean setPlaybackFile(File playbackFile, boolean uploadSuccessfulAll) {
        this.playbackFile = playbackFile;
        this.playbackFileName = playbackFile.getName();
        boolean ret = false;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Date d = formatter.parse(this.playbackFileName);
            this.dateSecond = d.getTime();
            SimpleDateFormat format = new SimpleDateFormat(DateUtil.dateFormatYMDHM);
            this.nameShow = format.format(d);
            if (uploadSuccessfulAll) {
                for (String fileName : X8FlightLogListPresenter.fileNames) {
                    if (this.playbackFileName.contains(fileName.replace(".playback", ""))) {
                        this.isUpload = true;
                        ret = true;
                        setState(FdsUploadState.SUCCESS);
                    } else if (this.playbackFileName.contains(X8FcLogManager.prexSD)) {
                        this.isUpload = true;
                        ret = true;
                        setState(FdsUploadState.SUCCESS);
                    }
                }
            } else if (this.playbackFileName.contains(X8FcLogManager.prexSD)) {
                this.isUpload = true;
                ret = true;
                setState(FdsUploadState.SUCCESS);
            }
            if (this.playbackFileName.contains(X8FcLogManager.getInstance().prexCollect)) {
                setFileLogCollectState("0");
            }
            setFileSuffix(X8FcLogManager.prexSD);
            setFileSuffixCollect(X8FcLogManager.getInstance().prexCollect);
            setShowLen(calculationLen());
        } catch (Exception e) {
            this.nameShow = this.playbackFileName;
        }
        return ret;
    }

    public long getDateSecond() {
        return this.dateSecond / 1000;
    }
}
