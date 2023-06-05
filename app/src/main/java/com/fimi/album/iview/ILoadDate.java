package com.fimi.album.iview;

import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public interface ILoadDate<T> {
    CopyOnWriteArrayList<T> allList();

    boolean isHadLoadDate();

    CopyOnWriteArrayList<T> photoList();

    CopyOnWriteArrayList<T> videoList();
}
