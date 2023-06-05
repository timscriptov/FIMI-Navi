package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.viewHolder.CameraEVParamListener;
import com.fimi.app.x8s.viewHolder.CameraParamsViewHolder;

import java.util.List;

/* loaded from: classes.dex */
public class CameraEVParamsAdatper extends RecyclerView.Adapter {
    private Context context;
    private CameraEVParamListener listener;
    private String paramKey;
    private List<String> plist;
    private int selectIndex = 0;
    private boolean isEnable = false;

    public CameraEVParamsAdatper(Context context, List<String> params, CameraEVParamListener paramListener, String typeKey) {
        this.context = context;
        this.plist = params;
        this.paramKey = typeKey;
        this.listener = paramListener;
    }

    public void updateDatas(List<String> params) {
        this.plist = params;
        notifyDataSetChanged();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.x8_iso_recycler_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new CameraParamsViewHolder(rootView);
        return viewHolder;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if ((holder instanceof CameraParamsViewHolder) && this.plist.size() > 0) {
            ((CameraParamsViewHolder) holder).initView(this.plist.get(position), this.isEnable);
            ((CameraParamsViewHolder) holder).upSelected(position == this.selectIndex);
            holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.adapter.CameraEVParamsAdatper.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (CameraEVParamsAdatper.this.isEnable && CameraEVParamsAdatper.this.listener != null) {
                        CameraEVParamsAdatper.this.listener.updateParams(CameraEVParamsAdatper.this.paramKey, (String) CameraEVParamsAdatper.this.plist.get(position));
                    }
                }
            });
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.plist != null) {
            return this.plist.size();
        }
        return 0;
    }

    public void upSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }

    public void setEnable(boolean enable) {
        this.isEnable = enable;
        notifyDataSetChanged();
    }
}
