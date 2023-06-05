package com.fimi.x8sdk.modulestate;

import com.fimi.x8sdk.dataparser.AckNoFlyNormal;

/* loaded from: classes2.dex */
public class NfzState extends BaseState {
    AckNoFlyNormal mAckNoFlyNormal;

    @Override // com.fimi.x8sdk.modulestate.BaseState
    public boolean isAvailable() {
        return false;
    }

    public AckNoFlyNormal getAckNoFlyNormal() {
        return this.mAckNoFlyNormal;
    }

    public void setAckNoFlyNormal(AckNoFlyNormal mAckNoFlyNormal) {
        this.mAckNoFlyNormal = mAckNoFlyNormal;
    }
}
