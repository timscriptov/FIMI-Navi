package com.fimi.x8sdk.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;

import androidx.annotation.NonNull;

import com.fimi.x8sdk.connect.CommunicationManager;
import com.fimi.x8sdk.connect.ConnectType;
import com.fimi.x8sdk.listener.ConnectStatusListener;


public class ConnectAOAManager {
    private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
    private final PendingIntent mPermissionIntent;
    Context mContext;
    UsbManager usbManager;
    boolean isRequestPermission = false;
    private volatile boolean mConnected = false;

    public ConnectAOAManager(@NonNull Context mContext) {
        this.mContext = mContext;
        this.usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        this.mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
    }

    public void conectAOA() {
        UsbAccessory accessory;
        if (!this.mConnected) {
            if (this.usbManager != null) {
                UsbAccessory[] accessories = this.usbManager.getAccessoryList();
                accessory = accessories != null ? accessories[0] : null;
                if (accessory != null) {
                    if (this.usbManager.hasPermission(accessory)) {
                        CommunicationManager.getCommunicationManager().setAccessory(accessory);
                        CommunicationManager.getCommunicationManager().startConnectThread(this.mContext, ConnectType.Aoa);
                        this.callback.onConnected();
                        this.mConnected = true;
                    } else if (!this.isRequestPermission) {
                        this.usbManager.requestPermission(accessory, this.mPermissionIntent);
                        this.callback.onConnectionEstablished();
                        this.isRequestPermission = true;
                    }
                }
            }
        } else if (this.usbManager != null) {
            UsbAccessory[] accessories2 = this.usbManager.getAccessoryList();
            accessory = accessories2 != null ? accessories2[0] : null;
            if (accessory == null) {
                this.mConnected = false;
                this.callback.onConnectionClosed();
                CommunicationManager.getCommunicationManager().stopConnectThread();
            }
        }
    }

    public void unConnectAOA() {
        CommunicationManager.getCommunicationManager().stopConnectThread();
        this.mConnected = false;
        this.callback.onConnectionClosed();
    }

    ConnectStatusListener.IEngineCallback callback = this.callback;
}
