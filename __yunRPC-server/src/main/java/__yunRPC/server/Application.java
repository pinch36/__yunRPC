package __yunRPC.server;

import __yunRPC.server.service.HttpServer;
import __yunRPC.server.service.Impl.NettyHttpServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/21:13
 * @Description:
 */
public class Application {
    public static void main(String[] args) {
        HttpServer httpServer = new NettyHttpServer();
        httpServer.doStart(8090);
    }
}
