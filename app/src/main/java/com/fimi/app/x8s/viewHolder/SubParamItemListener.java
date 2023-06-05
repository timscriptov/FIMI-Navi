package com.fimi.app.x8s.viewHolder;

import androidx.recyclerview.widget.RecyclerView;


public interface SubParamItemListener {
    void checkDetailParam(String str, String str2, int i, RecyclerView.ViewHolder viewHolder);

    void checkResolutionDetailParam(String str, String str2, String str3, int i, RecyclerView.ViewHolder viewHolder);

    void gotoParentItem();

    void setRecyclerScroller(boolean z);

    void styleParam(String str, int i);

    void updateAddContent(String str, String str2);
}
