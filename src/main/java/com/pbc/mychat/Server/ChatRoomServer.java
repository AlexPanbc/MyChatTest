package com.pbc.mychat.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Alex on 2018/1/19.
 */
public class ChatRoomServer {
    private int port;

    public ChatRoomServer(int port) {
        this.port = port;
    }

    public void start() {

        //创建两个线程组
        EventLoopGroup boss = new NioEventLoopGroup();//负责客户端的链接
        EventLoopGroup worker = new NioEventLoopGroup();//负责与已连接的客户端通讯
        try {

            ServerBootstrap boot = new ServerBootstrap();
            // TODO: 2018/1/19 配置服务器
            boot.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitialize()) // TODO: 2018/1/19 设置处理客户端请求的回调类
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = boot.bind(this.port).sync();//启动服务器
            System.out.println("服务器已启动...");
            future.channel().closeFuture().sync();//关闭服务器
            System.out.println("服务器关闭...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();//关闭
            boss.shutdownGracefully();//关闭
        }
    }

    public static void main(String[] args) {
        new ChatRoomServer(8888).start();
    }
}
