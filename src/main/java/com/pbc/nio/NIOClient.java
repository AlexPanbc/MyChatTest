package com.pbc.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Alex on 2018/1/22.
 */
public class NIOClient {
    public static void main(String [] args)
    {
        InetSocketAddress ip =new InetSocketAddress("127.0.0.1",8888);
        SocketChannel sc = null;     //声明通道
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            //打开通道
            sc = SocketChannel.open();
            sc.connect(ip);
            while (true)
            {
                byte[] bytes = new byte[1024];
                System.in.read(bytes);//
                buffer.put(bytes);
                buffer.flip();
                sc.write(buffer);//写给服务器端nioserver
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(sc!=null)
            {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
