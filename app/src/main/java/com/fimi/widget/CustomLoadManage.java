package com.fimi.widget;

import android.content.Context;

import com.fimi.android.app.R;

public class CustomLoadManage {
    private static CustomLoadDialog sCustomLoadDialog;

    public static void show(Context context) {
        if (sCustomLoadDialog == null) {
            sCustomLoadDialog = new CustomLoadDialog(context, R.style.network_load_progress_dialog);
            sCustomLoadDialog.show();
        }
    }

    public static void showNoClick(Context context) {
        if (sCustomLoadDialog == null) {
            sCustomLoadDialog = new CustomLoadDialog(context, R.style.network_load_progress_dialog);
            sCustomLoadDialog.setCanceledOnTouchOutside(false);
            sCustomLoadDialog.setCancelable(false);
            sCustomLoadDialog.show();
        }
    }

    public static void x8ShowNoClick(Context context) {
        if (sCustomLoadDialog == null) {
            sCustomLoadDialog = new CustomLoadDialog(context, R.style.network_load_progress_dialog);
            sCustomLoadDialog.setCanceledOnTouchOutside(false);
            sCustomLoadDialog.setCancelable(false);
            sCustomLoadDialog.x8Show();
        }
    }

    public static void showNoClickWithOutProgressBar(Context context, boolean isShowPb) {
        if (sCustomLoadDialog == null) {
            sCustomLoadDialog = new CustomLoadDialog(context, R.style.network_load_progress_dialog, isShowPb);
            sCustomLoadDialog.setCanceledOnTouchOutside(false);
            sCustomLoadDialog.setCancelable(false);
            sCustomLoadDialog.x8Show();
        }
    }

    public static void dismiss() {
        if (sCustomLoadDialog != null) {
            sCustomLoadDialog.dismiss();
            sCustomLoadDialog = null;
        }
    }
}
