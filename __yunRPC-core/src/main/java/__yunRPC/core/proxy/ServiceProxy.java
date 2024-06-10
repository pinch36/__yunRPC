package __yunRPC.core.proxy;

import __yunRPC.core.RpcApplication;
import __yunRPC.core.config.RpcConfig;
import __yunRPC.core.constant.RpcConstant;
import __yunRPC.core.factory.RegistryFactory;
import __yunRPC.core.model.RpcRequest;
import __yunRPC.core.model.RpcResponse;
import __yunRPC.core.model.ServiceMetaInfo;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.serializer.JsonSerializer;
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
public class ServiceProxy implements InvocationHandler {
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
        Gson gson = JsonSerializer.getGson();
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
        //发送请求
        try (HttpResponse response = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                .body(jsonStr)
                .execute()) {
            String result = response.body();
            RpcResponse rpcResponse = gson.fromJson(result, RpcResponse.class);
            Object data = rpcResponse.getData();
            data = gson.fromJson(gson.toJson(data),rpcResponse.getDataType());
            return data;
        }
    }
}
