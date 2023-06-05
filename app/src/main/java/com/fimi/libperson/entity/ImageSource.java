package com.fimi.libperson.entity;

import android.graphics.Bitmap;
import android.net.Uri;

import com.fimi.kernel.connect.tcp.SocketOption;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/* loaded from: classes.dex */
public class ImageSource {
    private final Bitmap bitmap;
    private final Integer resource;
    private final boolean tile;
    private final Uri uri;

    private ImageSource(Uri uri) {
        String uriString = uri.toString();
        if (uriString.startsWith("file:///")) {
            File uriFile = new File(uriString.substring("file:///".length() - 1));
            if (!uriFile.exists()) {
                try {
                    uri = Uri.parse(URLDecoder.decode(uriString, SocketOption.DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        this.bitmap = null;
        this.uri = uri;
        this.resource = null;
        this.tile = true;
    }

    public static ImageSource asset(String assetName) {
        if (assetName == null) {
            throw new NullPointerException("Asset name must not be null");
        }
        return uri("file:///android_asset/" + assetName);
    }

    public static ImageSource uri(String uri) {
        if (uri == null) {
            throw new NullPointerException("Uri must not be null");
        }
        if (!uri.contains("://")) {
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
            }
            uri = "file:///" + uri;
        }
        return new ImageSource(Uri.parse(uri));
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Uri getUri() {
        return this.uri;
    }

    public Integer getResource() {
        return this.resource;
    }

    public boolean isTile() {
        return this.tile;
    }
}
