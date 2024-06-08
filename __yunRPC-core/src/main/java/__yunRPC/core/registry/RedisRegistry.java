package __yunRPC.core.registry;

import __yunRPC.core.config.RegistryConfig;
import __yunRPC.core.model.ServiceMetaInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/08/15:22
 * @Description:
 */
public class RedisRegistry implements Registry{
    @Override
    public void init(RegistryConfig registryConfig) {

    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        return null;
    }
}
