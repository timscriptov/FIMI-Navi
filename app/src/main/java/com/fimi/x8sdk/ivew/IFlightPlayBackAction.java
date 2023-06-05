package com.fimi.x8sdk.ivew;

import com.fimi.x8sdk.dataparser.AckGetLowPowerOpt;
import com.fimi.x8sdk.dataparser.AckGetRcMode;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcBatteryPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcHeartPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcSignalStatePlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcSportStatePlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoHomeInfoPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoRelayHeartPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoRockerStatePlayback;
import com.fimi.x8sdk.entity.X8ErrorCodeInfo;
import com.fimi.x8sdk.modulestate.DroneStateFlightPlayback;

import java.util.List;

/* loaded from: classes2.dex */
public interface IFlightPlayBackAction {
    void parseFileDateEnd(int i, boolean z);

    void setPlaybackProgress(int i, boolean z);

    void showAutoFcBattery(AutoFcBatteryPlayback autoFcBatteryPlayback);

    void showAutoFcErrCode(List<X8ErrorCodeInfo> list);

    void showAutoFcHeart(AutoFcHeartPlayback autoFcHeartPlayback, DroneStateFlightPlayback droneStateFlightPlayback);

    void showAutoFcSignalState(AutoFcSignalStatePlayback autoFcSignalStatePlayback);

    void showAutoFcSportState(AutoFcSportStatePlayback autoFcSportStatePlayback);

    void showAutoHomeInfo(AutoHomeInfoPlayback autoHomeInfoPlayback);

    void showAutoRelayHeart(AutoRelayHeartPlayback autoRelayHeartPlayback);

    void showAutoRockerState(AutoRockerStatePlayback autoRockerStatePlayback);

    void showDroneDisconnectState();

    void showGetLowPowerOpt(AckGetLowPowerOpt ackGetLowPowerOpt);

    void showGetRcMode(AckGetRcMode ackGetRcMode);

    void showRemoteControlDisconnectState();
}
