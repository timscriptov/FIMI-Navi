package com.fimi.app.x8s.ui.album.x8s;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.biz.DataManager;
import com.fimi.album.biz.X9HandleType;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IBroadcastPreform;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.album.iview.IRecycleAdapter;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.widget.DownloadStateView;
import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.BodyRecycleViewHolder;
import com.fimi.app.x8s.adapter.PanelRecycleViewHolder;
import com.fimi.app.x8s.adapter.X8sPanelRecycleAdapter;
import com.fimi.app.x8s.ui.presenter.X8CameraFragmentPrensenter;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.interfaces.IPersonalDataCallBack;
import com.fimi.kernel.dataparser.ILinkMessage;
import com.fimi.kernel.dataparser.usb.JsonUiCallBackListener;
import com.fimi.kernel.utils.FileTool;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.widget.CustomLoadManage;
import com.fimi.x8sdk.controller.CameraManager;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public abstract class X8BaseMediaFragmentPresenter<T extends MediaModel> implements IRecycleAdapter, IHandlerCallback, IBroadcastPreform {
    private final String TAG = "X9BaseMediaFragmentPren";
    private final int RESH_NOTIFY = 888;
    private final int RESH_DELETE_NOTIFY = 887;
    private final IPersonalDataCallBack personalDataCallBack = new IPersonalDataCallBack() {
        @Override
        public void onPersonalDataCallBack(int groupId, int cmdId, ILinkMessage message) {
        }

        @Override
        public void onPersonalSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
        }
    };
    protected Context context;
    protected boolean isCamera;
    protected boolean isEnterSelectMode;
    protected boolean isScrollRecycle;
    protected GridLayoutManager mGridLayoutManager;
    protected ISelectData mISelectData;
    protected X8sPanelRecycleAdapter mPanelRecycleAdapter;
    protected RecyclerView mRecyclerView;
    protected CopyOnWriteArrayList<T> modelList;
    protected LinkedHashMap<String, CopyOnWriteArrayList<T>> stateHashMap;
    protected List<T> selectList = new ArrayList();
    protected List<T> deleteList = new ArrayList();
    protected String perfix = "file://";
    protected int defaultPhtotHeight = 120;
    protected int defaultPhtotWidth = 120;
    protected boolean isResh = true;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 888) {
                X8BaseMediaFragmentPresenter.this.isResh = true;
            } else if (msg.what == 887) {
                for (int i = 0; i < X8BaseMediaFragmentPresenter.this.deleteList.size(); i++) {
                    int finalI = i;
                    if (X8BaseMediaFragmentPresenter.this.modelList != null && X8BaseMediaFragmentPresenter.this.modelList.size() > 0 && X8BaseMediaFragmentPresenter.this.modelList.contains(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI)) && X8BaseMediaFragmentPresenter.this.modelList.indexOf(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI)) > 0) {
                        X8BaseMediaFragmentPresenter.this.mPanelRecycleAdapter.updateDeleteItem(X8BaseMediaFragmentPresenter.this.modelList.indexOf(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI)));
                    }
                }
                CustomLoadManage.dismiss();
            }
        }
    };

    public X8BaseMediaFragmentPresenter(RecyclerView mRecyclerView, X8sPanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context, boolean isCamera) {
        this.mRecyclerView = mRecyclerView;
        this.mPanelRecycleAdapter = mPanelRecycleAdapter;
        this.mISelectData = mISelectData;
        this.context = context;
        this.isCamera = isCamera;
    }

    protected static String englishConvertDigital(String englishData) {
        SimpleDateFormat digitalSdf = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat englishSdf = new SimpleDateFormat("MM/dd/yyyy", Locale.CHINA);
        try {
            String digitalData = englishSdf.format(digitalSdf.parse(englishData));
            return digitalData;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract void registerDownloadListerner();

    public abstract void registerReciver();

    public abstract void showCategorySelectView(boolean z);

    public abstract void unRegisterReciver();

    protected void changeViewState(View view, int state, int resBg) {
        view.setVisibility(state);
        view.setBackgroundResource(resBg);
    }

    public void changeSelectViewState(MediaModel model, RecyclerView.ViewHolder holder, int state) {
        if (model.isCategory()) {
            PanelRecycleViewHolder panelRecycleViewHolder = (PanelRecycleViewHolder) holder;
            panelRecycleViewHolder.mBtnAllSelect.setVisibility(state);
            return;
        }
        BodyRecycleViewHolder bodyRecycleViewHolder = (BodyRecycleViewHolder) holder;
        if (state == 0 && bodyRecycleViewHolder.mDownloadStateView.getVisibility() == 0) {
            bodyRecycleViewHolder.ivSelect.setVisibility(View.VISIBLE);
            bodyRecycleViewHolder.mIvDownloadMask.setVisibility(View.GONE);
            bodyRecycleViewHolder.mIvSelectMask.setVisibility(View.VISIBLE);
        } else if (state == 0) {
            bodyRecycleViewHolder.ivSelect.setVisibility(View.VISIBLE);
            bodyRecycleViewHolder.mIvSelectMask.setVisibility(View.VISIBLE);
        } else {
            bodyRecycleViewHolder.ivSelect.setVisibility(View.GONE);
            bodyRecycleViewHolder.mIvSelectMask.setVisibility(View.GONE);
        }
    }

    public void changeDownloadState(BodyRecycleViewHolder holder, DownloadStateView.State state) {
        holder.mDownloadStateView.setState(state);
        if (state == DownloadStateView.State.DOWNLOAD_FAIL) {
            holder.mTvDownloadState.setText(this.context.getString(R.string.media_downlown_fail));
        } else if (state == DownloadStateView.State.PAUSE) {
            holder.mTvDownloadState.setText(this.context.getString(R.string.media_downlown_stop));
        }
    }

    public void changeDownloadProgress(BodyRecycleViewHolder holder, int progress) {
        holder.mDownloadStateView.setProgress(progress);
        holder.mTvDownloadState.setText(this.context.getString(R.string.media_downlown_download, progress + "%"));
    }

    public void showDownloadImg(BodyRecycleViewHolder holder, boolean isShow) {
        holder.mDownloadStateView.setVisibility(isShow ? 0 : 8);
        holder.mTvDownloadState.setVisibility(isShow ? 0 : 8);
        if (isShow && holder.ivSelect.getVisibility() == 0) {
            holder.mIvSelectMask.setVisibility(View.VISIBLE);
            holder.mIvDownloadMask.setVisibility(View.GONE);
        } else if (isShow) {
            holder.mIvSelectMask.setVisibility(View.GONE);
            holder.mIvDownloadMask.setVisibility(View.VISIBLE);
        } else {
            holder.mIvDownloadMask.setVisibility(View.GONE);
        }
    }

    protected void getOriginalData() {
        if (X9HandleType.isCameraView()) {
            this.modelList = DataManager.obtain().getX9CameraDataList();
            this.stateHashMap = DataManager.obtain().getX9CameraDataHash();
            return;
        }
        this.modelList = DataManager.obtain().getLocalDataList();
        this.stateHashMap = DataManager.obtain().getDataHash();
    }

    public T getModel(int position) {
        if (this.modelList == null) {
            getOriginalData();
        }
        if (position >= this.modelList.size()) {
            return null;
        }
        return this.modelList.get(position);
    }

    protected boolean isContainsModel(T model) {
        if (this.modelList == null) {
            getOriginalData();
        }
        return this.modelList.contains(model);
    }

    protected int modelPosition(T model) {
        if (this.modelList == null) {
            getOriginalData();
        }
        return this.modelList.indexOf(model);
    }

    public void callBackSelectSize(int size) {
        if (this.mISelectData != null) {
            this.mISelectData.selectSize(size, calculationSelectFileSize());
        }
    }

    protected void callAddSingleFile() {
        if (this.mISelectData != null) {
            this.mISelectData.addSingleFile();
        }
    }

    public void callBackEnterSelectMode() {
        if (this.mISelectData != null) {
            this.mISelectData.enterSelectMode();
        }
    }

    protected void callBackQuitSelectMode() {
        if (this.mISelectData != null) {
            this.mISelectData.quitSelectMode();
        }
    }

    protected void callBackDeleteFile() {
        if (this.mISelectData != null) {
            this.mISelectData.deleteFile();
        }
    }

    protected void callStartDownload() {
        if (this.mISelectData != null) {
            this.mISelectData.startDownload();
        }
    }

    public void callDeleteComplete() {
        if (this.mISelectData != null) {
            this.mISelectData.onDeleteComplete();
        }
    }

    public void callAllSelectMode(boolean isAll) {
        if (this.mISelectData != null) {
            this.mISelectData.allSelectMode(isAll);
        }
    }

    protected void clearSelectData() {
        this.selectList.clear();
    }

    public void addSelectModel(T mode) {
        if (!mode.isCategory() && !mode.isHeadView()) {
            this.selectList.add(mode);
        }
    }

    public void removeSelectModel(T mode) {
        this.selectList.remove(mode);
    }

    public void preformMode(T model, RecyclerView.ViewHolder holder) {
        BodyRecycleViewHolder bodyRecycleViewHolder = (BodyRecycleViewHolder) holder;
        if (model != null && bodyRecycleViewHolder.mDownloadStateView.getVisibility() != 0) {
            if (!model.isSelect()) {
                addSelectModel(model);
                model.setSelect(true);
                changeSelectViewState(model, holder, 0);
            } else {
                removeSelectModel(model);
                model.setSelect(false);
                changeSelectViewState(model, holder, 8);
            }
            preformDataSelect(model);
            callAllSelectMode(this.selectList.size() == (this.modelList.size() - this.stateHashMap.size()) - 1);
        }
    }

    protected void preformDataSelect(T mode) {
        int firstIndex = 0;
        int count = 0;
        int selectCount = 0;
        boolean isEnter = false;
        for (int index = 0; index < this.modelList.size(); index++) {
            if (mode.getFormatDate().split(" ")[0].equals(this.modelList.get(index).getFormatDate() == null ? null : this.modelList.get(index).getFormatDate().split(" ")[0])) {
                if (!isEnter) {
                    firstIndex = index;
                } else {
                    count++;
                    if (this.modelList.get(index).isSelect()) {
                        selectCount++;
                    }
                }
                isEnter = true;
            } else if (isEnter) {
                break;
            }
        }
        if (count == selectCount) {
            if (!this.modelList.get(firstIndex).isSelect()) {
                this.modelList.get(firstIndex).setSelect(true);
                this.mPanelRecycleAdapter.notifyItemChanged(firstIndex);
            }
        } else if (this.modelList.get(firstIndex).isSelect()) {
            this.modelList.get(firstIndex).setSelect(false);
            this.mPanelRecycleAdapter.notifyItemChanged(firstIndex);
        }
    }

    public void enterSelectMode(boolean state) {
        this.isEnterSelectMode = state;
        if (!state) {
            preformSelectEvent(false);
        }
        showCategorySelectView(state);
    }

    public void setEnterSelectMode(boolean enterSelectMode) {
        this.isEnterSelectMode = enterSelectMode;
    }

    public int querySelectSize() {
        return this.selectList.size();
    }

    public void refreshData() {
        getOriginalData();
    }

    public void canCalAsyncTask() {
    }

    public void goMediaDetailActivity(int position) {
        if (this.modelList != null && this.modelList.size() > 0) {
            Intent intent = new Intent(this.context, X8MediaDetailActivity.class);
            MediaModel mediaModel = this.modelList.get(position);
            Log.i("moweiru", "mediaModel====" + mediaModel.toString());
            String formatDate = mediaModel.getFormatDate().split(" ")[0];
            int dataPosition = 0;
            for (Map.Entry<String, CopyOnWriteArrayList<T>> entry : this.stateHashMap.entrySet()) {
                if (formatDate != null && formatDate.compareTo(entry.getKey()) <= 0) {
                    dataPosition++;
                }
            }
            intent.putExtra(AlbumConstant.SELECTPOSITION, (position - dataPosition) - 1);
            LogUtil.i("zhej", "goMediaDetailActivity: modelList:" + this.modelList.size() + ",position:" + ((position - dataPosition) - 1));
            this.context.startActivity(intent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AlbumConstant.DELETEITEMACTION)) {
            MediaModel mediaModel = (MediaModel) intent.getSerializableExtra(AlbumConstant.DELETEITEM);
            if (this.modelList != null && this.modelList.contains(mediaModel)) {
                this.mPanelRecycleAdapter.updateDeleteItem(this.modelList.indexOf(mediaModel));
            }
        }
    }

    public void deleteSelectFile() {
        Integer startDeletePosition = null;
        for (int index = 0; index < this.selectList.size(); index++) {
            T t = this.selectList.get(index);
            if (isContainsModel(t)) {
                int position = modelPosition(t);
                this.mPanelRecycleAdapter.remoteItem(position);
                if (startDeletePosition == null) {
                    startDeletePosition = Integer.valueOf(position);
                }
            }
            String filePath = t.getFileLocalPath();
            sendBroadcastMediaScannerScanFile(filePath);
            FileTool.deleteFile(filePath);
        }
        sendBroadcastUpdateDeleteItem();
        this.mPanelRecycleAdapter.updateDeleteItems();
        this.selectList.clear();
        this.isEnterSelectMode = false;
        callBackDeleteFile();
        callDeleteComplete();
    }

    public void sendBroadcastUpdateDeleteItem() {
        Intent intent = new Intent();
        List<T> list = new ArrayList<>();
        list.addAll(this.selectList);
        intent.setAction(X8CameraFragmentPrensenter.LOCALFILEDELETEEIVER);
        intent.putExtra(X8CameraFragmentPrensenter.LOCLAFILEDELETEITEM, (Serializable) list);
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
    }

    public void downLoadFile() {
        for (int index = 0; index < this.selectList.size(); index++) {
            int j = 0;
            while (true) {
                if (j >= this.modelList.size()) {
                    break;
                } else if (this.selectList.get(index).getFileUrl().equals(this.modelList.get(j).getFileUrl()) && !this.selectList.get(index).isDownLoadOriginalFile()) {
                    this.modelList.get(j).setSelect(false);
                    this.mPanelRecycleAdapter.notifyItemChanged(j);
                    break;
                } else if (!this.selectList.get(index).getFileUrl().equals(this.modelList.get(j).getFileUrl()) || !this.selectList.get(index).isDownLoadOriginalFile()) {
                    j++;
                } else {
                    this.modelList.get(j).setSelect(false);
                    this.mPanelRecycleAdapter.notifyItemChanged(j);
                    break;
                }
            }
        }
        X8MediaFileDownloadManager.getInstance().startDownload(this.selectList);
        this.selectList.clear();
        this.isEnterSelectMode = false;
        callStartDownload();
        this.isResh = false;
        for (int index2 = 0; index2 < this.modelList.size(); index2++) {
            if (this.modelList.get(index2).isCategory()) {
                this.modelList.get(index2).setSelect(false);
                this.mPanelRecycleAdapter.notifyItemChanged(index2);
            }
        }
        Message message = new Message();
        message.what = 888;
        this.mHandler.sendMessageDelayed(message, 500L);
    }

    public void enterSelectAllMode() {
        preformSelectEvent(true);
    }

    public void cancalSelectAllMode() {
        preformSelectEvent(false);
    }

    private void preformSelectEvent(boolean state) {
        if (this.modelList != null) {
            for (int index = 0; index < this.modelList.size(); index++) {
                T model = getModel(index);
                if (state) {
                    if (!model.isSelect()) {
                        addSelectModel(model);
                        model.setSelect(state);
                    }
                } else if (model.isSelect()) {
                    removeSelectModel(model);
                    model.setSelect(state);
                }
            }
            notifyAllVisible();
            callBackSelectSize(this.selectList.size());
        }
    }

    public long calculationSelectFileSize() {
        long capacity = 0;
        for (MediaModel mediaModel : this.selectList) {
            capacity += mediaModel.getFileSize();
        }
        return capacity;
    }

    public void deleteCameraSelectFile() {
        this.deleteList.clear();
        this.deleteList.addAll(this.selectList);
        for (int index = 0; index < this.selectList.size(); index++) {
            int j = 0;
            while (true) {
                if (j < this.modelList.size()) {
                    if (!this.selectList.get(index).getFileUrl().equals(this.modelList.get(j).getFileUrl())) {
                        j++;
                    } else {
                        this.modelList.get(j).setSelect(false);
                        this.mPanelRecycleAdapter.notifyItemChanged(j);
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        this.selectList.clear();
        this.isEnterSelectMode = false;
        for (int index2 = 0; index2 < this.modelList.size(); index2++) {
            if (this.modelList.get(index2).isCategory()) {
                this.mPanelRecycleAdapter.notifyItemChanged(index2);
            }
        }
        callBackDeleteFile();
        if (this.deleteList.size() > 0) {
            CustomLoadManage.showNoClick(this.context);
        }
        for (int i = 0; i < this.deleteList.size(); i++) {
            final int finalI = i;
            CameraManager.getInstansCameraManager().deleteOnlineFile(this.deleteList.get(i).getFileUrl(), new JsonUiCallBackListener() {
                @Override
                public void onComplete(JSONObject rt, Object o) {
                    X8BaseMediaFragmentPresenter.this.mHandler.removeMessages(887);
                    X8BaseMediaFragmentPresenter.this.mHandler.sendEmptyMessageDelayed(887, 5000L);
                    if (X8BaseMediaFragmentPresenter.this.modelList != null && X8BaseMediaFragmentPresenter.this.modelList.size() > 0 && X8BaseMediaFragmentPresenter.this.modelList.contains(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI)) && X8BaseMediaFragmentPresenter.this.modelList.indexOf(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI)) > 0) {
                        X8BaseMediaFragmentPresenter.this.mPanelRecycleAdapter.updateDeleteItem(X8BaseMediaFragmentPresenter.this.modelList.indexOf(X8BaseMediaFragmentPresenter.this.deleteList.get(finalI)));
                    }
                    if (finalI >= X8BaseMediaFragmentPresenter.this.deleteList.size() - 1) {
                        X8BaseMediaFragmentPresenter.this.mHandler.removeMessages(887);
                        CustomLoadManage.dismiss();
                    }
                }
            });
        }
    }

    public boolean isModelListEmpty() {
        return this.modelList == null || this.modelList.size() <= 0;
    }

    public void notifyAllVisible() {
        int firstVisibleItem;
        int lastVisibleItem;
        if (this.mGridLayoutManager != null) {
            if (this.mGridLayoutManager != null) {
                int firstVisibleItem2 = this.mGridLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem2 = this.mGridLayoutManager.findLastVisibleItemPosition();
                if (firstVisibleItem2 != -1 && lastVisibleItem2 != -1) {
                    if (firstVisibleItem2 - 20 > 0) {
                        firstVisibleItem = firstVisibleItem2 - 20;
                    } else {
                        firstVisibleItem = 0;
                    }
                    if (lastVisibleItem2 + 20 < this.modelList.size()) {
                        lastVisibleItem = lastVisibleItem2 + 20;
                    } else {
                        lastVisibleItem = this.modelList.size() - 1;
                    }
                    this.mPanelRecycleAdapter.notifyItemRangeChanged(firstVisibleItem, (lastVisibleItem - firstVisibleItem) + 1);
                    return;
                }
                return;
            }
            this.mPanelRecycleAdapter.notifyDataSetChanged();
        }
    }

    public void sendBroadcastMediaScannerScanFile(String path) {
        if (path != null) {
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            Uri uri = Uri.fromFile(new File(path));
            intent.setData(uri);
            this.context.sendBroadcast(intent);
        }
    }


    public void notifyAddCallback(MediaModel model) {
        callAddSingleFile();
        if (this.isEnterSelectMode) {
            preformDataSelect((T) model);
            callAllSelectMode(this.selectList.size() == (this.modelList.size() - this.stateHashMap.size()) - 1);
        }
    }
}
