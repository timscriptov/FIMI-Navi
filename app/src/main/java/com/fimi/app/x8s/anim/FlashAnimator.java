package com.fimi.app.x8s.anim;

import android.animation.ObjectAnimator;
import android.view.View;


public class FlashAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.0f, 1.0f, 0.0f, 1.0f));
    }
}
