package com.fimi.app.x8s.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.viewHolder.CameraEVParamListener;
import com.fimi.app.x8s.viewHolder.CameraParamsViewHolder;

import java.util.List;


public class CameraEVParamsAdatper extends RecyclerView.Adapter {
    private final Context context;
    private final CameraEVParamListener listener;
    private final String paramKey;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.x8_iso_recycler_item, parent, false);
        return new CameraParamsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if ((holder instanceof CameraParamsViewHolder) && this.plist.size() > 0) {
            ((CameraParamsViewHolder) holder).initView(this.plist.get(position), this.isEnable);
            ((CameraParamsViewHolder) holder).upSelected(position == this.selectIndex);
            holder.itemView.setOnClickListener(v -> {
                if (CameraEVParamsAdatper.this.isEnable && CameraEVParamsAdatper.this.listener != null) {
                    CameraEVParamsAdatper.this.listener.updateParams(CameraEVParamsAdatper.this.paramKey, CameraEVParamsAdatper.this.plist.get(position));
                }
            });
        }
    }

    @Override
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
