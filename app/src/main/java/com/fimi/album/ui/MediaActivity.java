package com.fimi.album.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.iview.ISelectData;
import com.fimi.album.presenter.MediaPresenter;
import com.fimi.album.ui.albumfragment.LocalFragment;
import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.FontUtil;


public class MediaActivity extends BaseActivity implements ISelectData {
    private Button btnCancalAll;
    private ImageButton btnQuitActivity;
    private Button btnSelectAll;
    private Button btnmode;
    private ImageButton ibQuitMedia;
    private LocalFragment localFragment;
    private MediaPresenter mMediaPresenter;
    private ProgressBar mProgressBar;
    private RelativeLayout rlHeadDirection;
    private RelativeLayout rlMediaSelectTop;
    private RelativeLayout rootViewGroup;
    private TextView tvSelectModeSize;

    @Override
    protected void setStatusBarColor() {
    }

    @Override
    public void initData() {
        this.mProgressBar = findViewById(R.id.loading);
        this.ibQuitMedia = findViewById(R.id.media_back_btn);
        this.tvSelectModeSize = findViewById(R.id.select_n_tv);
        this.rlHeadDirection = findViewById(R.id.head_direction);
        this.rlMediaSelectTop = findViewById(R.id.media_select_top_rl);
        this.btnSelectAll = findViewById(R.id.all_select_btn);
        this.btnCancalAll = findViewById(R.id.cancel_btn);
        this.btnmode = findViewById(R.id.media_select_btn);
        this.btnQuitActivity = findViewById(R.id.media_back_btn);
        this.rootViewGroup = findViewById(R.id.view_group);
        FontUtil.changeFontLanTing(getAssets(), this.btnmode, this.btnSelectAll, this.btnCancalAll, this.tvSelectModeSize);
        this.localFragment = (LocalFragment) getSupportFragmentManager().findFragmentById(R.id.media_fragment);
        if (this.localFragment == null) {
            this.localFragment = LocalFragment.obtaion();
            getSupportFragmentManager().beginTransaction().add(R.id.media_fragment, this.localFragment).commitAllowingStateLoss();
        }
        Intent intent = getIntent();
        String floderPath = null;
        if (intent != null) {
            floderPath = intent.getStringExtra(AlbumConstant.MEDIAPATH);
        }
        this.mMediaPresenter = new MediaPresenter(this);
        if (!TextUtils.isEmpty(floderPath)) {
            this.mMediaPresenter.forEachFile(floderPath);
        }
    }

    @Override
    public void doTrans() {
        this.ibQuitMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaActivity.this.finish();
            }
        });
        this.btnmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaActivity.this.rlMediaSelectTop.setVisibility(0);
                MediaActivity.this.mMediaPresenter.enterSelectMode(true, true);
            }
        });
        this.btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaActivity.this.btnSelectAll.getText().equals(MediaActivity.this.getString(R.string.media_select_all))) {
                    MediaActivity.this.mMediaPresenter.selectBtn(true);
                    MediaActivity.this.changeBtnSelectState(MediaActivity.this.getString(R.string.media_select_all_no), MediaActivity.this.btnSelectAll);
                    return;
                }
                MediaActivity.this.mMediaPresenter.selectBtn(false);
                MediaActivity.this.changeBtnSelectState(MediaActivity.this.getString(R.string.media_select_all), MediaActivity.this.btnSelectAll);
            }
        });
        this.btnCancalAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaActivity.this.rlMediaSelectTop.setVisibility(8);
                MediaActivity.this.mMediaPresenter.enterSelectMode(false, true);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.album_activity_main;
    }

    public void changeBtnSelectState(String changeText, Button button) {
        button.setText(changeText);
    }

    @Override
    public void onDestroy() {
        this.mMediaPresenter.reDefaultVaribale();
        super.onDestroy();
    }

    @Override
    public void selectSize(int size, long capacity) {
        changeShowSelectTextView(this.tvSelectModeSize, size, R.string.media_select_n_item);
    }

    @Override
    public void enterSelectMode() {
        this.rlMediaSelectTop.setVisibility(0);
        this.mMediaPresenter.enterSelectMode(true, false);
    }

    @Override
    public void quitSelectMode() {
        this.rlMediaSelectTop.setVisibility(8);
        this.mMediaPresenter.enterSelectMode(false, false);
    }

    @Override
    public void deleteFile() {
        this.rlMediaSelectTop.setVisibility(8);
        this.mMediaPresenter.enterSelectMode(false, false);
    }

    @Override
    public void allSelectMode(boolean isAll) {
    }

    @Override
    public void startDownload() {
    }

    @Override
    public void onDeleteComplete() {
    }

    @Override
    public void initComplete(boolean isCamera) {
    }

    @Override
    public void addSingleFile() {
    }

    public ProgressBar getmProgressBar() {
        return this.mProgressBar;
    }

    private void changeShowSelectTextView(TextView textView, int size, int resStr) {
        textView.setText(String.format(getString(resStr), Integer.valueOf(size)));
    }

    public LocalFragment getLocalFragment() {
        return this.localFragment;
    }
}
