package com.fimi.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;

public class CustomLoadDialog extends Dialog {
    private boolean isShowPb;
    private ProgressBar mPbLoad;

    public CustomLoadDialog(@NonNull Context context) {
        super(context);
        this.isShowPb = false;
    }

    public CustomLoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.isShowPb = false;
    }

    public CustomLoadDialog(@NonNull Context context, int themeResId, boolean isShowPb) {
        super(context, themeResId);
        this.isShowPb = false;
        this.isShowPb = isShowPb;
    }

    protected CustomLoadDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.isShowPb = false;
    }

    private static void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.setSystemUiVisibility(5894);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fimisdk_dialog_loading);
        if (this.isShowPb) {
            this.mPbLoad = findViewById(R.id.pb_load);
            this.mPbLoad.setVisibility(View.GONE);
        }
    }

    public void x8Show() {
        getWindow().setFlags(8, 8);
        show();
        fullScreenImmersive(getWindow().getDecorView());
        getWindow().clearFlags(8);
    }
}
