package com.pbc.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alex on 2018/1/23.
 */
public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AIOServer> {

    //completed方法会在有客户端介入的时候自动调用
    @Override
    public void completed(AsynchronousSocketChannel channel, AIOServer attachment) {
        System.out.println("有客户端连入...");
        //当客户端被介入进来（completed）, 直接调用ServerChannel的accept方法去接待下一个客户端，若如此周而复始
        attachment.channel.accept(attachment, this);
        read(channel);
    }

    //读取用户端请求
    private void read(AsynchronousSocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);//1k
        channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                System.out.println("Server:收到客户端反悔的数据长度为：" + result);
                String data = new String(buffer.array()).trim();
                System.out.println("Server:客户端说：" + data);
                String response = "你好 客户端";
                write(channel, response);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
            }
        });
    }

    //服务端发送消息给客户端
    private void write(AsynchronousSocketChannel channel, String msg) {

        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(msg.getBytes());
            buffer.flip();
            channel.write(buffer).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    //失败时 异常捕获
    @Override
    public void failed(Throwable exc, AIOServer attachment) {
        exc.printStackTrace();
    }
}
