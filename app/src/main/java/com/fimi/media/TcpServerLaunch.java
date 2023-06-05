package com.fimi.media;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TcpServerLaunch extends Thread {
    static StringBuffer s = new StringBuffer();
    private static TextView logView;
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TcpServerLaunch.s.length() > 300) {
                TcpServerLaunch.s.delete(0, TcpServerLaunch.s.length());
            }
            TcpServerLaunch.s.append((String) msg.obj);
            TcpServerLaunch.logView.setText(TcpServerLaunch.s.toString());
        }
    };

    public TcpServerLaunch(TextView logView2) {
        logView = logView2;
    }

    @Override
    public void run() {
        main();
    }

    public void main() {
        try {
            ServerSocket ss = new ServerSocket(54321);
            while (true) {
                Socket s2 = ss.accept();
                mHandler.obtainMessage(0, "start").sendToTarget();
                new TcpServer(s2).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
