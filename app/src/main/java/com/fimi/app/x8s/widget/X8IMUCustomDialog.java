package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;

/* loaded from: classes.dex */
public class X8IMUCustomDialog extends X8sBaseDialog {
    private final DialogInterface.OnKeyListener keyListener;

    public X8IMUCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull String messageTwo, @NonNull boolean isShowImage, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        this.keyListener = (dialogInterface, i, keyEvent) -> i == 4 && keyEvent.getRepeatCount() == 0;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.x8_imu_view_custom_dialog);
        TextView tvMessage = findViewById(R.id.tv_message);
        TextView tvMessageTwo = findViewById(R.id.tv_message_two);
        ImageView x8ImgvImucheckState = findViewById(R.id.x8_imgv_imucheck_state);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        if (message != null) {
            tvMessage.setText(message);
            if (message.equalsIgnoreCase(context.getString(R.string.x8_fc_item_imu_normal))) {
                tvMessage.setTextColor(context.getApplicationContext().getResources().getColor(R.color.x8_fc_imu_check_namal));
            } else {
                tvMessage.setTextColor(context.getApplicationContext().getResources().getColor(R.color.x8_fc_imu_check_exception));
            }
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.GONE);
        }
        if (messageTwo != null) {
            tvMessageTwo.setText(messageTwo);
            tvMessageTwo.setVisibility(View.VISIBLE);
        } else {
            tvMessageTwo.setVisibility(View.GONE);
        }
        if (isShowImage) {
            x8ImgvImucheckState.setVisibility(View.VISIBLE);
        } else {
            x8ImgvImucheckState.setVisibility(View.GONE);
        }
        Button tvSure = findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(v -> {
            X8IMUCustomDialog.this.dismiss();
            if (listener != null) {
                listener.onSingleButtonClick();
            }
        });
        setOnKeyListener(this.keyListener);
    }

    public X8IMUCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String messageTwo, @NonNull int imgSrc, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        this.keyListener = (dialogInterface, i, keyEvent) -> i == 4 && keyEvent.getRepeatCount() == 0;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.x8_imu_view_custom_dialog);
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setVisibility(View.GONE);
        TextView tvMessageTwo = findViewById(R.id.tv_message_two);
        ImageView x8ImgvImucheckState = findViewById(R.id.x8_imgv_imucheck_state);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        if (messageTwo != null) {
            tvMessageTwo.setText(messageTwo);
            tvMessageTwo.setVisibility(View.VISIBLE);
        } else {
            tvMessageTwo.setVisibility(View.GONE);
        }
        x8ImgvImucheckState.setImageResource(imgSrc);
        Button tvSure = findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(v -> {
            X8IMUCustomDialog.this.dismiss();
            if (listener != null) {
                listener.onSingleButtonClick();
            }
        });
        setOnKeyListener(this.keyListener);
    }

    public interface onDialogButtonClickListener {
        void onSingleButtonClick();
    }
}
