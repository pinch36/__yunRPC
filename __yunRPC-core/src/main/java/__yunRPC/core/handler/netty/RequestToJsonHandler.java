package __yunRPC.core.handler.netty;

import __yunRPC.core.model.RpcRequest;
import __yunRPC.core.serializer.ClassCodec;
import __yunRPC.core.serializer.JdkSerializer;
import __yunRPC.core.serializer.JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/8:49
 * @Description:
 */
@Slf4j
public class RequestToJsonHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        log.info("请求转换为Json...");
        //转化为json
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = new String(reqContent, StandardCharsets.UTF_8);
        RpcRequest rpcRequest = null;
        Gson gson = JsonSerializer.getGson();
        try {
            rpcRequest = gson.fromJson(strContent,RpcRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        channelHandlerContext.fireChannelRead(rpcRequest);
    }
}
