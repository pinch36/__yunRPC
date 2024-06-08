package __yunRPC.provider;

import __yunRPC.common.service.UserService;
import __yunRPC.core.RpcApplication;
import __yunRPC.core.registry.LocalRegistry;
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
        LocalRegistry.registry(UserService.class.getName(),UserServiceImpl.class);
        HttpServer httpServer = new NettyHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
