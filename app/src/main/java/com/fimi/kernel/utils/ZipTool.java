package com.fimi.kernel.utils;

import com.file.zip.ZipEntry;
import com.file.zip.ZipFile;
import com.file.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;


public class ZipTool {
    public static final int EXIST_UNZIPFILE = 2;
    public static final int EXIST_ZIPFILE = 4;
    public static final int NOTEXIST_ZIPFILE = 1;
    public static final int NULL_ZIPPATH = 0;
    public static final int ZIPOPTION_FAIL = 5;
    public static final int ZIPOPTION_SUCCESS = 3;

    public static int unzip(String zipFilePath, String unzipPath) {
        if (zipFilePath == null || "".equals(zipFilePath.trim())) {
            return 0;
        }
        File dirFile = new File(unzipPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(zipFilePath);
        if (!file.exists()) {
            return 1;
        }
        try {
            ZipFile zipFile = new ZipFile(file, "GBK");
            Enumeration<? extends ZipEntry> entries = zipFile.getEntries();
            String unzipAbpath = dirFile.getAbsolutePath();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                unzipEachFile(zipFile, entry, unzipAbpath);
            }
            return 3;
        } catch (IOException e) {
            e.printStackTrace();
            return 5;
        }
    }

    private static void unzipEachFile(ZipFile zipFile, ZipEntry entry, String unzipAbpath) {
        byte[] buffer = new byte[5120];
        String name = entry.getName();
        String fileName = name;
        String tempDir = "";
        if (entry.isDirectory()) {
            File tempFile = new File(unzipAbpath + File.separator + name);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
        }
        int index = name.lastIndexOf(File.separator);
        if (index != -1) {
            fileName = name.substring(index);
            tempDir = name.substring(0, index);
            File tempDirFile = new File(unzipAbpath + File.separator + tempDir);
            if (!tempDirFile.exists()) {
                tempDirFile.mkdirs();
            }
        }
        String zipPath = unzipAbpath + File.separator + tempDir + fileName;
        File tempFile2 = new File(zipPath);
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            try {
                is = zipFile.getInputStream(entry);
                if (!tempFile2.exists()) {
                    tempFile2.createNewFile();
                }
                FileOutputStream fos2 = new FileOutputStream(tempFile2);
                while (true) {
                    try {
                        int readSize = is.read(buffer);
                        if (readSize > 0) {
                            fos2.write(buffer, 0, readSize);
                        } else {
                            try {
                                is.close();
                                fos2.close();
                                return;
                            } catch (IOException e) {
                                new File(unzipAbpath).delete();
                                e.printStackTrace();
                                return;
                            }
                        }
                    } catch (Exception e2) {
                        fos = fos2;
                        new File(unzipAbpath).delete();
                        try {
                            is.close();
                            fos.close();
                            return;
                        } catch (IOException e3) {
                            new File(unzipAbpath).delete();
                            e3.printStackTrace();
                            return;
                        }
                    } catch (Throwable th) {
                        th = th;
                        fos = fos2;
                        try {
                            is.close();
                            fos.close();
                        } catch (IOException e4) {
                            new File(unzipAbpath).delete();
                            e4.printStackTrace();
                        }
                        throw th;
                    }
                }
            } catch (Throwable th2) {
            }
        } catch (Exception e5) {
        }
    }

    public static int zip(String newZipPath, File[] wantZipFile) {
        File file = new File(newZipPath);
        if (file.exists()) {
            return 4;
        }
        String filePath = file.getAbsolutePath();
        String basePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        File dirFile = new File(basePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        ZipOutputStream zos = null;
        try {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                ZipOutputStream zos2 = new ZipOutputStream(fos);
                for (File tempFile : wantZipFile) {
                    if (tempFile.exists()) {
                        zipEachFile(zos2, tempFile, "");
                    }
                }
                try {
                    zos2.close();
                    return 3;
                } catch (IOException e) {
                    file.delete();
                    e.printStackTrace();
                    return 5;
                }
            } catch (IOException e5) {
            }
        } catch (Throwable th2) {
        }
        return 0;
    }

    private static void zipEachFile(ZipOutputStream zos, File zipFile, String baseDir) {
        if (zipFile.isDirectory()) {
            String baseDir2 = baseDir + zipFile.getName() + File.separator;
            File[] tempFiles = zipFile.listFiles();
            for (File tempFile : tempFiles) {
                zipEachFile(zos, tempFile, baseDir2);
            }
            return;
        }
        FileInputStream fis = null;
        try {
            ZipEntry entry = new ZipEntry(baseDir + zipFile.getName());
            try {
                zos.putNextEntry(entry);
                FileInputStream fis2 = new FileInputStream(zipFile);
                try {
                    byte[] buffer = new byte[5120];
                    while (true) {
                        int readSize = fis2.read(buffer);
                        if (readSize > 0) {
                            zos.write(buffer, 0, readSize);
                        } else {
                            try {
                                fis2.close();
                                zos.closeEntry();
                                return;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                } catch (IOException e2) {
                    fis = fis2;
                    try {
                        fis.close();
                        zos.closeEntry();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                } catch (Throwable th) {
                    th = th;
                    fis = fis2;
                    try {
                        fis.close();
                        zos.closeEntry();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
            }
        } catch (Throwable th3) {
        }
    }
}
