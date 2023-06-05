package com.file.zip;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipException;

public class ZipOutputStream extends FilterOutputStream {
    public static final int DEFAULT_COMPRESSION = -1;
    public static final int DEFLATED = 8;
    @Deprecated
    public static final int EFS_FLAG = 2048;
    public static final int STORED = 0;
    protected static final byte[] LFH_SIG = ZipLong.LFH_SIG.getBytes();
    protected static final byte[] DD_SIG = ZipLong.DD_SIG.getBytes();
    protected static final byte[] CFH_SIG = ZipLong.CFH_SIG.getBytes();
    protected static final byte[] EOCD_SIG = ZipLong.getBytes(101010256);
    static final String DEFAULT_ENCODING = null;
    static final byte[] ZIP64_EOCD_SIG = ZipLong.getBytes(101075792);
    static final byte[] ZIP64_EOCD_LOC_SIG = ZipLong.getBytes(117853008);
    private static final int BUFFER_SIZE = 512;
    private static final int DEFLATER_BLOCK_SIZE = 8192;
    private static final byte[] EMPTY = new byte[0];
    private static final byte[] ZERO = new byte[2];
    private static final byte[] LZERO = new byte[4];
    private static final byte[] ONE = ZipLong.getBytes(1);
    protected final Deflater def;
    private final CRC32 crc;
    private final List<ZipEntry> entries;
    private final Map<java.util.zip.ZipEntry, Long> offsets;
    protected byte[] buf;
    private long cdLength;
    private long cdOffset;
    private String comment;
    private UnicodeExtraFieldPolicy createUnicodeExtraFields;
    private String encoding;
    private CurrentEntry entry;
    private boolean fallbackToUTF8;
    private boolean finished;
    private boolean hasCompressionLevelChanged;
    private boolean hasUsedZip64;
    private int level;
    private int method;
    private RandomAccessFile raf;
    private boolean useUTF8Flag;
    private long written;
    private Zip64Mode zip64Mode;
    private ZipEncoding zipEncoding;

    public ZipOutputStream(OutputStream out) {
        super(out);
        this.finished = false;
        this.comment = "";
        this.level = -1;
        this.hasCompressionLevelChanged = false;
        this.method = 8;
        this.entries = new LinkedList<>();
        this.crc = new CRC32();
        this.written = 0L;
        this.cdOffset = 0L;
        this.cdLength = 0L;
        this.offsets = new HashMap<>();
        this.encoding = null;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(DEFAULT_ENCODING);
        this.def = new Deflater(this.level, true);
        this.buf = new byte[512];
        this.useUTF8Flag = true;
        this.fallbackToUTF8 = false;
        this.createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
        this.hasUsedZip64 = false;
        this.zip64Mode = Zip64Mode.AsNeeded;
        this.raf = null;
    }

    public ZipOutputStream(File file) throws IOException {
        super(null);
        this.finished = false;
        this.comment = "";
        this.level = -1;
        this.hasCompressionLevelChanged = false;
        this.method = 8;
        this.entries = new LinkedList<>();
        this.crc = new CRC32();
        this.written = 0L;
        this.cdOffset = 0L;
        this.cdLength = 0L;
        this.offsets = new HashMap<>();
        this.encoding = null;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(DEFAULT_ENCODING);
        this.def = new Deflater(this.level, true);
        this.buf = new byte[512];
        this.useUTF8Flag = true;
        this.fallbackToUTF8 = false;
        this.createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
        this.hasUsedZip64 = false;
        this.zip64Mode = Zip64Mode.AsNeeded;
        RandomAccessFile _raf = null;
        try {
            RandomAccessFile _raf2 = new RandomAccessFile(file, "rw");
            try {
                _raf2.setLength(0L);
                _raf = _raf2;
            } catch (IOException e) {
                _raf = _raf2;
                if (_raf != null) {
                    try {
                        _raf.close();
                    } catch (IOException e2) {
                    }
                    _raf = null;
                }
                this.out = new FileOutputStream(file);
                this.raf = _raf;
            }
        } catch (IOException e3) {
        }
        this.raf = _raf;
    }

    @NonNull
    @Contract("_ -> new")
    protected static ZipLong toDosTime(Date time) {
        return ZipUtil.toDosTime(time);
    }

    protected static byte[] toDosTime(long t) {
        return ZipUtil.toDosTime(t);
    }

    protected static long adjustToLong(int i) {
        return ZipUtil.adjustToLong(i);
    }

    public boolean isSeekable() {
        return this.raf != null;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        if (this.useUTF8Flag && !ZipEncodingHelper.isUTF8(encoding)) {
            this.useUTF8Flag = false;
        }
    }

    public void setUseLanguageEncodingFlag(boolean b) {
        this.useUTF8Flag = b && ZipEncodingHelper.isUTF8(this.encoding);
    }

    public void setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy b) {
        this.createUnicodeExtraFields = b;
    }

    public void setFallbackToUTF8(boolean b) {
        this.fallbackToUTF8 = b;
    }

    public void setUseZip64(Zip64Mode mode) {
        this.zip64Mode = mode;
    }

    public void finish() throws IOException {
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        if (this.entry != null) {
            closeEntry();
        }
        this.cdOffset = this.written;
        for (ZipEntry ze : this.entries) {
            writeCentralFileHeader(ze);
        }
        this.cdLength = this.written - this.cdOffset;
        writeZip64CentralDirectory();
        writeCentralDirectoryEnd();
        this.offsets.clear();
        this.entries.clear();
        this.def.end();
        this.finished = true;
    }

    public void closeEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (this.entry == null) {
            throw new IOException("No current entry to close");
        }
        if (!this.entry.hasWritten) {
            write(EMPTY, 0, 0);
        }
        flushDeflater();
        Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
        long bytesWritten = this.written - this.entry.dataStart;
        long realCrc = this.crc.getValue();
        this.crc.reset();
        boolean actuallyNeedsZip64 = handleSizesAndCrc(bytesWritten, realCrc, effectiveMode);
        if (this.raf != null) {
            rewriteSizesAndCrc(actuallyNeedsZip64);
        }
        writeDataDescriptor(this.entry.entry);
        this.entry = null;
    }

    private void flushDeflater() throws IOException {
        if (this.entry.entry.getMethod() == 8) {
            this.def.finish();
            while (!this.def.finished()) {
                deflate();
            }
        }
    }

    private boolean handleSizesAndCrc(long bytesWritten, long crc, Zip64Mode effectiveMode) throws ZipException {
        if (this.entry.entry.getMethod() != 8) {
            if (this.raf == null) {
                if (this.entry.entry.getCrc() != crc) {
                    throw new ZipException("bad CRC checksum for entry " + this.entry.entry.getName() + ": " + Long.toHexString(this.entry.entry.getCrc()) + " instead of " + Long.toHexString(crc));
                }
                if (this.entry.entry.getSize() != bytesWritten) {
                    throw new ZipException("bad size for entry " + this.entry.entry.getName() + ": " + this.entry.entry.getSize() + " instead of " + bytesWritten);
                }
            } else {
                this.entry.entry.setSize(bytesWritten);
                this.entry.entry.setCompressedSize(bytesWritten);
                this.entry.entry.setCrc(crc);
            }
        } else {
            this.entry.entry.setSize(this.entry.bytesRead);
            this.entry.entry.setCompressedSize(bytesWritten);
            this.entry.entry.setCrc(crc);
            this.def.reset();
        }
        boolean actuallyNeedsZip64 = effectiveMode == Zip64Mode.Always || this.entry.entry.getSize() >= 4294967295L || this.entry.entry.getCompressedSize() >= 4294967295L;
        if (actuallyNeedsZip64 && effectiveMode == Zip64Mode.Never) {
            throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
        }
        return actuallyNeedsZip64;
    }

    private void rewriteSizesAndCrc(boolean actuallyNeedsZip64) throws IOException {
        long save = this.raf.getFilePointer();
        this.raf.seek(this.entry.localDataStart);
        writeOut(ZipLong.getBytes(this.entry.entry.getCrc()));
        if (hasZip64Extra(this.entry.entry) && actuallyNeedsZip64) {
            writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            writeOut(ZipLong.ZIP64_MAGIC.getBytes());
        } else {
            writeOut(ZipLong.getBytes(this.entry.entry.getCompressedSize()));
            writeOut(ZipLong.getBytes(this.entry.entry.getSize()));
        }
        if (hasZip64Extra(this.entry.entry)) {
            this.raf.seek(this.entry.localDataStart + 12 + 4 + getName(this.entry.entry).limit() + 4);
            writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getSize()));
            writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getCompressedSize()));
            if (!actuallyNeedsZip64) {
                this.raf.seek(this.entry.localDataStart - 10);
                writeOut(ZipShort.getBytes(10));
                this.entry.entry.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
                this.entry.entry.setExtra();
                if (this.entry.causedUseOfZip64) {
                    this.hasUsedZip64 = false;
                }
            }
        }
        this.raf.seek(save);
    }

    public void putNextEntry(ZipEntry archiveEntry) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (this.entry != null) {
            closeEntry();
        }
        this.entry = new CurrentEntry(archiveEntry, null);
        this.entries.add(this.entry.entry);
        setDefaults(this.entry.entry);
        Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
        validateSizeInformation(effectiveMode);
        if (shouldAddZip64Extra(this.entry.entry, effectiveMode)) {
            Zip64ExtendedInformationExtraField z64 = getZip64Extra(this.entry.entry);
            ZipEightByteInteger size = ZipEightByteInteger.ZERO;
            if (this.entry.entry.getMethod() == 0 && this.entry.entry.getSize() != -1) {
                size = new ZipEightByteInteger(this.entry.entry.getSize());
            }
            z64.setSize(size);
            z64.setCompressedSize(size);
            this.entry.entry.setExtra();
        }
        if (this.entry.entry.getMethod() == 8 && this.hasCompressionLevelChanged) {
            this.def.setLevel(this.level);
            this.hasCompressionLevelChanged = false;
        }
        writeLocalFileHeader(this.entry.entry);
    }

    private void setDefaults(@NonNull java.util.zip.ZipEntry entry) {
        if (entry.getMethod() == -1) {
            entry.setMethod(this.method);
        }
        if (entry.getTime() == -1) {
            entry.setTime(System.currentTimeMillis());
        }
    }

    private void validateSizeInformation(Zip64Mode effectiveMode) throws ZipException {
        if (this.entry.entry.getMethod() == 0 && this.raf == null) {
            if (this.entry.entry.getSize() == -1) {
                throw new ZipException("uncompressed size is required for STORED method when not writing to a file");
            }
            if (this.entry.entry.getCrc() == -1) {
                throw new ZipException("crc checksum is required for STORED method when not writing to a file");
            }
            this.entry.entry.setCompressedSize(this.entry.entry.getSize());
        }
        if ((this.entry.entry.getSize() >= 4294967295L || this.entry.entry.getCompressedSize() >= 4294967295L) && effectiveMode == Zip64Mode.Never) {
            throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
        }
    }

    private boolean shouldAddZip64Extra(java.util.zip.ZipEntry entry, Zip64Mode mode) {
        return mode == Zip64Mode.Always || entry.getSize() >= 4294967295L || entry.getCompressedSize() >= 4294967295L || !(entry.getSize() != -1 || this.raf == null || mode == Zip64Mode.Never);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLevel(int level) {
        if (level < -1 || level > 9) {
            throw new IllegalArgumentException("Invalid compression level: " + level);
        }
        this.hasCompressionLevelChanged = this.level != level;
        this.level = level;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public boolean canWriteEntryData(ZipEntry ae) {
        return ZipUtil.canHandleEntryData(ae);
    }

    @Override
    public void write(byte[] b, int offset, int length) throws IOException {
        ZipUtil.checkRequestedFeatures(this.entry.entry);
        this.entry.hasWritten = true;
        if (this.entry.entry.getMethod() == 8) {
            writeDeflated(b, offset, length);
        } else {
            writeOut(b, offset, length);
            this.written += length;
        }
        this.crc.update(b, offset, length);
    }

    private void writeDeflated(byte[] b, int offset, int length) throws IOException {
        if (length > 0 && !this.def.finished()) {
            this.entry.bytesRead += length;
            if (length <= 8192) {
                this.def.setInput(b, offset, length);
                deflateUntilInputIsNeeded();
                return;
            }
            int fullblocks = length / 8192;
            for (int i = 0; i < fullblocks; i++) {
                this.def.setInput(b, (i * 8192) + offset, 8192);
                deflateUntilInputIsNeeded();
            }
            int done = fullblocks * 8192;
            if (done < length) {
                this.def.setInput(b, offset + done, length - done);
                deflateUntilInputIsNeeded();
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (!this.finished) {
            finish();
        }
        destroy();
    }

    @Override
    public void flush() throws IOException {
        if (this.out != null) {
            this.out.flush();
        }
    }

    protected final void deflate() throws IOException {
        int len = this.def.deflate(this.buf, 0, this.buf.length);
        if (len > 0) {
            writeOut(this.buf, 0, len);
            this.written += len;
        }
    }

    protected void writeLocalFileHeader(@NonNull ZipEntry ze) throws IOException {
        boolean encodable = this.zipEncoding.canEncode(ze.getName());
        ByteBuffer name = getName(ze);
        if (this.createUnicodeExtraFields != UnicodeExtraFieldPolicy.NEVER) {
            addUnicodeExtraFields(ze, encodable, name);
        }
        this.offsets.put(ze, this.written);
        writeOut(LFH_SIG);
        this.written += 4;
        int zipMethod = ze.getMethod();
        writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, !encodable && this.fallbackToUTF8, hasZip64Extra(ze));
        this.written += 4;
        writeOut(ZipShort.getBytes(zipMethod));
        this.written += 2;
        writeOut(ZipUtil.toDosTime(ze.getTime()));
        this.written += 4;
        this.entry.localDataStart = this.written;
        if (zipMethod == 8 || this.raf != null) {
            writeOut(LZERO);
            if (hasZip64Extra(this.entry.entry)) {
                writeOut(ZipLong.ZIP64_MAGIC.getBytes());
                writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            } else {
                writeOut(LZERO);
                writeOut(LZERO);
            }
        } else {
            writeOut(ZipLong.getBytes(ze.getCrc()));
            byte[] size = ZipLong.ZIP64_MAGIC.getBytes();
            if (!hasZip64Extra(ze)) {
                size = ZipLong.getBytes(ze.getSize());
            }
            writeOut(size);
            writeOut(size);
        }
        this.written += 12;
        writeOut(ZipShort.getBytes(name.limit()));
        this.written += 2;
        byte[] extra = ze.getLocalFileDataExtra();
        writeOut(ZipShort.getBytes(extra.length));
        this.written += 2;
        writeOut(name.array(), name.arrayOffset(), name.limit() - name.position());
        this.written += name.limit();
        writeOut(extra);
        this.written += extra.length;
        this.entry.dataStart = this.written;
    }

    private void addUnicodeExtraFields(ZipEntry ze, boolean encodable, ByteBuffer name) throws IOException {
        if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !encodable) {
            ze.addExtraField(new UnicodePathExtraField(ze.getName(), name.array(), name.arrayOffset(), name.limit() - name.position()));
        }
        String comm = ze.getComment();
        if (comm != null && !"".equals(comm)) {
            boolean commentEncodable = this.zipEncoding.canEncode(comm);
            if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !commentEncodable) {
                ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
                ze.addExtraField(new UnicodeCommentExtraField(comm, commentB.array(), commentB.arrayOffset(), commentB.limit() - commentB.position()));
            }
        }
    }

    protected void writeDataDescriptor(@NonNull ZipEntry ze) throws IOException {
        if (ze.getMethod() == 8 && this.raf == null) {
            writeOut(DD_SIG);
            writeOut(ZipLong.getBytes(ze.getCrc()));
            int sizeFieldSize = 4;
            if (!hasZip64Extra(ze)) {
                writeOut(ZipLong.getBytes(ze.getCompressedSize()));
                writeOut(ZipLong.getBytes(ze.getSize()));
            } else {
                sizeFieldSize = 8;
                writeOut(ZipEightByteInteger.getBytes(ze.getCompressedSize()));
                writeOut(ZipEightByteInteger.getBytes(ze.getSize()));
            }
            this.written += (sizeFieldSize * 2) + 8;
        }
    }

    protected void writeCentralFileHeader(ZipEntry ze) throws IOException {
        writeOut(CFH_SIG);
        this.written += 4;
        long lfhOffset = this.offsets.get(ze);
        boolean needsZip64Extra = hasZip64Extra(ze) || ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L || lfhOffset >= 4294967295L;
        if (needsZip64Extra && this.zip64Mode == Zip64Mode.Never) {
            throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
        }
        handleZip64Extra(ze, lfhOffset, needsZip64Extra);
        writeOut(ZipShort.getBytes((!this.hasUsedZip64 ? 20 : 45) | (ze.getPlatform() << 8)));
        this.written += 2;
        int zipMethod = ze.getMethod();
        boolean encodable = this.zipEncoding.canEncode(ze.getName());
        writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, !encodable && this.fallbackToUTF8, needsZip64Extra);
        this.written += 4;
        writeOut(ZipShort.getBytes(zipMethod));
        this.written += 2;
        writeOut(ZipUtil.toDosTime(ze.getTime()));
        this.written += 4;
        writeOut(ZipLong.getBytes(ze.getCrc()));
        if (ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L) {
            writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            writeOut(ZipLong.ZIP64_MAGIC.getBytes());
        } else {
            writeOut(ZipLong.getBytes(ze.getCompressedSize()));
            writeOut(ZipLong.getBytes(ze.getSize()));
        }
        this.written += 12;
        ByteBuffer name = getName(ze);
        writeOut(ZipShort.getBytes(name.limit()));
        this.written += 2;
        byte[] extra = ze.getCentralDirectoryExtra();
        writeOut(ZipShort.getBytes(extra.length));
        this.written += 2;
        String comm = ze.getComment();
        if (comm == null) {
            comm = "";
        }
        ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
        writeOut(ZipShort.getBytes(commentB.limit()));
        this.written += 2;
        writeOut(ZERO);
        this.written += 2;
        writeOut(ZipShort.getBytes(ze.getInternalAttributes()));
        this.written += 2;
        writeOut(ZipLong.getBytes(ze.getExternalAttributes()));
        this.written += 4;
        writeOut(ZipLong.getBytes(Math.min(lfhOffset, 4294967295L)));
        this.written += 4;
        writeOut(name.array(), name.arrayOffset(), name.limit() - name.position());
        this.written += name.limit();
        writeOut(extra);
        this.written += extra.length;
        writeOut(commentB.array(), commentB.arrayOffset(), commentB.limit() - commentB.position());
        this.written += commentB.limit();
    }

    private void handleZip64Extra(ZipEntry ze, long lfhOffset, boolean needsZip64Extra) {
        if (needsZip64Extra) {
            Zip64ExtendedInformationExtraField z64 = getZip64Extra(ze);
            if (ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L) {
                z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
                z64.setSize(new ZipEightByteInteger(ze.getSize()));
            } else {
                z64.setCompressedSize(null);
                z64.setSize(null);
            }
            if (lfhOffset >= 4294967295L) {
                z64.setRelativeHeaderOffset(new ZipEightByteInteger(lfhOffset));
            }
            ze.setExtra();
        }
    }

    protected void writeCentralDirectoryEnd() throws IOException {
        writeOut(EOCD_SIG);
        writeOut(ZERO);
        writeOut(ZERO);
        int numberOfEntries = this.entries.size();
        if (numberOfEntries > 65535 && this.zip64Mode == Zip64Mode.Never) {
            throw new Zip64RequiredException("archive contains more than 65535 entries.");
        }
        if (this.cdOffset > 4294967295L && this.zip64Mode == Zip64Mode.Never) {
            throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
        }
        byte[] num = ZipShort.getBytes(Math.min(numberOfEntries, 65535));
        writeOut(num);
        writeOut(num);
        writeOut(ZipLong.getBytes(Math.min(this.cdLength, 4294967295L)));
        writeOut(ZipLong.getBytes(Math.min(this.cdOffset, 4294967295L)));
        ByteBuffer data = this.zipEncoding.encode(this.comment);
        writeOut(ZipShort.getBytes(data.limit()));
        writeOut(data.array(), data.arrayOffset(), data.limit() - data.position());
    }

    protected byte[] getBytes(String name) throws ZipException {
        try {
            ByteBuffer b = ZipEncodingHelper.getZipEncoding(this.encoding).encode(name);
            byte[] result = new byte[b.limit()];
            System.arraycopy(b.array(), b.arrayOffset(), result, 0, result.length);
            return result;
        } catch (IOException ex) {
            throw new ZipException("Failed to encode name: " + ex.getMessage());
        }
    }

    protected void writeZip64CentralDirectory() throws IOException {
        if (this.zip64Mode != Zip64Mode.Never) {
            if (!this.hasUsedZip64 && (this.cdOffset >= 4294967295L || this.cdLength >= 4294967295L || this.entries.size() >= 65535)) {
                this.hasUsedZip64 = true;
            }
            if (this.hasUsedZip64) {
                long offset = this.written;
                writeOut(ZIP64_EOCD_SIG);
                writeOut(ZipEightByteInteger.getBytes(44L));
                writeOut(ZipShort.getBytes(45));
                writeOut(ZipShort.getBytes(45));
                writeOut(LZERO);
                writeOut(LZERO);
                byte[] num = ZipEightByteInteger.getBytes(this.entries.size());
                writeOut(num);
                writeOut(num);
                writeOut(ZipEightByteInteger.getBytes(this.cdLength));
                writeOut(ZipEightByteInteger.getBytes(this.cdOffset));
                writeOut(ZIP64_EOCD_LOC_SIG);
                writeOut(LZERO);
                writeOut(ZipEightByteInteger.getBytes(offset));
                writeOut(ONE);
            }
        }
    }

    protected final void writeOut(byte[] data) throws IOException {
        writeOut(data, 0, data.length);
    }

    protected final void writeOut(byte[] data, int offset, int length) throws IOException {
        if (this.raf != null) {
            this.raf.write(data, offset, length);
        } else {
            this.out.write(data, offset, length);
        }
    }

    private void deflateUntilInputIsNeeded() throws IOException {
        while (!this.def.needsInput()) {
            deflate();
        }
    }

    private void writeVersionNeededToExtractAndGeneralPurposeBits(int zipMethod, boolean utfFallback, boolean zip64) throws IOException {
        int versionNeededToExtract = 10;
        GeneralPurposeBit b = new GeneralPurposeBit();
        b.useUTF8ForNames(this.useUTF8Flag || utfFallback);
        if (zipMethod == 8 && this.raf == null) {
            versionNeededToExtract = 20;
            b.useDataDescriptor(true);
        }
        if (zip64) {
            versionNeededToExtract = 45;
        }
        writeOut(ZipShort.getBytes(versionNeededToExtract));
        writeOut(b.encode());
    }

    @NonNull
    private Zip64ExtendedInformationExtraField getZip64Extra(ZipEntry ze) {
        if (this.entry != null) {
            this.entry.causedUseOfZip64 = !this.hasUsedZip64;
        }
        this.hasUsedZip64 = true;
        Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField) ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        if (z64 == null) {
            z64 = new Zip64ExtendedInformationExtraField();
        }
        ze.addAsFirstExtraField(z64);
        return z64;
    }

    private boolean hasZip64Extra(@NonNull ZipEntry ze) {
        return ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID) != null;
    }

    private Zip64Mode getEffectiveZip64Mode(java.util.zip.ZipEntry ze) {
        return (this.zip64Mode == Zip64Mode.AsNeeded && this.raf == null && ze.getMethod() == 8 && ze.getSize() == -1) ? Zip64Mode.Never : this.zip64Mode;
    }

    private ZipEncoding getEntryEncoding(@NonNull java.util.zip.ZipEntry ze) {
        boolean encodable = this.zipEncoding.canEncode(ze.getName());
        return (encodable || !this.fallbackToUTF8) ? this.zipEncoding : ZipEncodingHelper.UTF8_ZIP_ENCODING;
    }

    private ByteBuffer getName(java.util.zip.ZipEntry ze) throws IOException {
        return getEntryEncoding(ze).encode(ze.getName());
    }

    void destroy() throws IOException {
        if (this.raf != null) {
            this.raf.close();
        }
        if (this.out != null) {
            this.out.close();
        }
    }

    public static final class UnicodeExtraFieldPolicy {
        public static final UnicodeExtraFieldPolicy ALWAYS = new UnicodeExtraFieldPolicy("always");
        public static final UnicodeExtraFieldPolicy NEVER = new UnicodeExtraFieldPolicy("never");
        public static final UnicodeExtraFieldPolicy NOT_ENCODEABLE = new UnicodeExtraFieldPolicy("not encodeable");
        private final String name;

        private UnicodeExtraFieldPolicy(String n) {
            this.name = n;
        }

        @NonNull
        public String toString() {
            return this.name;
        }
    }

    public static final class CurrentEntry {
        private final ZipEntry entry;
        private long bytesRead;
        private boolean causedUseOfZip64;
        private long dataStart;
        private boolean hasWritten;
        private long localDataStart;

        private CurrentEntry(ZipEntry entry) {
            this.localDataStart = 0L;
            this.dataStart = 0L;
            this.bytesRead = 0L;
            this.causedUseOfZip64 = false;
            this.entry = entry;
        }

        CurrentEntry(ZipEntry zipEntry, CurrentEntry currentEntry) {
            this(zipEntry);
        }
    }
}
