package com.fimi.app.x8s.ui.presenter;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.fimi.album.adapter.MediaDetailViewPaperAdapter;
import com.fimi.album.adapter.MediaDetialViewHolder;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.FrescoControllerListener;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.interfaces.OnDownloadUiListener;
import com.fimi.album.iview.IViewpaper;
import com.fimi.android.app.R;
import com.fimi.app.x8s.ui.album.x8s.FmMediaPlayer;
import com.fimi.app.x8s.ui.album.x8s.X8FimiPlayerActivity;
import com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity;
import com.fimi.app.x8s.ui.album.x8s.X8MediaFileDownloadManager;
import com.fimi.app.x8s.widget.videoview.X8CustomVideoView;
import com.fimi.app.x8s.widget.videoview.X8FmMediaInfo;
import com.fimi.host.HostLogBack;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.utils.FileTool;
import com.fimi.kernel.utils.FrescoUtils;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.x8sdk.controller.CameraManager;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import me.relex.photodraweeview.OnPhotoTapListener;

/* loaded from: classes.dex */
public class X8MediaDetailPresenter<T extends X8MediaDetailActivity> implements IViewpaper, OnPhotoTapListener {
    protected String perfix = "file://";
    private int defaultDisplayHeight;
    private int defaultDisplayWidth;
    private ViewGroup mCacheContainer;
    private int mCurrentPosition;
    private X8MediaDetailActivity mMediaActivity;
    private MediaModel mediaModel;
    private CopyOnWriteArrayList<? extends MediaModel> modelList;
    private MediaDetialViewHolder viewHolder;
    private ViewPager viewPaper;
    private WeakReference<T> weakReference;
    private DataManager<MediaModel> mDataManager = DataManager.obtain();
    private IPersonalDataCallBack personalDataCallBack = new IPersonalDataCallBack() { // from class: com.fimi.app.x8s.ui.presenter.X8MediaDetailPresenter.3
        @Override // com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
        public void onPersonalDataCallBack(int groupId, int cmdId, ILinkMessage packet) {
            LogUtil.i("media", "onPersonalDataCallBack: ");
        }

        @Override // com.fimi.kernel.connect.interfaces.IPersonalDataCallBack
        public void onPersonalSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
            LogUtil.i("media", "onPersonalSendTimeOut: ");
        }
    };
    private OnDownloadUiListener mOnOriginalDownloadUiListener = new OnDownloadUiListener() { // from class: com.fimi.app.x8s.ui.presenter.X8MediaDetailPresenter.4
        @Override // com.fimi.album.interfaces.OnDownloadUiListener
        public void onProgress(MediaModel model, int progrss) {
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                model.setProgress(progrss);
                if (X8MediaDetailPresenter.this.mMediaActivity.topBarShowing()) {
                    X8MediaDetailPresenter.this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                    X8MediaDetailPresenter.this.mMediaActivity.getRlDownloadBottom().setVisibility(0);
                } else {
                    X8MediaDetailPresenter.this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                    X8MediaDetailPresenter.this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                }
                X8MediaDetailPresenter.this.mMediaActivity.getBtnStart().setText(R.string.media_detail_cancle);
                X8MediaDetailPresenter.this.mMediaActivity.getTvPercent().setText(model.getProgress() + "%");
                X8MediaDetailPresenter.this.mMediaActivity.getMediaDownloadProgressView().setCurrentCount(model.getProgress());
            }
        }

        @Override // com.fimi.album.interfaces.OnDownloadUiListener
        public void onSuccess(MediaModel model) {
            Intent intent = new Intent();
            intent.setAction(X8LocalFragmentPresenter.UPDATELOCALITEMRECEIVER);
            intent.putExtra(X8LocalFragmentPresenter.UPDATELOCALITEM, model.m7clone());
            LocalBroadcastManager.getInstance(X8MediaDetailPresenter.this.mMediaActivity).sendBroadcast(intent);
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                model.setProgress(0);
                X8MediaDetailPresenter.this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                X8MediaDetailPresenter.this.mMediaActivity.getRlSelectBottom().setVisibility(0);
                X8MediaDetailPresenter.this.mMediaActivity.getRlDownload().setVisibility(8);
                X8MediaDetailPresenter.this.initItemData(X8MediaDetailPresenter.this.viewHolder, model, true);
            }
        }

        @Override // com.fimi.album.interfaces.OnDownloadUiListener
        public void onFailure(MediaModel model) {
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile333:" + X8MediaDetailPresenter.this.mediaModel.toString());
                X8MediaDetailPresenter.this.mMediaActivity.getBtnStart().setText(R.string.media_detail_start);
            }
        }

        @Override // com.fimi.album.interfaces.OnDownloadUiListener
        public void onStop(MediaModel model) {
            if (X9HandleType.isCameraView() && X8MediaDetailPresenter.this.isCurrentModel(model)) {
                HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile444:" + X8MediaDetailPresenter.this.mediaModel.toString());
                X8MediaDetailPresenter.this.mMediaActivity.getBtnStart().setText(R.string.media_detail_start);
            }
        }
    };

    public X8MediaDetailPresenter(T activity, ViewPager viewPaper) {
        this.weakReference = new WeakReference<>(activity);
        this.mMediaActivity = this.weakReference.get();
        this.defaultDisplayWidth = this.mMediaActivity.getResources().getDisplayMetrics().widthPixels;
        this.defaultDisplayHeight = this.mMediaActivity.getResources().getDisplayMetrics().heightPixels;
        this.viewPaper = viewPaper;
        initData();
        X8MediaFileDownloadManager.getInstance().setUiDownloadListener(this.mOnOriginalDownloadUiListener);
    }

    private void initData() {
        if (X9HandleType.isCameraView()) {
            if (this.mDataManager.getX9CameraDataNoHeadList() != null) {
                this.modelList = this.mDataManager.getX9CameraDataNoHeadList();
                return;
            }
            return;
        }
        this.modelList = this.mDataManager.getLocalDataNoHeadList();
    }

    @Override // com.fimi.album.iview.IViewpaper
    public Object instantiateItem(ViewGroup container, int position) {
        MediaModel mediaModel = this.modelList.get(position);
        View view = LayoutInflater.from(this.mMediaActivity.getApplicationContext()).inflate(R.layout.album_adapter_detail_item, container, false);
        this.viewHolder = new MediaDetialViewHolder(view);
        container.setTag(this.viewHolder);
        this.mCacheContainer = container;
        initItemData(this.viewHolder, mediaModel, false);
        container.addView(view);
        return view;
    }

    public void updateItem(int position) {
        if (position <= this.modelList.size() - 1) {
            this.mediaModel = this.modelList.get(position);
            this.mCurrentPosition = position;
            MediaModel mediaModel = this.modelList.get(position);
            HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile11111:" + mediaModel.toString());
            this.mMediaActivity.getPhotoText().setText(mediaModel.getName());
            if (X9HandleType.isCameraView()) {
                if (mediaModel.isDownLoadOriginalFile()) {
                    this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                    this.mMediaActivity.getRlDownload().setVisibility(8);
                    if (this.mMediaActivity.topBarShowing()) {
                        this.mMediaActivity.getRlSelectBottom().setVisibility(0);
                    } else {
                        this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                    }
                } else if (this.mMediaActivity.topBarShowing()) {
                    this.mMediaActivity.getRlDownload().setVisibility(0);
                    this.mMediaActivity.getTvFileName().setText(mediaModel.getName());
                    if (mediaModel.isDownloading()) {
                        this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                        this.mMediaActivity.getRlDownloadBottom().setVisibility(0);
                        this.mMediaActivity.getTvPercent().setText(mediaModel.getProgress() + "%");
                        this.mMediaActivity.getBtnStart().setText(R.string.media_detail_cancle);
                    } else {
                        this.mMediaActivity.getRlSelectBottom().setVisibility(0);
                        this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                    }
                } else {
                    this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                    this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                }
                initDownload(mediaModel);
            } else {
                this.mMediaActivity.getRlDownload().setVisibility(8);
                this.mMediaActivity.getRlDownloadBottom().setVisibility(8);
                if (this.mMediaActivity.topBarShowing()) {
                    this.mMediaActivity.getRlSelectBottom().setVisibility(0);
                } else {
                    this.mMediaActivity.getRlSelectBottom().setVisibility(8);
                }
            }
            if (mediaModel.isVideo()) {
                this.mMediaActivity.getBtnPlayMax().setVisibility(0);
            } else {
                this.mMediaActivity.getBtnPlayMax().setVisibility(8);
            }
        }
    }

    public void showTopBottom(int currentPosition) {
        X8CustomVideoView customVideoView;
        MediaModel mediaModel = this.modelList.get(currentPosition);
        if (mediaModel.isVideo() && (customVideoView = (X8CustomVideoView) this.mCacheContainer.getTag(R.id.iv_top_bar + currentPosition)) != null) {
            if (this.mMediaActivity.topBarShowing()) {
                customVideoView.showBar(false);
            } else {
                customVideoView.showBar(false);
            }
            customVideoView.setTotalTime(mediaModel.getVideoDuration());
        }
    }

    private void initDownload(MediaModel model) {
        this.mMediaActivity.getMediaDownloadProgressView().setCurrentCount(model.getProgress());
        if (model.isStop() && !model.isDownloading()) {
            HostLogBack.getInstance().writeLog("Alanqiu  =============downloadFile222:" + this.mediaModel.toString());
            this.mMediaActivity.getBtnStart().setText(R.string.media_detail_start);
        }
    }

    public void initItemData(final MediaDetialViewHolder mMediaDetialViewHolder, final MediaModel mediaModel, final boolean isReload) {
        String fileUrl;
        String filePath = mediaModel.getFileLocalPath();
        File file = new File(filePath);
        if (file.exists() && !mediaModel.isVideo()) {
            fileUrl = this.perfix + filePath;
        } else if (mediaModel.isVideo()) {
            fileUrl = this.perfix + mediaModel.getThumLocalFilePath();
        } else {
            fileUrl = this.perfix + mediaModel.getThumLocalFilePath();
        }
        mMediaDetialViewHolder.mProgressBar.setVisibility(0);
        mMediaDetialViewHolder.mPhotoDraweeView.setOnPhotoTapListener(this);
        if (isReload) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(Uri.parse(fileUrl));
            imagePipeline.evictFromDiskCache(Uri.parse(fileUrl));
            imagePipeline.evictFromCache(Uri.parse(fileUrl));
        }
        FrescoUtils.displayPhoto(mMediaDetialViewHolder.mPhotoDraweeView, fileUrl, this.defaultDisplayWidth, this.defaultDisplayHeight, new FrescoControllerListener() { // from class: com.fimi.app.x8s.ui.presenter.X8MediaDetailPresenter.1
            @Override
            // com.fimi.album.biz.FrescoControllerListener, com.facebook.drawee.controller.ControllerListener
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                mMediaDetialViewHolder.mProgressBar.setVisibility(8);
                HostLogBack.getInstance().writeLog("Alanqiu  ===========initItemData onFailure:" + isReload + "mediaModel:" + mediaModel.toString());
            }

            @Override
            // com.fimi.album.biz.FrescoControllerListener, com.facebook.drawee.controller.ControllerListener
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                HostLogBack.getInstance().writeLog("Alanqiu  ===========initItemData:" + isReload + "mediaModel:" + mediaModel.toString());
                mMediaDetialViewHolder.mProgressBar.setVisibility(8);
                mMediaDetialViewHolder.mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
    }

    public void deleteItem(int position) {
        if (X9HandleType.isCameraView()) {
            CameraManager.getInstansCameraManager().deleteOnlineFile(this.modelList.get(position).getFileUrl(), new JsonUiCallBackListener() { // from class: com.fimi.app.x8s.ui.presenter.X8MediaDetailPresenter.2
                @Override // com.fimi.kernel.dataparser.usb.JsonUiCallBackListener
                public void onComplete(JSONObject rt, Object o) {
                    if (X8MediaDetailPresenter.this.mCurrentPosition < X8MediaDetailPresenter.this.modelList.size()) {
                        MediaModel mediaModel = (MediaModel) X8MediaDetailPresenter.this.modelList.get(X8MediaDetailPresenter.this.mCurrentPosition);
                        ((MediaDetailViewPaperAdapter) X8MediaDetailPresenter.this.viewPaper.getAdapter()).deleteItem(X8MediaDetailPresenter.this.mCurrentPosition);
                        X8MediaDetailPresenter.this.notifyMediaBroardcast(mediaModel);
                        if (X8MediaDetailPresenter.this.modelList.size() == 0) {
                            X8MediaDetailPresenter.this.mMediaActivity.finish();
                            X8MediaDetailPresenter.this.setOnDestory();
                            return;
                        } else if (X8MediaDetailPresenter.this.mCurrentPosition < X8MediaDetailPresenter.this.modelList.size()) {
                            X8MediaDetailPresenter.this.updateItem(X8MediaDetailPresenter.this.mCurrentPosition);
                            return;
                        } else {
                            X8MediaDetailPresenter.this.updateItem(X8MediaDetailPresenter.this.mCurrentPosition - 1);
                            return;
                        }
                    }
                    X8MediaDetailPresenter.this.mMediaActivity.finish();
                    X8MediaDetailPresenter.this.setOnDestory();
                }
            });
        } else if (position < this.modelList.size()) {
            MediaModel mediaModel = this.modelList.get(position);
            ((MediaDetailViewPaperAdapter) this.viewPaper.getAdapter()).deleteItem(position);
            FileTool.deleteFile(mediaModel.getFileLocalPath());
            notifyMediaBroardcast(mediaModel);
            notifyCameraBroardcast(mediaModel);
            if (this.modelList.size() == 0) {
                this.mMediaActivity.finish();
                setOnDestory();
            } else if (this.mCurrentPosition < this.modelList.size()) {
                updateItem(this.mCurrentPosition);
            } else {
                updateItem(this.mCurrentPosition - 1);
            }
        } else {
            this.mMediaActivity.finish();
            setOnDestory();
        }
    }

    public void notifyMediaBroardcast(MediaModel model) {
        Intent deleteIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AlbumConstant.DELETEITEM, model);
        deleteIntent.putExtras(bundle);
        deleteIntent.setAction(AlbumConstant.DELETEITEMACTION);
        LocalBroadcastManager.getInstance(this.mMediaActivity.getApplicationContext()).sendBroadcast(deleteIntent);
    }

    public void notifyCameraBroardcast(MediaModel model) {
        Intent intent = new Intent();
        List<MediaModel> list = new ArrayList<>();
        list.add(model);
        intent.setAction(X8CameraFragmentPrensenter.LOCALFILEDELETEEIVER);
        intent.putExtra(X8CameraFragmentPrensenter.LOCLAFILEDELETEITEM, (Serializable) list);
        LocalBroadcastManager.getInstance(this.mMediaActivity).sendBroadcast(intent);
    }

    public void downloadFile(int position) {
        if (this.modelList.size() > 0) {
            MediaModel mMediaModel = this.modelList.get(position);
            if (!mMediaModel.isDownloading() && !mMediaModel.isStop()) {
                X8MediaFileDownloadManager.getInstance().startDownload(mMediaModel);
            } else if (mMediaModel.isDownloading()) {
                mMediaModel.setStop(true);
                mMediaModel.setDownloading(false);
                mMediaModel.stopTask();
            } else if (mMediaModel.isStop() || mMediaModel.isDownloadFail()) {
                this.mMediaActivity.getBtnStart().setText(R.string.media_detail_cancle);
                X8MediaFileDownloadManager.getInstance().startDownload(mMediaModel);
            }
            updateItem(position);
        }
    }

    @Override // me.relex.photodraweeview.OnPhotoTapListener
    public void onPhotoTap(View view, float x, float y) {
        if (this.mMediaActivity.getIvTopBar().isShown()) {
            this.mMediaActivity.showTopBar(false);
            return;
        }
        this.mMediaActivity.showTopBar(true);
        updateItem(this.mCurrentPosition);
    }

    public void updateFileName(int currentSelectPosition) {
        if (currentSelectPosition < this.modelList.size()) {
            this.mMediaActivity.getPhotoText().setText(this.modelList.get(currentSelectPosition).getName());
        }
    }

    public boolean isCurrentModel(MediaModel model) {
        LogUtil.i("zhej", "isCurrentModel: modelNoList:" + this.modelList.size() + ",position:" + this.mCurrentPosition);
        return this.mCurrentPosition < this.modelList.size() && this.modelList.get(this.mCurrentPosition).getFileUrl().equals(model.getFileUrl());
    }

    public void setOnDestory() {
        if (this.mOnOriginalDownloadUiListener != null) {
            this.mOnOriginalDownloadUiListener = null;
        }
    }

    public void showShare() {
    }

    public void hideFragment() {
    }

    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }

    public String getMediaFileName() {
        if (this.modelList == null || this.modelList.size() <= 0) {
            return null;
        }
        String name = this.modelList.get(this.mCurrentPosition).getName();
        return name;
    }

    public boolean isDownloadFinish() {
        return this.mediaModel.isDownLoadOriginalFile();
    }

    public void startActivity() {
        Log.i("moweiru", "startActivity==");
        if (this.modelList != null && this.modelList.size() > 0 && this.mCurrentPosition < this.modelList.size()) {
            X8FmMediaInfo info = new X8FmMediaInfo();
            info.setName(this.modelList.get(this.mCurrentPosition).getName());
            info.setPath(this.perfix + this.modelList.get(this.mCurrentPosition).getFileLocalPath());
            info.setDuration(this.modelList.get(this.mCurrentPosition).getVideoDuration());
            Intent intent = new Intent(this.mMediaActivity, X8FimiPlayerActivity.class);
            intent.putExtra(FmMediaPlayer.FM_MEDIA_INFO, info);
            this.mMediaActivity.startActivity(intent);
        }
    }
}
