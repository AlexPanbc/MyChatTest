package com.pbc.ws.netty;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 用来区分用户的请求是 请求的http聊天页面  还是发送的websocket请求
 * Created by Alex on 2018/1/21.
 */
public class HttpRequesstHanler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String chatUri;//websocket的请求地址
    private static File indexFile;

    public HttpRequesstHanler(String chatUri) {
        this.chatUri = chatUri;
    }

    static {
        URL location = HttpRequesstHanler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            indexFile = new File(path);
            System.out.println(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel cc = ctx.channel();
        System.out.println(cc.id() + "拜拜...");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        System.out.println(request.getUri());
        if (chatUri.equalsIgnoreCase(request.getUri())) {
            //用户请求的是websocket地址  此时不做处理，转给管道的下一个处理环节
            ctx.fireChannelRead(request.retain());
        } else {
            //读取聊天页面，将页面的内容写回给客户端浏览器
            if (HttpHeaders.is100ContinueExpected(request)) {
                FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(resp);
            }
            //读取默认页面
            RandomAccessFile file = new RandomAccessFile(indexFile, "r");
            HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);//用的是DefaultHttpResponse不是DefaultFullHttpResponse
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=utf-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ctx.write(new ChunkedNioFile(file.getChannel()));
            System.out.println(file.length());
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
            file.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
