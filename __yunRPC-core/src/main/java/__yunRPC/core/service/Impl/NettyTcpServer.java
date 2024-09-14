package __yunRPC.core.service.Impl;

import __yunRPC.core.handler.netty.ProtocolMessageCodec;
import __yunRPC.core.handler.netty.RpcHttpServerHandler;
import __yunRPC.core.handler.netty.RpcTcpServerHandler;
import __yunRPC.core.service.TcpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/7:55
 * @Description:
 */
@Slf4j
public class NettyTcpServer implements TcpServer {

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
                    socketChannel.pipeline().addLast("self-codec", new ProtocolMessageCodec());
                    socketChannel.pipeline().addLast("tcp-server", new RpcTcpServerHandler());
                }
            });
            Channel channel = serverBootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("服务器关闭..");
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
