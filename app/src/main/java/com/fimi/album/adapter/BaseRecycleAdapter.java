package com.fimi.album.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.IHandlerCallback;
import com.fimi.album.iview.INodataTip;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public abstract class BaseRecycleAdapter<T extends MediaModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IHandlerCallback {
    public static final int ITEMBODYTYPE = 32;
    public static final int ITEMHEADTYPE = 16;
    public static final String TAG = BaseRecycleAdapter.class.getName();
    protected Context context;
    protected INodataTip mINodataTip;
    protected Handler mainHandler;
    protected Handler otherHandler;
    private CopyOnWriteArrayList<T> modelList;
    private CopyOnWriteArrayList<T> modelNoHeadList;
    private HashMap<String, CopyOnWriteArrayList<T>> stateHashMap;
    private final DataManager mdataManager = DataManager.obtain();
    private final int headSpanCount = 4;
    private final int bodySpanCount = 1;
    private final int internalListBound = 2;

    public BaseRecycleAdapter(Context context, INodataTip mINodataTip) {
        initData();
        this.context = context;
        this.otherHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
        this.mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);
        this.mINodataTip = mINodataTip;
    }

    private void initData() {
        this.modelList = this.mdataManager.getLocalDataList();
        this.stateHashMap = this.mdataManager.getDataHash();
        this.modelNoHeadList = this.mdataManager.getLocalDataNoHeadList();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager mGridLayoutManager) {
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isCategory = BaseRecycleAdapter.this.isCategoryType(position);
                    return isCategory ? BaseRecycleAdapter.this.headSpanCount : BaseRecycleAdapter.this.bodySpanCount;
                }
            });
        }
    }

    public void remoteItem(int position) {
        CopyOnWriteArrayList<T> internalList;
        MediaModel mediaModel = this.modelList.get(position);
        String localPath = mediaModel.getFileLocalPath();
        String formateDate = mediaModel.getFormatDate();
        this.modelNoHeadList.remove(mediaModel);
        this.modelList.remove(mediaModel);
        if (this.stateHashMap != null && localPath != null && (internalList = this.stateHashMap.get(formateDate)) != null) {
            Iterator<T> it = internalList.iterator();
            while (it.hasNext()) {
                MediaModel cacheModel = it.next();
                if (cacheModel != null && localPath.equals(cacheModel.getFileLocalPath())) {
                    internalList.remove(cacheModel);
                }
            }
            if (internalList.size() < this.internalListBound) {
                this.modelList.remove(internalList.get(0));
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
            notifyItemRemoved(position);
            if (this.stateHashMap != null && localPath != null && (internalList = this.stateHashMap.get(formateDate)) != null) {
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

    public void addNewItem(T mediaModel) {
        Message message = new Message();
        message.what = 6;
        message.obj = mediaModel;
        this.otherHandler.sendMessage(message);
    }


    public void addItemReally(T mediaModel) {
        int inserterPosition = 0;
        if (mediaModel != null) {
            this.modelNoHeadList.add(mediaModel);
            String currentFormatData = mediaModel.getFormatDate();
            if (!this.stateHashMap.containsKey(currentFormatData)) {
                CopyOnWriteArrayList newList = new CopyOnWriteArrayList();
                int forEachPosition = 0;
                for (Map.Entry<String, CopyOnWriteArrayList<T>> entry : this.stateHashMap.entrySet()) {
                    forEachPosition++;
                    int compareValue = currentFormatData.compareTo(entry.getKey());
                    if (compareValue <= 0) {
                        break;
                    }
                    inserterPosition += entry.getValue().size();
                }
                if (forEachPosition == 1) {
                    MediaModel mediaModel1 = new MediaModel();
                    mediaModel1.setCategory(true);
                    mediaModel1.setFormatDate(mediaModel.getFormatDate());
                    newList.add(mediaModel1);
                    newList.add(mediaModel);
                    this.modelList.add((T) mediaModel1);
                    this.modelList.add(mediaModel);
                } else {
                    MediaModel mediaModel12 = new MediaModel();
                    mediaModel12.setCategory(true);
                    mediaModel12.setFormatDate(mediaModel.getFormatDate());
                    newList.add(mediaModel12);
                    newList.add(mediaModel);
                    this.modelList.add(inserterPosition, (T) mediaModel12);
                    this.modelList.add(inserterPosition + 1, mediaModel);
                }
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
        this.mainHandler.sendMessage(message);
    }

    @Override
    public int getItemViewType(int position) {
        return isCategoryType(position) ? 16 : 32;
    }

    protected boolean isCategoryType(int position) {
        if (this.modelList == null || this.modelList.size() <= 0 || position < 0) {
            return false;
        }
        MediaModel model = this.modelList.get(position);
        return model.isCategory();
    }

    @Override
    public int getItemCount() {
        judgeIsNoData();
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


    @Override
    public boolean handleMessage(Message message) {
        if (message.what == 5) {
            notifyDataSetChanged();
            return true;
        } else if (message.what == 6) {
            addItemReally((T) message.obj);
            return true;
        } else if (message.what == 7) {
            notifyAddNewItem(message.arg1);
            return true;
        } else {
            return true;
        }
    }

    private void notifyAddNewItem(int position) {
        notifyItemInserted(position);
    }

    public void removeCallBack() {
    }
}
