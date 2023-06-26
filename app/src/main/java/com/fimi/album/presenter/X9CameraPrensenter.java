package com.fimi.album.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.album.adapter.BodyRecycleViewHolder;
import com.fimi.album.adapter.PanelRecycleAdapter;
import com.fimi.album.adapter.PanelRecycleViewHolder;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.album.iview.ISelectData;
import com.fimi.android.app.R;
import com.fimi.kernel.utils.FrescoUtils;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


public class X9CameraPrensenter<T extends MediaModel> extends BaseFragmentPresenter implements Handler.Callback {
    private final int defaultBound;
    private final Handler durationHandler;
    private final Handler mainHandler;
    private GridLayoutManager mGridLayoutManager;

    public X9CameraPrensenter(RecyclerView mRecyclerView, PanelRecycleAdapter mPanelRecycleAdapter, ISelectData mISelectData, Context context) {
        super(mRecyclerView, mPanelRecycleAdapter, mISelectData, context);
        this.defaultBound = 50;
        this.durationHandler = HandlerManager.obtain().getHandlerInOtherThread(this);
        this.mainHandler = HandlerManager.obtain().getHandlerInMainThread(this);
        doTrans();
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            this.mGridLayoutManager = (GridLayoutManager) manager;
        }
    }

    private void doTrans() {
        this.mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                if (holder instanceof BodyRecycleViewHolder mBodyRecycleViewHolder) {
                    mBodyRecycleViewHolder.tvDuringdate.setVisibility(View.GONE);
                    mBodyRecycleViewHolder.ivSelect.setVisibility(View.GONE);
                }
            }
        });
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                BaseFragmentPresenter.isScrollRecycle = false;
                X9CameraPrensenter.this.durationHandler.sendEmptyMessage(1);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) <= X9CameraPrensenter.this.defaultBound) {
                    BaseFragmentPresenter.isScrollRecycle = false;
                    X9CameraPrensenter.this.durationHandler.sendEmptyMessage(1);
                    return;
                }
                BaseFragmentPresenter.isScrollRecycle = true;
            }
        });
    }

    @Override
    public boolean handleMessage(Message message) {
        return true;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyRecycleViewHolder) {
            doBodyTrans((BodyRecycleViewHolder) holder, position);
        } else {
            doPanelTrans((PanelRecycleViewHolder) holder, position);
        }
    }

    private void doPanelTrans(final PanelRecycleViewHolder holder, final int position) {
        final MediaModel mediaModel = getModel(position);
        if (mediaModel != null) {
            holder.tvTitleDescription.setText(getModel(position).getFormatDate());
            if (mediaModel.isSelect()) {
                holder.ivIconSelect.setImageResource(R.drawable.album_btn_category_select_press);
                holder.tvAllSelect.setText(R.string.media_select_all_no);
            } else {
                holder.ivIconSelect.setImageResource(R.drawable.album_btn_category_select_normal);
                holder.tvAllSelect.setText(R.string.media_select_all);
            }
        }
        holder.rlRightSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                X9CameraPrensenter.this.onItemCategoryClick(holder, position, mediaModel);
            }
        });
    }

    public void onItemCategoryClick(PanelRecycleViewHolder holder, int position, MediaModel mediaModel) {
        if (mediaModel != null) {
            String formatDate = mediaModel.getFormatDate();
            CopyOnWriteArrayList<MediaModel> internalList = (CopyOnWriteArrayList<MediaModel>) this.stateHashMap.get(formatDate);
            perfomSelectCategory(internalList, this.context.getString(R.string.media_select_all).equals(holder.tvAllSelect.getText()));
        }
    }


    private void perfomSelectCategory(CopyOnWriteArrayList<MediaModel> internalList, boolean isSelect) {
        Iterator<MediaModel> it = internalList.iterator();
        while (it.hasNext()) {
            MediaModel mMediaModel = it.next();
            if (isSelect) {
                mMediaModel.setSelect(true);
                addSelectModel(mMediaModel);
            } else {
                mMediaModel.setSelect(false);
                removeSelectModel(mMediaModel);
            }
        }
        int firstVisibleItem = this.mGridLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = this.mGridLayoutManager.findLastVisibleItemPosition();
        this.mPanelRecycleAdapter.notifyItemRangeChanged(firstVisibleItem, lastVisibleItem);
    }

    private void doBodyTrans(final BodyRecycleViewHolder holder, final int position) {
        MediaModel mediaModel = getModel(position);
        mediaModel.setItemPosition(position);
        if (mediaModel != null) {
            String currentFilePath = mediaModel.getFileLocalPath();
            if (!TextUtils.isEmpty(currentFilePath)) {
                if (!currentFilePath.equals(holder.sdvImageView.getTag()) && mediaModel.isLoadThulm()) {
                    holder.sdvImageView.setTag(currentFilePath);
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + currentFilePath, this.defaultPhtotWidth, this.defaultPhtotHeight);
                    mediaModel.setLoadThulm(true);
                } else if (!currentFilePath.equals(holder.sdvImageView.getTag())) {
                    FrescoUtils.displayPhoto(holder.sdvImageView, this.perfix + currentFilePath, this.defaultPhtotWidth, this.defaultPhtotHeight);
                }
                if (mediaModel.isVideo()) {
                    holder.tvDuringdate.setTag(currentFilePath);
                    if (!TextUtils.isEmpty(mediaModel.getVideoDuration())) {
                        holder.tvDuringdate.setVisibility(View.VISIBLE);
                        holder.tvDuringdate.setText(mediaModel.getVideoDuration());
                    }
                } else {
                    holder.tvDuringdate.setVisibility(View.GONE);
                }
                if (this.isEnterSelectMode) {
                    if (mediaModel.isSelect()) {
                        changeViewState(holder.ivSelect, 0, R.drawable.album_icon_share_media_active);
                    } else {
                        changeViewState(holder.ivSelect, 0, R.drawable.album_icon_share_media_nomal);
                    }
                } else if (mediaModel.isSelect()) {
                    changeViewState(holder.ivSelect, 0, R.drawable.album_icon_share_media_active);
                } else {
                    changeViewState(holder.ivSelect, 8, R.drawable.album_icon_share_media_nomal);
                }
                holder.sdvImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        X9CameraPrensenter.this.onItemClick(holder, view, position);
                    }
                });
                holder.sdvImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        X9CameraPrensenter.this.onItemLongClick(holder, view, position);
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
        preformMode(getModel(position), holder.ivSelect, 0, R.drawable.album_icon_share_media_active, R.drawable.album_icon_share_media_nomal);
        callBackSelectSize(this.selectList.size());
    }

    public void onItemClick(BodyRecycleViewHolder holder, View view, int position) {
        T model = (T) getModel(position);
        if (this.isEnterSelectMode) {
            preformMode(model, holder.ivSelect, 0, R.drawable.album_icon_share_media_active, R.drawable.album_icon_share_media_nomal);
            callBackSelectSize(this.selectList.size());
            return;
        }
        goMediaDetailActivity(this.modelList.indexOf(model));
    }
}
