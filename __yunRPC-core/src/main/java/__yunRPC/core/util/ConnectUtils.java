package __yunRPC.core.util;

import __yunRPC.core.RpcApplication;
import __yunRPC.core.config.RpcConfig;
import __yunRPC.core.constant.RpcConstant;
import __yunRPC.core.factory.RegistryFactory;
import __yunRPC.core.model.protocol.ProtocolMessage;
import __yunRPC.core.model.rpc.RpcRequest;
import __yunRPC.core.model.rpc.RpcResponse;
import __yunRPC.core.model.service.ServiceMetaInfo;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.serializer.JsonSerializer;
import cn.hutool.core.collection.CollUtil;
import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/12/9:45
 * @Description:
 */
@Slf4j
public class ConnectUtils {
    public static Object doRequest(RpcRequest rpcRequest, String serviceName) {
        Gson gson = JsonSerializer.getGson();
        int port = 8090;
        final Object[] re = new Object[1];
        String jsonStr = gson.toJson(rpcRequest);
        //获取服务地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            throw new RuntimeException("暂无服务地址");
        }
        ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);
        NioEventLoopGroup group = group = new NioEventLoopGroup();
        CountDownLatch latch = new CountDownLatch(1);
        try {
            log.info("发送tcp请求...");
            //发送tcp请求
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.remoteAddress("localhost", 8090);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("codec", new ByteToMessageCodec<ProtocolMessage>() {
                        @Override
                        protected void encode(ChannelHandlerContext channelHandlerContext, ProtocolMessage protocolMessage, ByteBuf byteBuf) throws Exception {
                            byteBuf.writeByte(protocolMessage.getHeader().getMagic());
                            byteBuf.writeByte(protocolMessage.getHeader().getVersion());
                            byteBuf.writeByte(protocolMessage.getHeader().getSerializer());
                            byteBuf.writeByte(protocolMessage.getHeader().getType());
                            byteBuf.writeByte(protocolMessage.getHeader().getStatus());
                            byteBuf.writeLong(protocolMessage.getHeader().getRequestId());
                            //过测试序列化器默认json
                            JsonSerializer jsonSerializer = new JsonSerializer();
                            byte[] body = jsonSerializer.serialize(protocolMessage.getBody());
                            byteBuf.writeInt(body.length);
                            byteBuf.writeBytes(body);
                        }

                        @Override
                        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
                            byte magic = byteBuf.readByte();
                            byte version = byteBuf.readByte();
                            byte serializer = byteBuf.readByte();
                            byte type = byteBuf.readByte();
                            byte status = byteBuf.readByte();
                            long requestId = byteBuf.readLong();
                            int bodyLength = byteBuf.readInt();
                            byte[] bytes = new byte[bodyLength];
                            byteBuf.readBytes(bytes, 0, bodyLength);
                            JsonSerializer jsonSerializer = new JsonSerializer();
                            RpcResponse rpcResponse = jsonSerializer.deSerialize(bytes, RpcResponse.class);
                            list.add(rpcResponse);
                        }
                    });
                    socketChannel.pipeline().addLast("getData", new SimpleChannelInboundHandler<RpcResponse>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
                            Object data = rpcResponse.getData();
                            data = gson.fromJson(gson.toJson(data), rpcResponse.getDataType());
                            re[0] = data;
                            latch.countDown();
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect().sync().channel();
            ProtocolMessage<RpcRequest> protocolMessage = ProtocolMessage.getRpcRequestProtocolMessage(rpcRequest);
            channel.writeAndFlush(protocolMessage);
            latch.await();
            channel.close().sync();
            return re[0];
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
