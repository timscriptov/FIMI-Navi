package com.fimi.app.x8s.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class X8WithTimeDoubleCustomDialog extends X8sBaseDialog {
    final TextView tvRight;
    private final X8DoubleCustomDialog.onDialogButtonClickListener listener;
    private final Handler mHandler;
    private final String prex;
    private int i;

    @SuppressLint("HandlerLeak")
    public X8WithTimeDoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull final X8DoubleCustomDialog.onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        this.i = 10;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (X8WithTimeDoubleCustomDialog.this.i < 0) {
                    if (X8WithTimeDoubleCustomDialog.this.listener != null) {
                        X8WithTimeDoubleCustomDialog.this.listener.onRight();
                    }
                    X8WithTimeDoubleCustomDialog.this.dismiss();
                    return;
                }
                X8WithTimeDoubleCustomDialog.this.tvRight.setText(String.format(X8WithTimeDoubleCustomDialog.this.prex, X8WithTimeDoubleCustomDialog.this.i -= 1));
                X8WithTimeDoubleCustomDialog.this.mHandler.sendEmptyMessageDelayed(0, 1000L);
            }
        };
        setContentView(R.layout.x8_whith_time_double_dialog_custom);
        this.listener = listener;
        this.prex = context.getString(R.string.x8_battery_ok_time_tip);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(message);
        TextView tvLeft = findViewById(R.id.btn_left);
        this.tvRight = findViewById(R.id.btn_right);
        View viewById = findViewById(R.id.x8_cb_sing_dialog);
        viewById.setVisibility(View.INVISIBLE);
        tvLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeft();
            }
            X8WithTimeDoubleCustomDialog.this.dismiss();
        });
        this.tvRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRight();
            }
            X8WithTimeDoubleCustomDialog.this.dismiss();
        });
        this.tvRight.setText(String.format(this.prex, this.i));
    }

    @Override
    public void show() {
        super.show();
        this.mHandler.sendEmptyMessageDelayed(0, 1000L);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.mHandler.removeMessages(0);
    }
}
