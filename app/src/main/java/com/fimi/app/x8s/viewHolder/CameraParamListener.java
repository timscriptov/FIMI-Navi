package com.fimi.app.x8s.viewHolder;

import androidx.recyclerview.widget.RecyclerView;


public interface CameraParamListener {
    void gotoSubItem(String str, String str2, RecyclerView.ViewHolder viewHolder);

    void itemReturnBack(String str, String... strArr);
}
