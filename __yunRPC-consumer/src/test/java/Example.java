import __yunRPC.core.config.RpcConfig;
import __yunRPC.core.util.ConfigUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/9:42
 * @Description:
 */
public class Example {
    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}
