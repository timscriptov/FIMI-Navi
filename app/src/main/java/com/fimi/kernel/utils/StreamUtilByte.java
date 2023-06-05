package com.fimi.kernel.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class StreamUtilByte {
    public static byte[] getbyte(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4048];
            while (true) {

                int length = in.read(buffer);
                if (length == -1) {
                    break;
                }
                out.write(buffer, 0, length);

            }
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
