package com.fimi.x8sdk.update.fwpack;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.fimi.kernel.dataparser.milink.ByteArrayToIntArray;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.FileUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/* loaded from: classes2.dex */
public class FirmwareBuildPack implements IBuildPackInfo {
    public static final int BUFSIZE = 8192;
    public static final int ERROR = 1;
    public static final int SUCCESS = 0;
    public static final String PKG_UPDATE_OUTFILE = DirectoryPath.getFwTempFilePath() + "/update_fileData";
    public static final String PKG_HEADER_FILE = DirectoryPath.getFwTempFilePath() + "/update_file_header";
    public static final String PKG_NOCRC = DirectoryPath.getFwTempFilePath() + "/pgk_no_crc";
    public static final String PKG_UPDATE_FILE = DirectoryPath.getFirmwarePath() + "/all_chips.bin";
    public static final String PKG_CRC = DirectoryPath.getFwTempFilePath() + "/pgk_crc";
    MergFileListener listener;
    Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.fimi.x8sdk.update.fwpack.FirmwareBuildPack.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0 && FirmwareBuildPack.this.listener != null) {
                FirmwareBuildPack.this.listener.mergResult(0);
            }
        }
    };
    private List<FwInfo> fws;

    public FirmwareBuildPack(MergFileListener listener, List<FwInfo> fws) {
        this.listener = listener;
        this.fws = fws;
        FileUtil.createFileAndPaperFile(DirectoryPath.getFwTempFilePath());
    }

    @Override // com.fimi.x8sdk.update.fwpack.IBuildPackInfo
    public void createUpdatePkg() {
        mergFwDataFile(PKG_UPDATE_OUTFILE);
        FileUtil.createFile(PKG_HEADER_FILE);
        FileUtil.addFileContent(PKG_HEADER_FILE, getfwPackInfo());
        FileUtil.addFileContent(PKG_HEADER_FILE, getFwInfo());
        String[] merfwFile = {PKG_HEADER_FILE, PKG_UPDATE_OUTFILE};
        FileUtil.meragerFiles(PKG_NOCRC, merfwFile);
        byte[] crc = getPackCRC(PKG_NOCRC);
        FileUtil.createFile(PKG_CRC);
        FileUtil.addFileContent(PKG_CRC, crc);
        String[] files = {PKG_CRC, PKG_NOCRC};
        FileUtil.meragerFiles(PKG_UPDATE_FILE, files);
        this.mHandler.sendEmptyMessage(0);
    }

    @Override // com.fimi.x8sdk.update.fwpack.IBuildPackInfo
    public byte[] getfwPackInfo() {
        byte[] packInfo = new byte[124];
        byte[] mainver = ByteHexHelper.shortToBytes((short) 0);
        System.arraycopy(mainver, 0, packInfo, 0, 2);
        byte[] subver = ByteHexHelper.shortToBytes((short) 0);
        System.arraycopy(subver, 0, packInfo, 2, 2);
        packInfo[4] = (byte) this.fws.size();
        byte[] id1 = ByteHexHelper.intToFourHexBytes(0);
        System.arraycopy(id1, 0, packInfo, 5, 4);
        return packInfo;
    }

    @Override // com.fimi.x8sdk.update.fwpack.IBuildPackInfo
    public byte[] getPackCRC(String fileName) {
        byte[] fileBytes = FileUtil.getFileBytes(fileName);
        int crc = ByteArrayToIntArray.CRC32Software(fileBytes, fileBytes.length);
        byte[] byteCrc = ByteHexHelper.intToFourHexBytes(crc);
        return byteCrc;
    }

    @Override // com.fimi.x8sdk.update.fwpack.IBuildPackInfo
    public byte[] getFwInfo() {
        int count = this.fws.size();
        byte[] fwInfos = new byte[count * 64];
        int header = fwInfos.length + 128;
        int fwLen = 0;
        for (int i = 0; i < count; i++) {
            byte[] oneFw = getOneFwInfo(this.fws.get(i));
            if (i > 0) {
                fwLen = (int) (FileUtil.getFileLenght(DirectoryPath.getFirmwarePath() + "/" + this.fws.get(i - 1).getSysName()) + fwLen);
            }
            int sartAddr = fwLen + header;
            byte[] addr = ByteHexHelper.intToFourHexBytes(sartAddr);
            System.arraycopy(addr, 0, oneFw, 0, 4);
            System.arraycopy(oneFw, 0, fwInfos, i * 64, 64);
            System.out.println("after:" + ByteHexHelper.bytesToHexString(addr));
        }
        return fwInfos;
    }

    @Override // com.fimi.x8sdk.update.fwpack.IBuildPackInfo
    public byte[] getOneFwInfo(FwInfo fw) {
        byte[] iByte = new byte[64];
        byte[] size = ByteHexHelper.intToFourHexBytes((int) FileUtil.getFileLenght(DirectoryPath.getFirmwarePath() + "/" + fw.getSysName()));
        System.arraycopy(size, 0, iByte, 4, 4);
        iByte[8] = fw.getModelId();
        iByte[9] = fw.getTypeId();
        iByte[10] = fw.getFwType();
        byte[] softver = ByteHexHelper.shortToBytes(fw.getSoftwareVer());
        System.arraycopy(softver, 0, iByte, 11, 2);
        iByte[13] = fw.getStepVer();
        return iByte;
    }

    @Override // com.fimi.x8sdk.update.fwpack.IBuildPackInfo
    public void mergFwDataFile(String outFile) {
        FileChannel outChannel = null;
        try {
            try {
                outChannel = new FileOutputStream(outFile).getChannel();
                for (FwInfo fw : this.fws) {
                    FileChannel fc = new FileInputStream(DirectoryPath.getFirmwarePath() + "/" + fw.getSysName()).getChannel();
                    ByteBuffer bb = ByteBuffer.allocate(8192);
                    while (fc.read(bb) != -1) {
                        bb.flip();
                        outChannel.write(bb);
                        bb.clear();
                    }
                    fc.close();
                }
                if (outChannel != null) {
                    try {
                        outChannel.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                if (outChannel != null) {
                    try {
                        outChannel.close();
                    } catch (IOException e2) {
                    }
                }
            }
        } catch (Throwable th) {
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    /* loaded from: classes2.dex */
    public interface MergFileListener {
        void mergResult(int i);
    }
}
