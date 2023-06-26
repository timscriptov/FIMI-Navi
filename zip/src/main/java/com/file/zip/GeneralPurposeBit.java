package com.file.zip;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public final class GeneralPurposeBit {
    public static final int UFT8_NAMES_FLAG = 2048;
    private static final int DATA_DESCRIPTOR_FLAG = 8;
    private static final int ENCRYPTION_FLAG = 1;
    private static final int STRONG_ENCRYPTION_FLAG = 64;
    private boolean languageEncodingFlag = false;
    private boolean dataDescriptorFlag = false;
    private boolean encryptionFlag = false;
    private boolean strongEncryptionFlag = false;

    @NonNull
    public static GeneralPurposeBit parse(byte[] data, int offset) {
        int generalPurposeFlag = ZipShort.getValue(data, offset);
        GeneralPurposeBit b = new GeneralPurposeBit();
        b.useDataDescriptor((generalPurposeFlag & 8) != 0);
        b.useUTF8ForNames((generalPurposeFlag & 2048) != 0);
        b.useStrongEncryption((generalPurposeFlag & 64) != 0);
        b.useEncryption((generalPurposeFlag & 1) != 0);
        return b;
    }

    public boolean usesUTF8ForNames() {
        return this.languageEncodingFlag;
    }

    public void useUTF8ForNames(boolean b) {
        this.languageEncodingFlag = b;
    }

    public boolean usesDataDescriptor() {
        return this.dataDescriptorFlag;
    }

    public void useDataDescriptor(boolean b) {
        this.dataDescriptorFlag = b;
    }

    public boolean usesEncryption() {
        return this.encryptionFlag;
    }

    public void useEncryption(boolean b) {
        this.encryptionFlag = b;
    }

    public boolean usesStrongEncryption() {
        return this.encryptionFlag && this.strongEncryptionFlag;
    }

    public void useStrongEncryption(boolean b) {
        this.strongEncryptionFlag = b;
        if (b) {
            useEncryption(true);
        }
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public byte[] encode() {
        return ZipShort.getBytes((this.encryptionFlag ? 1 : 0) | (this.languageEncodingFlag ? 2048 : 0) | (this.dataDescriptorFlag ? 8 : 0) | (this.strongEncryptionFlag ? 64 : 0));
    }

    public int hashCode() {
        return ((((this.languageEncodingFlag ? 1 : 0) + (((this.strongEncryptionFlag ? 1 : 0) + ((this.encryptionFlag ? 1 : 0) * 17)) * 13)) * 7) + (this.dataDescriptorFlag ? 1 : 0)) * 3;
    }

    public boolean equals(Object o) {
        if (o instanceof GeneralPurposeBit g) {
            return g.encryptionFlag == this.encryptionFlag && g.strongEncryptionFlag == this.strongEncryptionFlag && g.languageEncodingFlag == this.languageEncodingFlag && g.dataDescriptorFlag == this.dataDescriptorFlag;
        }
        return false;
    }
}
