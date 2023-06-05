package com.fimi.app.x8s.ui.album.x8s;

import android.os.Build;
import android.view.View;

import com.fimi.android.app.R;
import com.fimi.app.x8s.widget.videoview.X8FmMediaInfo;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.permission.PermissionManager;
import com.fimi.player.widget.FimiVideoView;

/* loaded from: classes.dex */
public class X8FimiPlayerActivity extends BaseActivity implements FmMediaPlayer.OnActivityHander {
    private FmMediaPlayer mediaPlayer;

    @Override // com.fimi.kernel.base.BaseActivity
    protected void setStatusBarColor() {
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionManager.requestFind_LocationPermissions();
            PermissionManager.requestCoarseLocationPermissions();
            PermissionManager.requestStoragePermissions();
        }
        X8FmMediaInfo info = (X8FmMediaInfo) getIntent().getSerializableExtra(FmMediaPlayer.FM_MEDIA_INFO);
        getWindow().setFlags(1024, 1024);
        View controlView = findViewById(R.id.media_layout);
        FimiVideoView videoView = (FimiVideoView) findViewById(R.id.fimi_video);
        this.mediaPlayer = new FmMediaPlayer(videoView, info, this);
        this.mediaPlayer.initFmPlayer(this, controlView);
        this.mediaPlayer.startPlay();
        this.mediaPlayer.setShowContoller(true);
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.activity_x8_fimi_player;
    }

    @Override // com.fimi.app.x8s.ui.album.x8s.FmMediaPlayer.OnActivityHander
    public void onBack() {
        finish();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.onDestroy();
        }
    }
}
