package com.fimi.album.biz;

import android.os.Handler;
import android.os.Message;

import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IDateHandler;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.kernel.utils.DateFormater;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Gh2FolderDispater<T extends MediaModel> implements IHandlerCallback {
    public static final int PHOTO = 2;
    public static final String TAG = Gh2FolderDispater.class.getName();
    public static final int VIDEO = 1;
    public boolean isHadForEachFolder;
    private IDateHandler mIDateHandler;
    private final SuffixUtils mSuffixUtils = SuffixUtils.obtain();
    private final String defaultFormatPattern = "yyyy.MM.dd HH:mm:ss";
    private long videoCount = 0;
    private long photoCount = 0;
    private final Handler otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private final CopyOnWriteArrayList<T> localDataList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<T> localDataNoHeadList = new CopyOnWriteArrayList<>();
    private final LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHash = new LinkedHashMap<>();

    public void forEachFolder(String folderPath) {
        if (!this.isHadForEachFolder && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 3;
            mMessage.obj = folderPath;
            this.otherHandler.sendMessage(mMessage);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == 3) {
            reallyHandlerFolderFile((String) message.obj);
            return true;
        }
        return true;
    }

    public void reallyHandlerFolderFile(String path) {
        List<T> cacheList = new ArrayList<>();
        File floder = new File(path);
        if (floder.exists()) {
            LinkedList<File> list = new LinkedList<>();
            File[] files = floder.listFiles();
            this.videoCount = 0L;
            this.photoCount = 0L;
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file);
                } else {
                    String filePath = file.getAbsolutePath();
                    MediaModel mMediaModel = new MediaModel();
                    mMediaModel.setFileSize(file.length());
                    mMediaModel.setCreateDate(file.lastModified());
                    mMediaModel.setFormatDate(DateFormater.dateString(file.lastModified(), this.defaultFormatPattern));
                    mMediaModel.setName(file.getName());
                    mMediaModel.setFileLocalPath(filePath);
                    if (filePath.contains(".")) {
                        if (this.mSuffixUtils.judgeFileType(filePath)) {
                            mMediaModel.setVideo(true);
                            this.videoCount++;
                        } else {
                            mMediaModel.setVideo(false);
                            this.photoCount++;
                        }
                        cacheList.add((T) mMediaModel);
                    }
                }
            }
            while (!list.isEmpty()) {
                File temp_file = list.removeFirst();
                File[] files2 = temp_file.listFiles();
                for (File file2 : files2) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    } else {
                        String filePath2 = file2.getAbsolutePath();
                        MediaModel mMediaModel2 = new MediaModel();
                        mMediaModel2.setFileSize(file2.length());
                        mMediaModel2.setCreateDate(file2.lastModified());
                        mMediaModel2.setFormatDate(DateFormater.dateString(file2.lastModified(), this.defaultFormatPattern));
                        mMediaModel2.setName(file2.getName());
                        mMediaModel2.setFileLocalPath(filePath2);
                        mMediaModel2.setVideo(this.mSuffixUtils.judgeFileType(filePath2));
                        cacheList.add((T) mMediaModel2);
                    }
                }
            }
            Collections.sort(cacheList, DateComparator.createDateComparator());
            this.localDataNoHeadList.addAll(cacheList);
            addHeadModelBean(cacheList, this.localDataList, this.dataHash);
            if (this.mIDateHandler != null) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolder = true;
            }
        } else if (this.mIDateHandler != null) {
            this.mIDateHandler.loadDateComplete(false, false);
        }
    }

    private void addHeadModelBean(List<T> cacheList, CopyOnWriteArrayList<T> saveList, HashMap<String, CopyOnWriteArrayList<T>> saveHash) {
        String cacheFormateDate = null;
        CopyOnWriteArrayList copyOnWriteArrayList = null;
        boolean isOneTime = false;
        if (cacheList.size() > 0) {
            MediaModel headViewModel = new MediaModel();
            headViewModel.setHeadView(true);
            saveList.add((T) headViewModel);
        }
        for (T t : cacheList) {
            String lastModifyDate = t.getFormatDate().split(" ")[0];
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

    public void reDefaultList() {
        this.videoCount = 0L;
        this.photoCount = 0L;
        this.localDataList.clear();
        this.localDataNoHeadList.clear();
        this.dataHash.clear();
        this.isHadForEachFolder = false;
    }

    public boolean isHadForEachFolder() {
        return this.isHadForEachFolder;
    }

    public void setHadForEachFolder(boolean hadForEachFolder) {
        this.isHadForEachFolder = hadForEachFolder;
    }

    public CopyOnWriteArrayList<T> getLocalDataList() {
        return this.localDataList;
    }

    public CopyOnWriteArrayList<T> getLocalDataNoHeadList() {
        return this.localDataNoHeadList;
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHash() {
        return this.dataHash;
    }

    public void setmIDateHandler(IDateHandler mIDateHandler) {
        this.mIDateHandler = mIDateHandler;
    }

    public long getVideoCount() {
        return this.videoCount;
    }

    public void setVideoCount(long videoCount) {
        this.videoCount = videoCount;
    }

    public long getPhotoCount() {
        return this.photoCount;
    }

    public void setPhotoCount(long photoCount) {
        this.photoCount = photoCount;
    }

    public void removeCallBack() {
    }
}
