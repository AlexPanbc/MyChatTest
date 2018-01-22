package com.pbc.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by Alex on 2018/1/22.
 */
public class NIOServer implements Runnable {

    private Selector selector = null;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);//一个字节

    public NIOServer(int port) {
        try {
            //打开Selector
            selector = Selector.open();
            //打开服务器通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            //谁知服务器通道为非阻塞形式
            ssc.configureBlocking(false);
            //绑定端口
            ssc.bind(new InetSocketAddress(port));//jdk1.7以上版本写法
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器已启动");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        while (true)
        {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //启动轮询线程
        new Thread(new NIOServer(8888)).start();
    }

}
