package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.viewHolder.CameraArrayParamsViewHolder;

import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class PhotoArrayParamsAdapter extends RecyclerView.Adapter {
    private Context context;
    private PhotoArrayItemClickListener itemClickListener;
    private Map<String, String> keyMap;
    private List<String> pList;
    private String paramKey;
    private int select_position;

    public PhotoArrayParamsAdapter(Context context, List<String> mlist) {
        this.context = context;
        this.pList = mlist;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(this.context).inflate(R.layout.x8_photo_array_param_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new CameraArrayParamsViewHolder(parentView);
        return viewHolder;
    }

    public void updateData(List<String> uplist, Map<String, String> map, String paramKey, int selected_index) {
        if (uplist != null) {
            this.pList = uplist;
        }
        if (map != null) {
            this.keyMap = map;
        }
        if (paramKey != null) {
            this.paramKey = paramKey;
        }
        this.select_position = selected_index;
        notifyDataSetChanged();
    }

    public void setSelect_position(int select_position) {
        this.select_position = select_position;
        notifyDataSetChanged();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CameraArrayParamsViewHolder) {
            ((CameraArrayParamsViewHolder) holder).initView(this.pList.get(position), this.keyMap);
            ((CameraArrayParamsViewHolder) holder).upSelected(position == this.select_position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.adapter.PhotoArrayParamsAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (PhotoArrayParamsAdapter.this.itemClickListener != null) {
                    PhotoArrayParamsAdapter.this.itemClickListener.onItemClickListener(PhotoArrayParamsAdapter.this.paramKey, (String) PhotoArrayParamsAdapter.this.pList.get(position));
                }
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.pList != null) {
            return this.pList.size();
        }
        return 0;
    }

    public void setItemClickListener(PhotoArrayItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /* loaded from: classes.dex */
    public interface PhotoArrayItemClickListener {
        void onItemClickListener(String str, String str2);
    }
}
