package __yunRPC.core;

import __yunRPC.core.config.RegistryConfig;
import __yunRPC.core.config.RpcConfig;
import __yunRPC.core.constant.RpcConstant;
import __yunRPC.core.factory.RegistryFactory;
import __yunRPC.core.registry.Registry;
import __yunRPC.core.service.Impl.NettyHttpServer;
import __yunRPC.core.service.HttpServer;
import __yunRPC.core.util.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/21:13
 * @Description:
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",newRpcConfig.toString());
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init,config = {}",registryConfig);
        //利用Shutdown Hook,jvm退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }
    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }
    public static RpcConfig getRpcConfig(){
        if (rpcConfig == null){
            synchronized (RpcConfig.class){
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
    public static void main(String[] args) {
        HttpServer httpServer = new NettyHttpServer();
        httpServer.doStart(8090);
    }
}
