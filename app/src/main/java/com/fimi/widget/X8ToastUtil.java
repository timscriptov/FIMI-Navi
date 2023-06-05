package com.fimi.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fimi.android.app.R;

public class X8ToastUtil {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    private static Toast mTosat;

    public static void showToast(Context context, String content, int duration) {
        if (context != null) {
            if (mTosat == null) {
                if (duration == 0) {
                    mTosat = Toast.makeText(context, content, Toast.LENGTH_SHORT);
                } else if (duration == 1) {
                    mTosat = Toast.makeText(context, content, Toast.LENGTH_LONG);
                }
                View view = mTosat.getView();
                if (view != null) {
                    view.setBackgroundResource(R.drawable.x8_img_tost_bg);
                    TextView message = view.findViewById(android.R.id.message);
                    message.setTextColor(-1);
                    message.setGravity(17);
                }
            } else {
                mTosat.setText(content);
            }
            mTosat.show();
        }
    }
}
