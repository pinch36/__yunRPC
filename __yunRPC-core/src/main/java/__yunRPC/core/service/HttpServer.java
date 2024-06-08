package __yunRPC.core.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/06/16:42
 * @Description:
 */
public interface HttpServer {
    /**
     * 服务器启动
     *
     * @param port
     */
    void doStart(int port);
}
