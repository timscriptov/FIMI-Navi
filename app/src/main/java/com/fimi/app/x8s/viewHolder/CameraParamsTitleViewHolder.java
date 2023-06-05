package com.fimi.app.x8s.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;

/* loaded from: classes.dex */
public class CameraParamsTitleViewHolder extends RecyclerView.ViewHolder {
    private ImageView backBtn;
    private TextView paramView;

    public CameraParamsTitleViewHolder(View itemView) {
        super(itemView);
        this.paramView = (TextView) itemView.findViewById(R.id.params_key);
        this.backBtn = (ImageView) itemView.findViewById(R.id.item_back_btn);
    }

    public void initView(String params) {
        this.paramView.setText(params);
    }

    public void upSelected(boolean selected) {
        this.paramView.setSelected(selected);
    }
}
