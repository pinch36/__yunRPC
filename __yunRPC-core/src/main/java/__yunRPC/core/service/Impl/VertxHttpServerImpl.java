//package __yunRPC.server.service.Impl;
//
//import __yunRPC.server.handler.vertx.HttpServerHandler;
//import __yunRPC.server.service.HttpServer;
//import io.vertx.core.Vertx;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Author: __yun
// * @Date: 2024/06/06/16:44
// * @Description:
// */
//@Slf4j
//public class VertxHttpServerImpl implements HttpServer {
//    /**
//     * 服务器启动
//     *
//     * @param port
//     */
//    @Override
//    public void doStart(int port) {
//        Vertx vertx = Vertx.vertx();
//        io.vertx.core.http.HttpServer server = vertx.createHttpServer();
//        server.requestHandler(new HttpServerHandler());
//        server.listen(port,result -> {
//            if (result.succeeded()) {
//                log.info("listen port{}",port);
//            }else{
//                log.info("failed to start{}",result.cause());
//            }
//        });
//    }
//}
