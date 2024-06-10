package __yunRPC.core.handler.netty;

import __yunRPC.core.model.RpcRequest;
import __yunRPC.core.model.RpcResponse;
import __yunRPC.core.registry.LocalRegistry;
import __yunRPC.core.serializer.JsonSerializer;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/16:52
 * @Description:
 */
@Slf4j
public class RpcHttpServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        //调用目标方法,得到结果
        RpcResponse rpcResponse = RpcResponse.builder().build();
        Gson gson = JsonSerializer.getGson();
        try {
            String serviceName = rpcRequest.getServiceName();
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Class<?> aClass = LocalRegistry.get(serviceName);
            Method method = aClass.getMethod(methodName,parameterTypes);
            Object[] args = rpcRequest.getArgs();
            for (int i = 0; i < args.length; i++) {
                args[i] = gson.fromJson(gson.toJson(args[i]),parameterTypes[i]);
            }
            Object invoke = method.invoke(aClass.getConstructor().newInstance(), args);
            log.info("请求返回数据{}", invoke);
            //返回
            rpcResponse.setMessage("Ok");
            rpcResponse.setData(invoke);
            rpcResponse.setDataType(method.getReturnType());
        } catch (NoSuchMethodException e) {
            rpcResponse.setException(e);
            rpcResponse.setMessage(e.getMessage());
            throw new RuntimeException(e);
        }
        //返回结果,发送请求
        doResponse(channelHandlerContext, gson, rpcResponse);
    }

    private static void doResponse(ChannelHandlerContext channelHandlerContext, Gson gson, RpcResponse rpcResponse) {
        String json = gson.toJson(rpcResponse);
        ByteBuf buffer = channelHandlerContext.alloc().buffer();
        buffer.writeBytes(json.getBytes(CharsetUtil.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, buffer);
        response.headers().set("Content-Type", "application/json;charset=UTF-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        channelHandlerContext.writeAndFlush(response);
    }
}
