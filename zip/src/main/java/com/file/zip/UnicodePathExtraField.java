package com.file.zip;

public class UnicodePathExtraField extends AbstractUnicodeExtraField {
    public static final ZipShort UPATH_ID = new ZipShort(28789);

    public UnicodePathExtraField() {
    }

    public UnicodePathExtraField(String text, byte[] bytes, int off, int len) {
        super(text, bytes, off, len);
    }

    public UnicodePathExtraField(String name, byte[] bytes) {
        super(name, bytes);
    }

    @Override
    public ZipShort getHeaderId() {
        return UPATH_ID;
    }
}
