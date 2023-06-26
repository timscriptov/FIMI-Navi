package com.fimi.app.x8s.ui.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.album.biz.DataManager;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.ISelectData;
import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.BodyRecycleViewHolder;
import com.fimi.app.x8s.adapter.HeadRecyleViewHolder;
import com.fimi.app.x8s.adapter.PanelRecycleViewHolder;
import com.fimi.app.x8s.adapter.X8sPanelRecycleAdapter;
import com.fimi.app.x8s.ui.album.x8s.X8BaseMediaFragmentPresenter;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.kernel.utils.DateFormater;
import com.fimi.kernel.utils.FrescoUtils;
import com.fimi.kernel.utils.VideoDuration;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


public class X8LocalFragmentPresenter<T extends MediaModel> extends X8BaseMediaFragmentPresenter {
    public static final String UPDATELOCALITEM = "UPDATELOCALITEM";
    public static final String UPDATELOCALITEMRECEIVER = "UPDATELOCALITEMRECEIVER";
    private static final String TAG = "X9LocalFragmentPresente";
    private final int defaultBound;
    private final Handler durationHandler;
    private final Handler mainHandler;
    private X8LocalFragmentPresenter<T>.UpdateLocalItemReceiver mUpdateLocalItemReceiver;

    public X8LocalFragmentPresenter(RecyclerView mRecyclerView, X8sPanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context) {
        super(mRecyclerView, mPanelRecycleAdapter, mISelectData, context, false);
        this.defaultBound = 50;
        this.durationHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
        this.mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);
        doTrans();
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            this.mGridLayoutManager = (GridLayoutManager) manager;
        }
        registerReciver();
    }

    private void doTrans() {
        this.mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                if (holder instanceof BodyRecycleViewHolder mBodyRecycleViewHolder) {
                    mBodyRecycleViewHolder.tvDuringdate.setVisibility(View.INVISIBLE);
                    mBodyRecycleViewHolder.ivSelect.setVisibility(View.GONE);
                }
            }
        });
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                X8LocalFragmentPresenter.this.isScrollRecycle = false;
                X8LocalFragmentPresenter.this.durationHandler.sendEmptyMessage(1);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) <= X8LocalFragmentPresenter.this.defaultBound) {
                    X8LocalFragmentPresenter.this.isScrollRecycle = false;
                    X8LocalFragmentPresenter.this.durationHandler.sendEmptyMessage(1);
                    return;
                }
                X8LocalFragmentPresenter.this.isScrollRecycle = true;
            }
        });
    }

    @Override
    public boolean handleMessage(Message message) {
        try {
            if (message.what == 1 && this.modelList.size() > 0 && this.mGridLayoutManager != null) {
                int firstVisibleItem = this.mGridLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = this.mGridLayoutManager.findLastVisibleItemPosition();
                if (firstVisibleItem != -1) {
                    while (true) {
                        if (firstVisibleItem <= lastVisibleItem) {
                            MediaModel mediaModel = getModel(firstVisibleItem);
                            if (mediaModel != null && !mediaModel.isCategory() && TextUtils.isEmpty(mediaModel.getVideoDuration())) {
                                String time = DateFormater.dateString(VideoDuration.getVideoDuration(this.context, mediaModel.getFileLocalPath()), "mm:ss");
                                mediaModel.setVideoDuration(time);
                                Message updateMessage = new Message();
                                updateMessage.what = 2;
                                updateMessage.arg1 = firstVisibleItem;
                                this.mainHandler.sendMessage(updateMessage);
                            }
                            firstVisibleItem++;
                            if (this.isScrollRecycle) {
                                this.durationHandler.removeMessages(1);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            } else if (message.what == 2) {
                int currentPsition = message.arg1;
                this.mPanelRecycleAdapter.notifyItemChanged(currentPsition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadRecyleViewHolder) {
            doHeadTrans((HeadRecyleViewHolder) holder, position);
        } else if (holder instanceof BodyRecycleViewHolder) {
            doBodyTrans((BodyRecycleViewHolder) holder, position);
        } else {
            doPanelTrans((PanelRecycleViewHolder) holder, position);
        }
    }

    private void doHeadTrans(HeadRecyleViewHolder headRecyleViewHolder, int position) {
        headRecyleViewHolder.mTvHeard.setText(this.context.getString(R.string.x8_album_head_title, DataManager.obtain().getLocalVideoCount() + "", DataManager.obtain().getLocalPhotoCount() + ""));
    }

    private void doPanelTrans(final PanelRecycleViewHolder holder, final int position) {
        final MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            holder.tvTitleDescription.setText(getModel(position).getFormatDate().split(" ")[0]);
            if (mediaModel.isSelect()) {
                holder.mBtnAllSelect.setImageResource(R.drawable.x8_ablum_select);
            } else {
                holder.mBtnAllSelect.setImageResource(R.drawable.x8_ablum_unselect);
                holder.mBtnAllSelect.setSelected(false);
            }
        }
        holder.mBtnAllSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X8LocalFragmentPresenter.this.onItemCategoryClick(holder, position, mediaModel);
            }
        });
        if (this.isEnterSelectMode) {
            holder.mBtnAllSelect.setVisibility(View.VISIBLE);
        } else {
            holder.mBtnAllSelect.setVisibility(View.GONE);
        }
    }

    public void onItemCategoryClick(PanelRecycleViewHolder holder, int position, MediaModel mediaModel) {
        if (mediaModel != null) {
            String formatDate = mediaModel.getFormatDate().split(" ")[0];
            CopyOnWriteArrayList<MediaModel> internalList = (CopyOnWriteArrayList<MediaModel>) this.stateHashMap.get(formatDate);
            Log.i("moweiru", "(mediaModel.isSelect():" + mediaModel.isSelect());
            perfomSelectCategory(internalList, !mediaModel.isSelect());
        }
    }


    private void perfomSelectCategory(CopyOnWriteArrayList<MediaModel> internalList, boolean isSelect) {
        Iterator<MediaModel> it = internalList.iterator();
        while (it.hasNext()) {
            MediaModel mMediaModel = it.next();
            if (isSelect) {
                if (!mMediaModel.isSelect()) {
                    mMediaModel.setSelect(true);
                    addSelectModel(mMediaModel);
                }
            } else if (mMediaModel.isSelect()) {
                mMediaModel.setSelect(false);
                removeSelectModel(mMediaModel);
            }
        }
        notifyAllVisible();
        callBackSelectSize(this.selectList.size());
        callAllSelectMode(this.selectList.size() == (this.modelList.size() - this.stateHashMap.size()) - 1);
    }

    private void doBodyTrans(final BodyRecycleViewHolder holder, final int position) {
        MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            mediaModel.setItemPosition(position);
            String photoUrl = null;
            String currentFilePath = mediaModel.getFileLocalPath();
            if (!TextUtils.isEmpty(mediaModel.getThumLocalFilePath())) {
                File file = new File(mediaModel.getThumLocalFilePath());
                if (file.exists()) {
                    photoUrl = mediaModel.getThumLocalFilePath();
                }
            } else {
                photoUrl = currentFilePath;
            }
            if (!TextUtils.isEmpty(currentFilePath) && !TextUtils.isEmpty(photoUrl)) {
                if (mediaModel.isVideo()) {
                    holder.sdvImageView.setBackgroundResource(R.drawable.album_video_loading);
                } else {
                    holder.sdvImageView.setBackgroundResource(R.drawable.album_photo_loading);
                }
                if (mediaModel.getFileSize() > 0) {
                    holder.mFileSize.setText(ByteUtil.getNetFileSizeDescription(mediaModel.getFileSize()));
                } else {
                    holder.mFileSize.setVisibility(View.GONE);
                }
                if (!currentFilePath.equals(holder.sdvImageView.getTag()) && !mediaModel.isLoadThulm()) {
                    holder.sdvImageView.setTag(currentFilePath);
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + photoUrl, this.defaultPhtotWidth, this.defaultPhtotHeight);
                    mediaModel.setLoadThulm(true);
                } else if (!currentFilePath.equals(holder.sdvImageView.getTag())) {
                    holder.sdvImageView.setTag(currentFilePath);
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + photoUrl, this.defaultPhtotWidth, this.defaultPhtotHeight);
                }
                if (mediaModel.isVideo()) {
                    holder.mIvVideoFlag.setImageResource(R.drawable.x8_ablumn_normal_vedio_mark);
                    holder.tvDuringdate.setTag(currentFilePath);
                    if (!TextUtils.isEmpty(mediaModel.getVideoDuration())) {
                        holder.tvDuringdate.setVisibility(View.VISIBLE);
                        holder.tvDuringdate.setText(mediaModel.getVideoDuration());
                    }
                } else {
                    holder.mIvVideoFlag.setImageResource(R.drawable.x8_ablumn_normal_photo_mark);
                    holder.tvDuringdate.setVisibility(View.INVISIBLE);
                }
                if (this.isEnterSelectMode) {
                    if (mediaModel.isSelect()) {
                        changeSelectViewState(mediaModel, holder, 0);
                    } else {
                        changeSelectViewState(mediaModel, holder, 8);
                    }
                } else if (mediaModel.isSelect()) {
                    changeSelectViewState(mediaModel, holder, 0);
                } else {
                    changeSelectViewState(mediaModel, holder, 8);
                }
                holder.sdvImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        X8LocalFragmentPresenter.this.onItemClick(holder, view, position);
                    }
                });
                holder.sdvImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        X8LocalFragmentPresenter.this.onItemLongClick(holder, view, position);
                        return true;
                    }
                });
            }
        }
    }

    public void onItemLongClick(BodyRecycleViewHolder holder, View view, int position) {
        if (!this.isEnterSelectMode) {
            this.isEnterSelectMode = true;
            callBackEnterSelectMode();
        }
        preformMode(getModel(position), holder);
        callBackSelectSize(this.selectList.size());
    }

    public void onItemClick(BodyRecycleViewHolder holder, View view, int position) {
        T model = (T) getModel(position);
        if (this.isEnterSelectMode) {
            preformMode(model, holder);
            callBackSelectSize(this.selectList.size());
            return;
        }
        goMediaDetailActivity(this.modelList.indexOf(model));
    }

    @Override
    public void showCategorySelectView(boolean state) {
        int firstVisibleItem = this.mGridLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = this.mGridLayoutManager.findLastVisibleItemPosition();
        if (firstVisibleItem != -1) {
            while (firstVisibleItem <= lastVisibleItem) {
                MediaModel mediaModel = getModel(firstVisibleItem);
                if (mediaModel != null && mediaModel.isCategory()) {
                    this.mPanelRecycleAdapter.notifyItemChanged(firstVisibleItem);
                }
                firstVisibleItem++;
            }
        }
    }

    @Override
    public void registerReciver() {
        this.mUpdateLocalItemReceiver = new UpdateLocalItemReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATELOCALITEMRECEIVER);
        LocalBroadcastManager.getInstance(this.context).registerReceiver(this.mUpdateLocalItemReceiver, intentFilter);
    }

    @Override
    public void registerDownloadListerner() {
    }

    @Override
    public void unRegisterReciver() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(this.mUpdateLocalItemReceiver);
    }


    public class UpdateLocalItemReceiver extends BroadcastReceiver {
        public UpdateLocalItemReceiver() {
        }


        @Override
        public void onReceive(Context context, Intent intent) {
            MediaModel mediaModel;
            String action = intent.getAction();
            if (action.equals(X8LocalFragmentPresenter.UPDATELOCALITEMRECEIVER) && (mediaModel = (MediaModel) intent.getSerializableExtra(X8LocalFragmentPresenter.UPDATELOCALITEM)) != null) {
                X8LocalFragmentPresenter.this.mPanelRecycleAdapter.addNewItem(mediaModel);
            }
        }
    }
}
