package com.file.zip;

public class UnrecognizedExtraField implements CentralDirectoryParsingZipExtraField {
    private byte[] centralData;
    private ZipShort headerId;
    private byte[] localData;

    @Override
    public ZipShort getHeaderId() {
        return this.headerId;
    }

    public void setHeaderId(ZipShort headerId) {
        this.headerId = headerId;
    }

    @Override
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(this.localData.length);
    }

    @Override
    public byte[] getLocalFileDataData() {
        return ZipUtil.copy(this.localData);
    }

    public void setLocalFileDataData(byte[] data) {
        this.localData = ZipUtil.copy(data);
    }

    @Override
    public ZipShort getCentralDirectoryLength() {
        return this.centralData != null ? new ZipShort(this.centralData.length) : getLocalFileDataLength();
    }

    @Override
    public byte[] getCentralDirectoryData() {
        return this.centralData != null ? ZipUtil.copy(this.centralData) : getLocalFileDataData();
    }

    public void setCentralDirectoryData(byte[] data) {
        this.centralData = ZipUtil.copy(data);
    }

    @Override
    public void parseFromLocalFileData(byte[] data, int offset, int length) {
        byte[] tmp = new byte[length];
        System.arraycopy(data, offset, tmp, 0, length);
        setLocalFileDataData(tmp);
    }

    @Override
    public void parseFromCentralDirectoryData(byte[] data, int offset, int length) {
        byte[] tmp = new byte[length];
        System.arraycopy(data, offset, tmp, 0, length);
        setCentralDirectoryData(tmp);
        if (this.localData == null) {
            setLocalFileDataData(tmp);
        }
    }
}
