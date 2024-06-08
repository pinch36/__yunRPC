package __yunRPC.provider;

import __yunRPC.common.service.UserService;
import __yunRPC.core.RpcApplication;
import __yunRPC.core.config.RegistryConfig;
import __yunRPC.core.config.RpcConfig;
import __yunRPC.core.constant.RpcConstant;
import __yunRPC.core.factory.RegistryFactory;
import __yunRPC.core.model.ServiceMetaInfo;
import __yunRPC.core.registry.LocalRegistry;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.service.HttpServer;
import __yunRPC.core.service.Impl.NettyHttpServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/16:38
 * @Description:
 */
public class Application {
    public static void main(String[] args) {
        RpcApplication.init();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        String serviceName = UserService.class.getName();
        LocalRegistry.registry(serviceName,UserServiceImpl.class);
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpServer httpServer = new NettyHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
