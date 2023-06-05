package com.file.zip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simple8BitZipEncoding implements ZipEncoding {
    private final char[] highChars;
    private final List<Simple8BitChar> reverseMapping;

    public Simple8BitZipEncoding(@NonNull char[] highChars) {
        this.highChars = highChars.clone();
        List<Simple8BitChar> temp = new ArrayList<>(this.highChars.length);
        byte code = Byte.MAX_VALUE;
        for (int i = 0; i < this.highChars.length; i++) {
            code = (byte) (code + 1);
            temp.add(new Simple8BitChar(code, this.highChars[i]));
        }
        Collections.sort(temp);
        this.reverseMapping = Collections.unmodifiableList(temp);
    }

    public char decodeByte(byte b) {
        return b >= 0 ? (char) b : this.highChars[b + 128];
    }

    public boolean canEncodeChar(char c) {
        if (c < 0 || c >= 128) {
            Simple8BitChar r = encodeHighChar(c);
            return r != null;
        }
        return true;
    }

    public boolean pushEncodedChar(ByteBuffer bb, char c) {
        if (c >= 0 && c < 128) {
            bb.put((byte) c);
            return true;
        }
        Simple8BitChar r = encodeHighChar(c);
        if (r == null) {
            return false;
        }
        bb.put(r.code);
        return true;
    }

    @Nullable
    private Simple8BitChar encodeHighChar(char c) {
        int i0 = 0;
        int i1 = this.reverseMapping.size();
        while (i1 > i0) {
            int i = i0 + ((i1 - i0) / 2);
            Simple8BitChar m = this.reverseMapping.get(i);
            if (m.unicode != c) {
                if (m.unicode < c) {
                    i0 = i + 1;
                } else {
                    i1 = i;
                }
            } else {
                return m;
            }
        }
        if (i0 >= this.reverseMapping.size()) {
            return null;
        }
        Simple8BitChar r = this.reverseMapping.get(i0);
        if (r.unicode != c) {
            return null;
        }
        return r;
    }

    @Override
    public boolean canEncode(@NonNull String name) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!canEncodeChar(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ByteBuffer encode(@NonNull String name) {
        ByteBuffer out = ByteBuffer.allocate(name.length() + 6 + ((name.length() + 1) / 2));
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (out.remaining() < 6) {
                out = ZipEncodingHelper.growBuffer(out, out.position() + 6);
            }
            if (!pushEncodedChar(out, c)) {
                ZipEncodingHelper.appendSurrogate(out, c);
            }
        }
        out.limit(out.position());
        out.rewind();
        return out;
    }

    @Override
    public String decode(@NonNull byte[] data) throws IOException {
        char[] ret = new char[data.length];
        for (int i = 0; i < data.length; i++) {
            ret[i] = decodeByte(data[i]);
        }
        return new String(ret);
    }

    public static final class Simple8BitChar implements Comparable<Simple8BitChar> {
        public final byte code;
        public final char unicode;

        Simple8BitChar(byte code, char unicode) {
            this.code = code;
            this.unicode = unicode;
        }

        @Contract(pure = true)
        @Override
        public int compareTo(@NonNull Simple8BitChar a) {
            return this.unicode - a.unicode;
        }

        @NonNull
        public String toString() {
            return "0x" + Integer.toHexString(65535 & this.unicode) + "->0x" + Integer.toHexString(this.code & 255);
        }

        public boolean equals(Object o) {
            if (o instanceof Simple8BitChar other) {
                return this.unicode == other.unicode && this.code == other.code;
            }
            return false;
        }

        public int hashCode() {
            return this.unicode;
        }
    }
}
