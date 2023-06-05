package com.fimi.kernel.fds;

import java.io.File;
import java.util.concurrent.Future;


public interface IFdsFileModel {
    File getFile();

    String getFileFdsUrl();

    void setFileFdsUrl(String str);

    String getFileSuffix();

    void setFileSuffix(String str);

    String getFileSuffixCollect();

    void setFileSuffixCollect(String str);

    int getFlightDuration();

    String getFlightMileage();

    String[] getNeedZipFileBySuffix();

    String getObjectName();

    void setObjectName(String str);

    File getPlaybackFile();

    FdsUploadTask getRunable();

    void setRunable(FdsUploadTask fdsUploadTask);

    FdsUploadState getState();

    void setState(FdsUploadState fdsUploadState);

    Future<?> getTaskFutrue();

    void setTaskFutrue(Future<?> future);

    File getZipFile();

    void setZipFile(File file);

    void resetFile(File file);

    void resetPlaybackFile(File file);

    void stopTask();
}
