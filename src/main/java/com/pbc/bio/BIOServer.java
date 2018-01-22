package com.pbc.bio;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞试 网络通讯方式
 * 先启动BIOServer 然后在启动BIOClient 可以启动多个BIOClient
 * Created by Alex on 2018/1/22.
 */
public class BIOServer {
    private static int PROT = 8888;

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PROT);
            System.out.println("服务启动...");
            while (true) {
                System.out.println("等待客户进入...");
                Socket socket = ss.accept();
                System.out.println("客人进房...");
                new Thread(new BIOServerHandler(socket)).start();//每一个客户端消息过来都会开启一个线程
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ss != null)
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            ss = null;
        }
    }

}
