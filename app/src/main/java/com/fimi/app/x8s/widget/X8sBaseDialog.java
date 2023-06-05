package com.fimi.app.x8s.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class X8sBaseDialog extends Dialog {
    public X8sBaseDialog(@NonNull Context context) {
        super(context);
    }

    public X8sBaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected X8sBaseDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.setSystemUiVisibility(5894);
        }
    }

    @Override
    public void show() {
        getWindow().setFlags(8, 8);
        super.show();
        fullScreenImmersive(getWindow().getDecorView());
        getWindow().clearFlags(8);
    }
}
