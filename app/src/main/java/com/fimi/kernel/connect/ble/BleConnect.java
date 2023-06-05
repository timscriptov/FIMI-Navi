package com.fimi.kernel.connect.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.SystemClock;

import com.fimi.kernel.Constants;
import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.ResultListener;
import com.fimi.kernel.connect.interfaces.IDataTransfer;
import com.fimi.kernel.connect.interfaces.IRetransmissionHandle;
import com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle;
import com.fimi.kernel.connect.interfaces.ble.IBleSendData;
import com.fimi.kernel.connect.retransmission.RetransmissionThread;
import com.fimi.kernel.connect.retransmission.TimerSendQueueThread;
import com.fimi.kernel.connect.session.NoticeManager;
import com.fimi.kernel.connect.session.SessionManager;
import com.fimi.kernel.dataparser.fmlink4.LinkPacket4;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;
import com.fimi.kernel.dataparser.milink.LinkPacket;
import com.fimi.kernel.dataparser.milink.Parser;
import com.fimi.kernel.utils.ByteUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: classes.dex */
public class BleConnect extends BaseConnect implements IDataTransfer, IRetransmissionHandle, ITimerSendQueueHandle {
    private final int SEND_LEN = 20;
    public LinkedBlockingDeque<Object> cmdQuene = new LinkedBlockingDeque<>();
    public LinkedBlockingDeque<byte[]> cmdQuene2 = new LinkedBlockingDeque<>();
    public int[] cmds = {193, 201, 202, 203, 204};
    BaseCommand bleStateCommand;
    Logger logger = LoggerFactory.getLogger("gh2_communication_log");
    boolean isWait = false;
    private BluetoothGattCharacteristic characterWrite;
    private Context context;
    private BluetoothDevice device;
    private ResultListener gh2ResultListener;
    private long lastTime;
    private IBleSendData mBleSendDataHandle;
    private CheckDeviceConnectThread mCheckDeviceConnectThread;
    private ReadThread mReadThread;
    private RetransmissionThread mRetransmissionThread;
    private TimerSendQueueThread mTimerSendQueueThread;
    private WriteThread mWriteThread;
    private Parser p = new Parser();
    private BaseConnect.DeviceConnectState mDeviceConnectState = BaseConnect.DeviceConnectState.IDEL;

    public BleConnect(IBleSendData mIBleSendData, ResultListener listener) {
        this.mBleSendDataHandle = mIBleSendData;
        this.gh2ResultListener = listener;
    }

    public void setBleState(BaseCommand bleStateCommand) {
        this.bleStateCommand = bleStateCommand;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public void sendCmd(BaseCommand cmd) {
        this.cmdQuene.offer(cmd);
        sendSignal();
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public void sendJsonCmd(BaseCommand cmd) {
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public void sendTimeCmd(BaseCommand cmd) {
        sendDividedData(cmd.getMsgGroudId(), cmd.getMsgId(), cmd.getCmdData(), cmd.getCmdData().length);
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public void startSession() {
        setClientAddress(this.device.getAddress());
        setClientName(this.device.getName());
        SessionManager.getInstance().addSession(this, this.device.getAddress());
        receiveLog("startSession");
        this.mWriteThread = new WriteThread();
        this.mReadThread = new ReadThread();
        this.mRetransmissionThread = new RetransmissionThread(this);
        this.mTimerSendQueueThread = new TimerSendQueueThread();
        this.gh2ResultListener.setRetransmissionHandle(this);
        this.gh2ResultListener.setTimerSendQueueHandle(this);
        if (this.mWriteThread != null) {
            this.mWriteThread.start();
        }
        if (this.mReadThread != null) {
            this.mReadThread.start();
        }
        if (this.mRetransmissionThread != null) {
            this.mRetransmissionThread.start();
        }
        if (this.mTimerSendQueueThread != null) {
            this.mTimerSendQueueThread.start();
        }
    }

    public void startRetransmissionThread() {
        receiveLog("" + hashCode() + " 蓝牙通途收发启动.................");
        if (this.mCheckDeviceConnectThread == null) {
            receiveLog("" + hashCode() + " 开启连接检测线程.................");
            this.mDeviceConnectState = BaseConnect.DeviceConnectState.CONNECTED;
            SessionManager.getInstance().onDeviveState(1);
            this.mCheckDeviceConnectThread = new CheckDeviceConnectThread();
            updateTime();
            this.mCheckDeviceConnectThread.start();
        }
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public void closeSession() {
        receiveLog("closeSession");
        if (this.mWriteThread != null) {
            this.mWriteThread.releaseConnection();
            this.mWriteThread = null;
        }
        if (this.mReadThread != null) {
            this.mReadThread.releaseConnection();
            this.mReadThread = null;
        }
        if (this.mCheckDeviceConnectThread != null) {
            this.mCheckDeviceConnectThread.exit();
            this.mCheckDeviceConnectThread = null;
        }
        if (this.mRetransmissionThread != null) {
            this.mRetransmissionThread.exit();
            this.mRetransmissionThread = null;
        }
        if (this.mTimerSendQueueThread != null) {
            this.mTimerSendQueueThread.exit();
            this.mTimerSendQueueThread = null;
        }
        this.mDeviceConnectState = BaseConnect.DeviceConnectState.DISCONNECT;
        SessionManager.getInstance().onDeviveState(0);
        SessionManager.getInstance().removeSession();
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public boolean isDeviceConnected() {
        return this.mDeviceConnectState == BaseConnect.DeviceConnectState.CONNECTED;
    }

    private boolean isUpdateCmd(int groupId, int msgId) {
        int[] iArr;
        if (groupId != 9) {
            return false;
        }
        for (int c : this.cmds) {
            if (msgId == c) {
                return true;
            }
        }
        return false;
    }

    public void sendDividedData(int groupId, int msgId, byte[] bytes, int len) {
        int slices;
        int divided = len / 20;
        int remainder = len % 20;
        if (remainder == 0) {
            slices = divided;
        } else {
            slices = divided + 1;
        }
        int deviceModle = bytes[22] & 255;
        int sleepTime = 10;
        if (groupId == 9 && msgId == 201 && deviceModle == 8) {
            sleepTime = 15;
        } else if (groupId == 9 && msgId == 201 && deviceModle == 3) {
            sleepTime = 15;
        }
        for (int i = 0; i < slices; i++) {
            if (i == slices - 1 && remainder != 0) {
                byte[] buff = new byte[remainder];
                System.arraycopy(bytes, i * 20, buff, 0, remainder);
                this.mBleSendDataHandle.sendMessage(groupId, msgId, buff, sleepTime);
            } else {
                byte[] buff2 = new byte[20];
                System.arraycopy(bytes, i * 20, buff2, 0, 20);
                this.mBleSendDataHandle.sendMessage(groupId, msgId, buff2, sleepTime);
            }
        }
        updateTime();
        String hex = ByteHexHelper.bytesToHexString(bytes);
        sendLog(hex);
    }

    @Override // com.fimi.kernel.connect.interfaces.IRetransmissionHandle
    public boolean removeFromListByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        if (this.mRetransmissionThread == null) {
            return false;
        }
        return this.mRetransmissionThread.removeFromListByCmdID(groupId, cmdId, seq, packet);
    }

    @Override // com.fimi.kernel.connect.interfaces.IRetransmissionHandle
    public boolean removeFromListByCmdIDLinkPacket4(int groupId, int cmdId, int seq, LinkPacket4 packet) {
        return false;
    }

    @Override // com.fimi.kernel.connect.interfaces.ITimerSendQueueHandle
    public boolean removeFromTimerSendQueueByCmdID(int groupId, int cmdId, int seq, LinkPacket packet) {
        if (this.mTimerSendQueueThread == null) {
            return false;
        }
        return this.mTimerSendQueueThread.removeFromListByCmdID(groupId, cmdId, seq, packet);
    }

    @Override // com.fimi.kernel.connect.interfaces.IDataTransfer
    public void sendRestransmissionData(BaseCommand cmd) {
        sendLog("" + hashCode() + " 重发数据 seq =" + cmd.getMsgSeq() + " " + Integer.toHexString(cmd.getMsgId()));
        sendDividedData(cmd.getMsgGroudId(), cmd.getMsgId(), cmd.getCmdData(), cmd.getCmdData().length);
    }

    @Override // com.fimi.kernel.connect.interfaces.IDataTransfer
    public void onSendTimeOut(int groupId, int cmdId, BaseCommand bcd) {
        NoticeManager.getInstance().onSendTimeOut(groupId, cmdId, bcd);
    }

    public void sendSignal() {
        synchronized (this.cmdQuene) {
            if (this.isWait) {
                this.isWait = false;
                this.cmdQuene.notify();
            } else {
                this.isWait = true;
                try {
                    this.cmdQuene.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateTime() {
        this.lastTime = System.currentTimeMillis();
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public void receiveLog(String msg) {
        this.logger.info("                App ==>" + msg);
    }

    @Override // com.fimi.kernel.connect.BaseConnect
    public void sendLog(String msg) {
        this.logger.info("             send   ==> " + msg);
    }

    public void onBleData(byte[] data) {
        this.cmdQuene2.add(data);
    }

    /* loaded from: classes.dex */
    public class WriteThread extends Thread {
        private boolean mExit;

        public WriteThread() {
        }

        public void releaseConnection() {
            this.mExit = true;
            interrupt();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.mExit) {
                try {
                    if (!BleConnect.this.cmdQuene.isEmpty()) {
                        BaseCommand cmd = (BaseCommand) BleConnect.this.cmdQuene.poll();
                        if (cmd != null) {
                            cmd.setLastSendTime(SystemClock.uptimeMillis());
                            if (cmd.isAddToSendQueue()) {
                                if (cmd.isTimerCmd()) {
                                    BleConnect.this.mTimerSendQueueThread.addToSendList(cmd);
                                } else {
                                    BleConnect.this.mRetransmissionThread.addToSendList(cmd);
                                }
                            }
                            BleConnect.this.sendDividedData(cmd.getMsgGroudId(), cmd.getMsgId(), cmd.getCmdData(), cmd.getCmdData().length);
                        }
                    } else {
                        BleConnect.this.sendSignal();
                    }
                } catch (Exception e) {
                    BleConnect.this.receiveLog(" sessionhandler writethread error " + e.toString());
                    return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class ReadThread extends Thread {
        private boolean mExit;

        public ReadThread() {
        }

        public void releaseConnection() {
            this.mExit = true;
            interrupt();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.mExit) {
                try {
                    if (!BleConnect.this.cmdQuene2.isEmpty()) {
                        byte[] data = BleConnect.this.cmdQuene2.poll();
                        if (data != null) {
                            BleConnect.this.gh2ResultListener.messageReceived(data);
                            BleConnect.this.receiveLog(ByteUtil.bytesToHexString(data) + "=======");
                        }
                    } else {
                        Thread.sleep(100L);
                    }
                } catch (Exception e) {
                    BleConnect.this.receiveLog(" sessionhandler writethread error " + e.toString());
                    return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class CheckDeviceConnectThread extends Thread {
        private boolean isLoop = true;

        public CheckDeviceConnectThread() {
        }

        public void exit() {
            this.isLoop = false;
            interrupt();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            BleConnect.this.receiveLog(" CheckDeviceConnectThread run");
            while (this.isLoop) {
                if (System.currentTimeMillis() - BleConnect.this.lastTime > 2000 && BleConnect.this.cmdQuene.size() <= 0 && Constants.isCheckDeviceConnect && SessionManager.getInstance().hasSession()) {
                    BleConnect.this.sendCmd(BleConnect.this.bleStateCommand);
                }
                try {
                    sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
