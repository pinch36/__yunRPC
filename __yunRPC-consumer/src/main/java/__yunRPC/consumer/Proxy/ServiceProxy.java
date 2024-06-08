package __yunRPC.consumer.Proxy;

import __yunRPC.common.model.RpcRequest;
import __yunRPC.common.model.RpcResponse;
import __yunRPC.common.serializer.ClassCodec;
import __yunRPC.common.serializer.JdkSerializer;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/20:52
 * @Description:
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        JdkSerializer serializer = new JdkSerializer();
        RpcRequest rpcRequest = RpcRequest.builder()
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .serviceName(method.getDeclaringClass().getName())
                .args(args)
                .build();
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
        String jsonStr = gson.toJson(rpcRequest);
//        String jsonStr = JSONUtil.toJsonStr(build);

        //            byte[] bodyBytes = serializer.serialize(rpcRequest);
        try (HttpResponse response = HttpRequest.post("http://localhost:8090")
//                    .body(bodyBytes)
                .body(jsonStr)
                .execute()) {
            String result = response.body();
//            String json = new String(bytes, StandardCharsets.UTF_8);
//                RpcResponse rpcResponse = serializer.deSerialize(result, RpcResponse.class);
            RpcResponse rpcResponse = gson.fromJson(result, RpcResponse.class);
            Object data = rpcResponse.getData();
            data = gson.fromJson(gson.toJson(data),rpcResponse.getDataType());
            return data;
        }
    }
}
