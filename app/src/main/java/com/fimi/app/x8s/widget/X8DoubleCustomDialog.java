package com.fimi.app.x8s.widget;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class X8DoubleCustomDialog extends X8sBaseDialog {
    public CheckBox x8CbSingDialog;

    public X8DoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        this.x8CbSingDialog = findViewById(R.id.x8_cb_sing_dialog);
        this.x8CbSingDialog.setVisibility(View.GONE);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(message);
        TextView tvLeft = findViewById(R.id.btn_left);
        TextView tvRight = findViewById(R.id.btn_right);
        tvLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeft();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
        tvRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRight();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
    }

    public X8DoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, String btnRight, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        this.x8CbSingDialog = findViewById(R.id.x8_cb_sing_dialog);
        this.x8CbSingDialog.setVisibility(View.GONE);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(message);
        TextView tvLeft = findViewById(R.id.btn_left);
        TextView tvRight = findViewById(R.id.btn_right);
        tvRight.setText(btnRight);
        tvLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeft();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
        tvRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRight();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
    }

    public X8DoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, String btnLeft, String btnRight, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        this.x8CbSingDialog = findViewById(R.id.x8_cb_sing_dialog);
        this.x8CbSingDialog.setVisibility(View.GONE);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(message);
        TextView tvLeft = findViewById(R.id.btn_left);
        TextView tvRight = findViewById(R.id.btn_right);
        tvLeft.setText(btnLeft);
        tvRight.setText(btnRight);
        tvLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeft();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
        tvRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRight();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
    }

    public X8DoubleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, String btnLeft, String btnRight, String checkStr, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_double_dialog_custom);
        this.x8CbSingDialog = findViewById(R.id.x8_cb_sing_dialog);
        this.x8CbSingDialog.setVisibility(View.VISIBLE);
        this.x8CbSingDialog.setText(checkStr);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(message);
        TextView tvLeft = findViewById(R.id.btn_left);
        TextView tvRight = findViewById(R.id.btn_right);
        tvLeft.setText(btnLeft);
        tvRight.setText(btnRight);
        tvLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeft();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
        tvRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRight();
            }
            X8DoubleCustomDialog.this.dismiss();
        });
    }

    public interface onDialogButtonClickListener {
        void onLeft();

        void onRight();
    }
}
