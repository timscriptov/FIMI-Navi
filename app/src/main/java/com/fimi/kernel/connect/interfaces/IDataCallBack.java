package com.fimi.kernel.connect.interfaces;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.dataparser.ILinkMessage;

/* loaded from: classes.dex */
public interface IDataCallBack {
    void onDataCallBack(int i, int i2, ILinkMessage iLinkMessage);

    void onSendTimeOut(int i, int i2, BaseCommand baseCommand);
}
