package com.fimi.x8sdk.modulestate;

import com.fimi.x8sdk.dataparser.AutoFcErrCode;
import com.fimi.x8sdk.entity.X8ErrorCodeInfo;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ErrorCodeState {
    public static boolean appTest = false;
    public static long a = 0;
    private int atc;
    private int lastAtc;
    private int lastMtc;
    private int lastNfzs;
    private int lastRcs;
    private long lastSystemStatusCodA;
    private long lastSystemStatusCodB;
    private long lastSystemStatusCodC;
    private int mtc;
    private int nfzs;
    private int rcs;
    private long systemStatusCodA;
    private long systemStatusCodB;
    private long systemStatusCodC;

    public static void setErrorCode(long n) {
        a = n;
    }

    private void setErrorCode(long systemStatusCodA, long systemStatusCodB, long systemStatusCodC) {
        if (appTest) {
            systemStatusCodB = a;
        }
        this.systemStatusCodA = systemStatusCodA;
        this.systemStatusCodB = systemStatusCodB;
        this.systemStatusCodC = systemStatusCodC;
    }

    public void getErrorCode() {
        this.lastSystemStatusCodA = this.systemStatusCodA;
        this.lastSystemStatusCodB = this.systemStatusCodB;
        this.lastSystemStatusCodC = this.systemStatusCodC;
    }

    public void setErrorCode(AutoFcErrCode fcErrCode) {
        setErrorCode(fcErrCode.getSystemStatusCodA(), fcErrCode.getSystemStatusCodB(), fcErrCode.getSystemStatusCodC());
    }

    public void setErroCodeAtcAndMtc(int mtc, int atc) {
        this.mtc = mtc & 255;
        this.atc = atc & 255;
    }

    public void setErrorCodeRcs(int rcs) {
        this.rcs = rcs;
    }

    public void setNfzErrorCode(int nfzs) {
        this.nfzs = nfzs;
    }

    public List<X8ErrorCodeInfo> getErrooInfo() {
        List<X8ErrorCodeInfo> list = new ArrayList<>();
        this.lastSystemStatusCodA = this.systemStatusCodA;
        this.lastSystemStatusCodB = this.systemStatusCodB;
        this.lastSystemStatusCodC = this.systemStatusCodC;
        this.lastMtc = this.mtc;
        this.lastAtc = this.atc;
        this.lastRcs = this.rcs;
        this.lastNfzs = this.nfzs;
        if (0 != this.lastSystemStatusCodA) {
            for (int i = 0; i < 32; i++) {
                long code = this.lastSystemStatusCodA >> i;
                if ((code & 1) == 1) {
                    X8ErrorCodeInfo mX8ErrorCodeInfo = new X8ErrorCodeInfo(0, i);
                    list.add(mX8ErrorCodeInfo);
                }
            }
        }
        if (0 != this.lastSystemStatusCodB) {
            for (int i2 = 0; i2 < 32; i2++) {
                long code2 = this.lastSystemStatusCodB >> i2;
                if ((code2 & 1) == 1) {
                    X8ErrorCodeInfo mX8ErrorCodeInfo2 = new X8ErrorCodeInfo(1, i2);
                    list.add(mX8ErrorCodeInfo2);
                }
            }
        }
        if (0 != this.lastSystemStatusCodC) {
            for (int i3 = 0; i3 < 32; i3++) {
                long code3 = this.lastSystemStatusCodC >> i3;
                if ((code3 & 1) == 1) {
                    X8ErrorCodeInfo mX8ErrorCodeInfo3 = new X8ErrorCodeInfo(2, i3);
                    list.add(mX8ErrorCodeInfo3);
                }
            }
        }
        if (this.lastMtc != 0) {
            X8ErrorCodeInfo mX8ErrorCodeInfo4 = new X8ErrorCodeInfo(3, this.lastMtc, false);
            list.add(mX8ErrorCodeInfo4);
        }
        if (this.lastAtc != 0) {
            X8ErrorCodeInfo mX8ErrorCodeInfo5 = new X8ErrorCodeInfo(4, this.lastAtc, false);
            list.add(mX8ErrorCodeInfo5);
        }
        if (this.lastRcs != 0) {
            for (int i4 = 0; i4 < 8; i4++) {
                long code4 = this.lastRcs >> i4;
                if ((code4 & 1) == 1) {
                    X8ErrorCodeInfo mX8ErrorCodeInfo6 = new X8ErrorCodeInfo(5, i4);
                    list.add(mX8ErrorCodeInfo6);
                }
            }
        }
        if (this.lastNfzs != 0) {
            X8ErrorCodeInfo mX8ErrorCodeInfo7 = new X8ErrorCodeInfo(6, this.lastNfzs, false);
            list.add(mX8ErrorCodeInfo7);
        }
        return list;
    }

    public boolean unMountCloud() {
        int n = (int) Math.pow(2.0d, 15.0d);
        long code = this.lastSystemStatusCodB & n;
        return (code >> 7) == 1;
    }

    public boolean isLowPower() {
        int n = (int) Math.pow(2.0d, 7.0d);
        long code = this.lastSystemStatusCodB & n;
        if ((code >> 7) == 1) {
            return false;
        }
        int n2 = (int) Math.pow(2.0d, 11.0d);
        long code2 = this.lastSystemStatusCodB & n2;
        if ((code2 >> 11) != 1) {
            return true;
        }
        return false;
    }

    public boolean isGpsError() {
        int n = (int) Math.pow(2.0d, 0.0d);
        long code = this.lastSystemStatusCodA & n;
        if (code == 1) {
            return true;
        }
        long code2 = this.lastSystemStatusCodB & n;
        if (code2 != 1) {
            return false;
        }
        return true;
    }

    public boolean isMagneticError() {
        int n = (int) Math.pow(2.0d, 20.0d);
        long code = this.lastSystemStatusCodA & n;
        if ((code >> 20) != 1) {
            return false;
        }
        return true;
    }

    public boolean isCampError() {
        int[] indexA = {2, 3};
        for (int i = 0; i < indexA.length; i++) {
            int n = (int) Math.pow(2.0d, indexA[i]);
            long code = this.lastSystemStatusCodA & n;
            if ((code >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = {1, 3, 28};
        for (int i2 = 0; i2 < indexB.length; i2++) {
            int n2 = (int) Math.pow(2.0d, indexB[i2]);
            long code2 = this.lastSystemStatusCodB & n2;
            if ((code2 >> indexB[i2]) == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isImuError() {
        int[] indexA = {4, 5};
        for (int i = 0; i < indexA.length; i++) {
            int n = (int) Math.pow(2.0d, indexA[i]);
            long code = this.lastSystemStatusCodA & n;
            if ((code >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = {2, 4};
        for (int i2 = 0; i2 < indexB.length; i2++) {
            int n2 = (int) Math.pow(2.0d, indexB[i2]);
            long code2 = this.lastSystemStatusCodB & n2;
            if ((code2 >> indexB[i2]) == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isBatteryError() {
        int[] indexA = {7, 8, 18, 19, 25, 26, 27};
        for (int i = 0; i < indexA.length; i++) {
            int n = (int) Math.pow(2.0d, indexA[i]);
            long code = this.lastSystemStatusCodA & n;
            if ((code >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = {6, 7, 8, 9, 10, 11, 23, 27};
        for (int i2 = 0; i2 < indexB.length; i2++) {
            int n2 = (int) Math.pow(2.0d, indexB[i2]);
            long code2 = this.lastSystemStatusCodB & n2;
            if ((code2 >> indexB[i2]) == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isGimbalError() {
        int[] indexA = {14, 16};
        for (int i = 0; i < indexA.length; i++) {
            int n = (int) Math.pow(2.0d, indexA[i]);
            long code = this.lastSystemStatusCodA & n;
            if ((code >> indexA[i]) == 1) {
                return true;
            }
        }
        int[] indexB = {17, 18, 19, 20, 21, 22};
        for (int i2 = 0; i2 < indexB.length; i2++) {
            int n2 = (int) Math.pow(2.0d, indexB[i2]);
            long code2 = this.lastSystemStatusCodB & n2;
            if ((code2 >> indexB[i2]) == 1) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        this.systemStatusCodA = 0L;
        this.systemStatusCodB = 0L;
        this.systemStatusCodC = 0L;
        this.mtc = 0;
        this.atc = 0;
        this.rcs = 0;
        this.nfzs = 0;
    }
}
