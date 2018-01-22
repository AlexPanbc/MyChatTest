package com.pbc.mychat.webchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by Alex on 2018/1/21.
 */
public class TextWebSocketFrameHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //所有客户端
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    // TODO: 2018/1/19 当有客户端连接时 执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        Channel incoming = ctx.channel();// TODO: 2018/1/19 获得客户端通道
        channels.add(incoming);//新加入的客户端加入到队列去
        for (Channel ch : channels)
            if (ch != incoming)
                ch.writeAndFlush("欢迎：" + incoming.remoteAddress() + "进入聊天室" + "\n");

//        super.handlerAdded(ctx);
    }

    // TODO: 2018/1/19 当有客户端断开时 执行
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();// TODO: 2018/1/19 获得客户端通道
        channels.remove(incoming);//有客户端开 时执行
        for (Channel ch : channels)
            if (ch != incoming)
                ch.writeAndFlush("再见：" + incoming.remoteAddress() + "离开了聊天室" + "\n");

        // super.handlerRemoved(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel client = ctx.channel();
        System.out.println("服务器接收到[" + client.id() + "]说：" + msg.text() + "\n");
        for (Channel ch : channels)
            if (ch != client) {
                ch.writeAndFlush(new TextWebSocketFrame("【用户：" + client.remoteAddress() + "说】：" + msg.text() + "\n"));
            } else {
                ch.writeAndFlush("[我说]：" + msg.text() + "\n");
            }

    }
}
