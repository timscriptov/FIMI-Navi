package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.PhotoSubParamItemEntity;
import com.fimi.app.x8s.viewHolder.CameraParamsTitleViewHolder;
import com.fimi.app.x8s.viewHolder.SubParamItemListener;
import com.fimi.app.x8s.viewHolder.SubParamsViewHolder;
import com.fimi.x8sdk.command.CameraJsonCollection;


public class PhotoSubParamsAdapter extends RecyclerView.Adapter {
    private final Context context;
    SubParamsViewHolder subParamsViewHolderTwo;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View subView = LayoutInflater.from(this.context).inflate(R.layout.x8_iso_recycler_item_title, parent, false);
            return new CameraParamsTitleViewHolder(subView);
        }
        View subView2 = LayoutInflater.from(this.context).inflate(R.layout.x8_photo_sub_param_list_item, parent, false);
        return new SubParamsViewHolder(subView2, this.paramListener);
    }

    public void updateData(PhotoSubParamItemEntity entity) {
        if (entity != null) {
            this.subEntity = entity;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof final SubParamsViewHolder subParamsViewHolder) {
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
            holder.itemView.setOnClickListener(v -> {
                if (paramListener != null && isEnable && !subParamsViewHolder.isRecordingUnclickableItem(subEntity.getTitleName()) && !subParamsViewHolder.isDelayedPhotographyUnclickableItem(subEntity.getTitleName())) {
                    String paramKey = subEntity.getParamKey();
                    if (paramKey != null && !"".equals(paramKey)) {
                        if (paramKey.equals("video_resolution")) {
                            if (((SubParamsViewHolder) holder).getContentLayout().getChildCount() <= 0) {
                                paramListener.checkResolutionDetailParam(subEntity.getTitleName(), subEntity.getOptions().get(position), subEntity.getParamValue(), position, subParamsViewHolder);
                            } else {
                                ((SubParamsViewHolder) holder).getContentLayout().removeAllViews();
                                return;
                            }
                        } else if (paramKey.equals("capture_mode") && position != 1) {
                            if (((SubParamsViewHolder) holder).getContentLayout().getChildCount() <= 0) {
                                paramListener.checkResolutionDetailParam(subEntity.getTitleName(), subEntity.getOptions().get(position), subEntity.getParamValue(), position, subParamsViewHolder);
                            } else {
                                ((SubParamsViewHolder) holder).getContentLayout().removeAllViews();
                                return;
                            }
                        } else if (!paramKey.equals(CameraJsonCollection.KEY_RECORD_MODE) || position == 1) {
                            paramListener.checkDetailParam(subEntity.getTitleName(), subEntity.getOptions().get(position), position, subParamsViewHolder);
                        } else if (((SubParamsViewHolder) holder).getContentLayout().getChildCount() <= 0) {
                            paramListener.checkResolutionDetailParam(subEntity.getTitleName(), subEntity.getOptions().get(position), subEntity.getParamValue(), position, subParamsViewHolder);
                        } else {
                            ((SubParamsViewHolder) holder).getContentLayout().removeAllViews();
                            return;
                        }
                    }
                    option_index = position;
                }
            });
        } else if (holder instanceof CameraParamsTitleViewHolder paramsViewHolder) {
            paramsViewHolder.initView(this.subEntity.getTitleName());
            if (position == 0) {
                holder.itemView.findViewById(R.id.item_back_btn).setOnClickListener(v -> {
                    if (paramListener != null) {
                        paramListener.gotoParentItem();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (this.subEntity == null || this.subEntity.getOptions() == null) {
            return 0;
        }
        return this.subEntity.getOptions().size();
    }

    @Override
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
