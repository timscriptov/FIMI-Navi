package com.fimi.kernel.connect.interfaces;

import com.fimi.kernel.dataparser.milink.LinkPacket;

/* loaded from: classes.dex */
public interface ITimerSendQueueHandle {
    boolean removeFromTimerSendQueueByCmdID(int i, int i2, int i3, LinkPacket linkPacket);
}
