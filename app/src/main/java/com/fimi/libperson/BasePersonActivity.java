package com.fimi.libperson;

import android.view.View;

import com.fimi.kernel.base.BaseActivity;


public abstract class BasePersonActivity extends BaseActivity implements View.OnClickListener {
    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
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
