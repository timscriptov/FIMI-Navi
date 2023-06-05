package com.fimi.album.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.album.adapter.PanelRecycleAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.IBroadcastPreform;
import com.fimi.album.iview.IRecycleAdapter;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.ui.MediaDetailActivity;
import com.fimi.kernel.utils.FileTool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.qos.logback.core.net.SyslogConstants;

/* loaded from: classes.dex */
public abstract class BaseFragmentPresenter<T extends MediaModel> implements IBroadcastPreform, IRecycleAdapter {
    public static final String TAG = BaseFragmentPresenter.class.getName();
    protected static boolean isScrollRecycle;
    protected Context context;
    protected boolean isEnterSelectMode;
    protected ISelectData mISelectData;
    protected PanelRecycleAdapter mPanelRecycleAdapter;
    protected RecyclerView mRecyclerView;
    protected CopyOnWriteArrayList<T> modelList;
    protected LinkedHashMap<String, CopyOnWriteArrayList<T>> stateHashMap;
    protected List<T> selectList = new ArrayList();
    protected String perfix = "file://";
    protected int defaultPhtotHeight = SyslogConstants.LOG_CLOCK;
    protected int defaultPhtotWidth = SyslogConstants.LOG_CLOCK;

    public BaseFragmentPresenter(RecyclerView mRecyclerView, PanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context) {
        this.mRecyclerView = mRecyclerView;
        this.mPanelRecycleAdapter = mPanelRecycleAdapter;
        this.mISelectData = mISelectData;
        this.context = context;
    }

    public void changeViewState(View view, int state, int resBg) {
        view.setVisibility(state);
        view.setBackgroundResource(resBg);
    }

    protected void getOriginalData() {
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
            this.mISelectData.selectSize(size, 0L);
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

    protected void clearSelectData() {
        this.selectList.clear();
    }

    public void addSelectModel(T mode) {
        this.selectList.add(mode);
    }

    public void removeSelectModel(T mode) {
        this.selectList.remove(mode);
    }

    public void preformMode(T model, View view, int state, int selectBg, int unSelectBg) {
        if (model != null) {
            if (!model.isSelect()) {
                addSelectModel(model);
                model.setSelect(true);
                changeViewState(view, state, selectBg);
                return;
            }
            removeSelectModel(model);
            model.setSelect(false);
            changeViewState(view, state, unSelectBg);
        }
    }

    public void enterSelectMode(boolean state) {
        this.isEnterSelectMode = state;
        if (!state) {
            preformSelectEvent(false);
        }
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
        Intent intent = new Intent(this.context, MediaDetailActivity.class);
        MediaModel mediaModel = this.modelList.get(position);
        String formatDate = mediaModel.getFormatDate();
        int dataPosition = 0;
        for (Map.Entry<String, CopyOnWriteArrayList<T>> entry : this.stateHashMap.entrySet()) {
            dataPosition++;
            if (formatDate != null && formatDate.equals(entry.getKey())) {
                break;
            }
        }
        intent.putExtra(AlbumConstant.SELECTPOSITION, position - dataPosition);
        this.context.startActivity(intent);
    }

    @Override // com.fimi.album.iview.IBroadcastPreform
    public void onReceive(Context context, Intent intent) {
        MediaModel mediaModel = (MediaModel) intent.getSerializableExtra(AlbumConstant.DELETEITEM);
        if (this.modelList.contains(mediaModel)) {
            this.mPanelRecycleAdapter.updateDeleteItem(this.modelList.indexOf(mediaModel));
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
            FileTool.deleteFile(filePath);
        }
        this.mPanelRecycleAdapter.updateDeleteItems();
        this.selectList.clear();
        this.isEnterSelectMode = false;
        callBackDeleteFile();
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
            this.mPanelRecycleAdapter.notifyDataSetChanged();
            callBackSelectSize(this.selectList.size());
        }
    }
}
