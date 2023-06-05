package com.fimi.app.x8s.interfaces;

import android.view.View;

import com.fimi.app.x8s.X8Application;


public abstract class AbsX8AiController extends AbsX8Controllers {
    public AbsX8AiController(View rootView) {
        super(rootView);
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void openUi() {
        super.openUi();
        X8Application.enableGesture = false;
    }

    @Override
    // com.fimi.app.x8s.interfaces.AbsX8Controllers, com.fimi.app.x8s.interfaces.IControllers
    public void closeUi() {
        super.closeUi();
        X8Application.enableGesture = true;
    }
}
