package com.fimi.app.x8s.entity;

import android.support.v4.media.session.PlaybackStateCompat;
import android.view.animation.Animation;

import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.fds.FdsUploadTask;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.entity.X8FdsFile;
import com.fimi.x8sdk.util.X8FileHelper;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public class X8B2oxFile extends X8FdsFile {
    private FdsUploadTask fdsUploadTask;
    private File file;
    private String fileName;
    private boolean isUpload;
    private String nameShow;
    private Animation operatingAnim;
    private String showLen;
    private Future<?> taskFutrue;
    private File zipFile;
    private String[] zipFileSuffix = {X8FcLogManager.prexFC, X8FcLogManager.prexCM, X8FcLogManager.prexAPP, X8FcLogManager.prexFcStatus};

    public void setZipFileSuffix(String[] zipFileSuffix) {
        this.zipFileSuffix = zipFileSuffix;
    }

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
        return "";
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setFileSuffixCollect(String suffix) {
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public File getPlaybackFile() {
        return null;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public String getFlightMileage() {
        return null;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public int getFlightDuration() {
        return 0;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void resetPlaybackFile(File file) {
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

    public void setNameShow(String nameShow) {
        this.nameShow = nameShow;
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

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public FdsUploadState getState() {
        return this.state;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void setState(FdsUploadState state) {
        this.state = state;
    }

    public boolean setFile(File file, String prx) {
        this.file = file;
        this.fileName = file.getName();
        boolean ret = false;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
            Date d = formatter.parse(this.fileName);
            SimpleDateFormat format = new SimpleDateFormat(DateUtil.dateFormatHMS);
            this.nameShow = format.format(d);
            if (this.fileName.endsWith(X8FcLogManager.prexSD)) {
                this.isUpload = true;
                ret = true;
                setState(FdsUploadState.SUCCESS);
            }
            setFileSuffix(X8FcLogManager.prexSD);
            setShowLen(calculationLen());
        } catch (Exception e) {
            this.nameShow = this.fileName;
        }
        return ret;
    }

    @Override // com.fimi.kernel.fds.IFdsFileModel
    public void resetFile(File file) {
        this.file = file;
    }

    public String calculationLen() {
        long fileS = X8FileHelper.getDirSize(this.file);
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileS < 1024) {
            if (fileS == 0) {
                return "0.00B";
            }
            String size = df.format(fileS) + "B";
            return size;
        } else if (fileS < PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            String size2 = df.format(fileS / 1024.0d) + "K";
            return size2;
        } else if (fileS < 1073741824) {
            String size3 = df.format(fileS / 1048576.0d) + "M";
            return size3;
        } else {
            String size4 = df.format(fileS / 1.073741824E9d) + "G";
            return size4;
        }
    }

    public String getFilePrexName() {
        String tmp = this.fileName.substring(0, this.fileName.lastIndexOf("."));
        return tmp;
    }
}
