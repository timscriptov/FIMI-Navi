package com.fimi.app.x8s.ui.album.x8s;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.fimi.album.biz.SuffixUtils;
import com.fimi.album.download.interfaces.IMediaFileLoad;
import com.fimi.album.entity.MediaModel;
import com.fimi.album.handler.HandlerManager;
import com.fimi.app.x8s.ui.album.x8s.listener.DownMediaFileLinstener;
import com.fimi.kernel.connect.session.MediaDataListener;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.x8sdk.command.X8DownLoadCmd;
import com.fimi.x8sdk.command.X8MediaCmd;
import com.fimi.x8sdk.dataparser.MediaFileDownLoadPacket;
import com.fimi.x8sdk.dataparser.X8MediaFileInfo;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class X8MediaFileLoad<T extends MediaModel> implements IMediaFileLoad, MediaDataListener {
    public SuffixUtils mSuffixUtils = SuffixUtils.obtain();
    X8MediaFileInfo downingFileInfo;
    RandomAccessFile randomAccessFile;
    List<VideoThumInfo> thumInfos = new ArrayList();
    long fileLength = 0;
    boolean isAwait = false;
    X8FileInfo info = new X8FileInfo();
    private List<MediaModel> listData;
    private OnX8MediaFileListener listener;
    private String X8_MEDIA_DES = "media.xml";
    private String rootPath = Environment.getExternalStorageDirectory().getPath() + "/x8/media";
    public final String xmlPath = this.rootPath + "/" + this.X8_MEDIA_DES;
    File rootFile = new File(this.xmlPath);
    public final String thumPath = this.rootPath + "/thum";
    public final String orginPath = this.rootPath + "/orgin";
    public final String tempPath = this.rootPath + "/temp";
    private short max_size = NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE;
    private boolean isErr = false;
    private Handler mHandler = new Handler() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaFileLoad.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    X8MediaFileLoad.this.listener.onComplete(false);
                    return;
                case 1:
                    X8MediaFileLoad.this.listener.onComplete(true);
                    return;
                default:
                    return;
            }
        }
    };
    DownMediaFileLinstener fileLinstener = new DownMediaFileLinstener() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaFileLoad.1
        @Override // com.fimi.app.x8s.ui.album.x8s.listener.DownMediaFileLinstener
        public void onSartFile() {
            X8MediaFileLoad.this.sendCmd(new X8DownLoadCmd().getMediaXmlFile(X8MediaFileLoad.this.X8_MEDIA_DES));
        }

        @Override // com.fimi.app.x8s.ui.album.x8s.listener.DownMediaFileLinstener
        public void onProgress(MediaFileDownLoadPacket downLoadPacket) {
            if (X8MediaFileLoad.this.randomAccessFile != null) {
                try {
                    X8MediaFileLoad.this.fileLength = X8MediaFileLoad.this.randomAccessFile.length();
                    if (X8MediaFileLoad.this.fileLength == downLoadPacket.getOffSet()) {
                        X8MediaFileLoad.this.randomAccessFile.seek(X8MediaFileLoad.this.fileLength);
                        X8MediaFileLoad.this.randomAccessFile.write(downLoadPacket.getPlayData());
                        if (downLoadPacket.isFinished()) {
                            onEndFile(DownFileResultEnum.Success);
                        }
                    } else if (!X8MediaFileLoad.this.isAwait) {
                        X8MediaFileLoad.this.isAwait = true;
                        HandlerManager.obtain().getHandlerInMainThread().postDelayed(new Runnable() { // from class: com.fimi.app.x8s.ui.album.x8s.X8MediaFileLoad.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                X8MediaFileLoad.this.reqNextPacket(X8MediaFileLoad.this.fileLength);
                                X8MediaFileLoad.this.isAwait = false;
                            }
                        }, 3000L);
                    }
                } catch (IOException e) {
                    onEndFile(DownFileResultEnum.Fail);
                    e.printStackTrace();
                }
            }
        }

        @Override // com.fimi.app.x8s.ui.album.x8s.listener.DownMediaFileLinstener
        public void onDownFilePre(X8MediaFileInfo fileInfo) {
            X8MediaFileLoad.this.createRootFile();
            X8MediaFileLoad.this.startDownloadTask(fileInfo);
        }

        @Override // com.fimi.app.x8s.ui.album.x8s.listener.DownMediaFileLinstener
        public void onEndFile(DownFileResultEnum resultEnum) {
            switch (AnonymousClass3.$SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[resultEnum.ordinal()]) {
                case 1:
                    X8MediaFileLoad.this.mHandler.sendEmptyMessageDelayed(0, 500L);
                    return;
                case 2:
                default:
                    return;
                case 3:
                    X8MediaFileLoad.this.closeWriteStream();
                    X8MediaFileLoad.this.parseOnlineData();
                    NoticeManager.getInstance().removeMediaListener(X8MediaFileLoad.this);
                    X8MediaFileLoad.this.mHandler.sendEmptyMessageDelayed(1, 500L);
                    return;
            }
        }
    };

    public X8MediaFileLoad(OnX8MediaFileListener listener, List<MediaModel> listData) {
        this.listener = listener;
        this.listData = listData;
        NoticeManager.getInstance().addMediaListener(this);
    }

    @Override // com.fimi.album.download.interfaces.IMediaFileLoad
    public void startLoad() {
        this.fileLinstener.onSartFile();
    }

    public void sendCmd(X8MediaCmd cmd) {
        if (cmd != null) {
            SessionManager.getInstance().sendCmd(cmd);
        }
    }

    @Override // com.fimi.album.download.interfaces.IMediaFileLoad
    public void stopLoad() {
        this.info.setStop(true);
    }

    @Override // com.fimi.kernel.connect.session.MediaDataListener
    public void mediaDataCallBack(byte[] data) {
        if (data != null && data.length > 0) {
            byte cmdType = data[0];
            if (cmdType == 0) {
                X8MediaFileInfo x8MediaFileInfo = new X8MediaFileInfo();
                x8MediaFileInfo.unPacket(data);
                x8MediaFileInfo.setMediaFileType(X8MediaFileInfo.MediaFileType.RootFile);
                this.fileLinstener.onDownFilePre(x8MediaFileInfo);
            } else if (cmdType == 1) {
                MediaFileDownLoadPacket downLoadPacket = new MediaFileDownLoadPacket();
                downLoadPacket.unPacket(data);
                if (this.X8_MEDIA_DES.equals(downLoadPacket.getFileName())) {
                    this.fileLinstener.onProgress(downLoadPacket);
                }
            }
        }
    }

    public void createRootFile() {
        File rootDir = new File(this.rootPath);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        if (this.rootFile.exists()) {
            this.rootFile.delete();
        }
        try {
            this.rootFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.randomAccessFile = new RandomAccessFile(this.rootFile, "rwd");
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    public void startDownloadTask(X8MediaFileInfo x8MediaFileInfo) {
        this.downingFileInfo = x8MediaFileInfo;
        sendCmd(new X8DownLoadCmd().downMediaFile(0, NTLMConstants.TARGET_INFORMATION_SUBBLOCK_DNS_DOMAIN_NAME_TYPE, x8MediaFileInfo.getFileName(), false));
    }

    public void reqNextPacket(long offset) {
        if (this.downingFileInfo.getFileSize() - offset > 0) {
            this.max_size = ((long) this.downingFileInfo.getFileSize()) - offset >= ((long) this.max_size) ? this.max_size : (short) (this.downingFileInfo.getFileSize() - offset);
            sendCmd(new X8DownLoadCmd().downMediaFile((int) offset, this.max_size, this.X8_MEDIA_DES, false));
        }
    }

    public void parseOnlineData() {
        File thumDir = new File(this.thumPath);
        if (!thumDir.exists()) {
            thumDir.mkdir();
        }
        File orignDir = new File(this.orginPath);
        if (!orignDir.exists()) {
            orignDir.mkdir();
        }
        File tempDir = new File(this.tempPath);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        this.thumInfos.clear();
        File xmlFile = new File(this.xmlPath);
        if (xmlFile.exists()) {
            BufferedReader bufr = null;
            try {
                try {
                    this.listData.clear();
                    BufferedReader bufr2 = new BufferedReader(new FileReader(xmlFile));
                    while (true) {
                        try {
                            String line = bufr2.readLine();
                            if (line == null) {
                                break;
                            }
                            String[] infoArray = line.split("\\|");
                            if (infoArray != null && infoArray.length == 4) {
                                MediaModel model = new MediaModel();
                                String fileSize = infoArray[0];
                                model.setFileSize(Long.parseLong(fileSize));
                                String dataString = infoArray[1];
                                String fileName = infoArray[2];
                                model.setName(fileName);
                                String url = infoArray[3];
                                model.setFileUrl(url);
                                SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MMM-dd_HH:mm:ss", Locale.US);
                                Date date = dtf.parse(dataString);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                                String formatDate = format.format(date);
                                model.setFormatDate(formatDate);
                                model.setCreateDate(date.getTime());
                                model.setMd5(date.getTime() + fileName);
                                model.setLocalFileDir(this.orginPath);
                                if (this.mSuffixUtils.judgeVideo(fileName)) {
                                    model.setVideo(true);
                                    String fileURL = url + fileName;
                                    String thumName = fileName.replace(this.mSuffixUtils.fileFormatThm, this.mSuffixUtils.fileFormatJpg);
                                    String thumURL = url + thumName;
                                    model.setFileUrl(fileURL);
                                    model.setThumFileUrl(url + fileName.replace(this.mSuffixUtils.fileFormatThm, this.mSuffixUtils.fileFormatRlv));
                                    model.setThumName(thumURL);
                                    model.setDownLoadOriginalPath(fileURL);
                                    model.setThumLocalFilePath(this.thumPath + "/" + thumName);
                                    model.setFileLocalPath(this.orginPath + "/" + fileName);
                                    model.setDownLoadOriginalFile(isExits(this.orginPath, fileName));
                                    model.setDownLoadThum(isExits(this.thumPath, fileName.replace(this.mSuffixUtils.fileFormatThm, this.mSuffixUtils.fileFormatJpg), Long.parseLong(fileSize)));
                                } else if (this.mSuffixUtils.judgePhotho(fileName)) {
                                    model.setVideo(false);
                                    String fileURL2 = url + fileName;
                                    String thumURL2 = url + fileName;
                                    model.setFileUrl(fileURL2);
                                    model.setThumFileUrl(url + fileName.replace(this.mSuffixUtils.fileFormatJpg, this.mSuffixUtils.fileFormatRlv));
                                    model.setThumName(thumURL2);
                                    model.setDownLoadOriginalPath(fileURL2);
                                    model.setThumLocalFilePath(this.thumPath + "/" + fileName);
                                    model.setFileLocalPath(this.orginPath + "/" + fileName);
                                    model.setDownLoadOriginalFile(isExits(this.orginPath, fileName));
                                    model.setDownLoadThum(isExits(this.thumPath, fileName, Long.parseLong(fileSize)));
                                }
                                if (this.listData.contains(model)) {
                                    this.listData.remove(model);
                                }
                                this.listData.add(model);
                            }
                        } catch (Exception e) {
                            e = e;
                            bufr = bufr2;
                            e.printStackTrace();
                            if (bufr != null) {
                                try {
                                    bufr.close();
                                    return;
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                    return;
                                }
                            }
                            return;
                        } catch (Throwable th) {
                            th = th;
                            bufr = bufr2;
                            if (bufr != null) {
                                try {
                                    bufr.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                    if (bufr2 != null) {
                        try {
                            bufr2.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                }
            } catch (Exception e5) {
            }
        }
    }

    public boolean isExits(String path, String name) {
        File file = new File(path, name);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    public boolean isExits(String path, String name, long fileSize) {
        File file = new File(path, name);
        if (!file.exists() || file.length() <= 0) {
            return false;
        }
        return true;
    }

    public void closeWriteStream() {
        if (this.randomAccessFile != null) {
            try {
                this.randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.fimi.app.x8s.ui.album.x8s.X8MediaFileLoad$3 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum = new int[DownFileResultEnum.values().length];

        static {
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Fail.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Stop.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$fimi$app$x8s$ui$album$x8s$DownFileResultEnum[DownFileResultEnum.Success.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }
}
