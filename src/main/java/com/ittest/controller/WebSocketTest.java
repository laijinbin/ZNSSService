package com.ittest.controller;


import com.alibaba.fastjson.JSON;
import com.ittest.socket.WIFIServiceSocket;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/Miniprogram")
@Component
public class WebSocketTest {
    private Session session;

    private static int onlineCount =0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();
    public static Map<String,WebSocketTest> socketTestMap=new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
//        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
//        socketTestMap.put("CONN_9527",this);
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */

//    @RequestMapping("/closeSocket")
//    @ResponseBody
//    public Map closeSocket(String deviceName){
//        Map<String,Object> map=new HashMap<>();
//        subOnlineCount();           //在线数减1
//        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
//        socketTestMap.remove(deviceName);
//        map= WebUtil.generateModelMap("0","socket已关闭");
//        return map;
//    }
    @OnClose
    public void onClose(){
//        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        Map<String,Object> messageMap= (Map<String, Object>) JSON.parse(message);
        String msg= (String) messageMap.get("msg");
        String flag= (String) messageMap.get("flag");
        String equipment= (String) messageMap.get("equipment");
        String binddevice= (String) messageMap.get("binddevice");
        if ("2".equals(flag)){
            socketTestMap.remove(binddevice);
            return;
        }

         if (socketTestMap.get(binddevice)==null || !this.equals(socketTestMap.get(binddevice))){
            socketTestMap.put(binddevice,this);
            return;
        }
        System.out.println(msg);
        System.out.println(equipment);
        byte[] bytes=null;
        try {
             bytes=(msg+"\r\n").getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //发给单一的客户端,map发送
        for (String s : WIFIServiceSocket.socketMap.keySet()) {
            if (equipment.equals(s)){
                try {
                 Socket socket=WIFIServiceSocket.socketMap.get(s);
                OutputStream os = socket.getOutputStream();
                os.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }
        /*for (Socket socket : WIFIServiceSocket.socketList) {
            try {
                OutputStream os = socket.getOutputStream();
                os.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }



}
