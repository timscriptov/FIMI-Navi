package com.fimi.app.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;


public class HostX8sProductView extends FrameLayout {
    public HostX8sProductView(@NonNull Context context) {
        super(context);
    }

    public HostX8sProductView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_main_x8s_product, this);
    }
}
