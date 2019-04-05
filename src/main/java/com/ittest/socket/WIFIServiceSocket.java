package com.ittest.socket;

import com.ittest.OperatorDate.OperatorSocketData;
import com.sun.corba.se.spi.transport.SocketInfo;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class WIFIServiceSocket extends Thread {
    // 定义保存所有Socket的ArrayList
    public static ArrayList<Socket> socketList = new ArrayList<Socket>();
    // 保存socketList中每个socket的信息
    public static ArrayList<SocketInfo> socketInfo = new ArrayList<SocketInfo>();
    private ServerSocket serverSocket = null;
    private ServletContext servletContext = null;

    public WIFIServiceSocket(ServerSocket serverSocket, ServletContext servletContext) {
        if (serverSocket == null) {
            try {
                this.serverSocket = new ServerSocket(30000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.servletContext = servletContext;
    }


    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see #start()
     * @see #stop()
     */
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                //  客户端地址
                System.out.println("###getInetAddress # " + socket.getInetAddress());
                //  客户端端口
                System.out.println("###getPort # " + socket.getPort());
                socketList.add(socket);
                System.out.println("###连接成功");

                if (socket != null) {
// 每当客户端连接后启动一条ServerThread线程为该客户端服务
                    new Thread(new OperatorSocketData(socket)).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeSocketServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

