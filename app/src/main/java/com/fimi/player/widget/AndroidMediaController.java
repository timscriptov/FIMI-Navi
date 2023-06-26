package com.fimi.player.widget;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;


public class AndroidMediaController extends FmMediaController implements IMediaController {
    private final ArrayList<View> mShowOnceArray;
    private ActionBar mActionBar;

    public AndroidMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public AndroidMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public AndroidMediaController(Context context) {
        super(context);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    private void initView(Context context) {
    }

    public void setSupportActionBar(@Nullable ActionBar actionBar) {
        this.mActionBar = actionBar;
        if (isShowing()) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    @Override
    public void show() {
        super.show();
        if (this.mActionBar != null) {
            this.mActionBar.show();
        }
        Iterator<View> it = this.mShowOnceArray.iterator();
        while (it.hasNext()) {
            View view = it.next();
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (this.mActionBar != null) {
            this.mActionBar.hide();
        }
        Iterator<View> it = this.mShowOnceArray.iterator();
        while (it.hasNext()) {
            View view = it.next();
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void showOnce(@NonNull View view) {
        this.mShowOnceArray.add(view);
        view.setVisibility(View.VISIBLE);
        show();
    }
}
