package com.fimi.app.x8s.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.fimi.album.widget.HackyViewPager;
import com.fimi.android.app.R;
import com.fimi.app.x8s.adapter.X8CategoryAdapter;
import com.fimi.app.x8s.interfaces.IX8AiLineHistoryListener;
import com.fimi.app.x8s.tools.X8sNavigationBarUtils;
import com.fimi.app.x8s.ui.fragment.X8AiLineFavoritesFragment;
import com.fimi.app.x8s.ui.fragment.X8AiLineRecentFragment;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.utils.LanguageUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class X8AiLineHistoryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private ImageView imgReturn;
    private X8CategoryAdapter mCategolyAdapter;
    private X8AiLineFavoritesFragment mFavoritesFragment;
    private List<Fragment> mFragmentList;
    private HackyViewPager mHackyViewPager;
    private X8AiLineRecentFragment mRecentFragment;
    private TabLayout mTlTitleCategoly;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_x8_ai_line_history);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        X8sNavigationBarUtils.hideBottomUIMenu(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            X8sNavigationBarUtils.hideBottomUIMenu(this);
        }
    }

    public void initView() {
        this.mTlTitleCategoly = findViewById(R.id.tl_title_categoly);
        this.mHackyViewPager = findViewById(R.id.viewpaper);
        this.imgReturn = findViewById(R.id.img_return);
        this.mRecentFragment = new X8AiLineRecentFragment();
        this.mFavoritesFragment = new X8AiLineFavoritesFragment();
        this.mRecentFragment.setOnX8AiLineSelectListener(new IX8AiLineHistoryListener() {
            @Override
            public void onSelectId(long id, int type) {
                X8AiLineHistoryActivity.this.onSelectEvent(id, type);
            }

            @Override
            public void onItemChange(long id, int saveFlag, int potion) {
                X8AiLineHistoryActivity.this.mFavoritesFragment.notityItemChange(id, saveFlag);
            }

            @Override
            public void addLineItem(X8AiLinePointInfo info) {
                X8AiLineHistoryActivity.this.mFavoritesFragment.addLineItem(info);
            }

            @Override
            public int favoritesCapacity() {
                return X8AiLineHistoryActivity.this.mFavoritesFragment.favoritesCapacity();
            }

            @Override
            public void goFavorites() {
                X8AiLineHistoryActivity.this.mTlTitleCategoly.getTabAt(1).select();
            }

            @Override
            public void onItemChange(long id, String name, int position) {
                X8AiLineHistoryActivity.this.mFavoritesFragment.notityItemChange(id, name);
            }
        });
        this.mFavoritesFragment.setOnX8AiLineSelectListener(new IX8AiLineHistoryListener() {
            @Override
            public void onSelectId(long id, int type) {
                X8AiLineHistoryActivity.this.onSelectEvent(id, type);
            }

            @Override
            public void onItemChange(long id, int saveFlag, int position) {
                X8AiLineHistoryActivity.this.mRecentFragment.notityItemChange(id);
            }

            @Override
            public void addLineItem(X8AiLinePointInfo info) {
            }

            @Override
            public int favoritesCapacity() {
                return 0;
            }

            @Override
            public void goFavorites() {
            }

            @Override
            public void onItemChange(long id, String name, int position) {
            }
        });
        this.mFragmentList = new ArrayList();
        this.mFragmentList.add(this.mRecentFragment);
        this.mFragmentList.add(this.mFavoritesFragment);
        this.mCategolyAdapter = new X8CategoryAdapter(getSupportFragmentManager(), this.mFragmentList);
        this.mHackyViewPager.setAdapter(this.mCategolyAdapter);
        this.mHackyViewPager.setOverScrollMode(2);
        this.mTlTitleCategoly.setupWithViewPager(this.mHackyViewPager);
        int[] tabStringRes = {R.string.x8_ai_fly_line_history_title, R.string.x8_ai_fly_line_favorites_title};
        for (int index = 0; index < this.mTlTitleCategoly.getTabCount(); index++) {
            View tabItem = LayoutInflater.from(this).inflate(R.layout.x8_tab_ai_line_history_view, null);
            if (index == 0) {
                changeViewVariablw(tabItem, getResources().getColor(R.color.x8_value_select), 0, tabStringRes[index]);
            } else {
                changeViewVariablw(tabItem, getResources().getColor(R.color.white_100), 4, tabStringRes[index]);
            }
            TabLayout.Tab tab = this.mTlTitleCategoly.getTabAt(index);
            if (tab != null) {
                tab.setCustomView(tabItem);
            }
        }
        this.mTlTitleCategoly.addOnTabSelectedListener(this);
        this.imgReturn.setOnClickListener(this);
    }

    private void changeViewVariablw(View view, int resColor, int indicatorState, int resStr) {
        TextView tvTitleDescription = view.findViewById(R.id.tv_title_desprition);
        tvTitleDescription.setTextColor(resColor);
        if (resStr != 0) {
            tvTitleDescription.setText(resStr);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        changeViewVariablw(tab.getCustomView(), getResources().getColor(R.color.x8_value_select), 4, 0);
        this.mHackyViewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 1) {
            this.mFavoritesFragment.showMaxSaveDialog();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        changeViewVariablw(tab.getCustomView(), getResources().getColor(R.color.white_100), 4, 0);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_return) {
            finish();
        }
    }

    public void onSelectEvent(long id, int type) {
        Intent intent = getIntent();
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        setResult(-1, intent);
        finish();
    }
}
