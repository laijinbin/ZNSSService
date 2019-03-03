package com.ittest.OperatorDate;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.ittest.controller.WebSocketTest;
import com.ittest.socket.WIFIServiceSocket;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;


public class OperatorSocketData implements Runnable {
    // 定义当前线程所处理的Socket
    private Socket s = null;
    // 该线程所处理的Socket所对应的输入流
    private BufferedReader br = null;
    private InputStream in=null;

    public OperatorSocketData(Socket s) throws IOException {
        this.s = s;
// 初始化该Socket对应的输入流
in=s.getInputStream();
       // br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        try {
            String content = null;
            // 采用循环不断从Socket中读取客户端发送过来的数据
            while (true) {
                content = readFromClient();
                System.out.println("客户端--->服务器：  " + content);
                WebSocketTest.socketTestMap.get("CONN_9527").sendMessage(content);
                //  TODO:实现客户端与服务器的一对一交流，暂未实现客户端之间的交流
                // 遍历socketList中的每个Socket，
                // 将读到的内容向每个Socket发送一次
//           for (Iterator<Socket> it = WebServer.socketList.iterator(); it.hasNext(); )
                //           {
                //               Socket s = it.next();
//                try {
//
//                    OutputStream os = s.getOutputStream();
//                    os.write((content + "\r\n").getBytes("utf-8"));
//                    System.out.println("服务器--->客户端：  content + --C-- " + content);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                    // 删除该Socket。
//                    //            it.remove();
//                    s.close();
//                    System.out.println(WIFIServiceSocket.socketList);
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    // 定义读取客户端数据的方法
    private String readFromClient() {
        try {
           byte[] bytes=new byte[30];
            in.read(bytes);
           return new String(bytes).trim();
        }
// 如果捕捉到异常，表明该Socket对应的客户端已经关闭
        catch (IOException e) {
            e.printStackTrace();
// 删除该Socket。
            WIFIServiceSocket.socketList.remove(s);
        }
        return null;
    }
}

