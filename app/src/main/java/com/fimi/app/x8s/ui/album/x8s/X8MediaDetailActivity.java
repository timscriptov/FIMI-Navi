package com.fimi.app.x8s.ui.album.x8s;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.fimi.album.adapter.MediaDetailViewPaperAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.widget.HackyViewPager;
import com.fimi.album.widget.MediaDetailDownloadStateView;
import com.fimi.album.widget.MediaDownloadProgressView;
import com.fimi.android.app.R;
import com.fimi.app.x8s.tools.X8sNavigationBarUtils;
import com.fimi.app.x8s.ui.presenter.X8MediaDetailPresenter;
import com.fimi.app.x8s.widget.X8DoubleCustomDialog;
import com.fimi.app.x8s.widget.videoview.X8CustomVideoView;
import com.fimi.app.x8s.widget.videoview.X8FmMediaInfo;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.kernel.utils.LogUtil;
import com.fimi.widget.X8ToastUtil;

/* loaded from: classes.dex */
public class X8MediaDetailActivity extends BaseActivity implements X8CustomVideoView.VideoPlayerListener {
    private static final String TAG = "X8MediaDetailActivity";
    private int currentSelectPosition;
    private boolean isDataRefresh = false;
    private Button mBtnPlayMax;
    private Button mBtnStart;
    private X8CustomVideoView mCustomVideoView;
    private FragmentManager mFragmentManager;
    private ImageButton mIbtnBottomDelete;
    private ImageButton mIbtnDelete;
    private ImageButton mIbtnDwonload;
    private ImageButton mIbtnLeftSlide;
    private ImageButton mIbtnMediaBack;
    private ImageButton mIbtnRightSlide;
    private ImageView mIvTopBar;
    private MediaDetailDownloadStateView mMediaDetailDownloadStateView;
    private X8MediaDetailPresenter mMediaDetailPresenter;
    private MediaDetailViewPaperAdapter mMediaDetailViewPaperAdapter;
    private MediaDownloadProgressView mMediaDownloadProgressView;
    private RelativeLayout mRelativeLayout;
    private RelativeLayout mRlDelete;
    private RelativeLayout mRlDownload;
    private RelativeLayout mRlDownloadBottom;
    private RelativeLayout mRlSelectBottom;
    private TextView mTvDelete;
    private TextView mTvDownload;
    private TextView mTvFileName;
    private TextView mTvPercent;
    private HackyViewPager mViewPager;
    private TextView tvPhotoName;

    @Override // com.fimi.kernel.base.BaseActivity
    protected void setStatusBarColor() {
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
    }

    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        X8sNavigationBarUtils.hideBottomUIMenu(this);
        if (this.isDataRefresh) {
            this.mMediaDetailViewPaperAdapter.notifyDataSetChanged();
        }
    }

    @Override
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        this.isDataRefresh = true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            X8sNavigationBarUtils.hideBottomUIMenu(this);
        }
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void initData() {
        this.mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        this.mIvTopBar = (ImageView) findViewById(R.id.iv_top_bar);
        this.mViewPager = (HackyViewPager) findViewById(R.id.viewpaper);
        this.mIbtnMediaBack = (ImageButton) findViewById(R.id.media_back_btn);
        this.mIbtnBottomDelete = (ImageButton) findViewById(R.id.ibtn_delete);
        this.mBtnPlayMax = (Button) findViewById(R.id.btn_play_max);
        this.mIbtnLeftSlide = (ImageButton) findViewById(R.id.ibtn_left_slide);
        this.mIbtnRightSlide = (ImageButton) findViewById(R.id.ibtn_right_slide);
        this.mMediaDetailDownloadStateView = (MediaDetailDownloadStateView) findViewById(R.id.download_state_view);
        this.tvPhotoName = (TextView) findViewById(R.id.tv_photo_name);
        this.mTvFileName = (TextView) findViewById(R.id.tv_file_name);
        this.mTvPercent = (TextView) findViewById(R.id.tv_percent);
        this.mRlDelete = (RelativeLayout) findViewById(R.id.rl_delete);
        this.mRlDownload = (RelativeLayout) findViewById(R.id.rl_download);
        this.mIbtnDelete = (ImageButton) findViewById(R.id.ibtn_delete);
        this.mTvDelete = (TextView) findViewById(R.id.tv_bottom_delete);
        this.mIbtnDwonload = (ImageButton) findViewById(R.id.ibtn_download);
        this.mTvDownload = (TextView) findViewById(R.id.tv_bottom_download);
        this.mRlDownloadBottom = (RelativeLayout) findViewById(R.id.rl_media_download);
        this.mRlSelectBottom = (RelativeLayout) findViewById(R.id.rl_bottom_bar);
        this.mMediaDownloadProgressView = (MediaDownloadProgressView) findViewById(R.id.pv_progress);
        this.mBtnStart = (Button) findViewById(R.id.btn_start);
        FontUtil.changeFontLanTing(getAssets(), this.tvPhotoName);
        this.mIbtnDelete.setAlpha(1.0f);
        this.mIbtnDelete.setEnabled(true);
        this.mIbtnDwonload.setAlpha(1.0f);
        this.mIbtnDwonload.setEnabled(true);
        this.mTvDelete.setAlpha(1.0f);
        this.mTvDelete.setEnabled(true);
        this.mTvDownload.setAlpha(1.0f);
        this.mTvDownload.setEnabled(true);
        this.mMediaDownloadProgressView.setFrontColor(-16717571);
        this.mMediaDownloadProgressView.setMaxCount(100.0f);
        Intent intent = getIntent();
        if (intent != null) {
            this.currentSelectPosition = intent.getIntExtra(AlbumConstant.SELECTPOSITION, 0);
        }
        if (this.currentSelectPosition < 0) {
            this.currentSelectPosition = 0;
        }
        this.mMediaDetailPresenter = new X8MediaDetailPresenter(this, this.mViewPager);
        this.mMediaDetailViewPaperAdapter = new MediaDetailViewPaperAdapter(this.mMediaDetailPresenter);
        this.mMediaDetailViewPaperAdapter.notifyDataSetChanged();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(0);
        this.mViewPager.setAdapter(this.mMediaDetailViewPaperAdapter);
        if (this.currentSelectPosition < this.mMediaDetailViewPaperAdapter.getCount()) {
            this.mMediaDetailPresenter.updateFileName(this.currentSelectPosition);
            this.mViewPager.setCurrentItem(this.currentSelectPosition);
            this.mMediaDetailPresenter.updateItem(this.currentSelectPosition);
        }
        this.mFragmentManager = getSupportFragmentManager();
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void doTrans() {
        this.mIbtnMediaBack.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                X8MediaDetailActivity.this.finish();
                X8MediaDetailActivity.this.mMediaDetailPresenter.setOnDestory();
            }
        });
        this.mIbtnDelete.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                X8DoubleCustomDialog doubleCustomDialog = new X8DoubleCustomDialog(X8MediaDetailActivity.this.mContext, X8MediaDetailActivity.this.getString(R.string.x8_album_warn_tip), X8MediaDetailActivity.this.getString(R.string.album_dialog_delete_title), X8MediaDetailActivity.this.getString(R.string.media_delete), new X8DoubleCustomDialog.onDialogButtonClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.2.1
                    @Override
                    // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                    public void onLeft() {
                    }

                    @Override
                    // com.fimi.app.x8s.widget.X8DoubleCustomDialog.onDialogButtonClickListener
                    public void onRight() {
                        X8MediaDetailActivity.this.mMediaDetailPresenter.deleteItem(X8MediaDetailActivity.this.mViewPager.getCurrentItem());
                    }
                });
                doubleCustomDialog.show();
            }
        });
        this.mIbtnDwonload.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                X8MediaDetailActivity.this.mMediaDetailPresenter.downloadFile(X8MediaDetailActivity.this.mViewPager.getCurrentItem());
            }
        });
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.4
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
                X8MediaDetailActivity.this.mMediaDetailPresenter.updateItem(position);
                X8MediaDetailActivity.this.mMediaDetailPresenter.showTopBottom(position);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int state) {
            }
        });
        this.mIbtnLeftSlide.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                X8MediaDetailActivity.this.mViewPager.setCurrentItem(X8MediaDetailActivity.this.mViewPager.getCurrentItem() - 1, true);
            }
        });
        this.mIbtnRightSlide.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                X8MediaDetailActivity.this.mViewPager.setCurrentItem(X8MediaDetailActivity.this.mViewPager.getCurrentItem() + 1, true);
            }
        });
        this.mBtnPlayMax.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (X8MediaDetailActivity.this.mMediaDetailPresenter.isDownloadFinish()) {
                    X8MediaDetailActivity.this.mMediaDetailPresenter.startActivity();
                } else {
                    X8ToastUtil.showToast(X8MediaDetailActivity.this, X8MediaDetailActivity.this.getString(R.string.x8_download_hint), 1);
                }
            }
        });
        this.mBtnStart.setOnClickListener(new View.OnClickListener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaDetailActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                X8MediaDetailActivity.this.mMediaDetailPresenter.downloadFile(X8MediaDetailActivity.this.mViewPager.getCurrentItem());
            }
        });
    }

    @Override // com.fimi.kernel.base.BaseActivity
    protected int getContentViewLayoutID() {
        return R.layout.x8_activity_media_detial;
    }

    public TextView getPhotoText() {
        return this.tvPhotoName;
    }

    public ImageView getIvTopBar() {
        return this.mIvTopBar;
    }

    public ImageButton getIbtnBottomDelete() {
        return this.mIbtnBottomDelete;
    }

    public X8CustomVideoView getCustomVideoView() {
        if (this.mCustomVideoView == null) {
            this.mCustomVideoView = new X8CustomVideoView(this, this.mRelativeLayout);
        }
        return this.mCustomVideoView;
    }

    public RelativeLayout getRelativeLayout() {
        return this.mRelativeLayout;
    }

    public FragmentManager getMediaFragmentManager() {
        return this.mFragmentManager;
    }

    public ImageButton getIbtnMediaBack() {
        return this.mIbtnMediaBack;
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onBufferUpdate(int time) {
        LogUtil.i(TAG, "onBufferUpdate: time:" + time);
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onClickFullScreenBtn() {
        LogUtil.i(TAG, "onClickFullScreenBtn: ");
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onClickVideo() {
        LogUtil.i(TAG, "onClickVideo: ");
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onClickBackBtn() {
        LogUtil.i(TAG, "onClickBackBtn: ");
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onClickPlay(X8FmMediaInfo info) {
        LogUtil.i("moweiru", "onClickPlay===== " + info.toString());
        String name = this.mMediaDetailPresenter.getMediaFileName();
        if (name != null && !name.equals("")) {
            this.mMediaDetailPresenter.startActivity();
        }
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onVideoPause(boolean isPause) {
        this.mIbtnRightSlide.setVisibility(isPause ? 8 : 0);
        this.mIbtnLeftSlide.setVisibility(isPause ? 8 : 0);
        this.mViewPager.setScrollble(isPause ? false : true);
        LogUtil.i(TAG, "onVideoPause: ");
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onVideoLoadSuccess() {
        LogUtil.i(TAG, "onVideoLoadSuccess: ");
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onVideoLoadFailed() {
        LogUtil.i(TAG, "onVideoLoadFailed: ");
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void onVideoLoadComplete() {
        LogUtil.i(TAG, "onVideoLoadComplete: ");
    }

    @Override // com.fimi.app.x8s.widget.videoview.X8CustomVideoView.VideoPlayerListener
    public void showBar(boolean isShow) {
        if (isShow) {
            showTopBar(true);
            this.mMediaDetailPresenter.updateItem(this.mMediaDetailPresenter.getCurrentPosition());
            return;
        }
        showTopBar(false);
    }

    @Override
    // android.support.v7.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.mMediaDetailPresenter.setOnDestory();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showTopBar(boolean isShow) {
        this.mIvTopBar.setVisibility(isShow ? 0 : 4);
        this.mRlSelectBottom.setVisibility(isShow ? 0 : 4);
        this.mRlDownloadBottom.setVisibility(isShow ? 0 : 4);
        this.mIbtnMediaBack.setVisibility(isShow ? 0 : 4);
        this.tvPhotoName.setVisibility(isShow ? 0 : 4);
    }

    public boolean topBarShowing() {
        return this.mIvTopBar.getVisibility() == 0;
    }

    public MediaDetailViewPaperAdapter getMediaDetailViewPaperAdapter() {
        return this.mMediaDetailViewPaperAdapter;
    }

    public Button getBtnPlayMax() {
        return this.mBtnPlayMax;
    }

    public TextView getTvFileName() {
        return this.mTvFileName;
    }

    public TextView getTvPercent() {
        return this.mTvPercent;
    }

    public RelativeLayout getRlDelete() {
        return this.mRlDelete;
    }

    public RelativeLayout getRlDownload() {
        return this.mRlDownload;
    }

    public ImageButton getIbtnDelete() {
        return this.mIbtnDelete;
    }

    public ImageButton getIbtnDwonload() {
        return this.mIbtnDwonload;
    }

    public Button getBtnStart() {
        return this.mBtnStart;
    }

    public RelativeLayout getRlDownloadBottom() {
        return this.mRlDownloadBottom;
    }

    public RelativeLayout getRlSelectBottom() {
        return this.mRlSelectBottom;
    }

    public MediaDownloadProgressView getMediaDownloadProgressView() {
        return this.mMediaDownloadProgressView;
    }
}
