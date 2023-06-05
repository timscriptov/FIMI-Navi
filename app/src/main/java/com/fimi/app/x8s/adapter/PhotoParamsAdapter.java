package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.PhotoParamItemEntity;
import com.fimi.app.x8s.viewHolder.CameraParamListener;
import com.fimi.app.x8s.viewHolder.PhotoParamsViewHolder;

import java.util.List;


public class PhotoParamsAdapter extends RecyclerView.Adapter {
    private final Context context;
    private boolean isEnable = false;
    private List<PhotoParamItemEntity> pList;
    private CameraParamListener paramListener;

    public PhotoParamsAdapter(Context context, List<PhotoParamItemEntity> mlist) {
        this.context = context;
        this.pList = mlist;
    }

    public void setParamListener(CameraParamListener paramListener) {
        this.paramListener = paramListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(this.context).inflate(R.layout.x8_photo_param_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new PhotoParamsViewHolder(parentView);
        return viewHolder;
    }

    public void updateData(List<PhotoParamItemEntity> uplist) {
        if (uplist != null) {
            this.pList = uplist;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final PhotoParamItemEntity itemEntity = this.pList.get(position);
        if (holder instanceof PhotoParamsViewHolder) {
            ((PhotoParamsViewHolder) holder).initItemData(this.pList.get(position), this.isEnable);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PhotoParamsAdapter.this.paramListener != null) {
                        PhotoParamsAdapter.this.paramListener.gotoSubItem(itemEntity.getParamKey(), itemEntity.getParamValue(), holder);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (this.pList != null) {
            return this.pList.size();
        }
        return 0;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public void setEnable(boolean enable) {
        this.isEnable = enable;
        notifyDataSetChanged();
    }
}
