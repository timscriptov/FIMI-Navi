package com.fimi.app.x8s.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fimi.android.app.R;
import com.fimi.app.x8s.interfaces.IX8AiLineHistoryListener;
import com.fimi.app.x8s.tools.X8NumberUtil;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.X8EditorCustomDialog;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.store.sqlite.helper.X8AiLinePointInfoHelper;
import com.fimi.kernel.utils.DateUtil;
import com.umeng.commonsdk.proguard.g;

import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;

public class X8AiLineHistoryAdapter2 extends RecyclerView.Adapter<X8AiLineHistoryAdapter2.HistoryViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<X8AiLinePointInfo> list;
    private final int type;
    String p;
    private IX8AiLineHistoryListener mX8AiLineSelectListener;

    public X8AiLineHistoryAdapter2(Context context, List<X8AiLinePointInfo> list, int type) {
        this.p = null;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.type = type;
        this.p = context.getString(R.string.x8_ai_fly_line_history_time_pattern);
    }

    @NonNull
    @Contract(pure = true)
    public static String secToTime(int time) {
        String timeStr;
        if (time <= 0) {
            return "00:00";
        }
        int minute = time / 60;
        if (minute == 0) {
            timeStr = time + g.ap;
        } else if (minute < 60) {
            int second = time % 60;
            timeStr = minute + "min " + second + g.ap;
        } else {
            int hour = minute / 60;
            if (hour > 99) {
                return "99:59:59";
            }
            int minute2 = minute % 60;
            int second2 = (time - (hour * 3600)) - (minute2 * 60);
            timeStr = hour + "h " + minute2 + "min " + second2 + g.ap;
        }
        return timeStr;
    }

    @NonNull
    public static String unitFormat(int i) {
        if (i >= 0 && i < 10) {
            return "0" + i;
        }
        return "" + i;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.x8_ai_line_history_item_layout, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        X8AiLinePointInfo info = this.list.get(position);
        holder.mTvItemTitle1.setText("" + (position + 1));
        holder.mTvItemTitle2.setText(info.getLocality());
        String time = DateUtil.getStringByFormat3(info.getTime(), this.p);
        holder.mTvItemTitle3.setText(time);
        holder.mTvItemTitle4.setText("" + getDistanceString(info.getDistance()));
        int t = (int) (info.getDistance() / (info.getSpeed() / 10.0f));
        holder.mTvItemTitle5.setText("" + secToTime(t));
        holder.mTvItemTitle6.setText(info.getName());
        if (info.getSaveFlag() == 1) {
            if (this.type == 0) {
                holder.mImgSaveFlag.setBackgroundResource(R.drawable.x8_btn_save_flag_selector);
            } else {
                holder.mImgSaveFlag.setBackgroundResource(R.drawable.x8_btn_cancle_save_flag_selector);
            }
        } else if (info.getSaveFlag() == 0) {
            holder.mImgSaveFlag.setBackgroundResource(R.drawable.x8_btn_unsave_flag_selector);
        }
        holder.rlSaveFlag.setTag(info);
        holder.rlSaveFlag.setOnClickListener(view -> {
            X8AiLinePointInfo info1 = (X8AiLinePointInfo) view.getTag();
            if (X8AiLineHistoryAdapter2.this.type != 0) {
                if (X8AiLineHistoryAdapter2.this.type == 1 && info1.getSaveFlag() == 1) {
                    X8AiLineHistoryAdapter2.this.showCancleDialog(info1, position);
                }
            } else if (info1.getSaveFlag() == 1) {
                info1.setSaveFlag(0);
                X8AiLinePointInfoHelper.getIntance().updatelineSaveFlag(0, info1.getId());
                if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                    X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position)).getId(), 0, position);
                }
                X8AiLineHistoryAdapter2.this.notifyItemChanged(position);
            } else if (info1.getSaveFlag() == 0) {
                int size = X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.favoritesCapacity();
                if (size == 20) {
                    X8AiLineHistoryAdapter2.this.showMaxSaveDialog();
                    return;
                }
                info1.setSaveFlag(1);
                X8AiLinePointInfoHelper.getIntance().updatelineSaveFlag(1, info1.getId());
                if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                    X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position)).getId(), 1, position);
                    X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.addLineItem((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position));
                }
                X8AiLineHistoryAdapter2.this.notifyItemChanged(position);
                if (info1.getName() != null && info1.getName().equals("")) {
                    X8AiLineHistoryAdapter2.this.showEditorDialog(position, info1);
                }
            }
        });
        holder.rlRootView.setOnClickListener(v -> {
            if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onSelectId(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(position)).getId(), X8AiLineHistoryAdapter2.this.type);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void addData(X8AiLinePointInfo info) {
        this.list.add(0, info);
        Collections.sort(this.list, (a, b) -> (int) (b.getId() - a.getId()));
        notifyDataSetChanged();
    }

    public void removeData(X8AiLinePointInfo info, int position) {
        this.list.remove(info);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.list.size() - position);
    }

    public void setOnX8AiLineSelectListener(IX8AiLineHistoryListener listener) {
        this.mX8AiLineSelectListener = listener;
    }

    public void showCancleDialog(final X8AiLinePointInfo info, final int index) {
        String t = this.context.getString(R.string.x8_ai_line_cancle_save_title);
        String m = this.context.getString(R.string.x8_ai_line_cancle_save_tip);
        String l = this.context.getString(R.string.cancel);
        String r = this.context.getString(R.string.x8_dialog_delete);
        X8DoubleCustomDialog dialog = new X8DoubleCustomDialog(this.context, t, m, l, r, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                info.setSaveFlag(0);
                X8AiLinePointInfoHelper.getIntance().updatelineSaveFlag(0, info.getId());
                if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                    X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(index)).getId(), 0, index);
                }
                X8AiLineHistoryAdapter2.this.list.remove(info);
                X8AiLineHistoryAdapter2.this.notifyItemRemoved(index);
                X8AiLineHistoryAdapter2.this.notifyItemRangeChanged(index, X8AiLineHistoryAdapter2.this.list.size() - index);
            }
        });
        dialog.show();
    }

    public void showMaxSaveDialog() {
        String t = this.context.getString(R.string.x8_ai_line_max_save_title);
        String m = this.context.getString(R.string.x8_ai_line_max_save_tip);
        String l = this.context.getString(R.string.x8_ai_line_save_cancle);
        String r = this.context.getString(R.string.x8_ai_line_save_ok);
        X8DoubleCustomDialog dialogMax = new X8DoubleCustomDialog(this.context, t, m, l, r, new X8DoubleCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onRight() {
                X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.goFavorites();
            }
        });
        dialogMax.show();
    }

    public String getDistanceString(float distance) {
        int d = Math.round(distance);
        return X8NumberUtil.getDistanceNumberString(d, 0, false);
    }

    public void showEditorDialog(final int positon, final X8AiLinePointInfo info1) {
        String t = this.context.getString(R.string.x8_ai_line_editor);
        final X8EditorCustomDialog dialogEditor = new X8EditorCustomDialog(this.context, t, new X8EditorCustomDialog.onDialogButtonClickListener() {
            @Override
            public void onLeft() {
            }

            @Override
            public void onCenter(String text) {
                info1.setName(text);
                X8AiLinePointInfoHelper.getIntance().updateLineName(text, info1.getId());
                if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                    X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(positon)).getId(), 1, positon);
                }
                X8AiLineHistoryAdapter2.this.notifyItemChanged(positon);
            }

            @Override
            public void onRight(String text) {
                if (!text.equals("")) {
                    info1.setName(text);
                    X8AiLinePointInfoHelper.getIntance().updateLineName(text, info1.getId());
                    if (X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener != null) {
                        X8AiLineHistoryAdapter2.this.mX8AiLineSelectListener.onItemChange(((X8AiLinePointInfo) X8AiLineHistoryAdapter2.this.list.get(positon)).getId(), 1, positon);
                    }
                    X8AiLineHistoryAdapter2.this.notifyItemChanged(positon);
                }
            }
        });
        dialogEditor.show();
        dialogEditor.setOnDismissListener(dialogInterface -> dialogEditor.hideSoftInputFromWindow());
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImgSaveFlag;
        private final View rlSaveFlag;
        public TextView mTvItemTitle1;
        public TextView mTvItemTitle2;
        public TextView mTvItemTitle3;
        public TextView mTvItemTitle4;
        public TextView mTvItemTitle5;
        public TextView mTvItemTitle6;
        public View rlRootView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            this.rlRootView = itemView.findViewById(R.id.rlRootView);
            this.mTvItemTitle1 = (TextView) itemView.findViewById(R.id.tvItme1);
            this.mTvItemTitle2 = (TextView) itemView.findViewById(R.id.tvItme2);
            this.mTvItemTitle3 = (TextView) itemView.findViewById(R.id.tvItme3);
            this.mTvItemTitle4 = (TextView) itemView.findViewById(R.id.tvItme4);
            this.mTvItemTitle5 = (TextView) itemView.findViewById(R.id.tvItme5);
            this.mTvItemTitle6 = (TextView) itemView.findViewById(R.id.tvItme6);
            this.mImgSaveFlag = (ImageView) itemView.findViewById(R.id.img_save_flag);
            this.rlSaveFlag = itemView.findViewById(R.id.rlSaveFlag);
        }
    }
}
