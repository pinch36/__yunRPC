package __yunRPC.core.model.protocol;

import __yunRPC.core.constant.ProtocolConstant;
import __yunRPC.core.model.rpc.RpcRequest;
import __yunRPC.core.model.rpc.RpcResponse;
import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/12/7:38
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    private Header header;
    private T body;
    @Data
    public static class Header{
        private byte magic;
        private byte version;
        private byte serializer;
        private byte type;
        private byte status;
        private long requestId;
        private int bodyLength;
    }
    public static ProtocolMessage<RpcRequest> getRpcRequestProtocolMessage(RpcRequest rpcRequest) {
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<RpcRequest>();
        Header header = new Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        //json
        header.setSerializer((byte) 0);
        //request 0
        header.setType((byte) 0);
        header.setStatus((byte) 0);
        header.setRequestId(IdUtil.getSnowflakeNextId());
        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);
        return protocolMessage;
    }
    public static ProtocolMessage<RpcResponse> getRpcResponseProtocolMessage(RpcResponse rpcResponse) {
        ProtocolMessage<RpcResponse> protocolMessage = new ProtocolMessage<RpcResponse>();
        Header header = new Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        //json
        header.setSerializer((byte) 0);
        //request 0
        header.setType((byte) 0);
        header.setStatus((byte) 0);
        header.setRequestId(IdUtil.getSnowflakeNextId());
        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcResponse);
        return protocolMessage;
    }
}
