package com.pbc.mychat.webchat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by Alex on 2018/1/21.
 */
public class WebChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec())//将请求和应答消息编码或者解码为http协议的消息
                .addLast(new HttpObjectAggregator(64*1024))
                .addLast(new ChunkedWriteHandler())//向客户端发送html5也页面文件
                .addLast(new HttpRequesstHanler("/chat"))//自定义类  区分http请求和websocket请求 读取html页面 并写回浏览器
                .addLast(new WebSocketServerProtocolHandler("/chat"))
                .addLast(new TextWebSocketFrameHandle());

    }
}
