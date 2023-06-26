package com.fimi.apk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.fimi.android.app.R;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDownloadListener;
import com.fimi.network.DownloadManager;

import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

import java.io.File;


public class DownloadApkService extends Service {
    public static final String APK_FILE_NAME = "temp_update.apk";
    private static final int NOTIFACTION_ID = 8888;
    private static boolean isDownApking = false;
    DownloadManager downloadManager = new DownloadManager();
    private Notification notification;
    private NotificationManager notificationManager;
    private final Handler handler = new Handler() {
        private int nPos = 0;

        public void resetPos() {
            this.nPos = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int progrss = msg.arg1;
                    if (this.nPos != progrss) {
                        DownloadApkService.this.notification.contentView.setProgressBar(R.id.download_bar, 100, progrss, false);
                        DownloadApkService.this.notification.contentView.setTextViewText(R.id.text_download_process, progrss + "%");
                        this.nPos = progrss;
                        DownloadApkService.this.showNotification();
                        return;
                    }
                    return;
                case 1:
                    resetPos();
                    return;
                default:
            }
        }
    };
    private String path;
    public DisposeDataHandle dataHandle = new DisposeDataHandle(new DisposeDownloadListener() {
        @Override
        public void onProgress(int progrss, int currentLength) {
            Message message = new Message();
            message.what = 0;
            message.arg1 = progrss;
            message.arg2 = currentLength;
            DownloadApkService.this.handler.sendMessage(message);
            Log.i("zdy", "" + progrss + " +++++++++++++  " + currentLength);
        }

        @Override
        public void onSuccess(Object responseObj) {
            Uri data;
            DownloadApkService.this.handler.sendEmptyMessage(1);
            Toast.makeText(DownloadApkService.this, R.string.fimi_sdk_apk_down_success, 0).show();
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setFlags(NTLMConstants.FLAG_UNIDENTIFIED_11);
            if (Build.VERSION.SDK_INT >= 24) {
                data = FileProvider.getUriForFile(DownloadApkService.this, DownloadApkService.this.getPackageName() + ".fileprovider", new File(DownloadApkService.this.path));
                intent.addFlags(1);
            } else {
                data = Uri.fromFile(new File(DownloadApkService.this.path));
            }
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            DownloadApkService.this.startActivity(intent);
            DownloadApkService.this.notificationManager.cancel(DownloadApkService.NOTIFACTION_ID);
            boolean unused = DownloadApkService.isDownApking = false;
            DownloadApkService.this.stopSelf();
        }

        @Override
        public void onFailure(Object reasonObj) {
            DownloadApkService.this.handler.sendEmptyMessage(1);
            DownloadApkService.this.notificationManager.cancel(DownloadApkService.NOTIFACTION_ID);
            boolean unused = DownloadApkService.isDownApking = false;
            DownloadApkService.this.stopSelf();
        }
    });

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("down_url");
        if (intent != null && url != null && !isDownApking) {
            this.path = intent.getStringExtra("down_savepath") + "/" + APK_FILE_NAME;
            this.downloadManager.downloadApk(url, this.path, this.dataHandle);
            showNotification();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager) getSystemService("notification");
        Intent intent = new Intent();
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent, 0);
        this.notification = new Notification();
        this.notification.icon = R.drawable.ic_launcher;
        this.notification.tickerText = getString(R.string.fimi_sdk_apk_downing) + getString(R.string.app_product_name);
        this.notification.contentIntent = pendIntent;
        this.notification.contentView = new RemoteViews(getPackageName(), R.layout.fimisdk_apk_download_notification);
        this.notification.contentView.setProgressBar(R.id.download_bar, 100, 0, false);
        this.notification.contentView.setTextViewText(R.id.text_download_title, getString(R.string.fimi_sdk_apk_downing) + " " + getString(R.string.app_product_name));
        this.notification.contentView.setTextViewText(R.id.text_download_process, "0%");
    }

    public void showNotification() {
        this.notificationManager.notify(NOTIFACTION_ID, this.notification);
    }
}
