package com.fimi.app.x8s.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/* loaded from: classes.dex */
public class FlashAnimator extends BaseViewAnimator {
    @Override // com.fimi.app.x8s.anim.BaseViewAnimator
    public void prepare(View target) {
        getAnimatorAgent().playTogether(ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.0f, 1.0f, 0.0f, 1.0f));
    }
}
