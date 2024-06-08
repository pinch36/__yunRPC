package __yunRPC.core.registry;

import __yunRPC.core.config.RegistryConfig;
import __yunRPC.core.model.ServiceMetaInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/15:18
 * @Description:
 */
public interface Registry {
    /**
     * 初始化，登录启动端口等
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;
    void unRegister(ServiceMetaInfo serviceMetaInfo);
    void destroy();
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);
}
