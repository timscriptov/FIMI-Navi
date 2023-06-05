package com.fimi.app.x8s.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;

/* loaded from: classes.dex */
public class HeadRecyleViewHolder extends RecyclerView.ViewHolder {
    public TextView mTvHeard;

    public HeadRecyleViewHolder(View itemView) {
        super(itemView);
        this.mTvHeard = (TextView) itemView.findViewById(R.id.tv_head);
        this.mTvHeard.setText("视频多少");
    }
}
