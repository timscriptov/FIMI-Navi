package com.file.zip;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.CRC32;

public abstract class ZipUtil {
    private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(8448);

    @NonNull
    @Contract("_ -> new")
    public static ZipLong toDosTime(@NonNull Date time) {
        return new ZipLong(toDosTime(time.getTime()));
    }

    public static byte[] toDosTime(long t) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t);
        int year = c.get(1);
        if (year < 1980) {
            return copy(DOS_TIME_MIN);
        }
        int month = c.get(2) + 1;
        long value = ((year - 1980) << 25) | (month << 21) | (c.get(5) << 16) | (c.get(11) << 11) | (c.get(12) << 5) | (c.get(13) >> 1);
        return ZipLong.getBytes(value);
    }

    public static long adjustToLong(int i) {
        return i < 0 ? 4294967296L + i : i;
    }

    @NonNull
    @Contract("_ -> new")
    public static Date fromDosTime(@NonNull ZipLong zipDosTime) {
        long dosTime = zipDosTime.getValue();
        return new Date(dosToJavaTime(dosTime));
    }

    public static long dosToJavaTime(long dosTime) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, ((int) ((dosTime >> 25) & 127)) + 1980);
        cal.set(2, ((int) ((dosTime >> 21) & 15)) - 1);
        cal.set(5, ((int) (dosTime >> 16)) & 31);
        cal.set(11, ((int) (dosTime >> 11)) & 31);
        cal.set(12, ((int) (dosTime >> 5)) & 63);
        cal.set(13, ((int) (dosTime << 1)) & 62);
        return cal.getTime().getTime();
    }

    public static void setNameAndCommentFromExtraFields(@NonNull ZipEntry ze, byte[] originalNameBytes, byte[] commentBytes) {
        UnicodePathExtraField name = (UnicodePathExtraField) ze.getExtraField(UnicodePathExtraField.UPATH_ID);
        String originalName = ze.getName();
        String newName = getUnicodeStringIfOriginalMatches(name, originalNameBytes);
        if (newName != null && !originalName.equals(newName)) {
            ze.setName(newName);
        }
        if (commentBytes != null && commentBytes.length > 0) {
            UnicodeCommentExtraField cmt = (UnicodeCommentExtraField) ze.getExtraField(UnicodeCommentExtraField.UCOM_ID);
            String newComment = getUnicodeStringIfOriginalMatches(cmt, commentBytes);
            if (newComment != null) {
                ze.setComment(newComment);
            }
        }
    }

    private static String getUnicodeStringIfOriginalMatches(AbstractUnicodeExtraField f, byte[] orig) {
        if (f != null) {
            CRC32 crc32 = new CRC32();
            crc32.update(orig);
            long origCRC32 = crc32.getValue();
            if (origCRC32 == f.getNameCRC32()) {
                try {
                    return ZipEncodingHelper.UTF8_ZIP_ENCODING.decode(f.getUnicodeName());
                } catch (IOException e) {
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public static byte[] copy(byte[] from) {
        if (from != null) {
            byte[] to = new byte[from.length];
            System.arraycopy(from, 0, to, 0, to.length);
            return to;
        }
        return null;
    }

    public static boolean canHandleEntryData(ZipEntry entry) {
        return supportsEncryptionOf(entry) && supportsMethodOf(entry);
    }

    private static boolean supportsEncryptionOf(@NonNull ZipEntry entry) {
        return !entry.getGeneralPurposeBit().usesEncryption();
    }

    private static boolean supportsMethodOf(@NonNull ZipEntry entry) {
        return entry.getMethod() == 0 || entry.getMethod() == 8;
    }

    public static void checkRequestedFeatures(ZipEntry ze) throws UnsupportedZipFeatureException {
        if (!supportsEncryptionOf(ze)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.ENCRYPTION, ze);
        }
        if (!supportsMethodOf(ze)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.METHOD, ze);
        }
    }
}
