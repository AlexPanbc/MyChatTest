package com.pbc.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * Created by panbingcan on 2018/1/23.
 */
public class AIOClient implements Runnable {
    private AsynchronousSocketChannel channel;

    public AIOClient() {
        try {
            channel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //写入数据到服务器
    public void write(String data) {
        try {
            channel.write(ByteBuffer.wrap(data.getBytes())).get();
            read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer).get();
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String data = new String(bytes, "utf-8");
            System.out.println(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //创建连接
    public void connect() {
        channel.connect(new InetSocketAddress("127.0.0.1", 8888));
    }

    @Override
    public void run() {
        while (true) {

        }
    }

    public static void main(String[] args) {
        try {
            AIOClient c1 = new AIOClient();
            AIOClient c2 = new AIOClient();
            AIOClient c3 = new AIOClient();

            c1.connect();
            c2.connect();
            c3.connect();

            new Thread(c1).start();
            new Thread(c2).start();
            new Thread(c3).start();


            Thread.sleep(1000);
            c1.write("aaaa");
            c2.write("bbbb");
            c3.write("cccc");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
