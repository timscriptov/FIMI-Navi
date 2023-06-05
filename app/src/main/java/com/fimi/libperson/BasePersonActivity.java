package com.fimi.libperson;

import android.view.View;

import com.fimi.kernel.base.BaseActivity;

/* loaded from: classes.dex */
public abstract class BasePersonActivity extends BaseActivity implements View.OnClickListener {
    @Override
    // com.fimi.kernel.base.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    protected void initMVP() {
    }

    @Override // com.fimi.kernel.base.BaseActivity
    public void setStatusBarColor() {
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }
}
