package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.section.X8B2oxSection;
import com.fimi.app.x8s.entity.X8B2oxFile;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.host.HostConstants;
import com.fimi.kernel.Constants;
import com.fimi.kernel.fds.FdsManager;
import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.store.shared.SPStoreManager;
import com.fimi.kernel.utils.DNSLookupThread;
import com.fimi.widget.X8ToastUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import router.Router;


public class X8B2oxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final Map<String, X8B2oxSection> sections = new LinkedHashMap<>();
    private X8DoubleCustomDialog loginDialogRestart;

    public X8B2oxAdapter(Context context) {
        this.context = context;
    }

    public void addSection(String title, X8B2oxSection section) {
        this.sections.put(title, section);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_black_box_header_layout, parent, false);
            return new X8B2oxHeaderViewHolder(view);
        } else if (viewType == 1) {
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_black_box_item_layout, parent, false);
            return new X8B2oxViewHolder(view2);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int currentPos = 0;
        for (Map.Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = entry.getValue();
            int sectionTotal = section.getSectionItemsTotal();
            if (position >= currentPos && position <= (currentPos + sectionTotal) - 1 && section.isHasHeader()) {
                if (position == currentPos) {
                    onHeaderBindViewHolder(holder, section.getTitle());
                } else {
                    onBindContentViewHolder(holder, getPositionInSection(position), position, section);
                }
            }
            currentPos += sectionTotal;
        }
    }

    private void onBindContentViewHolder(RecyclerView.ViewHolder holder, final int positionSection, final int position1, X8B2oxSection section) {
        final X8B2oxFile file = section.getList().get(positionSection);
        ((X8B2oxViewHolder) holder).mTvItemTitle1.setText(file.getNameShow());
        ((X8B2oxViewHolder) holder).mTvItemTitle3.setText(file.getShowLen());
        switch (file.getState()) {
            case IDLE:
                int resId = R.drawable.x8_img_upload_idel;
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(resId);
                break;
            case WAIT:
                int resId2 = R.drawable.x8_img_upload_wait;
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(resId2);
                break;
            case LOADING:
                int resId3 = R.drawable.x8_img_upload_runing;
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(resId3);
                if (((X8B2oxViewHolder) holder).mImgSaveFlag.getAnimation() == null) {
                    Animation rorateAnimation = AnimationUtils.loadAnimation(this.context, R.anim.rotate_anim);
                    rorateAnimation.setInterpolator(new LinearInterpolator());
                    ((X8B2oxViewHolder) holder).mImgSaveFlag.startAnimation(rorateAnimation);
                    break;
                }
                break;
            case SUCCESS:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_success);
                ((X8B2oxViewHolder) holder).mImgSaveFlag.clearAnimation();
                ((X8B2oxViewHolder) holder).rlSaveFlag.setOnClickListener(null);
                break;
            case FAILED:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_restart);
                ((X8B2oxViewHolder) holder).mImgSaveFlag.clearAnimation();
                break;
            case STOP:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(R.drawable.x8_img_upload_idel);
                ((X8B2oxViewHolder) holder).mImgSaveFlag.clearAnimation();
                break;
        }
        if (file.getState() != FdsUploadState.IDLE && file.getState() != FdsUploadState.STOP && file.getState() != FdsUploadState.FAILED) {
            return;
        }
        ((X8B2oxViewHolder) holder).rlSaveFlag.setOnClickListener(v -> {
            if (!DNSLookupThread.isDSNSuceess()) {
                X8ToastUtil.showToast(context, context.getString(R.string.x8_fds_connect_internet), 0);
            } else if (HostConstants.getUserDetail().getFimiId() != null && !HostConstants.getUserDetail().getFimiId().equals("")) {
                file.setSectionPostion(positionSection);
                file.setItemPostion(position1);
                FdsManager.getInstance().startDownload(file);
                notifyItemChanged(position1);
            } else {
                loginDialogRestart = new X8DoubleCustomDialog(context, context.getString(R.string.x8_modify_black_box_login_title), context.getString(R.string.x8_modify_black_box_login_content), context.getString(R.string.x8_modify_black_box_login_go), new X8DoubleCustomDialog.onDialogButtonClickListener() {
                    @Override
                    public void onLeft() {
                        loginDialogRestart.dismiss();
                    }

                    @Override
                    public void onRight() {
                        SPStoreManager.getInstance().saveInt(Constants.SP_PERSON_USER_TYPE, Constants.UserType.Ideal.ordinal());
                        Intent intent = Router.invoke(context, "activity://app.SplashActivity");
                        context.startActivity(intent);
                    }
                });
                loginDialogRestart.show();
            }
        });
    }

    public void onHeaderBindViewHolder(RecyclerView.ViewHolder holder, String title) {
        ((X8B2oxHeaderViewHolder) holder).tvTitle.setText(title);
    }

    public void uploadAll() {
        int position = 0;
        for (Map.Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = entry.getValue();
            int listSize = section.getSectionItemsTotal() - 1;
            position++;
            for (int i = 0; i < listSize; i++) {
                X8B2oxFile x8B2oxFile = section.getList().get(i);
                if (x8B2oxFile.getState() == FdsUploadState.IDLE || x8B2oxFile.getState() == FdsUploadState.STOP || x8B2oxFile.getState() == FdsUploadState.FAILED) {
                    x8B2oxFile.setSectionPostion(i);
                    x8B2oxFile.setItemPostion(position);
                    FdsManager.getInstance().startDownload(x8B2oxFile);
                }
                position++;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (Map.Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = entry.getValue();
            count += section.getSectionItemsTotal();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int currentPos = 0;
        for (Map.Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = entry.getValue();
            int sectionTotal = section.getSectionItemsTotal();
            if (position >= currentPos && position <= (currentPos + sectionTotal) - 1 && section.isHasHeader()) {
                return position == currentPos ? 0 : 1;
            }
            currentPos += sectionTotal;
        }
        throw new IndexOutOfBoundsException("Invalid position");
    }

    public int getPositionInSection(int position) {
        int currentPos = 0;
        for (Map.Entry<String, X8B2oxSection> entry : this.sections.entrySet()) {
            X8B2oxSection section = entry.getValue();
            int sectionTotal = section.getSectionItemsTotal();
            if (position >= currentPos && position <= (currentPos + sectionTotal) - 1) {
                return (position - currentPos) - (section.isHasHeader() ? 1 : 0);
            }
            currentPos += sectionTotal;
        }
        throw new IndexOutOfBoundsException("Invalid position");
    }

    public void clear() {
        this.sections.clear();
        notifyDataSetChanged();
    }


    public static class X8B2oxViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImgSaveFlag;
        private final View rlSaveFlag;
        public TextView mTvItemTitle1;
        public TextView mTvItemTitle2;
        public TextView mTvItemTitle3;
        public View rlRootView;


        public X8B2oxViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
            this.mTvItemTitle1 = itemView.findViewById(R.id.tvItme1);
            this.mTvItemTitle2 = itemView.findViewById(R.id.tvItme2);
            this.mTvItemTitle3 = itemView.findViewById(R.id.tvItme6);
            this.mImgSaveFlag = itemView.findViewById(R.id.img_save_flag);
            this.rlSaveFlag = itemView.findViewById(R.id.rlSaveFlag);
        }
    }

    public static class X8B2oxHeaderViewHolder extends RecyclerView.ViewHolder {
        public View rlRootView;
        public TextView tvTitle;


        public X8B2oxHeaderViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
            this.tvTitle = itemView.findViewById(R.id.tvItme1);
        }
    }
}
