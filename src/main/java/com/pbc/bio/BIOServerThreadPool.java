package com.pbc.bio;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞试 网络通讯方式
 * 先启动BIOServer 然后在启动BIOClient 可以启动多个BIOClient
 * Created by Alex on 2018/1/22.
 */
public class BIOServerThreadPool {
    private static int PROT = 8888;

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PROT);
            System.out.println("服务启动...");
            HanderExecutorPool pool = new HanderExecutorPool(4, 1);
            boolean isTrue = false;
            while (true) {
                if (pool.wqueue.size() < 1) {
                    System.out.println("等待人数：" + pool.wqueue.size());
                    System.out.println("等待客户进入...");
                    Socket socket = ss.accept();
                    System.out.println("客人进房...");
                    pool.execute(new BIOServerHandler(socket));
                    System.out.println("活动线程数：" + pool.activeCount + "个");
                    if (pool.wqueue.size() == 1) isTrue = true;
                    // new Thread(new BIOServerHandler(socket)).start();//每一个客户端消息过来都会开启一个线程
                } else {
                    if (isTrue) {
                        System.out.println("满员 稍等再来");
                        isTrue = false;
                    }
                }
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
