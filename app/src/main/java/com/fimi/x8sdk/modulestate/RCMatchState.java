package com.fimi.x8sdk.modulestate;

import com.fimi.x8sdk.dataparser.AckRightRoller;
import com.fimi.x8sdk.dataparser.AutoRCMatchRt;


public class RCMatchState extends BaseState {
    private AckRightRoller ackRightRoller;
    private AutoRCMatchRt matchRt;

    public AckRightRoller getAckRightRoller() {
        return this.ackRightRoller;
    }

    public void setAckRightRoller(AckRightRoller ackRightRoller) {
        this.ackRightRoller = ackRightRoller;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    public AutoRCMatchRt getMatchRt() {
        return this.matchRt;
    }

    public void setMatchRt(AutoRCMatchRt matchRt) {
        this.matchRt = matchRt;
    }

    public String toString() {
        return "RCMatchState{matchRt=" + this.matchRt + '}';
    }
}
