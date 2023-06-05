package com.fimi.album.ui;

import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.iview.IVideoPlayer;
import com.fimi.album.presenter.VideoPlayerPresneter;
import com.fimi.album.widget.CustomVideoView;
import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseActivity;


public class VideoPlayActivity extends BaseActivity implements CustomVideoView.VideoPlayerListener, IVideoPlayer {
    private CustomVideoView mCustomVideoView;
    private RelativeLayout mRl;
    private String mUrl;
    private VideoPlayerPresneter mVideoPlayerPresneter;

    @Override
    protected void setStatusBarColor() {
    }

    @Override
    public void initData() {
        this.mRl = findViewById(R.id.activity_video_play);
        this.mUrl = getIntent().getStringExtra(AlbumConstant.VIDEOPLARURL);
        this.mVideoPlayerPresneter = new VideoPlayerPresneter(this);
        this.mCustomVideoView = new CustomVideoView(this, this.mRl);
        this.mCustomVideoView.setDataSource(this.mUrl);
        this.mCustomVideoView.setListener(this);
        this.mRl.addView(this.mCustomVideoView);
    }

    @Override
    public void doTrans() {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.album_activity_video_play;
    }

    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.mVideoPlayerPresneter != null) {
            this.mVideoPlayerPresneter.startPlay();
        }
    }

    @Override
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        if (this.mVideoPlayerPresneter != null) {
            this.mVideoPlayerPresneter.removeCallBack();
        }
    }

    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mCustomVideoView.destory();
        this.mRl.removeView(this.mCustomVideoView);
    }

    @Override
    // android.support.v7.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBufferUpdate(int time) {
    }

    @Override
    public void onClickFullScreenBtn() {
    }

    @Override
    public void onClickVideo() {
    }

    @Override
    public void onClickBackBtn() {
        finish();
    }

    @Override
    public void onClickPlay() {
    }

    @Override
    public void onAdVideoLoadSuccess() {
    }

    @Override
    public void onAdVideoLoadFailed() {
    }

    @Override
    public void onAdVideoLoadComplete() {
        finish();
    }

    @Override
    public void timeFunction() {
        if (this.mCustomVideoView != null) {
            this.mCustomVideoView.timeFunction();
        }
    }
}
