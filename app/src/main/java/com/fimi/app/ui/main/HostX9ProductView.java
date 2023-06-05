package com.fimi.app.ui.main;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.kernel.animutils.AnimationsContainer;


public class HostX9ProductView extends FrameLayout {
    AnimationsContainer.FramesSequenceAnimation animation;
    AnimationDrawable animationDrawable;
    ImageView imgX9Product;

    public HostX9ProductView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_main_x9_product, this);
        this.imgX9Product = findViewById(R.id.img_x9_produce);
        this.animation = AnimationsContainer.getInstance(R.array.x9_drone_frame_anim, 50).createProgressDialogAnim(this.imgX9Product);
    }

    public void startAnimation() {
        if (this.animation != null && !this.animation.isShouldRun()) {
            this.animation.start();
        }
    }

    public void stopnAnimation() {
        if (this.animation != null && this.animation.isShouldRun()) {
            this.animation.stop();
        }
    }
}
