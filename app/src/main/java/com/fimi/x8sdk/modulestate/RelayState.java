package com.fimi.x8sdk.modulestate;

import com.fimi.kernel.utils.BitUtil;
import com.fimi.x8sdk.dataparser.AckVersion;
import com.fimi.x8sdk.dataparser.AutoRelayHeart;

/* loaded from: classes2.dex */
public class RelayState extends BaseState {
    RelayState relayState;
    private long lastRlHeartTime;
    private AutoRelayHeart relayHeart;

    public void updateLastRlHeartTime() {
        this.lastRlHeartTime = System.currentTimeMillis();
    }

    public boolean isRlTimeOut() {
        return System.currentTimeMillis() - this.lastRlHeartTime >= 1500;
    }

    @Override // com.fimi.x8sdk.modulestate.BaseState
    public boolean isAvailable() {
        return true;
    }

    public RelayState getRelayState() {
        return this.relayState;
    }

    public void setRelayState(RelayState relayState) {
        this.relayState = relayState;
    }

    public AckVersion getRelayVersion() {
        return StateManager.getInstance().getVersionState().getModuleRepeaterRcVersion();
    }

    public boolean isConnect() {
        AckVersion version = StateManager.getInstance().getVersionState().getModuleRepeaterRcVersion();
        return version != null && version.getSoftVersion() > 0;
    }

    public AutoRelayHeart getRelayHeart() {
        return this.relayHeart;
    }

    public void setRelayHeart(AutoRelayHeart relayHeart) {
        this.relayHeart = relayHeart;
    }

    public int getApModel() {
        if (this.relayHeart == null) {
            return 0;
        }
        return BitUtil.getBitByByte(this.relayHeart.getStatus(), 10);
    }
}
