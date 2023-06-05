package com.fimi.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.album.entity.MediaModel;
import com.fimi.album.iview.INodataTip;
import com.fimi.album.iview.IRecycleAdapter;
import com.fimi.android.app.R;


public class PanelRecycleAdapter<T extends MediaModel> extends BaseRecycleAdapter {
    public static final String TAG = PanelRecycleAdapter.class.getName();
    private IRecycleAdapter mIRecycleAdapter;

    public PanelRecycleAdapter(Context context, INodataTip mINodataTip) {
        super(context, mINodataTip);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 16) {
            View view = LayoutInflater.from(this.context).inflate(R.layout.album_panel_view_holder, parent, false);
            RecyclerView.ViewHolder viewHolder = new PanelRecycleViewHolder(view);
            return viewHolder;
        }
        View view2 = LayoutInflater.from(this.context).inflate(R.layout.album_body_view_holder, parent, false);
        RecyclerView.ViewHolder viewHolder2 = new BodyRecycleViewHolder(view2);
        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (this.mIRecycleAdapter != null) {
            this.mIRecycleAdapter.onBindViewHolder(holder, position);
        }
    }

    public void setmIRecycleAdapter(IRecycleAdapter mIRecycleAdapter) {
        this.mIRecycleAdapter = mIRecycleAdapter;
    }
}
