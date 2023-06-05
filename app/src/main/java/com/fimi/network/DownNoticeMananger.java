package com.fimi.network;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class DownNoticeMananger {
    private static DownNoticeMananger noticeMananger = new DownNoticeMananger();
    CopyOnWriteArrayList<IDownProgress> noticeList = new CopyOnWriteArrayList<>();

    public static DownNoticeMananger getDownNoticManger() {
        return noticeMananger;
    }

    public void addDownNoticeList(IDownProgress iDownProgress) {
        this.noticeList.add(iDownProgress);
    }

    public void remioveDownNoticeList(IDownProgress iDownProgress) {
        Iterator<IDownProgress> it = this.noticeList.iterator();
        while (it.hasNext()) {
            IDownProgress iProgress = it.next();
            if (iProgress == null || iProgress != iDownProgress) {
                if (0 != 0) {
                    this.noticeList.remove((Object) null);
                }
            } else {
                return;
            }
        }
    }

    public void remioveDownNoticeListAll() {
        if (this.noticeList != null) {
            this.noticeList.clear();
        }
    }

    public CopyOnWriteArrayList<IDownProgress> getNoticeList() {
        return this.noticeList;
    }
}
