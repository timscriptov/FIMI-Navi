package com.fimi.x8sdk.presenter;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.fmlink4.Parser4;
import com.fimi.kernel.percent.PercentRelativeLayout;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.kernel.utils.DesEncryptUtil;
import com.fimi.kernel.utils.ThreadUtils;
import com.fimi.x8sdk.dataparser.AckGetLowPowerOpt;
import com.fimi.x8sdk.dataparser.AckGetRcMode;
import com.fimi.x8sdk.dataparser.AutoFcErrCode;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcBatteryPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcHeartPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcSignalStatePlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoFcSportStatePlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoHomeInfoPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoRelayHeartPlayback;
import com.fimi.x8sdk.dataparser.flightplayback.AutoRockerStatePlayback;
import com.fimi.x8sdk.ivew.IFlightPlayBackAction;
import com.fimi.x8sdk.modulestate.DroneStateFlightPlayback;
import com.fimi.x8sdk.modulestate.ErrorCodeState;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class X8FlightPlayBackPresenter {
    public final int X8_PLAYBACK_PALYBACK = 1;
    private final int lengthIndex = 2;
    private final int X8_PLAYBACK_PARSEFILE_END = 0;
    private final long nextTime = 100;
    private final Parser4 p = new Parser4();
    private final ErrorCodeState errorCodeState = new ErrorCodeState();
    public LinkedHashMap<Integer, List<Object>> listLinkedHashMap;
    public int play2Second = 10;
    public PlayStatus mPlayStatus = PlayStatus.Payback;
    public volatile int currentProgress = 0;
    private List<byte[]> flightPlaybackAllDatas;
    private int groupId;
    private IFlightPlayBackAction iFlightPlayBackAction;
    private boolean isDisconnectDrone;
    private boolean isFileEnd;
    private boolean isX8FlightPlayBackEnd;
    private int msgId;
    private int packetLength;
    private int ptsb;
    private long total;
    private int totalTime;
    private PercentRelativeLayout x8ProgressLoading;
    private RandomAccessFile randomFile = null;
    private long offset = 0;
    private boolean isDroneDisConnectState = true;

    public void setOnFlightPlayBackAction(IFlightPlayBackAction iFlightPlayBackAction) {
        this.iFlightPlayBackAction = iFlightPlayBackAction;
    }

    public void setX8ProgressLoading(PercentRelativeLayout x8ProgressLoading) {
        this.x8ProgressLoading = x8ProgressLoading;
    }

    public void parseFileDate(final String filePath) {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                if (filePath != null && !filePath.equals("")) {
                    X8FlightPlayBackPresenter.this.offset = 0L;
                    try {
                        try {
                            int totalPtsdTime = X8FlightPlayBackPresenter.this.readTotalPtsdTime(filePath);
                            X8FlightPlayBackPresenter.this.flightPlaybackAllDatas = new ArrayList();
                            X8FlightPlayBackPresenter.this.randomFile = new RandomAccessFile(filePath, "r");
                            X8FlightPlayBackPresenter.this.total = X8FlightPlayBackPresenter.this.randomFile.length();
                            while (!X8FlightPlayBackPresenter.this.isFileEnd) {
                                X8FlightPlayBackPresenter.this.randomFile.seek(X8FlightPlayBackPresenter.this.offset + 2);
                                byte[] packetLengthBytes = new byte[2];
                                X8FlightPlayBackPresenter.this.randomFile.read(packetLengthBytes);
                                X8FlightPlayBackPresenter.this.packetLength = ByteUtil.byteToShort(packetLengthBytes, 0);
                                X8FlightPlayBackPresenter.this.randomFile.seek(X8FlightPlayBackPresenter.this.offset);
                                byte[] packetDatas = new byte[X8FlightPlayBackPresenter.this.packetLength];
                                X8FlightPlayBackPresenter.this.randomFile.read(packetDatas);
                                X8FlightPlayBackPresenter.this.offset += X8FlightPlayBackPresenter.this.packetLength;
                                if (X8FlightPlayBackPresenter.this.offset >= X8FlightPlayBackPresenter.this.total || X8FlightPlayBackPresenter.this.isEndPacket(packetDatas)) {
                                    X8FlightPlayBackPresenter.this.isFileEnd = true;
                                    X8FlightPlayBackPresenter.this.toFlightPlaybackEntity(X8FlightPlayBackPresenter.this.flightPlaybackAllDatas, totalPtsdTime);
                                } else if (!X8FlightPlayBackPresenter.this.flightPlaybackAllDatas.contains(packetDatas)) {
                                    X8FlightPlayBackPresenter.this.flightPlaybackAllDatas.add(packetDatas);
                                }
                            }
                            try {
                                if (X8FlightPlayBackPresenter.this.randomFile != null) {
                                    X8FlightPlayBackPresenter.this.randomFile.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e2) {
                            if (X8FlightPlayBackPresenter.this.x8ProgressLoading != null) {
                                X8FlightPlayBackPresenter.this.x8ProgressLoading.setVisibility(View.GONE);
                            }
                            e2.printStackTrace();
                            try {
                                if (X8FlightPlayBackPresenter.this.randomFile != null) {
                                    X8FlightPlayBackPresenter.this.randomFile.close();
                                }
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            if (X8FlightPlayBackPresenter.this.randomFile != null) {
                                X8FlightPlayBackPresenter.this.randomFile.close();
                            }
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                        throw th;
                    }
                }
            }
        });
    }

    public void toFlightPlaybackEntity(final List<byte[]> flightPlaybackAllDatas, final int totalPtsdTime) {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                int cacheTime = 0;
                int currPtsb = 0;
                ArrayList arrayList = new ArrayList();
                X8FlightPlayBackPresenter.this.listLinkedHashMap = new LinkedHashMap<>();
                for (byte[] bytes : flightPlaybackAllDatas) {
                    X8FlightPlayBackPresenter.this.ptsb = ByteUtil.bytesToInt(bytes, 4) / 100;
                    int diffPtsb = X8FlightPlayBackPresenter.this.ptsb - currPtsb;
                    if (diffPtsb > 10 && diffPtsb < 600) {
                        for (int i = 1; i < diffPtsb; i++) {
                            List<Object> list = new ArrayList<>();
                            list.add(new Object());
                            X8FlightPlayBackPresenter.this.listLinkedHashMap.put(Integer.valueOf(currPtsb + i), list);
                        }
                    } else {
                        byte[] packetData = new byte[bytes.length - 8];
                        System.arraycopy(bytes, 8, packetData, 0, packetData.length);
                        byte[] packetData2 = DesEncryptUtil.getInstans().desCbcDecrypt(packetData, DesEncryptUtil.getInstans().ENCRYPT_KEY_BYTES);
                        if (X8FlightPlayBackPresenter.this.ptsb <= 0) {
                            X8FlightPlayBackPresenter.this.ptsb = 0;
                        }
                        if (cacheTime == 0) {
                            cacheTime = X8FlightPlayBackPresenter.this.ptsb;
                        }
                        Object object = X8FlightPlayBackPresenter.this.byte2Object(packetData2);
                        if (X8FlightPlayBackPresenter.this.ptsb != cacheTime) {
                            cacheTime = X8FlightPlayBackPresenter.this.ptsb;
                            List<Object> list2 = new ArrayList<>();
                            list2.addAll(arrayList);
                            X8FlightPlayBackPresenter.this.listLinkedHashMap.put(Integer.valueOf(X8FlightPlayBackPresenter.this.ptsb), list2);
                            X8FlightPlayBackPresenter.this.totalTime = X8FlightPlayBackPresenter.this.ptsb;
                            arrayList.clear();
                            if (object != null) {
                                arrayList.add(object);
                            }
                        } else if (object != null) {
                            arrayList.add(object);
                        }
                    }
                    currPtsb = X8FlightPlayBackPresenter.this.ptsb;
                }
                int diffTotalPts = (totalPtsdTime / 100) - X8FlightPlayBackPresenter.this.ptsb;
                if (diffTotalPts > 1) {
                    for (int i2 = 1; i2 < diffTotalPts; i2++) {
                        List<Object> list3 = new ArrayList<>();
                        list3.add(new Object());
                        X8FlightPlayBackPresenter.this.listLinkedHashMap.put(Integer.valueOf(X8FlightPlayBackPresenter.this.ptsb + i2), list3);
                    }
                    X8FlightPlayBackPresenter.this.totalTime = totalPtsdTime / 100;
                }
                if (X8FlightPlayBackPresenter.this.listLinkedHashMap.size() > 0) {
                    X8FlightPlayBackPresenter.this.handler.obtainMessage(0, true).sendToTarget();
                } else {
                    X8FlightPlayBackPresenter.this.handler.obtainMessage(0, false).sendToTarget();
                }
            }
        });
    }

    public Object byte2Object(byte[] packetData) {
        if (packetData != null && packetData.length > 0) {
            for (byte b : packetData) {
                LinkPacket4 packet = this.p.unPacket(b & 255);
                if (packet != null) {
                    this.groupId = packet.getPayLoad4().getGroupId();
                    this.msgId = packet.getPayLoad4().getMsgId();
                    if (this.groupId == 12) {
                        switch (this.msgId) {
                            case 1:
                                AutoFcHeartPlayback autoFcHeartPlayback = new AutoFcHeartPlayback();
                                autoFcHeartPlayback.unPacket(packet);
                                return autoFcHeartPlayback;
                            case 2:
                                AutoFcSportStatePlayback sportStatePlayback = new AutoFcSportStatePlayback();
                                sportStatePlayback.unPacket(packet);
                                return sportStatePlayback;
                            case 3:
                                AutoFcSignalStatePlayback signalStatePlayback = new AutoFcSignalStatePlayback();
                                signalStatePlayback.unPacket(packet);
                                return signalStatePlayback;
                            case 4:
                                AutoFcErrCode fcErrCodePlayback = new AutoFcErrCode();
                                fcErrCodePlayback.unPacket(packet);
                                return fcErrCodePlayback;
                            case 5:
                                AutoFcBatteryPlayback batteryPlayback = new AutoFcBatteryPlayback();
                                batteryPlayback.unPacket(packet);
                                return batteryPlayback;
                            case 6:
                                AutoHomeInfoPlayback homeInfoPlayback = new AutoHomeInfoPlayback();
                                homeInfoPlayback.unPacket(packet);
                                return homeInfoPlayback;
                        }
                    } else if (this.groupId == 11) {
                        switch (this.msgId) {
                            case 2:
                                AutoRockerStatePlayback rockerStatePlayback = new AutoRockerStatePlayback();
                                rockerStatePlayback.unPacket(packet);
                                return rockerStatePlayback;
                            case 18:
                                AckGetRcMode rcMode = new AckGetRcMode();
                                rcMode.unPacket(packet);
                                return rcMode;
                        }
                    } else if (this.groupId == 14) {
                        if (this.msgId == 5) {
                            AutoRelayHeartPlayback relayHeartPlayback = new AutoRelayHeartPlayback();
                            relayHeartPlayback.unPacket(packet);
                            return relayHeartPlayback;
                        }
                    } else if (this.groupId == 4) {
                        if (this.msgId == 23) {
                            AckGetLowPowerOpt lowPowerOpt = new AckGetLowPowerOpt();
                            lowPowerOpt.unPacket(packet);
                            return lowPowerOpt;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgWhat = msg.what;
            switch (msgWhat) {
                case 0:
                    boolean isParseFileSucceed = ((Boolean) msg.obj).booleanValue();
                    int playTotalTime = Float.valueOf(Math.round(X8FlightPlayBackPresenter.this.totalTime / (X8FlightPlayBackPresenter.this.play2Second * 1.0f))).intValue();
                    X8FlightPlayBackPresenter.this.iFlightPlayBackAction.parseFileDateEnd(playTotalTime, isParseFileSucceed);
                    return;
                case 1:
                    X8FlightPlayBackPresenter.this.iFlightPlayBackAction.setPlaybackProgress(X8FlightPlayBackPresenter.this.currentProgress / X8FlightPlayBackPresenter.this.play2Second, false);
                    X8FlightPlayBackPresenter.this.handlerOneFps(X8FlightPlayBackPresenter.this.currentProgress);
                    X8FlightPlayBackPresenter.this.currentProgress++;
                    if (X8FlightPlayBackPresenter.this.currentProgress % 5 == 0) {
                        if (X8FlightPlayBackPresenter.this.isDroneDisConnectState) {
                            X8FlightPlayBackPresenter.this.isDisconnectDrone = true;
                            X8FlightPlayBackPresenter.this.iFlightPlayBackAction.showDroneDisconnectState();
                        }
                        X8FlightPlayBackPresenter.this.isDroneDisConnectState = true;
                    }
                    if (X8FlightPlayBackPresenter.this.currentProgress == 1) {
                    }
                    if (X8FlightPlayBackPresenter.this.currentProgress <= X8FlightPlayBackPresenter.this.totalTime) {
                        X8FlightPlayBackPresenter.this.handler.sendEmptyMessageDelayed(1, 100L);
                        return;
                    }
                    X8FlightPlayBackPresenter.this.currentProgress = 0;
                    X8FlightPlayBackPresenter.this.iFlightPlayBackAction.setPlaybackProgress(X8FlightPlayBackPresenter.this.currentProgress, true);
                    X8FlightPlayBackPresenter.this.mPlayStatus = PlayStatus.Payback;
                    return;
                default:
            }
        }
    };

    public void handlerOneFps(int index) {
        if (this.listLinkedHashMap != null) {
            List<Object> list = this.listLinkedHashMap.get(Integer.valueOf(index));
            DroneStateFlightPlayback droneStateFlightPlayback = new DroneStateFlightPlayback();
            if (list != null) {
                for (Object obj : list) {
                    if (obj != null) {
                        if (obj instanceof AutoFcHeartPlayback autoFcHeartPlayback) {
                            droneStateFlightPlayback.setFcHeart(autoFcHeartPlayback);
                            if (this.isDisconnectDrone) {
                                if (droneStateFlightPlayback.isOnGround()) {
                                    this.isX8FlightPlayBackEnd = true;
                                }
                                this.isDisconnectDrone = false;
                            }
                            if (!this.isX8FlightPlayBackEnd) {
                                this.iFlightPlayBackAction.showAutoFcHeart(autoFcHeartPlayback, droneStateFlightPlayback);
                            }
                            this.isDroneDisConnectState = false;
                        } else if (obj instanceof AutoFcSignalStatePlayback autoFcSignalStatePlayback) {
                            this.iFlightPlayBackAction.showAutoFcSignalState(autoFcSignalStatePlayback);
                            this.isDroneDisConnectState = false;
                        } else if (obj instanceof AutoFcBatteryPlayback autoFcBatteryPlayback) {
                            this.iFlightPlayBackAction.showAutoFcBattery(autoFcBatteryPlayback);
                            this.isDroneDisConnectState = false;
                        } else if (obj instanceof AutoFcErrCode autoFcErrCodePlayback) {
                            this.errorCodeState.setErrorCode(autoFcErrCodePlayback);
                            this.iFlightPlayBackAction.showAutoFcErrCode(this.errorCodeState.getErrooInfo());
                            this.isDroneDisConnectState = false;
                        } else if (obj instanceof AutoFcSportStatePlayback autoFcSportStatePlayback) {
                            this.iFlightPlayBackAction.showAutoFcSportState(autoFcSportStatePlayback);
                            this.isDroneDisConnectState = false;
                        } else if (obj instanceof AutoHomeInfoPlayback autoHomeInfoPlayback) {
                            this.iFlightPlayBackAction.showAutoHomeInfo(autoHomeInfoPlayback);
                            this.isDroneDisConnectState = false;
                        } else if (obj instanceof AutoRockerStatePlayback autoRockerStatePlayback) {
                            this.iFlightPlayBackAction.showAutoRockerState(autoRockerStatePlayback);
                        } else if (obj instanceof AutoRelayHeartPlayback autoRelayHeartPlayback) {
                            this.iFlightPlayBackAction.showAutoRelayHeart(autoRelayHeartPlayback);
                            this.isDroneDisConnectState = false;
                        } else if (obj instanceof AckGetRcMode ackGetRcMode) {
                            this.iFlightPlayBackAction.showGetRcMode(ackGetRcMode);
                        } else if (obj instanceof AckGetLowPowerOpt ackGetLowPowerOpt) {
                            this.iFlightPlayBackAction.showGetLowPowerOpt(ackGetLowPowerOpt);
                            this.isDroneDisConnectState = false;
                        } else {
                            this.iFlightPlayBackAction.showRemoteControlDisconnectState();
                            this.isDroneDisConnectState = false;
                        }
                    }
                }
            }
        }
    }

    public int readTotalPtsdTime(String filePath) {
        int flightDuration = 0;
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                long fileSize = fileInputStream.getChannel().size();
                int offset = (int) (fileSize - 12);
                fileInputStream.skip(offset);
                if (fileSize != 0) {
                    byte[] bytes = new byte[12];
                    fileInputStream.read(bytes, 0, bytes.length);
                    if (bytes[0] == -3 && bytes[1] == 1) {
                        flightDuration = ByteUtil.bytesToInt(bytes, 4);
                    }
                }
                return flightDuration;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        } catch (Throwable th) {
            return 0;
        }
    }

    public void playFlightPlayback() {
        this.handler.obtainMessage(1).sendToTarget();
    }

    public void stopFlightPlayback() {
        this.handler.removeMessages(1);
    }

    public boolean isEndPacket(byte[] bytes) {
        return bytes.length == 12 && bytes[0] == -3;
    }

    public void removeMessages(int whatID) {
        if (this.handler.hasMessages(whatID)) {
            this.handler.removeMessages(whatID);
        }
    }

    public void sendEmptyMessageDelayed() {
        if (!this.handler.hasMessages(1)) {
            this.handler.sendEmptyMessageDelayed(1, 100L);
        }
    }

    public enum PlayStatus {
        Payback,
        Stop
    }




}
