package com.fimi.app.x8s.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.kernel.utils.DensityUtil;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LanguageUtil;


public class PanelRecycleViewHolder extends RecyclerView.ViewHolder {
    public ImageView mBtnAllSelect;
    public TextView tvTitleDescription;

    public PanelRecycleViewHolder(View itemView) {
        super(itemView);
        this.tvTitleDescription = itemView.findViewById(R.id.title_description);
        this.mBtnAllSelect = itemView.findViewById(R.id.btn_all_select);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mBtnAllSelect.getLayoutParams();
        if (LanguageUtil.isZh()) {
            params.width = DensityUtil.dip2px(itemView.getContext(), 54.3f);
        } else {
            params.width = DensityUtil.dip2px(itemView.getContext(), 70.6f);
        }
        this.mBtnAllSelect.setLayoutParams(params);
        FontUtil.changeFontLanTing(itemView.getContext().getAssets(), this.tvTitleDescription, this.mBtnAllSelect);
    }
}
