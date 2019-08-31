package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主类
 * @author ta
 *
 */
public class WebServer {

    private ServerSocket server;

    /**
     * 构造方法,用于初始化服务端
     */
    public WebServer() {
        try {
            System.out.println("正在启动服务端...");
            server = new ServerSocket(8088);
            System.out.println("服务端启动完毕!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 服务端开始工作的方法
     */
    private void start() {
        try {
            while(true) {
                System.out.println("等待客户端连接...");
                Socket socket = server.accept();
                System.out.println("一个客户端连接了!");
                ClientHandler handler
                        = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
