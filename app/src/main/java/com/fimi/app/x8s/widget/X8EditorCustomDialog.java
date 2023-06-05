package com.fimi.app.x8s.widget;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class X8EditorCustomDialog extends X8sBaseDialog {
    private final EditText mEtView;
    private final InputMethodManager inputManager;
    private boolean isShowInput;

    public X8EditorCustomDialog(@NonNull Context context, @Nullable String title, @NonNull final onDialogButtonClickListener listener) {
        super(context, R.style.fimisdk_custom_dialog);
        this.isShowInput = false;
        setContentView(R.layout.x8_editor_dialog_custom);
        if (title != null) {
            TextView tvTitle = findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        this.mEtView = findViewById(R.id.tv_message);
        this.inputManager = (InputMethodManager) this.mEtView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        final TextView tvLeft = findViewById(R.id.btn_left);
        final TextView tvRight = findViewById(R.id.btn_right);
        final View imgMiddle = findViewById(R.id.img_middle);
        final ImageView editorPic = findViewById(R.id.x8_dialog_editor_name_center_pic);
        final Button btnCenter = findViewById(R.id.x8_dialog_editor_name_center_ok);
        editorPic.setOnClickListener(view -> {
            X8EditorCustomDialog.this.mEtView.setVisibility(View.VISIBLE);
            tvRight.setVisibility(View.VISIBLE);
            tvLeft.setVisibility(View.VISIBLE);
            imgMiddle.setVisibility(View.VISIBLE);
            editorPic.setVisibility(View.GONE);
            btnCenter.setVisibility(View.GONE);
            X8EditorCustomDialog.this.showSoftInputFromWindow();
        });
        btnCenter.setOnClickListener(view -> {
            if (listener != null) {
                listener.onCenter("");
            }
            X8EditorCustomDialog.this.dismiss();
        });
        tvLeft.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLeft();
            }
            X8EditorCustomDialog.this.isShowInput = false;
            X8EditorCustomDialog.this.dismiss();
        });
        tvRight.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRight(X8EditorCustomDialog.this.mEtView.getText().toString().trim());
            }
            X8EditorCustomDialog.this.isShowInput = false;
            X8EditorCustomDialog.this.dismiss();
        });
    }

    public void showSoftInputFromWindow() {
        this.mEtView.setFocusable(true);
        this.mEtView.setFocusableInTouchMode(true);
        this.mEtView.requestFocus();
        if (this.inputManager != null) {
            this.inputManager.showSoftInput(this.mEtView, 0);
            this.isShowInput = true;
        }
    }

    public void hideSoftInputFromWindow() {
        if (this.inputManager != null && this.isShowInput) {
            this.inputManager.toggleSoftInput(0, 2);
            this.isShowInput = false;
        }
    }

    public interface onDialogButtonClickListener {
        void onCenter(String str);

        void onLeft();

        void onRight(String str);
    }
}
