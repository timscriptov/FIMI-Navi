package com.fimi.x8sdk.entity;

import com.fimi.kernel.fds.FdsUploadState;
import com.fimi.kernel.fds.IFdsFileModel;

/* loaded from: classes2.dex */
public abstract class X8FdsFile implements IFdsFileModel {
    protected String objectName;
    protected String filefdsUrl = "";
    protected FdsUploadState state = FdsUploadState.IDLE;
    protected String fileSuffix = "";
    protected String fileSuffixCollect = "";
    private int itemPostion;
    private int sectionPostion;

    public int getSectionPostion() {
        return this.sectionPostion;
    }

    public void setSectionPostion(int sectionPostion) {
        this.sectionPostion = sectionPostion;
    }

    public int getItemPostion() {
        return this.itemPostion;
    }

    public void setItemPostion(int itemPostion) {
        this.itemPostion = itemPostion;
    }
}
