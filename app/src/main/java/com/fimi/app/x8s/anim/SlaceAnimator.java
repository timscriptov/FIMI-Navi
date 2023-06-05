package com.fimi.app.x8s.anim;

import android.animation.ObjectAnimator;
import android.view.View;


public class SlaceAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 1.1f, 1.0f), ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 1.1f, 1.0f));
    }
}
