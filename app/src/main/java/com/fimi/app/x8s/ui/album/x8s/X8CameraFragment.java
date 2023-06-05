package com.fimi.app.x8s.ui.album.x8s;

import android.os.Bundle;

import com.fimi.android.app.R;

/* loaded from: classes.dex */
public class X8CameraFragment extends X8MediaBaseFragment {
    public static X8CameraFragment obtaion() {
        return obtaion(null);
    }

    public static X8CameraFragment obtaion(Bundle bundle) {
        X8CameraFragment x8CameraFragment = new X8CameraFragment();
        if (bundle != null) {
            Bundle bundle1 = x8CameraFragment.getArguments();
            bundle1.putAll(bundle);
        }
        return x8CameraFragment;
    }

    @Override
        // com.fimi.app.x8s.ui.album.x8s.X8MediaBaseFragment
    int getContentID() {
        return R.layout.x8_fragment_camera;
    }

    @Override
        // com.fimi.app.x8s.ui.album.x8s.X8MediaBaseFragment
    boolean judgeTypeCurrentFragment() {
        return true;
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mBaseFragmentPresenter.registerDownloadListerner();
    }

    public void unRegisterReciver() {
        if (this.mBaseFragmentPresenter != null) {
            this.mBaseFragmentPresenter.unRegisterReciver();
        }
    }

    public void onDisConnect() {
        this.rlMediaSelectBottom.setVisibility(8);
        this.mBaseFragmentPresenter.setEnterSelectMode(false);
    }
}
