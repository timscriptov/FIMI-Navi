package com.fimi.app.x8s.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.host.HostConstants;
import com.fimi.kernel.store.shared.SPStoreManager;

public class X8SingleCustomDialog extends X8sBaseDialog {

    public X8SingleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_view_custom_dialog);
        CheckBox x8CbSingDialog = findViewById(R.id.x8_cb_sing_dialog);
        x8CbSingDialog.setVisibility(View.GONE);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(message);
        Button tvSure = findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(v -> {
            X8SingleCustomDialog.this.dismiss();
            if (listener != null) {
                listener.onSingleButtonClick();
            }
        });
    }

    public X8SingleCustomDialog(@NonNull Context context, @Nullable String title, @NonNull String message, @NonNull String btnTxt, boolean isShowCheckBox, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        setContentView(R.layout.x8_view_custom_dialog);
        final CheckBox x8CbSingDialog = findViewById(R.id.x8_cb_sing_dialog);
        if (isShowCheckBox) {
            x8CbSingDialog.setVisibility(View.VISIBLE);
            x8CbSingDialog.setChecked(SPStoreManager.getInstance().getBoolean(HostConstants.SP_KEY_NOT_TIPS, false));
            x8CbSingDialog.setOnClickListener(view -> {
                if (x8CbSingDialog.isChecked()) {
                    SPStoreManager.getInstance().saveObject(HostConstants.SP_KEY_NOT_TIPS, true);
                } else {
                    SPStoreManager.getInstance().saveObject(HostConstants.SP_KEY_NOT_TIPS, false);
                }
            });
        } else {
            x8CbSingDialog.setVisibility(View.GONE);
        }
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        TextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(message);
        Button tvSure = findViewById(R.id.tv_sure);
        tvSure.setText(btnTxt);
        tvSure.setOnClickListener(v -> {
            X8SingleCustomDialog.this.dismiss();
            if (listener != null) {
                listener.onSingleButtonClick();
            }
        });
    }

    public interface onDialogButtonClickListener {
        void onSingleButtonClick();
    }
}
