package com.file.zip;

import androidx.annotation.NonNull;

import java.util.zip.ZipException;

public class Zip64RequiredException extends ZipException {
    static final String ARCHIVE_TOO_BIG_MESSAGE = "archive's size exceeds the limit of 4GByte.";
    static final String TOO_MANY_ENTRIES_MESSAGE = "archive contains more than 65535 entries.";
    private static final long serialVersionUID = 20110809;

    public Zip64RequiredException(String reason) {
        super(reason);
    }

    @NonNull
    public static String getEntryTooBigMessage(@NonNull ZipEntry ze) {
        return String.valueOf(ze.getName()) + "'s size exceeds the limit of 4GByte.";
    }
}
