package __yunRPC.core.registry;

import __yunRPC.core.model.ServiceMetaInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/06/10/9:24
 * @Description:
 */
public class RegistryServiceCache {
    Map<String, List<ServiceMetaInfo>> serviceCache = new HashMap<>();

    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        serviceCache.put(serviceKey, newServiceCache);
    }

    List<ServiceMetaInfo> readCache(String serviceKey) {
        if (serviceCache.containsKey(serviceKey)){
            return serviceCache.get(serviceKey);
        }
        return null;
    }

    void clearCache() {
        serviceCache = null;
    }
}