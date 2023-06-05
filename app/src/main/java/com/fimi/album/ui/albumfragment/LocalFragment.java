package com.fimi.album.ui.albumfragment;

import android.os.Bundle;

import com.fimi.album.entity.MediaModel;
import com.fimi.android.app.R;

/* loaded from: classes.dex */
public class LocalFragment extends BaseFragment {
    public static LocalFragment obtaion() {
        return obtaion(null);
    }

    public static LocalFragment obtaion(Bundle bundle) {
        LocalFragment mVideoFragment = new LocalFragment();
        if (bundle != null) {
            Bundle bundle1 = mVideoFragment.getArguments();
            bundle1.putAll(bundle);
        }
        return mVideoFragment;
    }

    @Override
        // com.fimi.album.ui.albumfragment.BaseFragment
    int getContentID() {
        return R.layout.album_fragment_local_media;
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // com.fimi.album.iview.INodataTip
    public void notifyAddCallback(MediaModel mediaModel) {
    }
}
