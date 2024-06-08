package __yunRPC.server.service.Impl;

import __yunRPC.server.handler.netty.RpcHttpServerHandler;
import __yunRPC.server.handler.netty.RequestToJsonHandler;
import __yunRPC.server.service.HttpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/7:55
 * @Description:
 */
@Slf4j
public class NettyHttpServer implements HttpServer {

        @Override
    public void doStart(int port) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            log.info("服务器启动...");
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    // 请求解码器
                    socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    // 将HTTP消息的多个部分合成一条完整的HTTP消息
                    socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                    // 响应转码器
                    socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                    socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    socketChannel.pipeline().addLast("getRpcRequest",new RequestToJsonHandler());
                    socketChannel.pipeline().addLast("http-server", new RpcHttpServerHandler());
                }
            });
            Channel channel = serverBootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
