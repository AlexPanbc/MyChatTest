package com.pbc.mychat.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Alex on 2018/1/19.
 */
public class ChatClient {
    private String host;
    private int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {

        EventLoopGroup worker = new NioEventLoopGroup();//负责与已连接的客户端通讯
        Bootstrap boot = new Bootstrap();//与服务端的不同
        try {
            // TODO: 2018/1/19 配置服务器
            boot.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitialize());// TODO: 2018/1/19 设置处理客户端请求的回调类
            Channel channel = boot.connect(this.host, this.port).sync().channel();
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                channel.writeAndFlush(input.readLine() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();//关闭
        }
    }

    public static void main(String[] args) {
        new ChatClient("127.0.0.1", 8888).start();
    }
}
