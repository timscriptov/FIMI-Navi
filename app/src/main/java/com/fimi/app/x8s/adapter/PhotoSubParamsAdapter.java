package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.PhotoSubParamItemEntity;
import com.fimi.app.x8s.viewHolder.CameraParamsTitleViewHolder;
import com.fimi.app.x8s.viewHolder.SubParamItemListener;
import com.fimi.app.x8s.viewHolder.SubParamsViewHolder;
import com.fimi.x8sdk.command.CameraJsonCollection;

/* loaded from: classes.dex */
public class PhotoSubParamsAdapter extends RecyclerView.Adapter {
    SubParamsViewHolder subParamsViewHolderTwo;
    private Context context;
    private SubParamItemListener paramListener;
    private PhotoSubParamItemEntity subEntity;
    private int option_index = -1;
    private boolean isEnable = false;

    public PhotoSubParamsAdapter(Context context, PhotoSubParamItemEntity entity) {
        this.context = context;
        this.subEntity = entity;
    }

    public void setParamListener(SubParamItemListener paramListener) {
        this.paramListener = paramListener;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View subView = LayoutInflater.from(this.context).inflate(R.layout.x8_iso_recycler_item_title, parent, false);
            RecyclerView.ViewHolder viewHolder = new CameraParamsTitleViewHolder(subView);
            return viewHolder;
        }
        View subView2 = LayoutInflater.from(this.context).inflate(R.layout.x8_photo_sub_param_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder2 = new SubParamsViewHolder(subView2, this.paramListener);
        return viewHolder2;
    }

    public void updateData(PhotoSubParamItemEntity entity) {
        if (entity != null) {
            this.subEntity = entity;
            notifyDataSetChanged();
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SubParamsViewHolder) {
            final SubParamsViewHolder subParamsViewHolder = (SubParamsViewHolder) holder;
            this.subParamsViewHolderTwo = subParamsViewHolder;
            subParamsViewHolder.initItemData(this.subEntity, position, this.isEnable, this.option_index);
            holder.itemView.setEnabled(true);
            holder.itemView.setAlpha(1.0f);
            if (this.subEntity.getTitleName().equals(this.context.getResources().getString(R.string.x8_camera_style))) {
                String optionName = this.subEntity.getOptions().get(position);
                if (optionName.equals(this.context.getResources().getString(R.string.x8_camera_saturation)) || optionName.equals(this.context.getResources().getString(R.string.x8_camera_contrast))) {
                    subParamsViewHolder.initViewStub();
                }
                if (optionName.equals(this.context.getResources().getString(R.string.x8_camera_sharpness))) {
                    subParamsViewHolder.initSharpViewStub();
                }
            } else if (this.subEntity.getTitleName().equals(this.context.getResources().getString(R.string.x8_photo_mode)) && this.subEntity.getOptions().get(position).equals("panorama_capture")) {
                holder.itemView.setEnabled(false);
                holder.itemView.setAlpha(0.4f);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.adapter.PhotoSubParamsAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (PhotoSubParamsAdapter.this.paramListener != null && PhotoSubParamsAdapter.this.isEnable && !subParamsViewHolder.isRecordingUnclickableItem(PhotoSubParamsAdapter.this.subEntity.getTitleName()) && !subParamsViewHolder.isDelayedPhotographyUnclickableItem(PhotoSubParamsAdapter.this.subEntity.getTitleName())) {
                        String paramKey = PhotoSubParamsAdapter.this.subEntity.getParamKey();
                        if (paramKey != null && !"".equals(paramKey)) {
                            if (paramKey.equals("video_resolution")) {
                                if (((SubParamsViewHolder) holder).getContentLayout().getChildCount() <= 0) {
                                    PhotoSubParamsAdapter.this.paramListener.checkResolutionDetailParam(PhotoSubParamsAdapter.this.subEntity.getTitleName(), PhotoSubParamsAdapter.this.subEntity.getOptions().get(position), PhotoSubParamsAdapter.this.subEntity.getParamValue(), position, subParamsViewHolder);
                                } else {
                                    ((SubParamsViewHolder) holder).getContentLayout().removeAllViews();
                                    return;
                                }
                            } else if (paramKey.equals("capture_mode") && position != 1) {
                                if (((SubParamsViewHolder) holder).getContentLayout().getChildCount() <= 0) {
                                    PhotoSubParamsAdapter.this.paramListener.checkResolutionDetailParam(PhotoSubParamsAdapter.this.subEntity.getTitleName(), PhotoSubParamsAdapter.this.subEntity.getOptions().get(position), PhotoSubParamsAdapter.this.subEntity.getParamValue(), position, subParamsViewHolder);
                                } else {
                                    ((SubParamsViewHolder) holder).getContentLayout().removeAllViews();
                                    return;
                                }
                            } else if (!paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE) || position == 1) {
                                PhotoSubParamsAdapter.this.paramListener.checkDetailParam(PhotoSubParamsAdapter.this.subEntity.getTitleName(), PhotoSubParamsAdapter.this.subEntity.getOptions().get(position), position, subParamsViewHolder);
                            } else if (((SubParamsViewHolder) holder).getContentLayout().getChildCount() <= 0) {
                                PhotoSubParamsAdapter.this.paramListener.checkResolutionDetailParam(PhotoSubParamsAdapter.this.subEntity.getTitleName(), PhotoSubParamsAdapter.this.subEntity.getOptions().get(position), PhotoSubParamsAdapter.this.subEntity.getParamValue(), position, subParamsViewHolder);
                            } else {
                                ((SubParamsViewHolder) holder).getContentLayout().removeAllViews();
                                return;
                            }
                        }
                        PhotoSubParamsAdapter.this.option_index = position;
                    }
                }
            });
        } else if (holder instanceof CameraParamsTitleViewHolder) {
            CameraParamsTitleViewHolder paramsViewHolder = (CameraParamsTitleViewHolder) holder;
            paramsViewHolder.initView(this.subEntity.getTitleName());
            if (position == 0) {
                holder.itemView.findViewById(R.id.item_back_btn).setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.adapter.PhotoSubParamsAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (PhotoSubParamsAdapter.this.paramListener != null) {
                            PhotoSubParamsAdapter.this.paramListener.gotoParentItem();
                        }
                    }
                });
            }
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.subEntity == null || this.subEntity.getOptions() == null) {
            return 0;
        }
        return this.subEntity.getOptions().size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return position == 0 ? 1 : 2;
    }

    public void forbid(boolean enable) {
        this.isEnable = enable;
        notifyDataSetChanged();
    }

    public void viewHolderRemoveAllViews() {
        if (this.subParamsViewHolderTwo != null && this.subParamsViewHolderTwo.getContentLayout().getChildCount() > 0) {
            this.subParamsViewHolderTwo.getContentLayout().removeAllViews();
        }
    }
}
