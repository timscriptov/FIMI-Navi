package com.fimi.kernel.network.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class RequestParams {
    public ConcurrentHashMap<String, Object> fileParams;
    public ConcurrentHashMap<String, String> urlParams;

    public RequestParams() {
        this(null);
    }

    public RequestParams(Map<String, String> source) {
        this.urlParams = new ConcurrentHashMap<>();
        this.fileParams = new ConcurrentHashMap<>();
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() { // from class: com.fimi.kernel.network.okhttp.request.RequestParams.1
            {
                put(key, value);
            }
        });
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            this.urlParams.put(key, value);
        }
    }

    public void put(String key, Object object) throws FileNotFoundException {
        if (key != null) {
            this.fileParams.put(key, object);
        }
    }

    public boolean hasParams() {
        return this.urlParams.size() > 0 || this.fileParams.size() > 0;
    }
}
