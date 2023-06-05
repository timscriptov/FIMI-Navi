package com.fimi.app.x8s.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;


public class CameraParamsTitleViewHolder extends RecyclerView.ViewHolder {
    private final ImageView backBtn;
    private final TextView paramView;

    public CameraParamsTitleViewHolder(View itemView) {
        super(itemView);
        this.paramView = itemView.findViewById(R.id.params_key);
        this.backBtn = itemView.findViewById(R.id.item_back_btn);
    }

    public void initView(String params) {
        this.paramView.setText(params);
    }

    public void upSelected(boolean selected) {
        this.paramView.setSelected(selected);
    }
}
