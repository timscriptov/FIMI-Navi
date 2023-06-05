package com.fimi.kernel.connect.tcp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fimi.kernel.connect.BaseCommand;
import com.fimi.kernel.connect.BaseConnect;
import com.fimi.kernel.connect.ResultListener;
import com.fimi.kernel.dataparser.CmdSession;
import com.fimi.kernel.dataparser.milink.ByteHexHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;


public class TcpConnect extends BaseConnect implements Runnable {
    boolean autoConnect;
    Socket socket;
    com.fimi.kernel.connect.SocketOption socketOption;
    private CmdSession cmdSession;
    private final LinkedBlockingDeque<Object> dataQue = new LinkedBlockingDeque<>();
    private boolean exitTcp = false;
    private ReadThread readThread;
    private WriteThread writeThread;
    private final ResultListener x9ResultListener;

    public TcpConnect(@NonNull com.fimi.kernel.connect.SocketOption option, ResultListener listener) {
        this.autoConnect = false;
        this.socketOption = option;
        this.autoConnect = option.isAutoReconnect();
        this.x9ResultListener = listener;
    }

    @Override
    public void run() {
    }

    private void connectSocket(@NonNull com.fimi.kernel.connect.SocketOption option) {
        try {
            Log.d("moweiru", "cmd tcp connect success");
            this.socket = new Socket(option.getHost(), option.getPort());
        } catch (IOException ex) {
            Log.d("moweiru", "fail to connect tcp ,exception:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void startSession() {
        connectSocket(this.socketOption);
        this.readThread = new ReadThread();
        this.writeThread = new WriteThread();
        this.readThread.start();
        this.writeThread.start();
    }

    @Override
    public void closeSession() {
        this.exitTcp = false;
        if (this.readThread != null) {
            this.readThread.interrupt();
        }
        if (this.writeThread != null) {
            this.writeThread.interrupt();
        }
        if (this.socket != null && this.socket.isConnected()) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendCmd(BaseCommand cmd) {
        this.dataQue.add(cmd);
    }

    @Override
    public boolean isDeviceConnected() {
        return false;
    }

    private void sendHeartBit() {
        this.dataQue.add(null);
    }

    @Override
    public void sendJsonCmd(BaseCommand cmd) {
    }


    public class ReadThread extends Thread {
        private InputStream mInputStream;

        public ReadThread() {
        }

        @Override
        public void run() {
            try {
                this.mInputStream = TcpConnect.this.socket.getInputStream();
                if (this.mInputStream != null) {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        this.mInputStream.read(buffer);
                        TcpConnect.this.x9ResultListener.messageReceived(buffer);
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e1) {
                Log.d("moweiru==>", e1.getMessage());
                e1.printStackTrace();
            }
        }
    }


    public class WriteThread extends Thread {
        OutputStream mOutput;
        private int count;

        public WriteThread() {
        }

        @Override
        public void run() {
            try {
                this.mOutput = TcpConnect.this.socket.getOutputStream();
                if (this.mOutput != null) {
                    while (!TcpConnect.this.exitTcp) {
                        synchronized (TcpConnect.this.dataQue) {
                            if (!TcpConnect.this.dataQue.isEmpty()) {
                                BaseCommand cmd = (BaseCommand) TcpConnect.this.dataQue.poll();
                                Log.d("moweiru", "tcp write cmd:" + ByteHexHelper.bytesToHexString(cmd.getCmdData()));
                                if (cmd != null) {
                                    this.mOutput.write(cmd.getCmdData(), 0, cmd.getCmdData().length);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("moweiru", "write exception:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
