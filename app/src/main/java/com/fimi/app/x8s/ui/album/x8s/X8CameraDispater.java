package com.fimi.app.x8s.ui.album.x8s;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fimi.album.MediaLoadProxy;
import com.fimi.album.biz.DateComparator;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.interfaces.ICameraDeviceDispater;
import com.fimi.album.iview.IDateHandler;
import com.fimi.album.iview.IHandlerCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class X8CameraDispater<T extends MediaModel> implements IHandlerCallback, OnX8MediaFileListener, ICameraDeviceDispater {
    private IDateHandler mIDateHandler;
    private final MediaLoadProxy mMediaLoadProxy = new MediaLoadProxy();
    private boolean isLoadCompleteSuccess = false;
    private boolean isLoading = false;
    private long videoCount = 0;
    private long photoCount = 0;
    private final Handler otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private final CopyOnWriteArrayList<T> cameraDataList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<T> cameraDataNoHeadList = new CopyOnWriteArrayList<>();
    private final LinkedHashMap<String, CopyOnWriteArrayList<T>> cameraDateHash = new LinkedHashMap<>();

    @Override
    public void forCameraFolder() {
        if (!this.isLoading && !this.otherHandler.hasMessages(10)) {
            this.isLoading = true;
            Message mMessage = new Message();
            mMessage.what = 10;
            this.otherHandler.sendMessage(mMessage);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        if (!this.isLoadCompleteSuccess && message.what == 10) {
            reallyCameraFolderFile();
        }
        Log.i("mediax9", "handleMessage: ");
        return true;
    }

    private void reallyCameraFolderFile() {
        X8MediaFileLoad mX8Loader = new X8MediaFileLoad(this, this.cameraDataNoHeadList);
        this.mMediaLoadProxy.setMediaLoad(mX8Loader);
        this.mMediaLoadProxy.startLoad();
    }


    private void addHeadModelBean(List<T> cacheList, CopyOnWriteArrayList<T> saveList, HashMap<String, CopyOnWriteArrayList<T>> saveHash) {
        String cacheFormateDate = null;
        CopyOnWriteArrayList copyOnWriteArrayList = null;
        boolean isOneTime = false;
        MediaModel headViewModel = new MediaModel();
        headViewModel.setHeadView(true);
        saveList.add((T) headViewModel);
        for (T t : cacheList) {
            String lastModifyDate = t.getFormatDate().split(" ")[0];
            Log.i("moweiru", "lastModifyDate===" + lastModifyDate);
            if (!lastModifyDate.equals(cacheFormateDate)) {
                if (copyOnWriteArrayList != null) {
                    saveHash.put(cacheFormateDate, copyOnWriteArrayList);
                    saveList.addAll(copyOnWriteArrayList);
                    isOneTime = false;
                }
                copyOnWriteArrayList = new CopyOnWriteArrayList();
                MediaModel headModel = new MediaModel();
                headModel.setFormatDate(lastModifyDate);
                headModel.setCategory(true);
                copyOnWriteArrayList.add(headModel);
                cacheFormateDate = lastModifyDate;
                if (!isOneTime) {
                    isOneTime = true;
                }
            }
            copyOnWriteArrayList.add(t);
        }
        if (isOneTime && copyOnWriteArrayList != null) {
            saveHash.put(cacheFormateDate, copyOnWriteArrayList);
            saveList.addAll(copyOnWriteArrayList);
        }
    }

    @Override
    public CopyOnWriteArrayList<T> getCameraDataList() {
        return this.cameraDataList;
    }

    @Override
    public CopyOnWriteArrayList<T> getCameraDataNoHeadList() {
        return this.cameraDataNoHeadList;
    }

    @Override
    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getCameraDateHash() {
        return this.cameraDateHash;
    }

    @Override
    public void reDefaultList() {
        this.isLoading = false;
        this.isLoadCompleteSuccess = false;
        this.videoCount = 0L;
        this.photoCount = 0L;
        this.cameraDataList.clear();
        this.cameraDataNoHeadList.clear();
        this.cameraDateHash.clear();
    }

    @Override
    public void onComplete(boolean success) {
        if (success && this.cameraDataNoHeadList != null && this.cameraDataNoHeadList.size() > 0) {
            List<T> cacheList = new ArrayList<>();
            cacheList.addAll(this.cameraDataNoHeadList);
            this.videoCount = 0L;
            this.photoCount = 0L;
            Iterator<T> it = this.cameraDataNoHeadList.iterator();
            while (it.hasNext()) {
                MediaModel mediaModel = it.next();
                if (mediaModel.isVideo()) {
                    this.videoCount++;
                } else {
                    this.photoCount++;
                }
            }
            Collections.sort(cacheList, DateComparator.createDateComparator());
            this.cameraDataNoHeadList.clear();
            this.cameraDataNoHeadList.addAll(cacheList);
            addHeadModelBean(cacheList, this.cameraDataList, this.cameraDateHash);
            if (this.mIDateHandler != null) {
                this.mIDateHandler.loadDateComplete(true, true);
                this.isLoadCompleteSuccess = true;
            }
        } else if (this.mIDateHandler != null) {
            this.mIDateHandler.loadDateComplete(true, false);
        }
        this.isLoading = false;
    }

    @Override
    public void setmIDateHandler(IDateHandler mIDateHandler) {
        this.mIDateHandler = mIDateHandler;
    }

    @Override
    public long getVideoCount() {
        return this.videoCount;
    }

    @Override
    public void setVideoCount(long videoCount) {
        this.videoCount = videoCount;
    }

    @Override
    public long getPhotoCount() {
        return this.photoCount;
    }

    @Override
    public void setPhotoCount(long photoCount) {
        this.photoCount = photoCount;
    }

    @Override
    public boolean isLoadCompleteSuccess() {
        return this.isLoadCompleteSuccess;
    }

    @Override
    public void setLoadCompleteSuccess(boolean loadCompleteSuccess) {
        this.isLoadCompleteSuccess = loadCompleteSuccess;
    }
}
