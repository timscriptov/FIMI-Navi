package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fimi.album.widget.DownloadStateView;
import com.fimi.album.widget.MediaStrokeTextView;
import com.fimi.android.app.R;
import com.fimi.kernel.utils.SizeTool;


public class BodyRecycleViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivSelect;
    public DownloadStateView mDownloadStateView;
    public TextView mFileSize;
    public ImageView mIvDownloadMask;
    public ImageView mIvDownloaded;
    public ImageView mIvSelectMask;
    public ImageView mIvVideoFlag;
    public TextView mTvDownloadState;
    public SimpleDraweeView sdvImageView;
    public MediaStrokeTextView tvDuringdate;

    public BodyRecycleViewHolder(View itemView) {
        super(itemView);
        this.sdvImageView = itemView.findViewById(R.id.simpledraweeview);
        this.ivSelect = itemView.findViewById(R.id.selected_iv);
        this.tvDuringdate = itemView.findViewById(R.id.duringdate_tv);
        this.tvDuringdate.getPaint().setFakeBoldText(true);
        this.mIvDownloadMask = itemView.findViewById(R.id.iv_download_mask);
        this.mIvSelectMask = itemView.findViewById(R.id.iv_select_mask);
        this.mIvVideoFlag = itemView.findViewById(R.id.iv_video_flag);
        this.mFileSize = itemView.findViewById(R.id.tv_filesize);
        this.mFileSize.getPaint().setFakeBoldText(true);
        this.mIvDownloaded = itemView.findViewById(R.id.iv_downloaded);
        this.mDownloadStateView = itemView.findViewById(R.id.download_state_view);
        this.mTvDownloadState = itemView.findViewById(R.id.tv_download_state);
    }

    private void initSimpleDraweeViewParams(@NonNull Context context, @NonNull View view) {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int screenWidth = mDisplayMetrics.widthPixels;
        int scteenHeight = mDisplayMetrics.heightPixels;
        if (screenWidth < scteenHeight) {
            screenWidth = scteenHeight;
        }
        layoutParams.width = ((screenWidth - (SizeTool.pixToDp(2.5f, context) * 3)) - (SizeTool.pixToDp(8.0f, context) * 2)) / 4;
        layoutParams.height = (layoutParams.width * 9) / 16;
        view.setLayoutParams(layoutParams);
    }

    private void initImageViewParams(Context context, @NonNull View parentView, @NonNull View view) {
        parentView.measure(0, 0);
        int currentHeight = parentView.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.addRule(11);
        layoutParams.rightMargin = SizeTool.pixToDp(12.0f, context);
        layoutParams.topMargin = currentHeight - SizeTool.pixToDp(25.0f, context);
        view.setLayoutParams(layoutParams);
    }

    private void initTextViewParams(Context context, @NonNull View parentView, @NonNull View view) {
        parentView.measure(0, 0);
        int currentHeight = parentView.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = SizeTool.pixToDp(10.0f, context);
        layoutParams.topMargin = currentHeight - SizeTool.pixToDp(24.0f, context);
        view.setLayoutParams(layoutParams);
    }
}
