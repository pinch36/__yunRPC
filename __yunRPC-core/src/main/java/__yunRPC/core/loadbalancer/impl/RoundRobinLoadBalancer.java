package __yunRPC.core.loadbalancer.impl;

import __yunRPC.core.loadbalancer.LoadBalancer;
import __yunRPC.core.model.service.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/09/13/7:03
 * @Description:
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos) {
        if (serviceMetaInfos.isEmpty()) {
            return null;
        }
        return serviceMetaInfos.get(currentIndex.getAndIncrement() % serviceMetaInfos.size());
    }
}
