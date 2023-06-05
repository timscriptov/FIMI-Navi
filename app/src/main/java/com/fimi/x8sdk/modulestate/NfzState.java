package com.fimi.x8sdk.modulestate;

import com.fimi.x8sdk.dataparser.AckNoFlyNormal;


public class NfzState extends BaseState {
    AckNoFlyNormal mAckNoFlyNormal;

    @Override
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
