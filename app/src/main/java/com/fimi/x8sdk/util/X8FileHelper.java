package com.fimi.x8sdk.util;

import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.FileUtil;
import com.fimi.x8sdk.X8FcLogManager;
import com.fimi.x8sdk.entity.X8FlightLogFile;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class X8FileHelper {
    private static final String[] flightLogFileSuffix = {X8FcLogManager.FLIGHT_PLAYBACK, X8FcLogManager.prexSD, X8FcLogManager.getInstance().prexCollect};
    private static final String[] zipFileSuffix = {X8FcLogManager.prexFC, X8FcLogManager.prexCM, X8FcLogManager.prexAPP, X8FcLogManager.prexFcStatus};

    public static void deleteFiles() {
        FileUtil.deleteFile(new File(DirectoryPath.getX8B2oxPath()));
    }

    public static void deleteFlightLogFiles(List<X8FlightLogFile> lists, boolean isDeleteAll) {
        if (isDeleteAll) {
            FileUtil.deleteFile(new File(DirectoryPath.getX8LoginFlightPlaybackPath(null)));
            return;
        }
        for (X8FlightLogFile x8FlightLogFile : lists) {
            if (!x8FlightLogFile.isFileLogCollect()) {
                FileUtil.deleteFile(new File(x8FlightLogFile.getFile().getAbsolutePath()));
            }
        }
    }

    public static List<File> listDirs() {
        List<File> list = FileUtil.listDirs(new File(DirectoryPath.getX8B2oxPath()), X8FcLogManager.prexSD, X8FcLogManager.getInstance().getCurrentWrite());
        return list;
    }

    public static List<File> flightLogListDirs(String path) {
        if (path == null || path.equals("")) {
            List<File> list = FileUtil.listFiles3(new File(DirectoryPath.getX8LoginFlightPlaybackPath("")));
            return list;
        }
        List<File> list2 = FileUtil.listFiles3(new File(path), flightLogFileSuffix);
        return list2;
    }

    public static long getFlightLogDirSize(File file) {
        long len = 0;
        List<File> list = FileUtil.listFiles3(file, flightLogFileSuffix);
        if (list != null && list.size() > 0) {
            for (File f : list) {
                len += f.length();
            }
        }
        return len;
    }

    public static long getDirSize(File file) {
        long len = 0;
        List<File> list = FileUtil.listFiles3(file, zipFileSuffix);
        if (list != null && list.size() > 0) {
            for (File f : list) {
                len += f.length();
            }
        }
        return len;
    }

    public void test() {
        Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = p.matcher("[INFO][2018-04 10:29:08 911][http-nio-6900-exec-8]");
        if (matcher.find()) {
            System.out.println(matcher.groupCount());
            System.out.println(matcher.group(0));
        }
    }
}
