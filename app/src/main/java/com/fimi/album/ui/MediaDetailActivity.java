package com.fimi.album.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.fimi.album.adapter.MediaDetailViewPaperAdapter;
import com.fimi.album.biz.AlbumConstant;
import com.fimi.album.presenter.MediaDetailPresenter;
import com.fimi.android.app.R;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.FontUtil;


public class MediaDetailActivity extends BaseActivity {
    private int currentSelectPosition;
    private ImageButton ibBottomDelete;
    private ImageButton ibMediaBack;
    private LinearLayout llHeadViewGroup;
    private MediaDetailPresenter mMediaDetailPresenter;
    private MediaDetailViewPaperAdapter mMediaDetailViewPaperAdapter;
    private RelativeLayout rlHeadDirection;
    private TextView tvDeleteTip;
    private TextView tvPhotoName;
    private ViewPager viewpaper;

    @Override
    protected void setStatusBarColor() {
    }

    @Override
    public void initData() {
        this.llHeadViewGroup = findViewById(R.id.shoto_top_tab_ll);
        this.rlHeadDirection = findViewById(R.id.media_select_bottom_rl);
        this.rlHeadDirection.setVisibility(0);
        this.viewpaper = findViewById(R.id.viewpaper);
        this.ibMediaBack = findViewById(R.id.media_back_btn);
        this.ibBottomDelete = findViewById(R.id.bottom_delete_ibtn);
        this.tvDeleteTip = findViewById(R.id.delete_tv);
        this.tvPhotoName = findViewById(R.id.photo_name_tv);
        FontUtil.changeFontLanTing(getAssets(), this.tvDeleteTip, this.tvPhotoName);
        Intent intent = getIntent();
        if (intent != null) {
            this.currentSelectPosition = intent.getIntExtra(AlbumConstant.SELECTPOSITION, 0);
        }
        this.mMediaDetailPresenter = new MediaDetailPresenter(this, this.viewpaper);
        this.mMediaDetailViewPaperAdapter = new MediaDetailViewPaperAdapter(this.mMediaDetailPresenter);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(0);
        this.viewpaper.setAdapter(this.mMediaDetailViewPaperAdapter);
        if (this.currentSelectPosition < this.mMediaDetailViewPaperAdapter.getCount()) {
            this.mMediaDetailPresenter.updateFileName(this.currentSelectPosition);
            this.viewpaper.setCurrentItem(this.currentSelectPosition);
        }
    }

    @Override
    public void doTrans() {
        this.ibMediaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaDetailActivity.this.finish();
            }
        });
        this.ibBottomDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaDetailActivity.this.mMediaDetailPresenter.deleteItem(MediaDetailActivity.this.viewpaper.getCurrentItem());
            }
        });
        this.viewpaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                MediaDetailActivity.this.mMediaDetailPresenter.updateItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.album_activity_media_detial;
    }

    public TextView getPhotoText() {
        return this.tvPhotoName;
    }

    public RelativeLayout getRlHeadDirection() {
        return this.rlHeadDirection;
    }

    public LinearLayout getLlHeadViewGroup() {
        return this.llHeadViewGroup;
    }
}
