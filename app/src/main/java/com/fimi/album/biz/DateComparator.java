package com.fimi.album.biz;

import com.fimi.album.entity.MediaModel;

import java.util.Comparator;

/* loaded from: classes.dex */
public class DateComparator {
    public static Comparator createDateComparator() {
        return new Comparator<MediaModel>() { // from class: com.fimi.album.biz.DateComparator.1
            @Override // java.util.Comparator
            public int compare(MediaModel mediaModel, MediaModel t1) {
                Long firstCreateTime = Long.valueOf(mediaModel.getCreateDate());
                Long seconedCreateTime = Long.valueOf(t1.getCreateDate());
                return seconedCreateTime.compareTo(firstCreateTime);
            }
        };
    }
}
