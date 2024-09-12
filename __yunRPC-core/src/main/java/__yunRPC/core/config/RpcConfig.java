package __yunRPC.core.config;

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
    private String serializer = "json";
    private boolean mock = false;
    private RegistryConfig registryConfig = new RegistryConfig();
}
