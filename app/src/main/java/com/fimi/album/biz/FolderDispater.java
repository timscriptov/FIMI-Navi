package com.fimi.album.biz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IDateHandler;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.DateFormater;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class FolderDispater<T extends MediaModel> implements IHandlerCallback {
    public static final String ORIGINAL_PATH = "original_path";
    public static final String TAG = FolderDispater.class.getName();
    public static final String THUMBNAIL_PATH = "thumbnail_path";
    private final SuffixUtils mSuffixUtils = SuffixUtils.obtain();
    private final String defaultFormatPattern = "yyyy.MM.dd HH:mm:ss";
    private final Handler otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
    private final CopyOnWriteArrayList<T> localDataList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<T> localPhotoDataList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<T> localVideoDataList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<T> localDataNoHeadList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<T> localPhotoDataNoHeadList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<T> localVideoDataNoHeadList = new CopyOnWriteArrayList<>();
    private final LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHash = new LinkedHashMap<>();
    private final LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHashPhoto = new LinkedHashMap<>();
    private final LinkedHashMap<String, CopyOnWriteArrayList<T>> dataHashVideo = new LinkedHashMap<>();
    public boolean isHadForEachFolder;
    public boolean isHadForEachFolderPhoto;
    public boolean isHadForEachFolderVideo;
    private IDateHandler mIDateHandler;
    private long videoCount = 0;
    private long photoCount = 0;

    public void forEachFolder(String folderPath) {
        if (!this.isHadForEachFolder && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 3;
            mMessage.obj = folderPath;
            this.otherHandler.sendMessage(mMessage);
        }
    }

    public void forEachFolderPhoto(String folderPath, String thumbnailPath) {
        if (!this.isHadForEachFolderPhoto && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 12;
            Bundle bundle = new Bundle();
            bundle.putString(ORIGINAL_PATH, folderPath);
            bundle.putString(THUMBNAIL_PATH, thumbnailPath);
            mMessage.setData(bundle);
            this.otherHandler.sendMessage(mMessage);
        }
    }

    public void forEachFolderVideo(String folderPath, String thumbnailPath) {
        if (!this.isHadForEachFolderVideo && !this.otherHandler.hasMessages(3)) {
            Message mMessage = new Message();
            mMessage.what = 13;
            Bundle bundle = new Bundle();
            bundle.putString(ORIGINAL_PATH, folderPath);
            bundle.putString(THUMBNAIL_PATH, thumbnailPath);
            mMessage.setData(bundle);
            this.otherHandler.sendMessage(mMessage);
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == 3) {
            this.photoCount = 0L;
            this.videoCount = 0L;
            reallyHandlerFolderFile((String) message.obj);
            return true;
        } else if (message.what == 12) {
            this.photoCount = 0L;
            Bundle bundle = message.getData();
            reallyHandlerFolderFile(bundle.getString(ORIGINAL_PATH), bundle.getString(THUMBNAIL_PATH), State.PHOTO);
            return true;
        } else if (message.what == 13) {
            this.videoCount = 0L;
            Bundle bundle2 = message.getData();
            reallyHandlerFolderFile(bundle2.getString(ORIGINAL_PATH), bundle2.getString(THUMBNAIL_PATH), State.VIDEO);
            return true;
        } else {
            return true;
        }
    }

    public void reallyHandlerFolderFile(String path) {
        this.localDataList.clear();
        List<T> cacheList = new ArrayList<>();
        List<T> cacheListPhoto = new ArrayList<>();
        List<T> cacheListVideo = new ArrayList<>();
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
                            cacheListVideo.add((T) mMediaModel);
                            mMediaModel.setDownLoadOriginalFile(true);
                            mMediaModel.setThumLocalFilePath(filePath.replace("orgin", "thum").replace(this.mSuffixUtils.fileFormatThm, this.mSuffixUtils.fileFormatJpg));
                        } else {
                            mMediaModel.setVideo(false);
                            this.photoCount++;
                            cacheListPhoto.add((T) mMediaModel);
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
            Collections.sort(cacheListPhoto, DateComparator.createDateComparator());
            this.localPhotoDataNoHeadList.addAll(cacheListPhoto);
            addHeadModelBean(cacheListPhoto, this.localPhotoDataList, this.dataHashPhoto);
            Collections.sort(cacheListVideo, DateComparator.createDateComparator());
            this.localVideoDataNoHeadList.addAll(cacheListVideo);
            addHeadModelBean(cacheListVideo, this.localVideoDataList, this.dataHashVideo);
            if (this.mIDateHandler != null) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolder = true;
            }
        } else if (this.mIDateHandler != null) {
            this.mIDateHandler.loadDateComplete(false, false);
        }
    }

    public void reallyHandlerFolderFile(String path, String thumbnailPath, State state) {
        List<T> cacheList = new ArrayList<>();
        List<T> cacheListPhoto = new ArrayList<>();
        List<T> cacheListVideo = new ArrayList<>();
        File floder = new File(path);
        if (floder.exists()) {
            LinkedList<File> list = new LinkedList<>();
            File[] files = floder.listFiles();
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
                    mMediaModel.setDownLoadOriginalFile(true);
                    if (filePath.contains(".")) {
                        if (this.mSuffixUtils.judgeFileType(filePath)) {
                            mMediaModel.setVideo(true);
                            if (mMediaModel.getName().contains("delay_mode")) {
                                mMediaModel.setVideoType(MediaModel.recordType.delay_record);
                            } else if (mMediaModel.getName().contains("dynamic_mode")) {
                                mMediaModel.setVideoType(MediaModel.recordType.dynamic_delay_record);
                            }
                            String thumbnailVedeoPath = thumbnailPath + file.getName().replace(Constants.VIDEO_FILE_SUFFIX, ".jpg");
                            File thumbnailFile = new File(thumbnailVedeoPath.replace("//", "/").replace("////", "/"));
                            if (thumbnailFile.exists()) {
                                mMediaModel.setThumLocalFilePath(thumbnailVedeoPath);
                            }
                            if (state != State.PHOTO) {
                                this.videoCount++;
                            }
                            cacheListVideo.add((T) mMediaModel);
                        } else {
                            mMediaModel.setVideo(false);
                            if (state != State.VIDEO) {
                                this.photoCount++;
                            }
                            cacheListPhoto.add((T) mMediaModel);
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
                        if (this.mSuffixUtils.judgeFileType(filePath2)) {
                            mMediaModel2.setVideo(true);
                            String thumbnailVedeoPath2 = thumbnailPath + file2.getName().replace(Constants.VIDEO_FILE_SUFFIX, ".jpg");
                            File thumbnailFile2 = new File(thumbnailVedeoPath2.replace("//", "/").replace("////", "/"));
                            if (thumbnailFile2.exists()) {
                                mMediaModel2.setThumLocalFilePath(thumbnailVedeoPath2);
                            }
                            this.videoCount++;
                            cacheListVideo.add((T) mMediaModel2);
                        } else {
                            mMediaModel2.setVideo(false);
                            this.photoCount++;
                            cacheListPhoto.add((T) mMediaModel2);
                        }
                        cacheList.add((T) mMediaModel2);
                    }
                }
            }
            if (state == State.ALL) {
                Collections.sort(cacheList, DateComparator.createDateComparator());
                this.localDataNoHeadList.addAll(cacheList);
                addHeadModelBean(cacheList, this.localDataList, this.dataHash);
            } else if (state == State.PHOTO) {
                Collections.sort(cacheListPhoto, DateComparator.createDateComparator());
                this.localPhotoDataNoHeadList.addAll(cacheListPhoto);
                addHeadModelBean(cacheListPhoto, this.localPhotoDataList, this.dataHashPhoto);
            } else {
                Collections.sort(cacheListVideo, DateComparator.createDateComparator());
                this.localVideoDataNoHeadList.addAll(cacheListVideo);
                addHeadModelBean(cacheListVideo, this.localVideoDataList, this.dataHashVideo);
            }
            if (this.mIDateHandler != null && state == State.ALL) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolder = true;
            }
            if (this.mIDateHandler != null && state == State.PHOTO) {
                this.mIDateHandler.loadDateComplete(true, true);
                this.isHadForEachFolderPhoto = true;
            }
            if (this.mIDateHandler != null && state == State.VIDEO) {
                this.mIDateHandler.loadDateComplete(false, true);
                this.isHadForEachFolderVideo = true;
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

    public void rePhotoDefaultList() {
        this.photoCount = 0L;
        this.localPhotoDataList.clear();
        this.localPhotoDataNoHeadList.clear();
        this.dataHashPhoto.clear();
        this.isHadForEachFolderPhoto = false;
    }

    public void reVideoDefaultList() {
        this.videoCount = 0L;
        this.localVideoDataList.clear();
        this.localVideoDataNoHeadList.clear();
        this.dataHashVideo.clear();
        this.isHadForEachFolderVideo = false;
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

    public CopyOnWriteArrayList<T> getLocalPhotoDataList() {
        return this.localPhotoDataList;
    }

    public CopyOnWriteArrayList<T> getLocalVideoDataList() {
        return this.localVideoDataList;
    }

    public CopyOnWriteArrayList<T> getLocalPhotoDataNoHeadList() {
        return this.localPhotoDataNoHeadList;
    }

    public CopyOnWriteArrayList<T> getLocalVideoDataNoHeadList() {
        return this.localVideoDataNoHeadList;
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHashPhoto() {
        return this.dataHashPhoto;
    }

    public LinkedHashMap<String, CopyOnWriteArrayList<T>> getDataHashVideo() {
        return this.dataHashVideo;
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


    public enum State {
        ALL,
        PHOTO,
        VIDEO
    }
}
