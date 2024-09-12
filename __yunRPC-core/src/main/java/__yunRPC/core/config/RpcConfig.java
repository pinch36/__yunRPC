package __yunRPC.core.config;

import __yunRPC.core.loadbalancer.LoadBalancerKeys;
import __yunRPC.core.serializer.SerializerKeys;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/9:21
 * @Description:
 */
@Data
public class RpcConfig {
    private String name = "__yunRpc";
    private String version = "1.0";
    private String serverHost = "localhost";
    private Integer serverPort = 8090;
    private String serializer = SerializerKeys.JSON;
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
    private boolean mock = false;
    private RegistryConfig registryConfig = new RegistryConfig();
}
