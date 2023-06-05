package com.file.zip;

import androidx.annotation.NonNull;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

public class ZipFile {
    static final int BYTE_SHIFT = 8;
    static final int NIBLET_MASK = 15;
    private static final int CFD_LOCATOR_OFFSET = 16;
    private static final int CFH_LEN = 42;
    private static final long CFH_SIG = ZipLong.getValue(ZipOutputStream.CFH_SIG);
    private static final int HASH_SIZE = 509;
    private static final long LFH_OFFSET_FOR_FILENAME_LENGTH = 26;
    private static final int MAX_EOCD_SIZE = 65557;
    private static final int MIN_EOCD_SIZE = 22;
    private static final int POS_0 = 0;
    private static final int POS_1 = 1;
    private static final int POS_2 = 2;
    private static final int POS_3 = 3;
    private static final int ZIP64_EOCDL_LENGTH = 20;
    private static final int ZIP64_EOCDL_LOCATOR_OFFSET = 8;
    private static final int ZIP64_EOCD_CFD_LOCATOR_OFFSET = 48;
    private final byte[] CFH_BUF;
    private final byte[] DWORD_BUF;
    private final Comparator<ZipEntry> OFFSET_COMPARATOR;
    private final byte[] SHORT_BUF;
    private final byte[] WORD_BUF;
    private final RandomAccessFile archive;
    private final String archiveName;
    private final String encoding;
    private final Map<ZipEntry, OffsetEntry> entries;
    private final Map<String, ZipEntry> nameMap;
    private final boolean useUnicodeExtraFields;
    private final ZipEncoding zipEncoding;
    private boolean closed;

    public ZipFile(File f) throws IOException {
        this(f, null);
    }

    public ZipFile(String name) throws IOException {
        this(new File(name), null);
    }

    public ZipFile(String name, String encoding) throws IOException {
        this(new File(name), encoding, true);
    }

    public ZipFile(File f, String encoding) throws IOException {
        this(f, encoding, true);
    }

    public ZipFile(@NonNull File f, String encoding, boolean useUnicodeExtraFields) throws IOException {
        this.entries = new LinkedHashMap<>(HASH_SIZE);
        this.nameMap = new HashMap<>(HASH_SIZE);
        this.DWORD_BUF = new byte[8];
        this.WORD_BUF = new byte[4];
        this.CFH_BUF = new byte[42];
        this.SHORT_BUF = new byte[2];
        this.OFFSET_COMPARATOR = (e1, e2) -> {
            if (e1 != e2) {
                OffsetEntry off1 = ZipFile.this.entries.get(e1);
                OffsetEntry off2 = ZipFile.this.entries.get(e2);
                if (off1 == null) {
                    return 1;
                }
                if (off2 != null) {
                    long val = off1.headerOffset - off2.headerOffset;
                    if (val == 0) {
                        return 0;
                    }
                    return val < 0 ? -1 : 1;
                }
                return -1;
            }
            return 0;
        };
        this.archiveName = f.getAbsolutePath();
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.useUnicodeExtraFields = useUnicodeExtraFields;
        this.archive = new RandomAccessFile(f, "r");
        try {
            Map<ZipEntry, NameAndComment> entriesWithoutUTF8Flag = populateFromCentralDirectory();
            resolveLocalFileHeaderData(entriesWithoutUTF8Flag);
            if (1 == 0) {
                try {
                    this.closed = true;
                    this.archive.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (0 == 0) {
                try {
                    this.closed = true;
                    this.archive.close();
                } catch (IOException e2) {
                }
            }
            throw th;
        }
    }

    public static void closeQuietly(ZipFile zipfile) {
        if (zipfile != null) {
            try {
                zipfile.close();
            } catch (IOException e) {
            }
        }
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void close() throws IOException {
        this.closed = true;
        this.archive.close();
    }

    public Enumeration<ZipEntry> getEntries() {
        return Collections.enumeration(this.entries.keySet());
    }

    public Enumeration<ZipEntry> getEntriesInPhysicalOrder() {
        ZipEntry[] allEntries = this.entries.keySet().toArray(new ZipEntry[0]);
        Arrays.sort(allEntries, this.OFFSET_COMPARATOR);
        return Collections.enumeration(Arrays.asList(allEntries));
    }

    public ZipEntry getEntry(String name) {
        return this.nameMap.get(name);
    }

    public boolean canReadEntryData(ZipEntry ze) {
        return ZipUtil.canHandleEntryData(ze);
    }

    public InputStream getInputStream(ZipEntry ze) throws IOException {
        OffsetEntry offsetEntry = this.entries.get(ze);
        if (offsetEntry == null) {
            return null;
        }
        ZipUtil.checkRequestedFeatures(ze);
        long start = offsetEntry.dataOffset;
        BoundedInputStream bis = new BoundedInputStream(start, ze.getCompressedSize());
        switch (ze.getMethod()) {
            case 0:
                return bis;
            case 8:
                bis.addDummy();
                final Inflater inflater = new Inflater(true);
                return new InflaterInputStream(bis, inflater) {
                    @Override
                    public void close() throws IOException {
                        super.close();
                        inflater.end();
                    }
                };
            default:
                throw new ZipException("Found unsupported compression method " + ze.getMethod());
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (!this.closed) {
                System.err.println("Cleaning up unclosed ZipFile for archive " + this.archiveName);
                close();
            }
        } finally {
            super.finalize();
        }
    }

    @NonNull
    private Map<ZipEntry, NameAndComment> populateFromCentralDirectory() throws IOException {
        HashMap<ZipEntry, NameAndComment> noUTF8Flag = new HashMap<>();
        positionAtCentralDirectory();
        this.archive.readFully(this.WORD_BUF);
        long sig = ZipLong.getValue(this.WORD_BUF);
        if (sig != CFH_SIG && startsWithLocalFileHeader()) {
            throw new IOException("central directory is empty, can't expand corrupt archive.");
        }
        while (sig == CFH_SIG) {
            readCentralDirectoryEntry(noUTF8Flag);
            this.archive.readFully(this.WORD_BUF);
            sig = ZipLong.getValue(this.WORD_BUF);
        }
        return noUTF8Flag;
    }

    private void readCentralDirectoryEntry(Map<ZipEntry, NameAndComment> noUTF8Flag) throws IOException {
        this.archive.readFully(this.CFH_BUF);
        ZipEntry ze = new ZipEntry();
        int versionMadeBy = ZipShort.getValue(this.CFH_BUF, 0);
        ze.setPlatform((versionMadeBy >> 8) & 15);
        int off = 2 + 2;
        GeneralPurposeBit gpFlag = GeneralPurposeBit.parse(this.CFH_BUF, off);
        boolean hasUTF8Flag = gpFlag.usesUTF8ForNames();
        ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
        ze.setGeneralPurposeBit(gpFlag);
        int off2 = off + 2;
        ze.setMethod(ZipShort.getValue(this.CFH_BUF, off2));
        int off3 = off2 + 2;
        long time = ZipUtil.dosToJavaTime(ZipLong.getValue(this.CFH_BUF, off3));
        ze.setTime(time);
        int off4 = off3 + 4;
        ze.setCrc(ZipLong.getValue(this.CFH_BUF, off4));
        int off5 = off4 + 4;
        ze.setCompressedSize(ZipLong.getValue(this.CFH_BUF, off5));
        int off6 = off5 + 4;
        ze.setSize(ZipLong.getValue(this.CFH_BUF, off6));
        int off7 = off6 + 4;
        int fileNameLen = ZipShort.getValue(this.CFH_BUF, off7);
        int off8 = off7 + 2;
        int extraLen = ZipShort.getValue(this.CFH_BUF, off8);
        int off9 = off8 + 2;
        int commentLen = ZipShort.getValue(this.CFH_BUF, off9);
        int off10 = off9 + 2;
        int diskStart = ZipShort.getValue(this.CFH_BUF, off10);
        int off11 = off10 + 2;
        ze.setInternalAttributes(ZipShort.getValue(this.CFH_BUF, off11));
        int off12 = off11 + 2;
        ze.setExternalAttributes(ZipLong.getValue(this.CFH_BUF, off12));
        byte[] fileName = new byte[fileNameLen];
        this.archive.readFully(fileName);
        ze.setName(entryEncoding.decode(fileName), fileName);
        OffsetEntry offset = new OffsetEntry(null);
        offset.headerOffset = ZipLong.getValue(this.CFH_BUF, off12 + 4);
        this.entries.put(ze, offset);
        this.nameMap.put(ze.getName(), ze);
        byte[] cdExtraData = new byte[extraLen];
        this.archive.readFully(cdExtraData);
        ze.setCentralDirectoryExtra(cdExtraData);
        setSizesAndOffsetFromZip64Extra(ze, offset, diskStart);
        byte[] comment = new byte[commentLen];
        this.archive.readFully(comment);
        ze.setComment(entryEncoding.decode(comment));
        if (!hasUTF8Flag && this.useUnicodeExtraFields) {
            noUTF8Flag.put(ze, new NameAndComment(fileName, comment, null));
        }
    }

    private void setSizesAndOffsetFromZip64Extra(@NonNull ZipEntry ze, OffsetEntry offset, int diskStart) throws IOException {
        Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField) ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        if (z64 != null) {
            boolean hasUncompressedSize = ze.getSize() == 4294967295L;
            boolean hasCompressedSize = ze.getCompressedSize() == 4294967295L;
            boolean hasRelativeHeaderOffset = offset.headerOffset == 4294967295L;
            z64.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, diskStart == 65535);
            if (hasUncompressedSize) {
                ze.setSize(z64.getSize().getLongValue());
            } else if (hasCompressedSize) {
                z64.setSize(new ZipEightByteInteger(ze.getSize()));
            }
            if (hasCompressedSize) {
                ze.setCompressedSize(z64.getCompressedSize().getLongValue());
            } else if (hasUncompressedSize) {
                z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
            }
            if (hasRelativeHeaderOffset) {
                offset.headerOffset = z64.getRelativeHeaderOffset().getLongValue();
            }
        }
    }

    private void positionAtCentralDirectory() throws IOException {
        positionAtEndOfCentralDirectoryRecord();
        boolean found = false;
        boolean searchedForZip64EOCD = this.archive.getFilePointer() > 20;
        if (searchedForZip64EOCD) {
            this.archive.seek(this.archive.getFilePointer() - 20);
            this.archive.readFully(this.WORD_BUF);
            found = Arrays.equals(ZipOutputStream.ZIP64_EOCD_LOC_SIG, this.WORD_BUF);
        }
        if (!found) {
            if (searchedForZip64EOCD) {
                skipBytes(16);
            }
            positionAtCentralDirectory32();
            return;
        }
        positionAtCentralDirectory64();
    }

    private void positionAtCentralDirectory64() throws IOException {
        skipBytes(4);
        this.archive.readFully(this.DWORD_BUF);
        this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
        this.archive.readFully(this.WORD_BUF);
        if (!Arrays.equals(this.WORD_BUF, ZipOutputStream.ZIP64_EOCD_SIG)) {
            throw new ZipException("archive's ZIP64 end of central directory locator is corrupt.");
        }
        skipBytes(44);
        this.archive.readFully(this.DWORD_BUF);
        this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
    }

    private void positionAtCentralDirectory32() throws IOException {
        skipBytes(16);
        this.archive.readFully(this.WORD_BUF);
        this.archive.seek(ZipLong.getValue(this.WORD_BUF));
    }

    private void positionAtEndOfCentralDirectoryRecord() throws IOException {
        boolean found = tryToLocateSignature(22L, 65557L, ZipOutputStream.EOCD_SIG);
        if (!found) {
            throw new ZipException("archive is not a ZIP archive");
        }
    }

    private boolean tryToLocateSignature(long minDistanceFromEnd, long maxDistanceFromEnd, byte[] sig) throws IOException {
        boolean found = false;
        long off = this.archive.length() - minDistanceFromEnd;
        long stopSearching = Math.max(0L, this.archive.length() - maxDistanceFromEnd);
        if (off >= 0) {
            while (true) {
                if (off >= stopSearching) {
                    this.archive.seek(off);
                    int curr = this.archive.read();
                    if (curr != -1) {
                        if (curr != sig[0] || this.archive.read() != sig[1] || this.archive.read() != sig[2] || this.archive.read() != sig[3]) {
                            off--;
                        } else {
                            found = true;
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (found) {
            this.archive.seek(off);
        }
        return found;
    }

    private void skipBytes(int count) throws IOException {
        int totalSkipped = 0;
        while (totalSkipped < count) {
            int skippedNow = this.archive.skipBytes(count - totalSkipped);
            if (skippedNow <= 0) {
                throw new EOFException();
            }
            totalSkipped += skippedNow;
        }
    }

    private void resolveLocalFileHeaderData(Map<ZipEntry, NameAndComment> entriesWithoutUTF8Flag) throws IOException {
        Map<ZipEntry, OffsetEntry> origMap = new LinkedHashMap<>(this.entries);
        this.entries.clear();
        for (Map.Entry<ZipEntry, OffsetEntry> ent : origMap.entrySet()) {
            ZipEntry ze = ent.getKey();
            OffsetEntry offsetEntry = ent.getValue();
            long offset = offsetEntry.headerOffset;
            this.archive.seek(LFH_OFFSET_FOR_FILENAME_LENGTH + offset);
            this.archive.readFully(this.SHORT_BUF);
            int fileNameLen = ZipShort.getValue(this.SHORT_BUF);
            this.archive.readFully(this.SHORT_BUF);
            int extraFieldLen = ZipShort.getValue(this.SHORT_BUF);
            int lenToSkip = fileNameLen;
            while (lenToSkip > 0) {
                int skipped = this.archive.skipBytes(lenToSkip);
                if (skipped <= 0) {
                    throw new IOException("failed to skip file name in local file header");
                }
                lenToSkip -= skipped;
            }
            byte[] localExtraData = new byte[extraFieldLen];
            this.archive.readFully(localExtraData);
            ze.setExtra(localExtraData);
            offsetEntry.dataOffset = LFH_OFFSET_FOR_FILENAME_LENGTH + offset + 2 + 2 + fileNameLen + extraFieldLen;
            if (entriesWithoutUTF8Flag.containsKey(ze)) {
                String orig = ze.getName();
                NameAndComment nc = entriesWithoutUTF8Flag.get(ze);
                ZipUtil.setNameAndCommentFromExtraFields(ze, nc.name, nc.comment);
                if (!orig.equals(ze.getName())) {
                    this.nameMap.remove(orig);
                    this.nameMap.put(ze.getName(), ze);
                }
            }
            this.entries.put(ze, offsetEntry);
        }
    }

    private boolean startsWithLocalFileHeader() throws IOException {
        this.archive.seek(0L);
        this.archive.readFully(this.WORD_BUF);
        return Arrays.equals(this.WORD_BUF, ZipOutputStream.LFH_SIG);
    }

    public static final class OffsetEntry {
        private long dataOffset;
        private long headerOffset;

        private OffsetEntry() {
            this.headerOffset = -1L;
            this.dataOffset = -1L;
        }

        OffsetEntry(OffsetEntry offsetEntry) {
            this();
        }
    }

    public static final class NameAndComment {
        private final byte[] comment;
        private final byte[] name;

        private NameAndComment(byte[] name, byte[] comment) {
            this.name = name;
            this.comment = comment;
        }

        NameAndComment(byte[] bArr, byte[] bArr2, NameAndComment nameAndComment) {
            this(bArr, bArr2);
        }
    }

    private class BoundedInputStream extends InputStream {
        private boolean addDummyByte = false;
        private long loc;
        private long remaining;

        BoundedInputStream(long start, long remaining) {
            this.remaining = remaining;
            this.loc = start;
        }

        @Override
        public int read() throws IOException {
            int read;
            long j = this.remaining;
            this.remaining = j - 1;
            if (j > 0) {
                synchronized (ZipFile.this.archive) {
                    long j2 = this.loc;
                    this.loc = j2 + 1;
                    ZipFile.this.archive.seek(j2);
                    read = ZipFile.this.archive.read();
                }
                return read;
            } else if (this.addDummyByte) {
                this.addDummyByte = false;
                return 0;
            } else {
                return -1;
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int ret;
            if (this.remaining <= 0) {
                if (this.addDummyByte) {
                    this.addDummyByte = false;
                    b[off] = 0;
                    return 1;
                }
                return -1;
            } else if (len > 0) {
                if (len > this.remaining) {
                    len = (int) this.remaining;
                }
                synchronized (ZipFile.this.archive) {
                    ZipFile.this.archive.seek(this.loc);
                    ret = ZipFile.this.archive.read(b, off, len);
                }
                if (ret > 0) {
                    this.loc += ret;
                    this.remaining -= ret;
                    return ret;
                }
                return ret;
            } else {
                return 0;
            }
        }

        void addDummy() {
            this.addDummyByte = true;
        }
    }
}
