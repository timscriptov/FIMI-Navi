package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.x8sdk.modulestate.StateManager;


public class FiveKeyDefineAdapter extends RecyclerView.Adapter<FiveKeyDefineAdapter.FiveKeyViewHolder> {
    private final String[] arr;
    private final int colorBlue;
    private final int colorWhite;
    private final Context context;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;
    private int selectIndex;

    public FiveKeyDefineAdapter(Context context, String[] arr) {
        this.arr = arr;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorWhite = context.getResources().getColor(R.color.white_100);
        this.colorBlue = context.getResources().getColor(R.color.x8_fc_all_setting_blue);
    }

    @NonNull
    @Override
    public FiveKeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.x8_main_rc_five_key_define_item, parent, false);
        return new FiveKeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FiveKeyViewHolder holder, final int position) {
        if (this.arr.length - 1 == position) {
            holder.btn.setVisibility(View.GONE);
        } else {
            holder.btn.setVisibility(View.VISIBLE);
        }
        holder.btn.setText(this.arr[position]);
        if (position == this.selectIndex && StateManager.getInstance().getX8Drone().isConnect()) {
            holder.btn.setTextColor(this.colorBlue);
            holder.btn.setAlpha(1.0f);
            holder.btn.setEnabled(true);
        } else if (!StateManager.getInstance().getX8Drone().isConnect()) {
            holder.btn.setTextColor(this.colorWhite);
            holder.btn.setAlpha(0.6f);
            holder.btn.setEnabled(false);
        } else {
            holder.btn.setTextColor(this.colorWhite);
            holder.btn.setAlpha(1.0f);
            holder.btn.setEnabled(true);
        }
        if (this.listener != null && this.selectIndex != position) {
            holder.btn.setOnClickListener(v -> {
                if (!arr[selectIndex].equalsIgnoreCase(holder.btn.getText().toString())) {
                    listener.onItemClicked(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.arr.length;
    }

    public void setItemSelect(int index) {
        if (index >= 0 && index <= this.arr.length - 1) {
            this.selectIndex = index;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClicked(int i);
    }


    public static class FiveKeyViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        public FiveKeyViewHolder(View itemView) {
            super(itemView);
            this.btn = itemView.findViewById(R.id.btn_item);
        }
    }
}
