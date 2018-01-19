package com.pbc.mychat.Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by Alex on 2018/1/19.
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    //所有客户端
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    // TODO: 2018/1/19 当有客户端连接时 执行 
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        Channel incoming = ctx.channel();// TODO: 2018/1/19 获得客户端通道
        channels.add(incoming);//新加入的客户端加入到队列去
        for (Channel ch : channels)
            if (ch != incoming)
                ch.writeAndFlush("欢迎：" + incoming.remoteAddress() + "进入聊天室"+ "\n");

//        super.handlerAdded(ctx);
    }

    // TODO: 2018/1/19 当有客户端断开时 执行
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();// TODO: 2018/1/19 获得客户端通道
        channels.remove(incoming);//新加入的客户端加入到队列去
        for (Channel ch : channels)
            if (ch != incoming)
                ch.writeAndFlush("再见：" + incoming.remoteAddress() + "离开了聊天室"+ "\n");

        // super.handlerRemoved(ctx);
    }

    // TODO: 2018/1/19 当监听到客户端活动时 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[" + ctx.channel().remoteAddress() + "]:在线中..."+ "\n");
//        super.channelActive(ctx);
    }

    // TODO: 2018/1/19 客户端没活动 离线了做的事 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("[" + ctx.channel().remoteAddress() + "]:拜拜了..."+ "\n");
//        super.channelInactive(ctx);
    }

    // TODO: 2018/1/19 出错时 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    // TODO: 2018/1/19 当有客户端消息写入的时
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel client = ctx.channel();
        for (Channel ch : channels)
            if (ch != client) {
                ch.writeAndFlush("用户：" + client.remoteAddress() + "说：" + msg + "\n");
            } else {
                ch.writeAndFlush("我说：" + msg + "\n");
            }

    }
}
