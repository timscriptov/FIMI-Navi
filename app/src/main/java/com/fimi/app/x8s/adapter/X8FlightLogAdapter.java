package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.section.X8FlightLogSection;
import com.fimi.app.x8s.interfaces.IX8FlightlogRenameFile;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.ui.activity.X8FlightPlaybackActivity;
import com.fimi.kernel.Constants;
import com.fimi.kernel.base.EventMessage;
import com.fimi.kernel.utils.TimerUtil;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.entity.X8FlightLogFile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.pattern.color.ANSIConstants;

/* loaded from: classes.dex */
public class X8FlightLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    X8FlightLogSection section;
    private Context context;
    private X8FlightLogFile furrentFile;
    private IX8FlightlogRenameFile mIX8FlightlogRenameFile;
    private String playbackDistance;
    private String playbackTotalTime;
    private Map<String, X8FlightLogSection> sections = new LinkedHashMap();

    public X8FlightLogAdapter(Context context, IX8FlightlogRenameFile renameFile) {
        this.context = context;
        this.mIX8FlightlogRenameFile = renameFile;
        EventBus.getDefault().register(this);
    }

    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ecentBusUI(EventMessage eventMessage) {
        if (eventMessage != null && eventMessage.getKey() == Constants.X8_FLIGHTLOG_EVENT_KEY) {
            boolean isCollect = ((Boolean) eventMessage.getMessage()).booleanValue();
            if (isCollect) {
                this.furrentFile.setFileLogCollectState("0");
                renameCollectFile(this.furrentFile, this.furrentFile.isFileLogCollect());
                sort(this.section.getList());
                notifyDataSetChanged();
                return;
            }
            this.furrentFile.setFileLogCollectState("1");
            renameCollectFile(this.furrentFile, this.furrentFile.isFileLogCollect());
            sort(this.section.getList());
            notifyDataSetChanged();
        }
    }

    public void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    public void addSection(String title, X8FlightLogSection section) {
        this.sections.put(title, section);
    }

    public X8FlightLogSection getSection() {
        if (this.sections.isEmpty()) {
            return null;
        }
        return this.sections.get("");
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_flight_log_header_layout, parent, false);
            return new X8B2oxHeaderViewHolder(view);
        } else if (viewType == 1) {
            View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.x8_flight_log_item_layout, parent, false);
            return new X8B2oxViewHolder(view2);
        } else {
            return null;
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int currentPos = 0;
        for (Map.Entry<String, X8FlightLogSection> entry : this.sections.entrySet()) {
            X8FlightLogSection section = entry.getValue();
            int sectionTotal = section.getSectionItemsTotal();
            if (position >= currentPos && position <= (currentPos + sectionTotal) - 1 && section.isHasHeader() && position != currentPos) {
                onBindContentViewHolder(holder, getPositionInSection(position), position, section);
            }
            currentPos += sectionTotal;
        }
    }

    private void onBindContentViewHolder(RecyclerView.ViewHolder holder, int positionSection, int position1, X8FlightLogSection section) {
        final X8FlightLogFile file = section.getList().get(positionSection);
        this.section = section;
        ((X8B2oxViewHolder) holder).x8FlightlogItmeDate.setText(file.getNameShow());
        ((X8B2oxViewHolder) holder).x8FlightlogItemSize.setText(file.getShowLen());
        float flightMileage = Float.parseFloat(file.getFlightMileage());
        if (flightMileage >= 1000.0f) {
            ((X8B2oxViewHolder) holder).x8FlightlogItemMileage.setText(X8NumberUtil.getDistanceNumberNoPrexString(flightMileage / 1000.0f, 1) + "km");
        } else if (flightMileage < 0.0f) {
            ((X8B2oxViewHolder) holder).x8FlightlogItemMileage.setText("N/A");
        } else {
            ((X8B2oxViewHolder) holder).x8FlightlogItemMileage.setText(file.getFlightMileage() + ANSIConstants.ESC_END);
        }
        ((X8B2oxViewHolder) holder).x8FlightlogItemTime.setText(TimerUtil.getInstance().stringForTime(file.getFlightDuration(), false));
        int i = R.drawable.x8_img_upload_success;
        switch (file.getState()) {
            case IDLE:
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(0);
                break;
            case SUCCESS:
                int resId = R.drawable.x8_img_playback_syn_end;
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setBackgroundResource(resId);
                ((X8B2oxViewHolder) holder).mImgSaveFlag.clearAnimation();
                ((X8B2oxViewHolder) holder).mImgSaveFlag.setOnClickListener(null);
                break;
        }
        if (file.isFileLogCollect()) {
            ((X8B2oxViewHolder) holder).x8IvFlightlogCollect.setVisibility(0);
        } else {
            ((X8B2oxViewHolder) holder).x8IvFlightlogCollect.setVisibility(4);
        }
        ((X8B2oxViewHolder) holder).rlRootView.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.adapter.X8FlightLogAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                X8FlightLogAdapter.this.furrentFile = file;
                Intent intent = new Intent(X8FlightLogAdapter.this.context, X8FlightPlaybackActivity.class);
                intent.putExtra(Constants.X8_FLIGHTLOG_PATH, file.getPlaybackFile().getAbsolutePath());
                X8FlightLogAdapter.this.context.startActivity(intent);
            }
        });
    }

    public void onHeaderBindViewHolder(RecyclerView.ViewHolder holder) {
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        int count = 0;
        for (Map.Entry<String, X8FlightLogSection> entry : this.sections.entrySet()) {
            X8FlightLogSection section = entry.getValue();
            count += section.getSectionItemsTotal();
        }
        return count;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        int currentPos = 0;
        for (Map.Entry<String, X8FlightLogSection> entry : this.sections.entrySet()) {
            X8FlightLogSection section = entry.getValue();
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
        for (Map.Entry<String, X8FlightLogSection> entry : this.sections.entrySet()) {
            X8FlightLogSection section = entry.getValue();
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

    public void sort(List<X8FlightLogFile> list) {
        Collections.sort(list, new Comparator<X8FlightLogFile>() { // from class: com.fimi.app.x8s.adapter.X8FlightLogAdapter.2
            @Override // java.util.Comparator
            public int compare(X8FlightLogFile arg0, X8FlightLogFile arg1) {
                int a = Integer.parseInt(arg1.getFileLogCollectState()) - Integer.parseInt(arg0.getFileLogCollectState());
                if (a != 0) {
                    return a < 0 ? 1 : -1;
                }
                int mark = arg1.getPlaybackFile().getName().compareTo(arg0.getPlaybackFile().getName());
                return mark;
            }
        });
    }

    private void renameCollectFile(X8FlightLogFile x8FlightLogFile, boolean isCollect) {
        String path;
        File file = x8FlightLogFile.getPlaybackFile();
        String path2 = file.getAbsolutePath();
        if (isCollect) {
            if (path2.contains(X8FcLogManager.prexSD)) {
                int index = path2.indexOf(X8FcLogManager.prexSD);
                StringBuffer stringBuffer = new StringBuffer(path2);
                stringBuffer.insert(index, X8FcLogManager.getInstance().prexCollect);
                path = stringBuffer.toString();
            } else {
                int index2 = path2.indexOf(X8FcLogManager.FLIGHT_PLAYBACK);
                StringBuffer stringBuffer2 = new StringBuffer(path2);
                stringBuffer2.insert(index2 - 1, X8FcLogManager.getInstance().prexCollect);
                path = stringBuffer2.toString();
            }
        } else {
            path = path2.replace(X8FcLogManager.getInstance().prexCollect, "");
        }
        File tmpFile = new File(path);
        file.renameTo(tmpFile);
        if (this.mIX8FlightlogRenameFile != null) {
            x8FlightLogFile.resetPlaybackFile(tmpFile);
            x8FlightLogFile.setPlaybackFile(tmpFile, false);
            this.mIX8FlightlogRenameFile.onRenameFileSuccess();
        }
    }

    public String getPlaybackDistance() {
        return this.playbackDistance;
    }

    public void setPlaybackDistance(String playbackDistance) {
        this.playbackDistance = playbackDistance;
    }

    public String getPlaybackTotalTime() {
        return this.playbackTotalTime;
    }

    public void setPlaybackTotalTime(String playbackTotalTime) {
        this.playbackTotalTime = playbackTotalTime;
    }

    /* loaded from: classes.dex */
    public class X8B2oxViewHolder extends RecyclerView.ViewHolder {
        public View rlRootView;
        public TextView x8FlightlogItemMileage;
        public TextView x8FlightlogItemSize;
        public TextView x8FlightlogItemTime;
        public TextView x8FlightlogItmeDate;
        private ImageView mImgSaveFlag;
        private ImageView x8IvFlightlogCollect;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public X8B2oxViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
            this.x8IvFlightlogCollect = (ImageView) itemView.findViewById(R.id.x8_iv_flightlog_collect);
            this.x8FlightlogItmeDate = (TextView) itemView.findViewById(R.id.x8_flightlog_itme_date);
            this.x8FlightlogItemMileage = (TextView) itemView.findViewById(R.id.x8_flightlog_item_mileage);
            this.x8FlightlogItemSize = (TextView) itemView.findViewById(R.id.x8_flightlog_item_size);
            this.x8FlightlogItemTime = (TextView) itemView.findViewById(R.id.x8_flightlog_item_time);
            this.mImgSaveFlag = (ImageView) itemView.findViewById(R.id.img_save_flag);
        }
    }

    /* loaded from: classes.dex */
    public class X8B2oxHeaderViewHolder extends RecyclerView.ViewHolder {
        public View rlRootView;

        public X8B2oxHeaderViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
        }
    }
}
