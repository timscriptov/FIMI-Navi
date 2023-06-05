package com.fimi.kernel.network.okhttp;

import com.fimi.kernel.fds.IFdsFileModel;
import com.fimi.kernel.fds.IFdsUploadListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/* loaded from: classes.dex */
public class ProgressBody extends RequestBody {
    private final int SEGMENT_SIZE = 4096;
    protected IFdsUploadListener listener;
    protected IFdsFileModel model;
    protected MultipartBody multipartBody;
    private BufferedSink bufferedSink;

    public ProgressBody(IFdsFileModel model, MultipartBody multipartBody, IFdsUploadListener listener) {
        this.model = model;
        this.multipartBody = multipartBody;
        this.listener = listener;
    }

    protected ProgressBody() {
    }

    @Override // okhttp3.RequestBody
    public long contentLength() throws IOException {
        return this.multipartBody.contentLength();
    }

    @Override // okhttp3.RequestBody
    public MediaType contentType() {
        return this.multipartBody.contentType();
    }

    @Override // okhttp3.RequestBody
    public void writeTo(BufferedSink sink) throws IOException {
        if (this.bufferedSink == null) {
            this.bufferedSink = Okio.buffer(sink(sink));
        }
        this.multipartBody.writeTo(this.bufferedSink);
        this.bufferedSink.flush();
    }

    private Sink sink(BufferedSink sink) {
        return new ForwardingSink(sink) { // from class: com.fimi.kernel.network.okhttp.ProgressBody.1
            long bytesWritten = 0;
            long contentLength = 0;

            @Override // okio.ForwardingSink, okio.Sink
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (this.contentLength == 0) {
                    this.contentLength = ProgressBody.this.contentLength();
                }
                this.bytesWritten += byteCount;
                ProgressBody.this.listener.onProgress(ProgressBody.this.model, this.bytesWritten, this.contentLength);
            }
        };
    }
}
