package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.INodataTip;
import com.fimi.album.iview.IRecycleAdapter;
import com.fimi.android.app.R;
import com.fimi.kernel.utils.DateFormater;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public class X8sPanelRecycleAdapter<T extends MediaModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Handler.Callback {
    public static final int HEADTYPE = 48;
    public static final int ITEMBODYTYPE = 32;
    public static final int ITEMHEADTYPE = 16;
    public static final String TAG = X8sPanelRecycleAdapter.class.getName();
    private final Context context;
    private final boolean isCamera;
    private final INodataTip mINodataTip;
    private IRecycleAdapter mIRecycleAdapter;
    private final Handler mainHandler;
    private CopyOnWriteArrayList<T> modelList;
    private CopyOnWriteArrayList<T> modelNoHeadList;
    private final Handler otherHandler;
    private HashMap<String, CopyOnWriteArrayList<T>> stateHashMap;
    private final DataManager mdataManager = DataManager.obtain();
    private final int headSpanCount = 4;
    private final int headCount = 4;
    private final int bodySpanCount = 1;
    private final int internalListBound = 2;

    public X8sPanelRecycleAdapter(Context context, boolean isCamera, INodataTip mINodataTip) {
        this.isCamera = isCamera;
        initData();
        this.context = context;
        this.otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
        this.mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);
        this.mINodataTip = mINodataTip;
    }

    public void setmIRecycleAdapter(IRecycleAdapter mIRecycleAdapter) {
        this.mIRecycleAdapter = mIRecycleAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder rvViewHolder;
        if (viewType == 48) {
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.x8_recyleview_head, parent, false);
            return new HeadRecyleViewHolder(inflate);
        }
        if (viewType == 16) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_panel_view_holder, parent, false);
            rvViewHolder = new PanelRecycleViewHolder(view);
        } else {
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_body_view_holder, parent, false);
            rvViewHolder = new BodyRecycleViewHolder(view2);
        }
        return rvViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager mGridLayoutManager) {
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (X8sPanelRecycleAdapter.this.isHeadView(position)) {
                        return X8sPanelRecycleAdapter.this.headCount;
                    }
                    boolean isCategory = X8sPanelRecycleAdapter.this.isCategoryType(position);
                    return isCategory ? X8sPanelRecycleAdapter.this.headSpanCount : X8sPanelRecycleAdapter.this.bodySpanCount;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (this.mIRecycleAdapter != null) {
            this.mIRecycleAdapter.onBindViewHolder(holder, position);
        }
    }

    public void remoteItem(int position) {
        CopyOnWriteArrayList<T> internalList;
        MediaModel mediaModel = this.modelList.get(position);
        String localPath = mediaModel.getFileLocalPath();
        String formateDate = mediaModel.getFormatDate().split(" ")[0];
        this.modelNoHeadList.remove(mediaModel);
        this.modelList.remove(mediaModel);
        notifyItemRemoved(mediaModel.getItemPosition());
        statisticalFileCount(mediaModel, false);
        if (this.stateHashMap != null && localPath != null && (internalList = this.stateHashMap.get(formateDate)) != null) {
            Iterator<T> it = internalList.iterator();
            while (it.hasNext()) {
                MediaModel cacheModel = it.next();
                if (cacheModel != null && localPath.equals(cacheModel.getFileLocalPath())) {
                    internalList.remove(cacheModel);
                }
            }
            if (internalList.size() < this.internalListBound) {
                this.stateHashMap.remove(internalList.get(0).getFormatDate().split(" ")[0]);
                this.modelList.remove(internalList.get(0));
                notifyItemRemoved(internalList.get(0).getItemPosition());
            }
        }
    }

    public void updateDeleteItems() {
        notifyDataSetChanged();
    }

    public void updateDeleteItem(int position) {
        CopyOnWriteArrayList<T> internalList;
        MediaModel mediaModel = this.modelList.get(position);
        if (mediaModel != null) {
            String formateDate = mediaModel.getFormatDate();
            String localPath = mediaModel.getFileLocalPath();
            this.modelList.remove(position);
            statisticalFileCount(mediaModel, false);
            notifyItemRemoved(position);
            if (this.stateHashMap != null && localPath != null && (internalList = this.stateHashMap.get(formateDate.split(" ")[0])) != null) {
                Iterator<T> it = internalList.iterator();
                while (it.hasNext()) {
                    MediaModel cacheModel = it.next();
                    if (cacheModel != null && localPath.equals(cacheModel.getFileLocalPath())) {
                        internalList.remove(cacheModel);
                    }
                }
                if (internalList.size() < this.internalListBound) {
                    if (internalList.size() < this.internalListBound) {
                        if (position - 1 < this.modelList.size()) {
                            this.modelList.remove(position - 1);
                            notifyItemRemoved(position - 1);
                            notifyItemRangeRemoved(position, this.modelList.size() - position);
                        } else {
                            this.modelList.remove(this.modelList.size() - 1);
                            notifyItemRemoved(this.modelList.size() - 1);
                        }
                    }
                    judgeIsNoData();
                    return;
                }
            }
            judgeIsNoData();
            notifyItemRangeRemoved(position, this.modelList.size() - position);
        }
    }

    private void statisticalFileCount(MediaModel mediaModel, boolean isAdd) {
        int count;
        if (!mediaModel.isCategory() && !mediaModel.isHeadView()) {
            if (isAdd) {
                count = 1;
            } else {
                count = -1;
            }
            if (this.isCamera) {
                if (mediaModel.isVideo()) {
                    DataManager.obtain().setX9CameraVideoCount(DataManager.obtain().getX9CameraVideoCount() + count);
                } else {
                    DataManager.obtain().setX9CameraPhotoCount(DataManager.obtain().getX9CameraPhotoCount() + count);
                }
                if (DataManager.obtain().getX9CameraPhotoCount() == 0 && DataManager.obtain().getX9CameraVideoCount() == 0) {
                    notifyItemRangeRemoved(0, 2);
                    DataManager.obtain().reX9CameraDefaultVaribale();
                    judgeIsNoData();
                }
            } else {
                if (mediaModel.isVideo()) {
                    DataManager.obtain().setLocalVideoCount(DataManager.obtain().getLocalVideoCount() + count);
                } else {
                    DataManager.obtain().setLocalPhotoCount(DataManager.obtain().getLocalPhotoCount() + count);
                }
                if (DataManager.obtain().getLocalPhotoCount() == 0 && DataManager.obtain().getLocalVideoCount() == 0) {
                    DataManager.obtain().reLocalDefaultVaribale();
                    judgeIsNoData();
                }
            }
            Message message = Message.obtain();
            message.what = 11;
            message.arg1 = 0;
            this.mainHandler.sendMessage(message);
        }
    }

    public void addNewItem(T mediaModel) {
        if (mediaModel != null) {
            Message message = new Message();
            message.what = 6;
            message.obj = mediaModel;
            this.otherHandler.sendMessage(message);
        }
    }


    public void addItemReally(T mediaModel) {
        changeMediaModelState(mediaModel);
        int inserterPosition = 0;
        if (mediaModel != null) {
            int modelNoHeadListPosition = 0;
            Iterator<T> it = this.modelNoHeadList.iterator();
            while (it.hasNext()) {
                MediaModel indexMediaModel = it.next();
                if (mediaModel.getFormatDate().compareTo(indexMediaModel.getFormatDate()) > 0) {
                    break;
                }
                modelNoHeadListPosition++;
            }
            this.modelNoHeadList.add(modelNoHeadListPosition, mediaModel);
            statisticalFileCount(mediaModel, true);
            String currentFormatData = mediaModel.getFormatDate().split(" ")[0];
            if (!this.stateHashMap.containsKey(currentFormatData)) {
                CopyOnWriteArrayList newList = new CopyOnWriteArrayList();
                int forEachPosition = 0;
                for (Map.Entry<String, CopyOnWriteArrayList<T>> entry : this.stateHashMap.entrySet()) {
                    forEachPosition++;
                    int compareValue = currentFormatData.compareTo(entry.getKey());
                    if (compareValue >= 0) {
                        break;
                    }
                    inserterPosition += entry.getValue().size();
                }
                if (this.modelList.size() == 0) {
                    MediaModel headviewModel = new MediaModel();
                    headviewModel.setHeadView(true);
                    this.modelList.add(0, (T) headviewModel);
                    inserterPosition++;
                    if (!this.isCamera) {
                        DataManager.obtain().setLocalLoad(true);
                    } else {
                        DataManager.obtain().setX9CameralLoad(true);
                    }
                } else {
                    inserterPosition++;
                }
                MediaModel mediaModel1 = new MediaModel();
                mediaModel1.setCategory(true);
                mediaModel1.setFormatDate(mediaModel.getFormatDate());
                newList.add(mediaModel1);
                newList.add(mediaModel);
                this.modelList.add(inserterPosition, (T) mediaModel1);
                this.modelList.add(inserterPosition + 1, mediaModel);
                this.stateHashMap.put(currentFormatData, newList);
            } else {
                CopyOnWriteArrayList internalList = this.stateHashMap.get(currentFormatData);
                if (internalList.size() > 0) {
                    int position = this.modelList.indexOf(internalList.get(0));
                    internalList.add(mediaModel);
                    this.modelList.add(position + 1, mediaModel);
                }
            }
        }
        Message message = Message.obtain();
        message.what = 7;
        message.arg1 = inserterPosition;
        message.obj = mediaModel;
        this.mainHandler.sendMessage(message);
    }

    private void changeMediaModelState(T mediaModel) {
        File file = new File(mediaModel.getFileLocalPath());
        if (file.exists()) {
            long createTime = file.lastModified();
            mediaModel.setCreateDate(createTime);
            mediaModel.setFormatDate(DateFormater.dateString(createTime, "yyyy.MM.dd HH:mm:ss"));
        }
        mediaModel.setSelect(false);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeadView(position)) {
            return 48;
        }
        if (isCategoryType(position)) {
            return 16;
        }
        return 32;
    }

    public boolean isHeadView(int position) {
        if (this.modelList == null || this.modelList.size() <= 0 || position < 0) {
            return false;
        }
        MediaModel model = this.modelList.get(position);
        return model.isHeadView();
    }

    public boolean isCategoryType(int position) {
        if (this.modelList == null || this.modelList.size() <= 0 || position < 0) {
            return false;
        }
        MediaModel model = this.modelList.get(position);
        return model.isCategory();
    }

    @Override
    public int getItemCount() {
        if (this.modelList == null) {
            return 0;
        }
        return this.modelList.size();
    }

    private void judgeIsNoData() {
        if (this.modelList != null) {
            if (this.modelList.size() == 0) {
                if (this.mINodataTip != null) {
                    this.mINodataTip.noDataTipCallback(true);
                }
            } else if (this.mINodataTip != null) {
                this.mINodataTip.noDataTipCallback(false);
            }
        } else if (this.mINodataTip != null) {
            this.mINodataTip.noDataTipCallback(true);
        }
    }

    public void refresh() {
        initData();
        this.mainHandler.sendEmptyMessage(5);
    }

    private void initData() {
        if (this.isCamera) {
            this.modelList = this.mdataManager.getX9CameraDataList();
            this.stateHashMap = this.mdataManager.getX9CameraDataHash();
            if (this.mdataManager.getX9CameraDataNoHeadList() != null) {
                this.modelNoHeadList = this.mdataManager.getX9CameraDataNoHeadList();
                return;
            }
            return;
        }
        this.modelList = this.mdataManager.getLocalDataList();
        this.stateHashMap = this.mdataManager.getDataHash();
        this.modelNoHeadList = this.mdataManager.getLocalDataNoHeadList();
    }


    @Override
    public boolean handleMessage(@NonNull Message message) {
        if (message.what == 5) {
            judgeIsNoData();
            notifyDataSetChanged();
            return true;
        } else if (message.what == 6) {
            addItemReally((T) message.obj);
            return true;
        } else if (message.what == 7) {
            notifyAddNewItem(message.arg1);
            this.mINodataTip.notifyAddCallback((MediaModel) message.obj);
            return true;
        } else if (message.what == 11) {
            notifyItemChanged(message.arg1);
            return true;
        } else {
            return true;
        }
    }

    private void notifyAddNewItem(int position) {
        notifyItemRangeInserted(position, this.modelList.size());
    }

    public void removeCallBack() {
    }
}
