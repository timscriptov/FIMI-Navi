package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.entity.X8DroneInfoState;

import java.util.List;


public class X8DroneInfoStateAdapter extends RecyclerView.Adapter<X8DroneInfoStateAdapter.AdapterViewHolder> {
    private final List<X8DroneInfoState> list;
    private Context context;
    private OnEventClickListener mListener;

    public X8DroneInfoStateAdapter(List<X8DroneInfoState> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(this.context).inflate(R.layout.x8_main_all_setting_drone_info_state_item_normal, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, final int position) {
        X8DroneInfoState state = this.list.get(position);
        holder.tvName.setText(state.getName());
        holder.tvNormal.setText(state.getInfo());
        switch (state.getState()) {
            case NA:
            case NORMAL:
                holder.tvName.setTextColor(this.context.getResources().getColor(R.color.white_100));
                holder.tvNormal.setTextColor(this.context.getResources().getColor(R.color.white_100));
                holder.btnEvent.setVisibility(View.GONE);
                return;
            case MIDDLE:
                holder.tvName.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type3));
                holder.tvNormal.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type3));
                holder.btnEvent.setVisibility(View.GONE);
                return;
            case ERROR:
                holder.tvName.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type1));
                holder.tvNormal.setTextColor(this.context.getResources().getColor(R.color.x8_error_code_type1));
                if (state.getErrorEvent() != 0) {
                    holder.btnEvent.setVisibility(View.GONE);
                    holder.btnEvent.setOnClickListener(view -> mListener.onItemClick(position, list.get(position)));
                    return;
                }
                return;
            default:
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.mListener = listener;
    }


    public interface OnEventClickListener {
        void onItemClick(int i, X8DroneInfoState x8DroneInfoState);
    }


    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        Button btnEvent;
        TextView tvName;
        TextView tvNormal;


        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_name);
            this.tvNormal = itemView.findViewById(R.id.tv_normal);
            this.btnEvent = itemView.findViewById(R.id.btn_event);
        }
    }
}
