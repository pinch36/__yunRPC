//package __yunRPC.server.handler.vertx;
//
//import __yunRPC.core.model.RpcRequest;
//import __yunRPC.core.model.RpcResponse;
//import __yunRPC.common.serializer.JdkSerializer;
//import __yunRPC.common.serializer.Serializer;
//import __yunRPC.server.registry.LocalRegistry;
//import io.vertx.core.Handler;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.http.HttpServerRequest;
//import io.vertx.core.http.HttpServerResponse;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.lang.reflect.Method;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Author: __yun
// * @Date: 2024/06/07/9:21
// * @Description:
// */
//@Slf4j
//public class HttpServerHandler implements Handler<HttpServerRequest> {
//    @Override
//    public void handle(HttpServerRequest request) {
//        final Serializer serializer = new JdkSerializer();
//        log.info("Received request:{} {}", request.method(), request.uri());
//        request.bodyHandler(body -> {
//            byte[] bytes = body.getBytes();
//            RpcRequest rpcRequest = null;
//            try {
//                rpcRequest = serializer.deSerialize(bytes, RpcRequest.class);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            RpcResponse rpcResponse = new RpcResponse();
//            if (rpcRequest == null) {
//                rpcResponse.setMessage("null");
//                doResponse(request, rpcResponse, serializer);
//                return;
//            }
//            try {
//                Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());
//                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
//                Object invoke = method.invoke(aClass.newInstance(), rpcRequest.getArgs());
//                rpcResponse.setData(invoke);
//                rpcResponse.setDataType(method.getReturnType());
//                rpcResponse.setMessage("OK");
//            } catch (Exception e) {
//                e.printStackTrace();
//                rpcResponse.setMessage(e.getMessage());
//                rpcResponse.setException(e);
//                throw new RuntimeException(e);
//            }
//            doResponse(request, rpcResponse, serializer);
//        });
//    }
//
//    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
//        HttpServerResponse response = request.response().putHeader("content-type", "application/json");
//        try {
//            byte[] serialized = serializer.serialize(rpcResponse);
//            response.end(Buffer.buffer(serialized));
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//}
