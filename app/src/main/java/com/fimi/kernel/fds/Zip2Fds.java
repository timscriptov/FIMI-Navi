package com.fimi.kernel.fds;

import androidx.annotation.NonNull;

import com.file.zip.ZipEntry;
import com.file.zip.ZipOutputStream;
import com.fimi.kernel.utils.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class Zip2Fds {
    public static final int EXIST_UNZIPFILE = 2;
    public static final int EXIST_ZIPFILE = 4;
    public static final int NOTEXIST_ZIPFILE = 1;
    public static final int NULL_ZIPPATH = 0;
    public static final int ZIPOPTION_FAIL = 5;
    public static final int ZIPOPTION_SUCCESS = 3;
    FileInputStream fis = null;
    private FileOutputStream fos;
    private boolean isStop;
    private ZipOutputStream zos = null;

    public boolean log2Zip(@NonNull File file, String[] suffix) {
        String zipName = file.getName() + ".zip";
        List<File> list = FileUtil.listFiles3(file, suffix);
        if (list == null || list.size() <= 0) {
            return false;
        }
        int code = zip2(file.getAbsolutePath(), zipName, list);
        return code == 3 || code == 4;
    }

    private void zipEachFile(ZipOutputStream zos, File zipFile, String baseDir) {
        try {
            ZipEntry entry = new ZipEntry(baseDir + zipFile.getName());
            try {
                zos.putNextEntry(entry);
                this.fis = new FileInputStream(zipFile);
                byte[] buffer = new byte[5120];
                while (true) {
                    int readSize = this.fis.read(buffer);
                    if (readSize > 0 && !this.isStop) {
                        zos.write(buffer, 0, readSize);
                    } else {
                        try {
                            this.fis.close();
                            this.fis = null;
                            zos.closeEntry();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            } catch (IOException e2) {
                try {
                    this.fis.close();
                    this.fis = null;
                    zos.closeEntry();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            } catch (Throwable th) {
                th = th;
                try {
                    this.fis.close();
                    this.fis = null;
                    zos.closeEntry();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                throw th;
            }
        } catch (Throwable th2) {
        }
    }

    public int zip2(String fileDir, String name, List<File> wantZipFile) {
        File file1 = new File(fileDir + "/" + name);
        if (file1.exists()) {
            return 4;
        }
        String tmpName = "" + name.hashCode() + ".zip";
        File file = new File(fileDir + "/" + tmpName);
        try {
            try {
                this.fos = new FileOutputStream(file);
                this.zos = new ZipOutputStream(this.fos);
                for (File tempFile : wantZipFile) {
                    if (tempFile.exists()) {
                        if (this.isStop) {
                            break;
                        }
                        zipEachFile(this.zos, tempFile, "");
                    }
                }
                if (!this.isStop) {
                    file.renameTo(file1);
                }
                try {
                    this.zos.close();
                    return 3;
                } catch (IOException e) {
                    file.delete();
                    e.printStackTrace();
                    return 5;
                }
            } catch (IOException e2) {
                file.delete();
                try {
                    this.zos.close();
                    return 5;
                } catch (IOException e3) {
                    file.delete();
                    e3.printStackTrace();
                    return 5;
                }
            }
        } catch (Throwable th) {
            try {
                this.zos.close();
                throw th;
            } catch (IOException e4) {
                file.delete();
                e4.printStackTrace();
                return 5;
            }
        }
    }

    public void stop() {
        this.isStop = true;
        if (this.fos != null) {
            try {
                this.fos.close();
            } catch (IOException e) {
            }
            this.fos = null;
        }
        if (this.zos != null) {
            try {
                this.zos.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (this.fis != null) {
            try {
                this.fis.close();
            } catch (IOException e3) {
            }
            this.fis = null;
        }
    }
}
