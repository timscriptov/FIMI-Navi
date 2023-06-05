package com.fimi.album.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.FontUtil;


public class PanelRecycleViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivIconSelect;
    public RelativeLayout rlRightSelect;
    public TextView tvAllSelect;
    public TextView tvTitleDescription;

    public PanelRecycleViewHolder(View itemView) {
        super(itemView);
        this.tvTitleDescription = itemView.findViewById(R.id.title_description);
        this.rlRightSelect = itemView.findViewById(R.id.right_select);
        this.tvAllSelect = itemView.findViewById(R.id.all_select);
        this.ivIconSelect = itemView.findViewById(R.id.icon_select);
        FontUtil.changeFontLanTing(itemView.getContext().getAssets(), this.tvTitleDescription, this.tvAllSelect);
    }
}
