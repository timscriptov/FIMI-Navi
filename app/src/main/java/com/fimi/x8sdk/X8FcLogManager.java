package com.fimi.x8sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.fimi.host.HostConstants;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.utils.ByteUtil;
import com.fimi.kernel.utils.DateUtil;
import com.fimi.kernel.utils.DesEncryptUtil;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.x8sdk.appsetting.DataJsonFactory;
import com.fimi.x8sdk.appsetting.ValueSensity;
import com.fimi.x8sdk.dataparser.AutoFcSportState;
import com.fimi.x8sdk.modulestate.StateManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class X8FcLogManager {
    public static final String FLIGHT_PLAYBACK = "playback";
    public static final byte FLIGHT_PLAYBACK_SIGN = -3;
    public static final int LENGTH_OF = 12;
    public static final float NA = -1000.0f;
    private static final byte[] rn = "\r\n".getBytes();
    public static String prexFC = "fc";
    public static String prexCM = "relay";
    public static String prexAPP = "setting";
    public static String prexFcStatus = "fcStatus";
    public static String prexSD = "_sd";
    private static volatile X8FcLogManager log;
    private static String currentWrite = "";
    private final int REBUILD_FC_LOG_MESSAGE = 0;
    private final int REBUILD_FLIGHT_PLAYBACK_LOG_MESSAGE = 1;
    private final int MILEAGE_FLIGHT_PLAYBACK_LOG_MESSAGE = 2;
    private final int MILEAGE_LAND_FLIGHT_PLAYBACK_LOG_MESSAGE = 3;
    public String prexCollect = "_collect";
    public LogState state = LogState.ONGROUND;
    public boolean closeRebuildflightPlaybackLog = false;
    public boolean closeRebuildFCLog = false;
    private FileOutputStream appLogOutputStream;
    private OutputStream cmOutputStream;
    private String currentWriteFile;
    private OutputStream fcOutputStream;
    private OutputStream flightPlaybackLogOutputStream;
    private boolean isWriteFirst;
    private boolean isWriteFlightPlaybackFirst;
    private volatile byte[] lowPowerData;
    private float originalDistance;
    private volatile byte[] rockerLandDownData;
    private volatile byte[] rockerModeData;
    private volatile byte[] rockerStateData;
    private long startTime;
    private AutoFcSportState autoFcSportState = null;
    private boolean isPreLandDown = true;

    public static X8FcLogManager getInstance() {
        if (log == null) {
            synchronized (X8FcLogManager.class) {
                if (log == null) {
                    log = new X8FcLogManager();
                }
            }
        }
        return log;
    }

    public String getCurrentWrite() {
        return currentWrite;
    }

    public void onDeviceStateChange(int type) {
        if (this.state == LogState.ONGROUND) {
            if (type == 1) {
                this.state = LogState.INSKY;
                initFileOutputStream();
            }
        } else if (this.state == LogState.INSKY && type == 0) {
            this.state = LogState.ONGROUND;
            closeFileOutputStream();
        }
    }

    public void initFileOutputStream() {
        File flightPlaybackPath;
        if (this.fcOutputStream == null || this.cmOutputStream == null || this.appLogOutputStream == null || this.flightPlaybackLogOutputStream == null) {
            String dirPath = DateUtil.getStringByFormat(System.currentTimeMillis(), "yyyy-MM-dd-HH-mm-ss-SSS");
            String dirPathPlayback = DateUtil.getStringByFormat(System.currentTimeMillis(), "yyyy-MM-dd-HH-mm-ss");
            currentWrite = dirPath;
            try {
                File f = new File(DirectoryPath.getX8B2oxPath() + "/" + dirPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                String uuid = UUID.randomUUID().toString();
                if (HostConstants.getUserDetail().getFimiId() != null && !HostConstants.getUserDetail().getFimiId().equals("")) {
                    flightPlaybackPath = new File(DirectoryPath.getX8LoginFlightPlaybackPath(uuid));
                } else {
                    flightPlaybackPath = new File(DirectoryPath.getX8flightPlaybackPath(uuid));
                }
                if (!flightPlaybackPath.exists()) {
                    flightPlaybackPath.mkdirs();
                }
                String fcName = dirPath + "." + prexFC;
                String cmName = dirPath + "." + prexCM;
                String appName = dirPath + "." + prexAPP;
                String str = dirPath + "." + prexFcStatus;
                String flightPlayback = dirPathPlayback + "." + FLIGHT_PLAYBACK;
                this.currentWriteFile = flightPlayback;
                this.fcOutputStream = new FileOutputStream(f.getAbsolutePath() + "/" + fcName, true);
                this.cmOutputStream = new FileOutputStream(f.getAbsolutePath() + "/" + cmName, true);
                this.appLogOutputStream = new FileOutputStream(f.getAbsolutePath() + "/" + appName, true);
                this.flightPlaybackLogOutputStream = new FileOutputStream(flightPlaybackPath.getAbsolutePath() + "/" + flightPlayback, true);
                this.isWriteFirst = true;
                this.isWriteFlightPlaybackFirst = true;
                this.startTime = 0L;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X8FcLogManager.this.originalDistance = -1000.0f;
                    X8FcLogManager.this.onDeviceStateChange(0);
                    X8FcLogManager.this.closeRebuildFCLog = true;
                    return;
                case 1:
                    X8FcLogManager.this.originalDistance = -1000.0f;
                    X8FcLogManager.this.onDeviceStateChange(0);
                    X8FcLogManager.this.closeRebuildflightPlaybackLog = true;
                    return;
                case 2:
                    if (X8FcLogManager.this.autoFcSportState == null) {
                        X8FcLogManager.this.originalDistance = 0.0f;
                        X8FcLogManager.this.autoFcSportState = StateManager.getInstance().getX8Drone().getFcSportState();
                        X8FcLogManager.this.originalDistance = X8FcLogManager.this.autoFcSportState.getGroupSpeed();
                    } else {
                        X8FcLogManager.this.originalDistance += StateManager.getInstance().getX8Drone().getRealTimeSpeed();
                    }
                    if (X8FcLogManager.this.rockerModeData != null) {
                        X8FcLogManager.this.flightPlaybackLogWrite(X8FcLogManager.this.rockerModeData, false);
                        X8FcLogManager.this.rockerModeData = null;
                    }
                    if (X8FcLogManager.this.rockerStateData != null) {
                        X8FcLogManager.this.flightPlaybackLogWrite(X8FcLogManager.this.rockerStateData, false);
                        X8FcLogManager.this.rockerStateData = null;
                    }
                    if (X8FcLogManager.this.lowPowerData != null) {
                        X8FcLogManager.this.flightPlaybackLogWrite(X8FcLogManager.this.lowPowerData, false);
                        X8FcLogManager.this.lowPowerData = null;
                    }
                    X8FcLogManager.this.handler.sendEmptyMessageDelayed(2, 500L);
                    return;
                case 3:
                    if (X8FcLogManager.this.rockerModeData != null) {
                        X8FcLogManager.this.flightPlaybackLogWrite(X8FcLogManager.this.rockerModeData, false);
                        X8FcLogManager.this.rockerModeData = null;
                    }
                    if (X8FcLogManager.this.rockerLandDownData != null) {
                        X8FcLogManager.this.flightPlaybackLogWrite(X8FcLogManager.this.rockerLandDownData, false);
                        X8FcLogManager.this.rockerLandDownData = null;
                    }
                    X8FcLogManager.this.handler.sendEmptyMessageDelayed(2, 500L);
                    return;
                default:
            }
        }
    };

    public void closeFileOutputStream() {
        this.currentWriteFile = "";
        this.isPreLandDown = true;
        this.handler.removeMessages(0);
        this.handler.removeMessages(2);
        this.handler.removeMessages(3);
        currentWrite = "";
        this.isWriteFirst = false;
        this.isWriteFlightPlaybackFirst = false;
        if (this.fcOutputStream != null) {
            try {
                this.fcOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.fcOutputStream = null;
        }
        if (this.cmOutputStream != null) {
            try {
                this.cmOutputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.cmOutputStream = null;
        }
        if (this.appLogOutputStream != null) {
            try {
                this.appLogOutputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            this.appLogOutputStream = null;
        }
        if (this.flightPlaybackLogOutputStream != null) {
            try {
                this.flightPlaybackLogOutputStream.write(addFlightPlaybackDataDistance());
                this.flightPlaybackLogOutputStream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            this.flightPlaybackLogOutputStream = null;
            this.autoFcSportState = null;
        }
    }

    public void cmLogWrite(byte[] bytes, long l, float distance) {
        if (this.cmOutputStream != null && this.state == LogState.INSKY) {
            try {
                byte[] time = ByteHexHelper.getLongBytes(l);
                byte[] d = ByteUtil.float2byte(distance);
                byte[] bytesSave = new byte[bytes.length + time.length + d.length];
                System.arraycopy(bytes, 0, bytesSave, 0, bytes.length);
                System.arraycopy(time, 0, bytesSave, bytes.length, time.length);
                System.arraycopy(d, 0, bytesSave, bytes.length + time.length, d.length);
                this.cmOutputStream.write(logDesData(bytesSave));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void flightPlaybackLogWrite(byte[] bytes, boolean isFc) {
        if (bytes != null) {
            if (isFc) {
                this.handler.removeMessages(1);
                this.handler.sendEmptyMessageDelayed(1, 60000);
            }
            if (this.flightPlaybackLogOutputStream != null && this.state == LogState.INSKY) {
                try {
                    if (this.isWriteFlightPlaybackFirst) {
                        this.startTime = System.currentTimeMillis();
                        this.isWriteFlightPlaybackFirst = false;
                        if (this.isPreLandDown) {
                            this.handler.sendEmptyMessage(3);
                        } else {
                            this.handler.sendEmptyMessage(2);
                        }
                    }
                    byte[] bytes2 = addFlightPlaybackData(bytes);
                    if (this.flightPlaybackLogOutputStream != null && bytes2 != null) {
                        this.flightPlaybackLogOutputStream.write(bytes2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (this.closeRebuildflightPlaybackLog && this.state == LogState.INSKY) {
                onDeviceStateChange(1);
                this.closeRebuildflightPlaybackLog = false;
            }
        }
    }

    public void fcLogWrite(byte[] bytes) {
        this.handler.removeMessages(0);
        this.handler.sendEmptyMessageDelayed(0, 60000);
        if (this.fcOutputStream != null && this.state == LogState.INSKY) {
            try {
                this.fcOutputStream.write(logDesData(bytes));
                if (this.isWriteFirst) {
                    appAllLogWrite();
                    this.isWriteFirst = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (this.closeRebuildFCLog && this.state == LogState.INSKY) {
            onDeviceStateChange(1);
            this.closeRebuildFCLog = false;
        }
    }

    @NonNull
    private byte[] logDesData(byte[] bytes) {
        byte[] desCbcEncrypts = DesEncryptUtil.getInstans().desCbcEncrypt(bytes, DesEncryptUtil.getInstans().ENCRYPT_KEY_BYTES_TWO);
        byte[] bytesSave = new byte[desCbcEncrypts.length + 3];
        short msgLen = (short) bytesSave.length;
        int index = 1;
        bytesSave[0] = -3;
        byte[] short2byte = ByteUtil.shortToByte(msgLen);
        int index2 = index + 1;
        bytesSave[index] = short2byte[0];
        bytesSave[index2] = short2byte[1];
        System.arraycopy(desCbcEncrypts, 0, bytesSave, index2 + 1, desCbcEncrypts.length);
        return bytesSave;
    }

    public void appAllLogWrite() {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                String jsonString = DataJsonFactory.getAllDataJsonString();
                this.appLogOutputStream.write(logDesData(jsonString.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appValueFloatChange(String tag, float oldV, float newV) {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                String json = DataJsonFactory.onValueChange(tag, oldV, newV);
                this.appLogOutputStream.write(logDesData(json.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appValueBoleanChange(String tag, boolean oldV, boolean newV) {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                String json = DataJsonFactory.onValueBooleanChange(tag, oldV, newV);
                this.appLogOutputStream.write(logDesData(json.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appValueSensityChange(String tag, ValueSensity oldV, ValueSensity newV) {
        if (this.appLogOutputStream != null && this.state == LogState.INSKY) {
            try {
                String json = DataJsonFactory.appValueSensityChange(tag, oldV, newV);
                this.appLogOutputStream.write(logDesData(json.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] addFlightPlaybackData(byte[] bytes) {
        byte[] desCbcEncrypts = DesEncryptUtil.getInstans().desCbcEncrypt(bytes, DesEncryptUtil.getInstans().ENCRYPT_KEY_BYTES);
        byte[] bytesSave = new byte[desCbcEncrypts.length + 8];
        short msgLen = (short) bytesSave.length;
        int ptsb = (int) (System.currentTimeMillis() - this.startTime);
        int index = 1;
        bytesSave[0] = -3;
        int index2 = index + 1;
        bytesSave[index] = 0;
        byte[] short2byte = ByteUtil.shortToByte(msgLen);
        int index3 = index2 + 1;
        bytesSave[index2] = short2byte[0];
        int index4 = index3 + 1;
        bytesSave[index3] = short2byte[1];
        byte[] int2byte = ByteUtil.intToByte(ptsb);
        int index5 = index4 + 1;
        bytesSave[index4] = int2byte[0];
        int index6 = index5 + 1;
        bytesSave[index5] = int2byte[1];
        int index7 = index6 + 1;
        bytesSave[index6] = int2byte[2];
        bytesSave[index7] = int2byte[3];
        System.arraycopy(desCbcEncrypts, 0, bytesSave, index7 + 1, desCbcEncrypts.length);
        return bytesSave;
    }

    public byte[] addFlightPlaybackDataDistance() {
        byte[] bytesSave = new byte[12];
        short msgLen = (short) bytesSave.length;
        int ptsb = (int) (System.currentTimeMillis() - this.startTime);
        float distance = (this.originalDistance / 100.0f) / 2.0f;
        int index = 1;
        bytesSave[0] = -3;
        int index2 = index + 1;
        bytesSave[index] = 1;
        byte[] short2byte = ByteUtil.shortToByte(msgLen);
        int index3 = index2 + 1;
        bytesSave[index2] = short2byte[0];
        int index4 = index3 + 1;
        bytesSave[index3] = short2byte[1];
        byte[] int2byte = ByteUtil.intToByte(ptsb);
        int index5 = index4 + 1;
        bytesSave[index4] = int2byte[0];
        int index6 = index5 + 1;
        bytesSave[index5] = int2byte[1];
        int index7 = index6 + 1;
        bytesSave[index6] = int2byte[2];
        int index8 = index7 + 1;
        bytesSave[index7] = int2byte[3];
        byte[] int2byteDistance = ByteUtil.float2byte(distance);
        int index9 = index8 + 1;
        bytesSave[index8] = int2byteDistance[0];
        int index10 = index9 + 1;
        bytesSave[index9] = int2byteDistance[1];
        int index11 = index10 + 1;
        bytesSave[index10] = int2byteDistance[2];
        int i = index11 + 1;
        bytesSave[index11] = int2byteDistance[3];
        return bytesSave;
    }

    public void setRockerModeData(byte[] rockerModeData) {
        this.rockerModeData = rockerModeData;
    }

    public void setRockerStateData(byte[] rockerStateData) {
        this.rockerStateData = rockerStateData;
    }

    public void setPreLandDown(boolean preLandDown) {
        this.isPreLandDown = preLandDown;
    }

    public void setRockerLandDownData(byte[] rockerLandDownData) {
        this.rockerLandDownData = rockerLandDownData;
    }

    public String getCurrentWriteFile() {
        return this.currentWriteFile;
    }

    public void setLowPowerData(byte[] lowPowerData) {
        this.lowPowerData = lowPowerData;
    }

    public void setOriginalDistance(float originalDistance) {
        this.originalDistance = originalDistance;
    }

    public enum LogState {
        INSKY,
        ONGROUND
    }


}
