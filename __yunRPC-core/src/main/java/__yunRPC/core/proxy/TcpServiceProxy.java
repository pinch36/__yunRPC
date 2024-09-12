package __yunRPC.core.proxy;

import __yunRPC.core.RpcApplication;
import __yunRPC.core.config.RpcConfig;
import __yunRPC.core.constant.RpcConstant;
import __yunRPC.core.factory.RegistryFactory;
import __yunRPC.core.model.rpc.RpcRequest;
import __yunRPC.core.model.rpc.RpcResponse;
import __yunRPC.core.model.service.ServiceMetaInfo;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.serializer.JsonSerializer;
import __yunRPC.core.util.ConnectUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/20:52
 * @Description:
 */
public class TcpServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //生成请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .serviceName(serviceName)
                .args(args)
                .build();
        return ConnectUtils.doRequest(rpcRequest, serviceName);
    }
}
