package com.pbc.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Alex on 2018/1/22.
 */
public class AIOServer {
    //定义线程池
    private ExecutorService exceutorService;
    //线程组
    private AsynchronousChannelGroup channelGroup;
    //服务器通道
    protected AsynchronousServerSocketChannel channel;

    public AIOServer(int port) {

        try {
            exceutorService = Executors.newCachedThreadPool();
            //创建线程组
            channelGroup = AsynchronousChannelGroup.withCachedThreadPool(exceutorService, 1);
//创建服务器通道
            channel = AsynchronousServerSocketChannel.open(channelGroup);
            channel.bind(new InetSocketAddress(port));
            System.out.println("服务器已启动 端口号是：" + port);
            channel.accept(this,new ServerCompletionHandler());
            Thread.sleep(Integer.MAX_VALUE);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        AIOServer server = new AIOServer(8888);
    }
}
