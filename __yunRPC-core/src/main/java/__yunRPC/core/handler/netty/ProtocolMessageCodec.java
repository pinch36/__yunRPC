package __yunRPC.core.handler.netty;

import __yunRPC.core.RpcApplication;
import __yunRPC.core.factory.SerializerFactory;
import __yunRPC.core.model.protocol.ProtocolMessage;
import __yunRPC.core.model.rpc.RpcRequest;
import __yunRPC.core.serializer.JsonSerializer;
import __yunRPC.core.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/12/7:45
 * @Description:
 */
@Slf4j
public class ProtocolMessageCodec extends ByteToMessageCodec<ProtocolMessage<RpcRequest>> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtocolMessage protocolMessage, ByteBuf byteBuf) throws Exception {
        log.info("编码数据并写入{}",protocolMessage);
        byteBuf.writeByte(protocolMessage.getHeader().getMagic());
        byteBuf.writeByte(protocolMessage.getHeader().getVersion());
        byteBuf.writeByte(protocolMessage.getHeader().getSerializer());
        byteBuf.writeByte(protocolMessage.getHeader().getType());
        byteBuf.writeByte(protocolMessage.getHeader().getStatus());
        byteBuf.writeLong(protocolMessage.getHeader().getRequestId());
        Serializer serializerObj = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        byte[] body = serializerObj.serialize(protocolMessage.getBody());
        byteBuf.writeInt(body.length);
        byteBuf.writeBytes(body);
        log.info("成功写入...");
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("解码...");
        byte magic = byteBuf.readByte();
        byte version = byteBuf.readByte();
        byte serializer = byteBuf.readByte();
        byte type = byteBuf.readByte();
        byte status = byteBuf.readByte();
        long requestId = byteBuf.readLong();
        int bodyLength = byteBuf.readInt();
        byte[] bytes = new byte[bodyLength];
        byteBuf.readBytes(bytes,0,bodyLength);
        //过测试序列化器默认json
        Serializer serializerObj = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        RpcRequest rpcRequest = serializerObj.deSerialize(bytes, RpcRequest.class);
        list.add(rpcRequest);
    }
}

