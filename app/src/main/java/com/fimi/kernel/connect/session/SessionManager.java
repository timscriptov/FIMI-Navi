package com.fimi.kernel.connect.session;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.interfaces.IConnectResultListener;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


public class SessionManager {
    private static final SessionManager mSessionManager = new SessionManager();
    private final int CONNECT_NETWORK = 0;
    private final int DISCONNECT_NETWORK = 1;
    private final int DEVICE_CONNECT = 3;
    private final int DEVICE_DISCONNECT = 4;
    private final int DEVICE_CONNECT_ERROR = 5;
    private final CopyOnWriteArrayList<IConnectResultListener> list = new CopyOnWriteArrayList<>();
    @SuppressLint("HandlerLeak")
    private final Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    String clientMessage = (String) msg.obj;
                    if (clientMessage == null) {
                        clientMessage = "startSession";
                    }
                    Iterator<IConnectResultListener> it = SessionManager.this.list.iterator();
                    while (it.hasNext()) {
                        IConnectResultListener l = it.next();
                        l.onConnected(clientMessage);
                    }
                    return;
                case 1:
                    Iterator<IConnectResultListener> it2 = SessionManager.this.list.iterator();
                    while (it2.hasNext()) {
                        IConnectResultListener l2 = it2.next();
                        l2.onDisconnect("removeSession");
                    }
                    return;
                case 2:
                default:
                    return;
                case 3:
                    Iterator<IConnectResultListener> it3 = SessionManager.this.list.iterator();
                    while (it3.hasNext()) {
                        IConnectResultListener l3 = it3.next();
                        l3.onDeviceConnect();
                    }
                    return;
                case 4:
                    Iterator<IConnectResultListener> it4 = SessionManager.this.list.iterator();
                    while (it4.hasNext()) {
                        IConnectResultListener l4 = it4.next();
                        l4.onDeviceDisConnnect();
                    }
                    return;
                case 5:
                    Iterator<IConnectResultListener> it5 = SessionManager.this.list.iterator();
                    while (it5.hasNext()) {
                        IConnectResultListener l5 = it5.next();
                        l5.onConnectError("");
                    }
            }
        }
    };
    public boolean CONNECTION_SUCCEED = false;
    private BaseConnect mSession;

    public static void initInstance() {
        getInstance();
    }

    public static SessionManager getInstance() {
        return mSessionManager;
    }

    public int getSize() {
        return this.list.size();
    }

    public void add2NoticeList(IConnectResultListener callback) {
        this.list.add(callback);
    }

    public void removeNoticeList(IConnectResultListener callBack) {
        IConnectResultListener remove = null;
        Iterator<IConnectResultListener> it = this.list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            IConnectResultListener l = it.next();
            if (l != null && l == callBack) {
                remove = l;
                break;
            }
        }
        if (remove != null) {
            this.list.remove(remove);
        }
    }

    public synchronized void addSession(BaseConnect mSession) {
        this.mSession = mSession;
        this.mHanlder.sendEmptyMessage(0);
    }

    public synchronized void addSession(BaseConnect mSession, String clientMessage) {
        this.mSession = mSession;
        this.mHanlder.obtainMessage(0, clientMessage).sendToTarget();
    }

    public synchronized void removeSession() {
        this.mSession = null;
        this.mHanlder.sendEmptyMessage(1);
    }

    public synchronized BaseConnect getSession() {
        return this.mSession;
    }

    public void sendCmd(BaseCommand cmd) {
        try {
            if (this.mSession != null) {
                this.mSession.sendCmd(cmd);
            }
        } catch (Exception e) {
        }
    }

    public void sendTimeCmd(BaseCommand cmd) {
        try {
            if (this.mSession != null) {
                this.mSession.sendTimeCmd(cmd);
            }
        } catch (Exception e) {
        }
    }

    public synchronized boolean hasSession() {
        return this.mSession != null;
    }

    public void closeSession() {
        if (this.mSession != null) {
            this.mSession.closeSession();
        }
    }

    public synchronized boolean isDeviceConnected() {
        boolean ret;
        ret = false;
        if (this.mSession != null) {
            if (this.mSession.isDeviceConnected()) {
                ret = true;
            }
        }
        return ret;
    }

    public synchronized void onDeviveState(int type) {
        if (type == 0) {
            this.mHanlder.sendEmptyMessage(4);
        } else if (type == 2) {
            this.mHanlder.sendEmptyMessage(5);
        } else {
            this.mHanlder.sendEmptyMessage(3);
        }
    }

    public void showListAll() {
        Iterator<IConnectResultListener> it = this.list.iterator();
        while (it.hasNext()) {
            it.next();
        }
    }
}
