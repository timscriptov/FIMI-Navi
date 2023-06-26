package com.fimi.album.x9;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.fimi.album.download.FileLoader;
import com.fimi.album.download.entity.FileInfo;
import com.fimi.album.download.interfaces.IMediaFileLoad;
import com.fimi.album.download.interfaces.OnDownloadListener;
import com.fimi.album.entity.MediaFileInfo;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.x9.interfaces.OnX9MediaFileListener;
import com.fimi.kernel.Constants;
import com.fimi.kernel.utils.DirectoryPath;
import com.fimi.kernel.utils.Dom4jManager;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class X9MediaFileLoad<T extends MediaModel> implements IMediaFileLoad {
    private final List<MediaModel> listData;
    private final OnX9MediaFileListener listener;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X9MediaFileLoad.this.listener.onComplete(false);
                    return;
                case 1:
                    X9MediaFileLoad.this.listener.onComplete(true);
                    return;
                default:
            }
        }
    };
    private final FileLoader mFileLoader = new FileLoader();
    private final Dom4jManager mDom4jManager = new Dom4jManager();
    private final FileInfo info = new FileInfo();
    String urlThumPrex = "http://192.168.40.1:8000/FIMI_PHOTO/.thumbnails/";
    String urlPrex = "http://192.168.40.1:8000/FIMI_PHOTO/";
    String thumPath = Environment.getExternalStorageDirectory().getPath() + "/MiDroneMini/cache";
    String imagePath = DirectoryPath.getX9LocalMedia();
    String xmlUrl = "http://192.168.40.1:8000/FIMI_PHOTO/MediaFileList.xml";
    String xmlPath = Environment.getExternalStorageDirectory().getPath() + "/MiDroneMini";

    public X9MediaFileLoad(OnX9MediaFileListener listener, List<MediaModel> listData) {
        this.listener = listener;
        this.listData = listData;
    }

    public static long stringToLong(String strTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = sdf.parse(strTime);
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }

    @Override
    public void startLoad() {
        this.info.setPath(this.xmlPath);
        this.info.setUrl(this.xmlUrl);
        this.info.setFileName("mini.mfl");
        this.mFileLoader.queueDownload(this.info, new OnDownloadListener() {
            @Override
            public void onProgress(Object responseObj, long progrss, long currentLength) {
            }

            @Override
            public void onSuccess(Object responseObj) {
                X9MediaFileLoad.this.parseXml(X9MediaFileLoad.this.info);
                X9MediaFileLoad.this.mHandler.obtainMessage(1).sendToTarget();
            }

            @Override
            public void onFailure(Object reasonObj) {
                X9MediaFileLoad.this.mHandler.obtainMessage(0).sendToTarget();
            }

            @Override
            public void onStop(MediaModel reasonObj) {
            }
        });
    }

    public void parseXml(FileInfo info) {
        List<MediaFileInfo> list = this.mDom4jManager.readXML(info.getPath() + "/" + info.getFileName(), "file", MediaFileInfo.class);
        File fileDir = new File(this.imagePath);
        File thumDir = new File(this.thumPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        if (!thumDir.exists()) {
            thumDir.mkdirs();
        }
        for (int i = 0; i < list.size(); i++) {
            MediaFileInfo mf = list.get(i);
            MediaModel m = new MediaModel();
            m.setName(mf.getName());
            m.setFileSize(Long.parseLong(mf.getSize()));
            m.setMd5(mf.getMd5());
            m.setLocalFileDir(this.imagePath);
            m.setLocalThumFileDir(this.thumPath);
            if (mf.getAttr().equals("pic")) {
                String url = this.urlPrex + mf.getName();
                String thum = this.urlThumPrex + mf.getName();
                m.setFileUrl(url);
                m.setThumFileUrl(thum);
                m.setThumName(mf.getName());
                m.setVideo(false);
            } else if (mf.getAttr().equals("video")) {
                m.setVideo(true);
                String url2 = this.urlPrex + mf.getName();
                String rename = mf.getName().replace(Constants.VIDEO_FILE_SUFFIX, ".jpg");
                String thum2 = this.urlThumPrex + rename;
                m.setThumName(rename);
                m.setFileUrl(url2);
                m.setThumFileUrl(thum2);
                String time = mf.getTime();
                String[] durations = mf.getTime().split(":");
                if (durations.length == 3 && durations[0].equals("00")) {
                    time = time.substring(3);
                }
                m.setVideoDuration(time);
                m.setVideo(true);
            }
            m.setFileLocalPath(this.imagePath + "/" + m.getName());
            m.setThumLocalFilePath(this.thumPath + "/" + m.getThumName());
            if (mf.getGentime() != null) {
                try {
                    String date = changeDateFormat(mf.getGentime());
                    m.setFormatDate(date);
                    m.setCreateDate(stringToLong(mf.getGentime()));
                } catch (ParseException e) {
                    m.setFormatDate(mf.getGentime());
                    m.setCreateDate(0L);
                    e.printStackTrace();
                }
                m.setThumSize(Long.parseLong(mf.getThumbsize()));
                m.setDownLoadOriginalFile(isExits(m.getLocalFileDir(), m.getName()));
                m.setDownLoadThum(isExits(m.getLocalThumFileDir(), m.getThumName()));
                this.listData.add(m);
            }
        }
    }

    public String changeDateFormat(String time) throws ParseException {
        DateFormat df1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date1 = df1.parse(time);
        DateFormat df12 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String strTime = df12.format(date1);
        return strTime;
    }

    @Override
    public void stopLoad() {
        this.info.setStop(true);
    }

    public List<MediaModel> getConfigList() {
        return this.listData;
    }

    public boolean isExits(String path, String name) {
        File file = new File(path, name);
        return file.exists();
    }
}
