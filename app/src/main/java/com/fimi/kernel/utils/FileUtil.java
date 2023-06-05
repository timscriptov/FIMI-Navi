package com.fimi.kernel.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class FileUtil {
    public static final int BUFSIZE = 8192;

    public static String[] getFileList(String path, FilenameFilter filter) {
        File mPath = new File(path);
        try {
            mPath.mkdirs();
            if (mPath.exists()) {
                return mPath.list(filter);
            }
        } catch (SecurityException e) {
        }
        return new String[0];
    }

    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals("mounted");
        } catch (Exception e) {
            LogUtil.e("", "手机SD卡不能用:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFile(File file) {
        try {
            if (isCanUseSD()) {
                if (file == null) {
                    return true;
                }
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File file2 : files) {
                        deleteFile(file2);
                    }
                    file.delete();
                } else {
                    file.delete();
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String createFileNmae(String extension) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        String formatDate = format.format(new Date());
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        return formatDate + extension;
    }

    public static List<File> listFiles(String file, String format) {
        return listFiles(new File(file), format, null);
    }

    public static List<File> listFiles(File file, final String extension, final String content) {
        File[] files;
        if (file == null || !file.exists() || !file.isDirectory() || (files = file.listFiles(new FilenameFilter() { // from class: com.fimi.kernel.utils.FileUtil.1
            @Override // java.io.FilenameFilter
            public boolean accept(File arg0, String arg1) {
                if (content == null || content.equals("")) {
                    return arg1.endsWith(extension);
                }
                return arg1.contains(content) && arg1.endsWith(extension);
            }
        })) == null) {
            return null;
        }
        List<File> list = new ArrayList<>(Arrays.asList(files));
        sortList(list, false);
        return list;
    }

    public static List<File> listFiles2(File file, final String endwith1, final String endwith2) {
        File[] files;
        if (file == null || !file.exists() || !file.isDirectory() || (files = file.listFiles(new FilenameFilter() { // from class: com.fimi.kernel.utils.FileUtil.2
            @Override // java.io.FilenameFilter
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(endwith2) || arg1.endsWith(endwith1);
            }
        })) == null) {
            return null;
        }
        List<File> list = new ArrayList<>(Arrays.asList(files));
        sortList(list, false);
        return list;
    }

    public static void sortList(List<File> list, final boolean asc) {
        Collections.sort(list, new Comparator<File>() { // from class: com.fimi.kernel.utils.FileUtil.3
            @Override // java.util.Comparator
            public int compare(File file, File newFile) {
                long diff = file.lastModified() - newFile.lastModified();
                if (asc) {
                    if (diff > 0) {
                        return -1;
                    }
                    return diff == 0 ? 0 : 1;
                } else if (diff > 0) {
                    return 1;
                } else {
                    return diff == 0 ? 0 : -1;
                }
            }
        });
    }

    public static void addFileContent(String fileName, byte[] content) {
        RandomAccessFile randomFile = null;
        try {
            RandomAccessFile randomFile2 = new RandomAccessFile(fileName, "rw");
            try {
                randomFile2.seek(randomFile2.length());
                randomFile2.write(content);
                if (randomFile2 != null) {
                    try {
                        randomFile2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e2) {
                randomFile = randomFile2;
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                th = th;
                randomFile = randomFile2;
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
        }
    }

    public static byte[] getFileBytes(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            while (true) {
                int n = fis.read(b);
                if (n != -1) {
                    bos.write(b, 0, n);
                } else {
                    fis.close();
                    bos.close();
                    byte[] buffer = bos.toByteArray();
                    return buffer;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static long getFileLenght(String path) {
        File f1 = new File(path);
        return f1.length();
    }

    public static void meragerFiles(String outFileName, String[] fileNames) {
        FileChannel outChannel = null;
        try {
            try {
                outChannel = new FileOutputStream(outFileName).getChannel();
                for (String fw : fileNames) {
                    FileChannel fc = new FileInputStream(fw).getChannel();
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

    public static void createFile(String pathName) {
        File f = new File(pathName);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mergeMultipleVideoFile(String outPath, String videoPath1, String videoPath2) {
        try {
            List<String> fileList = new ArrayList<>();
            List<Movie> movieList = new ArrayList<>();
            fileList.add(videoPath1);
            fileList.add(videoPath2);
            for (String filePath : fileList) {
                movieList.add(MovieCreator.build(filePath));
            }
            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();
            for (Movie movie : movieList) {
                for (Track track : movie.getTracks()) {
                    if (track.getHandler().equals("soun")) {
                        audioTracks.add(track);
                    }
                    if (track.getHandler().equals("vide")) {
                        videoTracks.add(track);
                    }
                }
            }
            Movie result = new Movie();
            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack((Track[]) audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack((Track[]) videoTracks.toArray(new Track[videoTracks.size()])));
            }
            writeMergeNewFile(outPath, result);
            deleteFile(new File(videoPath1));
            deleteFile(new File(videoPath2));
            fileList.clear();
            movieList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeMergeNewFile(String filePath, Movie result) {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                Container container = new DefaultMp4Builder().build(result);
                FileOutputStream fileOutputStream2 = new FileOutputStream(filePath);
                try {
                    container.writeContainer(fileOutputStream2.getChannel());
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                            fileOutputStream = fileOutputStream2;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
            }
        } catch (Throwable th2) {
        }
    }

    public static void createFileAndPaperFile(String fileName) {
        File file = new File(fileName);
        if (fileName.indexOf(".") != -1) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!file.exists()) {
            file.mkdir();
        }
    }

    public static String fileToString(String filePath) {
        try {
            if (!new File(filePath).exists()) {
                return null;
            }
            StringBuffer buffer = new StringBuffer();
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            while (true) {
                String s = bf.readLine();
                if (s != null) {
                    buffer.append(s.trim());
                } else {
                    bf.close();
                    return buffer.toString();
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String fileToString(InputStream in) {
        try {
            StringBuffer buffer = new StringBuffer();
            InputStreamReader inputReader = new InputStreamReader(in);
            BufferedReader bf = new BufferedReader(inputReader);
            while (true) {
                String s = bf.readLine();
                if (s != null) {
                    buffer.append(s.trim());
                } else {
                    bf.close();
                    in.close();
                    return buffer.toString();
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean deleteFiles(File file, String prexFL, String prexFLSD) {
        try {
            if (isCanUseSD()) {
                if (file == null) {
                    return true;
                }
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File file2 : files) {
                        deleteFile(file2);
                    }
                } else if (file.getName().endsWith(prexFL) || file.getName().endsWith(prexFLSD)) {
                    file.delete();
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readAssertResource(Context context, String strAssertFileName) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream ims = assetManager.open(strAssertFileName);
            String strResponse = getStringFromInputStream(ims);
            return strResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(a_is));
            while (true) {
                try {
                    String line = br2.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                } catch (IOException e) {
                    br = br2;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e2) {
                        }
                    }
                    return sb.toString();
                } catch (Throwable th) {
                    th = th;
                    br = br2;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            }
            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th2) {
        }
        return sb.toString();
    }

    public static List<File> listDirs(File file, final String endwith2, final String endwith1) {
        if (file != null && file.exists() && file.isDirectory()) {
            final Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{3}");
            File[] files = file.listFiles(new FilenameFilter() { // from class: com.fimi.kernel.utils.FileUtil.4
                @Override // java.io.FilenameFilter
                public boolean accept(File arg0, String arg1) {
                    File f = new File(arg0.getAbsolutePath() + "/" + arg1);
                    if (!f.isDirectory() || arg1.equals(endwith1)) {
                        return false;
                    }
                    int len = arg1.length();
                    if (len != 23 && len != 26) {
                        return false;
                    }
                    Matcher m = p.matcher(arg1);
                    if (!m.find()) {
                        return false;
                    }
                    if (len != 23 && !arg1.endsWith(endwith2)) {
                        return false;
                    }
                    return true;
                }
            });
            if (files != null) {
                List<File> list = new ArrayList<>(Arrays.asList(files));
                sortList(list, false);
                return list;
            }
            return null;
        }
        return null;
    }

    public static List<File> listFiles3(File file, final String[] suffix) {
        File[] files;
        if (file == null || !file.exists() || !file.isDirectory() || (files = file.listFiles(new FilenameFilter() { // from class: com.fimi.kernel.utils.FileUtil.5
            @Override // java.io.FilenameFilter
            public boolean accept(File arg0, String arg1) {
                String[] strArr;
                for (String endwith : suffix) {
                    if (arg1.endsWith(endwith)) {
                        return true;
                    }
                }
                return false;
            }
        })) == null) {
            return null;
        }
        List<File> list = new ArrayList<>(Arrays.asList(files));
        sortList(list, false);
        return list;
    }

    public static List<File> listFiles3(File file) {
        File[] files;
        if (file == null || !file.exists() || !file.isDirectory() || (files = file.listFiles()) == null) {
            return null;
        }
        List<File> list = new ArrayList<>(Arrays.asList(files));
        sortList(list, false);
        return list;
    }

    public static void copyDir(String oldPath, String newPath) {
        File file = new File(oldPath);
        String[] filePath = file.list();
        if (!new File(newPath).exists()) {
            new File(newPath).mkdir();
        }
        for (int i = 0; i < filePath.length; i++) {
            if (new File(oldPath + File.separator + filePath[i]).isDirectory()) {
                copyDir(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }
            if (new File(oldPath + File.separator + filePath[i]).isFile()) {
                copyFile(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        File source = new File(oldPath);
        File target = new File(newPath);
        InputStream in = null;
        OutputStream out = null;
        if (!source.exists() || !source.isFile()) {
            throw new IllegalArgumentException("file not exsits!");
        }
        if (target.exists()) {
            return;
        }
        byte[] buffer = new byte[3072];
        try {
            try {
                target.createNewFile();
                InputStream in2 = new BufferedInputStream(new FileInputStream(source));
                try {
                    OutputStream out2 = new BufferedOutputStream(new FileOutputStream(target));
                    while (true) {
                        try {
                            int n = in2.read(buffer);
                            if (n == -1) {
                                break;
                            }
                            out2.write(buffer, 0, n);
                        } catch (FileNotFoundException e) {
                            e = e;
                            out = out2;
                            in = in2;
                            e.printStackTrace();
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            if (out != null) {
                                out.flush();
                                out.close();
                            }
                        } catch (IOException e3) {
                            out = out2;
                            in = in2;
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                            }
                            if (out != null) {
                                out.flush();
                                out.close();
                            }
                        } catch (Throwable th) {
                            th = th;
                            out = out2;
                            in = in2;
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e5) {
                                    e5.printStackTrace();
                                    throw th;
                                }
                            }
                            if (out != null) {
                                out.flush();
                                out.close();
                            }
                            throw th;
                        }
                    }
                    if (in2 != null) {
                        try {
                            in2.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                            out = out2;
                            in = in2;
                        }
                    }
                    if (out2 != null) {
                        out2.flush();
                        out2.close();
                    }
                    out = out2;
                    in = in2;
                } catch (FileNotFoundException e7) {
                    in = in2;
                } catch (IOException e8) {
                    in = in2;
                } catch (Throwable th2) {
                    in = in2;
                }
            } catch (IOException e10) {
            }
        } catch (Throwable th3) {
        }
    }
}
