package com.fimi.kernel.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class DNSLookupThread extends Thread {
    private InetAddress addr;
    private String hostname;

    public DNSLookupThread(String hostname) {
        this.hostname = hostname;
    }

    public static boolean isDSNSuceess() {
        DNSLookupThread dnsTh = new DNSLookupThread("www.baidu.com");
        dnsTh.start();
        try {
            dnsTh.join(500L);
        } catch (Exception e) {
        }
        if (dnsTh.getIP() == null) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            InetAddress add = InetAddress.getByName(this.hostname);
            set(add);
        } catch (UnknownHostException e) {
        }
    }

    private synchronized void set(InetAddress addr) {
        this.addr = addr;
    }

    public synchronized String getIP() {
        return this.addr != null ? this.addr.getHostAddress() : null;
    }
}
