package com.pbc.mychat.Server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by Alex on 2018/1/19.
 */
public class ChatServerInitialize extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel channel) throws Exception {
        // TODO: 2018/1/19 当有客户端连接服务器时，netty会自动回调这个初始化器的initChannel方法
        System.out.println("有客户端连入"+channel.remoteAddress());
        ChannelPipeline  pipeline = channel.pipeline();// TODO: 2018/1/19 管道
        // TODO: 2018/1/19  管道中发送的数据最终都是0101的格式，无缝流动
        // TODO: 2018/1/19 所以在大数据量大的时候，我们需要将数据分帧
        //1 定长
        //2 适用固定的分隔符
        //3 将消息分为消息头和消息体两部分，在消息头中  用一个数据说明消息体的长度
        //4 或者其他复杂的协议

        pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8912, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder",new StringDecoder());
        pipeline.addLast("encoder",new StringEncoder());
        pipeline.addLast("handler",new ChatServerHandler());
    }
}
