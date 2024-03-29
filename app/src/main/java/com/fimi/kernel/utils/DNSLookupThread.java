package com.fimi.kernel.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class DNSLookupThread extends Thread {
    private final String hostname;
    private InetAddress addr;

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
        return dnsTh.getIP() != null;
    }

    @Override
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
