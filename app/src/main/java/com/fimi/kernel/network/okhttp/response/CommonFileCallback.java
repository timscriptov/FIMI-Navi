package com.fimi.kernel.network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.fimi.kernel.network.okhttp.exception.OkHttpException;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDownloadListener;
import com.fimi.kernel.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class CommonFileCallback implements Callback {
    private static final int PROGRESS_MESSAGE = 1;
    protected final int NETWORK_ERROR = -1;
    protected final int IO_ERROR = -2;
    protected final String EMPTY_MSG = "";
    private final String mFilePath;
    private final DisposeDownloadListener mListener;
    private final Handler mDeliveryHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CommonFileCallback.this.mListener.onProgress(msg.arg1, msg.arg2);
                return;
            }
        }
    };
    DisposeDataHandle dataHandle;
    private int mProgress;

    public CommonFileCallback(DisposeDataHandle handle) {
        this.mListener = (DisposeDownloadListener) handle.mListener;
        this.mFilePath = handle.mSource;
        this.dataHandle = handle;
    }

    @Override
    public void onFailure(Call call, final IOException ioexception) {
        this.mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                CommonFileCallback.this.mListener.onFailure(new OkHttpException(-1, ioexception));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final File file = handleResponse(response);
        this.mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (file != null) {
                    CommonFileCallback.this.mListener.onSuccess(file);
                } else {
                    CommonFileCallback.this.mListener.onFailure(new OkHttpException(-2, ""));
                }
            }
        });
    }

    private File handleResponse(Response response) {
        File file = null;
        if (response == null) {
            return null;
        }
        InputStream inputStream = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[20480];
        int currentLength = 0;
        try {
            try {
                checkLocalFilePath(this.mFilePath);
                File file2 = new File(this.mFilePath);
                try {
                    FileOutputStream fos2 = new FileOutputStream(file2);
                    try {
                        inputStream = response.body().byteStream();
                        double sumLength = response.body().contentLength();
                        LogUtil.d("moweiru", "dataHandle.isStop()" + this.dataHandle.isStop());
                        do {
                            int length = inputStream.read(buffer);
                            if (length == -1) {
                                break;
                            }
                            fos2.write(buffer, 0, length);
                            currentLength += length;
                            this.mProgress = (int) ((currentLength / sumLength) * 100.0d);
                            Message message = new Message();
                            message.what = 1;
                            message.arg1 = this.mProgress;
                            message.arg2 = currentLength;
                            this.mDeliveryHandler.sendMessage(message);
                        } while (!this.dataHandle.isStop());
                        fos2.flush();
                        if (fos2 != null) {
                            try {
                                fos2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                fos = fos2;
                                file = file2;
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        fos = fos2;
                        file = file2;
                    } catch (Exception e2) {
                        fos = fos2;
                        LogUtil.d("moweiru", "file exception:" + e2.getMessage());
                        file = null;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        return file;
                    } catch (Throwable th) {
                        th = th;
                        fos = fos2;
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                                throw th;
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable e5) {
                }
            } catch (Exception e6) {
            }
        } catch (Throwable th3) {
        }
        return file;
    }

    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0, localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
