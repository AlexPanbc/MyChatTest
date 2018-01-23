package com.pbc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Alex on 2018/1/23.
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHander());//客户端回调类 接收服务器发送来的数据
                    }
                });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8888);
        future.channel().writeAndFlush(Unpooled.copiedBuffer("你好".getBytes()));//此时 触发服务端绑定的 回调类（给服务端发送数据时 触发回调类）
        future.channel().closeFuture().sync();//接受服务器 返回的值触发 客户端的回调类//阻止 客户端结束
        workGroup.shutdownGracefully();
        System.out.println("end....");

    }
}
