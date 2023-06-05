package com.fimi.kernel.network.okhttp.listener;


public class DisposeDataHandle {
    public static boolean isStop = false;
    public boolean isArray;
    public Class<?> mClass;
    public DisposeDataListener mListener;
    public String mSource;

    public DisposeDataHandle(DisposeDataListener listener) {
        this.mListener = null;
        this.mClass = null;
        this.mSource = null;
        this.isArray = false;
        this.mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz) {
        this.mListener = null;
        this.mClass = null;
        this.mSource = null;
        this.isArray = false;
        this.mListener = listener;
        this.mClass = clazz;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz, boolean isArray) {
        this.mListener = null;
        this.mClass = null;
        this.mSource = null;
        this.isArray = false;
        this.mListener = listener;
        this.mClass = clazz;
        this.isArray = isArray;
    }

    public DisposeDataHandle(DisposeDataListener listener, String source) {
        this.mListener = null;
        this.mClass = null;
        this.mSource = null;
        this.isArray = false;
        this.mListener = listener;
        this.mSource = source;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}
