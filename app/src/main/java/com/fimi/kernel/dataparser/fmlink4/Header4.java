package com.fimi.kernel.dataparser.fmlink4;

import android.util.Log;

import com.fimi.kernel.dataparser.milink.CRCChecksum;
import com.fimi.kernel.utils.CRCUtil;
import com.fimi.x8sdk.command.FcCollection;
import com.fimi.x8sdk.command.X8GimbalCollection;


public class Header4 {
    public static final int HEADER_LEN = 16;
    public static byte startFlag = -2;
    byte[] headerArr = new byte[16];
    private int crcFrame;
    private int crcHeader;
    private byte destId;
    private byte encryptType;
    private int len;
    private int payloadLen;
    private byte reserve1;
    private byte reserve2;
    private byte reserve3;
    private short seq;
    private byte srcId;
    private byte type;
    private byte typeAndR1AndEncrypt;
    private short verAndLen;
    private byte ver = 4;

    public int getLen() {
        return this.len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getReserve1() {
        return this.reserve1;
    }

    public void setReserve1(byte reserve1) {
        this.reserve1 = reserve1;
    }

    public byte getEncryptType() {
        return this.encryptType;
    }

    public void setEncryptType(byte encryptType) {
        this.encryptType = encryptType;
    }

    public byte getSrcId() {
        return this.srcId;
    }

    public void setSrcId(byte srcId) {
        this.srcId = srcId;
    }

    public byte getDestId() {
        return this.destId;
    }

    public void setDestId(byte destId) {
        this.destId = destId;
    }

    public byte getReserve2() {
        return this.reserve2;
    }

    public void setReserve2(byte reserve2) {
        this.reserve2 = reserve2;
    }

    public byte getReserve3() {
        return this.reserve3;
    }

    public void setReserve3(byte reserve3) {
        this.reserve3 = reserve3;
    }

    public short getSeq() {
        return this.seq;
    }

    public void setSeq(short seq) {
        this.seq = seq;
    }

    public int getCrcHeader() {
        return this.crcHeader;
    }

    public void setCrcHeader(int crcHeader) {
        this.crcHeader = crcHeader;
    }

    public int getCrcFrame() {
        return this.crcFrame;
    }

    public void setCrcFrame(int crcFrame) {
        this.crcFrame = crcFrame;
    }

    public int getPayloadLen() {
        return this.payloadLen;
    }

    public void setPayloadLen(int payloadLen) {
        this.payloadLen = payloadLen;
    }

    public byte getVer() {
        return this.ver;
    }

    public void setVer(byte ver) {
        this.ver = ver;
    }

    public byte[] onPacket() {
        int i = 1;
        this.headerArr[0] = startFlag;
        this.verAndLen = (short) ((this.ver & X8GimbalCollection.MSG_ID_GET_GIMBAL_GAIN) | ((this.len & 511) << 6));
        int i2 = i + 1;
        this.headerArr[i] = (byte) (this.verAndLen & 255);
        int i3 = i2 + 1;
        this.headerArr[i2] = (byte) ((this.verAndLen >> 8) & 255);
        this.typeAndR1AndEncrypt = (byte) ((this.type & 3) | (this.reserve1 & FcCollection.MSG_SET_FC_RETURE_HOME_RESUME) | (this.encryptType & 224));
        int i4 = i3 + 1;
        this.headerArr[i3] = this.typeAndR1AndEncrypt;
        int i5 = i4 + 1;
        this.headerArr[i4] = this.srcId;
        int i6 = i5 + 1;
        this.headerArr[i5] = this.destId;
        int i7 = i6 + 1;
        this.headerArr[i6] = this.reserve2;
        int i8 = i7 + 1;
        this.headerArr[i7] = this.reserve3;
        int i9 = i8 + 1;
        this.headerArr[i8] = (byte) (this.seq & 255);
        int i10 = i9 + 1;
        this.headerArr[i9] = (byte) ((this.seq >> 8) & 255);
        this.crcHeader = CRCUtil.crc16Calculate(this.headerArr, 10);
        int i11 = i10 + 1;
        this.headerArr[i10] = (byte) (this.crcHeader & 255);
        int i12 = i11 + 1;
        this.headerArr[i11] = (byte) ((this.crcHeader >> 8) & 255);
        int i13 = i12 + 1;
        this.headerArr[i12] = (byte) (this.crcFrame & 255);
        int i14 = i13 + 1;
        this.headerArr[i13] = (byte) ((this.crcFrame >> 8) & 255);
        int i15 = i14 + 1;
        this.headerArr[i14] = (byte) ((this.crcFrame >> 16) & 255);
        int i16 = i15 + 1;
        this.headerArr[i15] = (byte) ((this.crcFrame >> 24) & 255);
        return this.headerArr;
    }

    public boolean checkHeaderCRC() {
        int crc16 = CRCChecksum.crc16_calculate(this.headerArr, 14);
        return crc16 == this.crcHeader;
    }

    public boolean checkFrameCRC(int crc32) {
        return crc32 == this.crcFrame;
    }

    public boolean checkCRC(int crc32) {
        return checkHeaderCRC() && checkFrameCRC(crc32);
    }

    public int getDataLen() {
        Log.d("mowweiru", "len:" + this.len);
        return this.len - 16;
    }
}
