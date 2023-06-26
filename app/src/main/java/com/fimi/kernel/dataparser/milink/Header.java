package com.fimi.kernel.dataparser.milink;

import com.fimi.kernel.utils.CRCUtil;


public class Header {
    public static final int FLAG = 254;
    public static final int HEADERLEN = 20;
    public final int VERSION = 3;
    private final byte startFlag = -2;
    private final byte[] header = new byte[20];
    private int cfg;
    private int crcFrame;
    private int crcHeader;
    private byte destId;
    private byte encryptType;
    private int len;
    private int lenTypeCfg;
    private int payloadLen;
    private int reserver;
    private int seq;
    private byte srcId;
    private byte subDestId;
    private byte subSrcId;
    private int type;
    private byte version = 3;

    public int getCrcHeader() {
        return this.crcHeader;
    }

    public void setCrcHeader(int crcHeader) {
        this.crcHeader = crcHeader;
    }

    public int getLenTypeCfg() {
        return this.lenTypeCfg;
    }

    public void setLenTypeCfg(int lenTypeCfg) {
        this.lenTypeCfg = lenTypeCfg;
    }

    public byte getVersion() {
        return this.version;
    }

    public void setVersion(byte version) {
        this.version = version;
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

    public int getLen() {
        return this.len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCfg() {
        return this.cfg;
    }

    public void setCfg(int cfg) {
        this.cfg = cfg;
    }

    public int getReserver() {
        return this.reserver;
    }

    public void setReserver(int reserver) {
        this.reserver = reserver;
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

    public byte getSubSrcId() {
        return this.subSrcId;
    }

    public void setSubSrcId(byte subSrcId) {
        this.subSrcId = subSrcId;
    }

    public byte getDestId() {
        return this.destId;
    }

    public void setDestId(byte destId) {
        this.destId = destId;
    }

    public byte getSubDestId() {
        return this.subDestId;
    }

    public void setSubDestId(byte subDestId) {
        this.subDestId = subDestId;
    }

    public int getSeq() {
        return this.seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public byte[] onPaket() {
        int i = 1;
        this.header[0] = this.startFlag;
        int i2 = i + 1;
        this.header[i] = this.version;
        this.len = this.payloadLen + 20;
        this.lenTypeCfg = (short) ((this.len & 1023) | ((this.type & 255) << 10) | ((this.cfg & 255) << 13));
        int i3 = i2 + 1;
        this.header[i2] = (byte) (this.lenTypeCfg & 255);
        int i4 = i3 + 1;
        this.header[i3] = (byte) ((this.lenTypeCfg >> 8) & 255);
        int i5 = i4 + 1;
        this.header[i4] = this.srcId;
        int i6 = i5 + 1;
        this.header[i5] = this.subSrcId;
        int i7 = i6 + 1;
        this.header[i6] = this.destId;
        int i8 = i7 + 1;
        this.header[i7] = this.subDestId;
        int i9 = i8 + 1;
        this.header[i8] = this.encryptType;
        int i10 = i9 + 1;
        this.header[i9] = (byte) (this.reserver & 255);
        int i11 = i10 + 1;
        this.header[i10] = (byte) ((this.reserver >> 8) & 255);
        int i12 = i11 + 1;
        this.header[i11] = (byte) ((this.reserver >> 16) & 255);
        int i13 = i12 + 1;
        this.header[i12] = (byte) (this.seq & 255);
        int i14 = i13 + 1;
        this.header[i13] = (byte) ((this.seq >> 8) & 255);
        this.crcHeader = CRCUtil.crc16Calculate(this.header, 14);
        int i15 = i14 + 1;
        this.header[i14] = (byte) (this.crcHeader & 255);
        int i16 = i15 + 1;
        this.header[i15] = (byte) ((this.crcHeader >> 8) & 255);
        int i17 = i16 + 1;
        this.header[i16] = (byte) (this.crcFrame & 255);
        int i18 = i17 + 1;
        this.header[i17] = (byte) ((this.crcFrame >> 8) & 255);
        int i19 = i18 + 1;
        this.header[i18] = (byte) ((this.crcFrame >> 16) & 255);
        int i20 = i19 + 1;
        this.header[i19] = (byte) ((this.crcFrame >> 24) & 255);
        return this.header;
    }

    public void unPacket() {
        int i = 1;
        this.header[0] = this.startFlag;
        int i2 = i + 1;
        this.header[i] = this.version;
        int i3 = i2 + 1;
        this.header[i2] = (byte) (this.lenTypeCfg & 255);
        int i4 = i3 + 1;
        this.header[i3] = (byte) ((this.lenTypeCfg >> 8) & 255);
        int i5 = i4 + 1;
        this.header[i4] = this.srcId;
        int i6 = i5 + 1;
        this.header[i5] = this.subSrcId;
        int i7 = i6 + 1;
        this.header[i6] = this.destId;
        int i8 = i7 + 1;
        this.header[i7] = this.subDestId;
        int i9 = i8 + 1;
        this.header[i8] = this.encryptType;
        int i10 = i9 + 1;
        this.header[i9] = (byte) (this.reserver & 255);
        int i11 = i10 + 1;
        this.header[i10] = (byte) ((this.reserver >> 8) & 255);
        int i12 = i11 + 1;
        this.header[i11] = (byte) ((this.reserver >> 16) & 255);
        int i13 = i12 + 1;
        this.header[i12] = (byte) (this.seq & 255);
        int i14 = i13 + 1;
        this.header[i13] = (byte) ((this.seq >> 8) & 255);
        int i15 = i14 + 1;
        this.header[i14] = (byte) (this.crcHeader & 255);
        int i16 = i15 + 1;
        this.header[i15] = (byte) ((this.crcHeader >> 8) & 255);
        int i17 = i16 + 1;
        this.header[i16] = (byte) (this.crcFrame & 255);
        int i18 = i17 + 1;
        this.header[i17] = (byte) ((this.crcFrame >> 8) & 255);
        int i19 = i18 + 1;
        this.header[i18] = (byte) ((this.crcFrame >> 16) & 255);
        int i20 = i19 + 1;
        this.header[i19] = (byte) ((this.crcFrame >> 24) & 255);
        this.len = this.lenTypeCfg & 511;
        this.type = (this.lenTypeCfg >> 9) & 255;
        this.cfg = (this.lenTypeCfg >> 12) & 255;
        setPayloadLen(this.len - 20);
    }

    public boolean checkHeaderCRC() {
        int crc16 = CRCChecksum.crc16_calculate(this.header, 14);
        return crc16 == this.crcHeader;
    }

    public boolean checkFrameCRC(int crc32) {
        return crc32 == this.crcFrame;
    }

    public boolean checkCRC(int crc32) {
        return checkHeaderCRC() && checkFrameCRC(crc32);
    }

    public int getDataLen() {
        return (this.lenTypeCfg & 511) - 20;
    }
}
