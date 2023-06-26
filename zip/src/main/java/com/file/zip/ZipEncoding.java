package com.file.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ZipEncoding {
    boolean canEncode(String str);

    String decode(byte[] bArr) throws IOException;

    ByteBuffer encode(String str) throws IOException;
}
