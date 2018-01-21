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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel client = ctx.channel();
        System.out.println("服务器接收到[" + client.id() + "]说：" + msg + "\n");
        for (Channel ch : channels)
            if (ch != client) {
                ch.writeAndFlush(new TextWebSocketFrame("用户：" + client.remoteAddress() + "说：" + msg.text() + "\n"));
            } else {
                ch.writeAndFlush("我说：" + msg + "\n");
            }

    }
}
