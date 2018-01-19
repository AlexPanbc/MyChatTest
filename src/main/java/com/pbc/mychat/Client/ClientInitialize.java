package com.pbc.mychat.Client;

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
public class ClientInitialize extends ChannelInitializer<SocketChannel> {


    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();// TODO: 2018/1/19 管道
        pipeline.addLast("framer",new DelimiterBasedFrameDecoder(8912,Delimiters.lineDelimiter()));
        pipeline.addLast("decoder",new StringDecoder());
        pipeline.addLast("encoder",new StringEncoder());
        pipeline.addLast("handler",new ChatClientHandler());

    }
}
