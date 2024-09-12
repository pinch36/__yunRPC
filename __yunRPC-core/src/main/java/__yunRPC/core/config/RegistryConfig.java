package __yunRPC.core.config;

import __yunRPC.core.registry.RegistryKeys;
import lombok.Data;
/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/15:14
 * @Description:
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ETCD;
    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";
    private String username;
    private String password = "123456";
    private Long timeout = 10000L;

}
