package com.fimi.host;

public class HostLogBack {
    private static final HostLogBack hostLogBack = new HostLogBack();

    public static HostLogBack getInstance() {
        return hostLogBack;
    }

    public void writeLog(String logStr) {
        System.out.println("App ==>  " + logStr);
    }

    public void writeRelayLog(String logStr) {
        System.out.println("Relay==>" + logStr);
    }
}
