package com.pbc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Alex on 2018/1/23.
 */
public class NettyServer {
    private int port = 8888;

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();// 用于处理服务器端 接受客户端连接
        EventLoopGroup workGroup = new NioEventLoopGroup();//进行SocketChannel的网络通讯，数据多谢

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();//辅助工具类，用于服务端通道的一系列设置
            bootstrap.group(bossGroup, workGroup)//绑定线程组
                    .channel(NioServerSocketChannel.class)//指定NIO模式
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ServerHandler());//设定回调类
                        }
                    })//设置回调  配置具体的数据处理方式
                    .option(ChannelOption.SO_BACKLOG, 128)//设置TCP缓冲区
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)//接受数据缓冲区大小
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            //组织程序结束
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new NettyServer().start();
    }
}
