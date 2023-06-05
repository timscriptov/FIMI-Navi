package com.fimi.album.adapter;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;

import me.relex.photodraweeview.PhotoDraweeView;

/* loaded from: classes.dex */
public class MediaDetialViewHolder extends RecyclerView.ViewHolder {
    public PhotoDraweeView mPhotoDraweeView;
    public ProgressBar mProgressBar;
    public RelativeLayout mRlItem;

    public MediaDetialViewHolder(View itemView) {
        super(itemView);
        this.mRlItem = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        this.mPhotoDraweeView = (PhotoDraweeView) itemView.findViewById(R.id.photo_drawee_view);
        this.mProgressBar = (ProgressBar) itemView.findViewById(R.id.loading);
    }

    public void setRlItemBg(int color) {
        this.mRlItem.setBackgroundResource(color);
    }
}
