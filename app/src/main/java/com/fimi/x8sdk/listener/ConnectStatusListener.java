package com.fimi.x8sdk.listener;

/* loaded from: classes2.dex */
public class ConnectStatusListener {

    /* loaded from: classes2.dex */
    public interface ICamConectCallBack {
        void onConnected();

        void onConnectionClosed();
    }

    /* loaded from: classes2.dex */
    public interface IEngineCallback {
        void onConnected();

        void onConnectionClosed();

        void onConnectionEstablished();
    }

    /* loaded from: classes2.dex */
    public interface IFCConnectCallBack {
        void onConnected();

        void onConnectionClosed();

        void onReConnected();
    }

    /* loaded from: classes2.dex */
    public interface IRCConnectCallBack {
        void onConnected();

        void onConnectionClosed();
    }
}
