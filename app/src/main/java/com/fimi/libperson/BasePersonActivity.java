package com.fimi.libperson;

import android.view.View;

import com.fimi.kernel.base.BaseActivity;


public abstract class BasePersonActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void initMVP() {
    }

    @Override
    public void setStatusBarColor() {
    }

    @Override
    public void onClick(View view) {
    }
}
