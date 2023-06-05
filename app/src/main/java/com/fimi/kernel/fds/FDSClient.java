package com.fimi.kernel.fds;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fimi.host.HostConstants;
import com.fimi.kernel.network.okhttp.CommonOkHttpClient;
import com.fimi.kernel.network.okhttp.ProgressBody;
import com.fimi.kernel.network.okhttp.listener.DisposeDataHandle;
import com.fimi.kernel.network.okhttp.listener.DisposeDataListener;
import com.fimi.kernel.network.okhttp.request.CommonRequest;
import com.fimi.kernel.network.okhttp.request.RequestParams;
import com.fimi.network.BaseManager;
import com.fimi.network.entity.NetModel;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;
import com.twitter.sdk.android.core.internal.scribe.EventsFilesManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* loaded from: classes.dex */
public class FDSClient extends BaseManager {
    private Call call;
    private Zip2Fds mZip2Fds;
    private Object object = new Object();
    private Call postFdsUrlCall;
    private Call reqeuestFdsUrlCall;

    public void startUpload(IFdsFileModel model, IFdsUploadListener listener) throws IOException {
        this.mZip2Fds = new Zip2Fds();
        boolean b = this.mZip2Fds.log2Zip(model.getFile(), model.getNeedZipFileBySuffix());
        Log.i("istep", "mZip2Fds=" + b);
        this.mZip2Fds = null;
        if (!b) {
            model.setState(FdsUploadState.FAILED);
            return;
        }
        String zipName = model.getFile().getName() + ".zip";
        model.setZipFile(new File(model.getFile().getAbsolutePath() + "/" + zipName));
        requesetFdsUrl(model.getZipFile().getName(), model);
        if (model.getFileFdsUrl() == null || model.getFileFdsUrl().equals("")) {
            model.setState(FdsUploadState.FAILED);
            return;
        }
        upload2Fds(model, listener);
        if (FdsUploadState.FAILED != model.getState() && FdsUploadState.STOP != model.getState()) {
            if (model.getPlaybackFile() != null) {
                saveFdsUrl2FimiPlayback(model);
            } else {
                saveFdsUrl2Fimi(model.getZipFile().getName(), model);
            }
        }
    }

    public void requesetFdsUrl(String fileName, final IFdsFileModel model) {
        String name;
        RequestParams requestParams = getRequestParams();
        requestParams.put("bucketName", "x8-fclog");
        if (model.getPlaybackFile() != null) {
            name = fileName;
        } else {
            name = fileName.replace(".zip", "-" + UUID.randomUUID() + ".zip");
        }
        requestParams.put("objectName", name);
        String url = HostConstants.QUESET_FDS_URL + "getGeneratePresignedUri";
        this.reqeuestFdsUrlCall = CommonOkHttpClient.get(CommonRequest.createGetRequest(url, getRequestParams(requestParams)), new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.kernel.fds.FDSClient.1
            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onSuccess(Object responseObj) {
                NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                if (netModel.isSuccess()) {
                    String url2 = netModel.getData().toString();
                    model.setFileFdsUrl(url2);
                }
                FDSClient.this.releaseLocked();
            }

            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onFailure(Object reasonObj) {
                FDSClient.this.releaseLocked();
            }
        }));
        lockedModel();
    }

    public void upload2Fds(IFdsFileModel model, IFdsUploadListener listener) throws IOException {
        String url = model.getFileFdsUrl();
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("file", model.getFile().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), new File(model.getZipFile().getAbsolutePath())));
        MultipartBody multipartBody = builder.build();
        Request request = new Request.Builder().header(OAuthConstants.HEADER_AUTHORIZATION, "Client-ID " + UUID.randomUUID()).url(url).put(new ProgressBody(model, multipartBody, listener)).build();
        this.call = client.newCall(request);
        model.setState(FdsUploadState.LOADING);
        listener.onProgress(model, 0L, model.getFile().length());
        Response response = this.call.execute();
        if (!response.isSuccessful()) {
            Log.i("istep", "Unexpected code " + response);
            model.setState(FdsUploadState.FAILED);
            return;
        }
        Log.i("istep", "ResponseBody " + response.toString() + " " + model.getFile().getName());
    }

    public void lockedModel() {
        synchronized (this.object) {
            try {
                this.object.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void releaseLocked() {
        synchronized (this.object) {
            try {
                this.object.notify();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("istep", "Exception   " + e.toString());
            }
        }
    }

    public void saveFdsUrl2Fimi(String fileName, final IFdsFileModel model) {
        RequestParams requestParams = getRequestParams();
        requestParams.put("droneId", "x8");
        requestParams.put("logOwnerId", HostConstants.getUserDetail().getFimiId());
        requestParams.put("fileFdsUrl", model.getFileFdsUrl());
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
            Date d = formatter.parse(name);
            SimpleDateFormat format = new SimpleDateFormat(HostConstants.FORMATDATE);
            name = format.format(d);
        } catch (Exception e) {
        }
        requestParams.put("createFileTime", name);
        String url = HostConstants.SAVE_FDS_URL_2_FIMI_URL + "uploadFlyLog";
        this.postFdsUrlCall = CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.kernel.fds.FDSClient.2
            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onSuccess(Object responseObj) {
                NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                Log.i("istep", "createFileTime " + netModel.toString());
                if (netModel.isSuccess()) {
                    model.setState(FdsUploadState.SUCCESS);
                    FDSClient.this.rename(model);
                } else {
                    model.setState(FdsUploadState.FAILED);
                }
                FDSClient.this.releaseLocked();
            }

            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onFailure(Object reasonObj) {
                Log.i("istep", "onFailure " + reasonObj.toString());
                model.setState(FdsUploadState.FAILED);
                FDSClient.this.releaseLocked();
            }
        }));
        lockedModel();
    }

    public void saveFdsUrl2FimiPlayback(final IFdsFileModel model) {
        String fileName;
        RequestParams requestParams = getRequestParams();
        requestParams.put("fimiId", HostConstants.getUserDetail().getFimiId());
        requestParams.put("flyDuration", model.getFlightDuration() + "");
        requestParams.put("flyDistance", model.getFlightMileage());
        requestParams.put("logFlieSize", model.getPlaybackFile().length() + "");
        requestParams.put("logFileUrl", model.getFileFdsUrl());
        String fileName2 = model.getPlaybackFile().getName();
        if (fileName2.indexOf(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR) != -1) {
            fileName = fileName2.substring(0, fileName2.lastIndexOf(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR));
        } else {
            fileName = fileName2.substring(0, fileName2.lastIndexOf("."));
        }
        Log.i("istep", "flightTime" + fileName);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Date d = formatter.parse(fileName);
            SimpleDateFormat format = new SimpleDateFormat(HostConstants.FORMATDATE);
            fileName = format.format(d);
        } catch (Exception e) {
        }
        requestParams.put("flightTime", fileName);
        String url = HostConstants.SAVE_FDS_URL_2_FIMI_URL_PLAYBACK + "record/upload_flyrecord";
        this.postFdsUrlCall = CommonOkHttpClient.post(CommonRequest.createPostRequest(url, getRequestParams(requestParams)), new DisposeDataHandle(new DisposeDataListener() { // from class: com.fimi.kernel.fds.FDSClient.3
            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onSuccess(Object responseObj) {
                NetModel netModel = (NetModel) JSON.parseObject(responseObj.toString(), NetModel.class);
                Log.i("istep", "createFileTime " + netModel.toString());
                if (netModel.isSuccess()) {
                    model.setState(FdsUploadState.SUCCESS);
                    FDSClient.this.rename(model);
                } else {
                    model.setState(FdsUploadState.FAILED);
                }
                FDSClient.this.releaseLocked();
            }

            @Override // com.fimi.kernel.network.okhttp.listener.DisposeDataListener
            public void onFailure(Object reasonObj) {
                Log.i("istep", "onFailure " + reasonObj.toString());
                model.setState(FdsUploadState.FAILED);
                FDSClient.this.releaseLocked();
            }
        }));
        lockedModel();
    }

    public void stopUpload(IFdsFileModel model, IFdsUploadListener listener) {
        if (this.mZip2Fds != null) {
            this.mZip2Fds.stop();
            model.setState(FdsUploadState.STOP);
            this.mZip2Fds = null;
        }
        if (this.reqeuestFdsUrlCall != null) {
            this.reqeuestFdsUrlCall.cancel();
            model.setState(FdsUploadState.STOP);
            this.reqeuestFdsUrlCall = null;
        }
        if (this.call != null) {
            this.call.cancel();
            model.setState(FdsUploadState.STOP);
            listener.onStop(model);
            this.call = null;
        }
        if (this.postFdsUrlCall != null) {
            this.postFdsUrlCall.cancel();
            model.setState(FdsUploadState.STOP);
            this.postFdsUrlCall = null;
        }
    }

    public boolean rename(IFdsFileModel model) {
        String path;
        if (model.getPlaybackFile() != null) {
            File filePlayback = model.getPlaybackFile();
            String path2 = filePlayback.getAbsolutePath();
            int index = path2.indexOf("_collect");
            if (index != -1) {
                StringBuffer stringBuffer = new StringBuffer(path2);
                stringBuffer.insert(index, model.getFileSuffix());
                path = stringBuffer.toString();
            } else {
                int indexTwo = path2.indexOf(".");
                StringBuffer stringBuffer2 = new StringBuffer(path2);
                stringBuffer2.insert(indexTwo, model.getFileSuffix());
                path = stringBuffer2.toString();
            }
            File tmpFile = new File(path);
            boolean b = filePlayback.renameTo(tmpFile);
            model.resetPlaybackFile(filePlayback);
            return b;
        }
        File file = model.getFile();
        File tmpFile2 = new File(file.getAbsolutePath() + model.getFileSuffix());
        boolean b2 = file.renameTo(tmpFile2);
        model.resetFile(file);
        return b2;
    }
}
