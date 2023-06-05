package com.fimi.widget.sticklistview.util;

import android.content.Context;
import android.widget.Checkable;

public class CheckableWrapperView extends WrapperView implements Checkable {
    public CheckableWrapperView(Context context) {
        super(context);
    }

    @Override
    public boolean isChecked() {
        return ((Checkable) this.mItem).isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        ((Checkable) this.mItem).setChecked(checked);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
