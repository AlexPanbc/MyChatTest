package com.pbc.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by Alex on 2018/1/22.
 */
public class NIOServer implements Runnable {

    private Selector selector = null;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);//1K  字节

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
            //注册通道
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器已启动");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        int a=0;
        while (true) {
            try {
                //轮训所有已注册通道
                selector.select();
                //收货所有已经注册通道的key值
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            accept(key);
                        }
                        if (key.isReadable()) {
                            read(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(selector,SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        try {
            //清空缓冲区
            buffer.clear();
            //获取通道
            SocketChannel sc = (SocketChannel) key.channel();
            //将通道获取的数据写入缓冲区
            int count = sc.read(buffer);
            if (count == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String msg = new String(bytes).trim();
            System.out.println("客户说：" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //启动轮询线程
        new Thread(new NIOServer(8888)).start();
    }

}
